package github.tornaco.android.thanos.theme;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.elvishew.xlog.XLog;

import java.util.Observable;
import java.util.Observer;

import github.tornaco.android.thanos.BaseDefaultMenuItemHandlingAppCompatActivity;

@SuppressLint("Registered")
public class ThemeActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {

    private final Observer themeObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            onThemeChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppThemePreferences.getInstance().addObserver(themeObserver);
        Theme theme = getAppTheme();
        setTheme(getThemeRes(theme));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    protected Theme getAppTheme() {
        Theme userTheme = AppThemePreferences.getInstance().getTheme(this);
        return userTheme == Theme.Auto ? getDayNightTheme() : userTheme;
    }

    private Theme getDayNightTheme() {
        Configuration configuration = getResources().getConfiguration();
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                return AppThemePreferences.getInstance().getPreferLightTheme(this);
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
            default:
                return AppThemePreferences.getInstance().getPreferDarkTheme(this);
        }
    }

    @StyleRes
    protected int getThemeRes(Theme theme) {
        return theme.themeRes;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppThemePreferences.getInstance().deleteObserver(themeObserver);
    }

    protected void onThemeChanged() {
        XLog.v("onThemeChanged.");
        finish();
    }
}
