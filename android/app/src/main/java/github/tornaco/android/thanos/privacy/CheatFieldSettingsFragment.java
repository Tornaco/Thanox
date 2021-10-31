package github.tornaco.android.thanos.privacy;

import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.Objects;

import github.tornaco.android.thanos.BaseWithFabPreferenceFragmentCompat;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import github.tornaco.android.thanos.core.util.Optional;
import lang3.RandomStringUtils;

public class CheatFieldSettingsFragment extends BaseWithFabPreferenceFragmentCompat {
    private String fieldId;

    public static CheatFieldSettingsFragment newInstance(String fieldId) {
        Bundle data = new Bundle();
        data.putString("id", fieldId);
        CheatFieldSettingsFragment f = new CheatFieldSettingsFragment();
        f.setArguments(data);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fieldId = Objects.requireNonNull(getArguments()).getString("id");
        // Update title.
        if (!TextUtils.isEmpty(fieldId)) {
            Fields f = ThanosManager.from(getContext()).getPrivacyManager().getFieldsProfileById(fieldId);
            if (f != null) {
                Objects.requireNonNull(getActivity()).setTitle(f.getLabel());
            }
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.cheat_field_pref, rootKey);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cheat_field_settings_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_fill_all_random == item.getItemId()) {
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.cheat_field_auto_gen)
                    .setMessage(R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(
                            android.R.string.ok,
                            (dialog, which) -> {
                                genForAllFields();
                                onBindPreferences();
                            })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void genForAllFields() {
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            return;
        }

        new AndroidId(null, false).updateValue(RandomStringUtils.randomAlphanumeric(12));
        new DeviceId(null, false).updateValue(RandomStringUtils.randomAlphanumeric(12));

        new Line1Num(null, false).updateValue(RandomStringUtils.randomNumeric(11));
        new SimSerial(null, false).updateValue(RandomStringUtils.randomAlphanumeric(16));

        new SimCountryIso(null, false).updateValue("us");
        new SimOp(null, false, -1).updateValue(RandomStringUtils.randomNumeric(5));
        new SimOpName(null, false, -1).updateValue(RandomStringUtils.randomAlphabetic(6));

        new NetCountryIso(null, false).updateValue("jp");
        new NetOp(null, false, -1).updateValue(RandomStringUtils.randomNumeric(5));
        new NetOpName(null, false, -1).updateValue(RandomStringUtils.randomAlphabetic(6));

        int phoneCount = thanos.getPrivacyManager().getPhoneCount();
        XLog.w("genForAllFields phoneCount: %s", phoneCount);

        SubscriptionInfo[] subscriptionInfos =
                thanos.getPrivacyManager().getAccessibleSubscriptionInfoList();
        XLog.w("genForAllFields subscriptionInfos: %s", Arrays.toString(subscriptionInfos));
        if (ArrayUtils.isEmpty(subscriptionInfos)) {
            return;
        }
        // Bind cheat imei.
        if (thanos.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_IMEI)) {
            int nameIndex = 1;
            for (SubscriptionInfo sub : subscriptionInfos) {
                int slotId = sub.getSimSlotIndex();
                XLog.w("genForAllFields nameIndex: %s, slotId: %s", nameIndex, slotId);
                new Imei(null, false, slotId).updateValue(RandomStringUtils.randomAlphanumeric(16));
                nameIndex++;
            }
        }

        // Bind cheat meid.
        if (thanos.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_MEID)) {
            int nameIndex = 1;
            for (SubscriptionInfo sub : subscriptionInfos) {
                int slotId = sub.getSimSlotIndex();
                XLog.w("genForAllFields nameIndex: %s, slotId: %s", nameIndex, slotId);
                new Meid(null, false, slotId).updateValue(RandomStringUtils.randomAlphanumeric(16));
                nameIndex++;
            }
        }
    }

    @Override
    protected void onBindPreferences() {
        super.onBindPreferences();
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            getPreferenceScreen().setEnabled(false);
            return;
        }

        // Cheats.
        new Line1Num(getString(R.string.key_cheat_field_line1_num), false).bind();
        new SimSerial(getString(R.string.key_cheat_field_sim_serial), false).bind();
        new SimCountryIso(getString(R.string.key_cheat_field_sim_country_iso), false).bind();
        new NetCountryIso(getString(R.string.key_cheat_field_net_country_iso), false).bind();
        new DeviceId(getString(R.string.key_cheat_field_device_id), false).bind();
        new AndroidId(getString(R.string.key_cheat_field_android_id), false).bind();

        // Orig.
        new Line1Num(getString(R.string.key_original_field_line1_num), true).bind();
        new SimSerial(getString(R.string.key_original_field_sim_serial), true).bind();
        new SimCountryIso(getString(R.string.key_original_field_sim_country_iso), true).bind();
        new NetCountryIso(getString(R.string.key_original_field_net_country_iso), true).bind();
        new DeviceId(getString(R.string.key_original_field_device_id), true).bind();
        new AndroidId(getString(R.string.key_original_field_android_id), true).bind();

        bindImeiAndMeid();
        bindShowN();
        bindNetworkOp();
    }

    private void bindNetworkOp() {
        // Cheat.
        new NetOp(getString(R.string.key_cheat_field_net_op), false, -1).bind();
        new NetOpName(getString(R.string.key_cheat_field_net_op_name), false, -1).bind();

        new SimOp(getString(R.string.key_cheat_field_sim_op), false, -1).bind();
        new SimOpName(getString(R.string.key_cheat_field_sim_op_name), false, -1).bind();

        // Orig.
        ThanosManager thanos = ThanosManager.from(getContext());
        int phoneCount = thanos.getPrivacyManager().getPhoneCount();
        XLog.w("phoneCount: %s", phoneCount);

        SubscriptionInfo[] subscriptionInfos =
                thanos.getPrivacyManager().getAccessibleSubscriptionInfoList();
        XLog.w("subscriptionInfos: %s", Arrays.toString(subscriptionInfos));
        if (ArrayUtils.isEmpty(subscriptionInfos)) {
            return;
        }
        int nameIndex = 1;
        for (SubscriptionInfo info : subscriptionInfos) {
            int subId = info.getSubscriptionId();
            new NetOp(getString(R.string.key_original_field_net_op_) + nameIndex, true, subId).bind();
            new NetOpName(getString(R.string.key_original_field_net_op_name_) + nameIndex, true, subId)
                    .bind();

            new SimOp(getString(R.string.key_original_field_sim_op_) + nameIndex, true, subId).bind();
            new SimOpName(getString(R.string.key_original_field_sim_op_name_) + nameIndex, true, subId)
                    .bind();
            nameIndex++;
        }
    }

    private void bindShowN() {
        SwitchPreferenceCompat showNotificationPref =
                findPreference(getString(R.string.key_cheat_field_show_cheated_app_notifications));
        Objects.requireNonNull(showNotificationPref).setChecked(getFieldsById().isShowN());
        showNotificationPref.setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    boolean checked = (boolean) newValue;
                    Fields newF = getFieldsById();
                    if (newF == null) {
                        return false;
                    }
                    newF.setShowN(checked);
                    ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(newF);
                    return true;
                });
    }

    private void bindImeiAndMeid() {
        ThanosManager thanos = ThanosManager.from(getContext());
        if (!thanos.isServiceInstalled()) {
            return;
        }

        int phoneCount = thanos.getPrivacyManager().getPhoneCount();
        XLog.w("phoneCount: %s", phoneCount);

        SubscriptionInfo[] subscriptionInfos =
                thanos.getPrivacyManager().getAccessibleSubscriptionInfoList();
        XLog.w("subscriptionInfos: %s", Arrays.toString(subscriptionInfos));
        if (ArrayUtils.isEmpty(subscriptionInfos)) {
            return;
        }

        // Bind cheat imei.
        if (thanos.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_IMEI)) {
            int nameIndex = 1;
            for (SubscriptionInfo sub : subscriptionInfos) {
                int slotId = sub.getSimSlotIndex();
                XLog.w("nameIndex: %s, slotId: %s", nameIndex, slotId);
                new Imei(getString(R.string.key_cheat_field_imei_slot_) + nameIndex, false, slotId).bind();
                new Imei(getString(R.string.key_original_field_imei_slot_) + nameIndex, true, slotId)
                        .bind();

                nameIndex++;
            }
        }

        // Bind cheat meid.
        if (thanos.hasFeature(BuildProp.THANOX_FEATURE_PRIVACY_FIELD_MEID)) {
            int nameIndex = 1;
            for (SubscriptionInfo sub : subscriptionInfos) {
                int slotId = sub.getSimSlotIndex();
                XLog.w("nameIndex: %s, slotId: %s", nameIndex, slotId);
                new Meid(getString(R.string.key_cheat_field_meid_slot_) + nameIndex, false, slotId).bind();
                new Meid(getString(R.string.key_original_field_meid_slot_) + nameIndex, true, slotId)
                        .bind();

                nameIndex++;
            }
        }
    }

    class Line1Num extends FieldPrefHandler {

        Line1Num(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getLine1Number();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setLine1Number(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalLine1Number();
        }
    }

    class SimSerial extends FieldPrefHandler {

        SimSerial(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getSimSerial();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setSimSerial(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalSimSerialNumber();
        }
    }

    class SimCountryIso extends FieldPrefHandler {

        SimCountryIso(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getSimCountryIso();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setSimCountryIso(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalSimCountryIso();
        }
    }

    class NetCountryIso extends FieldPrefHandler {

        NetCountryIso(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getNetCountryIso();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setNetCountryIso(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalNetworkCountryIso();
        }
    }

    class NetOp extends FieldPrefHandler {
        private int subId;

        NetOp(String key, boolean isOriginal, int subId) {
            super(key, isOriginal);
            this.subId = subId;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getNetOperator();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setNetOperator(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalNetworkOp(subId);
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    class SimOp extends FieldPrefHandler {
        private int subId;

        SimOp(String key, boolean isOriginal, int subId) {
            super(key, isOriginal);
            this.subId = subId;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getSimOperator();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setSimOperator(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalSimOp(subId);
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    class NetOpName extends FieldPrefHandler {
        private int subId;

        NetOpName(String key, boolean isOriginal, int subId) {
            super(key, isOriginal);
            this.subId = subId;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getNetOperatorName();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setNetOperatorName(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalNetworkOpName(subId);
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    class SimOpName extends FieldPrefHandler {
        private int subId;

        SimOpName(String key, boolean isOriginal, int subId) {
            super(key, isOriginal);
            this.subId = subId;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getSimOperatorName();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setSimOperatorName(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalSimOpName(subId);
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    class DeviceId extends FieldPrefHandler {

        DeviceId(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getDeviceId();
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalDeviceId();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setDeviceId(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }
    }

    class AndroidId extends FieldPrefHandler {

        AndroidId(String key, boolean isOriginal) {
            super(key, isOriginal);
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            return f == null ? null : f.getAndroidId();
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalAndroidId();
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setAndroidId(value.trim());
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }
    }

    class Imei extends FieldPrefHandler {
        private final int slotIndex;

        Imei(String key, boolean isOriginal, int slotIndex) {
            super(key, isOriginal);
            this.slotIndex = slotIndex;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            if (f == null) return null;
            return f.getImei(slotIndex);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalImei(slotIndex);
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setImei(slotIndex, value);
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    class Meid extends FieldPrefHandler {
        private final int slotIndex;

        Meid(String key, boolean isOriginal, int slotIndex) {
            super(key, isOriginal);
            this.slotIndex = slotIndex;
        }

        @Nullable
        @Override
        String getCurrentCheatValue() {
            Fields f = getFieldsById();
            if (f == null) return null;
            return f.getMeid(slotIndex);
        }

        @Nullable
        @Override
        String getOriginalValue() {
            return ThanosManager.from(getContext()).getPrivacyManager().getOriginalMeid(slotIndex);
        }

        @Override
        boolean updateValue(@NonNull String value) {
            Fields f = getFieldsById();
            f.setMeid(slotIndex, value);
            return ThanosManager.from(getContext()).getPrivacyManager().addOrUpdateFieldsProfile(f);
        }

        @Override
        void onBind(EditTextPreference editTextPreference) {
            editTextPreference.setVisible(true);
            super.onBind(editTextPreference);
        }
    }

    abstract class FieldPrefHandler {
        private String key;
        private boolean isOriginal;

        public FieldPrefHandler(String key, boolean isOriginal) {
            this.key = key;
            this.isOriginal = isOriginal;
        }

        void bind() {
            Optional.ofNullable((EditTextPreference) findPreference(key)).ifPresent(this::onBind);
        }

        void onBind(EditTextPreference editTextPreference) {
            String currentValue = getCurrentCheatValue();
            editTextPreference.setSummary(
                    isOriginal
                            ? getOriginalValue()
                            : (TextUtils.isEmpty(currentValue)
                            ? getString(R.string.pre_title_cheat_not_set)
                            : currentValue));
            if (!isOriginal) {
                editTextPreference.setOnBindEditTextListener(
                        editText -> editText.setHint(getCurrentCheatValue()));
                editTextPreference.setOnPreferenceChangeListener(
                        (preference, newValue) -> {
                            boolean res = updateValue(newValue.toString());
                            if (res) {
                                String newCurrentValue = getCurrentCheatValue();
                                editTextPreference.setSummary(
                                        isOriginal
                                                ? getOriginalValue()
                                                : (TextUtils.isEmpty(newCurrentValue)
                                                ? getString(R.string.pre_title_cheat_not_set)
                                                : newCurrentValue));
                            }
                            return res;
                        });
            } else {
                editTextPreference.setEnabled(false);
            }
        }

        @Nullable
        abstract String getCurrentCheatValue();

        @Nullable
        abstract String getOriginalValue();

        abstract boolean updateValue(@NonNull String value);
    }

    protected Fields getFieldsById() {
        return ThanosManager.from(getContext()).getPrivacyManager().getFieldsProfileById(fieldId);
    }
}
