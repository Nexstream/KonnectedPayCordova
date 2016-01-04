KonnectedPay Cordova Plugin
===========================

Installation
------------

Install this plugin using the Cordova command line tools:

`cordova plugin add cordova-plugin-konnectedpay`


Usage
-----

### Requesting Payment

Requesting payment with payment form page:

```javascript
konnectedpay.requestPayment(
    {
        merchantId: "your konnectedpay merchant id",     // Get from your KonnectedPay account.
        clientSecret: "your konnectedpay client secret", // Get from your KonnectedPay account.
        amount: 1234.56,
        transId: "your unique transaction id",
        currencyCode: "MYR",
        fullName: "payer's full name",
        email: "payer's email address",
        userId: "your internal user ID",
    },
    function (results) {
        // Successful Payment
    },
    function (results) {
        // Unsuccessful Payment
    }
)
```

Requesting payment with existing token (credit card) - skips payment page:

```javascript
konnectedpay.requestPayment(
    {
        merchantId: "your konnectedpay merchant id",     // Get from your KonnectedPay account.
        clientSecret: "your konnectedpay client secret", // Get from your KonnectedPay account.
        amount: 1234.56,
        transId: "your unique transaction id",
        currencyCode: "MYR",
        fullName: "payer's full name",
        email: "payer's email address",
        userId: "your internal user ID - used with konnectedpay.getTokens() method",
        token: "token from konnectedpay.getTokens() method",
    },
    function (results) {
        // Successful Payment
    },
    function (results) {
        // Unsuccessful Payment
    }
)
```

The success or failure callback will receive only one argument:

```javascript
{
    amount: "1234.56", // May be undefined if status is not "S"
    status: "status", // "S" == successful payment. Any other value indicates
        // failed payment. Will be "Incorrect usage" if the function was not
        // called correctly.
    code: "...", // May be undefined if status is not "S"
    desc: "...",
    transId: "the transaction id specified in requestPayment()", // May be undefined if status is not "S"
}
```

Note: You can only have 1 payment at any time - requesting a payment while the
previous payment is still in progress will result in failure.


### Retrieving Tokenised Payment Methods (credit cards)

Each time your users successfully make a payment, if they have not un-checked
the Remember My Card option on the payment page, their credit card information
will be saved in KonnectedPay as a "token".

```javascript
konnectedpay.getTokens(
    {
        merchantId: "your konnectedpay merchant id",     // Get from your KonnectedPay account.
        clientSecret: "your konnectedpay client secret", // Get from your KonnectedPay account.
        userId: "your internal user ID - used with konnectedpay.getTokens() method",
    },
    function (results) {
        // NOTE: results array may be empty.
        // results == [
        //     {
        //         token: "opaque string to pass into konnectedpay.requestPayment()",
        //         maskCard: "411111******1111",
        //         maskCardType: "e.g. VISA, etc",
        //     },
        //     ...
        // ]
    },
    function (errMsgOrUndefined) {
        // This function will be called if we failed to retrieve the tokens list.
    }
)
```


Troubleshooting
---------------

### Unable to build on Android

This plugin depends the AppCompat-v7 library, for which you must install a
version suitable for your Android targetSdkVersion.

By default, this plugin installs `com.android.support:appcompat-v7:22.+`, which
is suitable for Cordova-Android v4.1.1 (targetSdkVersion=22). If you are using
targetSdkVersion=23, then change your Android project to install
`com.android.support:appcompat-v7:23.+` instead (similar workaround for other
SDK versions.


### Unable to load tokens - getTokens fails with `undefined` error

Make sure you have configured the Content Security Policy on the page where you
are calling `.getTokens()`. You must add this directive:

`connect-src https://*.appxtream.com`

e.g.

`<meta http-equiv="Content-Security-Policy" content="default-src 'self' data: gap: https://ssl.gstatic.com 'unsafe-eval'; style-src 'self' 'unsafe-inline'; media-src *; connect-src https://*.appxtream.com">`
