package github.tornaco.android.thanos.services.xposed

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Method

fun beforeConstruct(
    clazz: Class<*>,
    log: (String) -> Unit,
    beforeConstruct: (param: XC_MethodHook.MethodHookParam) -> Unit,
) {
    XposedBridge.hookAllConstructors(clazz, object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam?) {
            super.beforeHookedMethod(param)
            param?.let {
                kotlin.runCatching {
                    beforeConstruct(it)
                }.onFailure {
                    log("beforeConstruct-$clazz-${it.stackTraceToString()}")
                }
            }
        }
    })
}

fun afterConstruct(
    clazz: Class<*>,
    log: (String) -> Unit,
    afterConstruct: (param: XC_MethodHook.MethodHookParam) -> Unit,
) {
    XposedBridge.hookAllConstructors(clazz, object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            param?.let {
                kotlin.runCatching {
                    afterConstruct(it)
                }.onFailure {
                    log("afterConstruct-$clazz-${it.stackTraceToString()}")
                }
            }
        }
    })
}

fun beforeMethod(
    clazz: Class<*>,
    methodName: String,
    log: (String) -> Unit,
    beforeMethod: (param: XC_MethodHook.MethodHookParam) -> Unit,
) {
    XposedBridge.hookAllMethods(clazz, methodName, object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam?) {
            super.beforeHookedMethod(param)
            param?.let {
                kotlin.runCatching {
                    beforeMethod(it)
                }.onFailure {
                    log("beforeHookedMethod-$clazz-$methodName ${it.stackTraceToString()}")
                }
            }
        }
    }).apply {
        require(this.size > 0) {
            "beforeMethod, unable to hook this method: $clazz#$methodName"
        }
        log("beforeMethod, unhooks: $this for method: $clazz#$methodName")
    }
}

fun afterMethod(
    clazz: Class<*>,
    methodName: String,
    log: (String) -> Unit,
    afterMethod: (param: XC_MethodHook.MethodHookParam) -> Unit,
) {
    XposedBridge.hookAllMethods(clazz, methodName, object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            param?.let {
                kotlin.runCatching {
                    afterMethod(it)
                }.onFailure {
                    log("afterHookedMethod-$clazz-$methodName-${it.stackTraceToString()}")
                }
            }
        }
    }).apply {
        require(this.size > 0) {
            "afterMethod, unable to hook this method: $clazz#$methodName"
        }
        log("afterMethod, unhooks: $this for method: $clazz#$methodName")
    }
}

fun afterMethod(
    method: Method,
    log: (String) -> Unit,
    afterMethod: (param: XC_MethodHook.MethodHookParam) -> Unit,
) {
    val unHook: XC_MethodHook.Unhook? = XposedBridge.hookMethod(method, object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            param?.let {
                kotlin.runCatching {
                    afterMethod(it)
                }.onFailure {
                    log("afterHookedMethod-$method-${it.stackTraceToString()}")
                }
            }
        }
    })
    require(unHook != null) {
        "afterMethod, unable to hook this method: $method"
    }
    log("afterMethod, unhook: $unHook for method: $method")
}
