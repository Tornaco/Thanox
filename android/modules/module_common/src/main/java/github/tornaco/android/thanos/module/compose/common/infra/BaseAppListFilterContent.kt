package github.tornaco.android.thanos.module.compose.common.infra

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.module.compose.common.theme.TypographyDefaults
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxSmallAppBarScaffold

@Composable
fun Activity.BaseAppListFilterContent() {
    ThanoxSmallAppBarScaffold(
        title = {
            Text(
                text = stringResource(github.tornaco.android.thanos.res.R.string.common_menu_title_batch_select),
                style = TypographyDefaults.appBarTitleTextStyle()
            )
        },
        onBackPressed = {
            finish()
        }
    ) { paddings ->

    }
}