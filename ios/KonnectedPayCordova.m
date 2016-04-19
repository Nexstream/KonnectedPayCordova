#import "KonnectedPay.h"
#import "KonnectedPayCordova.h"

// FIXME: This class depends on CurrencyCode type == NSUInteger...

//------------------------------------------------------------------------------
#pragma mark - Private properties

@interface KonnectedPayCordova ()

@property (nonatomic, retain) NSDictionary<NSString*,NSNumber*> *codes;
@property (atomic, retain) KonnectedPay *sdk;

@end


@implementation KonnectedPayCordova

@synthesize codes;


//------------------------------------------------------------------------------
#pragma mark - Plugin init

- (void)pluginInitialize
{
    self.codes = @{
        @"MYR": [NSNumber numberWithUnsignedInteger:MYR],
        @"USD": [NSNumber numberWithUnsignedInteger:USD],
        @"AED": [NSNumber numberWithUnsignedInteger:AED],
        @"AUD": [NSNumber numberWithUnsignedInteger:AUD],
        @"BND": [NSNumber numberWithUnsignedInteger:BND],
        @"CHF": [NSNumber numberWithUnsignedInteger:CHF],
        @"CNY": [NSNumber numberWithUnsignedInteger:CNY],
        @"EGP": [NSNumber numberWithUnsignedInteger:EGP],
        @"EUR": [NSNumber numberWithUnsignedInteger:EUR],
        @"GBP": [NSNumber numberWithUnsignedInteger:GBP],
        @"HKD": [NSNumber numberWithUnsignedInteger:HKD],
        @"IDR": [NSNumber numberWithUnsignedInteger:IDR],
        @"INR": [NSNumber numberWithUnsignedInteger:INR],
        @"JPY": [NSNumber numberWithUnsignedInteger:JPY],
        @"KRW": [NSNumber numberWithUnsignedInteger:KRW],
        @"LKR": [NSNumber numberWithUnsignedInteger:LKR],
        @"NZD": [NSNumber numberWithUnsignedInteger:NZD],
        @"PHP": [NSNumber numberWithUnsignedInteger:PHP],
        @"PKR": [NSNumber numberWithUnsignedInteger:PKR],
        @"SAR": [NSNumber numberWithUnsignedInteger:SAR],
        @"SEK": [NSNumber numberWithUnsignedInteger:SEK],
        @"SGD": [NSNumber numberWithUnsignedInteger:SGD],
        @"THB": [NSNumber numberWithUnsignedInteger:THB],
        @"TWD": [NSNumber numberWithUnsignedInteger:TWD],
        @"ZAR": [NSNumber numberWithUnsignedInteger:ZAR]
    };
}


//------------------------------------------------------------------------------
#pragma mark - Internal Helpers

- (bool)currencyCodeEnum: (NSString*)name target:(CurrencyCode*)target
{
    NSNumber *code = [self.codes objectForKey:name];
    if(code == nil) return false;
    else {
        *target = [code unsignedIntegerValue];
        return true;
    }
}


//------------------------------------------------------------------------------
#pragma mark - Public API

- (void)requestPayment: (CDVInvokedUrlCommand*)command
{
    // Retrieve parameters

    NSDictionary *params = (NSDictionary*) [command argumentAtIndex:0];
    NSString *merchantId    = [params objectForKey:@"merchantId"];
    NSString *clientSecret  = [params objectForKey:@"clientSecret"];
    NSString *amount        = [params objectForKey:@"amount"];
    NSString *transId       = [params objectForKey:@"transId"];
    NSString *currencyCode  = [params objectForKey:@"currencyCode"];
    NSString *fullName      = [params objectForKey:@"fullName"];
    NSString *email         = [params objectForKey:@"email"];
    NSString *userId        = [params objectForKey:@"userId"];
    NSString *token         = [params objectForKey:@"token"];

    bool rememberCard  = [[params objectForKey:@"rememberCard"] boolValue]; // TMP iOS SDK
        // does not yet have a `rememberCard` option

    CurrencyCode currency;
    if(![self currencyCodeEnum:currencyCode target:&currency]) {
        [self.commandDelegate
            sendPluginResult: [CDVPluginResult
                               resultWithStatus: CDVCommandStatus_ERROR
                               messageAsString:@"Invalid currency code."]
            callbackId: [command callbackId]];
        return;
    }

    // Make payment

    self.sdk = [[KonnectedPay alloc] initWithMerchantID:merchantId clientSecret:clientSecret];
    [self.sdk make:self.viewController
             fullName:fullName email:email userID:userId tranID:transId
             amount:amount currencyCode:currency token:token
             completion:^void(bool isSuccess, NSDictionary* paymentResult){
                self.sdk = nil;

                CDVCommandStatus status;
                if(isSuccess) status = CDVCommandStatus_OK;
                else status = CDVCommandStatus_ERROR;
                [self.commandDelegate
                    sendPluginResult: [CDVPluginResult
                                       resultWithStatus: status
                                       messageAsDictionary: paymentResult]
                    callbackId: [command callbackId]];
    }];
}

- (void)getTokenListUrl: (CDVInvokedUrlCommand*)command
{
    NSDictionary *params = (NSDictionary*) [command argumentAtIndex:0];
    NSString *merchantId    = [params objectForKey:@"merchantId"];
    NSString *clientSecret  = [params objectForKey:@"clientSecret"];
    NSString *userId        = [params objectForKey:@"userId"];

    self.sdk = [[KonnectedPay alloc] initWithMerchantID:merchantId clientSecret:clientSecret];
    NSString *tokenListUrl = [self.sdk getTokenListUrlWithUserID:userId];
    self.sdk = nil;

    [self.commandDelegate
        sendPluginResult: [CDVPluginResult
                           resultWithStatus: CDVCommandStatus_OK
                           messageAsString:tokenListUrl]
        callbackId: [command callbackId]];
}

@end
