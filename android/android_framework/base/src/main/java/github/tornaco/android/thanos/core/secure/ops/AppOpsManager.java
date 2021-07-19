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
import github.tornaco.android.thanos.core.annotation.Nullable;
import github.tornaco.android.thanos.core.compat.ManifestCompat;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import java.util.HashSet;
import java.util.Set;
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

  public static final int OP_PLACE_HOLDER_13 = 90;
  public static final int OP_PLACE_HOLDER_14 = 91;
  public static final int OP_PLACE_HOLDER_15 = 92;
  public static final int OP_PLACE_HOLDER_16 = 93;
  public static final int OP_PLACE_HOLDER_17 = 94;
  public static final int OP_PLACE_HOLDER_18 = 95;
  public static final int OP_PLACE_HOLDER_19 = 96;
  public static final int OP_PLACE_HOLDER_20 = 97;
  public static final int OP_PLACE_HOLDER_21 = 98;

  public static final int OP_GET_PACKAGE_INFO = 99;
  public static final int OP_CHANGE_BRIGHTNESS = 100;
  public static final int OP_GET_INSTALLED_PACKAGES = 101;
  public static final int OP_QUERY_INTENT_ACTIVITIES = 102;
  public static final int OP_ANY_LOCATION = 103;

  public static final int[] MERGED_LOCATION_OPS = new int[]{
          AppOpsManager.OP_COARSE_LOCATION,
      AppOpsManager.OP_FINE_LOCATION,
      AppOpsManager.OP_GPS,
      AppOpsManager.OP_WIFI_SCAN,
      AppOpsManager.OP_NEIGHBORING_CELLS,
      AppOpsManager.OP_MONITOR_LOCATION,
      AppOpsManager.OP_MONITOR_HIGH_POWER_LOCATION};

  /**
   * @hide
   */
  public static final int _NUM_OP = 104;

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

      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,

      null,
      null,
      null,
      null,
  };

  static {
    //noinspection ConstantConditions
    if (S_OP_PERMS.length != _NUM_OP) {
      throw new IllegalStateException("sOpPerms length " + S_OP_PERMS.length
          + " should be " + _NUM_OP);
    }
  }

  /**
   * Retrieve the permission associated with an operation, or null if there is not one.
   */
  @Nullable
  public static String opToPermission(int op) {
    return S_OP_PERMS[op];
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
}
