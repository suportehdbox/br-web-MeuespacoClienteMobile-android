package br.com.libertyseguros.mobile.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.InsuranceCoverages;
import br.com.libertyseguros.mobile.controller.ListVision360Controller;
import br.com.libertyseguros.mobile.controller.ParcelsController;
import br.com.libertyseguros.mobile.controller.PolicyController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.PolicyCalc;
import br.com.libertyseguros.mobile.model.PolicyModelV2;
import br.com.libertyseguros.mobile.util.ManagerDialogCodeBar;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class DetailPolicy extends BaseActionBar implements View.OnClickListener, OnBarCode {
    private ParcelsController parcelsController;

    private PolicyController policyController;

    private ListVision360Controller listVision360Controller;

    private Dialog dialogMessage;

    private Dialog dialogSecondCopyPolicy;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private ScrollView svContent;

    private LinearLayout llProgress;

    private CardView vision360CardView;

    private boolean value;

    private ButtonViewCustom btPolicyMore;

    private TextView tvPolicy;

    private TextView tvTitleCircle;

    private BroadcastReceiver broadcastReceiver;

    private CircularProgressView circularProgressView_1;

    private CircularProgressView circularProgressView_2;

    private CircularProgressView circularProgressView_3;

    private TextView tvDateCb1;

    private TextView tvDateCb2;

    private TextView tvDateCb3;

    private TextView tvPhone;

    private TextView tvEmail;

    private TextView tvName;

    private LinearLayout llPhone;

    private LinearLayout llEmail;

    private LinearLayout llCircle1;

    private LinearLayout llCircle2;

    private LinearLayout llCircle3;

    private PolicyCalc policyCalc;

    private TextView tvCoverages;

    private LinearLayout llCoverages;

    private LinearLayout llLineCoverages;

    private ButtonViewCustom btCoverage;

    private ButtonViewCustom btSecondCopyPolicy;

    private ProgressDialog progressDialog;

    private LinearLayout ll_container_description;
    private LinearLayout ll_container_payment;
    private LayoutInflater inflater;
    private int indexPayment;

    private boolean shareSecondCopyPolicy;

    @Override
    public void onResume() {
        super.onResume();
        if (ExtendPagament.isExtends) {
            if (indexPayment < 0) {
                try {
                    for (int i = 0; i < policyController.getDetailPolicyBeans().getPayment().length; i++) {
                        if (policyController.getDetailPolicyBeans().getPayment()[i].getInstallmentNumber() == ExtendPagament.numberFinish &&
                                policyController.getDetailPolicyBeans().getInsurance().getContract().equalsIgnoreCase(ExtendPagament.contractFinish)) {
                            indexPayment = i;
                        }
                    }
                }catch (Exception ex){
                    //Log.i(Config.TAG, ex.toString());
                }
            }

            try {
                if (policyController.getDetailPolicyBeans().getPayment()[indexPayment].getInstallmentNumber() == ExtendPagament.numberFinish &&
                        policyController.getDetailPolicyBeans().getInsurance().getContract().equalsIgnoreCase(ExtendPagament.contractFinish)) {

                    ll_container_payment.findViewWithTag("ivIconExtends" + indexPayment).setVisibility(View.INVISIBLE);
                    ((ImageViewCustom) ll_container_payment.findViewWithTag("ivIconPayment" + indexPayment)).setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_pending));
                    ((TextView) ll_container_payment.findViewWithTag("tvValuePayment" + indexPayment)).setText(ExtendPagament.valueFinish);
                    ((TextView) ll_container_payment.findViewWithTag("tvDatePayment" + indexPayment)).setText(policyCalc.getDateExtends(ExtendPagament.dataFinish, this, 1));
                    indexPayment = -1;
                }
            } catch (Exception ex){
                //Log.i(Config.TAG, ex.toString());
            }
            ExtendPagament.isExtends = false;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        indexPayment = -1;

        setContentView(R.layout.activity_detail_policy);

        inflater = this.getLayoutInflater();

        setTitle(getString(R.string.title_action_bar_8));

        mFirebaseAnalytics.setCurrentScreen(this, "Detalhe da Apólice", null);

        policyCalc = new PolicyCalc();

        vision360CardView = findViewById(R.id.card_vision);

        tvPolicy = (TextView) findViewById(R.id.tv_policy);

        tvCoverages = (TextView) findViewById(R.id.tv_coverages);

        llCoverages = (LinearLayout) findViewById(R.id.ll_coverages);

        btCoverage = (ButtonViewCustom) findViewById(R.id.bt_coverage);
        btCoverage.setOnClickListener(this);

        btSecondCopyPolicy = (ButtonViewCustom) findViewById(R.id.bt_second_copy_policy);
        btSecondCopyPolicy.setOnClickListener(this);

        llLineCoverages = (LinearLayout) findViewById(R.id.ll_line_coverages);

        ll_container_description = (LinearLayout) findViewById(R.id.ll_container_description);
        ll_container_payment = (LinearLayout) findViewById(R.id.ll_container_payment);

        tvName = (TextView) findViewById(R.id.tv_name);

        tvDateCb1 = (TextView) findViewById(R.id.tv_date_cb_1);
        tvDateCb2 = (TextView) findViewById(R.id.tv_date_cb_2);
        tvDateCb3 = (TextView) findViewById(R.id.tv_date_cb_3);

        tvTitleCircle = (TextView) findViewById(R.id.tv_title_circle_1);

        circularProgressView_1 = (CircularProgressView) findViewById(R.id.cp_1);

        circularProgressView_2 = (CircularProgressView) findViewById(R.id.cp_2);
        circularProgressView_3 = (CircularProgressView) findViewById(R.id.cp_3);

        llCircle1 = (LinearLayout) findViewById(R.id.ll_circle_1);
        llCircle2 = (LinearLayout) findViewById(R.id.ll_circle_2);
        llCircle3 = (LinearLayout) findViewById(R.id.ll_circle_3);

        tvDateCb1 = (TextView) findViewById(R.id.tv_date_cb_1);

        tvDateCb2 = (TextView) findViewById(R.id.tv_date_cb_2);

        tvDateCb3 = (TextView) findViewById(R.id.tv_date_cb_3);

        tvPhone = (TextView) findViewById(R.id.tv_phone);

        tvEmail = (TextView) findViewById(R.id.tv_email);

        llEmail = (LinearLayout) findViewById(R.id.ll_email);
        llEmail.setOnClickListener(this);

        llPhone = (LinearLayout) findViewById(R.id.ll_phone);
        llPhone.setOnClickListener(this);

        vision360CardView.setOnClickListener(this);

        TextView tvButtonNews = findViewById(R.id.button_news);
        tvButtonNews.setPaintFlags(tvButtonNews.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        policyController = new PolicyController(new OnConnectionResult() {
            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showLoading(false);
                            progressDialog.dismiss();

                            if (policyController.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {
                                if (policyController.getMessage().getMessage() != null) {
                                    tvMessageDialog.setText(policyController.getMessage().getMessage());
                                    dialogMessage.show();
                                } else {
                                    dialogMessageTwoButton.show();
                                }
                            }
                        } catch (Exception ex) {
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
                        try {
                            showLoading(false);
                            progressDialog.dismiss();

                            if (policyController.getTypeConnection() != 4) {

                                ll_container_description.removeAllViews();
                                for (int i = 0; i < PolicyModelV2.getInsurancesSel().getItens().length; i++) {
                                    View item_detail = inflater.inflate(R.layout.item_detail_policy, null);
                                    ll_container_description.addView(item_detail);

                                    //inflate from item
                                    TextView tvTitleDescription = (TextView) item_detail.findViewById(R.id.tv_title_description);

                                    TextView tvDescription = (TextView) item_detail.findViewById(R.id.tv_description);

                                    ImageView ivDescription = (ImageView) item_detail.findViewById(R.id.iv_description);


                                    tvDescription.setText(PolicyModelV2.getInsurancesSel().getItens()[i].getDescription());

                                    if (PolicyModelV2.getInsurancesSel().getBranch().equals(Config.auto)) {
                                        tvTitleDescription.setText(getString(R.string.car));
                                        ivDescription.setImageDrawable(getResources().getDrawable(R.drawable.bt_veichle_image));
                                        llCircle2.setVisibility(View.GONE);
                                        llCircle3.setVisibility(View.GONE);
                                    } else if (PolicyModelV2.getInsurancesSel().getBranch().equals(Config.life)) {
                                        tvTitleDescription.setText(getString(R.string.insured));
                                        ivDescription.setImageDrawable(getResources().getDrawable(R.drawable.icon_people));
                                        llCircle2.setVisibility(View.GONE);
                                        llCircle3.setVisibility(View.INVISIBLE);
                                    } else {
                                        tvTitleDescription.setText(getString(R.string.address));
                                        ivDescription.setImageDrawable(getResources().getDrawable(R.drawable.icon_home));
                                        llCircle2.setVisibility(View.GONE);
                                        llCircle3.setVisibility(View.GONE);
                                    }

                                }

                                tvPolicy.setText(PolicyModelV2.getInsurancesSel().getPolicy());


                                circularProgressView_1.setProgress(policyCalc.getPoliycDateDiff(policyController.getDetailPolicyBeans().getInsurance().getDataStartPolicy(),
                                        policyController.getDetailPolicyBeans().getInsurance().getDataEndPolicy()));

                                tvDateCb1.setText(policyCalc.getDay());

                                try {
                                    if (Integer.parseInt(policyCalc.getDay()) > 1) {
                                        tvTitleCircle.setText(getString(R.string.missing));
                                    } else {
                                        tvTitleCircle.setText(getString(R.string.missing_2));
                                    }
                                } catch (Exception ex) {
                                    if (tvTitleCircle != null) {
                                        tvTitleCircle.setText(getString(R.string.missing));
                                    }
                                }

                                if (PolicyModelV2.getInsurancesSel().getBranch().equals(Config.auto)) {
                                    circularProgressView_2.setProgress(policyCalc.getIPVA(policyController.licensePlate()));
                                    tvDateCb2.setText(policyCalc.getDay());

                                    circularProgressView_3.setProgress(policyCalc.getLinc(policyController.licensePlate()));
                                    tvDateCb3.setText(policyCalc.getDay());
                                }

                                tvPhone.setText(policyController.getDetailPolicyBeans().getInsurance().getBroker().getPhone());

                                tvEmail.setText(policyController.getDetailPolicyBeans().getInsurance().getBroker().getEmail());

                                tvName.setText(policyController.getDetailPolicyBeans().getInsurance().getBroker().getDescription());

                                ll_container_payment.removeAllViews();

                                for (int j = 0; j < policyController.getDetailPolicyBeans().getPayment().length; j++) {
                                    View item_payment = inflater.inflate(R.layout.item_payment_policy, null);
                                    ll_container_payment.addView(item_payment);

                                    TextView tvTitle = (TextView) item_payment.findViewById(R.id.tv_titlePayment);

                                    TextView tvitlePayment = (TextView) item_payment.findViewById(R.id.tv_titlePayment);
                                    tvitlePayment.setText(getString(R.string.payment) + " " + (j + 1));
                                    //inflate from item
                                    TextView tvDatePayment = (TextView) item_payment.findViewById(R.id.tv_date);
                                    tvDatePayment.setTag("tvDatePayment" + j);

                                    TextView tvValuePayment = (TextView) item_payment.findViewById(R.id.tv_value);
                                    tvValuePayment.setTag("tvValuePayment" + j);

                                    ImageViewCustom ivIconPayment = (ImageViewCustom) item_payment.findViewById(R.id.iv_icon_payment);
                                    ivIconPayment.setTag("ivIconPayment" + j);

                                    ButtonViewCustom btViewPayment = (ButtonViewCustom) item_payment.findViewById(R.id.bt_view_payment);
                                    btViewPayment.setTag("ivViewPayment" + j);
                                    btViewPayment.setOnClickListener(DetailPolicy.this);

                                    ImageViewCustom ivIconExtends = (ImageViewCustom) item_payment.findViewById(R.id.iv_icon_extends);
                                    ivIconExtends.setTag("ivIconExtends" + j);

                                    TextView titlePayment = (TextView) item_payment.findViewById(R.id.tv_title_ipayment);

                                    tvDatePayment.setText(policyCalc.getDate(policyController.getDetailPolicyBeans().getPayment()[j].getDueDate(), DetailPolicy.this, 1));

                                    tvValuePayment.setText(policyCalc.getMoney(policyController.getDetailPolicyBeans().getPayment()[j].getValue(), DetailPolicy.this));


                                    switch (policyController.getDetailPolicyBeans().getPayment()[j].getShowComponent()) {
                                        default:
                                            ivIconExtends.setVisibility(View.INVISIBLE);
                                            break;
                                        case 1:
                                        case 4:
                                        case 7:
                                        case 3:
                                            ivIconExtends.setVisibility(View.VISIBLE);
                                            ivIconExtends.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_pay_now));
                                            break;
                                        case 2:
                                        case 5:
                                        case 6:
                                            ivIconExtends.setVisibility(View.VISIBLE);
                                            ivIconExtends.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_extends));
                                            break;
                                    }

                                /*
                                if (policyController.getDetailPolicyBeans().getPayment()[j].isCanExtend()) {
                                    ivIconExtends.setVisibility(View.VISIBLE);
                                } else {
                                    ivIconExtends.setVisibility(View.INVISIBLE);
                                }*/

                                    ivIconExtends.setOnClickListener(DetailPolicy.this);

                                    if (policyController.getDetailPolicyBeans().getPayment()[j].getInstallmentNumber() > 0 && policyController.getDetailPolicyBeans().getPayment()[j].getAmountOfInstallment() > 1) {
                                        String text = "<b>" + getString(R.string.item_parcels) + " " + policyController.getDetailPolicyBeans().getPayment()[j].getInstallmentNumber()
                                                + " " + getString(R.string.in) + " " + policyController.getDetailPolicyBeans().getPayment()[j].getAmountOfInstallment() + "</b>";

                                        tvTitle.setText(Html.fromHtml(text));
                                    } else {
                                        tvTitle.setText(getString(R.string.payment));
                                    }


                                    if (policyController.getDetailPolicyBeans().getPayment()[j].getNextValue() != null) {
                                        if (!policyController.getDetailPolicyBeans().getPayment()[j].getNextValue().equals("0.00") &&
                                                policyController.getDetailPolicyBeans().getPayment()[j].getNextDueDate() != null) {
                                            String text = getString(R.string.next_payment) + "<b>" + " " +
                                                    policyCalc.getDate(policyController.getDetailPolicyBeans().getPayment()[j].getNextDueDate(), DetailPolicy.this, 2) + "</b>" +
                                                    getString(R.string.next_payment_1) + " " + "<b>" + policyCalc.getMoney(policyController.getDetailPolicyBeans().getPayment()[j].getNextValue(), DetailPolicy.this) + "</b>";

                                            titlePayment.setText(Html.fromHtml(text));
                                        }
                                    }

                                    if (policyController.getDetailPolicyBeans().getPayment()[j].getStatus() == 1) {
                                        if (policyController.getDetailPolicyBeans().getPayment()[j].getNextValue() != null) {
                                            ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_success));
                                        } else {
                                            ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_finish));
                                            titlePayment.setText(Html.fromHtml(getString(R.string.parcel_finish)));
                                        }
                                    } else if (policyController.getDetailPolicyBeans().getPayment()[j].getStatus() == 2) {
                                        ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_late));
                                    } else if (policyController.getDetailPolicyBeans().getPayment()[j].getStatus() == 3) {
                                        ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.processamento));
                                    } else if (policyController.getDetailPolicyBeans().getPayment()[j].getStatus() == 4) {
                                        ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.analise));

                                    } else {
                                        ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_pending));
                                    }


                                }


                                llCircle2.setVisibility(View.INVISIBLE);
                                llCircle3.setVisibility(View.INVISIBLE);
                                // homeOnController.getPolicyBeans().getClaim().setClaimType(50);

                                //String coverages = createInsuranceCoverages(policyController.getDetailPolicyBeans().getInsuranceCoverages());

                            /*if(coverages.equals("")){
                                llCoverages.setVisibility(View.GONE);
                                llLineCoverages.setVisibility(View.GONE);
                            } else {
                                tvCoverages.setText(coverages);
                            }*/

                                listVision360Controller = new ListVision360Controller(new OnConnectionResult() {
                                    @Override
                                    public void onError() {
                                        try{
                                            vision360CardView.setVisibility(View.INVISIBLE);
                                        }catch (Exception ex){
                                            //Log.i(Config.TAG, ex.toString());
                                        }
                                    }

                                    @Override
                                    public void onSucess() {
                                        try{
                                            if(listVision360Controller.getListVision360Beans() != null
                                                    && listVision360Controller.getListVision360Beans().isPossuiEvento()){

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        vision360CardView.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        vision360CardView.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                            }
                                        }catch (Exception ex){
                                            //Log.i(Config.TAG, ex.toString());
                                        }
                                    }
                                });

                                listVision360Controller.checkContentEvent(DetailPolicy.this, policyController.getDetailPolicyBeans().getInsurance().getPolicy());

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });

        svContent = (ScrollView) findViewById(R.id.sv_content);

        llProgress = (LinearLayout) findViewById(R.id.ll_loading);

        btPolicyMore = (ButtonViewCustom) findViewById(R.id.bt_policy_more);
        btPolicyMore.setOnClickListener(this);

        llCircle2.setVisibility(View.INVISIBLE);
        llCircle3.setVisibility(View.INVISIBLE);

        configDialog();

        showLoading(true);
        policyController.getDetailPolicy(this);


        IntentFilter filter = new IntentFilter();

        filter.addAction("br.com.libertyseguros.reloadScreen");

        final Context currentContext = this;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                policyController.getDetailPolicy(currentContext);
            }
        };

        registerReceiver(broadcastReceiver, filter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * Config Dialogs
     */
    private void configDialog() {

        createDialogProgress();

        dialogSecondCopyPolicy = new Dialog(this, R.style.AppThemeDialog);
        dialogSecondCopyPolicy.setContentView(R.layout.dialog_second_copy_policy);
        dialogSecondCopyPolicy.setCancelable(true);

        Button btShare = (Button) dialogSecondCopyPolicy.findViewById(R.id.bt_share);
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                shareSecondCopyPolicy = true;
                dialogSecondCopyPolicy.dismiss();
                policyController.startOpenShareSecondCopyPolicy(DetailPolicy.this, shareSecondCopyPolicy);
            }
        });

        Button btDownload = (Button) dialogSecondCopyPolicy.findViewById(R.id.bt_download);
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                shareSecondCopyPolicy = false;
                dialogSecondCopyPolicy.dismiss();
                policyController.startOpenShareSecondCopyPolicy(DetailPolicy.this, shareSecondCopyPolicy);
            }
        });

        dialogMessage = new Dialog(this, R.style.AppThemeDialog);
        dialogMessage.setContentView(R.layout.dialog_message);
        dialogMessage.setCancelable(false);


        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();

                if (policyController.getTypeConnection() == 3) {
                    finish();
                }
            }
        });

        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);
        dialogMessageTwoButton.setCancelable(false);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();

                if (policyController.getTypeConnection() == 3) {
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

                switch (policyController.getTypeConnection()) {
                    case 3:
                        policyController.getListPolicy(DetailPolicy.this, true);
                        break;
                    case 4:
                        policyController.getSecondCopyPolicy(DetailPolicy.this, shareSecondCopyPolicy);
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
                    svContent.setVisibility(View.GONE);
                } else {
                    llProgress.setVisibility(View.GONE);
                    svContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_policy_more:
                policyController.openMorePolicy(DetailPolicy.this);
                break;
            case R.id.ll_phone:
                policyController.openCall(DetailPolicy.this);
                break;
            case R.id.ll_email:
                policyController.sendEmail(DetailPolicy.this);
                break;
            case R.id.bt_view_payment:
                int indexIssuance = Integer.parseInt(String.valueOf(view.getTag()).replace("ivViewPayment", ""));
                policyController.openParcels(DetailPolicy.this, indexIssuance);
                break;
            case R.id.card_vision:
                policyController.openListVision(DetailPolicy.this);
                break;
             case R.id.iv_icon_extends:
                indexPayment = Integer.parseInt(String.valueOf(view.getTag()).replace("ivIconExtends", ""));


                parcelsController = new ParcelsController(new OnConnectionResult() {
                    @Override
                    public void onError() {
                        showLoading(false);

                        if (parcelsController.getTypeError() == 1) {
                            tvMessageDialog.setText(getString(R.string.message_error_server));
                            dialogMessage.show();
                        } else {
                            tvMessageDialog.setText(parcelsController.getMessage().getMessage());
                            dialogMessage.show();
                        }

                    }

                    @Override
                    public void onSucess() {
                        try {
                            showLoading(false);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ManagerDialogCodeBar managerDialogCodeBar = new ManagerDialogCodeBar(DetailPolicy.this);
                                    managerDialogCodeBar.createDialog(parcelsController.getBarCodeBeans());
                                }
                            });

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, policyController.getDetailPolicyBeans().getInsurance().getPolicy(),
                        policyController.getDetailPolicyBeans().getInsurance().getContract(),
                        policyController.getDetailPolicyBeans().getInsurance().getCiaCode(),
                        policyController.getDetailPolicyBeans().getInsurance().getIssuances()[indexPayment] + "");


                policyController.openExtend(this, indexPayment, this);

                break;
            case R.id.bt_coverage:
                policyController.openInsuranceCoverage(this);
                break;
            case R.id.bt_second_copy_policy:
                if (ContextCompat.checkSelfPermission(DetailPolicy.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(DetailPolicy.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(DetailPolicy.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                } else {
                    view.getBackground().clearColorFilter();
                    dialogSecondCopyPolicy.show();
                }


                break;

        }
    }


    public String createInsuranceCoverages(ArrayList<InsuranceCoverages> insuranceCoverages) {
        String coverages = "";

        try {

            if (insuranceCoverages.size() != 0) {
                if (insuranceCoverages.size() == 1) {
                    String text = insuranceCoverages.get(0).getDescription().toString().toLowerCase();
                    coverages = text.substring(0, 1).toUpperCase().concat(text.substring(1));
                } else if (insuranceCoverages.size() == 2) {
                    String text1 = insuranceCoverages.get(0).getDescription().toString().toLowerCase();
                    coverages = text1.substring(0, 1).toUpperCase().concat(text1.substring(1));

                    String text2 = insuranceCoverages.get(1).toString().toLowerCase();
                    coverages += " " + " " + text2.substring(0, 1).toUpperCase().concat(text2.substring(1));

                } else {
                    for (int ind = 0; ind < insuranceCoverages.size(); ind++) {
                        int count = ind + 2;

                        String text = insuranceCoverages.get(ind).getDescription().toString().toLowerCase();

                        text = text.substring(0, 1).toUpperCase().concat(text.substring(1));

                        if (count == insuranceCoverages.size()) {
                            coverages += "" + text + " " + getString(R.string.and) + " ";

                        } else if (count > insuranceCoverages.size()) {
                            coverages += "" + text;
                        } else if (ind == 0) {
                            coverages += text;
                        } else {
                            coverages += " • " + text;
                        }
                    }
                }

                if (!coverages.equals("")) {
                    //coverages += ".";
                }
            }
        } catch (Exception ex) {
            coverages = "";
        }

        //Log.i(Config.TAG, "Cobertura: " + coverages);

        return coverages;
    }

    public void onBarCode(int parcelNumber) {
        showLoading(true);
        parcelsController.getCod(DetailPolicy.this, parcelNumber);
    }

    private void createDialogProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        String msg = getString(R.string.loading_download_pdf);
        progressDialog.setMessage(msg);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialogSecondCopyPolicy.show();
                }
                return;
            }

        }
    }
}
