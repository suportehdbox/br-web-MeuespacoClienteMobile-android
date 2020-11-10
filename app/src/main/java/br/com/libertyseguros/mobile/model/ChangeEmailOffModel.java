package br.com.libertyseguros.mobile.model;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.ChangeEmailSendBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.QuestionBeans;
import br.com.libertyseguros.mobile.beans.QuestionSendBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.util.ValidCNPJ;
import br.com.libertyseguros.mobile.util.ValidCPF;
import br.com.libertyseguros.mobile.util.ValidEmail;
import br.com.libertyseguros.mobile.view.ChangeEmailStepThree;
import br.com.libertyseguros.mobile.view.ChangeEmailStepTwo;
import br.com.libertyseguros.mobile.view.custom.EditTextCustom;

public class ChangeEmailOffModel extends BaseModel{

    private ValidEmail validEmail;

    private ValidCPF validCPF;

    private ValidCNPJ validCNPJ;

    private Context context;

    private Connection conn;

    private OnConnectionResult onConnectionResult;

    private int typeError;

    private MessageBeans message;

    private int typeConnection;

    private String id;

    private static QuestionBeans questionBeans;

    private static String cpfCnpj;

    private static ArrayList<EditTextCustom> editTextCustom;

    private static boolean exit;

    public ChangeEmailOffModel(Context context, OnConnectionResult onConnectionResult){
        this.onConnectionResult = onConnectionResult;

        validCNPJ = new ValidCNPJ();
        validEmail = new ValidEmail();
        validCPF = new ValidCPF();
    }

    /**
     * Open Change Answer Screen
     * @param context
     */
    public void openPartTwo(Context context, int index){
        Intent it = new Intent(context, ChangeEmailStepTwo.class);
        it.putExtra("index", index + "");
        context.startActivity(it);
    }


    /**
     * Forgot email send Answers
     * @param ctx
     * @param email
     */
    public void forgotEmailSendAnswers(Context ctx, String email){
        String param = "";
        try{

            typeConnection = 2;

            context = ctx;

            conn = new Connection(context);

            createConnection();

            QuestionSendBeans questionSendBeans = new QuestionSendBeans();

            QuestionSendBeans.ListAnswers la[] = questionSendBeans.createInstance(questionBeans.getQuestion().getQuestionArray().length);

            id =  "[";

             for(int ind = 0; ind < editTextCustom.size(); ind++){
                 QuestionSendBeans.ListAnswers answers = questionSendBeans.getAnswers();

                 answers.setIdPergunta(questionBeans.getQuestion().getQuestionArray()[ind].getIdPergunta());

                 if(ind == 0){
                     id +=  questionBeans.getQuestion().getQuestionArray()[ind].getIdPergunta() + "";
                 } else {
                     id += "," + questionBeans.getQuestion().getQuestionArray()[ind].getIdPergunta() + "";
                 }

                 answers.addResposta(editTextCustom.get(ind).getEditText().getText().toString());

                 la[ind] = answers;
             }

            id = id + "]";

            loadFile.savePref(Config.TAGCHANGEEMAILQUESTION, id, Config.TAG, context);

            ChangeEmailSendBeans changeEmailSendBeans = new ChangeEmailSendBeans();
            changeEmailSendBeans.setCpfCnpj(cpfCnpj);
            changeEmailSendBeans.setEmail(email);
            changeEmailSendBeans.getRespostas().setListaDePerguntas(la);
            changeEmailSendBeans.setBrandMarketing(BuildConfig.brandMarketing);

            String json = gson.toJson(changeEmailSendBeans);


            //param = "CpfCnpj="+ URLEncoder.encode(cpfCnpj, "UTF-8") + "&Email="+ URLEncoder.encode(email, "UTF-8") +  "&Respostas=" +  URLEncoder.encode(json, "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing;
            param = json;

            conn.startConnection("Acesso/NovoEmail/Answer", param, 3, false);
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
    /**
     * Forgot password login on
     * @param ctx
     * @param cpfCnpj
     */
    public void forgotEmailSendCPF(Context ctx, String cpfCnpj){
        String param = "";
        try{
            typeConnection = 1;

            context = ctx;

            conn = new Connection(context);

            createConnection();

            String json = loadFile.loadPref(Config.TAG, context, Config.TAGCHANGEEMAILQUESTION);

            if(json == null){
                param = "CpfCnpj="+ URLEncoder.encode(cpfCnpj, "UTF-8") + "&brandMarketing=" + BuildConfig.brandMarketing;
            } else {
                param = "CpfCnpj="+ URLEncoder.encode(cpfCnpj, "UTF-8") + "&PerguntasSorteadas=" + json + "&brandMarketing=" + BuildConfig.brandMarketing;
            }


            ChangeEmailOffModel.cpfCnpj = cpfCnpj;
            conn.startConnection("Acesso/NovoEmail/Question", param, 1, false);
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

        /**
         * Valid Fields
         * @param context
         * @param cpfCnpj
         * @return
         */
    public String validFieldStepOne(Context context, String cpfCnpj){

        String msg = "";

        cpfCnpj = cpfCnpj.replace(".", "");
        cpfCnpj = cpfCnpj.replace("-", "");
        cpfCnpj = cpfCnpj.replace("/", "");

        if(cpfCnpj.length() == 0){
            msg = context.getString(R.string.message_empty_cpf_cnpj);
        } else if(!validCPF.isCPF(cpfCnpj) && !validCNPJ.isCNPJ(cpfCnpj)){
            msg = context.getString(R.string.message_error_cpf_cnpj);
        }

        return msg;
    }


    /**
     * Valid Fields
     * @param context
     * @param email
     * @param confirmEmail
     * @return
     */
    public String validFieldStepThree(Context context, String email, String confirmEmail){

        String msg = "";



        if(email.length() == 0){
            msg = context.getString(R.string.message_empty_email);
        } else if(!validEmail.checkemail(email)){
            msg = context.getString(R.string.message_error_email);
        } else if(!email.equals(confirmEmail)) {
            msg = context.getString(R.string.message_error_email_confirm);
        }

        return msg;
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
                //Log.i(Config.TAG, "typeConnection:" + typeConnection + " - " + "ChangeEmailOff: " + result);

                try {
                    if(typeConnection == 1){


                        try{
                            questionBeans = gson.fromJson(result, QuestionBeans.class);
                            if(questionBeans.getQuestion() == null){
                                Gson gson = new Gson();
                                message = gson.fromJson(result, MessageBeans.class);

                                typeError = 2;
                                onConnectionResult.onError();
                            } else {
                                onConnectionResult.onSucess();
                            }


                        }catch(Exception ex){
                            Gson gson = new Gson();
                            message = gson.fromJson(result, MessageBeans.class);

                            typeError = 2;
                            onConnectionResult.onError();
                        }

                    } else {
                        if (!result.contains("message")) {
                            onConnectionResult.onSucess();
                        } else {
                            Gson gson = new Gson();
                            message = gson.fromJson(result, MessageBeans.class);

                            typeError = 2;
                            onConnectionResult.onError();
                        }
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
    public int getTypeConnection() {
        return typeConnection;
    }

    /**
     * Set Type Connection
     * @param typeConnection
     */
    public void setTypeConnection(int typeConnection) {
        this.typeConnection = typeConnection;
    }

    /**
     * Get Question
     * @return
     */
    public static QuestionBeans getQuestionBeans(){
        return questionBeans;
    }

    /**
     * Add EditText
     * @param etAdd
     */
    public static void addEditText(EditTextCustom etAdd, int index){
        if(editTextCustom == null){
            editTextCustom = new ArrayList<>();
            editTextCustom.add(etAdd);
            editTextCustom.add(etAdd);
            editTextCustom.add(etAdd);
        } else {
            editTextCustom.set(index, etAdd);

        }
    }

    /**
     * Get exit boolean Activity
     * @return
     */
    public static boolean isExit() {
        return exit;
    }

    /**
     * Set exit boolean
     * @param exit
     */
    public static void setExit(boolean exit) {
        ChangeEmailOffModel.exit = exit;
    }

    /**
     * Open Screen Change email step three
     * @param context
     */
    public void openChangeEmailStepThree(Context context){
        Intent it = new Intent(context, ChangeEmailStepThree.class);
        context.startActivity(it);
    }

}
