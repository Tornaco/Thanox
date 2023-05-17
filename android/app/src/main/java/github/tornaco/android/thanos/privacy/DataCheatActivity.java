package github.tornaco.android.thanos.privacy;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.XposedScope;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.android.thanos.widget.QuickDropdown;
import util.CollectionUtils;

public class DataCheatActivity extends CommonAppListFilterActivity {
    private CommonAppListFilterAdapter appListFilterAdapter;

    @Verify
    public static void start(Context context) {
        ActivityUtils.startActivity(context, DataCheatActivity.class);
    }

    @NonNull
    @Override
    @Verify
    protected String getTitleString() {
        return getString(R.string.activity_title_data_cheat);
    }

    @Nullable
    @Override
    protected String provideFeatureDescText() {
        return getString(R.string.feature_desc_data_cheat);
    }

    @Override
    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        appListFilterAdapter =
                new CommonAppListFilterAdapter(
                        (appInfo, itemView) -> {
                            String noSelectionStr = getString(R.string.common_text_value_not_set);
                            List<Fields> fields =
                                    ThanosManager.from(getApplicationContext())
                                            .getPrivacyManager()
                                            .getAllFieldsProfiles();
                            Fields dummyNoop = Fields.builder().label(noSelectionStr).id(null).build();
                            fields.add(dummyNoop);
                            QuickDropdown.show(
                                    thisActivity(),
                                    itemView,
                                    index -> {
                                        if (index + 1 > fields.size()) {
                                            return null;
                                        }
                                        Fields f = fields.get(index);
                                        return f.getLabel();
                                    },
                                    id -> {
                                        Fields f = fields.get(id);
                                        boolean isDummyNoop = f.getId() == null;
                                        ThanosManager.from(getApplicationContext())
                                                .getPrivacyManager()
                                                .selectFieldsProfileForPackage(
                                                        appInfo.getPkgName(), isDummyNoop ? null : f.getId());
                                        XposedScope.INSTANCE.requestOrRemoveScope(getApplicationContext(), Pkg.fromAppInfo(appInfo));

                                        appListFilterAdapter.updateSingleItem(
                                                input -> {
                                                    // Find by pkg and update badge.
                                                    if (input.appInfo.getPkgName().equals(appInfo.getPkgName())) {
                                                        input.badge = isDummyNoop ? null : f.getLabel();
                                                        input.appInfo.setObj(isDummyNoop ? null : f);
                                                        return true;
                                                    }
                                                    return false;
                                                });
                                    });
                        });
        return appListFilterAdapter;
    }

    @NonNull
    @Override
    protected CommonFuncToggleAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new DataCheatAppsLoader(this.getApplicationContext());
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(this).isServiceInstalled()
                && ThanosManager.from(this).getPrivacyManager().isPrivacyEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(com.google.android.material.materialswitch.MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(this).getPrivacyManager().setPrivacyEnabled(isChecked);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FieldsTemplateListActivity.RESULT_DEL) {
            viewModel.start();
        }
    }

    @Override
    @Verify
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_cheat_menu, menu);
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            FieldsTemplateListActivity.start(thisActivity(), 10086);
            return true;
        }
        if (R.id.action_cheat_record == item.getItemId()) {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(thisActivity(), isSubscribed -> {
                if (isSubscribed) {
                    CheatRecordViewerActivity.start(this);
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(thisActivity());
                }
                return null;
            });
            return true;
        }

        if (R.id.action_batch_select == item.getItemId()) {
            List<Fields> allFs =
                    ThanosManager.from(thisActivity()).getPrivacyManager().getAllFieldsProfiles();
            String noSelectionStr = getString(R.string.common_text_value_not_set);
            Fields dummyNoop = Fields.builder().label(noSelectionStr).id(null).build();
            allFs.add(dummyNoop);
            List<String> labels = CollectionUtils.mappingAsString(allFs, Fields::getLabel);

            new MaterialAlertDialogBuilder(thisActivity(), R.style.DialogThemeCommon)
                    .setTitle(R.string.common_menu_title_batch_select)
                    .setCancelable(true)
                    .setNegativeButton(
                            android.R.string.cancel,
                            (dialog, which) -> dialog.dismiss())
                    .setItems(
                            labels.toArray(new String[0]),
                            (dialog, which) -> {
                                Fields f = allFs.get(which);
                                boolean isDummyNoop = f.getId() == null;
                                applyBatchSelection(isDummyNoop ? null : f.getId());
                                dialog.dismiss();
                            })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void applyBatchSelection(String id) {
        PrivacyManager priv = ThanosManager.from(thisActivity()).getPrivacyManager();
        ModernProgressDialog p = new ModernProgressDialog(thisActivity());
        p.setTitle(R.string.common_menu_title_batch_select);
        p.show();
        CollectionUtils.consumeRemaining(
                appListFilterAdapter.getListModels(),
                appListModel -> priv.selectFieldsProfileForPackage(appListModel.appInfo.getPkgName(), id));
        viewModel.start();
        p.dismiss();
    }
}
