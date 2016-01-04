package com.nexstream.konnectedsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nexstream.konnectedsdk.exception.KonnectedException;


public class Payment {
    private static String CLIENT_SECRET = null;
    private static String MERCHANT_ID = null;
    private static String HOST = "https://pay.appxtream.com";
    public static int PAYMENT_RETURN = 8888;

    public enum CurrencyCode {
        MYR, USD, AED, AUD, BND, CHF, CNY, EGP, EUR, GBP, HKD, IDR, INR, JPY, KRW, LKR, NZD, PHP, PKR, SAR, SEK, SGD, THB, TWD, ZAR
    }

    public static void init(String clientSecret, String merchantId) {
        CLIENT_SECRET = clientSecret;
        MERCHANT_ID = merchantId;
    }

    private static boolean isInitialize() throws KonnectedException {
        if (CLIENT_SECRET == null || MERCHANT_ID == null)
            return false;
        else
            return true;
    }

    public static String getTokenListUrl(String userId) throws KonnectedException {
        isInitialize();
        return HOST + "/payment/token/" + userId + "?clientSecret=" + CLIENT_SECRET;
    }

    public static void make(Context mContext, String fullName, String email, String userId, String tranId, String amount, CurrencyCode currencyCode, String token) throws KonnectedException {
        if (isInitialize()) {
            if(fullName == null || email == null || userId == null || tranId == null || amount == null || currencyCode == null){
                throw new KonnectedException("Please make sure all mandatory param cannot be null");
            }else {
                String url = HOST +
                        "/payment?merchantId=" + MERCHANT_ID +
                        "&tranId=" + tranId +
                        "&amount=" + amount +
                        "&currencyCode=" + currencyCode +
                        "&device=" + android.os.Build.MODEL +
                        "&os=" + android.os.Build.VERSION.RELEASE +
                        "&fullname=" + fullName +
                        "&email=" + email +
                        "&userId=" + userId;

                if (token != null) {
                    url += "&token=" + token;
                }

                Intent mIntent = new Intent(mContext, PaymentActivity.class);
                mIntent.putExtra("url", url);
                ((Activity) mContext).startActivityForResult(mIntent, PAYMENT_RETURN);
            }
        } else {
            throw new KonnectedException("Please initialize konnectedPay SDK before use");
        }
    }
}
