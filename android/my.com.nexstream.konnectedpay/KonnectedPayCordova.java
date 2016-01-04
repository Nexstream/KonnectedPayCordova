/***
 * Copyright 2015 Nexstream Sdn Bhd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/


package my.com.nexstream.konnectedpay;

import android.content.Intent;
import android.os.Bundle;
import com.nexstream.konnectedsdk.Payment;
import com.nexstream.konnectedsdk.exception.KonnectedException;
import java.util.Locale;
import java.io.Serializable;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KonnectedPayCordova extends CordovaPlugin {

    // State -------------------------------------------------------------------

    private boolean paymentOngoing = false;
    private CallbackContext paymentCallbackContext;


    // Plugin Entry Point ------------------------------------------------------

    @Override
    public boolean execute (String action, JSONArray args, CallbackContext callbackContext)
    throws JSONException
    {
        if(action.equals("requestPayment")) {

            if(paymentOngoing) {
                callbackContext.error("Another payment is in progress!");
            } else {
                paymentOngoing = true;
                paymentCallbackContext = callbackContext;

                JSONObject params = args.getJSONObject(0);

                Payment.init(
                    params.getString("clientSecret"),
                    params.getString("merchantId")
                );

                cordova.setActivityResultCallback(this);

                try {
                    Payment.make(
                        cordova.getActivity(),
                        params.getString("fullName"),
                        params.getString("email"),
                        params.getString("userId"),
                        params.getString("transId"),
                        params.getString("amount"),
                        stringToCurrencyCode(params.getString("currencyCode")),
                        params.optString("token", null)
                    );
                } catch (Exception e) {
                    paymentOngoing = false;
                    paymentCallbackContext = null;
                    callbackContext.error(e.getMessage());
                }
            }

            return true;

        } else if(action.equals("getTokenListUrl")) {

            JSONObject params = args.getJSONObject(0);

            Payment.init(
                params.getString("clientSecret"),
                params.getString("merchantId")
            );

            try {
                callbackContext.success(Payment.getTokenListUrl(params.getString("userId")));
            } catch (KonnectedException e) {
                callbackContext.error(e.getMessage());
            }

            return true;

        } else {
            return false;
        }
    }


    // Payment response handler ------------------------------------------------

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == Payment.PAYMENT_RETURN &&
           paymentOngoing && paymentCallbackContext != null
        ) {
            try {
                if(intent == null) throw new Exception("No response received");
                Bundle extras = intent.getExtras();
                if(extras == null) throw new Exception("No response received");
                String status = extras.getString("status");

                JSONObject resp = new JSONObject();
                resp.put("amount", extras.getString("amount"));
                resp.put("status", status);
                resp.put("code", extras.getString("code"));
                resp.put("desc", extras.getString("desc"));
                resp.put("transId", extras.getString("tranId"));

                if(status.equals("S")) paymentCallbackContext.success(resp);
                else paymentCallbackContext.error(resp);
            } catch (Exception e) {
                paymentCallbackContext.error("Unexpected failure: "+e.getMessage());
            } finally {
                paymentOngoing = false;
                paymentCallbackContext = null;
            }
        }
    }


    // Helpers -----------------------------------------------------------------

    private Payment.CurrencyCode stringToCurrencyCode (String code)
    throws Exception
    {
        if(code.equals("MYR")) return Payment.CurrencyCode.MYR;
        else if(code.equals("USD")) return Payment.CurrencyCode.USD;
        else if(code.equals("AED")) return Payment.CurrencyCode.AED;
        else if(code.equals("AUD")) return Payment.CurrencyCode.AUD;
        else if(code.equals("BND")) return Payment.CurrencyCode.BND;
        else if(code.equals("CHF")) return Payment.CurrencyCode.CHF;
        else if(code.equals("CNY")) return Payment.CurrencyCode.CNY;
        else if(code.equals("EGP")) return Payment.CurrencyCode.EGP;
        else if(code.equals("EUR")) return Payment.CurrencyCode.EUR;
        else if(code.equals("GBP")) return Payment.CurrencyCode.GBP;
        else if(code.equals("HKD")) return Payment.CurrencyCode.HKD;
        else if(code.equals("IDR")) return Payment.CurrencyCode.IDR;
        else if(code.equals("INR")) return Payment.CurrencyCode.INR;
        else if(code.equals("JPY")) return Payment.CurrencyCode.JPY;
        else if(code.equals("KRW")) return Payment.CurrencyCode.KRW;
        else if(code.equals("LKR")) return Payment.CurrencyCode.LKR;
        else if(code.equals("NZD")) return Payment.CurrencyCode.NZD;
        else if(code.equals("PHP")) return Payment.CurrencyCode.PHP;
        else if(code.equals("PKR")) return Payment.CurrencyCode.PKR;
        else if(code.equals("SAR")) return Payment.CurrencyCode.SAR;
        else if(code.equals("SEK")) return Payment.CurrencyCode.SEK;
        else if(code.equals("SGD")) return Payment.CurrencyCode.SGD;
        else if(code.equals("THB")) return Payment.CurrencyCode.THB;
        else if(code.equals("TWD")) return Payment.CurrencyCode.TWD;
        else if(code.equals("ZAR")) return Payment.CurrencyCode.ZAR;
        else throw new Exception("Invalid currency code");
    }
}
