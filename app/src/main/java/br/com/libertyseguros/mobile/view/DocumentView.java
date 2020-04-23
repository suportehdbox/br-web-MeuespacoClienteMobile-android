package br.com.libertyseguros.mobile.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.DocumentPicturesController;
import br.com.libertyseguros.mobile.libray.DocumentsImageManager;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;



public class DocumentView  extends BaseActionBar {

    private String path;

    private String nameImage;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_document_view);

        if(getIntent().getExtras().getString("image") != null){
            path = getIntent().getExtras().getString("image");
        }

        if(getIntent().getExtras().getString("nameImage") != null){
            nameImage = getIntent().getExtras().getString("nameImage");
        }

        setTitle(getString(R.string.title_action_bar_13));

        Bitmap bitmap = null;

        if(path != null) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(path, bmOptions);
        } else if(nameImage != null){
            DocumentsImageManager dim = new DocumentsImageManager(this);
            bitmap = dim.getImageBitmap(nameImage);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String dataURL= "data:image/png;base64," + imgageBase64;

        WebView webView = (WebView) findViewById(R.id.wv_photo);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.loadUrl(dataURL);
        webView.setBackgroundColor(Color.TRANSPARENT);

        webView.setInitialScale(110);
    }
}
