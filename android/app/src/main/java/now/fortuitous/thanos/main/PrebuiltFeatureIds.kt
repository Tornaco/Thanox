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

package now.fortuitous.thanos.main

object PrebuiltFeatureIds {
    const val ID_ONE_KEY_CLEAR = 1
    const val ID_BACKGROUND_START = 2
    const val ID_BACKGROUND_RESTRICT = 3
    const val ID_CLEAN_TASK_REMOVAL = 4
    const val ID_APPS_MANAGER = 5
    const val ID_PRIVACY_CHEAT = 6
    const val ID_OPS_BY_APP = 7
    const val ID_OPS_BY_OPS = 8
    const val ID_APP_LOCK = 9
    const val ID_TASK_BLUR = 10
    const val ID_OP_REMIND = 11

    const val ID_SCREEN_ON_NOTIFICATION = 12

    const val ID_NOTIFICATION_RECORDER = 13
    const val ID_TRAMPOLINE = 14
    const val ID_PROFILE = 15
    const val ID_SMART_STANDBY = 16

    const val ID_WECHAT_PUSH = 17
    const val ID_SMART_FREEZE = 18
    const val ID_INFINITE_Z = 19

    @Deprecated("Not supported")
    const val ID_PLUGINS = 20

    @Deprecated("Moved to Settings")
    const val ID_FEEDBACK = 21
    const val ID_GUIDE = 22
    const val ID_WAKELOCK_REMOVER = 23
    const val ID_NOTIFICATION_CENTER = 24

    const val ID_LAUNCH_OTHER_APP_BLOCKER = 25
    const val ID_RESIDENT = 26
    const val ID_SENSOR_OFF = 27

    const val ID_THANOX_OPS = 28
    const val ID_PROCESS_MANAGER = 29

    fun Int.isValidId() = this >= ID_ONE_KEY_CLEAR
}