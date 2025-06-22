package github.tornaco.android.thanos.module.compose.common.infra

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.common.R

sealed interface UiState<out R> {
    data object Loading : UiState<Nothing>
    data class Loaded<out T>(val data: T) : UiState<T>
    data class Error(val err: Throwable) : UiState<Nothing>
}


@Composable
fun Loading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 6.dp
        )
    }
}

@Composable
fun LoadError(
    modifier: Modifier = Modifier,
    message: String? = null,
    refresh: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp)
                .offset(y = (-80).dp)
                .fillMaxWidth(),
            painter = painterResource(
                id = R.drawable.ui_load_error
            ),
            contentDescription = null,
        )

        message?.let {
            TextButton(onClick = {
            }) {
                Text(
                    text = "ERROR",
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }

        refresh?.let {
            TextButton(onClick = { refresh() }) {
                Text(
                    text = "Retry",
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}


@Composable
fun EmptyList(modifier: Modifier = Modifier, refresh: (() -> Unit)? = null) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp)
                .offset(y = (-80).dp)
                .fillMaxWidth(),
            painter = painterResource(
                id = R.drawable.ui_empty_list
            ),
            contentDescription = null,
        )

        refresh?.let {
            TextButton(onClick = { refresh() }) {
                Text(
                    text = "Retry",
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}