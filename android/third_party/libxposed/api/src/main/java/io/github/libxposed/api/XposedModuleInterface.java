package io.github.libxposed.api;

import android.annotation.NonNull;
import android.content.pm.ApplicationInfo;
import android.os.Build;


/**
 * Interface for module initialization.
 */
@SuppressWarnings("unused")
public interface XposedModuleInterface {
    /**
     * Wraps information about the process in which the module is loaded.
     */
    interface ModuleLoadedParam {
        /**
         * Get information about whether the module is running in system server.
         *
         * @return {@code true} if the module is running in system server
         */
        boolean isSystemServer();

        /**
         * Get the process name.
         *
         * @return The process name
         */
        @NonNull
        String getProcessName();
    }

    /**
     * Wraps information about system server.
     */
    interface SystemServerLoadedParam {
        /**
         * Get the class loader of system server.
         *
         * @return The class loader
         */
        @NonNull
        ClassLoader getClassLoader();
    }

    /**
     * Wraps information about the package being loaded.
     */
    interface PackageLoadedParam {
        /**
         * Get the package name of the package being loaded.
         *
         * @return The package name.
         */
        @NonNull
        String getPackageName();

        /**
         * Get the ApplicationInfo of the package being loaded.
         *
         * @return The ApplicationInfo.
         */
        @NonNull
        ApplicationInfo getAppInfo();

        /**
         * Gets default class loader.
         *
         * @return the default class loader
         */
        // @RequiresApi(Build.VERSION_CODES.Q)
        @NonNull
        ClassLoader getDefaultClassLoader();

        /**
         * Get the class loader of the package being loaded.
         *
         * @return The class loader.
         */
        @NonNull
        ClassLoader getClassLoader();

        /**
         * Get information about whether is this package the first and main package of the app process.
         *
         * @return {@code true} if this is the first package.
         */
        boolean isFirstPackage();
    }

    /**
     * Get notified when a package is loaded into the app process.<br/>
     * This callback could be invoked multiple times for the same process on each package.
     *
     * @param param Information about the package being loaded
     */
    void onPackageLoaded(@NonNull PackageLoadedParam param);

    /**
     * Get notified when the system server is loaded.
     *
     * @param param Information about system server
     */
    void onSystemServerLoaded(@NonNull SystemServerLoadedParam param);
}
