package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.DetailPolicyBeans;
import br.com.libertyseguros.mobile.beans.InstallmentsBeans;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.PolicyBeansV2;
import br.com.libertyseguros.mobile.beans.SecondCopyPolicy;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.AutoClaimWebView;
import br.com.libertyseguros.mobile.view.DetailPolicy;
import br.com.libertyseguros.mobile.view.DialogPayments;
import br.com.libertyseguros.mobile.view.Documents;
import br.com.libertyseguros.mobile.view.InsuranceCoverage;
import br.com.libertyseguros.mobile.view.ListVision360;
import br.com.libertyseguros.mobile.view.Parcels;
import br.com.libertyseguros.mobile.view.VehicleAccidentStep1;

public class PolicyModelV2 {

    private static boolean morePolicy;

    private InfoUser infoUser;

    private Connection conn;

    private int typeError;

    private int typeConnection;

    private OnConnectionResult onConnectionResult;

    private Context context;

    private MessageBeans message;

    private PolicyBeansV2 policyBeans;

    private DetailPolicyBeans detailPolicyBeans;

    private static PolicyBeansV2.InsurancesV2 insurancesSel;

    private boolean vehicleAccident;

    private boolean documents;

    private boolean shareSecondCopyPolicy;

    private DialogPayments dialogPayments;



    public PolicyModelV2(OnConnectionResult OnConnectionResult){
        this.onConnectionResult = OnConnectionResult;
        infoUser = new InfoUser();
    }

    /**
     * Method Array salesman
     * @param ctx
     */
    public void getListPolicy(Context ctx, boolean active){
        context = (Activity) ctx;

        conn = new Connection(context);

        if(active){
            typeConnection = 1;
        } else {
            typeConnection = 2;
        }

        createConnection();

        String param = "?isActive=" + active;

        conn.startConnectionV2("Segurado/Seguro", param, 2, true);
    }


    /**
     * Get Detail Policy
     * @param ctx
     */
    public void getDetailPolicy(Context ctx){
        try{
            context = (Activity) ctx;

            conn = new Connection(context);

            typeConnection = 3;

            createConnection();

            LoginBeans lb = infoUser.getUserInfo(context);

            String param = "?CpfCnpj=" + URLEncoder.encode(lb.getCpfCnpj(), "UTF-8") + "&Policy=" + URLEncoder.encode(insurancesSel.getPolicy(), "UTF-8");

           // String param = "?CpfCnpj=" + URLEncoder.encode(lb.getCpfCnpj(), "UTF-8") + "&Policy=" + URLEncoder.encode("3116518491", "UTF-8");

            conn.startConnectionV2("Segurado/Seguro/Detalhe", param, 2, true);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * Create name Second Copy Policy
     * @return
     */
    private String createNamePolicy(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        String name = insurancesSel.getPolicy() + "_" + dateFormat.format(date) + ".pdf";

        return name;
    }

    /**
     * Get Second Policy
     * @param ctx
     */
    public void getSecondCopyPolicy(Context ctx, boolean shareSecondCopyPolicy){
        try{
            context = (Activity) ctx;

            conn = new Connection(context);

            typeConnection = 4;

            this.shareSecondCopyPolicy = shareSecondCopyPolicy;

            createConnection();

            String strDateEnd = PolicyModelV2.getInsurancesSel().getDataEndPolicy();
            strDateEnd = strDateEnd.replace("T", " ");
            strDateEnd = strDateEnd.replace("Z", " ");

            String strDateStart = PolicyModelV2.getInsurancesSel().getDataStartPolicy();
            strDateStart = strDateStart.replace("T", " ");
            strDateStart = strDateStart.replace("Z", " ");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date dateEnd = sdf.parse(strDateEnd);
            Date dateStart = sdf.parse(strDateStart);

            long dateNow = System.currentTimeMillis();

            int type = 0;
            if(dateNow < dateStart.getTime()){
                //Futuras
                type = 1;
            } else if(dateNow >= dateStart.getTime() && dateNow <= dateEnd.getTime()) {
                //Vigentes
                type = 2;
            } else if(dateNow > dateEnd.getTime()){
                //Vencidas
                type = 3;
            }


            String param = "?NumeroApolice=" + URLEncoder.encode(PolicyModelV2.getInsurancesSel().getPolicy(), "UTF-8") + "&TipoConsulta=" + URLEncoder.encode(String.valueOf(type), "UTF-8");
            conn.startConnection("ApoliceSegundaVia/GetPDFPolicy", param, 2, true);
        }catch (Exception ex){
            ex.printStackTrace();
        }

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
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "PolicyModel: " + result);

                try {

                    Gson gson = new Gson();

                    if(getTypeConnection() == 1){

                        policyBeans = gson.fromJson(result, PolicyBeansV2.class);

                        if(!vehicleAccident){
                            if(policyBeans.getInsurances().length == 1 && !documents){
                                openDetail(context , 0, true);
                            }
                        } else {
                            ArrayList<PolicyBeansV2.InsurancesV2> insurances = new ArrayList<PolicyBeansV2.InsurancesV2>();
                            for(int ind = 0; ind <  policyBeans.getInsurances().length; ind++){
                                if(policyBeans.getInsurances()[ind].getBranch().equals(Config.auto)){
                                    insurances.add(policyBeans.getInsurances()[ind]);
                                }
                            }

                            if(insurances.size() == 0){
                                message = new MessageBeans();
                                message.setMessage("Nenhuma apólice encontrarda");

                                typeError = 2;
                                onConnectionResult.onError();
                            } else {
                                PolicyBeansV2.InsurancesV2[] insurancesAuto = new PolicyBeansV2.InsurancesV2[insurances.size()];

                                for(int ind = 0; ind <  insurances.size(); ind++){
                                    insurancesAuto[ind] = insurances.get(ind);
                                }
                                policyBeans.setInsurances(insurancesAuto);
                            }
                        }


                        onConnectionResult.onSucess();
                    } else if(getTypeConnection() == 2){
                        try {
                            PolicyBeansV2 policyBeansMoreApolicy = gson.fromJson(result, PolicyBeansV2.class);

                            if (policyBeans == null) {
                                policyBeans = new PolicyBeansV2();
                            }

                            policyBeans.addInsurances(policyBeansMoreApolicy.getInsurances());
                        } catch (Exception ex) {
                            message = gson.fromJson(result, MessageBeans.class);

                            typeError = 2;
                            onConnectionResult.onError();
                            ex.printStackTrace();
                        }

                        if(policyBeans.getInsurances().length == 1 && !documents && !vehicleAccident){
                            openDetail(context , 0, true);
                        } else {
                            onConnectionResult.onSucess();
                        }

                    }else if(getTypeConnection() == 3){
                        if(result.contains("broker")){
                            detailPolicyBeans = gson.fromJson(result, DetailPolicyBeans.class);
                            onConnectionResult.onSucess();
                        } else {
                            message = gson.fromJson(result, MessageBeans.class);

                            typeError = 2;
                            onConnectionResult.onError();
                        }

                    }else if(getTypeConnection() == 4){
                        try {
                            SecondCopyPolicy secondCopyPolicy = gson.fromJson(result, SecondCopyPolicy.class);

                            if(secondCopyPolicy != null && secondCopyPolicy.getPdf() != null) {
                                byte[] pdf = Base64.decode(secondCopyPolicy.getPdf(), 0);

                                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }

                                File document = new File(dir, createNamePolicy());

                                if (document.exists()) {
                                    document.delete();
                                }

                                FileOutputStream fos = new FileOutputStream(document.getPath());
                                fos.write(pdf);
                                fos.close();

                                if(shareSecondCopyPolicy){
                                    shareSecondCopyPolicy(document);
                                } else {
                                    viewSecondCopyPolicy(document);
                                }

                                onConnectionResult.onSucess();

                            } else {
                                message = gson.fromJson(result, MessageBeans.class);
                                typeError = 2;
                                onConnectionResult.onError();
                            }

                        }catch (Exception ex){
                            message = gson.fromJson(result, MessageBeans.class);

                            typeError = 2;
                            onConnectionResult.onError();
                            ex.printStackTrace();
                        }
                }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Gson gson = new Gson();
                    message = gson.fromJson(result, MessageBeans.class);

                    typeError = 2;
                    onConnectionResult.onError();
                }
            }
        });
    }

    /**
     * Share Second Copy Policy
     * @param file
     */
    private void shareSecondCopyPolicy(File file){


            Intent intent = new Intent();

            Uri uri = null;

            if (Build.VERSION.SDK_INT >= 24) {
                 uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }

            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            context.startActivity(Intent.createChooser(intent, context.getResources().getText(R.string.app_name)));

    }

    /**
     * Share Open Second Copy Policy
     * @param file
     */
    private void viewSecondCopyPolicy(File file){
        try{
            Intent intent = new Intent();

            if (Build.VERSION.SDK_INT >= 24) {

                Uri apkURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            }

            intent.setAction(android.content.Intent.ACTION_VIEW);


            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, context.getString(R.string.erro_open_pdf), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start process Second Copy Policy
     * @param ctx
     * @param shareSecondCopyPolicy
     */
    public void startOpenShareSecondCopyPolicy(Context ctx, boolean shareSecondCopyPolicy){
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        File document = new File(dir, createNamePolicy());

        if (document.exists()) {
            if(shareSecondCopyPolicy){
                shareSecondCopyPolicy(document);
            } else {
                viewSecondCopyPolicy(document);
            }

            typeConnection = 4;
            onConnectionResult.onSucess();
        } else {
            getSecondCopyPolicy(ctx, shareSecondCopyPolicy);
        }
    }

    /**
     * Get Type Error
     * @return
     */
    public int getTypeError() {
        return typeError;
    }

    /**
     * Set Type Error
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.typeError = typeError;
    }

    /**
     * Get Message
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Set Message
     * @param message
     */
    public void setMessage(MessageBeans message) {
        this.message = message;
    }

    /**
     * Get Type Connection
     * @return
     */
    public int getTypeConnection(){
        return typeConnection;
    }

    /**
     * Get Policy Beans
     * @return
     */
    public PolicyBeansV2 getPolicyBeans() {
        return policyBeans;
    }

    /**
     * Set Policy Beans
     * @return
     */
    public void setPolicyBeans(PolicyBeansV2 policyBeans) {
        this.policyBeans = policyBeans;
    }

    /**
     * Get InsurancesSel
     * @return
     */
    public static PolicyBeansV2.InsurancesV2 getInsurancesSel() {
        return insurancesSel;
    }

    /**
     * Set Click more policy
     * @return
     */
    public static boolean isMorePolicy() {
        return morePolicy;
    }

    /**
     * Get Click more policy
     * @param morePolicy
     */
    public static void setMorePolicy(boolean morePolicy) {
        PolicyModelV2.morePolicy = morePolicy;
    }

    /**
     * Set Insurances
     * @param insurancesSel
     */
    public static void setInsurancesSel(PolicyBeansV2.InsurancesV2 insurancesSel) {
        PolicyModelV2.insurancesSel = insurancesSel;
    }

    /**
     * Open Detail Policy
     * @param ctx
     * @param index
     */
    public void openDetail(Context ctx, int index, boolean finish){
        Activity activity = (Activity) context;
        Intent it = new Intent(activity, DetailPolicy.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        insurancesSel = policyBeans.getInsurances()[index];
        activity.startActivityForResult(it, 0);
        activity.overridePendingTransition(0,0);

        if(finish){
            activity.finish();
        }
    }

    /**
     * Open Vehicle Accident
     * @param ctx
     * @param index
     */
    public void openVehicleAccident(Context ctx, int index){

        VehicleAccidentModel.vasb = new VehicleAccidentSendBeans();

        VehicleAccidentModel.vasb.setPolicy(policyBeans.getInsurances()[index].getPolicy());
        //VehicleAccidentModel.vasb.setClaimType(policyBeans.getInsurances()[index].getClaimBeans().getClaimType() + "");
        VehicleAccidentModel.vasb.setLicensePlate(policyBeans.getInsurances()[index].getItens()[0].getLicensePlate());
        VehicleAccidentModel.vasb.setItemCode(policyBeans.getInsurances()[index].getItens()[0].getCode() + "");
        VehicleAccidentModel.vasb.setContractCode(policyBeans.getInsurances()[index].getContract() + "");
        VehicleAccidentModel.vasb.setIssueCode(policyBeans.getInsurances()[index].getIssuances()[0] + "");
        VehicleAccidentModel.vasb.setCiaCode(policyBeans.getInsurances()[index].getCiaCode() + "");

        Intent it;
        if(Config.ClaimWebView){
            it = new Intent(ctx, AutoClaimWebView.class);
        }else{
            it = new Intent(ctx, VehicleAccidentStep1.class);
        }

        ctx.startActivity(it);
    }

    /**
     * Open Documents
     * @param ctx
     * @param index
     */
    public void openDocuments(Context ctx, int index){
        String number = policyBeans.getInsurances()[index].getPolicy();
        DocumentsPictureModel.numberPolicy = number;
        Intent it = new Intent(ctx, Documents.class);
        ctx.startActivity(it);
    }


    /**
     *
     * @param ctx
     */
    public void openMorePolicy(Context ctx){
        ((Activity)ctx).finish();
        morePolicy = true;
    }



    /**
     * Open Intent call
     */
    public void openCall(Context ctx){
        this.context = ctx;

        String number = detailPolicyBeans.getInsurance().getBroker().getPhone();
        Uri number1 = Uri.parse("tel:" + number);
        Intent dial = new Intent(Intent.ACTION_DIAL);
        dial.setData(number1);
        context.startActivity(dial);
    }


    /**
     * Send Email
     */
    public void sendEmail(Context ctx){
        this.context = ctx;

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ detailPolicyBeans.getInsurance().getBroker().getEmail()});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");


        //emailIntent.setType("message/rfc822");

        try {
            context.startActivity(Intent.createChooser(emailIntent,
                    context.getString(R.string.send_email_title)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Get Detail Policy
     * @return
     */
    public DetailPolicyBeans getDetailPolicyBeans() {
        return detailPolicyBeans;
    }

    /**
     * Set Detail Policy
     * @param detailPolicyBeans
     */
    public void setDetailPolicyBeans(DetailPolicyBeans detailPolicyBeans) {
        this.detailPolicyBeans = detailPolicyBeans;
    }

    /**
     * Get License Plate
     * @return
     */
    public int licensePlate(){
        int licensePlate = 1;

        try{
            licensePlate = Integer.parseInt(
                    detailPolicyBeans.getInsurance().getItens()[0].getLicensePlate().substring(
                            detailPolicyBeans.getInsurance().getItens()[0].getLicensePlate().length() - 1));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return licensePlate;
    }

    /**
     * Open Parcel
     * @param ctx
     */
    public void openParcels(Context ctx, int issuanceIndex){
        Intent it = new Intent(ctx, Parcels.class);
        it.putExtra("policyNumber", insurancesSel.getPolicy());
        it.putExtra("ciaCode", insurancesSel.getCiaCode() +  "");
        it.putExtra("contract", insurancesSel.getContract() + "");
        it.putExtra("issuance", insurancesSel.getIssuances()[issuanceIndex] + "");

        ctx.startActivity(it);
    }

    /**
     * Set VehicleAccident
     * @param value
     */
    public void setVehicleAccident(boolean value){
        vehicleAccident = value;
    }

    /**
     * Set Documents
     * @param value
     */
    public void setDocuments(boolean value){
        documents = value;
    }

    /**
     * Get is Vehicle Accident
     * @return
     */
    public boolean isVehicleAccident(){
        return vehicleAccident;
    }


    /**
     * Get is Documents
     * @return
     */
    public boolean isDocuments(){
        return documents;
    }

    /**
     * Open Extend Screen
     * @param context
     */
    public void openExtend(Context context, int indexPayment, OnBarCode barCode){
//        Intent it = new Intent(context, ExtendPagament.class);
//        it.putExtra("contract", insurancesSel.getContract() + "");
//        it.putExtra("installment", detailPolicyBeans.getPayment()[indexPayment].getInstallmentNumber() + "");
//        it.putExtra("issuance", insurancesSel.getIssuances()[indexPayment] + "");
//        it.putExtra("ciaCode", insurancesSel.getCiaCode() + "");
//        it.putExtra("cliCode", insurancesSel.getCliCode() + "");
//
//        boolean payment = false;
//
//        if(detailPolicyBeans.getPayment()[indexPayment].getCodigoTipoModalidadeCobranca().equals("FB")){
//            payment = true;
//        }
//
//        it.putExtra("payment", payment);
//        context.startActivity(it);

        InstallmentsBeans installment = new InstallmentsBeans();
        installment.setCodigoTipoModalidadeCobranca(detailPolicyBeans.getPayment()[indexPayment].getCodigoTipoModalidadeCobranca());
        installment.setShowComponent(detailPolicyBeans.getPayment()[indexPayment].getShowComponent());
        installment.setCanExtend(detailPolicyBeans.getPayment()[indexPayment].isCanExtend());
        installment.setNumber(detailPolicyBeans.getPayment()[indexPayment].getInstallmentNumber());
        installment.setStatus(detailPolicyBeans.getPayment()[indexPayment].getStatus());
        installment.setValue(detailPolicyBeans.getPayment()[indexPayment].getValue());

        dialogPayments = new DialogPayments(context,insurancesSel, installment,insurancesSel.getIssuances()[indexPayment], barCode);
        dialogPayments.show();
    }

    /**
     * Open Insurance Coverage
     * @param context
     */
    public void openInsuranceCoverage(Context context){


        if(detailPolicyBeans.getInsurance().getItens()[0].getInsuranceCoverages() == null){
            Toast.makeText(context, context.getString(R.string.insurance_coverage_unavailable), Toast.LENGTH_LONG).show();
        }else {
            Intent it = new Intent(context, InsuranceCoverage.class);
            InsuranceCoverageModel.setPolicyItens(detailPolicyBeans.getInsurance().getItens());
            context.startActivity(it);
        }
    }

    /**
     * Open List Policy
     * @param context
     */
    public void openListVision(Context context){
        Intent it = new Intent(context, ListVision360.class);
        it.putExtra("policy", detailPolicyBeans.getInsurance().getPolicy());
        context.startActivity(it);
    }
}
