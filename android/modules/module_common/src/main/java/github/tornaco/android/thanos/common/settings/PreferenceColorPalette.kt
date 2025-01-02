package github.tornaco.android.thanos.common.settings

import androidx.compose.ui.graphics.Color
import java.util.concurrent.atomic.AtomicInteger


val prefColorPalette = arrayOf(
    Color(0xffFF5145),
    Color(0xff00BBDF),
    Color(0xffF8B62E),
    Color(0xffF02D7D),
    Color(0xff1A9431),
    Color(0xff4586F3),
    Color(0xffEB4334),
    Color(0xffFBBD06),
    Color(0xff35AA53),
    Color(0xffFFDC00),
    Color(0xffC88C32),
    Color(0xff1C7670),
    Color(0xffFE7F1D),
    Color(0xffF550A6),
    Color(0xff1C7670),
    Color(0xffAE213B),
    Color(0xff11254A),
    Color(0xffFF0000),
    Color(0xff5EB6E4),
    Color(0xffA40084),

    Color(0xff0860A8),
    Color(0xffB5BD00),
    Color(0xff9A3B26),

    )

private val prefIndexCache = mutableMapOf<String, Int>()
private val prefIndex = AtomicInteger(0)

fun prefColorForPreference(preference: Preference): Color {
    val index = prefIndexCache.computeIfAbsent(preference.title) {
        prefIndex.getAndIncrement()
    }
    return prefColorPalette[index % (prefColorPalette.size - 1)]
}