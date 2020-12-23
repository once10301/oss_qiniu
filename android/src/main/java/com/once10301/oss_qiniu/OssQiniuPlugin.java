package com.once10301.oss_qiniu;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class OssQiniuPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private Activity activity;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        MethodChannel channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "oss_qiniu");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
        if (call.method.equals("upload")) {
            String path = call.argument("path");
            String key = call.argument("key");
            String token = call.argument("token");
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(path, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, final ResponseInfo info, final JSONObject res) {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Map<String, Object> map = new HashMap<>();
                            if (info.isOK()) {
                                try {
                                    map.put("success", 0);
                                    map.put("result", res.get("key"));
                                } catch (JSONException e) {
                                    map.put("success", 1);
                                    map.put("result", e.getMessage());
                                }
                            } else {
                                map.put("success", 1);
                                map.put("result", info.error);
                            }
                            result.success(map);
                        }
                    });
                }
            }, null);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {

    }
}
