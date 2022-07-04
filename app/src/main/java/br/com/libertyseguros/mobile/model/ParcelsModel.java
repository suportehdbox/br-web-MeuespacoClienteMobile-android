package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.beans.BarCodeBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.ParcelsBeans;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class ParcelsModel extends BaseModel {
    private Activity context;

    private Connection conn;

    private int typeError;

    private int typeConnection;

    private final OnConnectionResult onConnectionResult;

    private MessageBeans message;

    private final Gson gson;

    private final String contract;

    private final String ciaCode;

    private final String issuance;

    private ParcelsBeans installments;

    private BarCodeBeans barCodeBeans;

    public ParcelsModel(OnConnectionResult onConnectionResult, String numberPolicy, String contract, String ciaCode, String issuance) {
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
         new InfoUser();
        this.contract = contract;
        this.ciaCode = ciaCode;
        this.issuance = issuance;
    }

    /**
     * Get Parcels Beans
     *
     */
    public ParcelsBeans getInstallments() {
        return installments;
    }


    /**
     * Get Cod
     *
     */
    public void getCodNoListParcel(Context ctx, int installment) {

        typeConnection = 2;

        context = (Activity) ctx;

        conn = new Connection(context);

        message = null;

        createConnection();

        String param = "";

        try {
            param = "?Contract=" + URLEncoder.encode(contract, "UTF-8") + "&Issuance[]=" + URLEncoder.encode(issuance, "UTF-8") + "&Installment[]=" + URLEncoder.encode(installment + "", "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        conn.startConnectionV2("Segurado/Seguro/GetDigitableLine", param, 2, true);
    }

    /**
     * Get Cod
     *
     */
    public void getCod(Context ctx, int parcelNumber) {

        typeConnection = 2;

        context = (Activity) ctx;

        conn = new Connection(context);

        message = null;

        createConnection();

        String param = "";

        try {
            param = "?Contract=" + URLEncoder.encode(contract, "UTF-8") + "&Issuance[]=" + URLEncoder.encode(issuance, "UTF-8") + "&Installment[]=" + URLEncoder.encode(parcelNumber + "", "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        conn.startConnectionV2("Segurado/Seguro/GetDigitableLine", param, 2, true);
    }

    /**
     * Get Parcels
     *
     */
    public void getParcels(Context ctx) {
        typeConnection = 1;

        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        String param = "";

        message = null;

        try {
            param = "?CiaCode=" + URLEncoder.encode(ciaCode, "UTF-8") + "&Contract=" + URLEncoder.encode(contract, "UTF-8") + "&Issuances[]=" + URLEncoder.encode(issuance, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        conn.startConnectionV2("Segurado/Seguro/Pagamento", param, 2, true);
    }

    /**
     * Create
     */
    private void createConnection() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try {
                    typeError = 1;
                    onConnectionResult.onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "ParcelModel: " + result);

                if (typeConnection == 1) {
                    try {
                        if (result.contains("installments")) {

                            JsonElement jsonElementParcel = new JsonParser().parse(result);
                            JsonObject jsonParcel = jsonElementParcel.getAsJsonObject();
                            JsonArray issuances = jsonParcel.getAsJsonArray("issuances");


                            installments = gson.fromJson(issuances.get(0).toString(), ParcelsBeans.class);
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
                } else if (typeConnection == 2) {
                    try {
                        if (!result.contains("installments")) {
                            barCodeBeans = gson.fromJson(result, BarCodeBeans.class);
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

            }
        });
    }


    /**
     * Get message error
     *
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Get type error connection
     *
     */
    public int getTypeError() {
        return typeError;
    }

    /**
     * get Type Connection
     *
     */
    public int getTypeConnection() {
        return typeConnection;
    }

    /**
     * Set Type Connection
     *
     */
    public void setTypeConnection(int typeConnection) {
        this.typeConnection = typeConnection;
    }

    public BarCodeBeans getBarCodeBeans() {
        return barCodeBeans;
    }

}
