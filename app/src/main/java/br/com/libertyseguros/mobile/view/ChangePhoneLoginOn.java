package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ChangePhoneController;
import br.com.libertyseguros.mobile.model.ChangePhoneModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.PhoneMaskUtil;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePhoneLoginOn extends BaseActionBar implements View.OnClickListener{

    private ChangePhoneController changePhoneController;

    private EditTextCustom etPhone;

    private EditTextCustom etRamal;

    private EditTextCustom etMobilePhone;

    private ButtonViewCustom btSend;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private LinearLayout llContentField;

    private Dialog dialogPhoneChanged;

    private boolean value;

    private TextView tvNameHeader;

    private TextView tvEmailHeader;


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_change_phone_login_on);

        setTitle(getString(R.string.title_action_bar_5));

        mFirebaseAnalytics.setCurrentScreen(this, "Atualizar Telefone", null);

        llContentField = (LinearLayout) findViewById(R.id.ll_content_field);

        changePhoneController = new ChangePhoneController(this, new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                showLoading(false);

                                switch (changePhoneController.getTypeConnection()){
                                    case ChangePhoneModel.GET_PHONE:
                                        if (changePhoneController.getTypeError() == 1) {
                                            dialogMessageTwoButton.show();
                                        }
                                        break;
                                    case ChangePhoneModel.SEND_PHONE:
                                        if (changePhoneController.getTypeError() == 1) {
                                            dialogMessageTwoButton.show();
                                        } else {
                                            tvMessageDialog.setText(changePhoneController.getMessage().getMessage());
                                            dialogMessage.show();
                                        }
                                        break;

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

                            showLoading(false);

                            switch (changePhoneController.getTypeConnection()){
                                case ChangePhoneModel.GET_PHONE:
                                    etPhone.getEditText().setText(changePhoneController.getInfoPhoneBeans().getHomePhone());
                                    etMobilePhone.getEditText().setText(changePhoneController.getInfoPhoneBeans().getCelPhone());

                                    if(!changePhoneController.getInfoPhoneBeans().getBranchHomePhone().trim().equals("0")){
                                        etRamal.getEditText().setText(changePhoneController.getInfoPhoneBeans().getBranchHomePhone());
                                    }

                                    break;
                                case ChangePhoneModel.SEND_PHONE:
                                    try {
                                        dialogPhoneChanged.show();
                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                    break;

                            }

                        }
                    });
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        tvEmailHeader = (TextView) findViewById(R.id.tv_email);
        tvEmailHeader.setText(changePhoneController.getInfoUser().getEmail());

        tvNameHeader = (TextView) findViewById(R.id.tv_name);
        tvNameHeader.setText(changePhoneController.getInfoUser().getUserName());

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(changePhoneController.getInfoUser().getUserName().substring(0,1), getResources().getColor(R.color.background_status_bar));

        ImageView ivLetter = (ImageView) findViewById(R.id.iv_letter);
        ivLetter.setImageDrawable(drawable1);

        CircleImageView ci =(CircleImageView) findViewById(R.id.profile_image);

        if(changePhoneController.getImageUser(this,ci)){
            ci.setVisibility(View.VISIBLE);
            ivLetter.setVisibility(View.GONE);
        } else {
            ci.setVisibility(View.GONE);
            ivLetter.setVisibility(View.VISIBLE);
        }

        etPhone = new EditTextCustom(this);

        etRamal = new EditTextCustom(this);
        etRamal.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
        etMobilePhone = new EditTextCustom(this);


        llContentField.addView(etPhone.config("", getString(R.string.hint_change_phone), "", 2));
        etPhone.getEditText().addTextChangedListener(PhoneMaskUtil.insert(etPhone.getEditText()));

        llContentField.addView(etRamal.config("", getString(R.string.hint_change_ramal), "", 2));

        llContentField.addView(etMobilePhone.config("", getString(R.string.hint_change_mobile_phone), "", 2));
        etMobilePhone.getEditText().addTextChangedListener(PhoneMaskUtil.insert(etMobilePhone.getEditText()));

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        btSend = (ButtonViewCustom) findViewById(R.id.bt_send);
        btSend.setOnClickListener(this);

        configDialog();


        showLoading(true);
        changePhoneController.getPhoneInfo(this);

    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_send:
                sendPhone();
                break;

        }
    }

    private void configDialog(){
        dialogPhoneChanged = new Dialog(this, R.style.AppThemeDialog);
        dialogPhoneChanged.setCancelable(false);

        dialogPhoneChanged.setContentView(R.layout.dialog_change_phone);

        ImageView ivOk = (ImageView) dialogPhoneChanged.findViewById(R.id.iv_ok);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialogPhoneChanged.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
                if(changePhoneController.getTypeConnection() == ChangePhoneModel.SEND_PHONE){
                    sendPhone();
                } else {
                    changePhoneController.getPhoneInfo(ChangePhoneLoginOn.this);
                }

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

    private void sendPhone(){
        String msg[] = changePhoneController.validFields(this, etPhone.getText(), etMobilePhone.getText());

        boolean error = false;

        for(int ind = 0; ind < msg.length; ind++){
            if(msg[ind].length() != 0){
                error = true;

                switch (ind){
                    case 0:
                        etPhone.showMessageError(msg[0]);
                        break;
                    case 1:
                        etMobilePhone.showMessageError(msg[1]);
                        break;

                }
            }
        }

        if(!error){
            showLoading(true);
            changePhoneController.sendPhone(this, etPhone.getText().toString(), etRamal.getText().toString(), etMobilePhone.getText().toString());
        }

    }
}
