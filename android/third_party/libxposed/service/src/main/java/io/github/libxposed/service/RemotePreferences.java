package io.github.libxposed.service;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unchecked")
public final class RemotePreferences implements SharedPreferences {

    private static final String TAG = "RemotePreferences";
    private static final Object CONTENT = new Object();
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private final XposedService mService;
    private final String mGroup;
    private final Lock mLock = new ReentrantLock();
    private final Map<String, Object> mMap = new ConcurrentHashMap<>();
    private final Map<OnSharedPreferenceChangeListener, Object> mListeners = Collections.synchronizedMap(new WeakHashMap<>());

    private volatile boolean isDeleted = false;

    private RemotePreferences(XposedService service, String group) {
        this.mService = service;
        this.mGroup = group;
    }

    @Nullable
    static RemotePreferences newInstance(XposedService service, String group) throws RemoteException {
        Bundle output = service.getRaw().requestRemotePreferences(group);
        if (output == null) return null;
        var prefs = new RemotePreferences(service, group);
        if (output.containsKey("map")) {
            prefs.mMap.putAll((Map<String, Object>) output.getSerializable("map"));
        }
        return prefs;
    }

    void setDeleted() {
        this.isDeleted = true;
    }

    @Override
    public Map<String, ?> getAll() {
        return new TreeMap<>(mMap);
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        return (String) mMap.getOrDefault(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return (Set<String>) mMap.getOrDefault(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        var v = (Integer) mMap.getOrDefault(key, defValue);
        assert v != null;
        return v;
    }

    @Override
    public long getLong(String key, long defValue) {
        var v = (Long) mMap.getOrDefault(key, defValue);
        assert v != null;
        return v;
    }

    @Override
    public float getFloat(String key, float defValue) {
        var v = (Float) mMap.getOrDefault(key, defValue);
        assert v != null;
        return v;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        var v = (Boolean) mMap.getOrDefault(key, defValue);
        assert v != null;
        return v;
    }

    @Override
    public boolean contains(String key) {
        return mMap.containsKey(key);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mListeners.put(listener, CONTENT);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    public class Editor implements SharedPreferences.Editor {

        private final HashSet<String> mDelete = new HashSet<>();
        private final HashMap<String, Object> mPut = new HashMap<>();

        private void put(String key, @NonNull Object value) {
            mDelete.remove(key);
            mPut.put(key, value);
        }

        @Override
        public SharedPreferences.Editor putString(String key, @Nullable String value) {
            if (value == null) remove(key);
            else put(key, value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, @Nullable Set<String> values) {
            if (values == null) remove(key);
            else put(key, values);
            return this;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            put(key, value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            put(key, value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            put(key, value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            put(key, value);
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            mDelete.add(key);
            mPut.remove(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            mDelete.clear();
            mPut.clear();
            return this;
        }

        private void doUpdate(boolean throwing) {
            mService.deletionLock.readLock().lock();
            try {
                if (isDeleted) {
                    throw new IllegalStateException("This preferences group has been deleted");
                }
                mDelete.forEach(mMap::remove);
                mMap.putAll(mPut);
                List<String> changes = new ArrayList<>(mDelete.size() + mMap.size());
                changes.addAll(mDelete);
                changes.addAll(mMap.keySet());
                for (var key : changes) {
                    mListeners.keySet().forEach(listener -> listener.onSharedPreferenceChanged(RemotePreferences.this, key));
                }

                var bundle = new Bundle();
                bundle.putSerializable("delete", mDelete);
                bundle.putSerializable("put", mPut);
                try {
                    mService.getRaw().updateRemotePreferences(mGroup, bundle);
                } catch (RemoteException e) {
                    if (throwing) {
                        throw new RuntimeException(e);
                    } else {
                        Log.e(TAG, "Failed to update remote preferences", e);
                    }
                }
            } finally {
                mService.deletionLock.readLock().unlock();
            }
        }

        @Override
        public boolean commit() {
            if (!mLock.tryLock()) return false;
            try {
                doUpdate(true);
                return true;
            } finally {
                mLock.unlock();
            }
        }

        @Override
        public void apply() {
            HANDLER.post(() -> doUpdate(false));
        }
    }
}
