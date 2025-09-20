package github.tornaco.practice.honeycomb.locker.ui.verify

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.infra.LifeCycleAwareViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PinState(
    val step: PinStep = PinStep.First,
    val pin: String = ""
)

sealed interface PinStep {
    data object First : PinStep
    data object Second : PinStep
    data object Done : PinStep
}

sealed interface PinSettingsEvent {
    data object PinMisMatch : PinSettingsEvent
    data object Done : PinSettingsEvent
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class PinSettingsVM @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {

    private val _state = MutableStateFlow(
        PinState()
    )
    val state = _state.asStateFlow()

    val events = MutableSharedFlow<PinSettingsEvent>()

    private val thanox by lazy { ThanosManager.from(context) }

    fun inputPin(pin: String) {
        when (state.value.step) {
            PinStep.First -> {
                _state.update {
                    it.copy(pin = pin, step = PinStep.Second)
                }
            }

            PinStep.Second -> {
                if (pin != state.value.pin) {
                    _state.update {
                        it.copy(pin = "", step = PinStep.First)
                    }
                    viewModelScope.launch { events.emit(PinSettingsEvent.PinMisMatch) }
                } else {
                    _state.update {
                        it.copy(step = PinStep.Done)
                    }
                    thanox.activityStackSupervisor.setLockPin(pin)
                    viewModelScope.launch { events.emit(PinSettingsEvent.Done) }
                }
            }

            else -> {

            }
        }
    }
}