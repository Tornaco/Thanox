package github.tornaco.android.plugin.toolbox;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.app.ThanosManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setTitle(R.string.app_name);

        if (!ThanosManager.from(getApplicationContext()).isServiceInstalled()) {
            return;
        }
    }
}
