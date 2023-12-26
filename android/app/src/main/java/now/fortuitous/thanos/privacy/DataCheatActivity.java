/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.privacy;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;


import github.tornaco.android.thanos.R;
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
import now.fortuitous.thanos.XposedScope;
import util.CollectionUtils;

public class DataCheatActivity extends CommonAppListFilterActivity {
    private CommonAppListFilterAdapter appListFilterAdapter;


    public static void start(Context context) {
        ActivityUtils.startActivity(context, DataCheatActivity.class);
    }

    @NonNull
    @Override

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
                            String noSelectionStr = getString(github.tornaco.android.thanos.module.common.R.string.common_text_value_not_set);
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

    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.data_cheat_menu, menu);
    }

    @Override

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
            String noSelectionStr = getString(github.tornaco.android.thanos.module.common.R.string.common_text_value_not_set);
            Fields dummyNoop = Fields.builder().label(noSelectionStr).id(null).build();
            allFs.add(dummyNoop);
            List<String> labels = CollectionUtils.mappingAsString(allFs, Fields::getLabel);

            new MaterialAlertDialogBuilder(thisActivity(), github.tornaco.android.thanos.module.common.R.style.DialogThemeCommon)
                    .setTitle(github.tornaco.android.thanos.module.common.R.string.common_menu_title_batch_select)
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
        p.setTitle(github.tornaco.android.thanos.module.common.R.string.common_menu_title_batch_select);
        p.show();
        CollectionUtils.consumeRemaining(
                appListFilterAdapter.getListModels(),
                appListModel -> priv.selectFieldsProfileForPackage(appListModel.appInfo.getPkgName(), id));
        viewModel.start();
        p.dismiss();
    }
}
