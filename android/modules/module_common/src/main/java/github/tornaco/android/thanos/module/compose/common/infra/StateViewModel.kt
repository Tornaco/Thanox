package github.tornaco.android.thanos.module.compose.common.infra

abstract class StateViewModel<T>(
    initState: () -> T,
    private val stateImpl: WithStateImpl<T> = WithStateImpl(initState)
) :
    LifeCycleAwareViewModel(), WithState<T> by stateImpl {
}