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

package util

import android.content.Context
import com.elvishew.xlog.XLog
import java.io.File
import java.io.IOException

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

object AssetUtilsKt {

    @Throws(IOException::class)
    fun readFileToString(context: Context, name: String): String {
        val am = context.assets
        val input = am.open(name)
        return input.readBytes().decodeToString()
    }

    fun readFileToStringNoThrow(context: Context, name: String): String? {
        return try {
            readFileToString(context, name)
        } catch (e: IOException) {
            XLog.e("readFileToStringOrThrow", e)
            null
        }
    }

    @Throws(IOException::class)
    fun copyTo(context: Context, name: String, dest: File) {
        val am = context.assets
        val input = am.open(name)
        input.copyTo(dest.outputStream())
    }
}