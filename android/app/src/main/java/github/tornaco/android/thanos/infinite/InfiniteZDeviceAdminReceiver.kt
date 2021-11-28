package github.tornaco.android.thanos.infinite

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import com.elvishew.xlog.XLog

class InfiniteZDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        XLog.w("InfiniteZDeviceAdminReceiver, onEnabled")
    }

    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        super.onProfileProvisioningComplete(context, intent)
        XLog.w("InfiniteZDeviceAdminReceiver, onProfileProvisioningComplete")
    }
}