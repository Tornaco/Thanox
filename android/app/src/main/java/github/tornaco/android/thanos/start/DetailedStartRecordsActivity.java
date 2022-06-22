package github.tornaco.android.thanos.start;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.apps.AppDetailsActivity;
import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.start.StartReason;
import github.tornaco.android.thanos.core.app.start.StartRecord;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.pm.PrebuiltPkgSetsKt;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;
import util.CollectionUtils;

public class DetailedStartRecordsActivity extends CommonAppListFilterActivity {

    private String targetPackageName;

    private boolean showAllowed = true;
    private boolean showBlocked = true;

    public static void start(Context context, String targetPackageName) {
        Bundle data = new Bundle();
        data.putString("pkg", targetPackageName);
        ActivityUtils.startActivity(context, DetailedStartRecordsActivity.class, data);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        targetPackageName = getIntent().getStringExtra("pkg");
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }

    @Override
    protected void onSetupSorter(Chip sorterAnchor) {
        sorterAnchor.setVisibility(View.GONE);
        viewModel.setAppSort(null);
    }

    @Override
    protected void onSetupChip(ViewGroup chipContainer, ChipGroup chipGroup, Chip chip1, Chip chip2, Chip chip3, Chip chip4) {
        chip1.setText(R.string.title_allow);
        chip1.setChecked(showAllowed);
        chip1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showAllowed = isChecked;
            viewModel.start();
        });


        chip2.setText(R.string.title_block);
        chip2.setChecked(showBlocked);
        chip2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showBlocked = isChecked;
            viewModel.start();
        });

        chip3.setVisibility(View.GONE);
        chip4.setVisibility(View.GONE);
    }

    @Override
    protected int getTitleRes() {
        return R.string.menu_title_start_restrict_charts_view_detailed_records;
    }

    @Override
    protected void onSetupFilter(Chip filterAnchor) {
        if (targetPackageName != null) {
            filterAnchor.setVisibility(View.GONE);
            setTitle(getTitleRes());
            viewModel.setAppCategoryFilter(PrebuiltPkgSetsKt.PREBUILT_PACKAGE_SET_ID_3RD);
            return;
        }
        super.onSetupFilter(filterAnchor);
    }

    @NonNull
    @Override
    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return appInfo -> {
            // Noop.
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(appInfo.getAppLabel())
                    .setMessage(appInfo.getStr())
                    .setPositiveButton(R.string.feature_title_apps_manager,
                            (dialog, which) -> AppDetailsActivity.start(thisActivity(), appInfo)).setNegativeButton(R.string.menu_title_start_restrict_charts_view_detailed_records_for_this_package,
                            (dialog, which) -> DetailedStartRecordsActivity.start(thisActivity(), appInfo.getPkgName()))
                    .show();

        };
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
            ActivityManager activityManager = thanosManager.getActivityManager();


            List<StartRecord> startRecordList = new ArrayList<>();

            if (targetPackageName == null) {
                startRecordList = Lists.newArrayList(activityManager.getAllStartRecordsForPackageSetWithRes(index.pkgSetId, showAllowed, showBlocked));
            } else {
                if (showAllowed) {
                    startRecordList.addAll(activityManager.getStartRecordsAllowedByPackageName(targetPackageName));
                }
                if (showBlocked) {
                    startRecordList.addAll(activityManager.getStartRecordsBlockedByPackageName(targetPackageName));
                }
            }

            Collections.sort(startRecordList, (o1, o2) -> -Long.compare(o1.getWhenByMills(), o2.getWhenByMills()));

            List<AppListModel> res = new ArrayList<>(startRecordList.size());
            PackageManager packageManager = thanosManager.getPkgManager();

            String allowDesc = getString(R.string.title_allow);
            String blockDesc = getString(R.string.title_block);

            int colorGreen = ContextCompat.getColor(thisActivity(), R.color.md_green_800);
            int colorRed = ContextCompat.getColor(thisActivity(), R.color.md_red_500);

            CollectionUtils.consumeRemaining(startRecordList, startRecord -> {
                AppInfo appInfo = packageManager.getAppInfo(startRecord.getPackageName());
                if (appInfo != null) {
                    String badge1 = startRecord.getResult().res ? allowDesc : blockDesc;
                    String timeDesc = FuzzyDateTimeFormatter.getTimeAgo(getApplicationContext(), new Date(startRecord.getWhenByMills()));
                    String startMethodDesc = getStartMethodDesc(startRecord, packageManager);
                    String whyDesc = startRecord.getResult().why;
                    int badge1Color = startRecord.getResult().res ? colorGreen : colorRed;
                    String fullDesc = String.format("Method: %s\nReason: %s\nChecker: %s", startMethodDesc, whyDesc, startRecord.getChecker());
                    AppListModel model = new AppListModel(appInfo, badge1, timeDesc, badge1Color, 0, fullDesc, false);
                    // Set start str.
                    appInfo.setStr(startRecord.getRequestPayload());
                    res.add(model);
                }
            });

            return res;
        };
    }

    private String getStartMethodDesc(StartRecord startRecord, PackageManager packageManager) {
        int method = startRecord.getMethod();
        String methodStr;
        switch (method) {
            case StartReason.ACTIVITY:
                methodStr = "Activity";
                break;
            case StartReason.TOP_ACTIVITY:
                methodStr = "Top Activity";
                break;
            case StartReason.PRE_ACTIVITY:
                methodStr = "Pre Activity";
                break;
            case StartReason.PRE_TOP_ACTIVITY:
                methodStr = "Pre Top Activity";
                break;
            case StartReason.BROADCAST:
                methodStr = "Broadcast";
                break;
            case StartReason.PROVIDER:
                methodStr = "Content Provider";
                break;
            case StartReason.SERVICE:
            case StartReason.RESTART_SERVICE:
                methodStr = "Service";
                break;
            default:
                methodStr = "Others";
                break;
        }

        String starterAppLabel = "Android?";
        String starterPkgName = startRecord.getStarterPackageName();
        if (starterPkgName != null) {
            AppInfo starterAppInfo = packageManager.getAppInfo(starterPkgName);
            if (starterAppInfo != null) {
                starterAppLabel = starterAppInfo.getAppLabel();
            }
        }

        return String.format("By %s via %s", starterAppLabel, methodStr);
    }
}
