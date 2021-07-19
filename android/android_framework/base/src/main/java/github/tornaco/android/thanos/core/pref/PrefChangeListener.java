package github.tornaco.android.thanos.core.pref;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import lombok.Getter;

public class PrefChangeListener implements IPrefChangeListener {

    private final Handler h = new Handler(Looper.getMainLooper());
    @Getter
    private final IPrefChangeListener listener = new IPrefChangeListener.Stub() {
        @Override
        public void onPrefChanged(String key) {
            h.post(() -> PrefChangeListener.this.onPrefChanged(key));
        }
    };

    @Override
    public void onPrefChanged(String key) {
        // Noop.
    }

    @Override
    public IBinder asBinder() {
        return listener.asBinder();
    }
}
