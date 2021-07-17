package github.tornaco.android.plugin.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.app.ThanosManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThanosManager thanos = ThanosManager.from(getApplicationContext());
                if (thanos.isServiceInstalled()) {
                    String fp = thanos.fingerPrint();
                    Toast.makeText(getApplicationContext(), "Hello world! \nThanox: " + fp, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Hello world! \nThanox not active....", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
