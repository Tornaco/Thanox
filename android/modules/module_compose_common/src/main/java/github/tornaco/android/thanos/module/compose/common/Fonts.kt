package github.tornaco.android.thanos.module.compose.common

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class)
@Composable
fun fontFamily() = FontFamily(
    Font(LocalContext.current.assets, "fonts/google/ProductSansBold.ttf")
)

@Composable
fun productSansBoldTypography() = Typography(
    h5 = TextStyle(
        fontFamily = fontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
)