package github.tornaco.android.thanos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;
import com.topjohnwu.superuser.Shell;

import java.util.Arrays;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.su.ISu;
import github.tornaco.android.thanos.core.su.SuRes;

@Keep
public class SuSupportService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ISu.Stub() {
            @Override
            public SuRes exe(String[] command) {
                String customCommand = ThanosManager
                        .from(getApplicationContext())
                        .getProfileManager()
                        .getCustomSuCommand();
                if (!TextUtils.isEmpty(customCommand)) {
                    Shell shell = Shell.newInstance(customCommand);
                    Shell.Result result = shell.newJob().add(command).exec();
                    XLog.w("Custom SuSupportService, su: %s, exe: %s, result: %s", customCommand, Arrays.toString(command), result);
                    try {
                        shell.waitAndClose();
                    } catch (Throwable e) {
                        XLog.e("Shell close error", e);
                    }
                    return new SuRes(result.getOut(), result.getErr(), result.getCode());
                } else {
                    Shell.Result result = Shell.su(command).exec();
                    XLog.w("SuSupportService exe: %s, result: %s", Arrays.toString(command), result);
                    return new SuRes(result.getOut(), result.getErr(), result.getCode());
                }
            }
        };
    }
}
