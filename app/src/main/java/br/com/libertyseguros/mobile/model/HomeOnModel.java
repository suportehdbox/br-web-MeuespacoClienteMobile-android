package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.beans.HomeBeans;
import br.com.libertyseguros.mobile.beans.HomeBeansV2;
import br.com.libertyseguros.mobile.beans.HomePaymentsBeans;
import br.com.libertyseguros.mobile.beans.InstallmentsBeans;
import br.com.libertyseguros.mobile.beans.InsuranceStatusBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.PaymentBeans;
import br.com.libertyseguros.mobile.beans.PolicyBeansV2;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.DeviceVerificaionListener;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.libray.Security;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.Assistance;
import br.com.libertyseguros.mobile.view.DetailPolicy;
import br.com.libertyseguros.mobile.view.DialogPayments;
import br.com.libertyseguros.mobile.view.ExtendPagament;
import br.com.libertyseguros.mobile.view.HomeAssistanceWebView;
import br.com.libertyseguros.mobile.view.ListPolicy;
import br.com.libertyseguros.mobile.view.ListVehicleAccidentStatus;
import br.com.libertyseguros.mobile.view.ListVision360;
import br.com.libertyseguros.mobile.view.NovoClubeLiberty;
import br.com.libertyseguros.mobile.view.Workshop;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeOnModel extends BaseModel{
    private Activity context;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private MessageBeans message;

    private Gson gson;

    private InfoUser infoUser;

    private LoginBeans loginBeans;

    private HomeBeansV2 homeBeans;

    private int typeConnection;

    private int sizeListPolicy;

    public HomeOnModel(OnConnectionResult onConnectionResult, Context context){
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        infoUser = new InfoUser();
        loginBeans = infoUser.getUserInfo(context);
        sizeListPolicy = 0;
    }

    /**
     * Login Checks if user is logged in
     * @param ctx
     * @return
     */
    public boolean isLogin(Context ctx){
        context = (Activity) ctx;
        loginBeans = infoUser.getUserInfo(context);

        boolean value = false;

        if(loginBeans.getAccess_token() != null){
            value = true;
        }

        return value;
    }

    /**
     * Method Get List Policy
     * @param ctx
     */
    public void getListPolicy(Context ctx, boolean active){
        context = (Activity) ctx;

        conn = new Connection(context);

        typeConnection = 2;

        createConnection();

        String param = "?isActive=" + active;

        conn.startConnection("Segurado/Seguro", param, 2, true);
    }


    /**
     * Method Array salesman
     * @param ctx
     */
    public void getHome(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        typeConnection = 1;

        createConnection();

        String result = loadFile.loadPref(Config.TAG, context, Config.TAGHOMEON);



        if(result != null){
            if(result.equals("")){
                //Log.i(Config.TAG, "Info home on new login");
                createParams();
            } else {
                try {
                    String time = loadFile.loadPref(Config.TAG, context, Config.TAGHOMEONTIME);
                    long timeLong = Long.parseLong(time);

                    long timeDiff = System.currentTimeMillis() - timeLong;
                    timeDiff = - 99;

                    if (timeDiff >= -99) {
                        createParams();

                        //Log.i(Config.TAG, "Info home on cache > 1 day");


                    } else {
                        homeBeans = gson.fromJson(result, HomeBeansV2.class);

                        if (homeBeans.getInsurance().getBranch().equals(Config.auto)) {
                            Config.hasAutoPolicy = true;
                        }else{
                            Config.hasAutoPolicy = false;
                        }
                        if (homeBeans.getInsurance().isAllowPHS()) {
                            Config.hasHomeAssistance = true;
                        }else{
                            Config.hasHomeAssistance = false;
                        }


                        onConnectionResult.onSucess();

                        //Log.i(Config.TAG, "Info home on cache < 1 day");
                    }

                }  catch (Exception ex){
                    createParams();
                    //Log.i(Config.TAG, "Info home on error");

                }
            }

        } else {
            //Log.i(Config.TAG, "Info home on not found");
            createParams();
        }
    }

    public void validateDevice () {
        Security sec = new Security();
        sec.getDeviceVerification(context, loginBeans.getAccess_token(), new DeviceVerificaionListener() {
            @Override
            public void onVerificationComplete() {
                //Success
            }

            @Override
            public void onVerificationFail(@NotNull String message) {

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                context.finish();

            }
        });
    }

    /**
     * Method Get Payments Info
     * @param ctx
     */
    public void getPaymentsInfo(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        typeConnection = 3;

        createConnection();

        String param = "?Contract=" + homeBeans.getInsurance().getContract() + "&CiaCode=" + homeBeans.getInsurance().getCiaCode();

        for ( int issuance : homeBeans.getInsurance().getIssuances()) {
            param += "&Issuances[]="+issuance;

        }

        conn.startConnectionV2("Segurado/Seguro/DadosPagamentoSeguro", param, 2, true);
    }



    /**
     * Create Params connection
     */
    private void createParams(){
        String param = "";

        try{
            param = "?CpfCnpj=" + URLEncoder.encode(infoUser.getCpfCnpj(context), "UTF-8");
        }catch (Exception ex){
            ex.printStackTrace();
        }

       conn.startConnectionV3("Segurado/Home", param, 2, true);

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
                    if(!BuildConfig.prod) {
                        //Log.i(Config.TAG, "HomeOnModel - type Connection" + typeConnection + " : " + result);
                    }

                    if(typeConnection == 1) {
                    /*    result = "{\n" +
                                "                   \"policy\": \"3116516289\",\n" +
                                "                \"dataStartPolicy\": \"2015-12-14T02:00:00Z\",\n" +
                                "                \"dataEndPolicy\": \"2016-12-14T02:00:00Z\",\n" +
                                "                \"contract\": 60730240,\n" +
                                "                \"issuance\": 1,\n" +
                                "                \"ciaCode\": 1,\n" +
                                "                \"branch\": \"Auto\",\n" +
                                "                \"description\": \"CELTA SPIRIT  LT 1 0 MPFI 8V FLEXPOWER 5P  GAS Placa: SDG-6545\",\n" +
                                "                \"licensePlate\": \"SDG-6545\",\n" +
                                "                \"claim\": {\n" +
                                "                               \"number\": \"123456\",\n" +
                                "                               \"type\": \"\",\n" +
                                "                               \"status\": \"\",\n" +
                                "                               \"date\": \"2015-12-22T00:00:00\"\n" +
                                "                },\n" +
                                "                \"payment\": {\n" +
                                "                               \"canExtend\": true,\n" +
                                "                               \"dueDate\": \"2015-12-22T00:00:00\",\n" +
                                "                               \"nextDate\": \"2016-01-22T00:00:00\",\n" +
                                "                               \"nextValue\": 0,\n" +
                                "                               \"status\": 2,\n" +
                                "                               \"value\": 0\n" +
                                "                },\n" +
                                "                \"sucesso\": true,\n" +
                                "                \"message\": \"\",\n" +
                                "                \"rowsAffected\": 0\n" +
                                "}\n" +
                                " ";
*/
                        try {
                            if (result.contains("ciaCode")) {
                                homeBeans = gson.fromJson(result, HomeBeansV2.class);

                                if (homeBeans.getInsurance().getBranch().equals(Config.auto)) {
                                    Config.hasAutoPolicy = true;
                                }else{
                                    Config.hasAutoPolicy = false;
                                }

                                if (homeBeans.getInsurance().isAllowPHS()) {
                                    Config.hasHomeAssistance = true;
                                }else{
                                    Config.hasHomeAssistance = false;
                                }


                                loadFile.savePref(Config.TAGHOMEON, result, Config.TAG, context);
                                loadFile.savePref(Config.TAGHOMEONTIME, System.currentTimeMillis() + "", Config.TAG, context);









                                onConnectionResult.onSucess();
                            } else {
                                typeError = 2;
                                message = gson.fromJson(result, MessageBeans.class);
                                onConnectionResult.onError();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            onConnectionResult.onError();
                            message = gson.fromJson(result, MessageBeans.class);
                            typeError = 2;
                            onConnectionResult.onError();
                        }

                    } else if(typeConnection == 3) {
                        try{
                            HomePaymentsBeans payments = gson.fromJson(result, HomePaymentsBeans.class);
                            homeBeans.setPayment(payments.getPayment());
                            onConnectionResult.onSucess();
                        }catch(Exception ex){
                            if(onConnectionResult != null){
                                onConnectionResult.onError();
                            }
                        }


                    } else {
                        try {
                            PolicyBeansV2 policyBeans = gson.fromJson(result, PolicyBeansV2.class);

                            if(policyBeans.getInsurances().length == 1){
                                onConnectionResult.onError();
                            } else {
                                sizeListPolicy = policyBeans.getInsurances().length;
                                onConnectionResult.onSucess();
                            }
                        }catch(Exception ex){
                            if(onConnectionResult != null){
                                onConnectionResult.onError();
                            }
                        }

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
     * Get Size List Policy
     * @return
     */
    public int getSizeListPolicy() {
        return sizeListPolicy;
    }

    /**
     * Set List  Policy size
     * @param sizeListPolicy
     */
    public void setSizeListPolicy(int sizeListPolicy) {
        this.sizeListPolicy = sizeListPolicy;
    }

    /**
     * Get Connection Type
     * @return
     */
    public int getTypeConnection() {
        return typeConnection;
    }

    /**
     * Set Connection Type
     * @param typeConnection
     */
    public void setTypeConnection(int typeConnection) {
        this.typeConnection = typeConnection;
    }

    /**
     * Get type error connection
     * @return
     */

    public int getTypeError() {
        return typeError;
    }

    /**
     * Get InfoUser
     * @return
     */
    public LoginBeans getInfoUser(){
        return loginBeans;
    }


    /**
     * Get Image User
     * @param context
     * @param ivImageUser
     * @return
     */
    public boolean getImageUser(Context context, CircleImageView ivImageUser){
        return infoUser.getImageUser(context, ivImageUser);
    }

    /**
     * Open Assistence Screen
     */
    public void openAssistance(Context context){
        Intent it = null;
        if(Config.hasHomeAssistance && !Config.hasAutoPolicy ){
            it = new Intent(context, HomeAssistanceWebView.class);
        }else{
            it = new Intent(context, Assistance.class);
        }
        context.startActivity(it);
    }

    /**
     * Open Workshop Screen
     * @param context
     */
    public void openWorkShop(Context context){
        Intent it = new Intent(context, Workshop.class);
        context.startActivity(it);
    }

    /**
     * Open List Policy
     * @param context
     */
    public void openListVision(Context context){
        Intent it = new Intent(context, ListVision360.class);
        it.putExtra("policy", homeBeans.getInsurance().getPolicy());

        context.startActivity(it);
    }

    /**
     * Open List Policy
     * @param context
     */
    public void openListPolicy(Context context){
        Intent it = new Intent(context, ListPolicy.class);
        context.startActivity(it);
    }

    /**
     * Open Extend Screen
     * @param context
     */
    private DialogPayments dialogPayments;
    public void openExtend(Context context, int indexPayment, OnBarCode barCode){
//        Intent it = new Intent(context, ExtendPagament.class);
//        it.putExtra("contract", homeBeans.getInsurance().getContract() + "");
//        it.putExtra("installment", homeBeans.getPayment()[indexPayment].getInstallmentNumber() + "");
//        it.putExtra("issuance", homeBeans.getInsurance().getIssuances()[indexPayment] + "");
//        it.putExtra("ciaCode", homeBeans.getInsurance().getCiaCode() + "");
//        it.putExtra("cliCode", homeBeans.getInsurance().getCliCode() + "");
//
//        boolean payment = false;
//
//        if(homeBeans.getPayment()[indexPayment].getCodigoTipoModalidadeCobranca().equals("FB")){
//            payment = true;
//        }
//
//        it.putExtra("payment", payment);
//        context.startActivity(it);

        InstallmentsBeans installment = new InstallmentsBeans();
        installment.setCodigoTipoModalidadeCobranca(homeBeans.getPayment()[indexPayment].getCodigoTipoModalidadeCobranca());
        installment.setShowComponent(homeBeans.getPayment()[indexPayment].getShowComponent());
        installment.setCanExtend(homeBeans.getPayment()[indexPayment].isCanExtend());
        installment.setNumber(homeBeans.getPayment()[indexPayment].getInstallmentNumber());
        installment.setStatus(homeBeans.getPayment()[indexPayment].getStatus());
        installment.setValue(homeBeans.getPayment()[indexPayment].getValue());

        dialogPayments = new DialogPayments(context,homeBeans.getInsurance(), installment, homeBeans.getInsurance().getIssuances()[indexPayment], barCode);
        dialogPayments.show();
    }



    /**
     * Open Detail Screen
     * @param context
     */
    public  void openDetailList(Context context){


        PolicyBeansV2.InsurancesV2 insurances = new PolicyBeansV2().getInsurancesInstance();
        insurances = homeBeans.getInsurance();
//        PolicyBeans.Insurances[] insurancesNew = new PolicyBeans.Insurances[1];


//        insurances.setDescription(homeBeans.getInsurance().getItens()[0].getDescription());
//        insurances.setBranch(homeBeans.getInsurance().getBranch());
//        insurances.setCiaCode(homeBeans.getInsurance().getCiaCode());
//        insurances.setClaimBeans(homeBeans.getInsurance().getItens()[0].getClaim());
//        insurances.setContract(homeBeans.getInsurance().getContract());
//        insurances.setPolicy(homeBeans.getInsurance().getPolicy());

//        int issuance = 0;
//        try{
//            issuance = homeBeans.getInsurance().getIssuances()[0];
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//
//
//        insurances.setIssuance(issuance);
//
//        InsuranceStatusBeans insuranceStatusBeans = new InsuranceStatusBeans();
//        insuranceStatusBeans.setDataStartPolicy(homeBeans.getInsurance().getDataStartPolicy());
//        insuranceStatusBeans.setDataEndPolicy(homeBeans.getInsurance().getDataEndPolicy());
//        insuranceStatusBeans.setLicensePlate(homeBeans.getInsurance().getItens()[0].getLicensePlate());
//        insurances.setInsuranceStatus(insuranceStatusBeans);
//
//        insurancesNew[0] = insurances;
        PolicyModelV2.setInsurancesSel(insurances);

        Intent it = new Intent(context, DetailPolicy.class);
        context.startActivity(it);

    }

    /**
     * Get Policy Beans
     * @return
     */
    public HomeBeansV2 getPolicyBeans(){
        return homeBeans;
    }

    /**
     * Get License Plate
     * @return
     */
    public int licensePlate(){
        int licensePlate = 1;

        try{
            licensePlate = Integer.parseInt(
                    getPolicyBeans().getInsurance().getItens()[0].getLicensePlate().substring(
                            getPolicyBeans().getInsurance().getItens()[0].getLicensePlate().length() - 1));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //licensePlate = 0;
        return licensePlate;
    }

    /**
     * Open Club Screen
     * @param context
     */
    public void openClub(Context context){
        Intent it = new Intent(context, NovoClubeLiberty.class);
        context.startActivity(it);
    }

    /**
     * Open Vehicle Accident Screen
     * @param context
     */
    public void openVehicleAccidentStatus(Context context){
        Intent it = null;
        it = new Intent(context, ListVehicleAccidentStatus.class);
        context.startActivity(it);

    }


    /**
     * Show Popup Fingertipes
     * @param context
     * @return
     */
    public boolean showFingerprints(Context context){

        String isToken = loadFile.loadPref(Config.TAG, context, Config.TAGTOKEN);
        String isLoginUser = loadFile.loadPref(Config.TAG, context, Config.TAGLOGINUSER);
        if(isToken != null){
            if(isToken.equals("0")){
                return false;
            } else {
                if(isLoginUser.equals("1")){
                    return true;
                } else {
                    return false;
                }
            }
        }  else {
            return false;
        }
    }

//    public void setSalesForceSDK(final String policy, final String CPF, final String Ramo){
//        MarketingCloudSdk.requestSdk(new MarketingCloudSdk.WhenReadyListener() {
//            @Override public void ready(MarketingCloudSdk sdk) {
//                RegistrationManager registrationManager = sdk.getRegistrationManager();
//
//                // Set contact key
//                registrationManager
//                        .edit()
//                        .setContactKey(policy)
//                        .commit();
//
//                // Get contact key
////                String contactKey = registrationManager.getContactKey();
//
//                registrationManager.edit()
//                        // Set attribute value
//                        .setAttribute("CPF", CPF)
//                        .setAttribute("Apólice", policy)
//                        .setAttribute("Ramo", Ramo)
//
//                        // Commit changes
//                        .commit();
//                // Get Attributes
////                Map<String, String> attributes = registrationManager.getAttributesMap();
//
//            }
//        });

//        MarketingCloudSdk.requestSdk(new MarketingCloudSdk.WhenReadyListener() {
//            @Override public void ready(MarketingCloudSdk sdk) {
//                RegistrationManager registrationManager = sdk.getRegistrationManager();
//
//                registrationManager.edit()
//                        // Set attribute value
//                        .setAttribute("CPF", CPF)
//                        .setAttribute("Apólice", policy)
//                        .setAttribute("Ramo", Ramo)
//
//                        // Commit changes
//                        .commit();
//
//                // Get Attributes
//                Map<String, String> attributes = registrationManager.getAttributesMap();
//            }
//        });
//    }


    /**
     * Enable Fingerprints enable
     * @param context
     */
    public void enableFingerprints(Context context){
        try{
            loadFile.savePref(Config.TAGFINGERENALBE, "1", Config.TAG, context);
        }catch (Exception ex){
            //Log.i(Config.TAG, ex.toString());
        }
    }
}
