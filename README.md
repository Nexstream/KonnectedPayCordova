KonnectedPay Cordova Plugin
===========================

Installation
------------

`cordova plugin add my.com.nexstream.konnectedpay`


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
    status: "status", // Will be "Incorrect usage" if the function was not called correctly
    code: "{{.code}}", // May be undefined if status is not "S"
    desc: "{{.desc}}",
    transId: "the transaction id specified in requestPayment()", // May be undefined if status is not "S"
}
```

Status == `"S"` indicates successful payment. Any other response indicates that
the payment was **not** successful.


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
