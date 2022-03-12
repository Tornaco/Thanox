package github.tornaco.android.thanos.process.v2

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.google.android.material.composethemeadapter3.Mdc3Theme
import github.tornaco.android.thanos.app.BaseTrustedActivity
import github.tornaco.android.thanos.util.ActivityUtils

class ProcessManageActivityV2 : BaseTrustedActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, ProcessManageActivityV2::class.java)
        }
    }

    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mdc3Theme {
                Surface {
                    
                }
            }
        }
    }
}