package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.ExtendsBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.TicketBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class ExtendsModel extends BaseModel {
    private Activity context;

    private Connection conn;

    private int typeError;

    private int typeConnection;

    private OnConnectionResult onConnectionResult;

    private LoginBeans loginBeans;

    private MessageBeans message;

    private Gson gson;

    private InfoUser infoUser;

    private ExtendsBeans extendsBeans;

    private TicketBeans ticketBeans;

    private String contract;

    private String issuance;

    private String installment;

    private String ciaCode;

    private String cliCode;

    private boolean simulate;

    private boolean payment;

    private Toast toast;

    public ExtendsModel(OnConnectionResult onConnectionResult, String contract, String issuance, String installment, String ciaCode, String cliCode, boolean payment) {
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        infoUser = new InfoUser();
        this.contract = contract;
        this.issuance = issuance;
        this.installment = installment;
        this.ciaCode = ciaCode;
        this.cliCode = cliCode;
        this.payment = payment;
        simulate = true;

    }


    /**
     * Simulate Extend
     *
     * @param ctx
     */
    public void getSimulateExtend(Context ctx) {
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        String param = "";


        String method = "";

        if (simulate) {
            try {
                // param = "Contract=" +  URLEncoder.encode("60737336", "UTF-8") + "&Issuance=" +  URLEncoder.encode("1", "UTF-8") + "&Installment=" + "4" + "&CiaCode=60248031"  + "&simularboleto=S";
                param = "Contract=" + URLEncoder.encode(contract, "UTF-8") +
                        "&Issuance=" + URLEncoder.encode(issuance, "UTF-8") +
                        "&Installment=" + URLEncoder.encode(installment, "UTF-8") +
                        "&CiaCode=" + URLEncoder.encode(ciaCode, "UTF-8") +
                        "&ClientCode=" + URLEncoder.encode(cliCode, "UTF-8") +
                        "&simularboleto=" + URLEncoder.encode(payment + "", "UTF-8");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            typeConnection = 1;
            method = "Segurado/Seguro/SimulaProrrogarParcela/";
        } else {

            try {
                //param = "Contract=" +  URLEncoder.encode("60737336", "UTF-8") + "&Issuance=" +  URLEncoder.encode("1", "UTF-8") + "&Installment=" + "4" + "&CiaCode=60248031"  + "&simularboleto=S";
                param = "contract=" + URLEncoder.encode(contract, "UTF-8") + "&issuance=" + URLEncoder.encode(issuance, "UTF-8") + "&installment=" + URLEncoder.encode(installment, "UTF-8") + "&CiaCode=" + URLEncoder.encode(ciaCode, "UTF-8") + "&ClientCode=" + URLEncoder.encode(cliCode, "UTF-8")
                        + "&simularboleto=" + URLEncoder.encode(payment + "", "UTF-8")
                        + "&NovoVencimento=" + URLEncoder.encode(getDate(extendsBeans.getNovoVencimento(), ctx) + "", "UTF-8")
                        + "&NovoValor=" + URLEncoder.encode(getMoney(extendsBeans.getNovoValor().replace("R$", ""), ctx).replace("R$", "") + "", "UTF-8");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            typeConnection = 2;
            method = "Segurado/Seguro/ProrrogarParcela/";
        }


        conn.startConnection(method, param, 1, true);
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
                //Log.i(Config.TAG, "Extend Model: " + result);

                if (typeConnection == 1) {
                    try {
                        if (result.contains("novoVencimento")) {
                            simulate = false;
                            extendsBeans = gson.fromJson(result, ExtendsBeans.class);
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
                } else {
                    try {
                        if (result.contains("idDocumentoCobranca")) {
                            ticketBeans = gson.fromJson(result, TicketBeans.class);
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
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Get type error connection
     *
     * @return
     */
    public int getTypeError() {
        return typeError;
    }

    /**
     * Get Type connection
     *
     * @return
     */
    public int getTypeConnection() {
        return typeConnection;
    }

    /**
     * Get Extend
     *
     * @return
     */
    public ExtendsBeans getExtendsBeans() {
        return extendsBeans;
    }


    /**
     * @param date
     * @return
     */
    public String getDate(String date, Context context) {
        String value = "";

        Locale locale = new Locale("pt", "BR");

        date = date.replace("T", " ");
        date = date.replace("Z", " ");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateSdf = null;
        try {
            dateSdf = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        value = formatter.format(dateSdf);

        return value;

    }

    /**
     * Get Money
     *
     * @param money
     * @param context
     * @return
     */
    public String getMoney(String money, Context context) {
        try {
            money = money.replace(".", ",");
            money = context.getString(R.string.money) + " " + money;
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return money;
    }

    /**
     * Open PDF
     *
     * @param url
     * @param context
     */
    public void openPDF(String url, Context context) {
        try {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setDataAndType(Uri.parse(url), "application/pdf");

            context.startActivity(it);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, context.getString(R.string.erro_open_pdf), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get TicketBeans
     *
     * @return
     */
    public TicketBeans getTicketBeans() {
        return ticketBeans;
    }

    /**
     * Copy Text Clipboard
     *
     * @param context
     */
    public void copyText(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getResources().getString(R.string.cod_number).replace(":", ""), ticketBeans.getLinhaDigitavel());
        clipboard.setPrimaryClip(clip);


        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, context.getResources().getString(R.string.clipboard), Toast.LENGTH_LONG);


        toast.show();
    }

}
