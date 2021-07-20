package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.text.TextUtils;

import com.android.internal.telephony.ITelephony;
import com.elvishew.xlog.XLog;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.field.Fields;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

public class TelephonyManagerProxyProvider implements ProxyProvider, ExceptionTransformedInvocationHandler {
    @Override
    public IBinder provide(IBinder legacyBinder) {
        return proxyTelephonyManager(legacyBinder);
    }

    @Override
    public boolean isForService(String name) {
        return name.equals(Context.TELEPHONY_SERVICE);
    }

    private IBinder proxyTelephonyManager(IBinder original) {
        return new BinderProxy(original, new BinderProxy.InvocationHandler() {
            @Override
            public IInterface onQueryLocalInterface(String descriptor, IInterface iInterface) {
                if (ITelephony.class.getName().equals(descriptor)) {
                    ITelephony iTelephony = ITelephony.Stub.asInterface(original);
                    return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                            new Class[]{ITelephony.class},
                            (instance, method, args) -> {
                                logging("ITelephony %s %s", method.getName(), Arrays.toString(args));

                                // getDeviceId
                                if ("getDeviceId".equals(method.getName())
                                        || "getDeviceIdWithFeature".equals(method.getName())) {
                                    return handleGetDeviceIdWithFeature(iTelephony, instance, method, args);
                                }

                                if ("getLine1NumberForDisplay".equals(method.getName())) {
                                    return handleGetLine1NumberForDisplay(iTelephony, instance, method, args);
                                }

                                if ("getImeiForSlot".equals(method.getName())) {
                                    return handleGetImeiForSlot(iTelephony, instance, method, args);
                                }

                                return tryInvoke(iTelephony, method, args);
                            });
                }
                return iInterface;
            }
        });
    }

    // String getDeviceId(String callingPackage) throws RemoteException;
    // String getDeviceIdWithFeature(String callingPackage, String callingFeatureId) throws RemoteException;
    private Object handleGetDeviceIdWithFeature(ITelephony iTelephony, Object proxy, Method method, Object[] args) throws Throwable {
        String callerPkg = (String) args[0];
        if (callerPkg == null) {
            return tryInvoke(iTelephony, method, args);
        }
        IThanos thanos = ThanosManagerNative.getDefault();
        if (thanos == null) {
            return tryInvoke(iTelephony, method, args);
        }
        IPrivacyManager priv = thanos.getPrivacyManager();
        if (priv == null) {
            return tryInvoke(iTelephony, method, args);
        }
        if (!priv.isPrivacyEnabled()) {
            return tryInvoke(iTelephony, method, args);
        }

        boolean selected = priv.isPackageFieldsProfileSelected(callerPkg);
        if (!selected) {
            return tryInvoke(iTelephony, method, args);
        }

        // Skip if not set.
        Fields f = priv
                .getSelectedFieldsProfileForPackage(callerPkg, PrivacyManager.PrivacyOp.OP_DEVICE_ID);
        if (f == null || TextUtils.isEmpty(f.getDeviceId())) {
            return tryInvoke(iTelephony, method, args);
        }
        XLog.w("getDeviceIdWithFeature: %s, using value: %s", callerPkg, f.getDeviceId());
        return f.getDeviceId();
    }

    // public String getLine1NumberForDisplay(int subId, String callingPackage, String callingFeatureId) throws RemoteException {
    private Object handleGetLine1NumberForDisplay(ITelephony iTelephony, Object proxy, Method method, Object[] args) throws Throwable {
        String callPackageName = (String) args[1];

        if (callPackageName == null) {
            return tryInvoke(iTelephony, method, args);
        }

        IThanos thanos = ThanosManagerNative.getDefault();
        if (thanos == null) {
            return tryInvoke(iTelephony, method, args);
        }
        IPrivacyManager priv = thanos.getPrivacyManager();
        if (priv == null) {
            return tryInvoke(iTelephony, method, args);
        }
        if (!priv.isPrivacyEnabled()) {
            return tryInvoke(iTelephony, method, args);
        }

        boolean selected = priv.isPackageFieldsProfileSelected(callPackageName);
        if (!selected) {
            return tryInvoke(iTelephony, method, args);
        }

        // Skip if not set.
        Fields f = priv
                .getSelectedFieldsProfileForPackage(callPackageName, PrivacyManager.PrivacyOp.OP_LINE1NUM);
        if (f == null || TextUtils.isEmpty(f.getLine1Number())) {
            return tryInvoke(iTelephony, method, args);
        }
        XLog.w("getLine1NumberForDisplay: %s, using value: %s", callPackageName, f.getLine1Number());
        return f.getLine1Number();
    }

    // public String getImeiForSlot(int slotIndex, String callingPackage, String callingFeatureId) throws RemoteException
    private Object handleGetImeiForSlot(ITelephony iTelephony, Object proxy, Method method, Object[] args) throws Throwable {
        String callPackageName = (String) args[1];
        if (callPackageName == null) {
            return tryInvoke(iTelephony, method, args);
        }

        IThanos thanos = ThanosManagerNative.getDefault();
        if (thanos == null) {
            return tryInvoke(iTelephony, method, args);
        }
        IPrivacyManager priv = thanos.getPrivacyManager();
        if (priv == null) {
            return tryInvoke(iTelephony, method, args);
        }
        if (!priv.isPrivacyEnabled()) {
            return tryInvoke(iTelephony, method, args);
        }

        boolean enabledUid = priv.isPackageFieldsProfileSelected(callPackageName);
        if (!enabledUid) {
            return tryInvoke(iTelephony, method, args);
        }

        // Skip if not set.
        Fields f = priv
                .getSelectedFieldsProfileForPackage(callPackageName, PrivacyManager.PrivacyOp.OP_IMEI);
        if (f == null) {
            return tryInvoke(iTelephony, method, args);
        }
        int slotIndex = (int) args[0];
        String res = f.getImei(slotIndex);
        if (TextUtils.isEmpty(res)) {
            return tryInvoke(iTelephony, method, args);
        }
        XLog.w("getImei: %s, index: %s, using value: %s", callPackageName, slotIndex, res);
        return res;
    }
}
