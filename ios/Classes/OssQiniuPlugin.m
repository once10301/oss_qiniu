#import "OssQiniuPlugin.h"
#import <QiniuSDK.h>


@implementation OssQiniuPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"oss_qiniu"
                                     binaryMessenger:[registrar messenger]];
    OssQiniuPlugin* instance = [[OssQiniuPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"upload" isEqualToString:call.method]) {
        NSDictionary *argsMap = call.arguments;
        NSString *filePath = argsMap[@"path"];
        NSString *token = argsMap[@"token"];
        QNConfiguration *config =[QNConfiguration build:^(QNConfigurationBuilder *builder) {
            NSMutableArray *array = [[NSMutableArray alloc] init];
        }];
        QNUploadManager *upManager = [[QNUploadManager alloc] initWithConfiguration:config];
        [upManager putFile:filePath key:nil token:token complete:^(QNResponseInfo *info, NSString *key, NSDictionary *resp) {
            if(info.ok) {
                result(@"成功");
            } else {
                result(@"失败");
            }
            NSLog(@"info ===== %@", info);
            NSLog(@"resp ===== %@", resp);
        } option:nil];
    } else {
        result(FlutterMethodNotImplemented);
    }
}
@end
