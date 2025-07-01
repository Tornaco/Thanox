package github.tornaco.android.thanos.theme;

import android.content.Context;

import androidx.annotation.NonNull;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.module.compose.common.infra.sort.AppSortTools;
import util.Singleton;

public class CommonAppPrefs {

    private static final String PREF_KEY_APP_ICON_PACK = "PREF_KEY_APP_ICON_PACK";
    private static final String PREF_KEY_USE_ROUND_ICON = "github.tornaco.android.thanos.ui.used_round_icon";
    private static final String PREF_KEY_FEATURE_SORT_SELECTION = "PREF_KEY_FEATURE_SORT_SELECTION_";
    private static final String PREF_KEY_FEATURE_FILTER_SELECTION = "PREF_KEY_FEATURE_FILTER_SELECTION_";

    private static final Singleton<CommonAppPrefs> sPref = new Singleton<>() {
        @Override
        protected CommonAppPrefs create() {
            return new CommonAppPrefs();
        }
    };

    private CommonAppPrefs() {
    }

    public static CommonAppPrefs getInstance() {
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

    public AppSortTools getFeatureSortSelection(@NonNull Context context, String featId, AppSortTools defaultValue) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return defaultValue;
        try {
            int ord = thanos.getPrefManager().getInt(PREF_KEY_FEATURE_SORT_SELECTION + featId, defaultValue.ordinal());
            return AppSortTools.values()[ord];
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    public void setFeatureSortSelection(@NonNull Context context, @NonNull String featId, AppSortTools sort) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        thanos.getPrefManager().putInt(PREF_KEY_FEATURE_SORT_SELECTION + featId, sort.ordinal());
    }

    public String getFeatureAppFilterId(@NonNull Context context, String featId, String defaultValue) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return defaultValue;
        try {
            return thanos.getPrefManager().getString(PREF_KEY_FEATURE_FILTER_SELECTION + featId, defaultValue);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    public void setFeatureAppFilterId(@NonNull Context context, String featId, @NonNull String filter) {
        ThanosManager thanos = ThanosManager.from(context);
        if (!thanos.isServiceInstalled()) return;
        thanos.getPrefManager().putString(PREF_KEY_FEATURE_FILTER_SELECTION + featId, filter);
    }
}
