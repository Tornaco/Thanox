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

package github.tornaco.android.thanos.core.wm

object WindowTypeMapping {

    fun <K, V> MutableMap<K, V>.add(data: Pair<K, V>) {
        this[data.first] = data.second
    }

    val map = mutableMapOf<String, String>().apply {
        add("2" to "TYPE_APPLICATION")
        add("3" to "TYPE_APPLICATION_STARTING")
        add("4" to "TYPE_DRAWN_APPLICATION")
        add("1000" to "TYPE_APPLICATION_PANEL")
        add("1001" to "TYPE_APPLICATION_MEDIA")
        add("1002" to "TYPE_APPLICATION_SUB_PANEL")
        add("1003" to "TYPE_APPLICATION_ATTACHED_DIALOG")
        add("1004" to "TYPE_APPLICATION_MEDIA_OVERLAY")
        add("1005" to "TYPE_APPLICATION_ABOVE_SUB_PANEL")
        add("2000" to "TYPE_STATUS_BAR")
        add("2001" to "TYPE_SEARCH_BAR")
        add("2002" to "TYPE_PHONE")
        add("2003" to "TYPE_SYSTEM_ALERT")
        add("2004" to "TYPE_KEYGUARD")
        add("2005" to "TYPE_TOAST")
        add("2006" to "TYPE_SYSTEM_OVERLAY")
        add("2007" to "TYPE_PRIORITY_PHONE")
        add("2008" to "TYPE_SYSTEM_DIALOG")
        add("2009" to "TYPE_KEYGUARD_DIALOG")
        add("2010" to "TYPE_SYSTEM_ERROR")
        add("2011" to "TYPE_INPUT_METHOD")
        add("2012" to "TYPE_INPUT_METHOD_DIALOG")
        add("2013" to "TYPE_WALLPAPER")
        add("2014" to "TYPE_STATUS_BAR_PANEL")
        add("2015" to "TYPE_SECURE_SYSTEM_OVERLAY")
        add("2016" to "TYPE_DRAG")
        add("2017" to "TYPE_STATUS_BAR_SUB_PANEL")
        add("2018" to "TYPE_POINTER")
        add("2019" to "TYPE_NAVIGATION_BAR")
        add("2020" to "TYPE_VOLUME_OVERLAY")
        add("2021" to "TYPE_BOOT_PROGRESS")
        add("2022" to "TYPE_INPUT_CONSUMER")
        add("2024" to "TYPE_NAVIGATION_BAR_PANEL")
        add("2026" to "TYPE_DISPLAY_OVERLAY")
        add("2027" to "TYPE_MAGNIFICATION_OVERLAY")
        add("2030" to "TYPE_PRIVATE_PRESENTATION")
        add("2031" to "TYPE_VOICE_INTERACTION")
        add("2032" to "TYPE_ACCESSIBILITY_OVERLAY")
        add("2033" to "TYPE_VOICE_INTERACTION_STARTING")
        add("2034" to "TYPE_DOCK_DIVIDER")
        add("2035" to "TYPE_QS_DIALOG")
        add("2036" to "TYPE_SCREENSHOT")
        add("2037" to "TYPE_PRESENTATION")
        add("2038" to "TYPE_APPLICATION_OVERLAY")
        add("2039" to "TYPE_ACCESSIBILITY_MAGNIFICATION_OVERLAY")
        add("2040" to "TYPE_NOTIFICATION_SHADE")
        add("2041" to "TYPE_STATUS_BAR_ADDITIONAL")
    }
}

