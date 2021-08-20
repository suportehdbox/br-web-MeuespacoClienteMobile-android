package br.com.libertyseguros.mobile.model;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.security.SecureRandom;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.RegisterActivationBeans;
import br.com.libertyseguros.mobile.beans.RegisterBeans;
import br.com.libertyseguros.mobile.beans.VerifyBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.util.ValidCNPJ;
import br.com.libertyseguros.mobile.util.ValidCPF;
import br.com.libertyseguros.mobile.util.ValidEmail;
import br.com.libertyseguros.mobile.util.ValidPassword;
import br.com.libertyseguros.mobile.view.PrivacyPolicy;

public class RegisterModel extends BaseModel{

    public final static int REGISTER_STEP1 = -5;

    public final static int REGISTER_STEP2 = -6;

    public final static int EMAIL_ACTIVATION = -7;

    public final static int REGISTER_ACTIVATION = -8;

    private String[] warning;

    private ValidCPF validCPF;

    private ValidCNPJ validCNPJ;

    private ValidEmail validEmail;

    private ValidPassword validPassword;

    private Connection conn;

    private OnConnectionResult onConnectionResult;

    private int typeConnection;

    private int typeError;

    private Context context;

    private MessageBeans message;

    private RegisterActivationBeans registerActivationBeans;

    private RegisterBeans registerMessage;

    private boolean isFacebook;

    public RegisterBeans getRegisterMessage() {
        return registerMessage;
    }

    public void setRegisterMessage(RegisterBeans registerMessage) {
        this.registerMessage = registerMessage;
    }


    private String idFacebook;

    private VerifyBeans verifyBeans;

    public RegisterModel() {
        validCPF = new ValidCPF();
        validCNPJ = new ValidCNPJ();
        validEmail = new ValidEmail();
        validPassword = new ValidPassword();
    }

    public RegisterModel(OnConnectionResult onConnectionResult) {
        validCPF = new ValidCPF();
        validCNPJ = new ValidCNPJ();
        validEmail = new ValidEmail();
        validPassword = new ValidPassword();
        this.onConnectionResult = onConnectionResult;
        this.idFacebook = "";
    }

    public RegisterModel(OnConnectionResult onConnectionResult, String idFacebook, boolean isFacebook) {
        validCPF = new ValidCPF();

        validCNPJ = new ValidCNPJ();

        validEmail = new ValidEmail();

        validPassword = new ValidPassword();

        this.onConnectionResult = onConnectionResult;

        this.idFacebook = idFacebook;

        this.isFacebook = isFacebook;
    }

    /**
     * Connection Register
     *
     * @param context
     * @param policy
     * @param password
     * @param cpf
     * @param email
     */
    public void register(Context context, String policy, String password, String cpf, String email) {

        try {

            this.context = context;

            conn = new Connection(context);

            typeConnection = 1;

            createConnection();

            cpf = cpf.replace(".", "");
            cpf = cpf.replace("-", "");
            cpf = cpf.replace("/", "");

            String param = "";

            if (policy == null) {
                policy = "";
            }

            if (idFacebook.equals("")) {
                param = "Policy=" + URLEncoder.encode(policy, "UTF-8") + "&Pwd=" + URLEncoder.encode(password, "UTF-8") + "&CpfCnpj=" + URLEncoder.encode(cpf, "UTF-8") + "&Email=" + URLEncoder.encode(email, "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing;
            } else {

                try {
                    param = "Policy=" + URLEncoder.encode(policy, "UTF-8") + "&CpfCnpj=" + URLEncoder.encode(cpf, "UTF-8") + "&Email=" + URLEncoder.encode(email, "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing + "&IdMidiaSocial=" + URLEncoder.encode(idFacebook, "UTF-8") + "&Photo=" + URLEncoder.encode("http=", "UTF-8") + "//" + URLEncoder.encode("graph.facebook.com", "UTF-8") + "/" + idFacebook + "/picture";

                } catch (Exception ex) {
                    param = "Policy=" + URLEncoder.encode(policy, "UTF-8") + "&CpfCnpj=" + URLEncoder.encode(cpf, "UTF-8") + "&Email=" + URLEncoder.encode(email, "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing + "&IdMidiaSocial=" + URLEncoder.encode(idFacebook, "UTF-8") + "&Photo=" + URLEncoder.encode("http=", "UTF-8") + "//" + URLEncoder.encode("graph.facebook.com", "UTF-8") + "/" + idFacebook + "/picture";
                }

            }

            conn.startConnectionV2("Acesso/Segurado", param, 1, true);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    /**
     * Valid Field
     *
     * @param context
     * @param name
     * @param policy
     * @param cpf
     * @param email
     * @param confirmEmail
     * @param password
     * @param confirmPassword
     * @return
     */
    public String[] validField(Context context, String name, String policy, String cpf, String email, String confirmEmail, String password, String confirmPassword, boolean check) {

        warning = new String[8];
        for (int ind = 0; ind < 8; ind++) {
            warning[ind] = "";
        }

        if (name.length() == 0) {
            warning[0] = context.getString(R.string.message_empty_name);
        }

        if (policy.length() == 0) {
            warning[1] = context.getString(R.string.message_empty_policy);
        }

        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf = cpf.replace("/", "");

        if (cpf.length() == 0) {
            warning[2] = context.getString(R.string.message_empty_cpf_cnpj);
        } else if (!validCPF.isCPF(cpf) && !validCNPJ.isCNPJ(cpf)) {
            warning[2] = context.getString(R.string.message_error_cpf_cnpj);
        }

        if (email.length() == 0) {
            warning[3] = context.getString(R.string.message_empty_email);
        } else if (!validEmail.checkemail(email)) {
            warning[3] = context.getString(R.string.message_error_email);
        }

        if (!email.equals(confirmEmail)) {
            warning[4] = context.getString(R.string.message_error_email_confirm);
        }

        if (password.length() == 0) {
            warning[5] = context.getString(R.string.message_empty_password);
        } else if (!validPassword.checkPassword(password)) {
            warning[5] = context.getString(R.string.message_error_password);
        }

        if (!password.equals(confirmPassword)) {
            warning[6] = context.getString(R.string.message_error_password_confirm);
        }

        if (!check) {
            warning[7] = context.getString(R.string.message_check_box);
        }

        return warning;
    }

    /**
     * Set item error
     *
     * @param warning
     */
    public void setWarning(String[] warning) {
        this.warning = warning;
    }

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
                //Log.i(Config.TAG, "RegisterModel: " + result);

                try {

                    if (typeConnection == EMAIL_ACTIVATION) {
                        if (result.equals("")) {
//                            Gson gson = new Gson();
//                            message = gson.fromJson(result, MessageBeans.class);
                            onConnectionResult.onSucess();
                        } else {
                            onConnectionResult.onError();
                        }
                    } else if (typeConnection == REGISTER_STEP1) {
                        if (!result.equals("")) {

                            Gson gson = new Gson();
                            verifyBeans = gson.fromJson(result, VerifyBeans.class);
                            if (verifyBeans.getSucesso() && verifyBeans.getNumeroApolice() != null) {
                                onConnectionResult.onSucess();
                            } else {
                                message = new MessageBeans();
                                message.setMessage(verifyBeans.getMessage());
                                onConnectionResult.onError();
                            }
                        } else {
                            onConnectionResult.onError();
                        }

                    } else if (typeConnection == REGISTER_ACTIVATION) {
                        Gson gson = new Gson();

                        registerActivationBeans = gson.fromJson(result, RegisterActivationBeans.class);
                        if (registerActivationBeans.getMensagem() == null) {
                            typeError = 2;
                            onConnectionResult.onError();
                        } else {
                            onConnectionResult.onSucess();
                        }

                    } else {
                        Gson gson = new Gson();

                        if (!result.equals("")) {
                            registerMessage = gson.fromJson(result, RegisterBeans.class);
                            if (registerMessage.getSucesso()) {
                                onConnectionResult.onSucess();
                            } else {
                                onConnectionResult.onError();
                            }
                        } else {

                            message = gson.fromJson(result, MessageBeans.class);

                            typeError = 2;
                            onConnectionResult.onError();
                        }
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Gson gson = new Gson();
                    message = gson.fromJson(result, MessageBeans.class);
                    onConnectionResult.onError();
                }
            }
        });
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
     * Set type error connection
     *
     * @param typeError
     */
    public void setTypeError(int typeError) {
        this.typeError = typeError;
    }

    /**
     * Get type connection
     *
     * @return
     */
    public int getTypeConnection() {
        return typeConnection;
    }

    /**
     * Set type Connection
     *
     * @param typeConnection
     */
    public void setTypeConnection(int typeConnection) {
        this.typeConnection = typeConnection;
    }

    /**
     * Get Message Error
     *
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }


    /**
     * Open Terms
     */
    public void openLinkTerms(Context context) {
        Intent it = new Intent(context, PrivacyPolicy.class);
        context.startActivity(it);
    }




    /**
     * register STEP 1
     *
     * @param context
     * @param type
     * @param policy
     * @param cpfCnpj
     */
    public void registerSTEP1(Context context, int type, String policy, String cpfCnpj) {

        try {

            this.context = context;

            conn = new Connection(context);

            typeConnection = REGISTER_STEP1;

            createConnection();

            String param = "";

            String nameParameter = "";

            switch (type) {
                case 0:
                    if (policy.contains("-")) {
                        policy = policy.replace("-", "");
                        nameParameter = "Placa";
                    } else {
                        nameParameter = "NumeroApolice";

                    }

                    break;
                case 1:
                    if (policy.contains("-")) {
                        policy = policy.replace("-", "");
                        nameParameter = "CEP";
                    } else {
                        nameParameter = "NumeroApolice";

                    }
                    break;
                case 2:
                    if (policy.contains("/")) {
                        policy = policy.replace("-", "");
                        nameParameter = "DataNascimento";
                    } else {
                        nameParameter = "NumeroApolice";
                    }
                    break;
            }

            param = nameParameter + "=" + URLEncoder.encode(policy, "UTF-8") + "&CpfCnpj=" + URLEncoder.encode(cpfCnpj, "UTF-8") + "&MarcaComercializacao=" + BuildConfig.brandMarketing;

            conn.startConnection("Acesso/Verificar", param, 1);
/*
Connecion URL: https://act-dmz.libertyseguros.com.br/MobileApi/api/v1/Acesso/Verificar
I/LibertySeguros: Connecion PARAM: NumeroApolice=3116537524&CpfCnpj=82726755364&MarcaComercializacao=1
I/LibertySeguros: RegisterModel: {"message":"An error has occurred.","exceptionMessage":"Erro ao processar requisição. Por favor, tente novamente","exceptionType":"System.Exception","stackTrace":null}
 */

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    /**
     * Register STEP 2
     *
     * @param context
     * @param cpfCnpj
     * @param email
     * @param pwd
     */
    public void registerSTEP2(Context context, String cpfCnpj, String email, String pwd, String photoGoogle) {

        try {

            this.context = context;

            conn = new Connection(context);

            typeConnection = REGISTER_STEP2;

            createConnection();

            cpfCnpj = cpfCnpj.replace(".", "");
            cpfCnpj = cpfCnpj.replace("-", "");
            cpfCnpj = cpfCnpj.replace("/", "");

            if (pwd.isEmpty()) {
                pwd = generatePassword();
            }

            int origemCadastro;
            String photo = "";

            if (idFacebook.isEmpty()) {
                origemCadastro = 0;
            } else {
                if (isFacebook) {
                    origemCadastro = 2;
                    photo = URLEncoder.encode("http=", "UTF-8") + "//" + URLEncoder.encode("graph.facebook.com", "UTF-8") + "/" + idFacebook + "/picture";
                } else {
                    origemCadastro = 1;
                    photo = URLEncoder.encode(photoGoogle, "UTF-8");
                }
            }

            String param = "CpfCnpj=" + URLEncoder.encode(cpfCnpj, "UTF-8") + "&Policy=" + URLEncoder.encode(verifyBeans.getNumeroApolice(), "UTF-8") + "&InsuredsName=" + URLEncoder.encode(verifyBeans.getNomeSegurado(), "UTF-8") + "&Email=" + URLEncoder.encode(email, "UTF-8") + "&Pwd=" + URLEncoder.encode(pwd, "UTF-8") +
                    "&OrigemCadastro=" + URLEncoder.encode(origemCadastro + "", "UTF-8") + "&CIFCode=" + URLEncoder.encode(verifyBeans.getCodigoCIF() + "", "UTF-8");

            if (origemCadastro > 0) {
//                param +=  "&IdMidiaSocial=" + URLEncoder.encode(idMidiaSocial + "", "UTF-8") +  "&Photo=" + URLEncoder.encode(photo + "", "UTF-8");
                param += "&IdMidiaSocial=" + URLEncoder.encode(idFacebook, "UTF-8") + "&Photo=" + photo;
            }

            param += "&brandMarketing=" + BuildConfig.brandMarketing;

            conn.startConnectionV2("Acesso/Segurado", param, 1, false);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get Link Register
     *
     * @param context
     * @param cpfCnpj
     */
    public void getLinkRegister(Context context, String cpfCnpj) {

        try {

            this.context = context;

            conn = new Connection(context);

            typeConnection = EMAIL_ACTIVATION;

            createConnection();

            cpfCnpj = cpfCnpj.replace(".", "");
            cpfCnpj = cpfCnpj.replace("-", "");
            cpfCnpj = cpfCnpj.replace("/", "");

            String param = "CpfCnpj=" + URLEncoder.encode(cpfCnpj, "UTF-8") + "&MarcaComercializacao=" + BuildConfig.brandMarketing;

            conn.startConnection("Acesso/ReenviarEmail", param, 1);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Get Activation Register
     *
     * @param context
     */
    public void getRegisterActivation(Context context, String token) {

        try {

            this.context = context;

            conn = new Connection(context);

            typeConnection = REGISTER_ACTIVATION;

            createConnection();

            String param = "TokenAutenticacao=" + URLEncoder.encode(token, "UTF-8") + "&MarcaComercializacao=" + BuildConfig.brandMarketing;

            conn.startConnection("Acesso/AtivarCadastroSegurado", param, 1);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String[] validField(Context context, String cpf, String policy, int type) {

        warning = new String[2];

        warning[0] = "";

        switch (type) {
            case 0:


                if (policy.contains("-")) {
                    if (policy.length() != 8) {
                        warning[0] = context.getString(R.string.message_error_plate);
                    }
                } else {
                    if (policy.length() != 10 && policy.length() != 15) {
                        warning[0] = context.getString(R.string.message_error_policy);
                    }
                }
                break;
            case 1:

                if (policy.contains("-")) {
                    if (policy.length() != 9) {
                        warning[0] = context.getString(R.string.message_error_cep);
                    }
                } else {
                    if (policy.length() != 10 && policy.length() != 15) {
                        warning[0] = context.getString(R.string.message_error_policy);
                    }
                }

            case 2:

                if (policy.contains("/")) {
                    if (policy.length() != 10) {
                        warning[0] = context.getString(R.string.message_error_bir);
                    }
                } else {
                    if (policy.length() != 10 && policy.length() != 15) {
                        warning[0] = context.getString(R.string.message_error_policy);
                    }
                }
                break;

        }


        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf = cpf.replace("/", "");

        if (cpf.length() == 0) {
            warning[1] = context.getString(R.string.message_empty_cpf_cnpj);
        } else if (!validCPF.isCPF(cpf) && !validCNPJ.isCNPJ(cpf)) {
            warning[1] = context.getString(R.string.message_error_cpf_cnpj);
        }

        return warning;
    }


    /**
     * valid FieldSTEP2
     *
     * @param context
     * @param name
     * @param policy
     * @param cpf
     * @param email
     * @param confirmEmail
     * @param password
     * @param confirmPassword
     * @param check
     * @return
     */
    public String[] validFieldSTEP2(Context context, String name, String policy, String cpf, String email, String confirmEmail, String password, String confirmPassword, boolean check) {

        warning = new String[8];
        for (int ind = 0; ind < 8; ind++) {
            warning[ind] = "";
        }

        if (name.length() == 0) {
            warning[0] = context.getString(R.string.message_empty_name);
        }

        if (policy.length() == 0) {
            warning[1] = context.getString(R.string.message_empty_policy);
        }

        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf = cpf.replace("/", "");

        if (cpf.length() == 0) {
            warning[2] = context.getString(R.string.message_empty_cpf_cnpj);
        } else if (!validCPF.isCPF(cpf) && !validCNPJ.isCNPJ(cpf)) {
            warning[2] = context.getString(R.string.message_error_cpf_cnpj);
        }

        if (email.length() == 0) {
            warning[3] = context.getString(R.string.message_empty_email);
        } else if (!validEmail.checkemail(email)) {
            warning[3] = context.getString(R.string.message_error_email);
        }

        if (!email.equals(confirmEmail)) {
            warning[4] = context.getString(R.string.message_error_email_confirm);
        }

        if (password.length() == 0) {
            warning[5] = context.getString(R.string.message_empty_password);
        } else if (!validPassword.checkPassword(password)) {
            warning[5] = context.getString(R.string.message_error_password);
        }

        if (!password.equals(confirmPassword)) {
            warning[6] = context.getString(R.string.message_error_password_confirm);
        }


        return warning;
    }


    private String generatePassword() {
        String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@$*()+-";
        String numbers = "0123456789";
        SecureRandom rnd = new SecureRandom();

        int len = 5;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        sb.append(numbers.charAt(rnd.nextInt(numbers.length())));
        //Log.v((Config.TAG, sb.toString());

        return sb.toString();

    }

    public boolean isFacebook() {
        return isFacebook;
    }

    public void setFacebook(boolean facebook) {
        isFacebook = facebook;
    }

    public RegisterActivationBeans getRegisterActivationBeans() {
        return registerActivationBeans;
    }

    public void setRegisterActivationBeans(RegisterActivationBeans registerActivationBeans) {
        this.registerActivationBeans = registerActivationBeans;
    }
}
