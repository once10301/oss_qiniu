import 'dart:async';

import 'package:flutter/services.dart';

class OssQiniu {
  static const MethodChannel _channel = const MethodChannel('oss_qiniu');

  static Future<String> upload(String path, String key, String token) async {
    return await _channel.invokeMethod('upload', {'path': path, 'key': key, 'token': token});
  }
}
