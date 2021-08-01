package github.tornaco.android.thanos.process;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.apps.AppDetailsActivity;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.RunningState;
import github.tornaco.android.thanos.app.BaseTrustedActivity;

/**
 * Created by Tornaco on 2018/5/2 15:05.
 * God bless no bug!
 */
public class RunningServicesDetailsActivity extends BaseTrustedActivity {

    public static void start(Context context, Bundle args) {
        Intent starter = new Intent(context, RunningServicesDetailsActivity.class);
        starter.putExtra("args", args);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    private String mPackageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_running_services_details);

        setSupportActionBar(findViewById(R.id.toolbar));
        showHomeAsUpNavigator();
        setTitle(null);

        PackageManager pm = getPackageManager();
        RunningState.MergedItem mergedItem = RunningServiceDetails.sSelectedItem;
        if (mergedItem.mPackageInfo == null) {
            // Items for background processes don't normally load
            // their labels for performance reasons. Do it now.
            if (mergedItem.mProcess != null) {
                mergedItem.mProcess.ensureLabel(pm);
                mergedItem.mPackageInfo = mergedItem.mProcess.mPackageInfo;
                mergedItem.mDisplayLabel = mergedItem.mProcess.mDisplayLabel;
            }
        }

        if (mergedItem.mPackageInfo != null) {
            mPackageName = mergedItem.mPackageInfo.packageName;
        }

        findViewById(R.id.app_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPackageName != null) {
                    AppInfo appInfo = ThanosManager.from(getApplicationContext())
                            .getPkgManager()
                            .getAppInfo(mPackageName);
                    AppDetailsActivity.start(getApplicationContext(), appInfo);
                }
            }
        });
        findViewById(R.id.stop_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPackageName != null) {
                    ThanosManager.from(getApplicationContext())
                            .getActivityManager()
                            .forceStopPackage(mPackageName);
                    finish();
                }
            }
        });

        Bundle args = getIntent().getBundleExtra("args");
        RunningServiceDetails details = new RunningServiceDetails();
        details.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.container, details)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
