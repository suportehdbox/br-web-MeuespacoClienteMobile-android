package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ChangePasswordController;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;

public class ChangePassword extends BaseActionBar implements View.OnClickListener{

    private ChangePasswordController changePasswordController;

    private EditTextCustom etCPF;

    private EditTextCustom etEmail;

    private TextViewCustom tvPrivacy;

    private TextViewCustom tvRegister;

    private ButtonViewCustom btSkip;

    private ButtonViewCustom bSend;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private LinearLayout llContentField;

    private boolean value;


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_change_password);

        setTitle(getString(R.string.title_action_bar_0));

        mFirebaseAnalytics.setCurrentScreen(this, "Esqueceu Senha", null);

        llContentField = findViewById(R.id.ll_content_field);

        changePasswordController = new ChangePasswordController(this , new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try {
                            showLoading(false);

                            if (changePasswordController.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {
                                tvMessageDialog.setText(changePasswordController.getMessage().getMessage());
                                dialogMessage.show();
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

                                tvMessageDialog.setText(getString(R.string.message_forgot_password));
                                dialogMessage.show();
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

        etCPF = new EditTextCustom(this);
        etEmail = new EditTextCustom(this);

        llContentField.addView(etCPF.config("", getString(R.string.hint_cpf), "", 2));
        etCPF.getEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(14) });

        llContentField.addView(etEmail.config("", getString(R.string.hint_email), "", 1));

        tvPrivacy = (TextViewCustom) findViewById(R.id.tv_privacy);
        tvPrivacy.setOnClickListener(this);
        tvPrivacy.setPaintFlags(tvPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvRegister = (TextViewCustom) findViewById(R.id.tv_not_user);
        tvRegister.setPaintFlags(tvPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvRegister.setOnClickListener(this);

        btSkip = (ButtonViewCustom) findViewById(R.id.bt_skip);
        btSkip.setOnClickListener(this);

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        bSend = (ButtonViewCustom) findViewById(R.id.bt_send);
        bSend.setOnClickListener(this);

        configDialog();
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_skip:
                changePasswordController.openMain(ChangePassword.this);
                break;
            case R.id.tv_forgot_password:

                break;
            case R.id.bt_send:
                sendPassword();
                break;
            case R.id.tv_not_user:
                changePasswordController.openRegister(ChangePassword.this);
                break;
            case R.id.tv_privacy:
                changePasswordController.openPrivacy(this);
                break;
        }
    }

    private void configDialog(){
        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();

                if(getString(R.string.message_forgot_password).equals(tvMessageDialog.getText().toString())){
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
                sendPassword();

            }
        });

    }

    /* Show progress loading
    *
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

    private void sendPassword(){
        String msg[] = changePasswordController.validField(ChangePassword.this, etEmail.getText().toString(), etCPF.getText().toString());

        boolean error = false;

        for(int ind = 0; ind < msg.length; ind++){
            if(msg[ind].length() != 0){
                error = true;

                switch (ind){
                    case 0:
                        etCPF.showMessageError(msg[0]);
                        break;
                    case 1:
                        etEmail.showMessageError(msg[1]);
                        break;
                }
            }
        }





        if(!error){
            showLoading(true);
            changePasswordController.forgotPassword(ChangePassword.this, etEmail.getText().toString(), etCPF.getText().toString());
        }

    }
}
