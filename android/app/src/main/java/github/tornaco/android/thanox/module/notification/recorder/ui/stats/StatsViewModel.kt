package github.tornaco.android.thanox.module.notification.recorder.ui.stats

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.util.DateUtils
import github.tornaco.android.thanos.module.compose.common.widget.pie.ChartItem
import github.tornaco.android.thanos.module.compose.common.widget.pie.chartColorOfIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class StatsViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {

    private val thanos get() = ThanosManager.from(context)

    private val _flow = MutableStateFlow(
        StatsChartState(
            isLoading = false,
            totalCount = 0L,
            entries = emptyList()
        )
    )

    val flow = _flow.asStateFlow()

    fun startLoading() {
        viewModelScope.launch {
            loadNotifications()
        }
    }

    private suspend fun loadNotifications() {
        XLog.d("loadNotifications")
        _flow.value = _flow.value.copy(isLoading = true)

        withContext(Dispatchers.IO) {
            val notificationManager = thanos.notificationManager
            val records = notificationManager.getAllNotificationRecordsByPageAndKeywordInDateRange(
                0,
                1000,
                DateUtils.getToadyStartTimeInMills(), System.currentTimeMillis(),
                ""
            ).groupBy { it.pkgName }

            val totalCount = records.values.sumOf { it.size }
            var index = -1
            val chartItems = records.map {
                index += 1
                ChartItem(
                    data = it.key,
                    color = chartColorOfIndex(index),
                    value = it.value.size.toLong(),
                    label = (thanos.pkgManager.getAppInfo(it.key)?.appLabel
                        ?: it.key) + "\t" + it.value.size
                )
            }.sortedByDescending { it.value }

            _flow.value = _flow.value.copy(
                isLoading = false,
                totalCount = totalCount.toLong(),
                entries = chartItems
            )
        }
    }

}