package github.tornaco.android.thanos.privacy;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.chip.Chip;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.secure.PrivacyCheatRecord;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.core.secure.PrivacyManager.PrivacyOp;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;
import util.CollectionUtils;
import util.Consumer;

public class CheatRecordViewerActivity extends CommonAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, CheatRecordViewerActivity.class);
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
        return R.string.privacy_record;
    }

    @NonNull
    @Override
    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return new AppItemClickListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                // Noop.
            }
        };
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new CommonAppListFilterViewModel.ListModelLoader() {
            @Override
            public List<AppListModel> load(@NonNull CategoryIndex index) {
                ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
                PrivacyCheatRecord[] privacyCheatRecords = thanosManager.getPrivacyManager().getPrivacyCheatRecords();
                List<PrivacyCheatRecord> privacyCheatRecordList = Lists.newArrayList(privacyCheatRecords);
                Collections.sort(privacyCheatRecordList, new Comparator<PrivacyCheatRecord>() {
                    @Override
                    public int compare(PrivacyCheatRecord o1, PrivacyCheatRecord o2) {
                        return -Long.compare(o1.getTimeMills(), o2.getTimeMills());
                    }
                });
                List<AppListModel> res = new ArrayList<>();
                CollectionUtils.consumeRemaining(privacyCheatRecordList, new Consumer<PrivacyCheatRecord>() {
                    @Override
                    public void accept(PrivacyCheatRecord privacyCheatRecord) {
                        AppInfo appInfo = thanosManager.getPkgManager().getAppInfo(privacyCheatRecord.getPackageName());
                        if (appInfo != null) {
                            String desc = FuzzyDateTimeFormatter.getTimeAgo(getApplicationContext(), new Date(privacyCheatRecord.getTimeMills()));
                            res.add(new AppListModel(appInfo, decodeOp(privacyCheatRecord.getOp()), null, 0, 0, desc, false));
                        }
                    }
                });
                return res;
            }
        };
    }

    private String decodeOp(int op) {
        switch (op) {
            case PrivacyManager.PrivacyOp.OP_ANDROID_ID:
                return "Android Id";
            case PrivacyManager.PrivacyOp.OP_DEVICE_ID:
                return "Device Id";
            case PrivacyManager.PrivacyOp.OP_IMEI:
                return "IMEI";
            case PrivacyManager.PrivacyOp.OP_LINE1NUM:
                return "Line1 num";
            case PrivacyManager.PrivacyOp.OP_MEID:
                return "MEID";
            case PrivacyManager.PrivacyOp.OP_SIM_SERIAL:
                return "Sim serial";
            case PrivacyOp.OP_SIM_CONTRY_ISO:
                return "Sim country";
            case PrivacyOp.OP_NETWORK_CONTRY_ISO:
                return "Network country";
            case PrivacyOp.OP_NETWORK_OPERATOR_NAME:
                return "Network Operator name";
            case PrivacyOp.OP_NETWORK_OPERATOR:
                return "Network Operator";
            case PrivacyOp.OP_SIM_OPERATOR_NAME:
                return "SIM Operator name";
            case PrivacyOp.OP_SIM_OPERATOR:
                return "SIM Operator";
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cheat_record_viewer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
            thanosManager.getPrivacyManager().clearPrivacyCheatRecords();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
