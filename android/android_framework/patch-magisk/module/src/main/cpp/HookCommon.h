//
// Created by Tornaco on 7/7/21.
//

#ifndef THANOX_INTERNAL_HOOKCOMMON_H
#define THANOX_INTERNAL_HOOKCOMMON_H

#define XHOOK_REGISTER(NAME) \
    if (xhook_register(".*", #NAME, (void*) new_##NAME, (void **) &old_##NAME) == 0) { \
    } else { \
        LOGE("failed to register hook " #NAME "."); \
    }

#define NEW_FUNC_DEF(ret, func, ...) \
    static ret (*old_##func)(__VA_ARGS__); \
    static ret new_##func(__VA_ARGS__)

#endif //THANOX_INTERNAL_HOOKCOMMON_H
