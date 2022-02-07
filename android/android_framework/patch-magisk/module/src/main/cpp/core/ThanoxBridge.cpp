//
// Created by TORNACO on 2/21/21.
//

#include "ThanoxBridge.h"
#include "../util/Log.h"
#include <jni.h>
#include <cstring>
#include <cstdlib>
#include <ctime>

#define JAVA_BRIDGE_CLASS "github/tornaco/android/thanox/magisk/bridge/ThanoxBridge"
#define DEX_PATH  "system/framework/thanox-bridge.jar"

#define MAIN_METHOD_NAME "main"

static int invokeMain(JNIEnv *env, const char *event, const char *args) {
    jclass pathClassLoaderClazz = env->FindClass("dalvik/system/PathClassLoader");
    jmethodID constructor = env->GetMethodID(pathClassLoaderClazz, "<init>",
                                             "(Ljava/lang/String;Ljava/lang/ClassLoader;)V");
    jmethodID loadClazz = env->GetMethodID(pathClassLoaderClazz, "loadClass",
                                           "(Ljava/lang/String;)Ljava/lang/Class;");

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

    jstring clazzName = env->NewStringUTF(JAVA_BRIDGE_CLASS);
    jclass bridgeClazz = static_cast<jclass>(env->CallObjectMethod(jBridgePathClassLoaderIns,
                                                                   loadClazz,
                                                                   clazzName));
    // https://stackoverflow.com/questions/14765776/jni-error-app-bug-accessed-stale-local-reference-0xbc00021-index-8-in-a-tabl
    // droid.systemu: JNI ERROR (app bug): accessed stale Local 0x49  (index 4 in a table of size 2)
    // New ref, or it will be recycled by gc.
    env->ExceptionClear();

    if (bridgeClazz == nullptr) {
        LOGE("invokeMain, bridgeClazz == null");
        return JNI_FALSE;
    }

    jmethodID mainMethod = env->GetStaticMethodID(bridgeClazz, MAIN_METHOD_NAME,
                                                  "(Ljava/lang/String;Ljava/lang/String;)V");

    LOGI("invokeMain, bridgeClazz=%p mainMethod=%p", bridgeClazz, mainMethod);

    if (!mainMethod) {
        LOGE("Method not found when calling boot strap main entry.");
        return JNI_FALSE;
    }

    env->CallStaticVoidMethod(bridgeClazz,
                              mainMethod,
                              env->NewStringUTF(event),
                              env->NewStringUTF(args));
    env->ExceptionClear();

    return JNI_TRUE;
}

void startThanox(JNIEnv *env, const char *event, const char *args) {
    LOGD("startThanox, args: %s", args);
    if (invokeMain(env, event, args)) {
        LOGW("invokeMain OK");
    } else {
        LOGW("invokeMain FAIL");
    }
}

