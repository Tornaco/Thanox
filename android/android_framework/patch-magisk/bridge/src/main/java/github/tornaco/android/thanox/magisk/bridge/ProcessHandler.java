package github.tornaco.android.thanox.magisk.bridge;

public interface ProcessHandler {
    void onStartSystemServer();

    void onStartApplication();

    void onAppProcess();
}
