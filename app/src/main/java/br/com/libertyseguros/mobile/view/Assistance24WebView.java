package br.com.libertyseguros.mobile.view;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;


public class Assistance24WebView extends BaseActionBar {

       private LinearLayout llContent;

    private WebView wvClub;

    private LinearLayout llLoading;

    private boolean value;

    private ProgressBar progressBar;

    private String requestOring = null;
    private GeolocationPermissions.Callback requestCallback = null;

    private final int MY_REQUEST_PERMISSION = 123;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_glass_webview);

        wvClub = (WebView) findViewById(R.id.wb_glass);
//        setTitle(getResources().getString(R.string.title_action_bar_14));


        String url = getString(R.string.assist24_prod);

        if(!BuildConfig.prod){
            url = getString(R.string.assist24_act);
        }

        url += "?plate="+getIntent().getStringExtra("plate");;


        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } else{
            // do something for phones running an SDK before lollipop
            progressBar = findViewById(R.id.progress_bar);

            wvClub.setWebViewClient(new MyWebClient());
            wvClub.setWebChromeClient(new MyWebViewClient());
            wvClub.getSettings().setJavaScriptEnabled(true);

            wvClub.loadUrl(url);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_REQUEST_PERMISSION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(requestCallback != null){
                        requestCallback.invoke(requestOring, true, true);
                    }
                }else{
                    if(requestCallback != null){
                        requestCallback.invoke(requestOring, false, false);
                    }
                }
                return;
            }
            default:
                break;
        }
    }

    public class MyWebClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.contains("quit")){
                finish();
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(url.contains("quit")){
                finish();
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    public class MyWebViewClient extends WebChromeClient
    {






        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress > 99){
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
//            super.onGeolocationPermissionsShowPrompt(origin, callback);
            requestOring = null;
            requestCallback = null;

            if(ContextCompat.checkSelfPermission(Assistance24WebView.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(Assistance24WebView.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                    new AlertDialog.Builder(Assistance24WebView.this).setMessage(R.string.location_enable_alert)
                            .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestCallback = callback;
                                    requestOring = origin;
                                    ActivityCompat.requestPermissions(Assistance24WebView.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_PERMISSION);
                                }
                            }).show();

                }else{
                    requestCallback = callback;
                    requestOring = origin;
                    ActivityCompat.requestPermissions(Assistance24WebView.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_PERMISSION);
                }
            }else{
                callback.invoke(origin,true, true);
            }
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
