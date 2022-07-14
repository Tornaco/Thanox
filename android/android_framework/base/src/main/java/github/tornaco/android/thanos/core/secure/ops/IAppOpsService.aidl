package github.tornaco.android.thanos.core.secure.ops;

import android.os.Bundle;
import github.tornaco.android.thanos.core.IPrinter;
import github.tornaco.android.thanos.core.secure.ops.SettingsAccessRecord;

interface IAppOpsService {
    void setMode(int code, int uid, String packageName, int mode);

    void resetAllModes(String reqPackageName);

    int checkOperation(int code, int uid, String packageName);

    boolean isOpsEnabled();
    void setOpsEnabled(boolean enabled);

    void onStartOp(in IBinder token, int code, int uid, String packageName);
    void onFinishOp(in IBinder token, int code, int uid, String packageName);

    void setOpRemindEnable(int code, boolean enable);
    boolean isOpRemindEnabled(int code);

    void setPkgOpRemindEnable(String pkg, boolean enable);
    boolean isPkgOpRemindEnable(String pkg);

    int checkOperationNonCheck(int code, int uid, String packageName);

    void dump(in IPrinter p);

    void onSettingsGetString(String name, String value, String callerPackageName);
    void onSettingsPutString(String name, String value, String callerPackageName);

    List<SettingsAccessRecord> getSettingsReadRecords(String filterCallerPackageName /** nullable */);
    List<SettingsAccessRecord> getSettingsWriteRecords(String filterCallerPackageName /** nullable */);

    void clearSettingsReadRecords();
    void clearSettingsWriteRecords();

    void setSettingsRecordEnabled(boolean enable);
    boolean isSettingsRecordEnabled();
}
