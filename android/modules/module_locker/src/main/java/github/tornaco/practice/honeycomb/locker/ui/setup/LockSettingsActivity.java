package github.tornaco.practice.honeycomb.locker.ui.setup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.practice.honeycomb.locker.R;

public class LockSettingsActivity extends ThemeActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, LockSettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_locker_settings_activity);
        setSupportActionBar(findViewById(R.id.toolbar));
        showHomeAsUpNavigator();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commit();
        }
    }
}
