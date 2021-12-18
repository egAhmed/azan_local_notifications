#import "AzanNotificationsPlugin.h"
#if __has_include(<azan_notifications/azan_notifications-Swift.h>)
#import <azan_notifications/azan_notifications-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "azan_notifications-Swift.h"
#endif

@implementation AzanNotificationsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAzanNotificationsPlugin registerWithRegistrar:registrar];
}
@end
