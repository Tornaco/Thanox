package github.tornaco.android.thanos.privacy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.common.AppItemViewClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonFuncToggleAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.core.util.function.Function;
import github.tornaco.android.thanos.core.util.function.Predicate;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.QuickDropdown;
import util.CollectionUtils;
import util.Consumer;

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
                        new AppItemViewClickListener() {
                            @Override
                            public void onAppItemClick(AppInfo appInfo, View itemView) {
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
                                        new Function<Integer, String>() {
                                            @Override
                                            public String apply(Integer index) {
                                                if (index + 1 > fields.size()) {
                                                    return null;
                                                }
                                                Fields f = fields.get(index);
                                                return f.getLabel();
                                            }
                                        },
                                        new Consumer<Integer>() {
                                            @Override
                                            public void accept(Integer id) {
                                                Fields f = fields.get(id);
                                                boolean isDummyNoop = f.getId() == null;
                                                ThanosManager.from(getApplicationContext())
                                                        .getPrivacyManager()
                                                        .selectFieldsProfileForPackage(
                                                                appInfo.getPkgName(), isDummyNoop ? null : f.getId());

                                                appListFilterAdapter.updateSingleItem(
                                                        new Predicate<AppListModel>() {
                                                            @Override
                                                            public boolean test(AppListModel input) {
                                                                // Find by pkg and update badge.
                                                                if (input.appInfo.getPkgName().equals(appInfo.getPkgName())) {
                                                                    input.badge = isDummyNoop ? null : f.getLabel();
                                                                    input.appInfo.setObj(isDummyNoop ? null : f);
                                                                    return true;
                                                                }
                                                                return false;
                                                            }
                                                        });
                                            }
                                        });
                            }
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
    protected void onSwitchBarCheckChanged(Switch switchBar, boolean isChecked) {
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
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(getApplicationContext())) {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.module_donate_donated_available,
                        Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
            CheatRecordViewerActivity.start(this);
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
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .setItems(
                            labels.toArray(new String[0]),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Fields f = allFs.get(which);
                                    boolean isDummyNoop = f.getId() == null;
                                    applyBatchSelection(isDummyNoop ? null : f.getId());
                                    dialog.dismiss();
                                }
                            })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void applyBatchSelection(String id) {
        PrivacyManager priv = ThanosManager.from(thisActivity()).getPrivacyManager();
        ProgressDialog p = new ProgressDialog(thisActivity());
        p.setTitle(R.string.common_menu_title_batch_select);
        p.show();
        CollectionUtils.consumeRemaining(
                appListFilterAdapter.getListModels(),
                new Consumer<AppListModel>() {
                    @Override
                    public void accept(AppListModel appListModel) {
                        priv.selectFieldsProfileForPackage(appListModel.appInfo.getPkgName(), id);
                    }
                });
        viewModel.start();
        p.dismiss();
    }
}
