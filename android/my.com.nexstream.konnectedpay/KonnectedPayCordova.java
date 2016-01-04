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

    // Config ------------------------------------------------------------------

    private static int PAYMENT_ACT_REQUEST_CODE = 0x16CE3BFB;


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

                try {
                    Payment.make(
                        cordova.getActivity(),
                        params.getString("fullName"),
                        params.getString("email"),
                        params.getString("userId"),
                        params.getString("transId"),
                        params.getString("amount"),
                        params.getString("currencyCode"),
                        params.optString("token", null)
                    );
                } catch (KonnectedException e) {
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
        if(requestCode == PAYMENT_ACT_REQUEST_CODE &&
           paymentOngoing && paymentCallbackContext != null
        ) {
            try {
                Bundle extras = intent.getExtras();
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

}
