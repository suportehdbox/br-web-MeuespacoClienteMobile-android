package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ChangePasswordController;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePasswordLoginOn extends BaseActionBar implements View.OnClickListener{

    private ChangePasswordController changePasswordController;

    private EditTextCustom etPassword;

    private EditTextCustom etNewPassword;

    private EditTextCustom etConfirmPassword;


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

    private TextView etName;

    private TextView etEmail;



    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_change_password_login_on);

        setTitle(getString(R.string.title_action_bar_5));

        final Context _context = getApplicationContext();

        mFirebaseAnalytics.setCurrentScreen(this, "Troca Senha", null);
        llContentField = findViewById(R.id.ll_content_field);

        changePasswordController = new ChangePasswordController(this, new OnConnectionResult() {
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
                            } catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess() {
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try {
                            changePasswordController.changePasswordExpired(_context);
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

        etName = (TextView) findViewById(R.id.tv_name);
        etName.setText(changePasswordController.getInfoUser().getUserName());

        etEmail = (TextView) findViewById(R.id.tv_email);
        etEmail.setText(changePasswordController.getInfoUser().getEmail());

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(changePasswordController.getInfoUser().getUserName().substring(0,1), getResources().getColor(R.color.background_status_bar));

        ImageView ivLetter = (ImageView) findViewById(R.id.iv_letter);
        ivLetter.setImageDrawable(drawable1);

        CircleImageView ci =(CircleImageView) findViewById(R.id.profile_image);

        if(changePasswordController.getImageUser(this,ci)){
            ci.setVisibility(View.VISIBLE);
            ivLetter.setVisibility(View.GONE);
        } else {
            ci.setVisibility(View.GONE);
            ivLetter.setVisibility(View.VISIBLE);
        }

        etPassword = new EditTextCustom(this);
        etNewPassword = new EditTextCustom(this);
        etCPF = new EditTextCustom(this);
        etConfirmPassword = new EditTextCustom(this);

        LinearLayout llEmpty = new LinearLayout(this);
        llEmpty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60));

        //llContentField.addView(etCPF.config("49795363794", getString(R.string.hint_cpf), "", 2));
        llContentField.addView(etPassword.config("", getString(R.string.hint_password_old), "", 3));
        llContentField.addView(llEmpty);
        llContentField.addView(etNewPassword.config("", getString(R.string.hint_password_new), "", 3));
        llContentField.addView(etConfirmPassword.config("", getString(R.string.hint_confirm_password_new), "", 3));

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        btSend = (ButtonViewCustom) findViewById(R.id.bt_send);
        btSend.setOnClickListener(this);

        configDialog();

    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_skip:
                changePasswordController.openMain(ChangePasswordLoginOn.this);
                break;
            case R.id.tv_forgot_password:

                break;
            case R.id.bt_send:
                sendPassword();
                break;

        }
    }

    private void configDialog(){
        dialogSendPassword = new Dialog(this, R.style.AppThemeDialog);
        dialogSendPassword.setCancelable(false);

        dialogSendPassword.setContentView(R.layout.dialog_change_password);

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
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();

                if( getString(R.string.message_forgot_password).equals(tvMessageDialog.getText().toString())){
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
        String msg[] = changePasswordController.validFieldLoginOn(ChangePasswordLoginOn.this, etPassword.getText().toString(), etNewPassword.getText().toString(), etConfirmPassword.getText(), etCPF.getText());

        boolean error = false;

        for(int ind = 0; ind < msg.length; ind++){
            if(msg[ind].length() != 0){
                error = true;

                switch (ind){
                    case 0:
                        etCPF.showMessageError(msg[0]);
                        break;
                    case 1:
                        etPassword.showMessageError(msg[1]);
                        break;
                    case 2:
                        etNewPassword.showMessageError(msg[2]);
                        break;
                    case 3:
                        etConfirmPassword.showMessageError(msg[3]);
                        break;
                }
            }
        }


        if(!error){
            showLoading(true);
            changePasswordController.forgotPasswordLoginOn(ChangePasswordLoginOn.this, etPassword.getText(), etNewPassword.getText(),  true);
        }

    }
}
