package br.com.libertyseguros.mobile.view;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;


public class GlassAssistanceWebView extends BaseActionBar {

       private LinearLayout llContent;

    private WebView wvClub;

    private LinearLayout llLoading;

    private boolean value;


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

        wvClub.loadUrl(getString(R.string.url_glass_assistance));

//        wvClub.loadData("<a href='https://www.abraseuatendimento.com.br/#/liberty'>TESTE</a>","text/html","utf-8");
//        wvClub.postUrl("www.google.com.br", null);
//        Timer t = new Timer();

//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        wvClub.loadUrl(getString(R.string.url_glass_assistance));
//                    }});
//            }
//        },500);
//
//
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_glass_assistance)));
        startActivity(browserIntent);
    }


    public class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return false;
        }
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvClub.canGoBack()) {
            wvClub.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
