//
// Created by TORNACO on 2/21/21.
//

#include "ThanoxBridge.h"
#include "../helper/Log.h"
#include "AppHook.h"
#include "../helper/JniHelper.h"
#include <jni.h>
#include <cstring>
#include <cstdlib>

#define JAVA_BRIDGE_CLASS "github/tornaco/android/thanox/magisk/bridge/ThanoxBridge"
#define DEX_PATH  "system/framework/thanox-bridge.jar"

#define MAIN_METHOD_NAME "main"
#define GET_PROP_METHOD_NAME "getProp"

static jclass bridgeClazz;
static jmethodID bridgeGetPropMethod;

static void nativeInstallAppHook() {
    LOGW("nativeInstallAppHook~");
    installAppHooks();
}

static JNINativeMethod gBridgeNativeMethods[] = {
        {"nativeInstallAppHook", "()V", (void *) nativeInstallAppHook}
};

static int invokeMain(JNIEnv *env, const char *args) {
    LOGD("invokeMain");
    jclass pathClassLoaderClazz = env->FindClass("dalvik/system/PathClassLoader");
    LOGD("invokeMain, find pathClassLoaderClazz");
    jmethodID constructor = env->GetMethodID(pathClassLoaderClazz, "<init>",
                                             "(Ljava/lang/String;Ljava/lang/ClassLoader;)V");
    LOGD("invokeMain, find constructor");
    jmethodID loadClazz = env->GetMethodID(pathClassLoaderClazz, "loadClass",
                                           "(Ljava/lang/String;)Ljava/lang/Class;");
    LOGD("invokeMain, find loadClazz");

    FILE *bridgeDexFile = fopen(DEX_PATH, "rb");
    if (!bridgeDexFile) {
        LOGE("Fail to open bridge dex from %s", DEX_PATH);
        return JNI_FALSE;
    }
    fclose(bridgeDexFile);

    jstring jBridgeDexPath = env->NewStringUTF(DEX_PATH);
    jobject jParent = nullptr;
    jobject jBridgePathClassLoaderIns = env->NewObject(pathClassLoaderClazz, constructor,
                                                       jBridgeDexPath, jParent);
    LOGD("invokeMain, create jBridgePathClassLoaderIns");


    jstring clazzName = env->NewStringUTF(JAVA_BRIDGE_CLASS);
    jclass tmpBridgeClazz = static_cast<jclass>(env->CallObjectMethod(jBridgePathClassLoaderIns,
                                                                      loadClazz,
                                                                      clazzName));
    // https://stackoverflow.com/questions/14765776/jni-error-app-bug-accessed-stale-local-reference-0xbc00021-index-8-in-a-tabl
    // droid.systemu: JNI ERROR (app bug): accessed stale Local 0x49  (index 4 in a table of size 2)
    // New ref, or it will be recycled by gc.
    bridgeClazz = (jclass) env->NewGlobalRef(tmpBridgeClazz);
    env->ExceptionClear();

    if (bridgeClazz == NULL) {
        LOGE("invokeMain, bridgeClazz == NULL");
        return JNI_FALSE;
    }

    // Register methods.
    if (env->RegisterNatives(bridgeClazz, gBridgeNativeMethods,
                             sizeof(gBridgeNativeMethods) / sizeof(gBridgeNativeMethods[0])) < 0) {
        LOGE("RegisterNatives, res < 0");
        return JNI_FALSE;
    }

    jmethodID mainMethod = env->GetStaticMethodID(bridgeClazz, MAIN_METHOD_NAME,
                                                  "(Ljava/lang/String;)V");
    bridgeGetPropMethod = env->GetStaticMethodID(bridgeClazz, GET_PROP_METHOD_NAME,
                                                 "(Ljava/lang/String;)Ljava/lang/String;");
    LOGI("invokeMain, bridgeClazz=%p mainMethod=%p bridgeGetPropMethod=%p", bridgeClazz, mainMethod,
         bridgeGetPropMethod);

    if (!mainMethod || !bridgeGetPropMethod) {
        LOGE("Method not found when calling boot strap main entry.");
        return JNI_FALSE;
    }

    env->CallStaticVoidMethod(bridgeClazz,
                              mainMethod,
                              env->NewStringUTF(args));
    env->ExceptionClear();

    return JNI_TRUE;
}

void startThanox(JNIEnv *env, const char *args) {
    LOGD("startThanox");
    if (invokeMain(env, args)) {
        LOGW("invokeMain OK");
    } else {
        LOGW("invokeMain FAIL");
    }
}

const char *getReplacedSystemProp(const char *key) {
    if (!bridgeClazz || !bridgeGetPropMethod) {
        LOGE("getReplacedSystemProp, bridgeClazz or bridgeGetPropMethod is null.");
        return nullptr;
    }

    JNIEnv *env = JniHelper::getEnv();
    if (env == nullptr) {
        LOGE("getReplacedSystemProp, env is null.");
        return nullptr;
    }

    auto jValue = (jstring) env->CallStaticObjectMethod(bridgeClazz,
                                                        bridgeGetPropMethod,
                                                        env->NewStringUTF(key));
    env->ExceptionClear();
    if (jValue == nullptr) {
        return nullptr;
    }
    const char *chars = env->GetStringUTFChars(jValue, nullptr);
    env->DeleteLocalRef(jValue);
    LOGI("getReplacedSystemProp, res---> %s", chars);
    return chars;
}

