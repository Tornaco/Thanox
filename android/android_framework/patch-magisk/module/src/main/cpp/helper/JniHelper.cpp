#include"JniHelper.h"
#include <pthread.h>
#include "Log.h"

static pthread_key_t g_key;
using namespace std;

JniMethodInfo::~JniMethodInfo() {
    if (env != NULL && classID != NULL)
        env->DeleteLocalRef(classID);
}

jobject getGlobalContext() {
    JNIEnv *env = JniHelper::getEnv();
    jclass activityThread = env->FindClass("android/app/ActivityThread");
    if (activityThread == nullptr) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return nullptr;
    }
    //LOGE("--getGlobalContext--");
    jmethodID currentActivityThread = env->GetStaticMethodID(activityThread,
                                                             "currentActivityThread",
                                                             "()Landroid/app/ActivityThread;");
    jobject at = env->CallStaticObjectMethod(activityThread, currentActivityThread);
    jmethodID getApplication = env->GetMethodID(activityThread, "getApplication",
                                                "()Landroid/app/Application;");
    jobject context = env->CallObjectMethod(at, getApplication);
    return context;
}

jclass _getClassID(const char *className) {
    if (nullptr == className) {
        return nullptr;
    }
    JNIEnv *env = JniHelper::getEnv();
    jstring _jstrClassName = env->NewStringUTF(className);
    if (JniHelper::classloader == nullptr
        || JniHelper::loadclassMethod_methodID == nullptr) {
        jobject context = JniHelper::mAppContext;
        if (context == nullptr) {
            context = getGlobalContext();
            if (context == nullptr) {
                LOGE("@getGlobalContext == null");
            }
        }
        JniHelper::setClassLoaderFrom(context);
    }
    jclass _clazz = (jclass) env->CallObjectMethod(JniHelper::classloader,
                                                   JniHelper::loadclassMethod_methodID,
                                                   _jstrClassName);
    if (nullptr == _clazz) {
        LOGE("Classloader failed to find class of %s", className);
        env->ExceptionDescribe();
        env->ExceptionClear();
    }

    env->DeleteLocalRef(_jstrClassName);

    return _clazz;
}

void _detachCurrentThread(void *addr) {
    JNIEnv *_env = nullptr;
    jint ret = JniHelper::getJavaVM()->GetEnv((void **) &_env, JNI_VERSION_1_4);
    JniHelper::getJavaVM()->DetachCurrentThread();
    //LOGE("#######_detachCurrentThread %ld %ld ret:%d",(long)pthread_self(),(long)addr,ret);
}

JavaVM *JniHelper::_psJavaVM = nullptr;
jobject JniHelper::mAppContext = nullptr;
jmethodID JniHelper::loadclassMethod_methodID = nullptr;
jobject JniHelper::classloader = nullptr;

JavaVM *JniHelper::getJavaVM() {
    pthread_t thisthread = pthread_self();
    LOGD("JniHelper::getJavaVM(), pthread_self() = %ld", thisthread);
    return _psJavaVM;
}

void JniHelper::setAppContext(jobject context) {
    mAppContext = context;
}

void JniHelper::setJavaVM(JavaVM *javaVM) {
    pthread_t thisthread = pthread_self();
    LOGD("JniHelper::setJavaVM(%p), pthread_self() = %ld", javaVM, thisthread);
    _psJavaVM = javaVM;

    pthread_key_create(&g_key, _detachCurrentThread);
}

JNIEnv *JniHelper::cacheEnv(JavaVM *jvm) {
    JNIEnv *_env = nullptr;
    // get jni environment
    jint ret = jvm->GetEnv((void **) &_env, JNI_VERSION_1_4);

    switch (ret) {
        case JNI_OK :
            // Success!
            //LOGE("#######pthread_setspecific JNI_OK %ld",(long)pthread_self());
            pthread_setspecific(g_key, _env);
            return _env;
        case JNI_EDETACHED :
            // Thread not attached
            if (jvm->AttachCurrentThread(&_env, nullptr) < 0) {
                LOGE("Failed to get the environment using AttachCurrentThread()");
                return nullptr;
            } else {
                // Success : Attached and obtained JNIEnv!
                //LOGE("#######pthread_setspecific JNI_EDETACHED %ld",(long)pthread_self());
                pthread_setspecific(g_key, _env);
                return _env;
            }

        case JNI_EVERSION :
            // Cannot recover from this error
            LOGE("JNI interface version 1.4 not supported");
        default :
            LOGE("Failed to get the environment using GetEnv()");
            return nullptr;
    }
}

JNIEnv *JniHelper::getEnv() {
    JNIEnv *_env = (JNIEnv *) pthread_getspecific(g_key);
    if (_env == nullptr)
        _env = JniHelper::cacheEnv(_psJavaVM);
    return _env;
}

void JniHelper::setThreadEnv(JNIEnv *env) {
    JNIEnv *_env = (JNIEnv *) pthread_getspecific(g_key);
    if (_env == nullptr) {
        pthread_setspecific(g_key, env);
    }
}

bool JniHelper::setClassLoaderFrom(jobject activityinstance) {
    JniMethodInfo _getclassloaderMethod;
    if (!JniHelper::getMethodInfo_DefaultClassLoader(_getclassloaderMethod,
                                                     "android/content/Context",
                                                     "getClassLoader",
                                                     "()Ljava/lang/ClassLoader;")) {
        return false;
    }

    jobject _c = JniHelper::getEnv()->CallObjectMethod(activityinstance,
                                                       _getclassloaderMethod.methodID);

    if (nullptr == _c) {
        return false;
    }

    JniMethodInfo _m;
    if (!JniHelper::getMethodInfo_DefaultClassLoader(_m,
                                                     "java/lang/ClassLoader",
                                                     "loadClass",
                                                     "(Ljava/lang/String;)Ljava/lang/Class;")) {
        return false;
    }

    JniHelper::classloader = JniHelper::getEnv()->NewGlobalRef(_c);
    JniHelper::loadclassMethod_methodID = _m.methodID;

    return true;
}

bool JniHelper::getStaticMethodInfo(JniMethodInfo &methodinfo,
                                    const char *className,
                                    const char *methodName,
                                    const char *paramCode) {
    if ((nullptr == className) ||
        (nullptr == methodName) ||
        (nullptr == paramCode)) {
        return false;
    }

    JNIEnv *env = JniHelper::getEnv();
    if (!env) {
        LOGE("Failed to get JNIEnv");
        return false;
    }

    jclass classID = _getClassID(className);
    if (!classID) {
        LOGE("Failed to find class %s", className);
        env->ExceptionClear();
        return false;
    }

    jmethodID methodID = env->GetStaticMethodID(classID, methodName, paramCode);
    if (!methodID) {
        LOGE("Failed to find static method id of %s", methodName);
        env->ExceptionClear();
        return false;
    }

    methodinfo.classID = classID;
    methodinfo.env = env;
    methodinfo.methodID = methodID;
    return true;
}

bool JniHelper::getMethodInfo_DefaultClassLoader(JniMethodInfo &methodinfo,
                                                 const char *className,
                                                 const char *methodName,
                                                 const char *paramCode) {
    if ((nullptr == className) ||
        (nullptr == methodName) ||
        (nullptr == paramCode)) {
        LOGE("getmethod para err");
        return false;
    }

    JNIEnv *env = JniHelper::getEnv();
    if (!env) {
        LOGE("JniHelper::getEnv err");
        return false;
    }

    jclass classID = env->FindClass(className);
    if (!classID) {
        LOGE("Failed to find class %s", className);
        env->ExceptionClear();
        return false;
    }

    jmethodID methodID = env->GetMethodID(classID, methodName, paramCode);
    if (!methodID) {
        LOGE("Failed to find method id of %s", methodName);
        env->ExceptionClear();
        return false;
    }

    methodinfo.classID = classID;
    methodinfo.env = env;
    methodinfo.methodID = methodID;

    return true;
}

bool JniHelper::getMethodInfoDefault(JniMethodInfo &methodinfo,
                                     const char *className,
                                     const char *methodName,
                                     const char *paramCode) {
    return getMethodInfo_DefaultClassLoader(methodinfo, className, methodName, paramCode);
}

jclass JniHelper::findClass(const char *className) {
    if (className == nullptr) {
        return nullptr;
    }
    JNIEnv *env = JniHelper::getEnv();
    jclass clazz = env->FindClass(className);
    if (clazz == nullptr) {
        env->ExceptionClear();
        //LOGE("_getClassID %ld",(long)clazz);
        clazz = _getClassID(className);
        if (env->ExceptionCheck()) {
            //LOGE("ExceptionCheck %ld",(long)clazz);
            clazz = nullptr;
            env->ExceptionDescribe();
            env->ExceptionClear();
        }
        if (clazz == nullptr) {
            env->ExceptionDescribe();
            env->ExceptionClear();
        }
    }
    return clazz;
}

bool JniHelper::getMethodInfo(JniMethodInfo &methodinfo,
                              jobject jobj,
                              const char *methodName,
                              const char *paramCode) {
    //LOGE("----JniHelper::getMethodInfo----");
    if ((nullptr == jobj) ||
        (nullptr == methodName) ||
        (nullptr == paramCode)) {
        LOGE("getMethodInfo para err");
        return false;
    }

    JNIEnv *env = JniHelper::getEnv();
    if (!env) {
        LOGE("env err");
        return false;
    }

    jclass classID = env->GetObjectClass(jobj);
    if (!classID) {
        LOGE("Failed to find class");
        env->ExceptionDescribe();
        env->ExceptionClear();
        return false;
    }

    jmethodID methodID = env->GetMethodID(classID, methodName, paramCode);
    if (!methodID) {
        LOGE("Failed to find method id of %s", methodName);
        env->ExceptionDescribe();
        env->ExceptionClear();
        return false;
    }

    methodinfo.classID = classID;
    methodinfo.env = env;
    methodinfo.methodID = methodID;

    return true;
}

bool JniHelper::getMethodInfo(JniMethodInfo &methodinfo,
                              const char *className,
                              const char *methodName,
                              const char *paramCode) {
    if ((nullptr == className) ||
        (nullptr == methodName) ||
        (nullptr == paramCode)) {
        return false;
    }

    JNIEnv *env = JniHelper::getEnv();
    if (!env) {
        return false;
    }

    jclass classID = _getClassID(className);
    if (!classID) {
        LOGE("Failed to find class %s", className);
        env->ExceptionClear();
        return false;
    }

    jmethodID methodID = env->GetMethodID(classID, methodName, paramCode);
    if (!methodID) {
        LOGE("Failed to find method id of %s", methodName);
        env->ExceptionClear();
        return false;
    }

    methodinfo.classID = classID;
    methodinfo.env = env;
    methodinfo.methodID = methodID;

    return true;
}

std::string JniHelper::jstring2string(jstring jstr) {
    if (jstr == nullptr) {
        return "";
    }

    JNIEnv *env = JniHelper::getEnv();
    if (!env) {
        return nullptr;
    }

    const char *chars = env->GetStringUTFChars(jstr, nullptr);
    std::string ret(chars);
    env->ReleaseStringUTFChars(jstr, chars);

    return ret;
}

