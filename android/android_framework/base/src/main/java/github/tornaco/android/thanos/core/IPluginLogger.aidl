package github.tornaco.android.thanos.core;

interface IPluginLogger {
    void logV(String content);
    void logD(String content);
    void logW(String content);
    void logE(String content);
}