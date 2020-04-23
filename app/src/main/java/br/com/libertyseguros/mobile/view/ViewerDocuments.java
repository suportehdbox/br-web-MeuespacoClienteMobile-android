package br.com.libertyseguros.mobile.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.DocumentPicturesController;
import br.com.libertyseguros.mobile.model.DocumentsPictureModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class ViewerDocuments extends BaseActionBar {

    private DocumentPicturesController documentPicturesController;

    private int x;

    private int y;

    private FlexboxLayout llPhoto;

    private boolean closeScreen;

    private boolean value;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;

    private TextView tvMessageError;

    private LinearLayout llLoading;

    private LinearLayout llContent;

    private ProgressBar pbLoading;


    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        setContentView(R.layout.activity_viewer_documents);

        llPhoto = (FlexboxLayout) findViewById(R.id.ll_photo);
        llPhoto.setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        tvMessageError = (TextView) findViewById(R.id.tv_message_error);

        documentPicturesController = new DocumentPicturesController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    connection = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showLoading(false);

                                if (documentPicturesController.getTypeConnection() == 1 || documentPicturesController.getTypeConnection() == 2) {
                                    tvMessageDialog.setText(getString(R.string.error_get_document));
                                    closeScreen = true;
                                    dialogMessage.show();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                showLoading(false);
                                //tvMessageError.setVisibility(View.GONE);

                                if (documentPicturesController.getTypeConnection() == 1 || documentPicturesController.getTypeConnection() == 2) {
                                    llPhoto.removeAllViews();

                                    sizePhoto();

                                    for (int ind = 0; ind < documentPicturesController.getDocuments().getDocumentsData().size(); ind++) {
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(x, y);

                                        Bitmap bm = documentPicturesController.getImageBitmap(
                                                documentPicturesController.getDocuments().getDocumentsData().get(ind).getIdDocumento() +
                                                        documentPicturesController.getExtension());
                                        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bm, x, y);
                                        Bitmap bitmap = documentPicturesController.imageOreintationValidator(ThumbImage, documentPicturesController.getDocuments().getDocumentsData().get(ind).getIdDocumento() +
                                                documentPicturesController.getExtension());

                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(x * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        llPhoto.setLayoutParams(layoutParams);

                                        addPhoto(documentPicturesController.getDocuments().getDocumentsData().get(ind).getIdDocumento() +
                                                documentPicturesController.getExtension(), bitmap, params);
                                    }

                                    if (documentPicturesController.getDocuments().getDocumentsData().size() == 0) {
                                        showLoading(true);
                                        tvMessageError.setText(getString(R.string.documents_empty));
                                        tvMessageError.setVisibility(View.VISIBLE);
                                        pbLoading.setVisibility(View.GONE);
                                    }

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, this, 0, 0);


        configDialog();

        showLoading(true);
        documentPicturesController.getDocuments(this);


    }

    /**
     * set size thumb
     */
    private void sizePhoto() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        x = (width / 2);
        y = ((width / 2)) + (width / 8);
    }

    private void addPhoto(String uriImage, Bitmap bitmap, LinearLayout.LayoutParams parans) {
        ImageViewCustom ivPhoto = new ImageViewCustom(this);

        if (uriImage != null) {
            ivPhoto.setTag(uriImage);
        }

        if (bitmap == null) {
            ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_document_error));
        } else {
            ivPhoto.setImageBitmap(bitmap);
        }

        ivPhoto.setLayoutParams(parans);
        ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ImageUri", v.getTag().toString());

                setResult(98, resultIntent);
                finish();
            }
        });

        // tvMessageDelete.setVisibility(View.VISIBLE);

        llPhoto.addView(ivPhoto);
    }

    /**
     * Config Dialogs
     */
    private void configDialog() {


        dialogMessage = new Dialog(this, R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                if (closeScreen) {
                    closeScreen = false;
                    finish();
                }
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
                    llContent.setVisibility(View.GONE);
                    tvMessageError.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                    tvMessageError.setVisibility(View.GONE);
                }
            }
        });

    }


}
