package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.PaymentAccessBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.PaymentPriceBeans;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class PaymentModel extends BaseModel{

    public static Activity activityBefore;

    private Activity context;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private LoginBeans lb;

    private MessageBeans message;

    private Gson gson;

    private InfoUser infoUser;


    private PaymentAccessBeans clubBeans;

    //private String url = "http://libertyseguros.homolog.clubeben.proxy.media/auth/libertyseguros";
   // private String url = "http://libertyseguros.clubeben.com.br/auth/libertyseguros";

    private String url;
    private String postData;


    /**
     * Get Session Id
     Data
     * @return
     */
    public String getSessionId() {
        return postData;
    }


    /**
     * Get Url
     * @return
     */
    public String getUrl(Context context) {


        if(BuildConfig.prod){
            url = context.getString(R.string.url_club_prod);
        } else {
            url = context.getString(R.string.url_club_act);
        }

        return url;
    }



    public PaymentModel(Context ctx, OnConnectionResult onConnectionResult) {
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        infoUser = new InfoUser();
        this.context = (Activity) ctx;
        lb = infoUser.getUserInfo(ctx);

    }



    /**
     * Get Club info
     */
    public void getPaymentAccess(int installmentNumber, String contract, int issuance, String ciaCode, int issuingAgency){


        conn = new Connection(context);

        createConnection();

        String param = "";

        try{
            param = "ciaCode="+ciaCode+"&contract="+contract+"&installmentNumber="+installmentNumber+"&issuance="+issuance+"&issuingAgency="+issuingAgency;
        } catch(Exception ex){
            ex.printStackTrace();
        }


        conn.startConnection("Segurado/Seguro/PagamentoOnLine", param, 1, true);
    }

    /**
     * Get price payment
     */
    public void getPricePayment(int installmentNumber, String contract, int issuance, int showComponent, int issuingAgency, final OnPaymentPriceListener listenerPrice){

        if(showComponent < 3 || showComponent > 5){
            listenerPrice.OnPaymentPriceError("");
            return;
        }

        final Connection newConn = new Connection(context);

        newConn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try{
                    typeError = 1;
                    onConnectionResult.onError();
                }catch(Exception ex){
                    ex.printStackTrace();
                }


            }

            @Override
            public void onSucess(String result) {

                try {
                    if(result.contains("sucesso")){
                        PaymentPriceBeans beans = gson.fromJson(result, PaymentPriceBeans.class);

                        listenerPrice.OnPaymentValue(beans);
                    } else {
                        message = gson.fromJson(result, MessageBeans.class);
                        listenerPrice.OnPaymentPriceError(message.getMessage());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    message = gson.fromJson(result, MessageBeans.class);
                    listenerPrice.OnPaymentPriceError(message.getMessage());
                }


            }
        });

        String param = "";

        try{
            param = "?ShowComponent="+showComponent+"&contract="+contract+"&Installment="+installmentNumber+"&issuance="+issuance+"&issuingAgency="+issuingAgency;
        } catch(Exception ex){
            ex.printStackTrace();
        }


        newConn.startConnection("Segurado/Seguro/Pagamento/Value", param, 2, true);
    }

    /**
     * Create
     */
    private void createConnection() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try{
                    typeError = 1;
                    onConnectionResult.onError();
                }catch(Exception ex){
                    ex.printStackTrace();
                }


            }

            @Override
            public void onSucess(String result) {

                try {
                    if(result.contains("sessionId")){
                        clubBeans = gson.fromJson(result, PaymentAccessBeans.class);
                        postData = clubBeans.getSessionId();
                        onConnectionResult.onSucess();
                    } else {
                        message = gson.fromJson(result, MessageBeans.class);
                        typeError = 2;
                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    message = gson.fromJson(result, MessageBeans.class);
                    typeError = 2;
                    onConnectionResult.onError();
                }


            }
        });
    }





    /**
     * Get message error
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return typeError;
    }





}

