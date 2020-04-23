package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ChangeEmailController;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeEmailLoginOn extends BaseActionBar implements View.OnClickListener{

    private ChangeEmailController changeEmailController;

    private EditTextCustom etEmail;

    private EditTextCustom etConfirmEmail;

    private EditTextCustom etCPF;

    private ButtonViewCustom btSend;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private LinearLayout llContentField;

    private Dialog dialogSendPassword;

    private boolean value;

    private TextView tvNameHeader;

    private TextView tvEmailHeader;


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_change_email_login_on);

        setTitle(getString(R.string.title_action_bar_5));

        mFirebaseAnalytics.setCurrentScreen(this, "Troca Email", null);

        llContentField = findViewById(R.id.ll_content_field);

        changeEmailController = new ChangeEmailController(this, new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                showLoading(false);

                                if (changeEmailController.getTypeError() == 1) {
                                    dialogMessageTwoButton.show();
                                } else {
                                    tvMessageDialog.setText(changeEmailController.getMessage().getMessage());
                                    dialogMessage.show();
                                }
                            } catch(Exception ex){
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

                                dialogSendPassword.show();
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

        tvEmailHeader = (TextView) findViewById(R.id.tv_email);
        tvEmailHeader.setText(changeEmailController.getInfoUser().getEmail());

        tvNameHeader = (TextView) findViewById(R.id.tv_name);
        tvNameHeader.setText(changeEmailController.getInfoUser().getUserName());

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(changeEmailController.getInfoUser().getUserName().substring(0,1), getResources().getColor(R.color.background_status_bar));

        ImageView ivLetter = (ImageView) findViewById(R.id.iv_letter);
        ivLetter.setImageDrawable(drawable1);

        CircleImageView ci =(CircleImageView) findViewById(R.id.profile_image);

        if(changeEmailController.getImageUser(this,ci)){
            ci.setVisibility(View.VISIBLE);
            ivLetter.setVisibility(View.GONE);
        } else {
            ci.setVisibility(View.GONE);
            ivLetter.setVisibility(View.VISIBLE);
        }

        etEmail = new EditTextCustom(this);
        etConfirmEmail = new EditTextCustom(this);
        etCPF = new EditTextCustom(this);

        llContentField.addView(etEmail.config("", getString(R.string.hint_change_email), "", 1));
        etEmail.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        llContentField.addView(etConfirmEmail.config("", getString(R.string.hint_change_email_new), "", 1));
        etConfirmEmail.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        btSend = (ButtonViewCustom) findViewById(R.id.bt_send);
        btSend.setOnClickListener(this);

        configDialog();

    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_send:
                sendEmail();
                break;

        }
    }

    private void configDialog(){
        dialogSendPassword = new Dialog(this, R.style.AppThemeDialog);
        dialogSendPassword.setCancelable(false);

        dialogSendPassword.setContentView(R.layout.dialog_change_email);

        ImageView ivOk = (ImageView) dialogSendPassword.findViewById(R.id.iv_ok);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialogSendPassword.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
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
                if(getString(R.string.message_forgot_password).equals(tvMessageDialog.getText().toString())){
                    finish();
                }
            }
        });


        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

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
                sendEmail();
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

    private void sendEmail(){
        String msg[] = changeEmailController.validFieldLoginOn(ChangeEmailLoginOn.this, etEmail.getText().toString(), etConfirmEmail.getText().toString());

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
                    case 2:
                        etConfirmEmail.showMessageError(msg[2]);
                        break;

                }
            }
        }

        if(!error){
            showLoading(true);
            changeEmailController.forgotEmailLoginOn(ChangeEmailLoginOn.this, etEmail.getText(),  true);
        }

    }
}
