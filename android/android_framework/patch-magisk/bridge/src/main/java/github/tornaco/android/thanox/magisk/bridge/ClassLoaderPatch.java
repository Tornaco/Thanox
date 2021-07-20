package github.tornaco.android.thanox.magisk.bridge;

import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.util.Log;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.core.util.AbstractSafeR;
import util.XposedHelpers;

import static github.tornaco.android.thanos.core.util.AppUtils.currentApplicationInfo;
import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

public class ClassLoaderPatch {
    private static final boolean LOGV = false;

    private final ClassLoader system;
    private final ClassLoader boot;

    private ClassLoaderPatch() {
        this.system = ClassLoader.getSystemClassLoader();
        this.boot = system.getParent();
    }

    public static void install() {
        try {
            new ClassLoaderPatch().init0();
        } catch (Throwable e) {
            logging("ClassLoaderPatchInstaller init error: " + Log.getStackTraceString(e));
        }
    }

    private void init0() throws Exception {
        HookedSystemClassLoader wrappedSystem = new HookedSystemClassLoader();
        Class<?> systemClassLoaderClass = Class.forName("java.lang.ClassLoader$SystemClassLoader");
        XposedHelpers.setStaticObjectField(systemClassLoaderClass, "loader", wrappedSystem);
        logging("ClassLoaderPatchInstaller installed: " + wrappedSystem);
    }

    class HookedSystemClassLoader extends ClassLoader {
        public HookedSystemClassLoader() {
            super(system);
            ClassLoader wrappedBoot = new HookedBootClassLoader();
            XposedHelpers.setObjectField(this, "parent", wrappedBoot);
            logging("parent now is: " + getParent());
        }

        @Override
        protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
            if (LOGV) logging("HookedSystemClassLoader#loadClass: " + className);
            return super.loadClass(className, resolve);
        }
    }

    class HookedBootClassLoader extends ClassLoader {
        private boolean isInstalled = false;

        public HookedBootClassLoader() {
            super(boot);
        }

        @Override
        protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
            if (LOGV) logging("HookedBootClassLoader#loadClass: " + className);
            if (isInstalled) {
                return super.loadClass(className, resolve);
            }

            if (className.startsWith("com.android.server")) {
                int uid = Binder.getCallingUid();
                logging("onLoadSystemServer. calling uid: " + uid);
                if (uid == 1000) {
                    isInstalled = true;
                    onSystemServerProcess();
                }
            }

            if (className.equals("android.app.Application")) {
                ApplicationInfo currentApp = currentApplicationInfo();
                logging("HookedBootClassLoader#currentApp: " + currentApp);
                if (currentApp != null) {
                    isInstalled = true;
                    onAppProcess(currentApp);
                }
            }

            return super.loadClass(className, resolve);
        }

        private void onSystemServerProcess() {
            new AbstractSafeR() {
                @Override
                public void runSafety() {
                    ThanoxHookInstance.get().install(true);
                }
            }.setName("onSystemServerProcess").run();
        }

        private void onAppProcess(ApplicationInfo currentApp) {
            new AbstractSafeR() {
                @Override
                public void runSafety() {
                    if (currentApp == null) {
                        return;
                    }
                    String pkgName = currentApp.packageName;
                    if (pkgName == null) {
                        return;
                    }

                    // 2021-07-08 10:47:49.756 190-190/? E/SELinux: avc:  denied  { find } for pid=5258 uid=10149 name=appops scontext=u:r:permissioncontroller_app:s0:c149,c256,c512,c768 tcontext=u:object_r:appops_service:s0 tclass=service_manager permissive=0

                    ThanoxBridge.nativeInstallAppHook();

                    if (!pkgName.contains(BuildProp.THANOS_APP_PKG_NAME_PREFIX)) {
                        SystemServiceHookInstaller.installIServiceManagerHook();
                        SystemServiceHookInstaller.installServiceManagerCacheHook();
                        SystemServiceHookInstaller.installActivityManager();
                    }
                }
            }.setName("onAppProcess").run();
        }
    }
}
