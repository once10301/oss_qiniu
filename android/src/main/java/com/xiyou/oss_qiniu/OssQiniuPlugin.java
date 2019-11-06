package com.xiyou.oss_qiniu;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * OssQiniuPlugin
 */
public class OssQiniuPlugin implements MethodCallHandler {
    private MethodChannel.Result result;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
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
            public void complete(String key, ResponseInfo info, JSONObject res) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    try {
                        result.success(res.get("key"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        result.success("");
                    }
                } else {
                    result.success("");
                }
            }
        }, null);
    }
}
