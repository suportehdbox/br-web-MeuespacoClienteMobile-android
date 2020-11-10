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
import android.os.SystemClock;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;


import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.VehicleAccidentController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class VehicleAccidentStep4 extends BaseActionBar implements View.OnClickListener {

    private LinearLayout llEdittext;

    private EditText etDescription;

    private ScrollView svContent;

    private LinearLayout llLoading;

    private AdjustableImageView ivAudio;

    private AdjustableImageView ivTrash;

    private VehicleAccidentController vehicleAccidentController;

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

    private TextViewCustom tvSpeakLiberty;

    private int typeDelete;

    private View viewDelete;

    private boolean value;

    private TextView tvPhotoSize;

    private TextView tvNumber;

    private TextView tvMessageError;

    private TextView tvMessageErrorDialog;

    private static boolean skipOnActivity;

    private AdjustableImageView ivWarning;

    private TextView tvErrorOk;

    private boolean endRecording;

    private Chronometer chronometer;

    private boolean connectionNow;

    private TimeoutRecordAudio timeoutRecordAudio;

    private int x;

    private int y;

    private TextView tvMessageDelete;

    private LinearLayout.LayoutParams param;

    public void onDestroy() {
        super.onDestroy();

        try{
            if(vehicleAccidentController != null) {
                //Log.i(Config.TAG, "onDestroy: " + "cancel");

                cancel();
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public void onResume(){
        super.onResume();

        try{
            if(vehicleAccidentController != null) {
                //Log.i(Config.TAG, "onResume: " + "cancel");

                cancel();

            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_9));

        //VehicleAccidentModel.exit = true;

        timeoutRecordAudio = new TimeoutRecordAudio() {
            @Override
            public void onStop() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                vehicleAccidentController.stopRecording(VehicleAccidentStep4.this);


                                ivAudio.getAnimation().cancel();
                                ivAudio.setAnimation(null);

                                ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                                ivTrash.setVisibility(View.VISIBLE);
                                chronometer.stop();
                                chronometer.setVisibility(View.GONE);
                                endRecording = false;

                                tvMessageDialog.setText(getResources().getString(R.string.audio_time));

                                dialogMessage.show();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                    });
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        };

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.vehicle_accident_step4);

        skipOnActivity = false;

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        mFirebaseAnalytics.setCurrentScreen(this, "Enviar Sinistro passo 4", null);


        vehicleAccidentController = new VehicleAccidentController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    connectionNow = false;
                    connection = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(vehicleAccidentController.getTypeConnection() == 2){
                                showLoading(false);

                                tvNumber.setText(vehicleAccidentController.getNwvhbeans().getNumeroAvisoSinistro());
                                connectionNow = false;
                                connection = false;
                                dialogSendVehicleAccident.show();

                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Retorno");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Comunicar Acidente");
                                bundle.putString(FirebaseAnalytics.Param.CONTENT, "Comunicar Acidente");

                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                            } else {
                                try {
                                    showLoading(false);
                                    if (vehicleAccidentController.getTypeError() == 1) {
                                        dialogMessageTwoButton.show();
                                    } else {
                                        tvMessageDialog.setText(vehicleAccidentController.getMessage().getMessage());
                                        dialogMessage.show();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
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
                            if (vehicleAccidentController.getTypeConnection() == 1) {
                                showLoading(false);

                                tvNumber.setText(vehicleAccidentController.getNwvhbeans().getNumeroAvisoSinistro());

                                if (vehicleAccidentController.getArrayOutPutfileUri().size() < 1 && vehicleAccidentController.getAudioFile() == null) {
                                    connectionNow = false;
                                    connection = false;
                                    dialogSendVehicleAccident.show();



                                    Bundle bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Retorno");
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Comunicar Acidente");
                                    bundle.putString(FirebaseAnalytics.Param.CONTENT, "Comunicar Acidente");

                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                                } else {
                                    showLoading(true);
                                    tvMessageError.setVisibility(View.VISIBLE);
                                    tvMessageError.setText(getResources().getString(R.string.message_upload_photo));

                                    vehicleAccidentController.createUploadFiles(VehicleAccidentStep4.this);
                                }
                            } else {

                                if (vehicleAccidentController.getIndexRemove() == -1) {
                                    connectionNow = false;
                                    connection = false;
                                    dialogSendVehicleAccident.show();

                                    Bundle bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Retorno");
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Comunicar Acidente");
                                    bundle.putString(FirebaseAnalytics.Param.CONTENT, "Comunicar Acidente");

                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                                } else {
                                    tvMessageError.setText(getResources().getString(R.string.message_upload_photo));
                                    vehicleAccidentController.createUploadFiles(VehicleAccidentStep4.this);
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

        svContent = (ScrollView) findViewById(R.id.sv_content);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        ButtonViewCustom btNext = (ButtonViewCustom) findViewById(R.id.bt_next_step_4);
        btNext.setOnClickListener(this);

        llEdittext = (LinearLayout) findViewById(R.id.ll_edittext);

        etDescription = (EditText) findViewById(R.id.et_text);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(1500);
        etDescription.setFilters(FilterArray);

        ivTrash = (AdjustableImageView) findViewById(R.id.iv_trash);
        ivTrash.setOnClickListener(this);

        ivPhoto = (ImageViewCustom) findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(this);

        tvMessageDelete = (TextView) findViewById(R.id.tv_message_delete);
        tvMessageDelete.setVisibility(View.INVISIBLE);

        ivAudio = (AdjustableImageView) findViewById(R.id.iv_audio);

        ivAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        if (toast != null) {
                            toast.cancel();
                        }

                        toast = Toast.makeText(VehicleAccidentStep4.this, getString(R.string.error_audio), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (!vehicleAccidentController.isAudioRecord()) {
                            if (!endRecording) {
                                vehicleAccidentController.startRecording(timeoutRecordAudio);

                                chronometer.setVisibility(View.VISIBLE);
                                startChonometer();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.recording));

                                        Animation myAnim = AnimationUtils.loadAnimation(VehicleAccidentStep4.this, R.anim.bounce);
                                        ivAudio.startAnimation(myAnim);

                                    }
                                });

                                endRecording = true;
                            } else {

                                vehicleAccidentController.stopRecording(VehicleAccidentStep4.this);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivAudio.getAnimation().cancel();
                                        ivAudio.setAnimation(null);
                                        ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                                        ivTrash.setVisibility(View.VISIBLE);
                                        chronometer.stop();
                                        chronometer.setVisibility(View.GONE);
                                    }
                                });

                                endRecording = false;

                            }
                        } else {

                            if (vehicleAccidentController.isPlay()) {

                                vehicleAccidentController.pausePlaying();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                                    }
                                });
                            } else {
                                vehicleAccidentController.startPlaying(ivAudio, VehicleAccidentStep4.this);


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_pause));
                                    }
                                });
                            }

                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
       /* ivAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                if (ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    if (toast != null) {
                        toast.cancel();
                    }

                    toast = Toast.makeText(VehicleAccidentStep4.this, getString(R.string.error_audio), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    try {
                        if (action == MotionEvent.ACTION_DOWN) {
                            if (vehicleAccidentController.isAudioRecord()) {

                                if (vehicleAccidentController.isPlay()) {
                                    cancelTimer();

                                    vehicleAccidentController.pausePlaying();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                                        }
                                    });
                                } else {
                                    vehicleAccidentController.startPlaying(ivAudio, VehicleAccidentStep4.this);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_stop));
                                        }
                                    });
                                }
                            } else {

                                vehicleAccidentController.startRecording();

                                Animation myAnim = AnimationUtils.loadAnimation(VehicleAccidentStep4.this, R.anim.bounce);
                                ivAudio.startAnimation(myAnim);

                                setTimeAudioRecord();
                            }


                        } else if (action == MotionEvent.ACTION_UP ||
                                action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_CANCEL) {

                            if (!vehicleAccidentController.isAudioRecord()) {
                                Animation myAnim = AnimationUtils.loadAnimation(VehicleAccidentStep4.this, R.anim.bounce_remove);
                                ivAudio.startAnimation(myAnim);

                                vehicleAccidentController.stopRecording(VehicleAccidentStep4.this);
                                timer = new Timer();

                                timerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                                                ivTrash.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                };
                                timer.schedule(timerTask, 600);


                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
                return true;
            }
        });*/

        llPhoto = (FlexboxLayout) findViewById(R.id.ll_photo);
        llPhoto.setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);



        if (ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(VehicleAccidentStep4.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA},
                    1);

        }

        tvSpeakLiberty = (TextViewCustom) findViewById(R.id.tv_speak_liberty);
        tvSpeakLiberty.setOnClickListener(this);
        tvSpeakLiberty.setPaintFlags(tvSpeakLiberty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvPhotoSize = (TextView) findViewById(R.id.tv_photo_size);

        tvMessageError = (TextView) findViewById(R.id.tv_message_error);
        tvMessageError.setText(getResources().getString(R.string.message_upload_photo));

        ivWarning = (AdjustableImageView) findViewById(R.id.iv_warning);
        ivWarning.setOnClickListener(this);

        configDialog();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_warning:
                tvMessageErrorDialog.setText(Html.fromHtml(getResources().getString(R.string.message_step4)));
                tvErrorOk.setText(getResources().getString(R.string.bt_Ok_2));
                dialogError.show();
                break;
            case R.id.tv_speak_liberty:
                vehicleAccidentController.openSupport(VehicleAccidentStep4.this);
                break;
            case R.id.bt_next_step_4:

                if (etDescription.getText().toString().equals("")) {

                    if (vehicleAccidentController.getAudioFile() == null) {
                        tvMessageErrorDialog.setText(getResources().getString(R.string.message_empty_description_accident));
                        tvErrorOk.setText(getResources().getString(R.string.bt_Ok));
                        dialogError.show();
                    } else {
                        showLoading(true);
                        VehicleAccidentModel.vasb.setDescription(etDescription.getText().toString());
                        tvMessageError.setVisibility(View.VISIBLE);
                        tvMessageError.setText(getResources().getString(R.string.message_send_va));
                        connectionNow = true;
                        connection = true;
                        cancel();
                        vehicleAccidentController.sendVehicleAccident(VehicleAccidentStep4.this);
                    }
                } else {
                    showLoading(true);
                    VehicleAccidentModel.vasb.setDescription(etDescription.getText().toString());
                    tvMessageError.setVisibility(View.VISIBLE);
                    tvMessageError.setText(getResources().getString(R.string.message_send_va));
                    connectionNow = true;
                    connection = true;
                    cancel();
                    vehicleAccidentController.sendVehicleAccident(VehicleAccidentStep4.this);

                }

                break;
            case R.id.iv_trash:
                typeDelete = 2;
                dialogDelete.show();
                break;
            case R.id.iv_photo:
                skipOnActivity = false;

                if (vehicleAccidentController.getArrayOutPutfileUri().size() < 8) {
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
        dialogImageType.setCancelable(true);

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
                vehicleAccidentController.openDocuments(VehicleAccidentStep4.this);
            }
        });

        dialogSendVehicleAccident = new Dialog(this, R.style.AppThemeDialog);
        dialogSendVehicleAccident.setCancelable(false);
        dialogSendVehicleAccident.setContentView(R.layout.dialog_vehicle_accident);

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
                if (vehicleAccidentController.getTypeConnection() == 1) {
                    vehicleAccidentController.sendVehicleAccident(VehicleAccidentStep4.this);
                } else {
                    vehicleAccidentController.createUploadFiles(VehicleAccidentStep4.this);
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
                if (typeDelete == 2) {
                    deleteAudio();
                } else {
                    llPhoto.removeView(viewDelete);
                    vehicleAccidentController.removePhoto(viewDelete.getTag().toString());

                    if (vehicleAccidentController.getArrayOutPutfileUri().size() < 1) {
                        tvPhotoSize.setVisibility(View.GONE);
                        tvMessageDelete.setVisibility(View.INVISIBLE);

                    } else {
                        tvPhotoSize.setVisibility(View.VISIBLE);
                        tvPhotoSize.setText(Html.fromHtml(vehicleAccidentController.getTextSizePhots(VehicleAccidentStep4.this)));
                    }
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

                            uriImage = vehicleAccidentController.getUri();
                            vehicleAccidentController.getArrayOutPutfileUri().add(uriImage.toString());

                            String path = vehicleAccidentController.getRealPathFromURI(uriImage, VehicleAccidentStep4.this);

                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);
                            Bitmap bitmap = vehicleAccidentController.imageOreintationValidator(ThumbImage, path);

                            addPhoto(uriImage, bitmap);

                            configLengthPhotos();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            vehicleAccidentController.setUri(uriImage);
                            vehicleAccidentController.getArrayOutPutfileUri().add(uriImage.toString());

                            String path = vehicleAccidentController.getRealPathFromURI(uriImage, VehicleAccidentStep4.this);

                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);

                            Bitmap bitmap = vehicleAccidentController.imageOreintationValidator(ThumbImage, path);

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


                            uriImage = vehicleAccidentController.getUri();
                            vehicleAccidentController.getArrayOutPutfileUri().add(uriImage.toString());

                            String path = vehicleAccidentController.getRealPathFromURI(uriImage, VehicleAccidentStep4.this);

                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);
                            Bitmap bitmap = vehicleAccidentController.imageOreintationValidator(ThumbImage, path);


                           addPhoto(uriImage, bitmap);

                            configLengthPhotos();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            } else if(resultCode == 98) {
                sizePhoto();


                String value = (String) data.getExtras().getString("ImageUri");

                configLengthPhotos();

                Uri uriImage = Uri.parse(value);

                vehicleAccidentController.getArrayOutPutfileUri().add(value);

                Bitmap bitmap = vehicleAccidentController.getDim(VehicleAccidentStep4.this).getImageBitmap(value);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, x, y);
                addPhoto(uriImage, bitmap);

                configLengthPhotos();

            }

        }
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
                    svContent.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    svContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    /**
     * Delete Audio
     */
    private void deleteAudio() {

        if (vehicleAccidentController.isPlay()) {
            vehicleAccidentController.stopPlaying();
        }

        ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.audio));
        ivTrash.setVisibility(View.GONE);
        vehicleAccidentController.setPlay(false);
        vehicleAccidentController.setAudioRecord(false);
        vehicleAccidentController.deleteAudio();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putStringArrayList("ListPhotos", vehicleAccidentController.getArrayOutPutfileUri());
        outState.putParcelable("uri", vehicleAccidentController.getUri());

        super.onSaveInstanceState(outState);


    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (!skipOnActivity) {

            skipOnActivity = true;

            ArrayList<String> listPhotos = savedInstanceState.getStringArrayList("ListPhotos");


            if (listPhotos != null) {
                vehicleAccidentController.setArraryOutPutfileUri(listPhotos);
            } else {
                listPhotos = new ArrayList<>();
            }

            Uri uri = savedInstanceState.getParcelable("uri");


            if (uri != null) {

                llPhoto.removeAllViews();
                vehicleAccidentController.setUri(uri);

                sizePhoto();

                if (listPhotos.size() == 0) {
                    vehicleAccidentController.addOutPutfileUri();
                } else {
                    if (!listPhotos.get(listPhotos.size() - 1).equals(uri.toString())) {
                        vehicleAccidentController.addOutPutfileUri();
                    }
                }
                String size = vehicleAccidentController.getTextSizePhots(VehicleAccidentStep4.this);
                tvPhotoSize.setText(Html.fromHtml(size));
                tvPhotoSize.setVisibility(View.VISIBLE);


                listPhotos = vehicleAccidentController.getArrayOutPutfileUri();

                for (int ind = 0; ind < listPhotos.size(); ind++) {
                    Uri uriImage = Uri.parse(listPhotos.get(ind));

                    //Log.i(Config.TAG, "URI: " + uriImage.toString());
                    String path = vehicleAccidentController.getRealPathFromURI(uriImage, VehicleAccidentStep4.this);

                    Bitmap bitmap = null;

                    try{
                        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), x, y);
                        bitmap = vehicleAccidentController.imageOreintationValidator(ThumbImage, path);
                    }catch(Exception ex){
                        bitmap = vehicleAccidentController.getDim(VehicleAccidentStep4.this).getImageBitmap(listPhotos.get(ind));
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, x, y);

                    }


                    addPhoto(uriImage, bitmap);

                }

            }
        }

    }

    private void startChonometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }



    public void cancel(){
        try {
            if (!vehicleAccidentController.isAudioRecord()) {
                if (endRecording) {
                    vehicleAccidentController.stopRecording(VehicleAccidentStep4.this);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                ivAudio.getAnimation().cancel();
                                ivAudio.setAnimation(null);
                                ivAudio.setAnimation(null);
                                ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                                ivTrash.setVisibility(View.VISIBLE);
                                chronometer.stop();
                                chronometer.setVisibility(View.GONE);
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }


                        }
                    });

                    endRecording = false;
                }
            } else {

                if (vehicleAccidentController.isPlay()) {

                    vehicleAccidentController.pausePlaying();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                ivAudio.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                            } catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });
                }

            }
        } catch (Exception ex){
            ex.printStackTrace();
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

         param = new LinearLayout.LayoutParams(x, y);

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

        ivPhoto.setImageBitmap(bitmap);
        ivPhoto.setLayoutParams(param);
        ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDelete = 1;
                viewDelete = v;
                dialogDelete.show();
            }
        });

        tvMessageDelete.setVisibility(View.VISIBLE);

        llPhoto.addView(ivPhoto);
    }

    public void getPhoto(){

        if (ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(VehicleAccidentStep4.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(VehicleAccidentStep4.this, getString(R.string.error_camera), Toast.LENGTH_SHORT);
            toast.show();


        } else {
            vehicleAccidentController.openMakePhotoIntent(getString(R.string.choose_photo), VehicleAccidentStep4.this);
        }


    }

    /**
     * Config text size photos
     */
    public void configLengthPhotos(){
        String size = vehicleAccidentController.getTextSizePhots(VehicleAccidentStep4.this);
        tvPhotoSize.setText(Html.fromHtml(size));
        tvPhotoSize.setVisibility(View.VISIBLE);
    }

}



