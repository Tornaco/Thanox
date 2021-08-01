package github.tornaco.android.thanos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;
import com.topjohnwu.superuser.Shell;

import java.util.Arrays;

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
                Shell.Result result = Shell.su(command).exec();
                XLog.w("SuSupportService exe: %s, result: %s", Arrays.toString(command), result);
                return new SuRes(result.getOut(), result.getErr(), result.getCode());
            }
        };
    }
}
