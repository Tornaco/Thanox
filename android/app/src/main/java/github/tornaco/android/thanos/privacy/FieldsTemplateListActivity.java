package github.tornaco.android.thanos.privacy;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemViewClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.field.Fields;
import github.tornaco.android.thanos.core.util.function.Function;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.EditTextDialog;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;
import util.Consumer;

public class FieldsTemplateListActivity extends CommonAppListFilterActivity {
    public static final int RESULT_DEL = 10086;
    private boolean changed;

    public static void start(Activity activity, int reqCode) {
        ActivityUtils.startActivityForResult(activity, FieldsTemplateListActivity.class, reqCode);
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }

    @Override
    protected void onSetupFilter(Chip filterAnchor) {
        super.onSetupFilter(filterAnchor);
        filterAnchor.setVisibility(View.GONE);
        setTitle(getTitleRes());
    }

    @Override
    protected int getTitleRes() {
        return R.string.priv_title_fields_template;
    }

    @Override
    protected void onSetupFab(ExtendedFloatingActionButton fab) {
        fab.setText(null);
        fab.setIconResource(R.drawable.module_common_ic_add_fill);
        fab.show();
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRequestAddTemplate();
                    }
                });
    }

    private void onRequestAddTemplate() {
        EditTextDialog.show(
                thisActivity(),
                getString(R.string.pref_action_create_new_config_template),
                new Consumer<String>() {
                    @Override
                    public void accept(String content) {
                        if (TextUtils.isEmpty(content)) {
                            return;
                        }
                        String uuid = UUID.randomUUID().toString();
                        Fields f =
                                Fields.builder()
                                        .label(content)
                                        .id(uuid)
                                        .createAt(System.currentTimeMillis())
                                        .build();
                        boolean added =
                                ThanosManager.from(thisActivity()).getPrivacyManager().addOrUpdateFieldsProfile(f);
                        if (added) {
                            // Reload.
                            ToastUtils.ok(thisActivity());
                            CheatFieldSettingsActivity.start(thisActivity(), f.getId(), 10086);
                        } else {
                            ToastUtils.nook(thisActivity());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.start();
    }

    @Override
    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        return new CommonAppListFilterAdapter(
                new AppItemViewClickListener() {
                    @Override
                    public void onAppItemClick(AppInfo appInfo, View itemView) {
                        QuickDropdown.show(
                                thisActivity(),
                                itemView,
                                new Function<Integer, String>() {
                                    @Override
                                    public String apply(Integer index) {
                                        switch (index) {
                                            case 0:
                                                return getString(R.string.pref_action_edit_or_view_config_template);
                                            case 1:
                                                return getString(R.string.pref_action_delete_config_template);
                                        }
                                        return null;
                                    }
                                },
                                new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer id) {
                                        switch (id) {
                                            case 0:
                                                CheatFieldSettingsActivity.start(
                                                        thisActivity(), (String) appInfo.getObj(), 10086);
                                                break;
                                            case 1:
                                                ThanosManager.from(thisActivity())
                                                        .getPrivacyManager()
                                                        .deleteFieldsProfileById((String) appInfo.getObj());
                                                viewModel.start();
                                                changed = true;
                                                break;
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (changed) {
            setResult(RESULT_DEL);
        }
        super.onBackPressed();
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new CommonAppListFilterViewModel.ListModelLoader() {
            @Override
            public List<AppListModel> load(@NonNull CategoryIndex index) {
                ThanosManager thanos = ThanosManager.from(getApplicationContext());
                if (!thanos.isServiceInstalled()) return Lists.newArrayListWithCapacity(0);
                PrivacyManager priv = thanos.getPrivacyManager();
                List<Fields> fields = priv.getAllFieldsProfiles();
                List<AppListModel> res = new ArrayList<>();
                CollectionUtils.consumeRemaining(
                        fields,
                        f -> {
                            AppInfo appInfo = new AppInfo();
                            appInfo.setPkgName(f.getId());
                            appInfo.setObj(f.getId());
                            appInfo.setIconDrawable(R.drawable.module_common_ic_nothing);
                            appInfo.setAppLabel(f.getLabel());
                            int usage = priv.getUsageForFieldsProfile(f.getId());
                            appInfo.setArg1(usage);
                            res.add(
                                    new AppListModel(
                                            appInfo,
                                            null,
                                            null,
                                            usage == 0
                                                    ? getString(R.string.priv_title_fields_usage_count_noop)
                                                    : getString(
                                                    R.string.priv_title_fields_usage_count, String.valueOf(usage))));
                        });
                // Sort by usage.
                Collections.sort(
                        res,
                        new Comparator<AppListModel>() {
                            @Override
                            public int compare(AppListModel o1, AppListModel o2) {
                                return -Integer.compare(o1.appInfo.getArg1(), o2.appInfo.getArg1());
                            }
                        });
                return res;
            }
        };
    }
}
