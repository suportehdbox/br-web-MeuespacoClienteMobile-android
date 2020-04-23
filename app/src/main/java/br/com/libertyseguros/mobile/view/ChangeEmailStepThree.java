package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ChangeEmailOffController;
import br.com.libertyseguros.mobile.model.ChangeEmailOffModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;

public class ChangeEmailStepThree extends BaseActionBar implements View.OnClickListener{

    private ChangeEmailOffController changeEmailController;

    private EditTextCustom etEmail;

    private EditTextCustom etConfirmEmail;

    private TextView tvMessageDialog;

    private ButtonViewCustom btNext;

    private boolean value;

    private LinearLayout llLoading;

    private ScrollView svContent;

    private Dialog dialogChangeEmail;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private LinearLayout llEditText;

    private int index;

    private ImageView ivSteps;

    public void onResume(){
        super.onResume();

        if(ChangeEmailOffModel.isExit()){
            finish();
        }
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_change_email_3);

        setTitle(getString(R.string.title_action_bar_4));

        mFirebaseAnalytics.setCurrentScreen(this, "Troca Email", null);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            index = Integer.parseInt(extras.getString("index"));
        }

        changeEmailController = new ChangeEmailOffController(this, new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showLoading(false);
                                if (changeEmailController.getTypeError() == 1) {
                                    dialogMessageTwoButton.show();
                                } else {

                                    if (changeEmailController.getMessage() == null) {
                                        dialogMessageTwoButton.show();
                                    } else {
                                        tvMessageDialog.setText(changeEmailController.getMessage().getMessage());
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
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showLoading(false);
                                dialogChangeEmail.show();
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });
                }catch(Exception ex){
                    ex.printStackTrace();
                }

            }
        });


        btNext = (ButtonViewCustom) findViewById(R.id.bt_next);
        btNext.setOnClickListener(this);


        llEditText = (LinearLayout) findViewById(R.id.ll_edittext);

        etEmail = new EditTextCustom(this);
        etConfirmEmail = new EditTextCustom(this);

        llEditText.addView(etEmail.config("", getString(R.string.hint_email), "", 2));
        etEmail.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        llEditText.addView(etConfirmEmail.config("", getString(R.string.hint_confirm_email), "", 2));
        etConfirmEmail.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        svContent = (ScrollView) findViewById(R.id.sv_content);

        configDialog();

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_next:

                String msg = changeEmailController.validFieldStepThree(ChangeEmailStepThree.this, etEmail.getText(), etConfirmEmail.getText());

                if(msg.equals("")){
                    showLoading(true);
                    changeEmailController.forgotEmailSendAnswers(ChangeEmailStepThree.this, etEmail.getText());
                } else {
                    if(msg.equals(getString(R.string.message_error_email_confirm))){
                        etConfirmEmail.showMessageError(msg);
                    } else {
                        etEmail.showMessageError(msg);
                    }
                }
                break;
        }
    }


    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogChangeEmail = new Dialog(this,R.style.AppThemeDialog);
        dialogChangeEmail.setCancelable(false);
        dialogChangeEmail.setContentView(R.layout.dialog_change_email);

        ImageView ivOk = (ImageView) dialogChangeEmail.findViewById(R.id.iv_ok);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChangeEmail.dismiss();
                ChangeEmailOffController.setExit(true);
                finish();
            }
        });


        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
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
                ChangeEmailOffController.setExit(true);
                finish();
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);

            }
        });

    }

    /*Show progress loading
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
}
