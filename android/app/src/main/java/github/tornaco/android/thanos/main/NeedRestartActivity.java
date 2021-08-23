package github.tornaco.android.thanos.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.databinding.ActivityNeedRestartBinding;
import github.tornaco.android.thanos.util.ActivityUtils;

public class NeedRestartActivity extends AppCompatActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, NeedRestartActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNeedRestartBinding binding = ActivityNeedRestartBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.rebootButton.setOnClickListener(v -> ThanosManager.from(getApplicationContext())
                .ifServiceInstalled(thanosManager -> thanosManager.getPowerManager().reboot()));
        binding.ignoreButton.setOnClickListener(v -> finish());
    }
}
