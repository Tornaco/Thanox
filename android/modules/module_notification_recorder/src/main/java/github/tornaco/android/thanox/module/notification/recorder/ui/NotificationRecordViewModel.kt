package github.tornaco.android.thanox.module.notification.recorder.ui

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanox.module.notification.recorder.source.NotificationRecordRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

private const val PAGE_SIZE = 20

class NotificationRecordViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: NotificationRecordRepository
) : ViewModel() {
    companion object {
        const val KEY_KEYWORD = "keyword"
        const val DEFAULT_KEYWORD = ""
    }

    init {
        if (!savedStateHandle.contains(KEY_KEYWORD)) {
            savedStateHandle[KEY_KEYWORD] = DEFAULT_KEYWORD
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications = savedStateHandle.getLiveData<String>(KEY_KEYWORD)
        .asFlow()
        .flatMapLatest { repository.all(PAGE_SIZE, it) }
        // cachedIn() shares the paging state across multiple consumers of posts,
        // e.g. different generations of UI across rotation config change
        .cachedIn(viewModelScope)

    fun load(keyword: String) {
        if (!showLoad(keyword)) return
        savedStateHandle[KEY_KEYWORD] = keyword
    }

    private fun showLoad(keyword: String): Boolean {
        return savedStateHandle.get<String>(KEY_KEYWORD) != keyword
    }

    fun clearAllRecords(context: Context) {
        ThanosManager.from(context)
            .ifServiceInstalled { t -> t?.notificationManager?.cleanUpPersistNotificationRecords() }
    }

}
