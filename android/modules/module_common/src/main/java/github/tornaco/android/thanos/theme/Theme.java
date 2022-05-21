package github.tornaco.android.thanos.theme;

import androidx.annotation.StyleRes;

import github.tornaco.android.thanos.module.common.R;

public enum Theme {
    Light(R.style.AppThemeLight_NoActionBar, true),
    LightIndigo(R.style.AppThemeLight_Indigo_NoActionBar, true),
    LightRed(R.style.AppThemeLight_Red_NoActionBar, true),
    LightGreen(R.style.AppThemeLight_Green_NoActionBar, true),
    LightAmber(R.style.AppThemeLight_Amber_NoActionBar, true),
    LightBlack(R.style.AppThemeLight_Black_NoActionBar, true),
    LightOppo(R.style.AppThemeLight_Oppo_NoActionBar, true),
    LightPink(R.style.AppThemeLight_Pink_NoActionBar, true),

    Dark(R.style.AppThemeDark_NoActionBar, false),
    DarkIndigo(R.style.AppThemeDark_Indigo_NoActionBar, false),
    DarkAmber(R.style.AppThemeDark_Amber_NoActionBar, false),
    DarkRed(R.style.AppThemeDark_Red_NoActionBar, false),
    DarkGrey(R.style.AppThemeDark_Grey_NoActionBar, false),

    MD3(R.style.ThemeMD3_App_NoActionBar, true, true),

    Auto(R.style.AppThemeLight_NoActionBar, true);

    @StyleRes
    public int themeRes;

    public boolean isLight;
    public boolean shouldApplyDynamic;

    Theme(int themeRes, boolean isLight, boolean shouldApplyDynamic) {
        this.themeRes = themeRes;
        this.isLight = isLight;
        this.shouldApplyDynamic = shouldApplyDynamic;
    }

    Theme(int themeRes, boolean isLight) {
        this(themeRes, isLight, false);
    }

    public static Theme fromStringOrElse(String themeStr, Theme orElse) {
        for (Theme theme : values()) {
            if (theme.name().equals(themeStr)) {
                return theme;
            }
        }
        return orElse;
    }
}
