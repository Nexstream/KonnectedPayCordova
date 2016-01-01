KonnectedPay Cordova Plugin
===========================

Installation
------------

`cordova plugin add my.com.nexstream.konnectedpay`

Usage
-----

```javascript
konnectedpay.requestPayment(
    {
        amount: 1234.56,
        transId: "your-unique-transaction-id",
        currencyCode: "MYR",
        merchantId: "AEVI456"
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
    amount: "1234.56",
    status: "status",
    code: "{{.code}}",
    desc: "{{.desc}}",
    tranId: "the-transaction-id-you-specified-in-requestPayment()",
}
```

Status == `"S"` indicates successful payment. Any other response indicates that
the payment was **not** successful.
