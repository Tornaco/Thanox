package github.tornaco.android.thanos.theme;

import android.content.Context;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.core.app.ThanosManager;
import util.Singleton;

public class AppThemePreferences {

    private static final String PREF_KEY_APP_ICON_PACK = "PREF_KEY_APP_ICON_PACK";
    private static final String PREF_KEY_USE_ROUND_ICON = "github.tornaco.android.thanos.ui.used_round_icon";

    private static final Singleton<AppThemePreferences> sPref = new Singleton<>() {
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

    public String getIconPack(@NonNull Context context, String defaultValue) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return defaultValue;
        try {
            return thanos.getPrefManager().getString(PREF_KEY_APP_ICON_PACK, defaultValue);
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
