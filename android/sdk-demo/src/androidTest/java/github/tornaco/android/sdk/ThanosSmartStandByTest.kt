package github.tornaco.android.sdk

import android.content.pm.ApplicationInfo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import github.tornaco.android.thanos.core.util.PkgUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ThanosSmartStandByTest {
    @Test
    fun givenInstallPackages_whenLaunchAndBackToHome_thenSystemNotDie() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        runBlocking {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            PkgUtils.getInstalledApplicationsAsUser(appContext, 0, 0)
                .filter {
                    it.flags and ApplicationInfo.FLAG_SYSTEM == 0
                }
                .filter {
                    TestLog.println("Package: ${it.packageName}")
                    true
                }
                .mapNotNull {
                    appContext.packageManager.getLaunchIntentForPackage(it.packageName)
                }.forEach {
                    TestLog.println("Testing: $it")
                    appContext.startActivity(it)
                    delay(6000)
                    uiDevice.pressHome()
                    TestLog.println("Back to HOME")
                }
        }
    }
}