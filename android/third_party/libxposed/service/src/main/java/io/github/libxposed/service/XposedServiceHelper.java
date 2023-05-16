package io.github.libxposed.service;

import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public final class XposedServiceHelper {

    /**
     * Callback interface for Xposed service.
     */
    public interface OnServiceListener {
        /**
         * Callback when the service is connected.<br/>
         * This method could be called multiple times if multiple Xposed frameworks exist.
         *
         * @param service Service instance
         */
        void onServiceBind(@NonNull XposedService service);

        /**
         * Callback when the service is dead.
         */
        void onServiceDied(@NonNull XposedService service);
    }

    private static final String TAG = "XposedServiceHelper";
    private static final Set<XposedService> mCache = new HashSet<>();
    private static OnServiceListener mListener = null;

    static void onBinderReceived(IBinder binder) {
        if (binder == null) return;
        synchronized (mCache) {
            try {
                var service = new XposedService(IXposedService.Stub.asInterface(binder));
                if (mListener == null) {
                    mCache.add(service);
                } else {
                    binder.linkToDeath(() -> mListener.onServiceDied(service), 0);
                    mListener.onServiceBind(service);
                }
            } catch (Throwable t) {
                Log.e(TAG, "onBinderReceived", t);
            }
        }
    }

    /**
     * Register a ServiceListener to receive service binders from Xposed frameworks.<br/>
     * This method should only be called once.
     *
     * @param listener Listener to register
     */
    public static void registerListener(OnServiceListener listener) {
        synchronized (mCache) {
            mListener = listener;
            if (!mCache.isEmpty()) {
                for (var it = mCache.iterator(); it.hasNext(); ) {
                    try {
                        var service = it.next();
                        service.getRaw().asBinder().linkToDeath(() -> mListener.onServiceDied(service), 0);
                        mListener.onServiceBind(service);
                    } catch (Throwable t) {
                        Log.e(TAG, "registerListener", t);
                        it.remove();
                    }
                }
                mCache.clear();
            }
        }
    }
}
