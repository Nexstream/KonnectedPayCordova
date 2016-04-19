#import <Cordova/CDV.h>

@interface KonnectedPayCordova : CDVPlugin

- (void)requestPayment: (CDVInvokedUrlCommand*)command;
- (void)getTokenListUrl: (CDVInvokedUrlCommand*)command;

@end
