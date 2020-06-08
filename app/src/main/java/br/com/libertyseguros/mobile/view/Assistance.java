package br.com.libertyseguros.mobile.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.gridlayout.widget.GridLayout;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.AssistanceController;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class Assistance extends BaseActionBar implements OnClickListener{

    public  static Activity activity;

    private TextViewCustom tvVehicleAccidentStatus;

    private TextViewCustom tvSpeakLiberty;

    private ImageViewCustom ivAssitance;

    private ButtonViewCustom ibAcSinistro;

    private ButtonViewCustom ibAc24;

    private ImageViewCustom ivSinistro;

    private ImageViewCustom ivGlass;

    private ImageViewCustom ivHome;

    private boolean newVA;

    private AssistanceController assistanceController;

    private LinearLayout llStatus;

    private Dialog dialogMessage;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        assistanceController = new AssistanceController(this);




        setContentView(R.layout.activity_assistance);

        mFirebaseAnalytics.setCurrentScreen(this, "Assistencia24h", null);


        activity = this;

        getSupportActionBar().setTitle(getString(R.string.title_action_bar_2));

        tvSpeakLiberty = (TextViewCustom) findViewById(R.id.tv_speak_liberty);
        tvSpeakLiberty.setPaintFlags(tvSpeakLiberty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String htmlString="<u>" + getString(R.string.speak_liberty_part2) + "</u>";
        tvSpeakLiberty.setOnClickListener(this);
        ivAssitance = (ImageViewCustom) findViewById(R.id.iv_assistance);
        ivAssitance.setOnClickListener(this);

        ivSinistro = (ImageViewCustom) findViewById(R.id.iv_sinistro);
        ivSinistro.setOnClickListener(this);

        tvVehicleAccidentStatus = (TextViewCustom) findViewById(R.id.tv_click);
        tvVehicleAccidentStatus.setOnClickListener(this);

        ibAcSinistro = (ButtonViewCustom) findViewById(R.id.ib_ac_sinistro);
        ibAcSinistro.setOnClickListener(this);

        ibAc24 = (ButtonViewCustom) findViewById(R.id.ib_ac_24);
        ibAc24.setVisibility(View.GONE);

        ivGlass = (ImageViewCustom) findViewById(R.id.iv_glass_assistance);
        ivGlass.setOnClickListener(this);


        ivHome = (ImageViewCustom) findViewById(R.id.iv_home_assistance);
        ivHome.setOnClickListener(this);



        TextViewCustom tvClick = (TextViewCustom) findViewById(R.id.tv_click);
        tvClick.setPaintFlags(tvClick.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        llStatus = (LinearLayout) findViewById(R.id.ll_status);

        if(assistanceController.isloginOn()){
            llStatus.setVisibility(View.VISIBLE);
        } else {
            llStatus.setVisibility(View.INVISIBLE);
        }

        GridLayout gl_buttons = findViewById(R.id.grid_buttons);

        if(assistanceController.homeAssistanceAllowed()) {
            gl_buttons.setColumnCount(2);
        }else{
            gl_buttons.setColumnCount(3);
            ivHome.setVisibility(View.GONE);
        }


        configDialog();

    }

    @Override
    public void onClick(View v) {

       try {
           switch (v.getId()) {
               case R.id.tv_click:
               case R.id.ib_ac_sinistro:
                   assistanceController.openVehicleAccidentStatus(this);
                   break;
               case R.id.iv_assistance:
                   assistanceController.openAssitance();
                   break;
               case R.id.tv_speak_liberty:
                   assistanceController.openSupport(this);
                   break;
               case R.id.iv_sinistro:
                   assistanceController.openVehicleAccident(this);
                   break;
               case R.id.iv_home_assistance:
                   assistanceController.openHomeAssistWebView();

                   break;
               case R.id.iv_glass_assistance:
                   assistanceController.openGlassAssistance(this);
                   break;
           }
       }catch(Exception ex){
           ex.printStackTrace();
       }
    }

    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        this.assistanceController.checkPermissionsGranted(requestCode, permissions, grantResults);
    }


    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message_permission_location);

        TextView tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);
        tvMessageDialog.setText(getString(R.string.error_location_permission));

        TextView tvClose = (TextView) dialogMessage.findViewById(R.id.tv_close);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });


        TextView tvConfiguration = (TextView) dialogMessage.findViewById(R.id.tv_configuration);

        tvConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });

    }
}
