KonnectedPay Cordova Plugin
===========================

Installation
------------

1. Install this plugin using the Cordova command line tools:

    `cordova plugin add cordova-plugin-konnectedpay`

2. On any page where you want to use the `.getTokens()` method, configure the
    Content Security Policy by adding the directive
    `connect-src https://*.appxtream.com` to the `Content-Security-Policy` META
    tag, e.g.

    ```html
    <meta
        http-equiv="Content-Security-Policy"
        content="default-src 'self' data: gap: https://ssl.gstatic.com 'unsafe-eval'; style-src 'self' 'unsafe-inline'; media-src *; connect-src https://*.appxtream.com"
    />
    ```


### Android

On Android, a few more steps are required:

1. Ensure that your application uses an `android:theme` from AppCompat. If you
    do not already have such a theme:

    1. Create a file `platforms/android/res/values/styles.xml`.
    2. Insert these contents into the new file (sample):

        ```xml
        <resources>
            <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
                <!-- Customize your theme here. -->
                <item name="colorPrimary">#000000</item>
                <item name="colorPrimaryDark">#800000</item>
                <item name="colorAccent">#FF3030</item>
            </style>
        </resources>
        ```

    3. Edit the `<application>` tag in `AndroidManifest.xml` and add the
        following attribute: `android:theme="@style/AppTheme"`. For example:

        ```xml
        <application
            android:hardwareAccelerated="true"
            android:icon="@drawable/icon"
            android:label="@string/app_name"
            android:supportsRtl="true"
            android:theme="@style/AppTheme">
            ...
        </application>
        ```


Usage
-----

### Requesting Payment

Requesting payment with payment form page:

```javascript
konnectedpay.requestPayment(
    {
        merchantId: "your konnectedpay merchant id",     // From your KonnectedPay account.
        clientSecret: "your konnectedpay client secret", // From your KonnectedPay account.
        amount: 1234.56,
        transId: "your unique transaction id",
        currencyCode: "MYR",
        fullName: "payer's full name",
        email: "payer's email address",
        userId: "your internal user ID",
        rememberCard: true, // OPTIONAL. Set to false if you do NOT want to
            // remember the user's card (see Retrieving Tokenised Payment Method).
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
        merchantId: "your konnectedpay merchant id",     // From your KonnectedPay account.
        clientSecret: "your konnectedpay client secret", // From your KonnectedPay account.
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
        // Unsuccessful or Cancelled Payment
    }
)
```

The success or failure callback will receive only one argument.
Successful/failed payment:

```javascript
{
    status: "status", // "S" == successful payment. Any other value indicates
        // failed payment. Will be "Incorrect usage" if the function was not
        // called correctly.
    desc: "...",
    code: "...", // May be undefined if status is not "S"
    amount: "1234.56", // May be undefined if status is not "S"
    tranId: "the transaction id specified in requestPayment()", // May be
        // undefined if status is not "S"
}
```

Cancelled payment:

```javascript
{
    status: "Cancelled",
    desc: "User cancelled",
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

Make sure that you have configured the Content Security Policy on the page where
you are calling this function. Add `connect-src https://*.appxtream.com` to the
`Content-Security-Policy` META tag.


Troubleshooting
---------------

### Unable to load tokens

Symptoms:
- Android: `getTokens` fails with `undefined` error
- iOS: `getTokens` never calls the success or error callbacks

Make sure you have configured the Content Security Policy on the page where you
are calling `.getTokens()`. You must add this directive:

`connect-src https://*.appxtream.com`

e.g.

`<meta http-equiv="Content-Security-Policy" content="default-src 'self' data: gap: https://ssl.gstatic.com 'unsafe-eval'; style-src 'self' 'unsafe-inline'; media-src *; connect-src https://*.appxtream.com">`
