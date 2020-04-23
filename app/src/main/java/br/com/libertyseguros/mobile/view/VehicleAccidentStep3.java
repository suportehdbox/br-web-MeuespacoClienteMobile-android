package br.com.libertyseguros.mobile.view;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import java.util.Calendar;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.VehicleAccidentController;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class VehicleAccidentStep3 extends BaseActionBar implements View.OnClickListener {

    private VehicleAccidentController vehicleAccidentController;

    private LinearLayout llEdittext;

    private LinearLayout llEdittext2;

    private LinearLayout llContent1;

    private LinearLayout llContent2;

    private EditTextCustom etLocation;

    private EditTextCustom etAddress2;

    private EditTextCustom etNumber;

    private EditTextCustom etCity;

    private EditTextCustom etDistrict;

    private EditTextCustom etReference;

    private AdjustableImageView ivHours;

    private AdjustableImageView ivDate;

    private TextView tvDate;

    private TextView tvTime;

    private TextViewCustom tvSpeakLiberty;

    private TextView tvCheckErrorHours;

    private TextView tvCheckErrorDate;

    private TextView tvCheckErrorState;

    private int indexSpinner;


    public void onResume() {
        super.onResume();

        if (VehicleAccidentModel.exit) {
            finish();
        }
        if (VehicleAccidentModel.vasb != null && VehicleAccidentModel.vasb.getCityBeans() != null) {
            etCity.getEditText().setText(VehicleAccidentModel.vasb.getCityBeans().getCity());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VehicleAccidentModel.vasb.setCityBeans(null);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_9));

        setContentView(R.layout.vehicle_accident_step3);


        mFirebaseAnalytics.setCurrentScreen(this, "Enviar Sinistro passo 3", null);

        vehicleAccidentController = new VehicleAccidentController(new OnConnectionResult() {
            @Override
            public void onError() {

            }

            @Override
            public void onSucess() {

            }
        });

        ButtonViewCustom btNext = (ButtonViewCustom) findViewById(R.id.bt_next_step_3);
        btNext.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tv_check_error_date);
        tvDate.setOnClickListener(this);

        tvCheckErrorHours = (TextView) findViewById(R.id.tv_check_error_hours);

        tvCheckErrorDate = (TextView) findViewById(R.id.tv_check_error_date);

        tvCheckErrorState = (TextView) findViewById(R.id.tv_check_error_state);


        llEdittext = (LinearLayout) findViewById(R.id.ll_edittext);
        llEdittext2 = (LinearLayout) findViewById(R.id.ll_edittext_part2);
        llContent1 = (LinearLayout) findViewById(R.id.ll_content_part1);
        llContent2 = (LinearLayout) findViewById(R.id.ll_content_part2);

        etLocation = new EditTextCustom(this);
        etCity = new EditTextCustom(this);
        etDistrict = new EditTextCustom(this);
        etReference = new EditTextCustom(this);
        etNumber = new EditTextCustom(this);
        etAddress2 = new EditTextCustom(this);

        etLocation.setMaxLength(50);

        llEdittext.addView(etLocation.config("", getString(R.string.hint_local), "", 1));
        //llEdittext.addView(etAddress2.config("", getString(R.string.hint_address_2), "", 1));

        llContent1.addView(etNumber.config("", getString(R.string.hint_number), "", 1));
        llContent2.addView(etReference.config("", getString(R.string.hint_reference), "", 1));


        indexSpinner = 0;

        Spinner spinner = (Spinner) findViewById(R.id.sp_state);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner, vehicleAccidentController.getState());
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setSelection(0, true);
        View v = spinner.getSelectedView();
        ((TextView) v).setTextColor(getResources().getColor(R.color.hint_default_1));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (indexSpinner != position) {
                    VehicleAccidentModel.vasb.setCityBeans(null);
                    etCity.getEditText().setText("");
                }
                indexSpinner = position;
                ((TextView) view).setTextColor(getResources().getColor(R.color.text_default_3));

                tvCheckErrorState.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        llEdittext2.addView(etCity.config("", getString(R.string.hint_city), "", 1));

        final Activity context = VehicleAccidentStep3.this;

        etCity.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (indexSpinner > 0) {
                        vehicleAccidentController.openCities(context, indexSpinner);
                    } else {
                        tvCheckErrorState.setVisibility(View.VISIBLE);
                    }
                    etCity.getEditText().setCursorVisible(false);
                    etCity.getEditText().setFocusable(false);
                    etCity.getEditText().setFocusableInTouchMode(false);
                }
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etCity.getEditText().getWindowToken(), 0);
            }
        });

        etCity.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexSpinner > 0) {
                    vehicleAccidentController.openCities(context, indexSpinner);
                } else {
                    tvCheckErrorState.setVisibility(View.VISIBLE);
                }
            }
        });


        llEdittext2.addView(etDistrict.config("", getString(R.string.hint_district), "", 1));

        ivHours = (AdjustableImageView) findViewById(R.id.iv_hours);
        ivHours.setOnClickListener(this);

        ivDate = (AdjustableImageView) findViewById(R.id.iv_date);
        ivDate.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tv_date);
        tvDate.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        tvDate.setText(day + "/" + month + "/" + year);

        tvTime = (TextView) findViewById(R.id.tv_time);
        tvTime.setOnClickListener(this);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        tvTime.setText(String.format("%02d:%02d", hour, minute));

        tvSpeakLiberty = (TextViewCustom) findViewById(R.id.tv_speak_liberty);
        tvSpeakLiberty.setOnClickListener(this);
        tvSpeakLiberty.setPaintFlags(tvSpeakLiberty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_speak_liberty:
                vehicleAccidentController.openSupport(VehicleAccidentStep3.this);
                break;
            case R.id.tv_time:
            case R.id.iv_hours:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(VehicleAccidentStep3.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                        tvTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        tvCheckErrorHours.setVisibility(View.GONE);

                    }
                }, hour, minute, true);

                mTimePicker.setTitle(getString(R.string.timerpicker_title));
                mTimePicker.show();

                break;

            case R.id.tv_date:
            case R.id.iv_date:
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
            case R.id.bt_next_step_3:

                boolean valid = false;

                String msg[] = vehicleAccidentController.validStep3(VehicleAccidentStep3.this, etLocation.getText(), etReference.getText(), etCity.getText(), etDistrict.getText(), etNumber.getText());

                if (tvDate.getText().toString().equals(getResources().getString(R.string.hint_data))) {
                    tvCheckErrorDate.setVisibility(View.VISIBLE);
                    valid = true;
                }

                if (tvTime.getText().toString().equals(getResources().getString(R.string.hint_hours))) {
                    tvCheckErrorHours.setVisibility(View.VISIBLE);
                    valid = true;
                }


                if (!msg[0].equals("")) {
                    etLocation.showMessageError(msg[0]);
                    valid = true;
                }

                if (!msg[1].equals("")) {
                    //etReference.showMessageError(msg[1]);
                    //valid = true;
                }

                if (!msg[2].equals("")) {
                    etCity.showMessageError(msg[2]);
                    valid = true;
                }

                if (!msg[3].equals("")) {
                    etDistrict.showMessageError(msg[3]);
                    valid = true;
                }

                if (!msg[4].equals("")) {
                    etNumber.showMessageError(msg[4]);
                    valid = true;
                }

                if (indexSpinner == 0) {
                    tvCheckErrorState.setVisibility(View.VISIBLE);
                    valid = true;
                }

                if (!valid) {
                    VehicleAccidentModel.vasb.setClaimDateTime(tvDate.getText() + " " + tvTime.getText());
                    VehicleAccidentModel.vasb.setAddressLine1(etLocation.getText());
                    VehicleAccidentModel.vasb.setAddressLine2(etAddress2.getText());
                    VehicleAccidentModel.vasb.setNumber(etNumber.getText());
                    VehicleAccidentModel.vasb.setAddressSupport(etReference.getText());
                    VehicleAccidentModel.vasb.setCity(etCity.getText());
                    VehicleAccidentModel.vasb.setDistrict(etDistrict.getText());
                    VehicleAccidentModel.vasb.setState(indexSpinner);
                    vehicleAccidentController.openNextStep(VehicleAccidentStep3.this, 3);
                }

                break;


        }
    }


}