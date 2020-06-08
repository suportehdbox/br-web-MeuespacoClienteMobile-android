package br.com.libertyseguros.mobile.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.ExtendsController;
import br.com.libertyseguros.mobile.util.AnalyticsApplication;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;


public class ExtendPagament extends Activity implements View.OnClickListener{

    private ExtendsController extendsController;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private CardView cvContent;

    private boolean value;

    private Dialog dialogFinish;

    private Dialog dialogTerms;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private TextView tvDate;

    private TextView tvValue;

    private String contract;

    private String issuance;

    private String installment;

    private String ciaCode;

    private boolean payment;

    private ButtonViewCustom btOk;

    private CheckBox cbTerms;

    private TextView tvError;

    private AnalyticsApplication application;

    private FirebaseAnalytics mFirebaseAnalytics;

    public static boolean isExtends;

    public static String valueFinish;

    public static String dataFinish;

    public static String contractFinish;

    public static int numberFinish;

    private TextViewCustom tvTerms;

    private TextView tvDatePayment;

    private TextView tvValuePayment;

    private TextView tvCodNumber;

    private String cliCode;

    private CardView cardView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_8));

        setContentView(R.layout.activity_extends);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Bundle extras = getIntent().getExtras();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mFirebaseAnalytics = application.getFirebaseAnalitycs();

        if(extras != null) {
            contract =   extras.getString("contract");
            issuance =   extras.getString("issuance");
            installment = extras.getString("installment");
            ciaCode = extras.getString("ciaCode");
            cliCode = extras.getString("cliCode");
            payment = extras.getBoolean("payment");
        }

        cvContent = (CardView) findViewById(R.id.cv_content);

        tvMessageDialog = (TextView) findViewById(R.id.tv_message);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        tvDate = (TextView) findViewById(R.id.tv_date);

        tvValue = (TextView) findViewById(R.id.tv_value);

        tvError = (TextView) findViewById(R.id.tv_check_error);

        tvTerms = (TextViewCustom) findViewById(R.id.tv_terms);
        tvTerms.setOnClickListener(this);
        tvTerms.setPaintFlags(tvTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        extendsController = new ExtendsController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cvContent.setVisibility(View.INVISIBLE);
                                if (extendsController.getTypeError() == 1) {
                                    dialogMessageTwoButton.show();
                                } else {

                                    if (extendsController.getMessage() == null) {
                                        dialogMessageTwoButton.show();
                                    } else {
//To simulate uncomment below lines
//                                        dataFinish = "20/12/2016";
//                                        valueFinish = "R$ 195,00";
//                                        contractFinish = contract;
//                                        numberFinish = Integer.parseInt(installment);
//                                        isExtends = true;
                                        tvMessageDialog.setText(extendsController.getMessage().getMessage());
                                        dialogMessage.show();
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

            @Override
            public void onSucess() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showLoading(false);
                                if (extendsController.getTypeConnection() == 1) {

                                    tvValue.setText(extendsController.getMoney(extendsController.getExtendsBeans().getNovoValor(), ExtendPagament.this));
                                    tvDate.setText(extendsController.getDate(extendsController.getExtendsBeans().getNovoVencimento(), ExtendPagament.this));
                                    dataFinish = tvDate.getText().toString();
                                    valueFinish = tvValue.getText().toString();
                                    contractFinish = contract;
                                    numberFinish = Integer.parseInt(installment);
                                } else {
                                    cvContent.setVisibility(View.INVISIBLE);

                                    isExtends = true;

                                    tvValuePayment.setText(getResources().getString(R.string.next_payment_value_dialog) + " " + extendsController.getMoney(extendsController.getExtendsBeans().getNovoValor(), ExtendPagament.this));
                                    tvDatePayment.setText(getResources().getString(R.string.next_payment_dialog) + " " + extendsController.getDate(extendsController.getExtendsBeans().getNovoVencimento(), ExtendPagament.this));
                                    tvCodNumber.setText(extendsController.getTicketBeans().getLinhaDigitavel());

                                    dialogFinish.show();

                                    Bundle bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Retorno");
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Pagamento");
                                    bundle.putString(FirebaseAnalytics.Param.CONTENT, "Reprogramar Parcelas");

                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

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
        }, contract, issuance, installment, ciaCode, cliCode, payment);


        btOk = (ButtonViewCustom) findViewById(R.id.bt_ok);
        btOk.setVisibility(View.GONE);
        btOk.setOnClickListener(this);

        cbTerms  = (CheckBox) findViewById(R.id.cb_terms);
        cbTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btOk.setVisibility(View.VISIBLE);
                } else {
                    btOk.setVisibility(View.GONE);
                }
            }
        });


        configDialog();

        showLoading(true);
        extendsController.getSimulateExtend(this);
        //dialogTerms.show();
        //dialogFinish.show();
    }

    /**
     * Config Dialogs
     */
    private void configDialog(){

        dialogFinish = new Dialog(this,R.style.AppThemeDialog);
        dialogFinish.setCancelable(true);

        dialogFinish.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        dialogFinish.setContentView(R.layout.dialog_extends);

        ImageView ivOk = (ImageView) dialogFinish.findViewById(R.id.iv_ok);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFinish.dismiss();
            }
        });

        ImageView ivCP = (ImageView) dialogFinish.findViewById(R.id.iv_cp);
        ivCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extendsController.copyText(ExtendPagament.this);
            }
        });

        tvDatePayment = (TextView)   dialogFinish.findViewById(R.id.tv_date_payment);
        tvValuePayment = (TextView)   dialogFinish.findViewById(R.id.tv_value_payment);
        tvCodNumber = (TextView)   dialogFinish.findViewById(R.id.tv_cod_number);

        tvCodNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //extendsController.copyText(ExtendPagament.this);
            }
        });

        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvTitle = (TextView) dialogMessage.findViewById(R.id.tv_title_message);
        tvTitle.setVisibility(View.VISIBLE);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();

                finish();
            }
        });

        tvOk.setText(getResources().getString(R.string.bt_Ok_3));


        dialogTerms = new Dialog(this,R.style.AppThemeDialog);
        dialogTerms.setCancelable(false);

        dialogTerms.setContentView(R.layout.dialog_message_terms);


        /*
        TextView tvMessageDialogterms = (TextView) dialogTerms.findViewById(R.id.tv_dialog_message);
        tvMessageDialogterms.setMovementMethod(LinkMovementMethod.getInstance());
        cardView = (CardView)  dialogTerms.findViewById(R.id.cv_dialog);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);

            }
        });

       /* SpannableString myString = new SpannableString( getResources().getString(R.string.terms_extends_message));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                cardView.setVisibility(View.VISIBLE);
            }

        };

        int startIndex = 177;
        int lastIndex = 186;

        myString.setSpan(clickableSpan,startIndex,lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        myString.setSpan(new UnderlineSpan(),startIndex,lastIndex,0);

        tvMessageDialogterms.setText(myString);
        tvMessageDialogterms.setHighlightColor(Color.TRANSPARENT);*/

        TextView tvOkTerms = (TextView) dialogTerms.findViewById(R.id.tv_ok);

        tvOkTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTerms.dismiss();
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
                cvContent.setVisibility(View.VISIBLE);
                extendsController.getSimulateExtend(ExtendPagament.this);

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
                    llContent.setVisibility(View.INVISIBLE);
                } else {
                    llLoading.setVisibility(View.INVISIBLE);
                    llContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bt_ok:
                if(cbTerms.isChecked()){
                    tvError.setVisibility(View.GONE);
                    showLoading(true);
                    extendsController.getSimulateExtend(ExtendPagament.this);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_terms:
                dialogTerms.show();
                break;
        }
    }


}