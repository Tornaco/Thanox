/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package github.tornaco.android.thanos.module.compose.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.tornaco.android.thanos.module.common.R
import github.tornaco.android.thanos.module.compose.common.theme.getColorAttribute

class FeatureDescriptionAndroidView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(context, attrs, defStyleAttr) {
    private val desc: MutableState<String> = mutableStateOf("")
    private val isShow: MutableState<Boolean> = mutableStateOf(true)

    private var onCloseClick: () -> Unit = {}

    init {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    }

    fun setDescription(desc: String) {
        this.desc.value = desc
    }

    fun setOnCloseClickListener(onCloseClick: () -> Unit) {
        this.onCloseClick = onCloseClick
    }

    @Composable
    override fun Content() {
        AnimatedVisibility(visible = isShow.value) {
            FeatureDescription(modifier = Modifier, text = desc.value) {
                onCloseClick()
            }
        }
    }
}

@Composable
fun FeatureDescription(modifier: Modifier = Modifier, text: String, close: () -> Unit) {
    val primaryColor = getColorAttribute(R.attr.appCardBackground)

    Box(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .background(color = Color(0XFFFFECB3))
        .padding(16.dp)) {

        Text(modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(end = 32.dp),
            text = text,
            fontSize = 10.sp)

        FilledTonalIconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(24.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors(Color(primaryColor)),
            onClick = { close() }) {
            Icon(
                modifier = Modifier
                    .size(12.dp),
                imageVector = Icons.Filled.Check,
                contentDescription = "Check"
            )
        }
    }
}