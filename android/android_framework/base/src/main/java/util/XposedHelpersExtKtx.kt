package util

import de.robv.android.xposed.XC_MethodHook

object XposedHelpersExtKtx {
    fun XC_MethodHook.MethodHookParam.firstArgIndexOfType(type: Class<*>): Int {
        return args.indexOfFirst {
            it.javaClass == type
        }
    }
}