package github.tornaco.android.plugin.push.message.delegate;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import github.tornaco.android.thanos.theme.ThemeActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setSupportActionBar(findViewById(R.id.toolbar));
        showHomeAsUpNavigator();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commit();
        }
    }

    private void showHomeAsUpNavigator() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
