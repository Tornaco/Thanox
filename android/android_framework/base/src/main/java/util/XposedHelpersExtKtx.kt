package util

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object XposedHelpersExtKtx {
    fun XC_MethodHook.MethodHookParam.firstArgIndexOfType(type: Class<*>): Int {
        return args.indexOfFirst {
            it.javaClass == type
        }
    }

    fun hookAllMethods(
        hookClass: Class<*>,
        methodNames: Array<String>,
        callback: XC_MethodHook
    ): Set<XC_MethodHook.Unhook> {
        val res = mutableSetOf<XC_MethodHook.Unhook>()
        methodNames.forEach {
            runCatching {
                val resI = XposedBridge.hookAllMethods(
                    hookClass, it, callback
                )
                res.addAll(resI)
            }
        }
        return res
    }
}