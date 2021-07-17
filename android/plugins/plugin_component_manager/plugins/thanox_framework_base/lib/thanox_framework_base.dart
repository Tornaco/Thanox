import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

import 'app_info.dart';

class ThanoxFrameworkBase {
  static const MethodChannel _channel =
      const MethodChannel('thanox_framework_base');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> get isServiceInstalled async {
    final bool installed = await _channel.invokeMethod("isServiceInstalled");
    return installed;
  }

  static Future<String> get fingerPrint async {
    final String fp = await _channel.invokeMethod('fingerPrint');
    return fp;
  }

  static Future<AppInfoList> getInstalledPkgs(int flags) async {
    final String res = await _channel.invokeMethod('getInstalledPkgs');
    List<dynamic> list = json.decode(res);
    AppInfoList appList = AppInfoList.fromJson(list);
    return appList;
  }

  static Future<String> getAppIconCachePath(String pkg) async {
    final String res = await _channel.invokeMethod('getAppIconCachePath', pkg);
    return res;
  }
}
