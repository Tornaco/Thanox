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
    val sections: List<FeatureItemGroup>,
    val expandedGroupKey: String? = null,
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class NavEditViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val _state = MutableStateFlow(NavEditState(sections = emptyList()))
    val state = _state.asStateFlow()

    fun load() {
        val featureGroups = PrebuiltFeatures.all()
        val orderedFeatures = NavOrderPreference.applyOrder(context, featureGroups)
        val headerSections = listOf(
            FeatureItemGroup(
                key = PrebuiltFeatures.SECTION_KEY_CPU_MEM,
                titleRes = github.tornaco.android.thanos.res.R.string.section_title_cpu_mem,
                items = emptyList()
            ),
            FeatureItemGroup(
                key = PrebuiltFeatures.SECTION_KEY_RUNNING,
                titleRes = github.tornaco.android.thanos.res.R.string.section_title_running,
                items = emptyList()
            ),
        )
        val allSections = headerSections + orderedFeatures
        val allKeys = allSections.map { it.key }
        val orderedKeys = NavOrderPreference.applySectionOrder(context, allKeys)
        val sectionMap = allSections.associateBy { it.key }
        val orderedSections = orderedKeys.mapNotNull { sectionMap[it] }
        _state.value = _state.value.copy(sections = orderedSections)
    }

    fun toggleExpand(groupKey: String) {
        val current = _state.value.expandedGroupKey
        _state.value = _state.value.copy(
            expandedGroupKey = if (current == groupKey) null else groupKey
        )
    }

    fun moveSectionUp(sectionKey: String) {
        val sections = _state.value.sections.toMutableList()
        val index = sections.indexOfFirst { it.key == sectionKey }
        if (index > 0) {
            sections[index] = sections[index - 1].also { sections[index - 1] = sections[index] }
            _state.value = _state.value.copy(sections = sections)
            saveSectionOrder(sections)
        }
    }

    fun moveSectionDown(sectionKey: String) {
        val sections = _state.value.sections.toMutableList()
        val index = sections.indexOfFirst { it.key == sectionKey }
        if (index >= 0 && index < sections.size - 1) {
            sections[index] = sections[index + 1].also { sections[index + 1] = sections[index] }
            _state.value = _state.value.copy(sections = sections)
            saveSectionOrder(sections)
        }
    }

    fun moveFeatureUp(groupKey: String, featureId: Int) {
        val sections = _state.value.sections.toMutableList()
        val groupIndex = sections.indexOfFirst { it.key == groupKey }
        if (groupIndex < 0) return
        val items = sections[groupIndex].items.toMutableList()
        val index = items.indexOfFirst { it.id == featureId }
        if (index > 0) {
            items[index] = items[index - 1].also { items[index - 1] = items[index] }
            sections[groupIndex] = sections[groupIndex].copy(items = items)
            _state.value = _state.value.copy(sections = sections)
            saveFeatureOrder(groupKey, items)
        }
    }

    fun moveFeatureDown(groupKey: String, featureId: Int) {
        val sections = _state.value.sections.toMutableList()
        val groupIndex = sections.indexOfFirst { it.key == groupKey }
        if (groupIndex < 0) return
        val items = sections[groupIndex].items.toMutableList()
        val index = items.indexOfFirst { it.id == featureId }
        if (index >= 0 && index < items.size - 1) {
            items[index] = items[index + 1].also { items[index + 1] = items[index] }
            sections[groupIndex] = sections[groupIndex].copy(items = items)
            _state.value = _state.value.copy(sections = sections)
            saveFeatureOrder(groupKey, items)
        }
    }

    fun resetOrder() {
        NavOrderPreference.resetAll(context)
        load()
    }

    private fun saveSectionOrder(sections: List<FeatureItemGroup>) {
        NavOrderPreference.setSectionOrder(context, sections.map { it.key })
    }

    private fun saveFeatureOrder(groupKey: String, items: List<FeatureItem>) {
        NavOrderPreference.setFeatureOrder(context, groupKey, items.map { it.id })
    }
}
