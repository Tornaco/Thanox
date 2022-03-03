package github.tornaco.android.thanos

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    var uiHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiHandler = Handler(Looper.getMainLooper())
    }

    fun postOnUiDelayed(runnable: Runnable, delay: Long) {
        uiHandler?.postDelayed(runnable, delay)
    }

    fun runOnUiThread(runnable: Runnable) {
        uiHandler?.post(runnable)
    }

    open fun onBackPressed() = false
}