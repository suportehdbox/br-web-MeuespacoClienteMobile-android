package br.com.libertyseguros.mobile.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.InstallmentsBeans;
import br.com.libertyseguros.mobile.beans.PaymentPriceBeans;
import br.com.libertyseguros.mobile.beans.PolicyBeansV2;
import br.com.libertyseguros.mobile.libray.Security;
import br.com.libertyseguros.mobile.model.OnPaymentPriceListener;
import br.com.libertyseguros.mobile.model.PaymentModel;
import br.com.libertyseguros.mobile.model.PolicyModelV2;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

/**
 * TODO: document your custom view class.
 */
public class DialogPayments extends Dialog {

    private TextView title_view;
    private TextView payment_title1;
    private TextView payment_title2;
    private TextView payment_price1;
    private TextView payment_price2;
    private Button payment_button1;
    private Button payment_button2;
    private LinearLayout divisor;
    private LinearLayout linearLayoutButtons;
    private ProgressBar progress_bar;

    private PolicyBeansV2.InsurancesV2 currentInsurance;
    private InstallmentsBeans currentInstallment;
    private int currentIssuance;
    private OnBarCode barcode;

    private PaymentModel model;
    private Activity activity;

    public DialogPayments(Context context, PolicyBeansV2.InsurancesV2 insurance, InstallmentsBeans installmentsBeans,
                          int issuance, OnBarCode barCode) {
        super(context, R.style.AppThemeDialog);
        activity =  (Activity) context;
        this.setContentView(R.layout.dialog_payments);

        currentInsurance = insurance;
        currentInstallment = installmentsBeans;
        currentIssuance = issuance;

        if(currentInsurance == null){
            return;
        }
        Security security = new Security();
        if (security.isRootedDevice(context)){
            dismiss();
            return;
        }
        title_view = (TextView) findViewById(R.id.tv_title);
        title_view.setText(R.string.TituloPagarParcela);
        payment_title1 = (TextView) findViewById(R.id.txt_payment1);
        payment_price1 = (TextView) findViewById(R.id.txt_payment_price1);
        payment_price2 = (TextView) findViewById(R.id.txt_payment_price2);

        payment_title2 = (TextView) findViewById(R.id.txt_payment2);
        payment_button1 = (Button) findViewById(R.id.bt_payment1);
        payment_button2 = (Button) findViewById(R.id.bt_payment2);
        divisor = (LinearLayout) findViewById(R.id.line_divisor);
        linearLayoutButtons = (LinearLayout) findViewById(R.id.ll_buttons);
        progress_bar  = (ProgressBar) findViewById(R.id.progress_bar);

        this.barcode = barCode;

        switch (currentInstallment.getShowComponent()) {
            case 1:
                //Ticket = 1 à Código de barras
                payment_title1.setText(R.string.SegundaViaBoleto);
                payment_button1.setText(R.string.ButtonPagar);
                break;
            case 2:
                //Reprogramming = 2 à Reprograma
                payment_title1.setText(R.string.ProrrogarParcela);
                payment_button1.setText(R.string.ButtonPagar);
                break;
            case 3:
                //  OnlinePayment = 3 à Pagamento online
                payment_title1.setText(R.string.PagamentoOnline);
                payment_button1.setText(R.string.ButtonPagar);
                break;
            case 4:
                //TicketAndOnlinePayment = 4 à Código de barras e Pagamento online
                payment_title1.setText(R.string.SegundaViaBoleto);
                payment_title2.setText(R.string.PagamentoOnline);
                payment_button1.setText(R.string.ButtonPagar);
                payment_button2.setText(R.string.ButtonPagar);
                break;
            case 5:
                //ReprogrammingAndOnlinePayment = 5 à Reprograma e Pagamento online
                payment_title1.setText(R.string.ProrrogarParcela);
                payment_title2.setText(R.string.PagamentoOnline);
                payment_button1.setText(R.string.ButtonPagar);
                payment_button2.setText(R.string.ButtonPagar);
                break;
            case 6:
                //ReprogrammingAndOnlineStatus = 6  à Reprograma e consulta pgt. online
                payment_title1.setText(R.string.ProrrogarParcela);
                payment_title2.setText(R.string.ConsultarPagamento);
                payment_button1.setText(R.string.ButtonPagar);
                payment_button2.setText(R.string.Consultar);
                break;
            case 7:
                //TicketAndOnlineStatus = 7 à Código de barras e consulta pgt. online
                payment_title1.setText(R.string.SegundaViaBoleto);
                payment_title2.setText(R.string.ConsultarPagamento);
                payment_button1.setText(R.string.ButtonPagar);
                payment_button2.setText(R.string.Consultar);
                break;
            case 8:
                //OnlineStatus = 8 à Consulta pagamento online
                payment_title1.setText(R.string.ConsultarPagamento);
                payment_button1.setText(R.string.Consultar);
                break;
            default:
                break;
        }

        if(currentInstallment.getShowComponent() < 4 || currentInstallment.getShowComponent() == 8){
            payment_button2.setVisibility(View.GONE);
            divisor.setVisibility(View.GONE);
            payment_title2.setVisibility(View.GONE);
        }


        payment_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (currentInstallment.getShowComponent()) {
                    case 1:
                        //Ticket = 1 à Código de barras
                        barcode.onBarCode(currentInstallment.getNumber());
                        dismiss();
                        break;
                    case 2:
                        //Reprogramming = 2 à Reprograma
                        openExtend();
                        break;
                    case 3:
                        //  OnlinePayment = 3 à Pagamento online
                        openWebView();
                        break;
                    case 4:
                        //TicketAndOnlinePayment = 4 à Código de barras e Pagamento online
                        barcode.onBarCode(currentInstallment.getNumber());
                        dismiss();
                        break;
                    case 5:
                        //ReprogrammingAndOnlinePayment = 5 à Reprograma e Pagamento online
                        openExtend();
                        break;
                    case 6:
                        //ReprogrammingAndOnlineStatus = 6  à Reprograma e consulta pgt. online
                        openExtend();
                        break;
                    case 7: //ticket online payment
                        barcode.onBarCode(currentInstallment.getNumber());
                        dismiss();
                        break;
                    case 8:
                        //OnlineStatus = 8 à Consulta pagamento online
                        openWebView();
                        break;
                    default:
                        break;
                }

            }
        });

        payment_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView();
            }
        });


        model = new PaymentModel(activity, new OnConnectionResult() {
            @Override
            public void onError() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                    }
                });

            }

            @Override
            public void onSucess() {
                openWebViewActivity();
            }
        });

        if(currentInstallment.getShowComponent() >= 3 || currentInstallment.getShowComponent() <= 5){
            showLoading();
            model.getPricePayment(currentInstallment.getNumber(), currentInsurance.getContract(), currentIssuance,
                    currentInstallment.getShowComponent(), currentInsurance.getIssuingAgency(), new OnPaymentPriceListener() {
                        @Override
                        public void OnPaymentValue(final PaymentPriceBeans price) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stopLoading();
                                    if(!price.getValue().isEmpty()){
                                        if(currentInstallment.getShowComponent() == 3){
                                            payment_price1.setText(price.getFormattedValue());
                                            payment_price1.setVisibility(View.VISIBLE);
                                        }else{
                                            payment_price2.setText(price.getFormattedValue());
                                            payment_price2.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            });

                        }
                        @Override
                        public void OnPaymentPriceError(String message) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stopLoading();

                                }
                            });
                        }
                    });
        }



    }



    private void showLoading(){
        linearLayoutButtons.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
        progress_bar.animate();
    }

    private void stopLoading(){
        linearLayoutButtons.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
    }

    private void openWebView(){
        showLoading();


        model.getPaymentAccess(currentInstallment.getNumber(), currentInsurance.getContract(), currentIssuance, currentInsurance.getCiaCode(), currentInsurance.getIssuingAgency());
    }
    /**
     * Open Extend Screen
     */
    private void openExtend(){

        showLoading();

        boolean payment = false;

        if(currentInstallment.getCodigoTipoModalidadeCobranca().toLowerCase().equals("fb")){
            payment = true;
        }

        dismiss();

        Intent it = new Intent(activity, ExtendPagament.class);
        it.putExtra("contract", currentInsurance.getContract() + "");
        it.putExtra("installment",  currentInstallment.getNumber() + "");
        it.putExtra("issuance", currentIssuance + "");
        it.putExtra("ciaCode", currentInsurance.getCiaCode() + "");
        it.putExtra("cliCode", currentInsurance.getCliCode() + "");
        it.putExtra("payment", payment);

        activity.startActivity(it);

        stopLoading();
    }


    private void openWebViewActivity(){


        dismiss();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopLoading();
            }
        });
        Intent it = new Intent(activity, PaymentWebView.class);
        it.putExtra("sessionId", model.getSessionId() + "");
        activity.startActivity(it);
    }


    @Override
    public void show() {
        Security security = new Security();

        if(currentInsurance == null){

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText (activity, R.string.error_dialog_payment, Toast.LENGTH_SHORT).show();
                }
            });
            //do nothing
        }else if (security.isRootedDevice(activity)) {
            Toast.makeText(activity, activity.getString(R.string.dispositivo_rooted), Toast.LENGTH_LONG).show();
        }else {
            super.show();
        }

    }
}
