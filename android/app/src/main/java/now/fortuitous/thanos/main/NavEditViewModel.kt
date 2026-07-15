package now.fortuitous.thanos.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NavEditState(
    val groups: List<FeatureItemGroup>,
    val expandedGroupKey: String? = null,
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class NavEditViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val _state = MutableStateFlow(NavEditState(groups = emptyList()))
    val state = _state.asStateFlow()

    fun load() {
        val groups = PrebuiltFeatures.all()
        val ordered = NavOrderPreference.applyOrder(context, groups)
        _state.value = _state.value.copy(groups = ordered)
    }

    fun toggleExpand(groupKey: String) {
        val current = _state.value.expandedGroupKey
        _state.value = _state.value.copy(
            expandedGroupKey = if (current == groupKey) null else groupKey
        )
    }

    fun moveGroupUp(groupKey: String) {
        val groups = _state.value.groups.toMutableList()
        val index = groups.indexOfFirst { it.key == groupKey }
        if (index > 0) {
            groups[index] = groups[index - 1].also { groups[index - 1] = groups[index] }
            _state.value = _state.value.copy(groups = groups)
            saveGroupOrder(groups)
        }
    }

    fun moveGroupDown(groupKey: String) {
        val groups = _state.value.groups.toMutableList()
        val index = groups.indexOfFirst { it.key == groupKey }
        if (index >= 0 && index < groups.size - 1) {
            groups[index] = groups[index + 1].also { groups[index + 1] = groups[index] }
            _state.value = _state.value.copy(groups = groups)
            saveGroupOrder(groups)
        }
    }

    fun moveFeatureUp(groupKey: String, featureId: Int) {
        val groups = _state.value.groups.toMutableList()
        val groupIndex = groups.indexOfFirst { it.key == groupKey }
        if (groupIndex < 0) return
        val items = groups[groupIndex].items.toMutableList()
        val index = items.indexOfFirst { it.id == featureId }
        if (index > 0) {
            items[index] = items[index - 1].also { items[index - 1] = items[index] }
            groups[groupIndex] = groups[groupIndex].copy(items = items)
            _state.value = _state.value.copy(groups = groups)
            saveFeatureOrder(groupKey, items)
        }
    }

    fun moveFeatureDown(groupKey: String, featureId: Int) {
        val groups = _state.value.groups.toMutableList()
        val groupIndex = groups.indexOfFirst { it.key == groupKey }
        if (groupIndex < 0) return
        val items = groups[groupIndex].items.toMutableList()
        val index = items.indexOfFirst { it.id == featureId }
        if (index >= 0 && index < items.size - 1) {
            items[index] = items[index + 1].also { items[index + 1] = items[index] }
            groups[groupIndex] = groups[groupIndex].copy(items = items)
            _state.value = _state.value.copy(groups = groups)
            saveFeatureOrder(groupKey, items)
        }
    }

    fun resetOrder() {
        NavOrderPreference.resetAll(context)
        load()
    }

    private fun saveGroupOrder(groups: List<FeatureItemGroup>) {
        NavOrderPreference.setGroupOrder(context, groups.map { it.key })
    }

    private fun saveFeatureOrder(groupKey: String, items: List<FeatureItem>) {
        NavOrderPreference.setFeatureOrder(context, groupKey, items.map { it.id })
    }
}
