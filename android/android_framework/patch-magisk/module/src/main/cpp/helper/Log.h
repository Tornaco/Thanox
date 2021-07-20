#pragma once

#include <android/log.h>

#define TAG "Thanox-Magisk-Native"

#define LOGV_ENABLED 0

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO  ,TAG ,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG ,TAG ,__VA_ARGS__)
#define LOGV(...) if (LOGV_ENABLED) __android_log_print(ANDROID_LOG_DEBUG ,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR ,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN  ,TAG ,__VA_ARGS__)