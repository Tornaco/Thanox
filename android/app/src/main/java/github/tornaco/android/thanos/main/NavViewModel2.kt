package github.tornaco.android.thanos.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class FeatureItem(
    val id: Int = 0,
    @StringRes
    val titleRes: Int,
    @DrawableRes
    val iconRes: Int,
    val requiredFeature: String? = null
)

data class FeatureItemGroup(
    @StringRes
    val titleRes: Int,
    val items: List<FeatureItem>
)

data class NavState(val isLoading: Boolean, val features: List<FeatureItemGroup>)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class NavViewModel2 @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {

    private val _state =
        MutableStateFlow(NavState(isLoading = false, features = emptyList()))
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun loadFeatures() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            withContext(Dispatchers.IO) {
                val all = PrebuiltFeatures.all()
                _state.value = _state.value.copy(features = all)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun refresh() {

    }
}