package br.com.libertyseguros.mobile.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.AssistanceController;
import br.com.libertyseguros.mobile.controller.VehicleAccidentController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;


public class AutoClaimWebView extends BaseActionBar {

    private WebView wvClub;
    private ProgressBar progressBar;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int INPUT_CAMERA_REQUEST_CODE = 2;
    private Uri saveImageUri;
    private String packageName;
    private AssistanceController controller;
    private Dialog dialogMessage;

    private String requestOring = null;
    private GeolocationPermissions.Callback requestCallback = null;
    private final int MY_REQUEST_PERMISSION = 91;

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        controller = new AssistanceController(this);
        configDialog();
        controller.checkHomeAssistancePermissions();

        setContentView(R.layout.activity_glass_webview);
        wvClub = (WebView) findViewById(R.id.wb_glass);

        packageName = this.getApplicationContext().getPackageName();

        VehicleAccidentController controller = new VehicleAccidentController(new OnConnectionResult() {
            @Override
            public void onError() {

            }

            @Override
            public void onSucess() {

            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(controller.getWebViewUrl(this)));
            startActivity(browserIntent);
        } else {
            // do something for phones running an SDK before lollipop
            progressBar = findViewById(R.id.progress_bar);

            wvClub.setWebViewClient(new MyWebClient());
            wvClub.setWebChromeClient(new MyWebViewClient());
            wvClub.getSettings().setJavaScriptEnabled(true);
            wvClub.getSettings().setAllowFileAccess(true);
            wvClub.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            wvClub.getSettings().setDomStorageEnabled(true);
            wvClub.loadUrl(controller.getWebViewUrl(this));
        }



    }
    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogMessage = new Dialog(this ,R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message_permission_location);

        TextView tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);
        tvMessageDialog.setText(getString(R.string.error_location_permission));

        TextView tvClose = (TextView) dialogMessage.findViewById(R.id.tv_close);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                finish();
            }
        });


        TextView tvConfiguration = (TextView) dialogMessage.findViewById(R.id.tv_configuration);

        tvConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(!controller.checkPermissionsGranted(requestCode, permissions, grantResults,false)){
            dialogMessage.show();
        }
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

    public class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains("tel:") || url.contains("#external")) {
                // Could be cleverer and use a regex
                //Open links in new browser
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                // Here we can open new activity
                return true;
            }else if (url.contains("share:")) {
                // Could be cleverer and use a regex
                String[] texts = url.split("/");
                String shareBody = texts[1] + " " + texts[2];
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_title)));
                // Here we can open new activity
                return true;
            } else if (url.contains("quit") || url.contains("#home")) {
                finish();
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("quit")) {
                finish();
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if ((requestCode != INPUT_FILE_REQUEST_CODE && requestCode != INPUT_CAMERA_REQUEST_CODE)  || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {

                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (saveImageUri != null) {
                        results = new Uri[]{saveImageUri};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }else{
                        if(data.getData() != null) {
                            results = new Uri[]{data.getData()};
                        }else{
                            results = new Uri[]{saveImageUri};
                        }
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
        return;
    }


    public class MyWebViewClient extends WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
//            super.onGeolocationPermissionsShowPrompt(origin, callback);
            requestOring = null;
            requestCallback = null;

            if(ContextCompat.checkSelfPermission(AutoClaimWebView.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(AutoClaimWebView.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                    new AlertDialog.Builder(AutoClaimWebView.this).setMessage(R.string.location_enable_alert)
                            .setNeutralButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestCallback = callback;
                                    requestOring = origin;
                                    ActivityCompat.requestPermissions(AutoClaimWebView.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_PERMISSION);
                                }
                            }).show();

                }else{
                    requestCallback = callback;
                    requestOring = origin;
                    ActivityCompat.requestPermissions(AutoClaimWebView.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_PERMISSION);
                }
            }else{
                callback.invoke(origin,true, true);
            }
        }

        private void showDialog() {

            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "JPEG_" + System.currentTimeMillis() + ".jpg");

            } catch (Exception ex) {
                Log.v(Config.TAG, "Error image = " + ex.toString());
            }

            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(AutoClaimWebView.this,
                        (packageName + ".provider"),
                        photoFile);
                saveImageUri = Uri.fromFile(photoFile);
                takePicture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }

            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent chooser = Intent.createChooser(pickPhoto, getString(R.string.title_pick_image));
            Intent[] extraIntents = {takePicture};
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            startActivityForResult(chooser , INPUT_FILE_REQUEST_CODE);

        }

        private void startImageActivity (ValueCallback<Uri[]> uploadMsg) {
            mFilePathCallback = uploadMsg;
            showDialog();
        }

        protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            showDialog();
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }

            mFilePathCallback = filePathCallback;
            try {
                showDialog();
            } catch (ActivityNotFoundException e) {
                mUploadMessage = null;
                Toast.makeText(AutoClaimWebView.this, getString(R.string.error_pick_image), Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
        protected void openFileChooser(ValueCallback<Uri[]> uploadMsg, String acceptType, String capture) {
           this.startImageActivity(uploadMsg);
        }

        protected void openFileChooser(ValueCallback<Uri[]> uploadMsg) {
            this.startImageActivity(uploadMsg);
        }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress > 99) {
            progressBar.setVisibility(View.GONE);
        }
    }
}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvClub.canGoBack()) {
            wvClub.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
