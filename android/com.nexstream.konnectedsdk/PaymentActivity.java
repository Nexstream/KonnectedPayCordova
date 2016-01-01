package com.nexstream.konnectedsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        myWebView.loadUrl(getIntent().getExtras().getString("url"));
        myWebView.setWebViewClient(new WebViewClient());
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void paymentResult(String json) {
            try {
                JSONObject jsonObj  = new JSONObject(json);
                Bundle conData = new Bundle();
                conData.putString("amount", jsonObj.getString("amount"));
                conData.putString("status", jsonObj.getString("status"));
                conData.putString("code", jsonObj.getString("code"));
                conData.putString("desc", jsonObj.getString("desc"));
                conData.putString("tranId", jsonObj.getString("tranId"));
                Intent intent = new Intent();
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            finish();
        }
    }
}
