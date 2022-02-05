package github.tornaco.android.thanos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

public class BaseAppCompatActivity extends BaseFeatureActivity {
    protected Handler uiHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHandler = new Handler(Looper.getMainLooper());
    }

    protected void postOnUiDelayed(Runnable runnable, long delay) {
        uiHandler.postDelayed(runnable, delay);
    }
}
