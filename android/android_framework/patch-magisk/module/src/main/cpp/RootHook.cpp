//
// Created by Tornaco on 7/7/21.
//

#include "HookCommon.h"
#include "helper/Log.h"
#include "xh/xhook.h"
#include "helper/JniHelper.h"
#include "ThanoxBridge.h"
#include <jni.h>

void onRuntimeStart(JNIEnv *env) {
    LOGI("onRuntimeStart");
    if (env == nullptr) {
        LOGE("onRuntimeStart, env is null");
        return;
    }
    // Init JavaVM
    JavaVM *jvm;
    env->GetJavaVM(&jvm);
    JniHelper::setJavaVM(jvm);
    // Start Thanox Java.
    startThanox(env, "onRuntimeStart");
}

NEW_FUNC_DEF(int, _ZN7android14AndroidRuntime8startRegEP7_JNIEnv, JNIEnv *env) {
    int res = old__ZN7android14AndroidRuntime8startRegEP7_JNIEnv(env);
    LOGI("_ZN7android14AndroidRuntime8startRegEP7_JNIEnv");
    onRuntimeStart(env);
    return res;
}

int hookAndroidRuntimeStart() {
    LOGI("hookAndroidRuntimeStart");
    XHOOK_REGISTER(_ZN7android14AndroidRuntime8startRegEP7_JNIEnv);

    if (xhook_refresh(0) == 0) {
        xhook_clear();
        return JNI_OK;
    } else {
        LOGE("failed to refresh hook");
        return JNI_ABORT;
    }
}