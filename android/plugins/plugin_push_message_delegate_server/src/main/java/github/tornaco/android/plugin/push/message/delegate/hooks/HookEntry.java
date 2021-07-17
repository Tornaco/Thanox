package github.tornaco.android.plugin.push.message.delegate.hooks;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.patchpushdelegate.XposedHookRegistry_PATCHPUSHDELEGATE;

public class HookEntry implements IXposedHook {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (IXposedHookLoadPackage loadPackage
                : XposedHookRegistry_PATCHPUSHDELEGATE.getXposedHookLoadPackageForSdk(Build.VERSION.SDK_INT)) {
            loadPackage.handleLoadPackage(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (IXposedHookZygoteInit loadPackage
                : XposedHookRegistry_PATCHPUSHDELEGATE.getXposedHookZygoteInitForSdk(Build.VERSION.SDK_INT)) {
            loadPackage.initZygote(startupParam);
        }
    }
}
