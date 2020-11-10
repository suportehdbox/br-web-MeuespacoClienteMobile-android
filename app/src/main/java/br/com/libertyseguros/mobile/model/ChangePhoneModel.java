package br.com.libertyseguros.mobile.model;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.InfoPhoneBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.util.ValidCNPJ;
import br.com.libertyseguros.mobile.util.ValidCPF;
import br.com.libertyseguros.mobile.util.ValidEmail;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePhoneModel extends BaseModel {


    public static final int SEND_PHONE = 2;

    public static final int GET_PHONE = 1;

    private int typeConnection;

    private static boolean closeScreen;

    private Context context;

    private Connection conn;

    private OnConnectionResult onConnectionResult;

    private int typeError;

    private MessageBeans message;

    private LoginBeans loginBeans;

    private InfoPhoneBeans infoPhoneBeans;


    public ChangePhoneModel(Context context, OnConnectionResult onConnectionResult) {
        this.onConnectionResult = onConnectionResult;

        loginBeans = infoUser.getUserInfo(context);
    }

    /**
     * Get Phone Info
     * @param ctx
     */
    public void getPhoneInfo(Context ctx) {

        try {
            String param = "";

            context = ctx;

            typeConnection = GET_PHONE;

            conn = new Connection(context);

            createConnection();


            param = "";

            conn.startConnection("Acesso/GetPhone", param, 1, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Send phone update
     *
     * @param ctx
     * @param phone
     * @param ramal
     * @param phoneMobile
     */
    public void sendPhone(Context ctx, String phone, String ramal, String phoneMobile) {
        String param = "";

        String phoneCustom[] = new String[2];
        String phoneMobileCustom[] = new String[2];
        String ramalFim = ramal.trim();
        if (phone.length() == 0) {
            phoneCustom[0] = "";
            phoneCustom[1] = "";
        } else {


            phoneCustom = phone.split("\\)");

            phoneCustom[0] = phoneCustom[0].replace("(", "");
            phoneCustom[1] = phoneCustom[1].replace("-", "");
        }

        if (phoneMobile.length() == 0) {
            phoneMobileCustom[0] = "";
            phoneMobileCustom[1] = "";
        } else {


            phoneMobileCustom = phoneMobile.split("\\)");
            phoneMobileCustom[0] = phoneMobileCustom[0].replace("(", "");
            phoneMobileCustom[1] = phoneMobileCustom[1].replace("-", "");
        }

        if(ramalFim.equalsIgnoreCase("")){
            ramalFim = "9999";
        }

        try {
            context = ctx;

            typeConnection = SEND_PHONE;

            conn = new Connection(context);

            createConnection();


            param = "DDDFoneResidencial=" + URLEncoder.encode(phoneCustom[0].trim(), "UTF-8") + "&FoneResidencial=" + URLEncoder.encode(phoneCustom[1].trim(), "UTF-8") +
                    "&RamalFoneResidencial=" + URLEncoder.encode(ramalFim, "UTF-8") + "&DDDFoneCelular=" + URLEncoder.encode(phoneMobileCustom[0].trim(), "UTF-8") +
                    "&FoneCelular=" + URLEncoder.encode(phoneMobileCustom[1].trim() + "", "UTF-8") +
                    "&codigoCif=" + URLEncoder.encode(infoPhoneBeans.getCifCode() + "", "UTF-8") +
                    "&CodigoFoneResidencial=" + URLEncoder.encode(infoPhoneBeans.getHomePhoneCode(), "UTF-8") +
                    "&CodigoFoneCelular=" + URLEncoder.encode(infoPhoneBeans.getCelPhoneCode(), "UTF-8");

            conn.startConnection("Acesso/Phone", param, 1, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Valid Fields
     *
     * @param context
     * @param phone
     * @param mobilePhone
     * @return
     */
    public String[] validFields(Context context, String phone, String mobilePhone) {

        String msg[] = new String[2];

        boolean isPhone = true;

        if (phone.trim().length() < 1) {
            msg[0] = context.getString(R.string.message_empty_phone);
            isPhone = false;
        } else if (phone.length() < 14) {
            msg[0] = context.getString(R.string.message_error_phone);
            isPhone = false;
        } else {
            msg[0] = "";
        }


        if (mobilePhone.trim().length() < 1) {
            msg[1] = context.getString(R.string.message_empty_phone);
        } else if (mobilePhone.length() < 14) {
            msg[1] = context.getString(R.string.message_error_phone);
        } else {
            msg[1] = "";
        }

        return msg;
    }

    /**
     * Boolean close Screen
     *
     * @return
     */
    public static boolean isCloseScreen() {
        return closeScreen;
    }

    /**
     * Get boolean Close Screen
     *
     * @param closeScreen
     */
    public static void setCloseScreen(boolean closeScreen) {
        ChangePhoneModel.closeScreen = closeScreen;
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
                //Log.i(Config.TAG, "ChangeEmail: " + result);
                Gson gson = new Gson();

                try {

                    switch(typeConnection){
                        case GET_PHONE:
                            infoPhoneBeans = null;
                            infoPhoneBeans = gson.fromJson(result, InfoPhoneBeans.class);

                            if(infoPhoneBeans != null){
                                infoPhoneBeans.setHomePhone(infoPhoneBeans.getDddHomePhone() + infoPhoneBeans.getHomePhone());
                                infoPhoneBeans.setCelPhone(infoPhoneBeans.getDddCelPhone()  + infoPhoneBeans.getCelPhone());
                            } else {
                                infoPhoneBeans = new InfoPhoneBeans();
                            }

                            if(infoPhoneBeans.getBranchHomePhone().equalsIgnoreCase("9999")){
                                infoPhoneBeans.setBranchHomePhone("");
                            }

                            onConnectionResult.onSucess();

                            break;
                        case SEND_PHONE:
                            if (result.equals("")) {
                                onConnectionResult.onSucess();
                            } else {

                                message = gson.fromJson(result, MessageBeans.class);
                                typeError = 2;
                                onConnectionResult.onError();
                            }
                            break;
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    onConnectionResult.onError();
                }
            }
        });
    }


    /**
     * Get Type Error
     *
     * @return
     */
    public int getTypeError() {
        return typeError;
    }

    /**
     * Set Type Error
     *
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.typeError = typeError;
    }

    /**
     * Get Message
     *
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Set Message
     *
     * @param message
     */
    public void setMessage(MessageBeans message) {
        this.message = message;
    }


    /**
     * Get InfoUser
     *
     * @return
     */
    public LoginBeans getInfoUser() {
        return loginBeans;
    }

    /**
     * Get Image User
     *
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser) {
        return infoUser.getImageUser(context, ivImageUser);
    }

    /**
     * Get type connection
     * @return
     */
    public int getTypeConnection(){
        return  typeConnection;
    }

    public InfoPhoneBeans getInfoPhoneBeans() {
        return infoPhoneBeans;
    }

    public void setInfoPhoneBeans(InfoPhoneBeans infoPhoneBeans) {
        this.infoPhoneBeans = infoPhoneBeans;
    }
}
