#include <cstdlib>
#include <unistd.h>
#include <fcntl.h>
#include <android/log.h>

#include "zygisk.hpp"
#include "../helper/Log.h"
#include "../core/ThanoxBridge.h"

using zygisk::Api;
using zygisk::AppSpecializeArgs;
using zygisk::ServerSpecializeArgs;

class ThanoxModule : public zygisk::ModuleBase {
public:
    void onLoad(Api *api, JNIEnv *env) override {
        this->zApi = api;
        this->zEnv = env;
    }

    void preAppSpecialize(AppSpecializeArgs *args) override {
        // Use JNI to fetch our process name
        const char *process = zEnv->GetStringUTFChars(args->nice_name, nullptr);
        preSpecializeApp(process);
        zEnv->ReleaseStringUTFChars(args->nice_name, process);
    }

    void preServerSpecialize(ServerSpecializeArgs *args) override {
        preSpecializeSystemServer();
    }

    void postAppSpecialize(const AppSpecializeArgs *args) override {
        const char *process = zEnv->GetStringUTFChars(args->nice_name, nullptr);
        postSpecializeApp(process);
        zEnv->ReleaseStringUTFChars(args->nice_name, process);
    }

    void postServerSpecialize(const ServerSpecializeArgs *args) override {
        postSpecializeSystemServer();
    }

private:
    Api *zApi;
    JNIEnv *zEnv;

    void preSpecializeApp(const char *process) {

    }

    void preSpecializeSystemServer() {
        LOGD("preSpecialize, process=[system_server]");
    }

    void postSpecializeApp(const char *process) {
    }

    void postSpecializeSystemServer() {
        LOGD("postSpecialize, process=[system_server]");
        startThanox(zEnv, "postSpecializeSystemServer");
    }
};

REGISTER_ZYGISK_MODULE(ThanoxModule)
