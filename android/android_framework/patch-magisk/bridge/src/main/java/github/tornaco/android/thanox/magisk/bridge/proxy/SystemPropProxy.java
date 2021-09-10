package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.os.RemoteException;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.core.util.AppUtils;

@Deprecated
public class SystemPropProxy {
    public static String getProp(String key) throws RemoteException {
        String callerPkg = AppUtils.currentProcessName();
        XLog.d("SystemPropProxy getProp %s %s", callerPkg, key);

        IThanos thanos = ThanosManagerNative.getDefault();
        if (thanos == null) {
            return null;
        }
        IPrivacyManager priv = thanos.getPrivacyManager();
        if (priv == null) {
            return null;
        }

        boolean selected = priv.isPackageFieldsProfileSelected(callerPkg);
        if (!selected) {
            return null;
        }

        if ("gsm.sim.operator.iso-country".equals(key)) {
            Fields f = priv
                    .getSelectedFieldsProfileForPackage(callerPkg, PrivacyManager.PrivacyOp.OP_SIM_CONTRY_ISO);
            if (f == null) {
                return null;
            }
            return composeTelephonyPropValuesWithPhoneCount(f.getSimCountryIso());
        } else if ("gsm.operator.numeric".equals(key)) {
            Fields f = priv
                    .getSelectedFieldsProfileForPackage(callerPkg, PrivacyManager.PrivacyOp.OP_SIM_OPERATOR);
            if (f == null) {
                return null;
            }
            return composeTelephonyPropValuesWithPhoneCount(f.getNetOperator());
        } else if ("gsm.operator.alpha".equals(key)) {
            Fields f = priv
                    .getSelectedFieldsProfileForPackage(callerPkg, PrivacyManager.PrivacyOp.OP_SIM_OPERATOR_NAME);
            if (f == null) {
                return null;
            }
            return composeTelephonyPropValuesWithPhoneCount(f.getNetOperatorName());
        }
        return null;
    }

    private static String composeTelephonyPropValuesWithPhoneCount(String value) {
        // Assume has 2 phone.
        return value + "," + value;
    }
}
