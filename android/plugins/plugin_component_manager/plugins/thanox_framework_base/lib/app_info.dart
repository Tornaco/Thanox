class AppInfoList {
  static const int FLAGS_ALL = 127;

  List<AppInfo> appList;

  AppInfoList({this.appList});

  factory AppInfoList.fromJson(List<dynamic> listJson) {
    List<AppInfo> appList =
        listJson.map((value) => AppInfo.fromJson(value)).toList();

    return AppInfoList(appList: appList);
  }
}

class AppInfo {
  String appLabel;
  int flags;
  bool idle;
  bool isDummy;
  bool isSelected;
  String pkgName;
  int state;
  int uid;
  int versionCode;
  String versionName;
  String payload;

  AppInfo(
      {this.appLabel,
      this.flags,
      this.idle,
      this.isDummy,
      this.isSelected,
      this.pkgName,
      this.state,
      this.uid,
      this.versionCode,
      this.versionName,
      this.payload});

  factory AppInfo.fromJson(Map<String, dynamic> json) {
    return AppInfo(
      appLabel: json['appLabel'],
      flags: json['flags'],
      idle: json['idle'],
      isDummy: json['isDummy'],
      isSelected: json['isSelected'],
      pkgName: json['pkgName'],
      state: json['state'],
      uid: json['uid'],
      versionCode: json['versionCode'],
      versionName: json['versionName'],
      payload: json['payload'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['appLabel'] = this.appLabel;
    data['flags'] = this.flags;
    data['idle'] = this.idle;
    data['isDummy'] = this.isDummy;
    data['isSelected'] = this.isSelected;
    data['pkgName'] = this.pkgName;
    data['state'] = this.state;
    data['uid'] = this.uid;
    data['versionCode'] = this.versionCode;
    data['versionName'] = this.versionName;
    data['payload'] = this.payload;
    return data;
  }
}
