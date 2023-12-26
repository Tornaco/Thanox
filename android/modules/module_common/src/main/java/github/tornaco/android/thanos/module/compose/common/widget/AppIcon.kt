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

package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.skydoves.landscapist.glide.GlideImage
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.common.R
import github.tornaco.android.thanos.util.GlideApp

@Composable
fun AppIcon(modifier: Modifier, appInfo: AppInfo) {
    GlideImage(
        modifier = modifier,
        imageModel = { appInfo },
        failure = {
            Icon(
                painter = painterResource(R.mipmap.ic_fallback_app_icon),
                contentDescription = "failure"
            )
        },
        requestBuilder = {
            GlideApp.with(LocalContext.current.applicationContext).asDrawable()
                .transition(withCrossFade())
        },
    )
}