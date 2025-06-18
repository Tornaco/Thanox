package github.tornaco.android.thanos.module.compose.common.infra

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface WithState<T> {
    val state: StateFlow<T>

    val initState: () -> T

    fun updateState(block: T.() -> T)

    fun updateState(t: T)

    fun resetState() {
        updateState(initState())
    }
}

class WithStateImpl<T>(override val initState: () -> T) : WithState<T> {
    private val _state by lazy { MutableStateFlow(initState()) }
    override val state get() = _state.asStateFlow()


    override fun updateState(block: T.() -> T) {
        _state.value = block.invoke(_state.value)
    }

    override fun updateState(t: T) {
        _state.value = t
    }

    override fun resetState() {
        updateState(initState())
    }
}