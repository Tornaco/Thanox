package github.tornaco.android.plugin.toolbox.hooks;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.patchtoolbox.XposedHookRegistry_PATCHTOOLBOX;

public class HookEntry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (IXposedHookLoadPackage loadPackage
                : XposedHookRegistry_PATCHTOOLBOX.getXposedHookLoadPackageForSdk(Build.VERSION.SDK_INT)) {
            loadPackage.handleLoadPackage(lpparam);
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        for (IXposedHookZygoteInit loadPackage
                : XposedHookRegistry_PATCHTOOLBOX.getXposedHookZygoteInitForSdk(Build.VERSION.SDK_INT)) {
            loadPackage.initZygote(startupParam);
        }
    }
}
