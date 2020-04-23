package br.com.libertyseguros.mobile.view;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.VehicleAccidentController;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.PhoneMaskUtil;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class VehicleAccidentStep2 extends BaseActionBar implements View.OnClickListener {

    private VehicleAccidentController vehicleAccidentController;

    private LinearLayout llEdittext;

    private EditTextCustom etName;

    private EditTextCustom etEmail;

    private EditTextCustom etPhone;

    private EditTextCustom etNameDriver;

    private EditTextCustom etPhoneDriver;

    private EditTextCustom etBirthdayDriver;

    private LinearLayout llContentPart1;

    private LinearLayout llContentPart2;

    private LinearLayout llContentPart3;

    private TextView tvCheckErrorDate;

    private LinearLayout llDriver;

    private CheckBox cbNo;

    private CheckBox cbYes;

    private TextViewCustom tvSpeakLiberty;

    private TextView tvDate;

    public void onResume(){
        super.onResume();

        if(VehicleAccidentModel.exit){
            finish();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_9));

        setContentView(R.layout.vehicle_accident_step2);

        tvDate = (TextView) findViewById(R.id.tv_date);
        tvDate.setOnClickListener(this);

        mFirebaseAnalytics.setCurrentScreen(this, "Enviar Sinistro passo 2", null);

        vehicleAccidentController = new VehicleAccidentController(new OnConnectionResult() {
            @Override
            public void onError() {

            }

            @Override
            public void onSucess() {

            }
        });

        ButtonViewCustom btNext = (ButtonViewCustom) findViewById(R.id.bt_next_step_2);
        btNext.setOnClickListener(this);

        tvCheckErrorDate = (TextView) findViewById(R.id.tv_check_error_date);

        llEdittext = (LinearLayout) findViewById(R.id.ll_edittext);

        etName = new EditTextCustom(this);
        etEmail = new EditTextCustom(this);
        etPhone = new EditTextCustom(this);
        etNameDriver = new EditTextCustom(this);
        etPhoneDriver = new EditTextCustom(this);
        //etBirthdayDriver = new EditTextCustom(this);

        etName.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etNameDriver.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etEmail.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        llEdittext.addView(etName.config(vehicleAccidentController.getLoginBeans(this).getUserName(), getString(R.string.hint_name), "", 1));
        llEdittext.addView(etEmail.config(vehicleAccidentController.getLoginBeans(this).getEmail(), getString(R.string.hint_email), "", 1));
        llEdittext.addView(etPhone.config("", getString(R.string.hint_phone), "(##)#####-####", 2));
        etPhone.getEditText().addTextChangedListener(PhoneMaskUtil.insert(etPhone.getEditText()));

        llContentPart1 = (LinearLayout) findViewById(R.id.ll_content_part1);
        llContentPart2 = (LinearLayout) findViewById(R.id.ll_content_part2);
        llContentPart3 = (LinearLayout) findViewById(R.id.ll_content_part3);

        llContentPart2.addView(etPhoneDriver.config("", getString(R.string.hint_phone), "(##)#####-####", 2));
        etPhoneDriver.getEditText().addTextChangedListener(PhoneMaskUtil.insert(etPhoneDriver.getEditText()));
        etPhoneDriver.getEditText().setTextSize(12);

       // llContentPart1.addView(etBirthdayDriver.config("", getString(R.string.hint_birthday), "##/##/####", 2));
       // etBirthdayDriver.getEditText().addTextChangedListener(MaskEditText.insert("##/##/####", etBirthdayDriver.getEditText()));
       // etBirthdayDriver.getEditText().setTextSize(12);

        llContentPart3.addView(etNameDriver.config("", getString(R.string.hint_name), "", 1));

        llDriver = (LinearLayout) findViewById(R.id.ll_driver);

        cbNo = (CheckBox) findViewById(R.id.cb_no);
        cbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setCheckbox(2);
                }
            }
        });

        cbYes = (CheckBox) findViewById(R.id.cb_yes);
        cbYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setCheckbox(1);
                }
            }
        });

        cbYes.setChecked(true);
        llDriver.setVisibility(View.GONE);

        tvSpeakLiberty = (TextViewCustom) findViewById(R.id.tv_speak_liberty);
        tvSpeakLiberty.setOnClickListener(this);
        tvSpeakLiberty.setPaintFlags(tvSpeakLiberty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.tv_date:
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                tvDate.setText(dayOfMonth + "/" + month + "/" + year);
                                tvCheckErrorDate.setVisibility(View.GONE);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle(getString(R.string.datepicker_title));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

                break;
            case R.id.tv_speak_liberty:
                vehicleAccidentController.openSupport(VehicleAccidentStep2.this);
                break;
            case R.id.bt_next_step_2:
                Intent it = new Intent(VehicleAccidentStep2.this, VehicleAccidentStep3.class);
                //startActivity(it);

                String msg[] = vehicleAccidentController.validStep2(VehicleAccidentStep2.this,
                        etName.getText(), etEmail.getText(), etPhone.getText(),  cbYes.isChecked(), etNameDriver.getText(),
                        tvDate.getText().toString(), etPhoneDriver.getText());


                boolean valid = false;

                if(!msg[0].equals("")){
                    etName.showMessageError(msg[0]);
                    valid = true;
                }

                if(!msg[1].equals("")){
                    etEmail.showMessageError(msg[1]);
                    valid = true;
                }

                if(!msg[2].equals("")){
                    etPhone.showMessageError(msg[2]);
                    valid = true;
                }

                if(!msg[3].equals("")){
                    etNameDriver.showMessageError(msg[3]);
                    valid = true;
                }

                if(!msg[4].equals("")){
                    tvCheckErrorDate.setVisibility(View.VISIBLE);
                    valid = true;
                }

                if(!msg[5].equals("")){
                    etPhoneDriver.showMessageError(msg[5]);
                    valid = true;
                }


                if(!valid){
                    VehicleAccidentModel.vasb.setUserEmail(etEmail.getText());
                    VehicleAccidentModel.vasb.setUserName(etName.getText());
                    VehicleAccidentModel.vasb.setUserPhone(etPhone.getText());
                    VehicleAccidentModel.vasb.setUserIsDriver(cbYes.isChecked() +  "");

                    VehicleAccidentModel.vasb.setDriverName(etNameDriver.getText());
                    VehicleAccidentModel.vasb.setDriverPhone(etPhoneDriver.getText());
                    VehicleAccidentModel.vasb.setDriverBirthDate(tvDate.getText().toString());


                    vehicleAccidentController.openNextStep(VehicleAccidentStep2.this, 2);

                }


                break;
        }
    }

    /**
     * Set check box
     * @param index
     */
    private void setCheckbox(int index){
       switch(index){
           case 1:
               cbNo.setChecked(false);
               cbYes.setChecked(true);
               llDriver.setVisibility(View.GONE);
               break;
           case 2:
               cbNo.setChecked(true);
               cbYes.setChecked(false);
               llDriver.setVisibility(View.VISIBLE);
               break;
       }

    }

    public static float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }
}