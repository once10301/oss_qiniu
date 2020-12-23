package com.xiyou.oss_qiniu;

import android.app.Activity;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * OssQiniuPlugin
 */
public class OssQiniuPlugin implements MethodCallHandler {
    private static Activity activity;
    private MethodChannel.Result result;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        activity = registrar.activity();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "oss_qiniu");
        channel.setMethodCallHandler(new OssQiniuPlugin());
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        this.result = result;
        if (call.method.equals("upload")) {
            String path = call.argument("path");
            String key = call.argument("key");
            String token = call.argument("token");
            upload(path, key, token);
        } else {
            result.notImplemented();
        }
    }

    public void upload(String path, String key, String token) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(path, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, final ResponseInfo info, final JSONObject res) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Map<String, Object> map = new HashMap<>();
                                map.put("success", 0);
                                map.put("result", res.get("key"));
                                result.success(map);
                            } catch (JSONException e) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("success", 1);
                                map.put("result", e.getMessage());
                                result.success(map);
                            }
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Map<String, Object> map = new HashMap<>();
                            map.put("success", 1);
                            map.put("result", info.error);
                            result.success(map);
                        }
                    });
                }
            }
        }, null);
    }
}
