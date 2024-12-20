package github.tornaco.android.thanos.common

sealed interface UiState<out R> {
    data object Loading : UiState<Nothing>
    data class Loaded<out T>(val data: T) : UiState<T>
    data class Error(val err: Throwable) : UiState<Nothing>
}