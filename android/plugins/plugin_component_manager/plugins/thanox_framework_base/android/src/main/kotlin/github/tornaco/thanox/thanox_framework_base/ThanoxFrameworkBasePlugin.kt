package github.tornaco.thanox.thanox_framework_base

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.File

class ThanoxFrameworkBasePlugin(private val context: Context) : MethodCallHandler {

    private val gson = Gson()

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "thanox_framework_base")
            channel.setMethodCallHandler(ThanoxFrameworkBasePlugin(registrar.context()))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
            return
        }

        if (call.method == "isServiceInstalled") {
            val thanox = ThanosManager.from(context)
            result.success(thanox.isServiceInstalled)
            return
        }

        if (call.method == "fingerPrint") {
            val thanox = ThanosManager.from(context)
            result.success(thanox.fingerPrint())
            return
        }

        if (call.method == "versionName") {
            val thanox = ThanosManager.from(context)
            result.success(thanox.versionName)
            return
        }

        if (call.method == "getInstalledPkgs") {
            Log.d("TORNACO-LOG", "getInstalledPkgs")
            val thanox = ThanosManager.from(context)
            val res = thanox.pkgManager.getInstalledPkgs(AppInfo.FLAGS_ALL)
            res.forEach {
                        val file: File? = ImageUtils.getAppIconCachedFile(context, it)
                        it.payload = file?.absolutePath
                    }
            Log.d("TORNACO-LOG", "res=${gson.toJson(res)}")
            result.success(gson.toJson(res))
            return
        }

        if (call.method == "getAppIconCachePath") {
            val thanox = ThanosManager.from(context)
            val appPkgName = call.argument<String>("pkgName")
            val appInfo = thanox.pkgManager.getAppInfo(appPkgName)
            if (appInfo == null) {
                result.success(null)
                return
            }

            val file: File? = ImageUtils.getAppIconCachedFile(context, appInfo)
            result.success(file?.absolutePath)
            return
        }

        result.notImplemented()
    }
}
