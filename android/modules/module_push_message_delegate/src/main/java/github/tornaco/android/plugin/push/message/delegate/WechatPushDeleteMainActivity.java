package github.tornaco.android.plugin.push.message.delegate;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;

public class WechatPushDeleteMainActivity extends ThemeActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, WechatPushDeleteMainActivity.class);
    }

    @Override
    public boolean isADVF() {
        return true;
    }

    @Override
    public boolean isF() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_push_message_delegate_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        showHomeAsUpNavigator();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_push_message_delegate_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_gcm_diag) {
            Intent intent = new Intent();
            intent.setComponent(ComponentName.unflattenFromString("com.google.android.gms/com.google.android.gms.gcm.GcmDiagnostics"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityChecked(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startActivityChecked(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Noop.
        }
    }
}
