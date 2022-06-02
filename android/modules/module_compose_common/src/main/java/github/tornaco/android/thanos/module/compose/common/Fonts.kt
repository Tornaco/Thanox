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
fun fontFamilyProductSans() = FontFamily(
    Font(LocalContext.current.assets, "fonts/google/ProductSansBold.ttf")
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun fontFamilyJetBrainsMono() = FontFamily(
    Font(LocalContext.current.assets, "fonts/google/jetbrains/JetBrainsMonoRegular.ttf")
)

@Composable
fun productSansBoldTypography() = Typography(
    h5 = TextStyle(
        fontFamily = fontFamilyProductSans(),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    caption = TextStyle(
        fontFamily = fontFamilyProductSans(),
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 0.4.sp
    )
)

@Composable
fun jetbrainMonoTypography() = Typography(
    body1 = TextStyle(
        fontFamily = fontFamilyJetBrainsMono(),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = fontFamilyJetBrainsMono(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)