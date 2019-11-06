package com.xiyou.oss_qiniu;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

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
            String token = call.argument("token");
            upload(path, token);
        } else {
            result.notImplemented();
        }
    }

    public void upload(String path, String token) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(path, null, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    result.success(res.toString());
                    Log.i("qiniu", "Upload Success" + res.toString());
                } else {
                    Log.i("qiniu", "Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
            }
        }, null);
    }
}
