package github.tornaco.android.nitro.framework.plugin;

import android.os.Build;
import android.util.Log;

import com.elvishew.xlog.XLog;

import java.io.File;

import dalvik.system.BaseDexClassLoader;

public class PluginClassLoader extends BaseDexClassLoader {
    private static final String TAG = "PluginClassLoader";

    private ClassLoader hostClassLoader;

    public PluginClassLoader(ClassLoader hostClassLoader,
                             String dexPath,
                             File optimizedDirectory,
                             String librarySearchPath) {
        super(dexPath, optimizedDirectory, librarySearchPath, hostClassLoader.getParent());
        this.hostClassLoader = hostClassLoader;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        return loadClassInternal(className, resolve);
    }

    private Class<?> loadClassInternal(String className, boolean resolve) throws ClassNotFoundException {
        if ((Build.VERSION.SDK_INT < 28 && className.startsWith("org.apache.http"))) {
            // Android 9.0以下的系统里面带有http包，走系统的不走本地的
            return hostClassLoader.loadClass(className);
        } else if (className.startsWith("github.tornaco.android.thanos.core")) {
            return hostClassLoader.loadClass(className);
            // Framework util.
        } else if (className.startsWith("util")) {
            return hostClassLoader.loadClass(className);
        } else {
            try {
                return super.loadClass(className, resolve);
            } catch (ClassNotFoundException e) {
                if (className.equals("github.tornaco.android.nitro.framework.plugin.ComponentFactory")) {
                    XLog.e("ComponentFactory loading fail: %s", Log.getStackTraceString(e));
                    throw new RuntimeException(e);
                }
                return hostClassLoader.loadClass(className);
            }
        }
    }
}
