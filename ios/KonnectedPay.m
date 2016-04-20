//
//  Payment.m
//  Payment
//
//  Created by Rex Ong on 30/12/2015.
//  Copyright © 2015 Rex Ong. All rights reserved.
//

#import "KonnectedPay.h"

@implementation KonnectedPay{
    UIViewController* paymentController;
    UIViewController* viewDelegate;
    void(^completionHandler)(bool isSuccess, NSDictionary* paymentResult);
}

-(KonnectedPay*)initWithMerchantID:(NSString*)Id clientSecret:(NSString*)key{
    self = [super init];
    if (self) {
        self.merchantID = Id;
        self.clientSecret = key;
    }
    return self;
}
-(void)isValid{
    NSAssert(self.clientSecret !=nil, @"clientSecret cannnot be nil");
    NSAssert(self.merchantID != nil, @"merchantID cannot be nil");
    NSAssert([self.clientSecret length]!=0, @"clientSecret invalid");
    NSAssert([self.merchantID length]!=0, @"merchantID invalid");
    
}

//- (NSString *)encodeForString:(NSString *)str {
//    NSString *encodedUString =
//    (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
//                                                                          NULL, (CFStringRef)str, NULL, (CFStringRef) @":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~` ",
//                                                                          kCFStringEncodingUTF8));
//    return encodedUString;
//}
-(NSString*)getTokenListUrlWithUserID:(NSString*)userID{
    [self isValid];
    return [NSString stringWithFormat:@"%@/payment/token/%@?clientSecret=%@",kHost,userID,self.clientSecret];
}

- (NSString*) convertToString:(CurrencyCode) whichAlpha {
    NSString *result = nil;
    switch(whichAlpha) {
        case MYR:
            result = @"MYR";
            break;
        case USD:
            result = @"USD";
            break;
        case AED:
            result = @"AED";
            break;
        case AUD:
            result = @"AUD";
            break;
        case BND:
            result = @"BND";
            break;
        case CHF:
            result = @"CHF";
            break;
        case CNY:
            result = @"CNY";
            break;
        case EGP:
            result = @"EGP";
            break;
        case EUR:
            result = @"EUR";
            break;
        case GBP:
            result = @"GBP";
            break;
        case HKD:
            result = @"HKD";
            break;
        case IDR:
            result = @"IDR";
            break;
        case INR:
            result = @"INR";
            break;
        case JPY:
            result = @"JPY";
            break;
        case KRW:
            result = @"KRW";
            break;
        case LKR:
            result = @"LKR";
            break;
        case NZD:
            result = @"NZD";
            break;
        case PHP:
            result = @"PHP";
            break;
        case PKR:
            result = @"PKR";
            break;
        case SAR:
            result = @"SAR";
            break;
        case SEK:
            result = @"SEK";
            break;
        case SGD:
            result = @"SGD";
            break;
        case THB:
            result = @"THB";
            break;
        case TWD:
            result = @"TWD";
            break;
        case ZAR:
            result = @"ZAR";
            break;
        default:
            result = @"unknown";
    }
    
    return result;
}

-(void)make:(id)delegate fullName:(NSString*)name email:(NSString*)email userID:(NSString*)userID tranID:(NSString*)tranId amount:(NSString*)amount currencyCode:(NSInteger)curCode token:(NSString*)token rememberCard:(BOOL)rememberCard completion:(void(^)(bool isSuccess , NSDictionary* paymentResult))block{
    
    [self isValid];
    viewDelegate=delegate;
    completionHandler= block;
    
    NSAssert(name !=nil, @"Please make sure all mandatory param cannot be null -- name cannnot be nil");
    NSAssert(email !=nil, @"Please make sure all mandatory param cannot be null --  email cannnot be nil");
    NSAssert(userID !=nil, @"Please make sure all mandatory param cannot be null --  userID cannnot be nil");
    NSAssert(tranId !=nil, @"Please make sure all mandatory param cannot be null --  tranId cannnot be nil");
    NSAssert(amount !=nil, @"Please make sure all mandatory param cannot be null -- amount cannnot be nil");
    
    NSString* curCodeStr = [self convertToString:curCode];
    NSString* isRememberCard = @"true";
    if(rememberCard == NO){
        isRememberCard = @"false";
    }
    
    NSMutableString* url = [NSMutableString stringWithFormat:@"%@/payment?merchantId=%@&tranId=%@&amount=%@&currencyCode=%@&device=%@&os=iOS-%@&fullname=%@&email=%@&userId=%@&rememberCard=%@",kHost,self.merchantID,tranId,amount,curCodeStr,[[UIDevice currentDevice] model],[[UIDevice currentDevice] systemVersion],name,email,userID,isRememberCard];
    
    if (token != nil) {
        [url appendFormat:@"&token=%@",token];
    }
    
    [self showPaymentViewController:url];
}

-(void)showPaymentViewController:(NSString*)url{
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
        paymentController = [[UIViewController alloc]init];
        paymentController.view.frame = [UIScreen mainScreen].bounds;
        UINavigationBar *navBar = [[UINavigationBar alloc]initWithFrame:CGRectMake(0, 0, paymentController.view.frame.size.width, 64)];
        if(self.navBarColor == nil) {
            navBar.barTintColor = [UIColor whiteColor];
        }else{
            navBar.barTintColor = self.navBarColor;
        }
        navBar.translucent = NO;
        UINavigationItem *navItem = [[UINavigationItem alloc] init];
        UIBarButtonItem* cancelBtn = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(dismissView)];
        navItem.leftBarButtonItem = cancelBtn;
        navBar.items = @[ navItem ];
        
        if(self.navBarTitle!=nil){
            navItem.title = self.navBarTitle;
        }
        
        [paymentController.view addSubview:navBar];
        
        UIWebView* webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 64, paymentController.view.frame.size.width, paymentController.view.frame.size.height-64)];
        webView.delegate = self;
        NSMutableURLRequest *urlRequest = [[NSMutableURLRequest alloc] initWithURL: [NSURL URLWithString: [url stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]]];
        [webView loadRequest:urlRequest];
        [paymentController.view addSubview:webView];
        [viewDelegate presentViewController:paymentController animated:YES completion:nil];
    });
    
}
#pragma mark - WebViewDelegate

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSURL *url = [request URL];
    NSString *urlStr = url.absoluteString;
    
    if ([webView respondsToSelector:@selector(scrollView)])
    {
        CGSize contentSize = webView.scrollView.contentSize;
        CGSize viewSize = paymentController.view.bounds.size;
        float rw = viewSize.width / contentSize.width;
        webView.scrollView.minimumZoomScale = rw;
        webView.scrollView.maximumZoomScale = rw;
        webView.scrollView.zoomScale = rw;
    }
    return [self processURL:urlStr];
    
}

- (BOOL) processURL:(NSString *) url
{
    
    NSString *urlStr = [NSString stringWithString:url];
    
    NSString *protocolPrefix = @"payresult://";
    
    //process only our custom protocol
    if ([[urlStr lowercaseString] hasPrefix:protocolPrefix])
    {
        //strip protocol from the URL. We will get input to call a native method
        urlStr = [urlStr substringFromIndex:protocolPrefix.length];
        
        //Decode the url string
        urlStr = [urlStr stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        NSError *jsonError;
        
        //parse JSON input in the URL
        NSDictionary *paymentResult = [NSJSONSerialization
                                       JSONObjectWithData:[urlStr dataUsingEncoding:NSUTF8StringEncoding]
                                       options:kNilOptions
                                       error:&jsonError];
        
        //check if there was error in parsing JSON input
        if (jsonError != nil)
        {
            NSLog(@"Error parsing JSON for the url %@",url);
            [paymentController dismissViewControllerAnimated:YES completion:^void{
                completionHandler(NO,paymentResult);
            }];
            return NO;
        }
        if([paymentResult[@"status"] isEqualToString:@"S"]){
            [paymentController dismissViewControllerAnimated:YES completion:^void{
                completionHandler(YES,paymentResult);
            }];
            
        }else{
            [paymentController dismissViewControllerAnimated:YES completion:^void{
                completionHandler(NO,paymentResult);
            }];
        }
        
        //Do not load this url in the WebView
        return NO;
        
    }
    
    
    return YES;
}

-(void)dismissView{
    [paymentController dismissViewControllerAnimated:YES completion:nil];
}

-(void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error{
    NSLog(@"error: %@",[error localizedDescription]);
}

@end
