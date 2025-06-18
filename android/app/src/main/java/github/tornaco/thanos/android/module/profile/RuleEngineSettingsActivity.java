package github.tornaco.thanos.android.module.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

public class RuleEngineSettingsActivity extends ThemeActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, RuleEngineSettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.module_profile_activity_engine_settings);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new RuleEngineSettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
