package github.tornaco.android.thanos.core.phone

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SubscriptionManager
import android.text.TextUtils
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.telephonyManager
import github.tornaco.android.thanos.core.util.OsUtils
import java.lang.ref.WeakReference
import java.lang.reflect.Modifier
import java.util.*

/**
 * For better performance should be called on background thread
 * https://mvnrepository.com/artifact/com.kirianov.multisim/multisim
 */
@Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")
class MultiSimManager(context: Context) {

    private val reference = WeakReference(context)

    private val slots = arrayListOf<Slot>()

    fun getSlots(): MutableList<Slot> {
        return Collections.synchronizedList(slots)
    }

    @Synchronized
    @SuppressLint("MissingPermission", "HardwareIds")
    fun updateInfo(): String? = reference.get()?.run {
        val telephonyManager = telephonyManager
        val slots = getSlots()
        synchronized(slots) {
            slots.clear()
            val error = try {
                XLog.v("telephonyManager [$telephonyManager] ${telephonyManager.deviceId}")
                val subscriberIdIntValue = arrayListOf<String>()
                val subscriberIdIntIndex = arrayListOf<Int>()
                for (i in 0 until 100) {
                    val subscriber = runMethodReflect(
                        telephonyManager,
                        "android.telephony.TelephonyManager",
                        "getSubscriberId",
                        arrayOf(i),
                        null
                    ) as? String
                    if (subscriber != null && !subscriberIdIntValue.contains(subscriber)) {
                        subscriberIdIntValue.add(subscriber)
                        subscriberIdIntIndex.add(i)
                    }
                }
                val subscriberIdLongValue = arrayListOf<String>()
                val subscriberIdLongIndex = arrayListOf<Long>()
                for (i in 0L until 100L) {
                    runMethodReflect(
                        telephonyManager,
                        "android.telephony.TelephonyManagerSprd",
                        "getSubInfoForSubscriber",
                        arrayOf(i),
                        null
                    ) ?: continue
                    val subscriber = runMethodReflect(
                        telephonyManager,
                        "android.telephony.TelephonyManager",
                        "getSubscriberId",
                        arrayOf(i),
                        null
                    ) as? String
                    if (subscriber != null && !subscriberIdLongValue.contains(subscriber)) {
                        subscriberIdLongValue.add(subscriber)
                        subscriberIdLongIndex.add(i)
                    }
                }
                if (subscriberIdLongIndex.isEmpty()) {
                    for (i in 0L until 100L) {
                        val subscriber = runMethodReflect(
                            telephonyManager,
                            "android.telephony.TelephonyManager",
                            "getSubscriberId",
                            arrayOf(i),
                            null
                        ) as? String
                        if (subscriber != null && !subscriberIdLongValue.contains(subscriber)) {
                            subscriberIdLongValue.add(subscriber)
                            subscriberIdLongIndex.add(i)
                        }
                    }
                }
                var slotNumber = 0
                for (i in 0 until 100) {
                    val subIdInt = subscriberIdIntIndex.getOrNull(slotNumber)
                    val subIdLong = subscriberIdLongIndex.getOrNull(slotNumber)
                    val slot = touchSlot(slotNumber, subIdInt, subIdLong) ?: break
                    if (slot.indexIn(slots) in 0 until slotNumber) {
                        break
                    }
                    slots.add(slot)
                    slotNumber++
                }
                null
            } catch (e: Throwable) {
                XLog.e(e)
                e.toString()
            }
            slots.apply {
                removeAll { it.imsi == null }
                val imsi = arrayListOf<String?>()
                val iterator = iterator()
                while (iterator.hasNext()) {
                    val slot = iterator.next()
                    if (imsi.contains(slot.imsi)) {
                        iterator.remove()
                    } else {
                        imsi.add(slot.imsi)
                        slots.forEach {
                            if (it.imsi == slot.imsi) {
                                if (it.isActive) {
                                    slot.simState = it.simState
                                }
                                if (TextUtils.isEmpty(slot.simOperator)) {
                                    slot.simOperator = it.simOperator
                                    slot.simOperatorName = it.simOperatorName
                                    slot.simCountryIso = it.simCountryIso
                                }
                            }
                        }
                    }
                }
            }
            if (OsUtils.isMOrAbove()) {
                val subscriptionManager =
                    getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                val list = subscriptionManager.activeSubscriptionInfoList
                list?.forEach { info ->
                    slots.filter { it.simSerialNumber == info.iccId }.forEach {
                        it.mcc = info.mcc.toString().padStart(3, '0')
                    }
                }
            }
            error
        }
    }

    @Suppress("SpellCheckingInspection")
    @SuppressLint("MissingPermission", "HardwareIds")
    private fun touchSlot(slotNumber: Int, subIdI: Int?, subIdL: Long?): Slot? =
        reference.get()?.run {
            val telephonyManager = telephonyManager
            XLog.v("------------------------------------------")
            XLog.v("SLOT [$slotNumber]")
            val subIdInt = subIdI ?: try {
                runMethodReflect(
                    telephonyManager,
                    "android.telephony.TelephonyManager",
                    "getSubId",
                    arrayOf(slotNumber),
                    null
                ).toString().toInt()
            } catch (ignored: Throwable) {
                null
            }
            XLog.v("subIdInt $subIdInt")
            val subIdLong = subIdL ?: runMethodReflect(
                telephonyManager,
                "android.telephony.TelephonyManager",
                "getSubId",
                arrayOf(slotNumber),
                null
            ) as? Long
            XLog.v("subIdLong $subIdLong")
            val objectParamsSubs = arrayListOf<Any>().apply {
                if (subIdInt != null && !contains(subIdInt)) {
                    add(subIdInt)
                }
                if (subIdLong != null && !contains(subIdLong)) {
                    add(subIdLong)
                }
                if (!contains(slotNumber)) {
                    add(slotNumber)
                }
            }.toTypedArray()
            for (i in objectParamsSubs.indices) {
                XLog.v("ITERATE PARAMS_SUBS [$i]=[${objectParamsSubs[i]}]")
            }
            val objectParamsSlot = arrayListOf<Any>().apply {
                if (!contains(slotNumber)) {
                    add(slotNumber)
                }
                if (subIdInt != null && !contains(subIdInt)) {
                    add(subIdInt)
                }
                if (subIdLong != null && !contains(subIdLong)) {
                    add(subIdLong)
                }
            }.toTypedArray()
            for (i in objectParamsSlot.indices) {
                XLog.v("ITERATE PARAMS_SLOT [$i]=[${objectParamsSlot[i]}]")
            }
            return Slot().apply {
                if (OsUtils.isNOrAbove()) {
                    imei = telephonyManager.getDeviceId(slotNumber)
                }
                if (imei == null) {
                    imei = iterateMethods("getDeviceId", objectParamsSlot) as? String
                }
                if (imei == null) {
                    imei = runMethodReflect(
                        null,
                        "com.android.internal.telephony.Phone",
                        null,
                        null,
                        "GEMINI_SIM_${slotNumber + 1}"
                    ) as? String
                }
                if (imei == null) {
                    imei = runMethodReflect(
                        getSystemService("phone${slotNumber + 1}"),
                        null,
                        "getDeviceId",
                        null,
                        null
                    ) as? String
                }
                XLog.v("IMEI [$imei]")
                if (imei == null) {
                    if (slotNumber == 0) {
                        imei = if (OsUtils.isPOrAbove()) {
                            telephonyManager.imei
                        } else {
                            telephonyManager.deviceId
                        }
                        imsi = telephonyManager.subscriberId
                        simSerialNumber = telephonyManager.simSerialNumber
                        simState = telephonyManager.simState
                        simOperator = telephonyManager.simOperator
                        simOperatorName = telephonyManager.simOperatorName
                        simCountryIso = telephonyManager.simCountryIso
                        return this
                    }
                }
                if (imei == null) {
                    return null
                }
                imsi = iterateMethods("getSubscriberId", objectParamsSubs) as? String
                XLog.v("IMSI [$imsi]")
                simSerialNumber = iterateMethods("getSimSerialNumber", objectParamsSubs) as? String
                XLog.v("SIMSERIALNUMBER [$simSerialNumber]")
                setSimState(iterateMethods("getSimState", objectParamsSlot) as? Int)
                XLog.v("SIMSTATE [$simState]")
                simOperator = iterateMethods("getSimOperator", objectParamsSubs) as? String
                XLog.v("SIMOPERATOR [$simOperator]")
                simOperatorName = iterateMethods("getSimOperatorName", objectParamsSubs) as? String
                XLog.v("SIMOPERATORNAME [$simOperatorName]")
                simCountryIso = iterateMethods("getSimCountryIso", objectParamsSubs) as? String
                XLog.v("SIMCOUNTRYISO [$simCountryIso]")
            }
        }

    @SuppressLint("WrongConstant")
    private fun iterateMethods(methodName: String, methodParams: Array<Any>): Any? =
        reference.get()?.run {
            val telephonyManager = telephonyManager
            val instanceMethods = arrayListOf<Any?>()
            val multiSimTelephonyManagerExists = telephonyManager.toString()
                .startsWith("android.telephony.MultiSimTelephonyManager")
            for (methodParam in methodParams) {
                val objectMulti = if (multiSimTelephonyManagerExists) {
                    runMethodReflect(
                        null,
                        "android.telephony.MultiSimTelephonyManager",
                        "getDefault",
                        arrayOf(methodParam),
                        null
                    )
                } else {
                    telephonyManager
                }
                if (!instanceMethods.contains(objectMulti)) {
                    instanceMethods.add(objectMulti)
                }
            }
            if (!instanceMethods.contains(telephonyManager)) {
                instanceMethods.add(telephonyManager)
            }
            val telephonyManagerEx = runMethodReflect(
                null,
                "com.mediatek.telephony.TelephonyManagerEx",
                "getDefault",
                null,
                null
            )
            if (!instanceMethods.contains(telephonyManagerEx)) {
                instanceMethods.add(telephonyManagerEx)
            }
            val phoneMsim = getSystemService("phone_msim")
            if (!instanceMethods.contains(phoneMsim)) {
                instanceMethods.add(phoneMsim)
            }
            if (!instanceMethods.contains(null)) {
                instanceMethods.add(null)
            }
            for (className in classNames) {
                for (methodSuffix in suffixes) {
                    for (instanceMethod in instanceMethods) {
                        for (methodParam in methodParams) {
                            val result = runMethodReflect(
                                instanceMethod,
                                className,
                                methodName + methodSuffix,
                                if (multiSimTelephonyManagerExists) null else arrayOf(methodParam),
                                null
                            )
                            if (result != null) {
                                return result
                            }
                        }
                    }
                }
            }
            return null
        }

    private fun runMethodReflect(
        instanceInvoke: Any?,
        classInvokeName: String?,
        methodName: String?,
        methodParams: Array<Any>?,
        field: String?
    ): Any? {
        try {
            val classInvoke = when {
                classInvokeName != null -> Class.forName(classInvokeName)
                instanceInvoke != null -> instanceInvoke.javaClass
                else -> return null
            }
            return if (field != null) {
                val fieldReflect = classInvoke.getField(field)
                val accessible = fieldReflect.isAccessible
                fieldReflect.isAccessible = true
                val result = fieldReflect.get(null).toString()
                fieldReflect.isAccessible = accessible
                result
            } else {
                val classesParams = if (methodParams != null) {
                    Array(methodParams.size) {
                        when {
                            methodParams[it] is Int -> Int::class.javaPrimitiveType
                            methodParams[it] is Long -> Long::class.javaPrimitiveType
                            methodParams[it] is Boolean -> Boolean::class.javaPrimitiveType
                            else -> null
                        } ?: methodParams[it].javaClass
                    }
                } else {
                    null
                }
                val method = if (classesParams != null) {
                    classInvoke.getDeclaredMethod(methodName.toString(), *classesParams)
                } else {
                    classInvoke.getDeclaredMethod(methodName.toString())
                }
                val accessible = method.isAccessible
                method.isAccessible = true
                val result = if (methodParams != null) {
                    method.invoke(instanceInvoke ?: classInvoke, *methodParams)
                } else {
                    method.invoke(instanceInvoke ?: classInvoke)
                }
                method.isAccessible = accessible
                result
            }
        } catch (ignored: Throwable) {
        }
        return null
    }

    val allFields: String
        get() = """
            Default: ${reference.get()?.telephonyManager}
            ${printAllFields("android.telephony.TelephonyManager")}
            ${printAllFields("android.telephony.MultiSimTelephonyManager")}
            ${printAllFields("android.telephony.MSimTelephonyManager")}
            ${printAllFields("com.mediatek.telephony.TelephonyManager")}
            ${printAllFields("com.mediatek.telephony.TelephonyManagerEx")}
            ${printAllFields("com.android.internal.telephony.ITelephony")}
        """.trimIndent()

    val allMethods: String
        get() = """
            Default: ${reference.get()?.telephonyManager}
            ${printAllMethods("android.telephony.TelephonyManager")}
            ${printAllMethods("android.telephony.MultiSimTelephonyManager")}
            ${printAllMethods("android.telephony.MSimTelephonyManager")}
            ${printAllMethods("com.mediatek.telephony.TelephonyManager")}
            ${printAllMethods("com.mediatek.telephony.TelephonyManagerEx")}
            ${printAllMethods("com.android.internal.telephony.ITelephony")}
        """.trimIndent()

    private fun printAllFields(className: String): String {
        val builder = StringBuilder()
        builder.append("========== $className\n")
        try {
            val cls = Class.forName(className)
            for (field in cls.fields) {
                builder.append("F: ${field.name} ${field.type}\n")
            }
        } catch (e: Throwable) {
            builder.append("E: $e\n")
        }
        return builder.toString()
    }

    private fun printAllMethods(className: String): String {
        val builder = StringBuilder()
        builder.append("========== $className\n")
        try {
            val cls = Class.forName(className)
            for (method in cls.methods) {
                val params = method.parameterTypes.map { it.name }
                builder.append(
                    "M: ${method.name} [${params.size}](${TextUtils.join(
                        ",",
                        params
                    )}) -> ${method.returnType}${if (Modifier.isStatic(method.modifiers)) " (static)" else ""}\n"
                )
            }
        } catch (e: Throwable) {
            builder.append("E: $e\n")
        }
        return builder.toString()
    }

    companion object {

        private val classNames = arrayOf(
            null,
            "android.telephony.TelephonyManager",
            "android.telephony.MSimTelephonyManager",
            "android.telephony.MultiSimTelephonyManager",
            "com.mediatek.telephony.TelephonyManagerEx",
            "com.android.internal.telephony.Phone",
            "com.android.internal.telephony.PhoneFactory"
        )

        private val suffixes = arrayOf(
            "",
            "Gemini",
            "Ext",
            "Ds",
            "ForSubscription",
            "ForPhone"
        )
    }
}