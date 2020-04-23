package br.com.libertyseguros.mobile.view;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.apache.http.util.EncodingUtils;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.AnalyticsApplication;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;


public class PaymentWebView extends BaseActionBar {

       private LinearLayout llContent;

    private WebView wvClub;

    private LinearLayout llLoading;

    private boolean value;

    private ProgressBar progress_bar;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);


        setContentView(R.layout.activity_glass_webview);

        wvClub = (WebView) findViewById(R.id.wb_glass);
//        setTitle(getResources().getString(R.string.title_action_bar_14));

        WebSettings settings = wvClub.getSettings();
        settings.setJavaScriptEnabled(true);
        wvClub.setWebViewClient(new MyWebViewClient());
        wvClub.getSettings().setJavaScriptEnabled(true);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        String url = "";

        if(BuildConfig.prod){
            url = getString(R.string.url_payment);
        }else{
            url = getString(R.string.url_payment_act);
        }

        Bundle extras = getIntent().getExtras();

        String urlFinal = url + "?idSessao=" + extras.getString("sessionId") + "&UserID=app";

        wvClub.loadUrl(urlFinal);

    }


    public class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            //
            super.onPageStarted(view, url, favicon);

            progress_bar.animate();
            progress_bar.setVisibility(View.VISIBLE);

            //https://act-dmz.libertyseguros.com.br/PagamentoDigitalClientesUI/UI/PagamentoOnline/PagamentoOnlineAPPController.aspx?status=pagok
            if(url.contains("status=pagok")){
                goBack(true);
                return;
            }else if(url.contains("status=cancel")){
                goBack(false);
                return;
            }

        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            if(url.contains("status=pagok")) {
                goBack(true);
                return true;
            }else if(url.contains("status=cancel")){
                goBack(false);
                return true;
            }
            return false;
        }
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(request.getUrl().toString().contains("status=pagok")){
                goBack(true);
                return true;
            }else if(request.getUrl().toString().contains("status=cancel")){
                goBack(false);
                return true;
            }

            view.loadUrl(request.getUrl().toString());
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress_bar.setVisibility(View.INVISIBLE);
        }
    }

    public void goBack(Boolean shouldNotify){
        if(shouldNotify){
            Intent local = new Intent();

            local.setAction("br.com.libertyseguros.reloadScreen");

            this.sendBroadcast(local);
        }

        finish();
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvClub.canGoBack()) {
//            wvClub.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
