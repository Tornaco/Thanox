package github.tornaco.android.thanos.theme;

import androidx.annotation.StyleRes;

import github.tornaco.android.thanos.module.common.R;

public enum Theme {
    Light(R.style.AppThemeLight_NoActionBar, true),
    LightRed(R.style.AppThemeLightRed_NoActionBar, true),
    LightGreen(R.style.AppThemeLightGreen_NoActionBar, true),
    LightAmber(R.style.AppThemeLightAmber_NoActionBar, true),
    LightBlack(R.style.AppThemeLightBlack_NoActionBar, true),
    LightOppo(R.style.AppThemeLightOppo_NoActionBar, true),
    LightPink(R.style.AppThemeLightPink_NoActionBar, true),

    BlueOrange(R.style.AppThemeBlueOrange_NoActionBar, true),

    Dark(R.style.AppTheme_NoActionBar, true),
    DarkAmber(R.style.AppThemeAmber_NoActionBar, true),
    DarkRed(R.style.AppThemeRed_NoActionBar, true),
    DarkGrey(R.style.AppThemeGrey_NoActionBar, true),

    Auto(R.style.AppThemeLight_NoActionBar, true);

    @StyleRes
    public int themeRes;

    public boolean isLight;

    private Theme(int themeRes, boolean isLight) {
        this.themeRes = themeRes;
        this.isLight = isLight;
    }
}
