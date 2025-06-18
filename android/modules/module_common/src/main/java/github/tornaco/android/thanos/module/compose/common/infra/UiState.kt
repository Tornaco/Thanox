package github.tornaco.android.thanos.module.compose.common.infra

sealed interface UiState<out R> {
    data object Loading : UiState<Nothing>
    data class Loaded<out T>(val data: T) : UiState<T>
    data class Error(val err: Throwable) : UiState<Nothing>
}