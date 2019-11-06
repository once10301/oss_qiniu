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
        NSString *key = argsMap[@"key"];
        NSString *token = argsMap[@"token"];
        QNConfiguration *config =[QNConfiguration build:^(QNConfigurationBuilder *builder) {
            NSMutableArray *array = [[NSMutableArray alloc] init];
        }];
        QNUploadManager *upManager = [[QNUploadManager alloc] initWithConfiguration:config];
        [upManager putFile:filePath key:key token:token complete:^(QNResponseInfo *info, NSString *key, NSDictionary *resp) {
            if(info.ok) {
                result(resp[@"key"]);
            } else {
                result(@"");
            }
        } option:nil];
    } else {
        result(FlutterMethodNotImplemented);
    }
}
@end
