import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:thanox_framework_base/thanox_framework_base.dart';

void main() {
  const MethodChannel channel = MethodChannel('thanox_framework_base');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await ThanoxFrameworkBase.platformVersion, '42');
  });
}
