package github.tornaco.android.thanos.theme;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Observable;

import github.tornaco.android.thanos.core.app.ThanosManager;
import util.Singleton;

public class AppThemePreferences extends Observable {

    private static final String PREF_KEY_APP_THEME = "PREF_KEY_APP_THEME";
    private static final String PREF_KEY_APP_THEME_PREFER_L = "PREF_KEY_APP_THEME_PREFER_L";
    private static final String PREF_KEY_APP_THEME_PREFER_D = "PREF_KEY_APP_THEME_PREFER_D";
    private static final String PREF_KEY_APP_ICON_PACK = "PREF_KEY_APP_ICON_PACK";
    private static final String PREF_KEY_USE_ROUND_ICON = "github.tornaco.android.thanos.ui.used_round_icon";

    private static Singleton<AppThemePreferences> sPref = new Singleton<AppThemePreferences>() {
        @Override
        protected AppThemePreferences create() {
            return new AppThemePreferences();
        }
    };

    private AppThemePreferences() {
    }

    public static AppThemePreferences getInstance() {
        return sPref.get();
    }

    public Theme getTheme(@NonNull Context context) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return Theme.Light;
        try {
            return Theme.valueOf(
                    thanos.getPrefManager().getString(PREF_KEY_APP_THEME,
                            Theme.Light.name()));
        } catch (Throwable e) {
            return Theme.Light;
        }
    }

    public void setTheme(@NonNull Context context, @NonNull Theme theme) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        thanos.getPrefManager().putString(PREF_KEY_APP_THEME, theme.name());
        setChanged();
        notifyObservers();
    }

    public void setPreferLDTheme(@NonNull Context context, @NonNull Theme theme) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        if (theme.isLight) {
            thanos.getPrefManager().putString(PREF_KEY_APP_THEME_PREFER_L, theme.name());
        } else {
            thanos.getPrefManager().putString(PREF_KEY_APP_THEME_PREFER_D, theme.name());
        }
    }

    public Theme getPreferDarkTheme(@NonNull Context context) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return Theme.Dark;
        try {
            return Theme.valueOf(
                    thanos.getPrefManager().getString(PREF_KEY_APP_THEME_PREFER_D,
                            Theme.Dark.name()));
        } catch (Throwable e) {
            return Theme.Dark;
        }
    }

    public Theme getPreferLightTheme(@NonNull Context context) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return Theme.Light;
        try {
            return Theme.valueOf(
                    thanos.getPrefManager().getString(PREF_KEY_APP_THEME_PREFER_L,
                            Theme.Light.name()));
        } catch (Throwable e) {
            return Theme.Light;
        }
    }

    public String getIconPack(@NonNull Context context, String defaultValue) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return defaultValue;
        try {
            return thanos.getPrefManager()
                    .getString(PREF_KEY_APP_ICON_PACK, defaultValue);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    public void setIconPack(@NonNull Context context, @NonNull String iconPkg) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        thanos.getPrefManager().putString(PREF_KEY_APP_ICON_PACK, iconPkg);
    }

    public void setUseRoundIcon(Context context, boolean use) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        thanos.getPrefManager().putBoolean(PREF_KEY_USE_ROUND_ICON, use);
    }

    public boolean useRoundIcon(Context context) {
        ThanosManager thanox = ThanosManager.from(context);
        if (!thanox.isServiceInstalled()) return false;
        try {
            return thanox.getPrefManager().getBoolean(PREF_KEY_USE_ROUND_ICON, false);
        } catch (Throwable e) {
            return false;
        }
    }
}
