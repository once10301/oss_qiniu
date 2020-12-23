
import 'dart:async';

import 'package:flutter/services.dart';

class OssQiniu {
  static const MethodChannel _channel =
      const MethodChannel('oss_qiniu');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
