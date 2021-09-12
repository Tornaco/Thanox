package github.tornaco.android.thanox.magisk.bridge;

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
        private static final String SYSTEM_SERVER_CLASS_NAME = "com.android.server.SystemServer";
        private static final String APPLICATION_CLASS_NAME = "android.app.Application";

        public HookedBootClassLoader() {
            super(boot);
        }

        @Override
        protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
            if (LOGV) XLog.d("HookedBootClassLoader#loadClass: " + className);

            if (SYSTEM_SERVER_CLASS_NAME.equals(className)) {
                int uid = Binder.getCallingUid();
                XLog.d("onLoadSystemServer. calling uid: " + uid);
                if (uid == 1000) {
                    onStartSystemServer();
                }
            } else if (APPLICATION_CLASS_NAME.equals(className)) {
                onStartApplication();
            }

            return super.loadClass(className, resolve);
        }

        private void onStartSystemServer() {
            new AbstractSafeR() {
                @Override
                public void runSafety() {
                    processHandler.onStartSystemServer();
                }
            }.setName("onStartSystemServer").run();
        }

        private void onStartApplication() {
            new AbstractSafeR() {
                @Override
                public void runSafety() {
                    processHandler.onStartApplication();
                }
            }.setName("onStartApplication").run();
        }
    }
}
