package github.tornaco.android.thanos.start.chart

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.widget.pie.ChartItem
import github.tornaco.android.thanos.module.compose.common.widget.pie.chartColorOfIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class StartChartViewModel @Inject constructor(@ApplicationContext val context: Context) :
    ViewModel() {
    private val thanos get() = ThanosManager.from(context)

    private val _flow = MutableStateFlow(StartChartState(isLoading = false,
        totalTimes = 0L,
        entries = emptyList(),
        category = Category.Merged))
    val flow = _flow.asStateFlow()

    fun startLoading() {
        viewModelScope.launch {
            loadStartEntries()
        }
    }

    fun selectCategory(category: Category) {
        _flow.value = _flow.value.copy(category = category)
        startLoading()
    }

    private suspend fun loadStartEntries() {
        XLog.d("loadStartEntries")
        _flow.value = _flow.value.copy(isLoading = true)

        delay(1000)

        withContext(Dispatchers.IO) {
            val pkgs = mutableSetOf<String>()
            val category = _flow.value.category

            val activityManager = thanos.activityManager
            if (category.withAllowed) pkgs.addAll(activityManager.startRecordAllowedPackages)
            if (category.withBlocked) pkgs.addAll(activityManager.startRecordBlockedPackages)

            var totalTimes = 0L

            val startEntryList = pkgs
                .filter {
                    it != "android" && it != "com.android.systemui"
                }
                .map { pkgName ->
                    var times = 0L
                    if (category.withAllowed) {
                        times += activityManager.getStartRecordAllowedCountByPackageName(pkgName)
                    }

                    if (category.withBlocked) {
                        times += activityManager.getStartRecordBlockedCountByPackageName(pkgName)
                    }

                    totalTimes += times

                    StartEntry(
                        pkg = pkgName,
                        appLabel = thanos.pkgManager.getAppInfo(pkgName)?.appLabel ?: pkgName,
                        times = times)
                }.sortedByDescending { it.times }

            val chartItems = startEntryList.mapIndexed { index, startEntry ->
                ChartItem(
                    data = startEntry.pkg,
                    color = chartColorOfIndex(index),
                    value = startEntry.times,
                    label = startEntry.appLabel + "\t" + startEntry.times)
            }.subList(0, min(startEntryList.size, 99))
            _flow.value = _flow.value.copy(isLoading = false,
                totalTimes = totalTimes,
                entries = chartItems)
        }
    }

    fun clearRecordsForCurrentCategory() {
        val category = _flow.value.category
        if (category == Category.Blocked) {
            ThanosManager.from(context)
                .activityManager
                .resetStartRecordsBlocked()
        }
        if (category == Category.Allowed) {
            ThanosManager.from(context)
                .activityManager
                .resetStartRecordsAllowed()
        }
        if (category == Category.Merged) {
            ThanosManager.from(context)
                .activityManager
                .resetStartRecordsBlocked()
            ThanosManager.from(context)
                .activityManager
                .resetStartRecordsAllowed()
        }
        startLoading()
    }
}