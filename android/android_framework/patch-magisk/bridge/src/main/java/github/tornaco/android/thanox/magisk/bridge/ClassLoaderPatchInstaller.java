package github.tornaco.android.thanox.magisk.bridge;

import static github.tornaco.android.thanos.core.util.AppUtils.currentApplicationInfo;

import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.util.Log;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.util.AbstractSafeR;
import util.XposedHelpers;

public class ClassLoaderPatchInstaller {
    private static final boolean LOGV = false;

    private final ClassLoader system;
    private final ClassLoader boot;

    private final ProcessHandler processHandler;

    private ClassLoaderPatchInstaller(ProcessHandler processHandler) {
        this.system = ClassLoader.getSystemClassLoader();
        this.boot = system.getParent();
        this.processHandler = processHandler;
    }

    public static void install(ProcessHandler processHandler) {
        try {
            XLog.i("ClassLoaderPatchInstaller install");
            new ClassLoaderPatchInstaller(processHandler).init0();
        } catch (Throwable e) {
            XLog.d("ClassLoaderPatchInstaller init error: " + Log.getStackTraceString(e));
        }
    }

    private void init0() throws Exception {
        HookedSystemClassLoader wrappedSystem = new HookedSystemClassLoader();
        Class<?> systemClassLoaderClass = Class.forName("java.lang.ClassLoader$SystemClassLoader");
        XposedHelpers.setStaticObjectField(systemClassLoaderClass, "loader", wrappedSystem);
        XLog.d("ClassLoaderPatchInstaller installed: " + wrappedSystem);
    }

    class HookedSystemClassLoader extends ClassLoader {
        public HookedSystemClassLoader() {
            super(system);
            ClassLoader wrappedBoot = new HookedBootClassLoader();
            XposedHelpers.setObjectField(this, "parent", wrappedBoot);
            XLog.d("parent now is: " + getParent());
        }

        @Override
        protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
            if (LOGV) XLog.d("HookedSystemClassLoader#loadClass: " + className);
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
            if (LOGV) XLog.d("HookedBootClassLoader#loadClass: " + className);
            if (isInstalled) {
                return super.loadClass(className, resolve);
            }

            if (className.startsWith("com.android.server")) {
                int uid = Binder.getCallingUid();
                XLog.d("onLoadSystemServer. calling uid: " + uid);
                if (uid == 1000) {
                    isInstalled = true;
                    onSystemServerProcess();
                }
            }

            if (className.equals("android.app.Application")) {
                ApplicationInfo currentApp = currentApplicationInfo();
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
                    processHandler.onStartSystemServer();
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
                    processHandler.onStartApplication(currentApp);
                }
            }.setName("onAppProcess").run();
        }
    }
}
