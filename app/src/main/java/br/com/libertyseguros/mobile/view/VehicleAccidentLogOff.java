package br.com.libertyseguros.mobile.view;


import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.VehicleAccidentController;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.PlateMaskUtil;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class VehicleAccidentLogOff extends BaseActionBar implements View.OnClickListener{

    private VehicleAccidentController vehicleAccidentController;

    private EditTextCustom etCpfCnpj;

    private EditTextCustom etPolicy;

    private LinearLayout llEditText;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private TextViewCustom tvSpeakLiberty;

    private TextViewCustom tvChangePassword;

    private TextViewCustom tvRegister;

    private ButtonViewCustom btLogin;

    private boolean value;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private ButtonViewCustom btStart;

    private int typeScreen;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        setContentView(R.layout.vehicle_accident_logoff);

        typeScreen = Integer.parseInt(getIntent().getExtras().getString("vehicleAccident"));

        if(typeScreen == 0){
            setTitle(getString(R.string.title_action_bar_9));
        } else {
            setTitle(getString(R.string.title_action_bar_14));

        }

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);

        mFirebaseAnalytics.setCurrentScreen(this, "Enviar Sinistro Deslogado", null);

        vehicleAccidentController = new VehicleAccidentController(new OnConnectionResult() {
            @Override
            public void onError() {

            }

            @Override
            public void onSucess() {

            }
        });

        btStart = (ButtonViewCustom) findViewById(R.id.bt_start);
        btStart.setOnClickListener(this);

        tvMessageDialog = (TextView) findViewById(R.id.tv_message);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        tvSpeakLiberty = (TextViewCustom)  findViewById(R.id.tv_speak_liberty);
        tvSpeakLiberty.setPaintFlags(tvSpeakLiberty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSpeakLiberty.setOnClickListener(this);

        tvChangePassword =  (TextViewCustom)  findViewById(R.id.tv_forgot_email);
        tvChangePassword.setPaintFlags(tvChangePassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvChangePassword.setOnClickListener(this);


        btLogin = (ButtonViewCustom) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

        tvRegister =  (TextViewCustom)  findViewById(R.id.tv_not_user);
        tvRegister.setPaintFlags(tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvRegister.setOnClickListener(this);

        etCpfCnpj = new EditTextCustom(this);
        etPolicy = new EditTextCustom(this);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(14);
        etCpfCnpj.getEditText().setFilters(FilterArray);


        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(15);
        etPolicy.getEditText().setFilters(FilterArray1);

        llEditText = (LinearLayout) findViewById(R.id.ll_edittext);


        if(typeScreen == 0){
            llEditText.addView(etPolicy.config("", getString(R.string.hint_policy_vehicle), "", 1));
            btStart.setText(getResources().getText(R.string.button_start_sinistro));
            tvTitle.setText(getString(R.string.vehicle_accident_title_log_off));
        } else {
            llEditText.addView(etPolicy.config("", getString(R.string.hint_plate), "", 1));
            btStart.setText(getResources().getText(R.string.button_start_assistance));
            tvTitle.setText(getString(R.string.vehicle_accident_24_title_log_off));
        }

        etPolicy.getEditText().addTextChangedListener(PlateMaskUtil.insert(etPolicy.getEditText()));
        etPolicy.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etPolicy.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        llEditText.addView(etCpfCnpj.config("", getString(R.string.hint_cpf), "", 2));

    }



    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.tv_forgot_email:
                vehicleAccidentController.openChangePassword(this);
                break;
            case R.id.bt_start:
                String msgError[] = vehicleAccidentController.validStepOff(VehicleAccidentLogOff.this, etPolicy.getText(), etCpfCnpj.getText(), typeScreen);

                boolean valid = false;

                if(!msgError[0].equals("")){
                    valid = true;
                    etPolicy.showMessageError(msgError[0]);
                }

                if(!msgError[1].equals("")){
                    valid = true;
                    etCpfCnpj.showMessageError(msgError[1]);
                }

                if(!valid){
                    if(typeScreen== 0){
                        VehicleAccidentModel.cpfCnpj = etCpfCnpj.getText();
                        VehicleAccidentModel.numberPolicy = etPolicy.getText();

                        vehicleAccidentController.openStep1Logoff(VehicleAccidentLogOff.this);
                    } else  if(typeScreen == 1){
                        vehicleAccidentController.open24hours(this, etCpfCnpj.getText(), etPolicy.getText());

                    }

                }

                break;
            case R.id.bt_login:
                vehicleAccidentController.openLogin(VehicleAccidentLogOff.this);
                break;
            case R.id.tv_speak_liberty:
                vehicleAccidentController.openSupport(this);
                break;
            case R.id.tv_not_user:
                vehicleAccidentController.openRegister(this);
                break;
            case R.id.tv_forgot_password:

                break;
        }
    }

}