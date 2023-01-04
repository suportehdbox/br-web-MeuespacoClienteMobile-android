package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.SalesmanAdapter;
import br.com.libertyseguros.mobile.beans.SalesmanBeans;
import br.com.libertyseguros.mobile.controller.SupportController;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;


public class Support extends BaseActionBar implements View.OnClickListener {

    private SupportController supportController ;

    private LinearLayout ll_crm;

    private LinearLayout ll_dm;

    private LinearLayout ll_aae;

    private LinearLayout ll_aer;

    private LinearLayout ll_cb;

    private TextView tvTitleAuto;

    private TextView tvTitleCompany;

    private TextView tvTitleSkype;


    private ListView lvSalesman;

    private LinearLayout llContent;
    private LinearLayout ll_line;
    private LinearLayout ll_enterprise;

    private LinearLayout llLoading;

    private boolean value;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;

    private Dialog dialogMessageTwoButton;

    private LinearLayout llSkype;


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        mFirebaseAnalytics.setCurrentScreen(this, "Atendimento", null);

        setTitle(getString(R.string.title_action_bar_7));

        support = true;

        supportController = new SupportController(new OnConnectionResult() {
            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showLoading(false);
                            if (supportController.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {

                                if (supportController.getMessage() == null) {
                                    dialogMessageTwoButton.show();
                                } else {
                                    tvMessageDialog.setText(supportController.getMessage().getMessage());
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
                     //   lvSalesman.setAdapter(new SalesmanAdapter(Support.this, supportController.getSalesman()));

                        try {
                            showLoading(false);
                            lvSalesman.setAdapter(new SalesmanAdapter(Support.this, supportController.getSalesman()));
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });

            }
        });


        if(supportController.isLogin(this)){
            setContentView(R.layout.activity_support);
        } else {
            setContentView(R.layout.activity_support_1);
        }


        ll_crm = (LinearLayout) findViewById(R.id.ll_crm);
        ll_crm.setOnClickListener(this);

        ll_dm = (LinearLayout) findViewById(R.id.ll_dl);
        ll_dm.setOnClickListener(this);

        ll_aae = (LinearLayout) findViewById(R.id.ll_aav);
        ll_aae.setOnClickListener(this);

        ll_aer = (LinearLayout) findViewById(R.id.ll_aer);
        ll_aer.setOnClickListener(this);

        tvMessageDialog = (TextView) findViewById(R.id.tv_message);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        tvTitleAuto = (TextView) findViewById(R.id.tv_title_auto_life);
        tvTitleAuto.setText(Html.fromHtml(getString(R.string.title_phone_1)));


        tvTitleCompany = (TextView) findViewById(R.id.tv_title_company_home);
        tvTitleCompany.setText(Html.fromHtml(getString(R.string.title_phone_2)));

        ll_enterprise = (LinearLayout) findViewById(R.id.ll_aav);
        ll_line = (LinearLayout) findViewById(R.id.ll_line);

        if(getString(R.string.title_phone_2).equalsIgnoreCase("")){
            ll_enterprise.setVisibility(View.GONE);
            ll_line.setVisibility(View.GONE);
            tvTitleCompany.setText(Html.fromHtml(getString(R.string.title_phone_1)));
        }

        tvTitleSkype = (TextView) findViewById(R.id.tv_skype);
        tvTitleSkype.setText(Html.fromHtml(getString(R.string.title_skype)));

        lvSalesman = (ListView) findViewById(R.id.lv_salesman);

        configDialog();

        if(!supportController.isLogin(this)){
            //lvSalesman.setVisibility(View.GONE);
            showLoading(false);
            lvSalesman.setAdapter(new SalesmanAdapter(Support.this, supportController.getCache()));

        } else {
            lvSalesman.setVisibility(View.GONE);
            showLoading(true);
            supportController.getArray(this);
        }

        llSkype = (LinearLayout) findViewById(R.id.skype);
        llSkype.setOnClickListener(this);
        llSkype.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view){

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Ligação");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Login");
        bundle.putString(FirebaseAnalytics.Param.CONTENT, "Atendimento");

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        switch (view.getId()){
            case R.id.ll_crm:
                supportController.call(this, 3);
                break;
            case R.id.ll_dl:
                supportController.call(this, 4);
                break;
            case R.id.ll_aav:
                supportController.call(this, 2);
                break;
            case R.id.ll_aer:
                supportController.call(this, 1);
                break;
            case R.id.ll_cb:
                supportController.call(this, 5);
                break;
            case R.id.skype:
                supportController.skype(this);
                break;

        }
    }


    public ArrayList<SalesmanBeans> getTeste(){
        ArrayList<SalesmanBeans> salesmanBeans = new ArrayList<>();

        SalesmanBeans sb = new SalesmanBeans();

        sb.setBrokerCode(1000);
        sb.setDescription("teste1");
        sb.setEmail("teste1@teste.com");
        sb.setPhone("11983402854");

        SalesmanBeans sb1 = new SalesmanBeans();

        sb1.setBrokerCode(1000);
        sb1.setDescription("teste");
        sb1.setEmail("teste@teste.com");
        sb1.setPhone("983402854");
        salesmanBeans.add(sb);
        salesmanBeans.add(sb1);


        return salesmanBeans;
    }

    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

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

                /*
                switch (loginController.getTypeConnection()){
                    case 1:
                        login();
                        break;
                    case 2:
                        loginController.getLoginFacebook(Login.this, loginController.getEmailFacebook(), loginController.getIdFacebook());
                        break;
                }
                */
            }
        });

        /*
        if(loginController.isTokenLogin()){
            showLoading(true);
            loginController.getLoginToken(this);
        }*/
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
                    lvSalesman.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    lvSalesman.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
