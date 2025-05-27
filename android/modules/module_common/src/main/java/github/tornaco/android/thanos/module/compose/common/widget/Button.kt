@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DropdownButtonLayout(
    text: String,
    dropState: DropdownSelectorState,
    modifier: Modifier = Modifier
) {
    SplitButtonLayout(
        modifier = modifier,
        leadingButton = {
            SplitButtonDefaults.TonalLeadingButton(
                onClick = { /* Do Nothing */ },
            ) {
                Text(text)
            }
        },
        trailingButton = {
            SplitButtonDefaults.TonalTrailingButton(
                checked = dropState.isMenuOpen,
                onCheckedChange = {
                    dropState.open()
                },
            ) {
                val rotation: Float by
                animateFloatAsState(
                    targetValue = if (dropState.isMenuOpen) 180f else 0f,
                    label = "Trailing Icon Rotation"
                )
                androidx.compose.material3.Icon(
                    Icons.Filled.KeyboardArrowDown,
                    modifier =
                        Modifier
                            .size(SplitButtonDefaults.TrailingIconSize)
                            .graphicsLayer {
                                this.rotationZ = rotation
                            },
                    contentDescription = ""
                )
            }
        }
    )
}


@Composable
fun DropdownButtonLayout(
    text: String,
    isMenuOpen: Boolean,
    open: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector = Icons.Filled.FilterAlt,
    trailingIcon: ImageVector = Icons.Filled.KeyboardArrowDown
) {
    SplitButtonLayout(
        modifier = modifier,
        leadingButton = {
            SplitButtonDefaults.TonalLeadingButton(
                onClick = { /* Do Nothing */ },
            ) {
                androidx.compose.material3.Icon(
                    imageVector = leadingIcon,
                    contentDescription = ""
                )
                Text(text)
            }
        },
        trailingButton = {
            SplitButtonDefaults.TonalTrailingButton(
                checked = isMenuOpen,
                onCheckedChange = {
                    open()
                },
            ) {
                val rotation: Float by
                animateFloatAsState(
                    targetValue = if (isMenuOpen) 180f else 0f,
                    label = "Trailing Icon Rotation"
                )
                androidx.compose.material3.Icon(
                    trailingIcon,
                    modifier =
                        Modifier
                            .size(SplitButtonDefaults.TrailingIconSize)
                            .graphicsLayer {
                                this.rotationZ = rotation
                            },
                    contentDescription = ""
                )
            }
        }
    )
}


@Composable
fun <T> MenuButtonLayout(
    text: String,
    menuDialogState: MenuDialogState<T>,
    modifier: Modifier = Modifier
) {
    SplitButtonLayout(
        modifier = modifier,
        leadingButton = {
            SplitButtonDefaults.OutlinedLeadingButton(
                onClick = { /* Do Nothing */ },
            ) {
                Text(text)
            }
        },
        trailingButton = {
            SplitButtonDefaults.OutlinedTrailingButton(
                checked = menuDialogState.isShowing,
                onCheckedChange = {
                    menuDialogState.show()
                },
            ) {
                val rotation: Float by
                animateFloatAsState(
                    targetValue = if (menuDialogState.isShowing) 180f else 0f,
                    label = "Trailing Icon Rotation"
                )
                androidx.compose.material3.Icon(
                    Icons.Filled.KeyboardArrowDown,
                    modifier =
                        Modifier
                            .size(SplitButtonDefaults.TrailingIconSize)
                            .graphicsLayer {
                                this.rotationZ = rotation
                            },
                    contentDescription = ""
                )
            }
        }
    )
}