package now.fortuitous.thanos.launchother

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class RuleItem(val rule: String)

data class OpsState(
    val isLoading: Boolean,
    val ruleItems: List<RuleItem>
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class LaunchOtherAppRuleViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val _state =
        MutableStateFlow(
            OpsState(
                isLoading = true,
                ruleItems = emptyList()
            )
        )
    val state = _state.asStateFlow()

    private val thanos by lazy { ThanosManager.from(context) }
    private val supervisor by lazy { thanos.activityStackSupervisor }

    fun refresh() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val rules = withContext(Dispatchers.IO) {
                supervisor.allLaunchOtherAppRules.map { RuleItem(it) }
            }
            _state.value = _state.value.copy(ruleItems = rules, isLoading = false)
        }
    }

    fun add(rule: String) {
        supervisor.addLaunchOtherAppRule(rule)
        refresh()
    }

    fun remove(rule: String) {
        supervisor.deleteLaunchOtherAppRule(rule)
        refresh()
    }
}