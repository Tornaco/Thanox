package github.tornaco.android.thanox.magisk.bridge.proxy.am;

import android.app.AppOpsManager;
import android.os.IBinder;
import android.os.IInterface;

import com.android.internal.app.IAppOpsService;
import com.elvishew.xlog.XLog;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanox.magisk.bridge.proxy.BinderProxy;
import github.tornaco.android.thanox.magisk.bridge.proxy.ProxyProvider;
import util.ExceptionTransformedInvocationHandler;

public class AppOpsManagerProxyProvider implements ProxyProvider, ExceptionTransformedInvocationHandler {
    private static final boolean DEBUG_OPS = false;

    @Override
    public IBinder provide(IBinder legacyBinder) {
        return proxyAppOpsManager(legacyBinder);
    }

    @Override
    public boolean isForService(String name) {
        return name.equals("appops");
    }

    private IBinder proxyAppOpsManager(IBinder original) {
        return new BinderProxy(original, new BinderProxy.InvocationHandler() {
            @Override
            public IInterface onQueryLocalInterface(String descriptor, IInterface iInterface) {
                if (IAppOpsService.class.getName().equals(descriptor)) {
                    IAppOpsService am = IAppOpsService.Stub.asInterface(original);
                    return (IInterface) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                            new Class[]{IAppOpsService.class},
                            (instance, method, args) -> {
                                if (DEBUG_OPS) {
                                    XLog.d("IAppOpsService %s %s", method.getName(), Arrays.toString(args));
                                }

                                IThanos thanos = ThanosManagerNative.getDefault();
                                if (thanos == null) {
                                    return tryInvoke(am, method, args);
                                }

                                if ("checkOperation".equals(method.getName()) || "noteOperation".equals(method.getName())) {
                                    int uid = (int) args[1];
                                    if (PkgUtils.isSystemOrPhoneOrShell(uid)) {
                                        return tryInvoke(am, method, args);
                                    }
                                    github.tornaco.android.thanos.core.secure.ops.IAppOpsService ops = ThanosManagerNative.getDefault()
                                            .getAppOpsService();
                                    boolean opsEnabled = ops.isOpsEnabled();
                                    if (!opsEnabled) {
                                        return tryInvoke(am, method, args);
                                    }
                                    int code = (int) args[0];
                                    String pkgName = (String) args[2];
                                    int mode = ops.checkOperation(code, uid, pkgName);
                                    if (mode == AppOpsManager.MODE_IGNORED) {
                                        return AppOpsManager.MODE_IGNORED;
                                    }
                                }

                                if (("startOperation".equals(method.getName()))) {
                                    github.tornaco.android.thanos.core.secure.ops.IAppOpsService ops = ThanosManagerNative.getDefault()
                                            .getAppOpsService();
                                    boolean opsEnabled = ops.isOpsEnabled();
                                    if (!opsEnabled) {
                                        // Report.
                                        ops.onStartOp(
                                                (IBinder) args[0],
                                                (int) args[1],
                                                (int) args[2],
                                                (String) args[3]);
                                        return tryInvoke(am, method, args);
                                    }

                                    int code = (int) args[1];
                                    int uid = (int) args[2];

                                    if (PkgUtils.isSystemOrPhoneOrShell(uid)) {
                                        return tryInvoke(am, method, args);
                                    }

                                    String pkgName = (String) args[3];
                                    int mode = ops.checkOperation(code, uid, pkgName);
                                    if (mode == AppOpsManager.MODE_IGNORED) {
                                        return AppOpsManager.MODE_IGNORED;
                                    } else {
                                        // Report.
                                        ops.onStartOp(
                                                (IBinder) args[0],
                                                (int) args[1],
                                                (int) args[2],
                                                (String) args[3]);
                                    }
                                }

                                if (("finishOperation".equals(method.getName()))) {
                                    github.tornaco.android.thanos.core.secure.ops.IAppOpsService ops = ThanosManagerNative.getDefault()
                                            .getAppOpsService();
                                    // Report.
                                    ops.onFinishOp(
                                            (IBinder) args[0],
                                            (int) args[1],
                                            (int) args[2],
                                            (String) args[3]);
                                }

                                return tryInvoke(am, method, args);
                            });
                }
                return iInterface;
            }
        });
    }
}
