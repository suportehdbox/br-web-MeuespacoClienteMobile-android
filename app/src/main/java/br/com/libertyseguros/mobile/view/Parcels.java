package br.com.libertyseguros.mobile.view;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ListParcelAdapter;
import br.com.libertyseguros.mobile.beans.BarCodeBeans;
import br.com.libertyseguros.mobile.controller.ParcelsController;
import br.com.libertyseguros.mobile.model.PolicyModelV2;
import br.com.libertyseguros.mobile.model.VehicleAccidentModel;
import br.com.libertyseguros.mobile.util.ManagerDialogCodeBar;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;



public class Parcels extends BaseActionBar implements OnBarCode{

    private ParcelsController parcelsController;

    private String numberPolicy;

    private String ciaCode;

    private String contract;

    private String issuance;

    private ListView lvParcels;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private boolean value;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private ListParcelAdapter listParcelAdapter;

    private int indexCod;

    @Override
    public void onResume() {
        super.onResume();

        if(ExtendPagament.isExtends && listParcelAdapter!= null &&(listParcelAdapter.index+1) == ExtendPagament.numberFinish){
//            ExtendPagament.isExtends = false;
            int index = listParcelAdapter.index;
            parcelsController.getInstallments().getInstallments().get(index).setStatus(3);
            parcelsController.getInstallments().getInstallments().get(index).setShowComponent(1);
            parcelsController.getInstallments().getInstallments().get(index).setDueDate(ExtendPagament.dataFinish);
            parcelsController.getInstallments().getInstallments().get(index).setValue(ExtendPagament.valueFinish);
            listParcelAdapter.updateList(parcelsController.getInstallments().getInstallments());
//            listParcelAdapter = new ListParcelAdapter(Parcels.this, parcelsController.getInstallments().getInstallments());
//            lvParcels.setAdapter(listParcelAdapter);
        }

    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_8));

        setContentView(R.layout.parcels);

        mFirebaseAnalytics.setCurrentScreen(this, "Parcelas", null);

        tvMessageDialog = (TextView) findViewById(R.id.tv_message);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            numberPolicy =   extras.getString("policyNumber");
            ciaCode =   extras.getString("ciaCode");
            contract =   extras.getString("contract");
            issuance =   extras.getString("issuance");
        }

        parcelsController = new ParcelsController(new OnConnectionResult() {
            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showLoading(false);
                            if (parcelsController.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {

                                if(parcelsController.getTypeConnection() == 1) {

                                    if (parcelsController.getMessage() == null) {
                                        dialogMessageTwoButton.show();
                                    } else {
                                        tvMessageDialog.setText(parcelsController.getMessage().getMessage());
                                        dialogMessage.show();
                                    }
                                }else if(parcelsController.getTypeConnection() == 2){
                                    tvMessageDialog.setText(parcelsController.getMessage().getMessage());
                                    dialogMessage.show();
                                }


                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSucess() {

                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                         if(parcelsController.getTypeConnection() == 1){
                             try {
                                 showLoading(false);
                                 listParcelAdapter = new ListParcelAdapter(Parcels.this, parcelsController.getInstallments().getInstallments(), Integer.valueOf(issuance), Parcels.this, PolicyModelV2.getInsurancesSel() );
                                 lvParcels.setAdapter(listParcelAdapter);
                             }catch(Exception ex){
                                 ex.printStackTrace();
                             }
                         } else  if(parcelsController.getTypeConnection() == 2){
                             try {
                                 showLoading(false);

                                 ManagerDialogCodeBar managerDialogCodeBar = new ManagerDialogCodeBar(Parcels.this);
                                 managerDialogCodeBar.createDialog(parcelsController.getBarCodeBeans());

                             }catch(Exception ex){
                                 ex.printStackTrace();
                             }
                         }


                    }
                });
            }

        }, numberPolicy, contract, ciaCode, issuance);

        lvParcels =  (ListView) findViewById(R.id.lv_parcels);


        configDialog();

        showLoading(true);
        parcelsController.getParcels(this);
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
                if(parcelsController.getTypeConnection() == 1){
                    parcelsController.getParcels(Parcels.this);
                } else  if(parcelsController.getTypeConnection() == 2){
                    parcelsController.getCod(Parcels.this, indexCod);
                }


            }
        });

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

    public void onBarCode(int parcelNumber){
        showLoading(true);
        parcelsController.getCod(Parcels.this, parcelNumber);
    }

}

