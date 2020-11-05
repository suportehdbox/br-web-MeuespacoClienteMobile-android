package br.com.libertyseguros.mobile.view;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.net.URLEncoder;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ClubController;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;


public class ClubWebView extends BaseActionBar implements View.OnClickListener{

    private ClubController clubController;

    private LinearLayout llContent;

    private WebView wvClub;

    private LinearLayout llLoading;

    private boolean value;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_club_webview);

        wvClub = (WebView) findViewById(R.id.wb_club);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ClubWebView.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

        }

        setTitle(getResources().getString(R.string.title_action_bar_11));

        mFirebaseAnalytics.setCurrentScreen(this, getResources().getString(R.string.title_action_bar_11), null);

        clubController = new ClubController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showLoading(false);
                                if (clubController.getTypeError() == 1) {
                                    dialogMessageTwoButton.show();
                                } else {

                                    if (clubController.getMessage() == null) {
                                        dialogMessageTwoButton.show();
                                    } else {
                                        tvMessageDialog.setText(clubController.getMessage().getMessage());
                                        dialogMessage.show();
                                    }

                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }

                        }
                    });
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess() {
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                showLoading(false);

                                wvClub.postUrl(clubController.getUrl(), clubController.getPostData());

                            } catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }, null, this);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        WebSettings webSettings = wvClub.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        //wvClub.setWebViewClient(new WebViewClient());
        wvClub.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("#external")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setPackage("com.android.chrome");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    return  true;

                } else {
                    return  false;

                }

            }
        });

        wvClub.getSettings().setGeolocationEnabled(true);

        wvClub.getSettings().setAppCacheEnabled(true);
        wvClub.getSettings().setDatabaseEnabled(true);
        wvClub.getSettings().setDomStorageEnabled(true);
        wvClub.getSettings().setGeolocationDatabasePath(getFilesDir().getPath());


        wvClub.setWebChromeClient(new WebChromeClient(){
            @Override
           public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
               // callback.invoke(String origin, boolean allow, boolean remember);
               callback.invoke(origin, true, false);
           }
       });


        configDialog();

        showLoading(true);

       clubController.getClubAccess(this);


    }

    @Override
    public void onClick(View view){
        switch(view.getId()){

            case R.id.tv_change_password:
                clubController.openForgotPassword(this);
                break;
        }
    }

    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                finish();
            }
        });


        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                finish();
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);
                clubController.getClubAccess(ClubWebView.this);

            }
        });

    }

    /* Show progress loading
    * @param v
    * @param m
    */
    private void showLoading(boolean v) {
        this.value = v;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    llLoading.setVisibility(View.VISIBLE);
                    wvClub.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    wvClub.setVisibility(View.VISIBLE);
                }
            }
        });

    }


}
