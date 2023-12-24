package now.fortuitous.thanos.start.chart

import androidx.annotation.StringRes
import github.tornaco.android.thanos.R

enum class Category(
    @StringRes val labelRes: Int,
    val withAllowed: Boolean,
    val withBlocked: Boolean,
) {
    Allowed(R.string.start_record_allowed, true, false),
    Blocked(R.string.start_record_blocked, false, true),
    Merged(R.string.start_record_merged, true, true)
}