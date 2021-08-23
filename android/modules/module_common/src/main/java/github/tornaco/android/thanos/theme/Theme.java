package github.tornaco.android.thanos.theme;

import androidx.annotation.StyleRes;

import github.tornaco.android.thanos.module.common.R;

public enum Theme {
    Light(R.style.AppThemeLight_NoActionBar),
    LightRed(R.style.AppThemeLightRed_NoActionBar),
    LightGreen(R.style.AppThemeLightGreen_NoActionBar),
    LightAmber(R.style.AppThemeLightAmber_NoActionBar),
    LightBlack(R.style.AppThemeLightBlack_NoActionBar),
    LightOppo(R.style.AppThemeLightOppo_NoActionBar),
    LightPink(R.style.AppThemeLightPink_NoActionBar),

    BlueOrange(R.style.AppThemeBlueOrange_NoActionBar),

    Dark(R.style.AppTheme_NoActionBar),
    DarkAmber(R.style.AppThemeAmber_NoActionBar),
    DarkRed(R.style.AppThemeRed_NoActionBar),
    DarkGrey(R.style.AppThemeGrey_NoActionBar),

    Auto(R.style.AppThemeLight_NoActionBar);

    @StyleRes
    public int themeRes;

    private Theme(int themeRes) {
        this.themeRes = themeRes;
    }
}
