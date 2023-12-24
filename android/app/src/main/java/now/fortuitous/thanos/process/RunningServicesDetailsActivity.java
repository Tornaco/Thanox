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

package now.fortuitous.thanos.process;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.thanos.R;
import now.fortuitous.app.BaseTrustedActivity;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.RunningState;
import now.fortuitous.thanos.apps.AppDetailsActivity;

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
