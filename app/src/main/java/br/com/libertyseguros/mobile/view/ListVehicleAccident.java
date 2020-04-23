package br.com.libertyseguros.mobile.view;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ListVehicleAccidentAdpater;
import br.com.libertyseguros.mobile.controller.ListVehicleAccidentController;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;

public class ListVehicleAccident extends BaseActionBar{

    private ListVehicleAccidentController listVehicleAccidentController;

    private ListView lvPolicy;

    private Dialog dialogMessageTwoButton;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;

    private LinearLayout llProgress;

    private boolean value;

    public static boolean refresh;

    private int typeScreen;


    public void onResume(){
        super.onResume();

        if(VehicleAccidentModel.exit){
            VehicleAccidentModel.exit = false;
            ListVehicleAccident.refresh = true;
            finish();
        } else {
            if(refresh){
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        ListVehicleAccident.refresh = false;
                        lvPolicy.setAdapter(new ListVehicleAccidentAdpater(ListVehicleAccident.this, listVehicleAccidentController.getVehicleAccidentSendBeans()));
                        }
                    });
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_list_policy);


        mFirebaseAnalytics.setCurrentScreen(ListVehicleAccident.this, "Lista de Apólice", null);

        llProgress = (LinearLayout) findViewById(R.id.ll_loading);

        lvPolicy = (ListView) findViewById(R.id.lv_policy);

        typeScreen = Integer.parseInt(getIntent().getExtras().getString("vehicleAccident"));

        if(typeScreen == 0){
            setTitle(getString(R.string.title_action_bar_9));
        } else {
            setTitle(getString(R.string.title_action_bar_14));

        }

        lvPolicy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listVehicleAccidentController.openVehicleAccident(ListVehicleAccident.this, position, typeScreen);

            }
        });

        listVehicleAccidentController = new ListVehicleAccidentController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try {
                            if (listVehicleAccidentController.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {
                                tvMessageDialog.setText(listVehicleAccidentController.getMessage().getMessage());
                                dialogMessage.show();
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        }
                    });
                }catch (Exception ex){
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
                                if(listVehicleAccidentController.getVehicleAccidentSendBeans().length == 1){
                                    finish();
                                    listVehicleAccidentController.openVehicleAccident(ListVehicleAccident.this, 0, typeScreen);
                                } else {
                                    lvPolicy.setAdapter(new ListVehicleAccidentAdpater(ListVehicleAccident.this, listVehicleAccidentController.getVehicleAccidentSendBeans()));
                                }

                                showLoading(false);
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

        configDialog();


        Bundle extras = getIntent().getExtras();

        String type = null;

        if(extras != null) {
            type =   extras.getString("type");

            if(type == null){
                VehicleAccidentModel.numberPolicy = "";
                VehicleAccidentModel.cpfCnpj = "";
            }
        }

        showLoading(true);

        listVehicleAccidentController.getList(ListVehicleAccident.this);

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
                finish();

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
                listVehicleAccidentController.getList(ListVehicleAccident.this);

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
                    llProgress.setVisibility(View.VISIBLE);
                    lvPolicy.setVisibility(View.GONE);
                } else {
                    llProgress.setVisibility(View.GONE);
                    lvPolicy.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
