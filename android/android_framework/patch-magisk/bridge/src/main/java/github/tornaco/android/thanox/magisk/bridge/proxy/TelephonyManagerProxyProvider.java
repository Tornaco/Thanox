package github.tornaco.android.thanox.magisk.bridge.proxy;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.app.ThanosManagerNative;
import github.tornaco.android.thanos.core.secure.IPrivacyManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.core.util.function.Function;

public class TelephonyManagerProxyProvider {

    public static TelephonyManager provide(Context context) {
        return new TelephonyManagerProxy(context);
    }

    @SuppressLint("HardwareIds")
    private static final class TelephonyManagerProxy extends TelephonyManager {
        private final String opPkgName;

        private TelephonyManagerProxy(Context context) {
            super(context);
            this.opPkgName = context.getOpPackageName();
        }

        @Override
        public String getDeviceId() {
            String superRes = super.getDeviceId();
            return getHookedDeviceIdOr(superRes);
        }

        @Override
        public String getDeviceId(int slotIndex) {
            String superRes = super.getDeviceId(slotIndex);
            return getHookedDeviceIdOr(superRes);
        }

        @Override
        public String getLine1Number() {
            String superRes = super.getLine1Number();
            return getHookedLine1NumberOr(superRes);
        }

        @Override
        public String getLine1Number(int subId) {
            String superRes = super.getLine1Number(subId);
            return getHookedLine1NumberOr(superRes);
        }

        @Override
        public String getLine1AlphaTag() {
            return super.getLine1AlphaTag();
        }

        @Override
        public String getLine1AlphaTag(int subId) {
            return super.getLine1AlphaTag(subId);
        }

        @Override
        public String getImei() {
            String superRes = super.getImei();
            return getHookedImeiOr(getSlotIndex(), superRes);
        }

        @Override
        public String getImei(int slotIndex) {
            String superRes = super.getImei(slotIndex);
            return getHookedImeiOr(slotIndex, superRes);
        }

        @Override
        public String getSimOperator() {
            return getHookSimOperatorOr(super.getSimOperator());
        }

        @Override
        public String getSimOperator(int subId) {
            return getHookSimOperatorOr(super.getSimOperator(subId));
        }

        @Override
        public String getSimOperatorName() {
            return getHookSimOperatorNameOr(super.getSimOperatorName());
        }

        @Override
        public String getSimOperatorName(int subId) {
            return getHookSimOperatorNameOr(super.getSimOperatorName(subId));
        }

        @Override
        public String getSimCountryIso() {
            return getHookedSimCountryIsoOr(super.getSimCountryIso());
        }

        @Override
        public String getNetworkCountryIso() {
            return getHookedSimCountryIsoOr(super.getNetworkCountryIso());
        }

        @Override
        public String getNetworkCountryIso(int slotIndex) {
            return getHookedSimCountryIsoOr(super.getNetworkCountryIso(slotIndex));
        }

        @Override
        public String getNetworkOperatorName() {
            return getHookSimOperatorNameOr(super.getNetworkOperatorName());
        }

        @Override
        public String getNetworkOperatorName(int subId) {
            return getHookSimOperatorNameOr(super.getNetworkOperatorName(subId));
        }

        @Override
        public String getNetworkOperator() {
            return getHookSimOperatorOr(super.getNetworkOperator());
        }

        @Override
        public String getNetworkOperator(int subId) {
            return getHookSimOperatorOr(super.getNetworkOperator(subId));
        }

        @Override
        public String getNetworkOperatorForPhone(int phoneId) {
            return getHookSimOperatorOr(super.getNetworkOperatorForPhone(phoneId));
        }

        private String getHookedDeviceIdOr(String elseValue) {
            return getHookFieldProfileOr(PrivacyManager.PrivacyOp.OP_DEVICE_ID,
                    Fields::getDeviceId, elseValue);
        }

        private String getHookedLine1NumberOr(String elseValue) {
            return getHookFieldProfileOr(PrivacyManager.PrivacyOp.OP_LINE1NUM,
                    Fields::getLine1Number, elseValue);
        }

        private String getHookedImeiOr(int slotIndex, String elseValue) {
            return getHookFieldProfileOr(PrivacyManager.PrivacyOp.OP_IMEI, input -> input.getImei(slotIndex), elseValue);
        }

        private String getHookedSimCountryIsoOr(String elseValue) {
            return getHookFieldProfileOr(PrivacyManager.PrivacyOp.OP_SIM_CONTRY_ISO, Fields::getSimCountryIso, elseValue);
        }

        private String getHookSimOperatorNameOr(String elseValue) {
            return getHookFieldProfileOr(PrivacyManager.PrivacyOp.OP_OPERATOR_NAME, Fields::getNetOperatorName, elseValue);
        }

        private String getHookSimOperatorOr(String elseValue) {
            return getHookFieldProfileOr(PrivacyManager.PrivacyOp.OP_OPERATOR, Fields::getNetOperator, elseValue);
        }

        private <T> T getHookFieldProfileOr(int privacyOp, Function<Fields, T> mapper, T orElseValue) {
            try {
                logging("getHookFieldProfileOr %s %s", privacyOp, opPkgName);

                String callPackageName = opPkgName;
                if (callPackageName == null) {
                    logging("getHookFieldProfileOr, callPackageName == null");
                    return orElseValue;
                }

                IThanos thanos = ThanosManagerNative.getDefault();
                if (thanos == null) {
                    logging("getHookFieldProfileOr, thanos == null");
                    return orElseValue;
                }
                IPrivacyManager priv = thanos.getPrivacyManager();
                if (priv == null) {
                    logging("getHookFieldProfileOr, priv == null");
                    return orElseValue;
                }
                if (!priv.isPrivacyEnabled()) {
                    logging("getHookFieldProfileOr, !isPrivacyEnabled");
                    return orElseValue;
                }

                boolean enabledUid = priv.isPackageFieldsProfileSelected(callPackageName);
                if (!enabledUid) {
                    logging("getHookFieldProfileOr, !enabledUid");
                    return orElseValue;
                }

                // Skip if not set.
                Fields f = priv
                        .getSelectedFieldsProfileForPackage(callPackageName, privacyOp);
                if (f == null) {
                    logging("getHookFieldProfileOr, selected f is null");
                    return orElseValue;
                }
                T res = mapper.apply(f);
                if (res == null) {
                    logging("getHookFieldProfileOr, mapper.apply is null");
                    return orElseValue;
                }
                logging("getHookFieldProfileOr, return cheat value %s %s %s", privacyOp, opPkgName, res);
                return res;
            } catch (Throwable e) {
                XLog.e("getHookFieldProfileOr error", e);
                return orElseValue;
            }
        }
    }

}
