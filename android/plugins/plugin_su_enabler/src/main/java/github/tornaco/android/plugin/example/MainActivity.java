package github.tornaco.android.plugin.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import github.tornaco.android.thanos.core.app.ThanosManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setTitle(R.string.app_name);

        if (!ThanosManager.from(getApplicationContext()).isServiceInstalled()) {
            return;
        }

        try {
            boolean installed = ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .isShellSuSupportInstalled();
            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = findViewById(R.id.switch1);
            s.setChecked(installed);
            s.setOnClickListener(v -> setSuSupportEnabled(s.isChecked()));
        } catch (Throwable e) {
            Toast.makeText(getApplicationContext(), Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    private void setSuSupportEnabled(boolean enable) {
        if (!enable) {
            ThanosManager.from(getApplicationContext())
                    .getProfileManager()
                    .setShellSuSupportInstalled(false);
            return;
        }


        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.dialog_desc_su_enabled)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    ThanosManager.from(getApplicationContext())
                            .getProfileManager()
                            .setShellSuSupportInstalled(true);
                })
                .show();
    }
}
