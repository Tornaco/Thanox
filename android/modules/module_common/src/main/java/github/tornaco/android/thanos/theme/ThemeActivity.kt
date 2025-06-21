package github.tornaco.android.thanos.theme

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.color.DynamicColors
import github.tornaco.android.thanos.BaseDefaultMenuItemHandlingAppCompatActivity
import github.tornaco.android.thanos.module.compose.common.ThemeActivityVM
import github.tornaco.android.thanos.module.compose.common.theme.isSystemInDarkTheme


@SuppressLint("Registered")
open class ThemeActivity : BaseDefaultMenuItemHandlingAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate()
        val theme = if (ThemeActivityVM.state.value.shouldUseDarkTheme(
                resources.configuration.isSystemInDarkTheme
            )
        ) {
            github.tornaco.android.thanos.module.common.R.style.ThemeMD3Dark_App_NoActionBar
        } else {
            github.tornaco.android.thanos.module.common.R.style.ThemeMD3Light_App_NoActionBar
        }
        setTheme(theme)

        if (ThemeActivityVM.state.value.themeSettings?.disableDynamicTheming == false) {
            DynamicColors.applyToActivityIfAvailable(
                this
            )
        }
        super.onCreate(savedInstanceState)
    }

    protected fun beforeOnCreate() {
    }
}
