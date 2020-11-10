package br.com.libertyseguros.mobile.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ListPolicyAdapter;
import br.com.libertyseguros.mobile.controller.PolicyController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.model.PolicyModelV2;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class ListPolicy extends BaseActionBar{

    private PolicyController policyController;

    private ListView lvPolicy;

    private Dialog dialogMessageTwoButton;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;

    private LinearLayout llProgress;

    private ProgressBar pbFooter;

    private boolean value;

    private  View footerView;

    private  View headerView;

    private ButtonViewCustom ivPolicyMore;


    private boolean morePolicy;

    private boolean vehicleAccident;

    private  boolean documents;


    public void onResume(){
        super.onResume();

        if(PolicyModelV2.isMorePolicy()){
            PolicyModelV2.setMorePolicy(false);

            if(!morePolicy){
                showLoadingFooter(true);
                policyController.getListPolicy(ListPolicy.this, false);
            }

        }

    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_list_policy);

        mFirebaseAnalytics.setCurrentScreen(this, "Liste de Apólice", null);

        llProgress = (LinearLayout) findViewById(R.id.ll_loading);

        lvPolicy = (ListView) findViewById(R.id.lv_policy);



        lvPolicy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i(Config.TAG, "documents 1");

                if(policyController.isVehicleAccident()){
                    policyController.openVehicleAccident(ListPolicy.this, position);
                    //Log.i(Config.TAG, "documents a");

                } else if (policyController.isDocument()) {
                    //Log.i(Config.TAG, "documents b");
                    policyController.openDocuments(ListPolicy.this, position);
                }else {

                    policyController.openDetail(ListPolicy.this, position, false);

                    //Log.i(Config.TAG, "documents c");

                }
            }
        });

        policyController = new PolicyController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try {
                            if (policyController.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {
                                tvMessageDialog.setText(policyController.getMessage().getMessage());
                                dialogMessage.show();
                            }

                            if (policyController.getTypeConnection() == 2) {
                                showLoadingFooter(false);
                            } else {
                                showLoadingFooter(false);
                            }
                        }catch(Exception ex){
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
                            try {

                                if(policyController.getPolicyBeans().getInsurances().length > 1 || policyController.isDocument()) {

                                    lvPolicy.setAdapter(new ListPolicyAdapter(ListPolicy.this, policyController.getPolicyBeans()));

                                    if (policyController.isVehicleAccident()) {
                                        footerView.setVisibility(View.GONE);
                                    }

                                    if (policyController.getTypeConnection() == 2) {
                                        morePolicy = true;

                                        try{
                                            showLoadingFooter(false);

                                            footerView.setVisibility(View.GONE);

                                            lvPolicy.setSelection(policyController.getPolicyBeans().getInsurances().length - 2);

                                            showLoading(false);

                                        }catch(Exception ex){
                                            ex.printStackTrace();
                                        }
                                    } else {
                                        showLoading(false);
                                    }

                                }
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

        footerView =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_apolicy, null, false);
        pbFooter = (ProgressBar) footerView.findViewById(R.id.pb_footer);



        ivPolicyMore = (ButtonViewCustom) footerView.findViewById(R.id.bt_policy_more);
        ivPolicyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingFooter(true);
                policyController.getListPolicy(ListPolicy.this, false);
            }
        });

        lvPolicy.addFooterView(footerView);

        Bundle extras = getIntent().getExtras();

        String morePolicy = null;

        if(extras != null) {
            morePolicy =   extras.getString("morePolicy");
            String va = extras.getString("vehicleAccident");
            String dc = extras.getString("documents");

            if(va == null){
                vehicleAccident = false;
            } else {
                vehicleAccident = true;
            }


            if(dc == null){
                documents = false;
            } else {
                documents = true;
            }

            policyController.setVehicleAccident(vehicleAccident);
            policyController.setDocuments(documents);
        }

        if(policyController.isVehicleAccident()){
            setTitle(getString(R.string.title_action_bar_9));
            headerView =  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_list_policy,  null, false);
            //lvPolicy.addHeaderView(headerView);
        } else {
            setTitle(getString(R.string.title_action_bar_8));
        }


        showLoading(true);

        if(morePolicy != null){
            policyController.getListPolicy(this, false);
        } else {
            policyController.getListPolicy(this, true);

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

//                if(policyController.getTypeConnection() == 1){
//                }
                try{
                    if(policyController.getPolicyBeans().getInsurances().length <= 0){
                        finish();
                    }
                } catch(Exception ex){
                    finish();
                }



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

                if(policyController.getTypeConnection() == 1){
                    finish();
                }
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);

                switch (policyController.getTypeConnection()){
                    case 1:
                        policyController.getListPolicy(ListPolicy.this, true);
                        break;
                    case 2:
                        policyController.getListPolicy(ListPolicy.this, false);
                        break;
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
                    llProgress.setVisibility(View.VISIBLE);
                    lvPolicy.setVisibility(View.GONE);
                } else {
                    llProgress.setVisibility(View.GONE);
                    lvPolicy.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    /* Show progress loading
   *
   * @param v
   * @param m
   */
    private void showLoadingFooter(boolean v) {
        this.value = v;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    pbFooter.setVisibility(View.VISIBLE);
                    ivPolicyMore.setVisibility(View.GONE);
                } else {
                    pbFooter.setVisibility(View.GONE);
                    ivPolicyMore.setVisibility(View.VISIBLE);
                }
            }
        });

    }


}
