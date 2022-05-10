package github.tornaco.android.thanos.util

import android.annotation.SuppressLint
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun pleaseReadCarefully(
    scope: CoroutineScope,
    dialog: AlertDialog,
    seconds: Int = 8
) {
    var remainingSeconds = seconds
    val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    val positiveText = button.text

    button.isEnabled = false

    scope.launch {
        while (remainingSeconds > 0) {
            if (dialog.isShowing) button.text = "$remainingSeconds"
            delay(1000)
            --remainingSeconds
        }
        if (dialog.isShowing) {
            button.text = positiveText
            button.isEnabled = true
        }
    }
}

@SuppressLint("SetTextI18n")
fun pleaseReadCarefully(
    handler: Handler,
    dialog: AlertDialog,
    seconds: Int = 8
) {
    var remainingSeconds = seconds
    val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    val neutral = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)

    val positiveText = positive?.text ?: ""
    val neutralText = neutral?.text ?: ""

    positive?.isEnabled = false
    neutral?.isEnabled = false

    if (dialog.isShowing) positive?.text = "$positiveText ${remainingSeconds}s"
    if (dialog.isShowing) neutral?.text = "$neutralText ${remainingSeconds}s"

    handler.postDelayed(object : Runnable {
        override fun run() {
            --remainingSeconds
            if (remainingSeconds > 0) {
                if (dialog.isShowing) positive?.text = "$positiveText ${remainingSeconds}s"
                if (dialog.isShowing) neutral?.text = "$neutralText ${remainingSeconds}s"
                handler.postDelayed(this, 1000)
            } else {
                if (dialog.isShowing) {
                    positive?.text = positiveText
                    neutral?.text = neutralText
                    positive?.isEnabled = true
                    neutral?.isEnabled = true
                }
            }
        }

    }, 1000)
}