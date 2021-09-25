package github.tornaco.android.thanos.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
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
        findViewById(R.id.guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserUtils.launch(thisActivity(), BuildProp.THANOX_URL_DOCS_HOME);
            }
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
        new AlertDialog.Builder(thisActivity())
                .setTitle(R.string.nav_title_feedback)
                .setMessage(R.string.dialog_message_feedback)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
