//
// Created by TORNACO on 2/21/21.
//

#ifndef THANOX_INTERNAL_THANOXBRIDGE_H
#define THANOX_INTERNAL_THANOXBRIDGE_H


#include <jni.h>

void startThanox(JNIEnv *env, const char *args);

const char *getReplacedSystemProp(const char *key);

#endif //THANOX_INTERNAL_THANOXBRIDGE_H
