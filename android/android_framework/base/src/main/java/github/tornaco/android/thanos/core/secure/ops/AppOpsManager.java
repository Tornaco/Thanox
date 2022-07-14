/*
 * Copyright (C) 2012 The Android Open Source Project
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
 */

package github.tornaco.android.thanos.core.secure.ops;

import android.annotation.TestApi;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import github.tornaco.android.thanos.core.compat.ManifestCompat;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@SuppressWarnings("JavaDoc")
@AllArgsConstructor
public class AppOpsManager {

    public static final int MODE_ALLOWED = 0;
    public static final int MODE_IGNORED = 1;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_FOREGROUND = 4;

    public static final int WATCH_FOREGROUND_CHANGES = 1 << 0;

    public static final String[] MODE_NAMES = new String[]{
            "allow",        // MODE_ALLOWED
            "ignore",       // MODE_IGNORED
            "deny",         // MODE_ERRORED
            "default",      // MODE_DEFAULT
            "foreground",   // MODE_FOREGROUND
    };

    public static final int[] FOREGROUND_ONLY_OP = new int[]{
            AppOpsManager.OP_CHANGE_BRIGHTNESS,
            AppOpsManager.OP_RUN_ANY_IN_BACKGROUND,
            AppOpsManager.OP_ANSWER_PHONE_CALLS,
            AppOpsManager.OP_INSTANT_APP_START_FOREGROUND,
    };

    /**
     * Metrics about an op when its uid is persistent.
     *
     * @hide
     */
    public static final int UID_STATE_PERSISTENT = 0;

    /**
     * Metrics about an op when its uid is at the top.
     *
     * @hide
     */
    public static final int UID_STATE_TOP = 1;

    /**
     * Metrics about an op when its uid is running a foreground service.
     *
     * @hide
     */
    public static final int UID_STATE_FOREGROUND_SERVICE = 2;

    /**
     * Last UID state in which we don't restrict what an op can do.
     *
     * @hide
     */
    public static final int UID_STATE_LAST_NON_RESTRICTED = UID_STATE_FOREGROUND_SERVICE;

    /**
     * Metrics about an op when its uid is in the foreground for any other reasons.
     *
     * @hide
     */
    public static final int UID_STATE_FOREGROUND = 3;

    /**
     * Metrics about an op when its uid is in the background for any reason.
     *
     * @hide
     */
    public static final int UID_STATE_BACKGROUND = 4;

    /**
     * Metrics about an op when its uid is cached.
     *
     * @hide
     */
    public static final int UID_STATE_CACHED = 5;

    /**
     * Number of uid states we track.
     *
     * @hide
     */
    public static final int _NUM_UID_STATE = 6;

    // when adding one of these:
    //  - increment _NUM_OP
    //  - define an OPSTR_* constant (marked as @SystemApi)
    //  - add rows to sOpToSwitch, sOpToString, sOpNames, sOpToPerms, sOpDefault
    //  - add descriptive strings to Settings/res/values/arrays.xml
    //  - add the op to the appropriate template in AppOpsState.OpsTemplate (settings app)

    /**
     * @hide No operation specified.
     */
    public static final int OP_NONE = -1;
    /**
     * @hide Access to coarse location information.
     */
    public static final int OP_COARSE_LOCATION = 0;
    /**
     * @hide Access to fine location information.
     */
    public static final int OP_FINE_LOCATION = 1;
    /**
     * @hide Causing GPS to run.
     */
    public static final int OP_GPS = 2;
    /**
     * @hide
     */
    public static final int OP_VIBRATE = 3;
    /**
     * @hide
     */
    public static final int OP_READ_CONTACTS = 4;
    /**
     * @hide
     */
    public static final int OP_WRITE_CONTACTS = 5;
    /**
     * @hide
     */
    public static final int OP_READ_CALL_LOG = 6;
    /**
     * @hide
     */
    public static final int OP_WRITE_CALL_LOG = 7;
    /**
     * @hide
     */
    public static final int OP_READ_CALENDAR = 8;
    /**
     * @hide
     */
    public static final int OP_WRITE_CALENDAR = 9;
    /**
     * @hide
     */
    public static final int OP_WIFI_SCAN = 10;
    /**
     * @hide
     */
    public static final int OP_POST_NOTIFICATION = 11;
    /**
     * @hide
     */
    public static final int OP_NEIGHBORING_CELLS = 12;
    /**
     * @hide
     */
    public static final int OP_CALL_PHONE = 13;
    /**
     * @hide
     */
    public static final int OP_READ_SMS = 14;
    /**
     * @hide
     */
    public static final int OP_WRITE_SMS = 15;
    /**
     * @hide
     */
    public static final int OP_RECEIVE_SMS = 16;
    /**
     * @hide
     */
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    /**
     * @hide
     */
    public static final int OP_RECEIVE_MMS = 18;
    /**
     * @hide
     */
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    /**
     * @hide
     */
    public static final int OP_SEND_SMS = 20;
    /**
     * @hide
     */
    public static final int OP_READ_ICC_SMS = 21;
    /**
     * @hide
     */
    public static final int OP_WRITE_ICC_SMS = 22;
    /**
     * @hide
     */
    public static final int OP_WRITE_SETTINGS = 23;
    /**
     * @hide Required to draw on top of other apps.
     */
    @TestApi
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    /**
     * @hide
     */
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    /**
     * @hide
     */
    public static final int OP_CAMERA = 26;
    /**
     * @hide
     */
    @TestApi
    public static final int OP_RECORD_AUDIO = 27;
    /**
     * @hide
     */
    public static final int OP_PLAY_AUDIO = 28;
    /**
     * @hide
     */
    public static final int OP_READ_CLIPBOARD = 29;
    /**
     * @hide
     */
    public static final int OP_WRITE_CLIPBOARD = 30;
    /**
     * @hide
     */
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;
    /**
     * @hide
     */
    public static final int OP_TAKE_AUDIO_FOCUS = 32;
    /**
     * @hide
     */
    public static final int OP_AUDIO_MASTER_VOLUME = 33;
    /**
     * @hide
     */
    public static final int OP_AUDIO_VOICE_VOLUME = 34;
    /**
     * @hide
     */
    public static final int OP_AUDIO_RING_VOLUME = 35;
    /**
     * @hide
     */
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;
    /**
     * @hide
     */
    public static final int OP_AUDIO_ALARM_VOLUME = 37;
    /**
     * @hide
     */
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    /**
     * @hide
     */
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    /**
     * @hide
     */
    public static final int OP_WAKE_LOCK = 40;
    /**
     * @hide Continually monitoring location data.
     */
    public static final int OP_MONITOR_LOCATION = 41;
    /**
     * @hide Continually monitoring location data with a relatively high power request.
     */
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    /**
     * @hide Retrieve current usage stats via {@link UsageStatsManager}.
     */
    public static final int OP_GET_USAGE_STATS = 43;
    /**
     * @hide
     */
    public static final int OP_MUTE_MICROPHONE = 44;
    /**
     * @hide
     */
    public static final int OP_TOAST_WINDOW = 45;
    /**
     * @hide Capture the device's display contents and/or audio
     */
    public static final int OP_PROJECT_MEDIA = 46;
    /**
     * @hide Activate a VPN connection without user intervention.
     */
    public static final int OP_ACTIVATE_VPN = 47;
    /**
     * @hide Access the WallpaperManagerAPI to write wallpapers.
     */
    public static final int OP_WRITE_WALLPAPER = 48;
    /**
     * @hide Received the assist structure from an app.
     */
    public static final int OP_ASSIST_STRUCTURE = 49;
    /**
     * @hide Received a screenshot from assist.
     */
    public static final int OP_ASSIST_SCREENSHOT = 50;
    /**
     * @hide Read the phone state.
     */
    public static final int OP_READ_PHONE_STATE = 51;
    /**
     * @hide Add voicemail messages to the voicemail content provider.
     */
    public static final int OP_ADD_VOICEMAIL = 52;
    /**
     * @hide Access APIs for SIP calling over VOIP or WiFi.
     */
    public static final int OP_USE_SIP = 53;
    /**
     * @hide Intercept outgoing calls.
     */
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;
    /**
     * @hide User the fingerprint API.
     */
    public static final int OP_USE_FINGERPRINT = 55;
    /**
     * @hide Access to body sensors such as heart rate, etc.
     */
    public static final int OP_BODY_SENSORS = 56;
    /**
     * @hide Read previously received cell broadcast messages.
     */
    public static final int OP_READ_CELL_BROADCASTS = 57;
    /**
     * @hide Inject mock location into the system.
     */
    public static final int OP_MOCK_LOCATION = 58;
    /**
     * @hide Read external storage.
     */
    public static final int OP_READ_EXTERNAL_STORAGE = 59;
    /**
     * @hide Write external storage.
     */
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    /**
     * @hide Turned on the screen.
     */
    public static final int OP_TURN_SCREEN_ON = 61;
    /**
     * @hide Get device accounts.
     */
    public static final int OP_GET_ACCOUNTS = 62;
    /**
     * @hide Control whether an application is allowed to run in the background.
     */
    public static final int OP_RUN_IN_BACKGROUND = 63;
    /**
     * @hide
     */
    public static final int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    /**
     * @hide Read the phone number.
     */
    public static final int OP_READ_PHONE_NUMBERS = 65;
    /**
     * @hide Request package installs through package installer
     */
    public static final int OP_REQUEST_INSTALL_PACKAGES = 66;
    /**
     * @hide Enter picture-in-picture.
     */
    public static final int OP_PICTURE_IN_PICTURE = 67;
    /**
     * @hide Instant app start foreground service.
     */
    public static final int OP_INSTANT_APP_START_FOREGROUND = 68;
    /**
     * @hide Answer incoming phone calls
     */
    public static final int OP_ANSWER_PHONE_CALLS = 69;
    /**
     * @hide Run jobs when in background
     */
    public static final int OP_RUN_ANY_IN_BACKGROUND = 70;
    /**
     * @hide Change Wi-Fi connectivity state
     */
    public static final int OP_CHANGE_WIFI_STATE = 71;
    /**
     * @hide Request package deletion through package installer
     */
    public static final int OP_REQUEST_DELETE_PACKAGES = 72;
    /**
     * @hide Bind an accessibility service.
     */
    public static final int OP_BIND_ACCESSIBILITY_SERVICE = 73;
    /**
     * @hide Continue handover of a call from another app
     */
    public static final int OP_ACCEPT_HANDOVER = 74;
    /**
     * @hide Create and Manage IPsec Tunnels
     */
    public static final int OP_MANAGE_IPSEC_TUNNELS = 75;
    /**
     * @hide Any app start foreground service.
     */
    public static final int OP_START_FOREGROUND = 76;
    /**
     * @hide
     */
    public static final int OP_BLUETOOTH_SCAN = 77;

    /**
     * @hide Use the BiometricPrompt/BiometricManager APIs.
     */
    public static final int OP_USE_BIOMETRIC = 78;
    /**
     * @hide Physical activity recognition.
     */
    public static final int OP_ACTIVITY_RECOGNITION = 79;
    /**
     * @hide Financial app sms read.
     */
    public static final int OP_SMS_FINANCIAL_TRANSACTIONS = 80;
    /**
     * @hide Read media of audio type.
     */
    public static final int OP_READ_MEDIA_AUDIO = 81;
    /**
     * @hide Write media of audio type.
     */
    public static final int OP_WRITE_MEDIA_AUDIO = 82;
    /**
     * @hide Read media of video type.
     */
    public static final int OP_READ_MEDIA_VIDEO = 83;
    /**
     * @hide Write media of video type.
     */
    public static final int OP_WRITE_MEDIA_VIDEO = 84;
    /**
     * @hide Read media of image type.
     */
    public static final int OP_READ_MEDIA_IMAGES = 85;
    /**
     * @hide Write media of image type.
     */
    public static final int OP_WRITE_MEDIA_IMAGES = 86;
    /**
     * @hide Has a legacy (non-isolated) view of storage.
     */
    public static final int OP_LEGACY_STORAGE = 87;
    /**
     * @hide Accessing accessibility features
     */
    public static final int OP_ACCESS_ACCESSIBILITY = 88;
    /**
     * @hide Read the device identifiers (IMEI / MEID, IMSI, SIM / Build serial)
     */
    public static final int OP_READ_DEVICE_IDENTIFIERS = 89;

    public static final int OP_ACCESS_MEDIA_LOCATION = AppProtoEnums.APP_OP_ACCESS_MEDIA_LOCATION;
    /**
     * @hide Query all apps on device, regardless of declarations in the calling app manifest
     */
    public static final int OP_QUERY_ALL_PACKAGES = AppProtoEnums.APP_OP_QUERY_ALL_PACKAGES;
    /**
     * @hide Access all external storage
     */
    public static final int OP_MANAGE_EXTERNAL_STORAGE =
            AppProtoEnums.APP_OP_MANAGE_EXTERNAL_STORAGE;
    /**
     * @hide Communicate cross-profile within the same profile group.
     */
    public static final int OP_INTERACT_ACROSS_PROFILES =
            AppProtoEnums.APP_OP_INTERACT_ACROSS_PROFILES;
    /**
     * Start (without additional user intervention) a Platform VPN connection, as used by VpnManager
     *
     * <p>This appop is granted to apps that have already been given user consent to start Platform
     * VPN connections. This appop is insufficient to start VpnService based VPNs; OP_ACTIVATE_VPN
     * is needed for that.
     *
     * @hide
     */
    public static final int OP_ACTIVATE_PLATFORM_VPN = AppProtoEnums.APP_OP_ACTIVATE_PLATFORM_VPN;
    /**
     * @hide Controls whether or not read logs are available for incremental installations.
     */
    public static final int OP_LOADER_USAGE_STATS = AppProtoEnums.APP_OP_LOADER_USAGE_STATS;

    // App op deprecated/removed.
    private static final int OP_DEPRECATED_1 = AppProtoEnums.APP_OP_DEPRECATED_1;

    /**
     * @hide Auto-revoke app permissions if app is unused for an extended period
     */
    public static final int OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED =
            AppProtoEnums.APP_OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED;

    /**
     * Whether {@link #OP_AUTO_REVOKE_PERMISSIONS_IF_UNUSED} is allowed to be changed by
     * the installer
     *
     * @hide
     */
    public static final int OP_AUTO_REVOKE_MANAGED_BY_INSTALLER =
            AppProtoEnums.APP_OP_AUTO_REVOKE_MANAGED_BY_INSTALLER;

    /**
     * @hide
     */
    public static final int OP_NO_ISOLATED_STORAGE = AppProtoEnums.APP_OP_NO_ISOLATED_STORAGE;

    // Update guide
    // 1. Add New OP from Android
    // 2. Fix _OP_ANDROID_LAST
    // 3. Fix NUM_OP
    // 4. Fix OPS title summary icon
    public static final int _OP_ANDROID_BASE = 0;
    public static final int _OP_THANOX_BASE = 1000099;

    public static final int OP_GET_PACKAGE_INFO = _OP_THANOX_BASE;
    public static final int OP_CHANGE_BRIGHTNESS = _OP_THANOX_BASE + 1;
    public static final int OP_GET_INSTALLED_PACKAGES = _OP_THANOX_BASE + 2;
    public static final int OP_QUERY_INTENT_ACTIVITIES = _OP_THANOX_BASE + 3;
    public static final int OP_ANY_LOCATION = _OP_THANOX_BASE + 4;

    public static final int _OP_ANDROID_LAST = OP_NO_ISOLATED_STORAGE;
    public static final int _OP_THANOX_LAST = OP_ANY_LOCATION;

    /**
     * Access to coarse location information.
     */
    public static final String OPSTR_COARSE_LOCATION = "android:coarse_location";
    /**
     * Access to fine location information.
     */
    public static final String OPSTR_FINE_LOCATION =
            "android:fine_location";
    /**
     * Continually monitoring location data.
     */
    public static final String OPSTR_MONITOR_LOCATION
            = "android:monitor_location";
    /**
     * Continually monitoring location data with a relatively high power request.
     */
    public static final String OPSTR_MONITOR_HIGH_POWER_LOCATION
            = "android:monitor_location_high_power";
    /**
     * Access to {@link android.app.usage.UsageStatsManager}.
     */
    public static final String OPSTR_GET_USAGE_STATS
            = "android:get_usage_stats";
    /**
     * Activate a VPN connection without user intervention. @hide
     */
    public static final String OPSTR_ACTIVATE_VPN
            = "android:activate_vpn";
    /**
     * Allows an application to read the user's contacts data.
     */
    public static final String OPSTR_READ_CONTACTS
            = "android:read_contacts";
    /**
     * Allows an application to write to the user's contacts data.
     */
    public static final String OPSTR_WRITE_CONTACTS
            = "android:write_contacts";
    /**
     * Allows an application to read the user's call log.
     */
    public static final String OPSTR_READ_CALL_LOG
            = "android:read_call_log";
    /**
     * Allows an application to write to the user's call log.
     */
    public static final String OPSTR_WRITE_CALL_LOG
            = "android:write_call_log";
    /**
     * Allows an application to read the user's calendar data.
     */
    public static final String OPSTR_READ_CALENDAR
            = "android:read_calendar";
    /**
     * Allows an application to write to the user's calendar data.
     */
    public static final String OPSTR_WRITE_CALENDAR
            = "android:write_calendar";
    /**
     * Allows an application to initiate a phone call.
     */
    public static final String OPSTR_CALL_PHONE
            = "android:call_phone";
    /**
     * Allows an application to read SMS messages.
     */
    public static final String OPSTR_READ_SMS
            = "android:read_sms";
    /**
     * Allows an application to receive SMS messages.
     */
    public static final String OPSTR_RECEIVE_SMS
            = "android:receive_sms";
    /**
     * Allows an application to receive MMS messages.
     */
    public static final String OPSTR_RECEIVE_MMS
            = "android:receive_mms";
    /**
     * Allows an application to receive WAP push messages.
     */
    public static final String OPSTR_RECEIVE_WAP_PUSH
            = "android:receive_wap_push";
    /**
     * Allows an application to send SMS messages.
     */
    public static final String OPSTR_SEND_SMS
            = "android:send_sms";
    /**
     * Required to be able to access the camera device.
     */
    public static final String OPSTR_CAMERA
            = "android:camera";
    /**
     * Required to be able to access the microphone device.
     */
    public static final String OPSTR_RECORD_AUDIO
            = "android:record_audio";
    /**
     * Required to access phone state related information.
     */
    public static final String OPSTR_READ_PHONE_STATE
            = "android:read_phone_state";
    /**
     * Required to access phone state related information.
     */
    public static final String OPSTR_ADD_VOICEMAIL
            = "android:add_voicemail";
    /**
     * Access APIs for SIP calling over VOIP or WiFi
     */
    public static final String OPSTR_USE_SIP
            = "android:use_sip";
    /**
     * Access APIs for diverting outgoing calls
     */
    public static final String OPSTR_PROCESS_OUTGOING_CALLS
            = "android:process_outgoing_calls";
    /**
     * Use the fingerprint API.
     */
    public static final String OPSTR_USE_FINGERPRINT
            = "android:use_fingerprint";
    /**
     * Access to body sensors such as heart rate, etc.
     */
    public static final String OPSTR_BODY_SENSORS
            = "android:body_sensors";
    /**
     * Read previously received cell broadcast messages.
     */
    public static final String OPSTR_READ_CELL_BROADCASTS
            = "android:read_cell_broadcasts";
    /**
     * Inject mock location into the system.
     */
    public static final String OPSTR_MOCK_LOCATION
            = "android:mock_location";
    /**
     * Read external storage.
     */
    public static final String OPSTR_READ_EXTERNAL_STORAGE
            = "android:read_external_storage";
    /**
     * Write external storage.
     */
    public static final String OPSTR_WRITE_EXTERNAL_STORAGE
            = "android:write_external_storage";
    /**
     * Required to draw on top of other apps.
     */
    public static final String OPSTR_SYSTEM_ALERT_WINDOW
            = "android:system_alert_window";
    /**
     * Required to write/modify/update system settings.
     */
    public static final String OPSTR_WRITE_SETTINGS
            = "android:write_settings";
    /**
     * @hide Get device accounts.
     */
    public static final String OPSTR_GET_ACCOUNTS
            = "android:get_accounts";
    public static final String OPSTR_READ_PHONE_NUMBERS
            = "android:read_phone_numbers";
    /**
     * Access to picture-in-picture.
     */
    public static final String OPSTR_PICTURE_IN_PICTURE
            = "android:picture_in_picture";
    /**
     * @hide
     */
    public static final String OPSTR_INSTANT_APP_START_FOREGROUND
            = "android:instant_app_start_foreground";
    /**
     * Answer incoming phone calls
     */
    public static final String OPSTR_ANSWER_PHONE_CALLS
            = "android:answer_phone_calls";
    /**
     * Accept call handover
     *
     * @hide
     */
    public static final String OPSTR_ACCEPT_HANDOVER
            = "android:accept_handover";
    /**
     * @hide
     */
    public static final String OPSTR_GPS = "android:gps";
    /**
     * @hide
     */
    public static final String OPSTR_VIBRATE = "android:vibrate";
    /**
     * @hide
     */
    public static final String OPSTR_WIFI_SCAN = "android:wifi_scan";
    /**
     * @hide
     */
    public static final String OPSTR_POST_NOTIFICATION = "android:post_notification";
    /**
     * @hide
     */
    public static final String OPSTR_NEIGHBORING_CELLS = "android:neighboring_cells";
    /**
     * @hide
     */
    public static final String OPSTR_WRITE_SMS = "android:write_sms";
    /**
     * @hide
     */
    public static final String OPSTR_RECEIVE_EMERGENCY_BROADCAST =
            "android:receive_emergency_broadcast";
    /**
     * @hide
     */
    public static final String OPSTR_READ_ICC_SMS = "android:read_icc_sms";
    /**
     * @hide
     */
    public static final String OPSTR_WRITE_ICC_SMS = "android:write_icc_sms";
    /**
     * @hide
     */
    public static final String OPSTR_ACCESS_NOTIFICATIONS = "android:access_notifications";
    /**
     * @hide
     */
    public static final String OPSTR_PLAY_AUDIO = "android:play_audio";
    /**
     * @hide
     */
    public static final String OPSTR_READ_CLIPBOARD = "android:read_clipboard";
    /**
     * @hide
     */
    public static final String OPSTR_WRITE_CLIPBOARD = "android:write_clipboard";
    /**
     * @hide
     */

    public static final String OPSTR_TAKE_MEDIA_BUTTONS = "android:take_media_buttons";
    /**
     * @hide
     */

    public static final String OPSTR_TAKE_AUDIO_FOCUS = "android:take_audio_focus";
    /**
     * @hide
     */

    public static final String OPSTR_AUDIO_MASTER_VOLUME = "android:audio_master_volume";
    /**
     * @hide
     */

    public static final String OPSTR_AUDIO_VOICE_VOLUME = "android:audio_voice_volume";
    /**
     * @hide
     */
    public static final String OPSTR_AUDIO_RING_VOLUME = "android:audio_ring_volume";
    /**
     * @hide
     */
    public static final String OPSTR_AUDIO_MEDIA_VOLUME = "android:audio_media_volume";
    /**
     * @hide
     */
    public static final String OPSTR_AUDIO_ALARM_VOLUME = "android:audio_alarm_volume";
    /**
     * @hide
     */
    public static final String OPSTR_AUDIO_NOTIFICATION_VOLUME =
            "android:audio_notification_volume";
    /**
     * @hide
     */
    public static final String OPSTR_AUDIO_BLUETOOTH_VOLUME = "android:audio_bluetooth_volume";
    /**
     * @hide
     */
    public static final String OPSTR_WAKE_LOCK = "android:wake_lock";
    /**
     * @hide
     */
    public static final String OPSTR_MUTE_MICROPHONE = "android:mute_microphone";
    /**
     * @hide
     */
    public static final String OPSTR_TOAST_WINDOW = "android:toast_window";
    /**
     * @hide
     */
    public static final String OPSTR_PROJECT_MEDIA = "android:project_media";
    /**
     * @hide
     */
    public static final String OPSTR_WRITE_WALLPAPER = "android:write_wallpaper";
    /**
     * @hide
     */
    public static final String OPSTR_ASSIST_STRUCTURE = "android:assist_structure";
    /**
     * @hide
     */
    public static final String OPSTR_ASSIST_SCREENSHOT = "android:assist_screenshot";
    /**
     * @hide
     */
    public static final String OPSTR_TURN_SCREEN_ON = "android:turn_screen_on";
    /**
     * @hide
     */
    public static final String OPSTR_RUN_IN_BACKGROUND = "android:run_in_background";
    /**
     * @hide
     */
    public static final String OPSTR_AUDIO_ACCESSIBILITY_VOLUME =
            "android:audio_accessibility_volume";
    /**
     * @hide
     */
    public static final String OPSTR_REQUEST_INSTALL_PACKAGES = "android:request_install_packages";
    /**
     * @hide
     */
    public static final String OPSTR_RUN_ANY_IN_BACKGROUND = "android:run_any_in_background";
    /**
     * @hide
     */
    public static final String OPSTR_CHANGE_WIFI_STATE = "android:change_wifi_state";
    /**
     * @hide
     */
    public static final String OPSTR_REQUEST_DELETE_PACKAGES = "android:request_delete_packages";
    /**
     * @hide
     */
    public static final String OPSTR_BIND_ACCESSIBILITY_SERVICE =
            "android:bind_accessibility_service";
    /**
     * @hide
     */
    public static final String OPSTR_MANAGE_IPSEC_TUNNELS = "android:manage_ipsec_tunnels";
    /**
     * @hide
     */
    public static final String OPSTR_START_FOREGROUND = "android:start_foreground";
    public static final String OPSTR_BLUETOOTH_SCAN = "android:bluetooth_scan";
    public static final String OPSTR_USE_BIOMETRIC = "android:use_biometric";
    public static final String OPSTR_ACTIVITY_RECOGNITION = "android:activity_recognition";
    public static final String OPSTR_SMS_FINANCIAL_TRANSACTIONS =
            "android:sms_financial_transactions";
    public static final String OPSTR_READ_MEDIA_AUDIO = "android:read_media_audio";
    public static final String OPSTR_WRITE_MEDIA_AUDIO = "android:write_media_audio";
    public static final String OPSTR_READ_MEDIA_VIDEO = "android:read_media_video";
    public static final String OPSTR_WRITE_MEDIA_VIDEO = "android:write_media_video";
    public static final String OPSTR_READ_MEDIA_IMAGES = "android:read_media_images";
    public static final String OPSTR_WRITE_MEDIA_IMAGES = "android:write_media_images";
    public static final String OPSTR_LEGACY_STORAGE = "android:legacy_storage";
    public static final String OPSTR_ACCESS_MEDIA_LOCATION = "android:access_media_location";
    public static final String OPSTR_ACCESS_ACCESSIBILITY = "android:access_accessibility";
    public static final String OPSTR_READ_DEVICE_IDENTIFIERS = "android:read_device_identifiers";
    public static final String OPSTR_QUERY_ALL_PACKAGES = "android:query_all_packages";
    public static final String OPSTR_MANAGE_EXTERNAL_STORAGE =
            "android:manage_external_storage";
    public static final String OPSTR_AUTO_REVOKE_PERMISSIONS_IF_UNUSED =
            "android:auto_revoke_permissions_if_unused";
    public static final String OPSTR_AUTO_REVOKE_MANAGED_BY_INSTALLER =
            "android:auto_revoke_managed_by_installer";
    public static final String OPSTR_INTERACT_ACROSS_PROFILES = "android:interact_across_profiles";
    public static final String OPSTR_ACTIVATE_PLATFORM_VPN = "android:activate_platform_vpn";
    public static final String OPSTR_LOADER_USAGE_STATS = "android:loader_usage_stats";
    public static final String OPSTR_NO_ISOLATED_STORAGE = "android:no_isolated_storage";

    public static final int[] MERGED_LOCATION_OPS = new int[]{
            AppOpsManager.OP_COARSE_LOCATION,
            AppOpsManager.OP_FINE_LOCATION,
            AppOpsManager.OP_GPS,
            AppOpsManager.OP_WIFI_SCAN,
            AppOpsManager.OP_NEIGHBORING_CELLS,
            AppOpsManager.OP_MONITOR_LOCATION,
            AppOpsManager.OP_MONITOR_HIGH_POWER_LOCATION};

    public static final String[] MERGED_LOCATION_OPS_STR = new String[]{
            AppOpsManager.OPSTR_COARSE_LOCATION,
            AppOpsManager.OPSTR_FINE_LOCATION,
            AppOpsManager.OPSTR_GPS,
            AppOpsManager.OPSTR_WIFI_SCAN,
            AppOpsManager.OPSTR_NEIGHBORING_CELLS,
            AppOpsManager.OPSTR_MONITOR_LOCATION,
            AppOpsManager.OPSTR_MONITOR_HIGH_POWER_LOCATION};

    /**
     * @hide
     */
    public static final int _NUM_OP = 105;

    /**
     * This optionally maps a permission to an operation.  If there is no permission associated with
     * an operation, it is null.
     */
    private static final String[] S_OP_PERMS = new String[]{
            ManifestCompat.permission.ACCESS_COARSE_LOCATION,
            ManifestCompat.permission.ACCESS_FINE_LOCATION,
            null,
            ManifestCompat.permission.VIBRATE,
            ManifestCompat.permission.READ_CONTACTS,
            ManifestCompat.permission.WRITE_CONTACTS,
            ManifestCompat.permission.READ_CALL_LOG,
            ManifestCompat.permission.WRITE_CALL_LOG,
            ManifestCompat.permission.READ_CALENDAR,
            ManifestCompat.permission.WRITE_CALENDAR,
            ManifestCompat.permission.ACCESS_WIFI_STATE,
            null, // no permission required for notifications
            null, // neighboring cells shares the coarse location perm
            ManifestCompat.permission.CALL_PHONE,
            ManifestCompat.permission.READ_SMS,
            null, // no permission required for writing sms
            ManifestCompat.permission.RECEIVE_SMS,
            ManifestCompat.permission.RECEIVE_EMERGENCY_BROADCAST,
            ManifestCompat.permission.RECEIVE_MMS,
            ManifestCompat.permission.RECEIVE_WAP_PUSH,
            ManifestCompat.permission.SEND_SMS,
            ManifestCompat.permission.READ_SMS,
            null, // no permission required for writing icc sms
            ManifestCompat.permission.WRITE_SETTINGS,
            ManifestCompat.permission.SYSTEM_ALERT_WINDOW,
            ManifestCompat.permission.ACCESS_NOTIFICATIONS,
            ManifestCompat.permission.CAMERA,
            ManifestCompat.permission.RECORD_AUDIO,
            null, // no permission for playing audio
            null, // no permission for reading clipboard
            null, // no permission for writing clipboard
            null, // no permission for taking media buttons
            null, // no permission for taking audio focus
            null, // no permission for changing master volume
            null, // no permission for changing voice volume
            null, // no permission for changing ring volume
            null, // no permission for changing media volume
            null, // no permission for changing alarm volume
            null, // no permission for changing notification volume
            null, // no permission for changing bluetooth volume
            ManifestCompat.permission.WAKE_LOCK,
            null, // no permission for generic location monitoring
            null, // no permission for high power location monitoring
            ManifestCompat.permission.PACKAGE_USAGE_STATS,
            null, // no permission for muting/unmuting microphone
            null, // no permission for displaying toasts
            null, // no permission for projecting media
            null, // no permission for activating vpn
            null, // no permission for supporting wallpaper
            null, // no permission for receiving assist structure
            null, // no permission for receiving assist screenshot
            ManifestCompat.permission.READ_PHONE_STATE,
            ManifestCompat.permission.ADD_VOICEMAIL,
            ManifestCompat.permission.USE_SIP,
            ManifestCompat.permission.PROCESS_OUTGOING_CALLS,
            ManifestCompat.permission.USE_FINGERPRINT,
            ManifestCompat.permission.BODY_SENSORS,
            ManifestCompat.permission.READ_CELL_BROADCASTS,
            null,
            ManifestCompat.permission.READ_EXTERNAL_STORAGE,
            ManifestCompat.permission.WRITE_EXTERNAL_STORAGE,
            null, // no permission for turning the screen on
            ManifestCompat.permission.GET_ACCOUNTS,
            null, // no permission for running in background
            null, // no permission for changing accessibility volume
            ManifestCompat.permission.READ_PHONE_NUMBERS,
            ManifestCompat.permission.REQUEST_INSTALL_PACKAGES,
            null, // no permission for entering picture-in-picture on hide
            ManifestCompat.permission.INSTANT_APP_FOREGROUND_SERVICE,
            ManifestCompat.permission.ANSWER_PHONE_CALLS,
            null, // no permission for OP_RUN_ANY_IN_BACKGROUND
            ManifestCompat.permission.CHANGE_WIFI_STATE,
            ManifestCompat.permission.REQUEST_DELETE_PACKAGES,
            ManifestCompat.permission.BIND_ACCESSIBILITY_SERVICE,
            ManifestCompat.permission.ACCEPT_HANDOVER,
            null, // no permission for OP_MANAGE_IPSEC_TUNNELS
            ManifestCompat.permission.FOREGROUND_SERVICE,
            null, // no permission for OP_BLUETOOTH_SCAN

            ManifestCompat.permission.USE_BIOMETRIC,
            ManifestCompat.permission.ACTIVITY_RECOGNITION,
            ManifestCompat.permission.SMS_FINANCIAL_TRANSACTIONS,
            null,
            null, // no permission for OP_WRITE_MEDIA_AUDIO
            null,
            null, // no permission for OP_WRITE_MEDIA_VIDEO
            null,
            null, // no permission for OP_WRITE_MEDIA_IMAGES
            null, // no permission for OP_LEGACY_STORAGE
            null, // no permission for OP_ACCESS_ACCESSIBILITY
            null, // no direct permission for OP_READ_DEVICE_IDENTIFIERS

            null, // 90
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null, // 99


            null, // Thanox start
            null,
            null,
            null,
            null,
    };

    private static final String[] S_OP_TO_STRING = new String[]{
            OPSTR_COARSE_LOCATION,
            OPSTR_FINE_LOCATION,
            OPSTR_GPS,
            OPSTR_VIBRATE,
            OPSTR_READ_CONTACTS,
            OPSTR_WRITE_CONTACTS,
            OPSTR_READ_CALL_LOG,
            OPSTR_WRITE_CALL_LOG,
            OPSTR_READ_CALENDAR,
            OPSTR_WRITE_CALENDAR,
            OPSTR_WIFI_SCAN,
            OPSTR_POST_NOTIFICATION,
            OPSTR_NEIGHBORING_CELLS,
            OPSTR_CALL_PHONE,
            OPSTR_READ_SMS,
            OPSTR_WRITE_SMS,
            OPSTR_RECEIVE_SMS,
            OPSTR_RECEIVE_EMERGENCY_BROADCAST,
            OPSTR_RECEIVE_MMS,
            OPSTR_RECEIVE_WAP_PUSH,
            OPSTR_SEND_SMS,
            OPSTR_READ_ICC_SMS,
            OPSTR_WRITE_ICC_SMS,
            OPSTR_WRITE_SETTINGS,
            OPSTR_SYSTEM_ALERT_WINDOW,
            OPSTR_ACCESS_NOTIFICATIONS,
            OPSTR_CAMERA,
            OPSTR_RECORD_AUDIO,
            OPSTR_PLAY_AUDIO,
            OPSTR_READ_CLIPBOARD,
            OPSTR_WRITE_CLIPBOARD,
            OPSTR_TAKE_MEDIA_BUTTONS,
            OPSTR_TAKE_AUDIO_FOCUS,
            OPSTR_AUDIO_MASTER_VOLUME,
            OPSTR_AUDIO_VOICE_VOLUME,
            OPSTR_AUDIO_RING_VOLUME,
            OPSTR_AUDIO_MEDIA_VOLUME,
            OPSTR_AUDIO_ALARM_VOLUME,
            OPSTR_AUDIO_NOTIFICATION_VOLUME,
            OPSTR_AUDIO_BLUETOOTH_VOLUME,
            OPSTR_WAKE_LOCK,
            OPSTR_MONITOR_LOCATION,
            OPSTR_MONITOR_HIGH_POWER_LOCATION,
            OPSTR_GET_USAGE_STATS,
            OPSTR_MUTE_MICROPHONE,
            OPSTR_TOAST_WINDOW,
            OPSTR_PROJECT_MEDIA,
            OPSTR_ACTIVATE_VPN,
            OPSTR_WRITE_WALLPAPER,
            OPSTR_ASSIST_STRUCTURE,
            OPSTR_ASSIST_SCREENSHOT,
            OPSTR_READ_PHONE_STATE,
            OPSTR_ADD_VOICEMAIL,
            OPSTR_USE_SIP,
            OPSTR_PROCESS_OUTGOING_CALLS,
            OPSTR_USE_FINGERPRINT,
            OPSTR_BODY_SENSORS,
            OPSTR_READ_CELL_BROADCASTS,
            OPSTR_MOCK_LOCATION,
            OPSTR_READ_EXTERNAL_STORAGE,
            OPSTR_WRITE_EXTERNAL_STORAGE,
            OPSTR_TURN_SCREEN_ON,
            OPSTR_GET_ACCOUNTS,
            OPSTR_RUN_IN_BACKGROUND,
            OPSTR_AUDIO_ACCESSIBILITY_VOLUME,
            OPSTR_READ_PHONE_NUMBERS,
            OPSTR_REQUEST_INSTALL_PACKAGES,
            OPSTR_PICTURE_IN_PICTURE,
            OPSTR_INSTANT_APP_START_FOREGROUND,
            OPSTR_ANSWER_PHONE_CALLS,
            OPSTR_RUN_ANY_IN_BACKGROUND,
            OPSTR_CHANGE_WIFI_STATE,
            OPSTR_REQUEST_DELETE_PACKAGES,
            OPSTR_BIND_ACCESSIBILITY_SERVICE,
            OPSTR_ACCEPT_HANDOVER,
            OPSTR_MANAGE_IPSEC_TUNNELS,
            OPSTR_START_FOREGROUND,
            OPSTR_BLUETOOTH_SCAN,
            OPSTR_USE_BIOMETRIC,
            OPSTR_ACTIVITY_RECOGNITION,
            OPSTR_SMS_FINANCIAL_TRANSACTIONS,
            OPSTR_READ_MEDIA_AUDIO,
            OPSTR_WRITE_MEDIA_AUDIO,
            OPSTR_READ_MEDIA_VIDEO,
            OPSTR_WRITE_MEDIA_VIDEO,
            OPSTR_READ_MEDIA_IMAGES,
            OPSTR_WRITE_MEDIA_IMAGES,
            OPSTR_LEGACY_STORAGE,
            OPSTR_ACCESS_ACCESSIBILITY,
            OPSTR_READ_DEVICE_IDENTIFIERS,
            OPSTR_ACCESS_MEDIA_LOCATION,
            OPSTR_QUERY_ALL_PACKAGES,
            OPSTR_MANAGE_EXTERNAL_STORAGE,
            OPSTR_INTERACT_ACROSS_PROFILES,
            OPSTR_ACTIVATE_PLATFORM_VPN,
            OPSTR_LOADER_USAGE_STATS,
            "", // deprecated
            OPSTR_AUTO_REVOKE_PERMISSIONS_IF_UNUSED,
            OPSTR_AUTO_REVOKE_MANAGED_BY_INSTALLER,
            OPSTR_NO_ISOLATED_STORAGE,

            "", // Thanox start
            "",
            "",
            "",
            ""
    };

    static {
        //noinspection ConstantConditions
        if (S_OP_PERMS.length != _NUM_OP) {
            throw new IllegalStateException("S_OP_PERMS length " + S_OP_PERMS.length
                    + " should be " + _NUM_OP);
        }

        if (S_OP_TO_STRING.length != _NUM_OP) {
            throw new IllegalStateException("S_OP_TO_STRING length " + S_OP_TO_STRING.length
                    + " should be " + _NUM_OP);
        }
    }

    public static List<Integer> getAllOp() {
        List<Integer> res = new ArrayList<>(_NUM_OP);
        // Android ops.
        for (int i = 0; i <= _OP_ANDROID_LAST; i++) {
            res.add(i);
        }
        // Thanox ops
        // 1000099
        for (int i = _OP_THANOX_BASE; i <= _OP_THANOX_LAST; i++) {
            res.add(i);
        }
        return res;
    }

    /**
     * Retrieve the permission associated with an operation, or null if there is not one.
     */
    public static String opToPermission(int op) {
        return S_OP_PERMS[getOpResIndex(op)];
    }

    public static int getOpResIndex(int op) {
        // [0, 1....ANDROID_LAST, THANOX_BASE, ....]
        // [0, 1....3, 10001,...]
        int resIndex = op;
        if (op >= AppOpsManager._OP_THANOX_BASE) {
            resIndex = resIndex - _OP_THANOX_BASE + AppOpsManager._OP_ANDROID_LAST + 1;
        }
        return resIndex;
    }

    public static int strToOp(String opStr) {
        return ArrayUtils.indexOf(S_OP_TO_STRING, opStr);
    }

    final Context context;
    final IAppOpsService service;

    private static final Set<Integer> CONTROLLABLE_OPS = new HashSet<>();

    @SneakyThrows
    public void setMode(int code, int uid, String packageName, int mode) {
        service.setMode(code, uid, packageName, mode);
    }

    @SneakyThrows
    public void resetAllModes(String reqPackageName) {
        service.resetAllModes(reqPackageName);
    }

    @SneakyThrows
    public int checkOperation(int code, int uid, String packageName) {
        return service.checkOperation(code, uid, packageName);
    }

    @SneakyThrows
    public int checkOperationNonCheck(int code, int uid, String packageName) {
        return service.checkOperationNonCheck(code, uid, packageName);
    }

    @SneakyThrows
    public boolean isOpsEnabled() {
        return service.isOpsEnabled();
    }

    @SneakyThrows
    public void setOpsEnabled(boolean enabled) {
        service.setOpsEnabled(enabled);
    }

    @SneakyThrows
    public void onStartOp(IBinder token, int code, int uid, String packageName) {
        service.onStartOp(token, code, uid, packageName);
    }

    @SneakyThrows
    public void onFinishOp(IBinder token, int code, int uid, String packageName) {
        service.onFinishOp(token, code, uid, packageName);
    }

    @SneakyThrows
    public void setOpRemindEnable(int code, boolean enable) {
        service.setOpRemindEnable(code, enable);
    }

    @SneakyThrows
    public boolean isOpRemindEnabled(int code) {
        return service.isOpRemindEnabled(code);
    }

    @SneakyThrows
    public boolean isOpRemindable(int code) {
        return code == OP_CAMERA
                || code == OP_RECORD_AUDIO
                || code == OP_ANY_LOCATION
                || code == OP_READ_CLIPBOARD
                || code == OP_WRITE_CLIPBOARD;
    }

    @SneakyThrows
    public void setPkgOpRemindEnable(String pkg, boolean enable) {
        service.setPkgOpRemindEnable(pkg, enable);
    }

    @SneakyThrows
    public boolean isPkgOpRemindEnable(String pkg) {
        return service.isPkgOpRemindEnable(pkg);
    }


    @SneakyThrows
    public boolean isForegroundOnlyOp(int code) {
        return ArrayUtils.contains(FOREGROUND_ONLY_OP, code);
    }

    public static boolean isControllableOp(int op) {
        if (CONTROLLABLE_OPS.contains(op)) {
            return true;
        }
        if (OpsTemplate.templateOfOp(op) != null) {
            CONTROLLABLE_OPS.add(op);
            return true;
        }
        return false;
    }

    public static boolean isCaredOp(int op) {
        return isControllableOp(op);
    }

    @SneakyThrows
    public List<SettingsAccessRecord> getSettingsReadRecords(String filterCallerPackageName) {
        return service.getSettingsReadRecords(filterCallerPackageName);
    }

    @SneakyThrows
    public List<SettingsAccessRecord> getSettingsWriteRecords(String filterCallerPackageName) {
        return service.getSettingsWriteRecords(filterCallerPackageName);
    }

    @SneakyThrows
    public void clearSettingsReadRecords() {
        service.clearSettingsReadRecords();
    }

    @SneakyThrows
    public void clearSettingsWriteRecords() {
        service.clearSettingsWriteRecords();
    }

    @SneakyThrows
    public void setSettingsRecordEnabled(boolean enable) {
        service.setSettingsRecordEnabled(enable);
    }

    @SneakyThrows
    public boolean isSettingsRecordEnabled() {
        return service.isSettingsRecordEnabled();
    }
}
