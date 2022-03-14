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

package github.tornaco.android.thanos.core.util

object ListsKt {
    fun <E> newArrayList(elements: Iterable<E>): ArrayList<E> {
        val list: ArrayList<E> = ArrayList()
        list.addAll(elements)
        return list
    }

    fun <E> newArrayList(vararg elements: E): ArrayList<E> {
        val list: ArrayList<E> = ArrayList()
        list.addAll(elements)
        return list
    }
}