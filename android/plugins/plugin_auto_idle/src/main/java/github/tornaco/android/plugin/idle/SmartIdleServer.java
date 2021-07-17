package github.tornaco.android.plugin.idle;

import android.content.Context;
import android.os.RemoteException;

import com.elvishew.xlog.XLog;

public class SmartIdleServer extends ISmartIdleManager.Stub {
    private Context context;

    public void systemReady(Context context) {
        XLog.d("SmartIdleServer comes up with context: %s", context);
    }

    @Override
    public void setEnabled(boolean enable) throws RemoteException {

    }

    @Override
    public boolean isEnabled() throws RemoteException {
        return false;
    }
}
