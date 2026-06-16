package github.tornaco.android.thanos.services.xposed

import github.tornaco.android.thanos.services.xposed.XposedAdapter.ExceptionModeCompat
import java.lang.reflect.Method

fun beforeConstruct(
    clazz: Class<*>,
    log: (String) -> Unit,
    exceptionModeCompat: ExceptionModeCompat = ExceptionModeCompat.PROTECTIVE,
    beforeConstruct: (param: ThanoxHookParam) -> Unit,
) {
    val constructors = clazz.declaredConstructors
    constructors.forEach { constructor ->
        kotlin.runCatching {
            XposedRuntime.current().hookBefore(constructor, exceptionModeCompat) { param ->
                kotlin.runCatching {
                    beforeConstruct(param)
                }.onFailure {
                    log("beforeConstruct-$clazz-${it.stackTraceToString()}")
                }
            }
        }.onFailure {
            log("beforeConstruct-$clazz-${it.stackTraceToString()}")
        }
    }
}

fun afterConstruct(
    clazz: Class<*>,
    log: (String) -> Unit,
    exceptionModeCompat: ExceptionModeCompat = ExceptionModeCompat.PROTECTIVE,
    afterConstruct: (param: ThanoxHookParam) -> Unit,
) {
    val constructors = clazz.declaredConstructors
    constructors.forEach { constructor ->
        kotlin.runCatching {
            XposedRuntime.current().hookAfter(constructor, exceptionModeCompat) { param ->
                kotlin.runCatching {
                    afterConstruct(param)
                }.onFailure {
                    log("afterConstruct-$clazz-${it.stackTraceToString()}")
                }
            }
        }.onFailure {
            log("afterConstruct-$clazz-${it.stackTraceToString()}")
        }
    }
}

fun beforeMethod(
    clazz: Class<*>,
    methodName: String,
    log: (String) -> Unit,
    exceptionModeCompat: ExceptionModeCompat = ExceptionModeCompat.PROTECTIVE,
    beforeMethod: (param: ThanoxHookParam) -> Unit,
) {
    val methods = clazz.declaredMethods.filter { it.name == methodName }
    require(methods.isNotEmpty()) {
        "beforeMethod, unable to hook this method: $clazz#$methodName"
    }
    methods.forEach { method ->
        XposedRuntime.current().hookBefore(method, exceptionModeCompat) { param ->
            kotlin.runCatching {
                beforeMethod(param)
            }.onFailure {
                log("beforeHookedMethod-$clazz-$methodName ${it.stackTraceToString()}")
            }
        }
    }
    log("beforeMethod, hooked methods: $methods for method: $clazz#$methodName")
}

fun afterMethod(
    clazz: Class<*>,
    methodName: String,
    log: (String) -> Unit,
    exceptionModeCompat: ExceptionModeCompat = ExceptionModeCompat.PROTECTIVE,
    afterMethod: (param: ThanoxHookParam) -> Unit,
) {
    val methods = clazz.declaredMethods.filter { it.name == methodName }
    require(methods.isNotEmpty()) {
        "afterMethod, unable to hook this method: $clazz#$methodName"
    }
    methods.forEach { method ->
        XposedRuntime.current().hookAfter(method, exceptionModeCompat) { param ->
            kotlin.runCatching {
                afterMethod(param)
            }.onFailure {
                log("afterHookedMethod-$clazz-$methodName-${it.stackTraceToString()}")
            }
        }
    }
    log("afterMethod, hooked methods: $methods for method: $clazz#$methodName")
}

fun afterMethod(
    method: Method,
    log: (String) -> Unit,
    exceptionModeCompat: ExceptionModeCompat = ExceptionModeCompat.PROTECTIVE,
    afterMethod: (param: ThanoxHookParam) -> Unit,
) {
    XposedRuntime.current().hookAfter(method, exceptionModeCompat) { param ->
        kotlin.runCatching {
            afterMethod(param)
        }.onFailure {
            log("afterHookedMethod-$method-${it.stackTraceToString()}")
        }
    }
    log("afterMethod, hooked method: $method")
}
