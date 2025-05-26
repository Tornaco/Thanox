package github.tornaco.android.thanos.theme;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.color.DynamicColors;

import github.tornaco.android.thanos.BaseDefaultMenuItemHandlingAppCompatActivity;

@SuppressLint("Registered")
public class ThemeActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeOnCreate();
        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);
    }

    protected void beforeOnCreate() {
    }
}
