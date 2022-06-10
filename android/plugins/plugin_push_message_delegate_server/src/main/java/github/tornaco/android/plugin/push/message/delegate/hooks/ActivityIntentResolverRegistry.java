package github.tornaco.android.plugin.push.message.delegate.hooks;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import util.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.plugin.push.message.delegate.Logging;
import github.tornaco.android.plugin.push.message.delegate.server.PushDelegateManagerNative;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._21;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._22;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._23;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._26;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._27;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._30;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._31;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._32;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._33;

@XposedHook(targetSdkVersion = {_21, _22, _23, _24, _25, _26, _27, _28, _29, _30, _31, _32, _33})
public class ActivityIntentResolverRegistry implements IXposedHook {

    // https://github.com/LineageOS/android_frameworks_base/blob/lineage-15.1/services/core/java/com/android/server/am/BroadcastQueue.java
    private void hookBroadcastRecordPerformReceive(XC_LoadPackage.LoadPackageParam lpparam) {
        Logging.logV("hookBroadcastRecordPerformReceive...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.BroadcastQueue",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllMethods(ams,
                    "performReceiveLocked",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            if (!PushDelegateManagerNative.isServiceInstalled()) {
                                return;
                            }
                            boolean hook = PushDelegateManagerNative.getDefault().shouldHookBroadcastPerformResult();
                            if (hook) {
                                Intent intent = (Intent) param.args[2];
                                int resultCode = (int) param.args[3];

                                int hookedCode = PushDelegateManagerNative.getDefault().onHookBroadcastPerformResult(intent, resultCode);
                                if (isValidResultCode(hookedCode) && resultCode != hookedCode) {
                                    param.args[3] = hookedCode;
                                    Logging.logV("BroadcastRecord perform receive hooked res code to: " + hookedCode);
                                }
                            }
                        }
                    });
            Logging.logV("hookBroadcastRecordPerformReceive OK:" + unHooks);
        } catch (Exception e) {
            Logging.logE("Fail hookBroadcastRecordPerformReceive: " + Log.getStackTraceString(e));
        }
    }

    // Only accept ok or canceled.
    private static boolean isValidResultCode(int code) {
        return code == Activity.RESULT_OK || code == Activity.RESULT_CANCELED;
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookBroadcastRecordPerformReceive(lpparam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {

    }
}
