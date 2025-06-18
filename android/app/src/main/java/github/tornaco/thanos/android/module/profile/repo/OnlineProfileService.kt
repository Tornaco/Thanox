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

package github.tornaco.thanos.android.module.profile.repo

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface OnlineProfileService {

    @GET("/repos/tornaco/thanox/contents/files/profile/profiles/")
    suspend fun getProfileFiles(): List<GithubFileInfo>

    @GET("/repos/tornaco/thanox/contents/files/profile/profiles/{name}")
    suspend fun getProfileFileContent(@Path("name") name: String): GithubFileContent

    object Factory {
        private const val BASE_URL = "https://api.github.com"

        fun create(context: Context): OnlineProfileService {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(ChuckerInterceptor.Builder(context).build())
            val client = builder.build()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
            return retrofit.create(OnlineProfileService::class.java)
        }
    }
}