package com.oneplus.android.server.context;

import android.content.Context;

public interface IOneplusContextStub {

  public enum EStubType {
    oneplus_permissioncontrol,
    oneplus_pmpermissioncontrol,
    oneplus_permissioncontrolservice,
    oneplus_package_manager_helper,
    oneplus_reserve_app,
    oneplus_openid,
    oneplus_heytap_business,
    oneplus_motor,
    oneplus_power_controller,
    oneplus_power_consumption_statistics,
    oneplus_highpowerdetect,
    oneplus_batterystatsservice,
    oneplus_opservice,
    oneplus_standbydetect,
    oneplus_colordisplay,
    oneplus_batteryoutlier,
    oneplus_exservice,
    oneplus_vibratorservice,
    oneplus_phonewindowmanager,
    oneplus_alertslider_manager,
    oneplus_fast_charge,
    oneplus_smart_power_control,
    oneplus_process_resident,
    oneplus_aggressive_doze,
    oneplus_smart_doze,
    oneplus_alarm_alignment,
    oneplus_nfc,
    oneplus_wifi,
    oneplus_bluetooth_manager,
    oneplus_oimcservice,
    oneplus_oputil,
    oneplus_applocker,
    oneplus_perf_manager,
    oneplus_activitystarter,
    oneplus_launcherappsservice,
    oneplus_restartprocessmanager,
    oneplus_displaypowercontroller,
    oneplus_zenmode,
    oneplus_screencompat,
    oneplus_uididle,
    oneplus_screenshotimprovement,
    oneplus_gps_notification,
    oneplus_quickpay,
    oneplus_networkpolicymanagerservice,
    oneplus_dexoptmanager,
    oneplus_appbootmanager,
    oneplus_smartboost,
    oneplus_memorytracker,
    oneplus_apprecord,
    oneplus_secrecy,
    oneplus_engineer,
    oneplus_skipdoframe,
    oneplus_powerkey_launch,
    oneplus_screenmode_service,
    oneplus_screenrotationimprovement,
    oneplus_activitytaskmanagerservice,
    oneplus_displaycontent,
    oneplus_windowmanagerservice,
    oneplus_power_manager,
    oneplus_process_adj_control,
    oneplus_alarm_manager,
    oneplus_jankmanager,
    oneplus_china_gms,
    oneplus_connor,
    oneplus_scenemode,
    oneplus_param_service,
    oneplus_carkit_identification,
    oneplus_verificationcode_controller,
    oneplus_background_freeze,
    oneplus_opsla,
    oneplus_scene_call_block,
    oneplus_longshot_manager_service,
    oneplus_compatibility_helper,
    oneplus_quickreply,
    oneplus_op_ads,
    oneplus_apperror_dialog,
    oneplus_game_shake,
    oneplus_onehandmode,
    oneplus_op_instant_app,
    oneplus_op_audiomonitor_service,
    oneplus_antiburn_manager,
    oneplus_force_dark_controller,
    oneplus_batterystats_service,
    oneplus_startingwindow,
    oneplus_font_controller
  }

  void bootComplete();

  Object queryInterface(EStubType eStubType);

  void shutdown();

  void start(Context context);

  void systemReady();
}
