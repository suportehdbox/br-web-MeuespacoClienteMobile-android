package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
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
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class ChangeEmail extends BaseActionBar implements View.OnClickListener{

    private ChangeEmailOffController changeEmailController;

    private EditTextCustom etCpf;

    private TextView tvMessageDialog;

    private ButtonViewCustom btNext;

    private boolean value;

    private LinearLayout llLoading;

    private ScrollView svContent;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private LinearLayout llEditText;

    public void onResume(){
        super.onResume();

        if(ChangeEmailOffModel.isExit()){
            finish();
            ChangeEmailOffModel.setExit(false);
        }
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_change_email);

        setTitle(getString(R.string.title_action_bar_4));

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
                            } catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess() {
                try {
                    showLoading(false);
                    changeEmailController.openPartTwo(ChangeEmail.this, 0);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        llEditText = (LinearLayout) findViewById(R.id.ll_edittext);

        etCpf = new EditTextCustom(this);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(14);
        etCpf.getEditText().setFilters(FilterArray);


        //llEditText.addView(etCpf.config("77519859843", getString(R.string.hint_cpf_change_email), "", 2));

        llEditText.addView(etCpf.config("", getString(R.string.hint_cpf_change_email), "", 2));

        btNext = (ButtonViewCustom) findViewById(R.id.bt_next);
        btNext.setOnClickListener(this);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        svContent = (ScrollView) findViewById(R.id.sv_content);
        configDialog();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_next:
                String msg = changeEmailController.validFieldStepOne(ChangeEmail.this, etCpf.getText());

                if(!msg.equals("")){
                    etCpf.showMessageError(msg);
                } else {
                    showLoading(true);
                    changeEmailController.forgotEmailSendCPF(ChangeEmail.this, etCpf.getText());
                }


                break;
        }
    }


    /**
     * Config Dialogs
     */
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
