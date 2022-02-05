//
// Created by Tornaco on 7/6/21.
//

#include <cstdio>
#include <cstring>
#include <chrono>
#include <fcntl.h>
#include <unistd.h>
#include <sys/vfs.h>
#include <sys/stat.h>
#include <dirent.h>
#include <dlfcn.h>
#include <cstdlib>
#include <string>
#include <sys/system_properties.h>
#include "../xh/xhook.h"
#include "../helper/Log.h"
#include "ThanoxBridge.h"
#include "HookCommon.h"
#include "TelephonyProps.h"
#include <map>
#include <list>

std::map<void *, void (*)(void *, const char *, const char *, uint32_t)> callbacks;

static void
handleSystemProperty(void *cookie, const char *name, const char *value, uint32_t serial) {
    void (*callback)(void *, const char *, const char *, uint32_t) = callbacks[cookie];
    if (strcmp(name, PROPERTY_OPERATOR_ALPHA) == 0
        || strcmp(name, PROPERTY_OPERATOR_NUMERIC) == 0
        || strcmp(name, PROPERTY_ICC_OPERATOR_ISO_COUNTRY) == 0) {
        const char *replacedValue = getReplacedSystemProp(name);
        LOGV("__system_property_read_callback: name: %s value: %s replacedValue: %s", name, value,
             replacedValue);
        const char *new_value = replacedValue == nullptr ? value : replacedValue;
        callback(cookie, name, new_value == nullptr ? value : new_value, serial);
    } else {
        callback(cookie, name, value, serial);
    }
}

NEW_FUNC_DEF(void, __system_property_read_callback, const prop_info *pi,
             void (*callback)(void *__cookie, const char *__name, const char *__value,
                     uint32_t __serial),
             void *cookie) __INTRODUCED_IN(26) {
    if (cookie == nullptr) {
        old___system_property_read_callback(pi, callback, cookie);
        return;
    }
    callbacks[cookie] = callback;
    old___system_property_read_callback(pi, handleSystemProperty, cookie);
}

NEW_FUNC_DEF(int, __system_property_get, const char *key, char *value) {
    int res = old___system_property_get(key, value);
    LOGV("__system_property_get: %s %s", key, value);
    return res;
}

NEW_FUNC_DEF(prop_info*, __system_property_find, const char *key) {
    prop_info *res = old___system_property_find(key);
    LOGV("__system_property_find: %s", key);
    return res;
}

NEW_FUNC_DEF(std::string,
             _ZN7android4base11GetPropertyERKNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEES9_,
             const std::string &key, const std::string &default_value) {
    std::string res = old__ZN7android4base11GetPropertyERKNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEES9_(
            key, default_value);
    LOGV("old__ZN7android4base11GetPropertyERKNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEES9_: %s %s",
         key.c_str(), res.c_str());
    return res;
}

void installAppHooks() {
    LOGI("installAppHooks");

    XHOOK_REGISTER(__system_property_get);
    XHOOK_REGISTER(__system_property_find);

    if (android_get_device_api_level() >= 28) {
        XHOOK_REGISTER(
                _ZN7android4base11GetPropertyERKNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEES9_);
    }

    XHOOK_REGISTER(__system_property_read_callback);

    xhook_ignore("system/lib/libthanox.so", "__system_property_find");
    xhook_ignore("system/lib/liblog.so", "__system_property_find");
    xhook_ignore("system/lib64/libthanox.so", "__system_property_find");
    xhook_ignore("system/lib64/liblog.so", "__system_property_find");
    xhook_ignore("system/lib64/libthanox.so", "__system_property_read_callback");
    xhook_ignore("system/lib64/liblog.so", "__system_property_read_callback");

    if (xhook_refresh(0) == 0) {
        xhook_clear();
    } else {
        LOGE("failed to refresh hook");
    }
}