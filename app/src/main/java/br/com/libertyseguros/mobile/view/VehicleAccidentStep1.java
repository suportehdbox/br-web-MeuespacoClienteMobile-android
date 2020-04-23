package br.com.libertyseguros.mobile.view;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.VehicleAccidentController;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class VehicleAccidentStep1 extends BaseActionBar implements View.OnClickListener{

    private VehicleAccidentController vehicleAccidentController;
    private LinearLayout llContent;

    private LinearLayout llLoading;

    private boolean value;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private ButtonViewCustom btNext;

    private CheckBox cbTheft;

    private CheckBox cbOverflow;

    private CheckBox cbAccidentsInvolvingThirdParties;

    private CheckBox cbAccidents;

    private TextViewCustom tvSpeakLiberty;

    public void onResume(){
        super.onResume();

        if(VehicleAccidentModel.exit){
            finish();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        setContentView(R.layout.vehicle_accident_step1);

        setTitle(getString(R.string.title_action_bar_9));

        ListVehicleAccident.refresh = true;

        mFirebaseAnalytics.setCurrentScreen(this, "Enviar Sinistro passo 1", null);

        cbTheft = (CheckBox) findViewById(R.id.cb_theft);
        cbTheft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setCheckBox(1);
                }
            }
        });

        cbOverflow = (CheckBox) findViewById(R.id.cb_overflow);
        cbOverflow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setCheckBox(2);
                }
            }
        });

        cbAccidentsInvolvingThirdParties = (CheckBox) findViewById(R.id.cb_accidents_involving_third_parties);
        cbAccidentsInvolvingThirdParties.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setCheckBox(3);
                }
            }
        });

        cbAccidents = (CheckBox) findViewById(R.id.cb_accidents);
        cbAccidents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setCheckBox(4);
                }
            }
        });

        tvMessageDialog = (TextView) findViewById(R.id.tv_message);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        btNext = (ButtonViewCustom) findViewById(R.id.bt_next);
        btNext.setOnClickListener(this);

        vehicleAccidentController = new VehicleAccidentController(new OnConnectionResult() {
            @Override
            public void onError() {

            }

            @Override
            public void onSucess() {

            }
        });

        configDialog();

       // vehicleAccidentModel.uploadTest(this, "1504033");
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
                //parcelsController.getParcels(VeichleAccidentStep1.this);

            }
        });

        tvSpeakLiberty = (TextViewCustom) findViewById(R.id.tv_speak_liberty);
        tvSpeakLiberty.setOnClickListener(this);
        tvSpeakLiberty.setPaintFlags(tvSpeakLiberty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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
    public void onClick(View view){
        switch(view.getId()){
            case R.id.tv_speak_liberty:
                vehicleAccidentController.openSupport(VehicleAccidentStep1.this);
                break;
            case R.id.bt_next:
                Intent it = new Intent(VehicleAccidentStep1.this, VehicleAccidentStep2.class);
                //startActivity(it);
                int index = -1;

                if(cbTheft.isChecked()){
                    index = 1;
                   // VehicleAccidentModel.vasb.setIssuingAgency("30");
                    VehicleAccidentModel.vasb.setClaimType("30");

                } else if(cbOverflow.isChecked()){
                    index = 2;
                   // VehicleAccidentModel.vasb.setIssuingAgency("50");
                    VehicleAccidentModel.vasb.setClaimType("50");
                } else if (cbAccidentsInvolvingThirdParties.isChecked()){
                    index = 3;
                } else if (cbAccidents.isChecked()){
                    index = 4;
//                    VehicleAccidentModel.vasb.setIssuingAgency("10");
                    VehicleAccidentModel.vasb.setClaimType("10");

                }

                VehicleAccidentModel.vasb.setLicensePlate(VehicleAccidentModel.vasb.getInsuranceStatus().getLicensePlate());

                if(index == -1){
                    tvMessageDialog.setText(getString(R.string.vehicle_accident_error_validation_step_1));
                    dialogMessage.show();
                } else {
                    vehicleAccidentController.openNextStep(VehicleAccidentStep1.this, 1);
                }


                break;
        }
    }

    /**
     * Set Checkbox
     * @param index
     */
    public void setCheckBox(int index){
        switch(index){
            case 1:
                cbTheft.setChecked(true);
                cbOverflow.setChecked(false);
                cbAccidentsInvolvingThirdParties.setChecked(false);
                cbAccidents.setChecked(false);
                break;
            case 2:
                cbTheft.setChecked(false);
                cbOverflow.setChecked(true);
                cbAccidentsInvolvingThirdParties.setChecked(false);
                cbAccidents.setChecked(false);
                break;
            case 3:
                cbTheft.setChecked(false);
                cbOverflow.setChecked(false);
                cbAccidentsInvolvingThirdParties.setChecked(true);
                cbAccidents.setChecked(false);
                break;
            case 4:
                cbTheft.setChecked(false);
                cbOverflow.setChecked(false);
                cbAccidentsInvolvingThirdParties.setChecked(false);
                cbAccidents.setChecked(true);
                break;
        }
    }

}