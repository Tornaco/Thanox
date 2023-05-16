package io.github.libxposed.api;


import android.annotation.NonNull;

/**
 * Super class which all Xposed module entry classes should extend.<br/>
 * Entry classes will be instantiated exactly once for each process.
 */
@SuppressWarnings("unused")
public abstract class XposedModule extends XposedContextWrapper implements XposedModuleInterface {
    /**
     * Instantiates a new Xposed module.<br/>
     * When the module is loaded into the target process, the constructor will be called.
     *
     * @param base  The base context provided by the framework, should not be used by the module
     * @param param Information about the process in which the module is loaded
     */
    public XposedModule(@NonNull XposedContext base, @NonNull ModuleLoadedParam param) {
        super(base);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPackageLoaded(@NonNull PackageLoadedParam param) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSystemServerLoaded(@NonNull SystemServerLoadedParam param) {

    }
}
