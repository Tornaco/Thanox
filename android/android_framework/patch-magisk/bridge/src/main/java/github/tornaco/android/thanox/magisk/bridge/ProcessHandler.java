package github.tornaco.android.thanox.magisk.bridge;

public interface ProcessHandler {
    void onSystemServerProcess();

    void onAppProcess(String processName);
}
