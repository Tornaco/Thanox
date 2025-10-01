package github.tornaco.practice.honeycomb.locker.ui.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.compose.common.widget.AppIcon
import github.tornaco.android.thanos.module.compose.common.widget.LargeTitle
import github.tornaco.android.thanos.module.compose.common.widget.MediumSpacer

@Composable
fun PinInputContent(
    appInfo: AppInfo?,
    title: String = stringResource(id = github.tornaco.android.thanos.res.R.string.module_locker_app_name),
    customHint: String? = null,
    onVerifyPin: (pin: String) -> Boolean,
    onSuccess: () -> Unit,
    onFailure: () -> Unit = {},
    shouldClearInput: Boolean = false,
    onClearInputHandled: () -> Unit = {},
    maxPinLength: Int = 6
) {
    var pinInput by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var internalShouldClearInput by remember { mutableStateOf(false) }

    // Clear input when internal shouldClearInput flag changes to true
    LaunchedEffect(internalShouldClearInput) {
        if (internalShouldClearInput) {
            pinInput = ""
            internalShouldClearInput = false
        }
    }

    // Clear input when external shouldClearInput flag changes to true
    LaunchedEffect(shouldClearInput) {
        if (shouldClearInput) {
            pinInput = ""
            onClearInputHandled()
        }
    }
    // maxPinLength 由调用方决定，默认为6；时间公式可传入动态长度

    // Handle PIN input completion
    fun handlePinComplete(pin: String) {
        val isCorrect = onVerifyPin(pin)
        internalShouldClearInput = true
        if (isCorrect) {
            showError = false
            onSuccess()
        } else {
            showError = true
            onFailure()
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            appInfo?.let { AppIcon(modifier = Modifier.size(80.dp), appInfo = it) }
            MediumSpacer()
            LargeTitle(text = title)
            
            // Custom hint display
            customHint?.let { hint ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // PIN dots display
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(maxPinLength) { index ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(
                                if (index < pinInput.length) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                }
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (showError) {
                Text(
                    text = stringResource(id = github.tornaco.android.thanos.res.R.string.module_locker_verify_error_pin_mismatch),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Number pad
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Row 1: 1, 2, 3
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    repeat(3) { index ->
                        NumberButton(
                            number = (index + 1).toString(),
                            onClick = {
                                if (pinInput.length < maxPinLength) {
                                    showError = false  // Clear error when user starts typing
                                    pinInput += (index + 1).toString()
                                    if (pinInput.length == maxPinLength) {
                                        handlePinComplete(pinInput)
                                    }
                                }
                            }
                        )
                    }
                }

                // Row 2: 4, 5, 6
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    repeat(3) { index ->
                        NumberButton(
                            number = (index + 4).toString(),
                            onClick = {
                                if (pinInput.length < maxPinLength) {
                                    showError = false  // Clear error when user starts typing
                                    pinInput += (index + 4).toString()
                                    if (pinInput.length == maxPinLength) {
                                        handlePinComplete(pinInput)
                                    }
                                }
                            }
                        )
                    }
                }

                // Row 3: 7, 8, 9
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    repeat(3) { index ->
                        NumberButton(
                            number = (index + 7).toString(),
                            onClick = {
                                if (pinInput.length < maxPinLength) {
                                    showError = false  // Clear error when user starts typing
                                    pinInput += (index + 7).toString()
                                    if (pinInput.length == maxPinLength) {
                                        handlePinComplete(pinInput)
                                    }
                                }
                            }
                        )
                    }
                }

                // Row 4: empty, 0, backspace
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Empty space
                    Spacer(modifier = Modifier.size(72.dp))

                    // 0 button
                    NumberButton(
                        number = "0",
                        onClick = {
                            if (pinInput.length < maxPinLength) {
                                showError = false  // Clear error when user starts typing
                                pinInput += "0"
                                if (pinInput.length == maxPinLength) {
                                    handlePinComplete(pinInput)
                                }
                            }
                        }
                    )

                    // Backspace button
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (pinInput.isNotEmpty()) {
                                    showError = false  // Clear error when user starts editing
                                    pinInput = pinInput.dropLast(1)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Backspace,
                            contentDescription = "Backspace",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberButton(
    number: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}