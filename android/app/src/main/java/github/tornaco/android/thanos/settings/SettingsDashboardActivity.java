package github.tornaco.android.thanos.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.onboarding.OnBoardingActivity;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.BrowserUtils;

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
