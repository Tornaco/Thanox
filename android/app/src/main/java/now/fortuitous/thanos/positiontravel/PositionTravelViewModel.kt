package now.fortuitous.thanos.positiontravel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import javax.inject.Inject

data class PositionTravelUiState(
    val isEnabled: Boolean = false,
    val latitude: String = "39.9042",
    val longitude: String = "116.4074",
    val isLoading: Boolean = true,
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class PositionTravelViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    var uiState by mutableStateOf(PositionTravelUiState())
        private set

    private val thanos by lazy { ThanosManager.from(context) }

    fun loadState() {
        try {
            val ptm = thanos.positionTravelManager
            uiState = PositionTravelUiState(
                isEnabled = ptm.isEnabled,
                latitude = ptm.latitude.toString(),
                longitude = ptm.longitude.toString(),
                isLoading = false,
            )
        } catch (e: Throwable) {
            XLog.e("PositionTravelVM: loadState error", e)
            uiState = uiState.copy(isLoading = false)
        }
    }

    fun setEnabled(enabled: Boolean) {
        try {
            thanos.positionTravelManager.setEnabled(enabled)
            uiState = uiState.copy(isEnabled = enabled)
        } catch (e: Throwable) {
            XLog.e("PositionTravelVM: setEnabled error", e)
        }
    }

    fun saveCoordinates(latStr: String, lonStr: String) {
        val lat = latStr.toDoubleOrNull()
        val lon = lonStr.toDoubleOrNull()
        if (lat == null || lon == null) return
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) return
        try {
            thanos.positionTravelManager.setCoordinates(lat, lon)
            uiState = uiState.copy(latitude = latStr, longitude = lonStr)
        } catch (e: Throwable) {
            XLog.e("PositionTravelVM: saveCoordinates error", e)
        }
    }
}
