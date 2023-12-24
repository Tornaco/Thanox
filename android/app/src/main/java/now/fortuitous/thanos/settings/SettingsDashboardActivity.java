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

package now.fortuitous.thanos.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.BrowserUtils;
import now.fortuitous.thanos.onboarding.OnBoardingActivity;

public class SettingsDashboardActivity extends ThemeActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, SettingsDashboardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings_dashboard);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new SettingsDashboardFragment())
                    .commit();
        }

        findViewById(R.id.fab).setOnClickListener(v -> showFeedbackDialog());
        findViewById(R.id.guide).setOnClickListener(v -> BrowserUtils.launch(thisActivity(), BuildProp.THANOX_URL_DOCS_HOME));
        findViewById(R.id.guide).setOnLongClickListener(v -> {
            OnBoardingActivity.Starter.INSTANCE.start(thisActivity());
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFeedbackDialog() {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(R.string.nav_title_feedback)
                .setMessage(R.string.dialog_message_feedback)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
