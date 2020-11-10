package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.DocumentsAdapter;
import br.com.libertyseguros.mobile.beans.DocumentsBeans;
import br.com.libertyseguros.mobile.controller.DocumentPicturesController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.model.DocumentsPictureModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ExpandableGridViewNoScroll;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;

public class Documents extends BaseActionBar implements View.OnClickListener {

    private static boolean skipOnActivity;

    private DocumentPicturesController documentPicturesController;

    private DocumentsAdapter.OnSelectedPhoto onSelectedPhoto;

    private LinearLayout llLoading;
    private LinearLayout llContent;
    private LinearLayout llRodape;

    private Dialog dialogMessage;
    private Dialog dialogMessageTwoButton;
    private Dialog dialogDelete;
    private Dialog dialogError;
    private Dialog dialogUploadDelete;

    private ImageViewCustom ivPhoto;

    private Toast toast;

    private TextViewCustom tvSpeakLiberty;
    private TextView tvMessageDialog;
    private TextView tvMessageUploadDeleteDialog;
    private TextView tvPhotoSize;
    private TextView tvMessageError;
    private TextView tvMessageErrorDialog;
    private TextView tvErrorOk;

    private boolean connectionNow;
    private boolean closeScreen;
    private boolean value;

    private int x;
    private int y;

    private ButtonViewCustom btNext;
    private ImageViewCustom ivDelete;

    private ExpandableGridViewNoScroll documentsGridView;

    private DocumentsAdapter documentsAdapter;

    public void onDestroy() {
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_13));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_documents);

        mFirebaseAnalytics.setCurrentScreen(this, "Envio Fotos e Documentos", null);

        onSelectedPhoto = new DocumentsAdapter.OnSelectedPhoto() {
            @Override
            public void onSelected(int index) {
                if (index > 0) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }
        };

        documentsGridView = findViewById(R.id.documents_grid_view);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        x = (width / 2) - (int) (((getResources().getDimension(R.dimen.margin_default_2) * 2.5)));
        y = ((width / 4)) + (width / 8);

        skipOnActivity = false;

        documentPicturesController = new DocumentPicturesController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    connectionNow = false;
                    connection = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showLoading(false);

                                switch (documentPicturesController.getTypeConnection()) {
                                    case 1:
                                    case 2:
                                        tvMessageDialog.setText(getString(R.string.error_get_document));
                                        closeScreen = true;
                                        dialogMessage.show();
                                        break;
                                    case 3:
                                        if (documentPicturesController.getMessage() == null) {
                                            tvMessageDialog.setText(R.string.error_upload_document);
                                        } else {
                                            tvMessageDialog.setText(documentPicturesController.getMessage().getMessage());
                                        }
                                        closeScreen = false;
                                        dialogMessage.show();
                                        break;
                                    case 4:
                                        tvMessageDialog.setText(R.string.error_delete_document);
                                        closeScreen = false;
                                        dialogMessage.show();
                                        break;
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
                                tvMessageError.setVisibility(View.GONE);

                                switch (documentPicturesController.getTypeConnection()) {
                                    case 1:
                                    case 2:
                                        ivDelete.setVisibility(View.GONE);


                                        if (documentPicturesController.getDocuments().getDocumentsData().size() > 0) {
                                            tvPhotoSize.setVisibility(View.VISIBLE);
                                            tvPhotoSize.setText(Html.fromHtml(documentPicturesController.getTextSizePhots(Documents.this)));
                                        } else {
                                            tvPhotoSize.setVisibility(View.GONE);
                                        }

                                        documentsAdapter = new DocumentsAdapter(Documents.this, documentPicturesController.getDocuments().getDocumentsData(), documentPicturesController, onSelectedPhoto);
                                        documentsGridView.setAdapter(documentsAdapter);
                                        break;
                                    case 3:
                                        tvMessageUploadDeleteDialog.setText(getString(R.string.success_upload_document));
                                        dialogUploadDelete.show();
                                        break;
                                    case 4:
                                        tvMessageUploadDeleteDialog.setText(getString(R.string.success_delete_document));
                                        dialogUploadDelete.show();
                                        break;
                                }
                                if (documentPicturesController.getDocuments().getDocumentsData().size() >= DocumentsPictureModel.TOTAL_DOCUMENTS) {
                                    btNext.setVisibility(View.GONE);
                                } else {
                                    btNext.setVisibility(View.VISIBLE);
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
        }, this, x, y);

        documentsAdapter = new DocumentsAdapter(this, new ArrayList<DocumentsBeans.DocumentData>(), documentPicturesController, onSelectedPhoto);

        documentsGridView.setAdapter(documentsAdapter);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        ivDelete = (ImageViewCustom) findViewById(R.id.iv_delete);
        ivDelete.setVisibility(View.GONE);
        ivDelete.setOnClickListener(this);

        btNext = (ButtonViewCustom) findViewById(R.id.bt_send);
        btNext.setOnClickListener(this);

        llRodape = (LinearLayout) findViewById(R.id.ll_rodape);

        ivPhoto = (ImageViewCustom) findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(Documents.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Documents.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Documents.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Documents.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA},
                    1);

        }

        tvPhotoSize = (TextView) findViewById(R.id.tv_photo_size);

        tvMessageError = (TextView) findViewById(R.id.tv_message_error);
        tvMessageError.setText(getResources().getString(R.string.message_upload_photo));

        tvSpeakLiberty = (TextViewCustom) findViewById(R.id.tv_speak_liberty);
        tvSpeakLiberty.setOnClickListener(this);
        tvSpeakLiberty.setPaintFlags(tvSpeakLiberty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        configDialog();

        showLoading(true);
        documentPicturesController.getDocuments(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_warning:
                tvMessageErrorDialog.setText(Html.fromHtml(getResources().getString(R.string.message_step4)));
                tvErrorOk.setText(getResources().getString(R.string.bt_Ok_2));
                dialogError.show();
                break;
            case R.id.tv_speak_liberty:
                documentPicturesController.openSupport(Documents.this);
                break;
            case R.id.bt_send:
                if (documentPicturesController.getDocuments().getDocumentsData().size() <= 0) {
                    tvMessageDialog.setText(getString(R.string.none_picture_error));
                    dialogMessage.show();
                    return;
                }
                showLoading(true);
                tvMessageError.setVisibility(View.VISIBLE);
                tvMessageError.setText(getResources().getString(R.string.message_upload_photo));
                documentPicturesController.createUploadFiles(this);
                break;
            case R.id.iv_delete:
                deletePhoto();
                break;
            case R.id.iv_photo:
                skipOnActivity = false;
                if (documentPicturesController.getDocuments().getDocumentsData().size() < DocumentsPictureModel.TOTAL_DOCUMENTS) {
                    if (ContextCompat.checkSelfPermission(Documents.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Documents.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(Documents.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (toast != null) {
                            toast.cancel();
                        }

                        toast = Toast.makeText(Documents.this, getString(R.string.error_camera), Toast.LENGTH_SHORT);
                        toast.show();


                    } else {
                        documentPicturesController.openMakePhotoIntent(getString(R.string.choose_photo), Documents.this);
                    }

                } else {
                    tvMessageDialog.setText(getString(R.string.photo_error_five));
                    dialogMessage.show();
                }

                break;
        }
    }

    /**
     * Config Dialogs
     */
    private void configDialog() {

        dialogUploadDelete = new Dialog(this, R.style.AppThemeDialog);
        dialogUploadDelete.setCancelable(false);
        dialogUploadDelete.setContentView(R.layout.dialog_upload_document);

        tvMessageUploadDeleteDialog = (TextView) dialogUploadDelete.findViewById(R.id.tv_message_documents);
        ImageView ivOk = (ImageView) dialogUploadDelete.findViewById(R.id.iv_ok);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUploadDelete.dismiss();
                showLoading(true);
                documentPicturesController.getDocuments(Documents.this);
            }
        });


        dialogError = new Dialog(this, R.style.AppThemeDialog);
        dialogError.setCancelable(false);

        dialogError.setContentView(R.layout.dialog_message);

        tvMessageErrorDialog = (TextView) dialogError.findViewById(R.id.tv_dialog_message);

        tvErrorOk = (TextView) dialogError.findViewById(R.id.tv_ok);

        tvErrorOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.dismiss();
                if (closeScreen) {
                    closeScreen = false;
                    finish();
                }
            }
        });


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

        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogMessageTwoButton.setCancelable(false);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);
                finish();

            }
        });


        dialogDelete = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogDelete.setCancelable(false);

        dialogDelete.setContentView(R.layout.dialog_message_two_button);

        TextView tvNo = (TextView) dialogDelete.findViewById(R.id.tv_cancel);
        tvNo.setText(getString(R.string.bt_no));

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });

        TextView tvYes = (TextView) dialogDelete.findViewById(R.id.tv_try_again);
        tvYes.setText(getString(R.string.bt_yes));

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView tvTitle = (TextView) dialogDelete.findViewById(R.id.tv_dialog_message);
        tvTitle.setText(getString(R.string.delete_file));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.i(Config.TAG, "onActivityResult");

        if (!skipOnActivity) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Uri uriImage = null;

                if (data != null) {
                    uriImage = data.getData();
                }

                if (uriImage == null) {
                    uriImage = documentPicturesController.getOutPutfileUri();
                }

                addPhoto(uriImage);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    llRodape.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                    llRodape.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (!connectionNow) {
            super.onBackPressed();
        }
    }

    /**
     * Delete Photos
     */
    private void deletePhoto() {
        showLoading(true);
        tvMessageError.setVisibility(View.VISIBLE);
        tvMessageError.setText(getResources().getString(R.string.message_delete_photo));
        documentPicturesController.deleteImages();
    }


    /**
     * Add photo in view
     */
    private void addPhoto(Uri uriImage) {
        try {

            String path = documentPicturesController.getRealPathFromURI(uriImage, this);

            if(path == null){
                Toast.makeText(this, getString(R.string.error_image_gallery), Toast.LENGTH_SHORT).show();
            } else {
                documentsAdapter.add(documentPicturesController.addNewPhoto(path));
                documentsAdapter.notifyDataSetChanged();

                tvPhotoSize.setVisibility(View.VISIBLE);
                tvPhotoSize.setText(Html.fromHtml(documentPicturesController.getTextSizePhots(Documents.this)));
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
