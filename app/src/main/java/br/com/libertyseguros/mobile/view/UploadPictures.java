package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.UploadPicturesController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;

public class UploadPictures extends BaseActionBar implements View.OnClickListener {


    private LinearLayout llLoading;

    private LinearLayout llContent;

    private UploadPicturesController uploadPicturesController;

    private FlexboxLayout llPhoto;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;

    private Dialog dialogMessageTwoButton;

    private Dialog dialogDelete;

    private Dialog dialogError;

    private Dialog dialogSendVehicleAccident;

    private Dialog dialogImageType;

    private ImageViewCustom ivPhoto;

    private Toast toast;

    private View viewDelete;

    private boolean value;

    private TextView tvPhotoSize;

    private TextView tvNumber;

    private TextView tvMessageError;

    private TextView tvMessageErrorDialog;

    private static boolean skipOnActivity;

    private TextView tvErrorOk;

    private TextView tvMessageDelete;

    private Chronometer chronometer;

    private boolean connectionNow;

    private int x;

    private int y;
    private TextViewCustom tvSpeakLiberty;

    private LinearLayout.LayoutParams params;

    private String numberPol;

    public void onDestroy() {
        super.onDestroy();
    }


    public void onResume(){
        super.onResume();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_9));

        //VehicleAccidentModel.exit = true;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_upload_pictures);

        skipOnActivity = false;

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        mFirebaseAnalytics.setCurrentScreen(this, "Envio Fotos e Documentos", null);

        uploadPicturesController = new UploadPicturesController(new OnConnectionResult() {
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
                                if (uploadPicturesController.getTypeError() == 1) {
                                    dialogMessageTwoButton.show();
                                } else {
                                    tvMessageDialog.setText(uploadPicturesController.getMessage().getMessage());
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
                                if (uploadPicturesController.getTypeConnection() != 1) {

                                    if (uploadPicturesController.getIndexRemove() == -1) {
                                        connectionNow = false;
                                        connection = false;
                                        dialogSendVehicleAccident.show();

                                    } else {
                                        tvMessageError.setText(getResources().getString(R.string.message_upload_photo));
                                        uploadPicturesController.createUploadFiles(UploadPictures.this);
                                        //Log.i(Config.TAG, "File Size > 10 new connection");
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
        });

        Bundle extras = getIntent().getExtras();

        String numberString = extras.getString("sinistro");
        uploadPicturesController.setNumberClaim(numberString);

        numberPol = extras.getString("policy");

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        ButtonViewCustom btNext = (ButtonViewCustom) findViewById(R.id.bt_send);
        btNext.setOnClickListener(this);


        ivPhoto = (ImageViewCustom) findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(this);

        llPhoto = (FlexboxLayout) findViewById(R.id.ll_photo);
        llPhoto.setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);

        tvMessageDelete = (TextView) findViewById(R.id.tv_message_delete);
        tvMessageDelete.setVisibility(View.INVISIBLE);

        if (ContextCompat.checkSelfPermission(UploadPictures.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(UploadPictures.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(UploadPictures.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UploadPictures.this,
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

        tvNumber.setText(numberString);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_warning:
                tvMessageErrorDialog.setText(Html.fromHtml(getResources().getString(R.string.message_step4)));
                tvErrorOk.setText(getResources().getString(R.string.bt_Ok_2));
                dialogError.show();
                break;
            case R.id.tv_speak_liberty:
                uploadPicturesController.openSupport(UploadPictures.this);
                break;
            case R.id.bt_send:
                if (uploadPicturesController.getArrayOutPutfileUri().size() <= 0 ) {
                    tvMessageDialog.setText(getString(R.string.none_picture_error));
                    dialogMessage.show();
                    return;
                }
                showLoading(true);
                tvMessageError.setVisibility(View.VISIBLE);
                tvMessageError.setText(getResources().getString(R.string.message_upload_photo));

                uploadPicturesController.createUploadFiles(UploadPictures.this);
                break;
            case R.id.iv_photo:

                skipOnActivity = false;

                if (uploadPicturesController.getArrayOutPutfileUri().size() < 8) {

                    dialogImageType.show();

                } else {
                    tvMessageDialog.setText(getString(R.string.photo_error));
                    dialogMessage.show();

                }

                break;
        }
    }

    /**
     * Config Dialogs
     */
    private void configDialog() {

        dialogImageType = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogImageType.setCancelable(false);

        dialogImageType.setContentView(R.layout.dialog_image);

        TextView tvNew = (TextView) dialogImageType.findViewById(R.id.tv_new);
        tvNew.setPaintFlags(tvNew.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImageType.dismiss();
                getPhoto();

            }
        });

        TextView tvOld = (TextView) dialogImageType.findViewById(R.id.tv_old);
        tvOld.setPaintFlags(tvOld.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        tvOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImageType.dismiss();
                uploadPicturesController.openDocuments(UploadPictures.this, numberPol);
            }
        });

        dialogSendVehicleAccident = new Dialog(this, R.style.AppThemeDialog);
        dialogSendVehicleAccident.setCancelable(false);
        dialogSendVehicleAccident.setContentView(R.layout.dialog_upload_success);


        tvNumber = (TextView) dialogSendVehicleAccident.findViewById(R.id.tv_number);

        ImageView ivOk = (ImageView) dialogSendVehicleAccident.findViewById(R.id.iv_ok);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSendVehicleAccident.dismiss();
                VehicleAccidentModel.exit = true;
                finish();
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
                if (uploadPicturesController.getTypeConnection() == 1) {
//                    uploadPicturesController.sendVehicleAccident(UploadPictures.this);
                } else {
                    uploadPicturesController.createUploadFiles(UploadPictures.this);
                }

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
                dialogDelete.dismiss();

                llPhoto.removeView(viewDelete);
                uploadPicturesController.removePhoto(viewDelete.getTag().toString());

                if (uploadPicturesController.getArrayOutPutfileUri().size() < 1) {
                    tvPhotoSize.setVisibility(View.GONE);
                    tvMessageDelete.setVisibility(View.INVISIBLE);
                } else {
                    tvPhotoSize.setVisibility(View.VISIBLE);
                    tvPhotoSize.setText(Html.fromHtml(uploadPicturesController.getTextSizePhots(UploadPictures.this)));
                }

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
                //Log.i(Config.TAG, "OK Camera");

                if (data != null) {
                    Uri uriImage = data.getData();

                    sizePhoto();

                    if (uriImage == null) {

                        try {

                            uriImage = uploadPicturesController.getUri();
                            uploadPicturesController.getArrayOutPutfileUri().add(uriImage.toString());

                            String path = uploadPicturesController.getRealPathFromURI(uriImage, UploadPictures.this);

                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);
                            Bitmap bitmap = uploadPicturesController.imageOreintationValidator(ThumbImage, path);

                            addPhoto(uriImage, bitmap);

                            configLengthPhotos();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            uploadPicturesController.setUri(uriImage);
                            uploadPicturesController.getArrayOutPutfileUri().add(uriImage.toString());

                            String path = uploadPicturesController.getRealPathFromURI(uriImage, UploadPictures.this);

                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);

                            Bitmap bitmap = uploadPicturesController.imageOreintationValidator(ThumbImage, path);

                            addPhoto(uriImage, bitmap);

                            configLengthPhotos();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {

                    Uri uriImage = null;

                    sizePhoto();

                    if (uriImage == null) {

                        try {


                            uriImage = uploadPicturesController.getUri();
                            uploadPicturesController.getArrayOutPutfileUri().add(uriImage.toString());

                            String path = uploadPicturesController.getRealPathFromURI(uriImage, UploadPictures.this);

                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);
                            Bitmap bitmap = uploadPicturesController.imageOreintationValidator(ThumbImage, path);


                            addPhoto(uriImage, bitmap);

                            configLengthPhotos();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }else if(resultCode == 98) {
                sizePhoto();


                String value = (String) data.getExtras().getString("ImageUri");

                configLengthPhotos();

                Uri uriImage = Uri.parse(value);

                uploadPicturesController
                        .getArrayOutPutfileUri().add(value);

                // Bitmap bitmap = Bitmap.createScaledBitmap(uploadPicturesController.getDim(UploadPictures.this).getImageBitmap(value), x, y, false);

                 Bitmap bitmap = uploadPicturesController.getDim(UploadPictures.this).getImageBitmap(value);



                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bitmap, x, y);

                addPhoto(uriImage, ThumbImage);

                configLengthPhotos();

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
                } else {
                    llLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putStringArrayList("ListPhotos", uploadPicturesController.getArrayOutPutfileUri());
        outState.putParcelable("uri", uploadPicturesController.getUri());

        super.onSaveInstanceState(outState);


    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (!skipOnActivity) {

            skipOnActivity = true;

            ArrayList<String> listPhotos = savedInstanceState.getStringArrayList("ListPhotos");


            if (listPhotos != null) {
                uploadPicturesController.setArraryOutPutfileUri(listPhotos);
            } else {
                listPhotos = new ArrayList<>();
            }

            Uri uri = savedInstanceState.getParcelable("uri");


            if (uri != null) {

                llPhoto.removeAllViews();
                uploadPicturesController.setUri(uri);

                sizePhoto();

                if (listPhotos.size() == 0) {
                    uploadPicturesController.addOutPutfileUri();
                } else {
                    if (!listPhotos.get(listPhotos.size() - 1).equals(uri.toString())) {
                        uploadPicturesController.addOutPutfileUri();
                    }
                }

                configLengthPhotos();


                listPhotos = uploadPicturesController.getArrayOutPutfileUri();

                for (int ind = 0; ind < listPhotos.size(); ind++) {
                    Uri uriImage = Uri.parse(listPhotos.get(ind));

                    //Log.i(Config.TAG, "URI: " + uriImage.toString());
                    String path = uploadPicturesController.getRealPathFromURI(uriImage, UploadPictures.this);


                    Bitmap bitmap = null;

                    try{
                        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);
                        bitmap = uploadPicturesController.imageOreintationValidator(ThumbImage, path);
                    }catch(Exception ex){
                        bitmap = uploadPicturesController.getDim(UploadPictures.this).getImageBitmap(listPhotos.get(ind));

                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, x , y);
                    }

                    addPhoto(uriImage, bitmap);

                }

            }
        }

    }



    @Override
    public void onBackPressed()
    {
        if(!connectionNow){
            super.onBackPressed();
        }
    }

    public interface TimeoutRecordAudio{
        public void onStop();
    }

    /**
     * set size thumb
     */
    private void sizePhoto(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        x = (width / 4) - (int)(((getResources().getDimension(R.dimen.margin_default_2) * 2) ) / 2.5);
        y = ((width / 4)) + (width/8);

        params = new LinearLayout.LayoutParams(x, y);

    }

    /**
     * Add photo in view
     * @param uriImage
     * @param bitmap
     */
    private void addPhoto(Uri uriImage, Bitmap bitmap){
        ImageViewCustom ivPhoto = new ImageViewCustom(this);
        ivPhoto.setTag(uriImage);
        ivPhoto.setPadding(20,20,20,20);
        ivPhoto.setLayoutParams(params);

        ivPhoto.setImageBitmap(bitmap);
        ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDelete = v;
                dialogDelete.show();
            }
        });

        tvMessageDelete.setVisibility(View.VISIBLE);

        llPhoto.addView(ivPhoto);
    }

    /**
     * Config text size photos
     */
    public void configLengthPhotos(){
        String size = uploadPicturesController.getTextSizePhots(UploadPictures.this);
        tvPhotoSize.setText(Html.fromHtml(size));
        tvPhotoSize.setVisibility(View.VISIBLE);

    }



    public void getPhoto(){
        if (ContextCompat.checkSelfPermission(UploadPictures.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(UploadPictures.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(UploadPictures.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(UploadPictures.this, getString(R.string.error_camera), Toast.LENGTH_SHORT);
            toast.show();


        } else {
            uploadPicturesController.openMakePhotoIntent(getString(R.string.choose_photo), UploadPictures.this);
        }

    }


}
