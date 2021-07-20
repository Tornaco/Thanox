#ifndef JNIHELPER_H
#define JNIHELPER_H

#include<jni.h>
#include<string>

class JniMethodInfo {
public:
    ~JniMethodInfo();

    JNIEnv *env;
    jclass classID;
    jmethodID methodID;

};

/**
 * @brief A jni env container,from cocos2d source code,but it still have some problem
 * @author lss
 * 2015-2016 puwell
 */
class JniHelper {
public:
    /*
     * if use JNI_ONLOAD style jni code please set JavaVM at JNI_OnLoad()function
     * else if use javah style jni code please update JavaVM before you want to use env in thread
     *
     * according to the testing result at 2016/01/22
     * ,if we use javah style jni code,we still get crash
     * when a  thread callback javaobj run long time
     */
    static void setAppContext(jobject context);

    static void setJavaVM(JavaVM *javaVM);

    static JavaVM *getJavaVM();

    static JNIEnv *getEnv();

    static void setThreadEnv(JNIEnv *env);

    static bool setClassLoaderFrom(jobject contextInstance);

    static bool getStaticMethodInfo(JniMethodInfo &methodinfo,
                                    const char *className,
                                    const char *methodName,
                                    const char *paramCode);

    static bool getMethodInfo(JniMethodInfo &methodinfo,
                              const char *className,
                              const char *methodName,
                              const char *paramCode);

    static bool getMethodInfo(JniMethodInfo &methodinfo,
                              jobject jobj,
                              const char *methodName,
                              const char *paramCode);

    static bool getMethodInfoDefault(JniMethodInfo &methodinfo,
                                     const char *className,
                                     const char *methodName,
                                     const char *paramCode);

    static jclass findClass(const char *className);

    static std::string jstring2string(jstring str);

    static jmethodID loadclassMethod_methodID;
    static jobject classloader;
    static jobject mAppContext;
private:
    static bool getMethodInfo_DefaultClassLoader(JniMethodInfo &methodinfo,
                                                 const char *className,
                                                 const char *methodName,
                                                 const char *paramCode);

    static JNIEnv *cacheEnv(JavaVM *jvm);

    static JavaVM *_psJavaVM;
};

#endif // JNIHELPER_H

