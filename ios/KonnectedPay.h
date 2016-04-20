//
//  Payment.h
//  Payment
//
//  Created by Rex Ong on 30/12/2015.
//  Copyright © 2015 Rex Ong. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#define kHost @"https://pay.appxtream.com"
//#define kHost @"http://192.168.1.144:8888"
@interface KonnectedPay : NSObject <UIWebViewDelegate>
typedef enum CurrencyCode : NSUInteger {
    MYR, USD, AED, AUD, BND, CHF, CNY, EGP, EUR, GBP, HKD, IDR, INR, JPY, KRW, LKR, NZD, PHP, PKR, SAR, SEK, SGD, THB, TWD, ZAR
} CurrencyCode;


@property NSString* clientSecret;
@property NSString* merchantID;
@property UIColor* navBarColor;
@property NSString* navBarTitle;
-(KonnectedPay*)initWithMerchantID:(NSString*)Id clientSecret:(NSString*)key;
-(NSString*)getTokenListUrlWithUserID:(NSString*)userID;
-(void)make:(id)delegate fullName:(NSString*)name email:(NSString*)email userID:(NSString*)userID tranID:(NSString*)tranId amount:(NSString*)amount currencyCode:(NSInteger)curCode token:(NSString*)token rememberCard:(BOOL)rememberCard completion:(void(^)(bool isSuccess , NSDictionary* paymentResult))block;

@end
