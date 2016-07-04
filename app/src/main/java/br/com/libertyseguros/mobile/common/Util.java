package br.com.libertyseguros.mobile.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.constants.Constants;

/**
 * Created by evandro on 8/31/15.
 */
public class Util {


    public static void showAlert(Context context, String message, String title)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        if (null != title) {
            if (title.compareTo("") == 0) {
                alertDialog.setTitle(context.getString(R.string.app_name));
            }
            else {
                alertDialog.setTitle(title);
            }
        }

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                context.getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();
    }

    public static void showException(Context context, Exception e)
    {
        e.printStackTrace();

        StackTraceElement element = e.getStackTrace()[0];

        String methodName = element.getMethodName();
        String fileName = element.getFileName();
        String line = Integer.toString(element.getLineNumber());
        String erro = e.getMessage();

        String mensagemErro = line + " (" + fileName + " - " + methodName + ") " + erro;


        if(null != context)
        {
            showAlert(context, mensagemErro, Constants.ERROR);
        }
        else
        {
            Log.e(Constants.ERROR, mensagemErro);
        }

    }


    public static void setTitleNavigationBar(Context context, int title) {
        Activity telaParent = (Activity)context;
        TextView textView = (TextView) telaParent.findViewById(R.id.nav_bar_text);
        textView.setText(title);
    }


    /**
     * Displays an alert to the user asking them to confirm an action before proceeding
     *
     * @param context
     *            the context within which to display the alert
     * @param title
     *            the title to display on the alert
     * @param message
     *            the message to display on the alert
     * @param positiveButton
     *            the text for the positive button
     * @param negativeButton
     *            the text for the negative button
     * @param listener
     *            the listener which will be listening for the user's selection
     */
    public static void displayConfirmAlert(Context context, String title, String message, String positiveButton,
                                           String negativeButton, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, listener);
        builder.setNegativeButton(negativeButton, listener);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void callGB() {
        System.gc();
        Runtime.getRuntime().gc();
    }

    public static String getDeviceUID(Context context) {

        TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String formataMinusculaMenosPrimeira(String nomeSemFormatacao){

        String[] nomes = nomeSemFormatacao.toLowerCase().split(" ");

        StringBuilder nomeFormatado = new StringBuilder();
        for (String nome : nomes) {
            String nomePrimeiraMaiuscula = nome.replaceFirst(nome.substring(0, 1), nome.substring(0, 1).toUpperCase());
            nomeFormatado.append(nomePrimeiraMaiuscula).append(" ");
        }

        return nomeFormatado.toString();
    }

    public static void setMaxTextEdit(EditText edt, int maxLength) {
        edt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength) });
    }

    public static boolean checkCPF(String CPF) {

        if ( CPF.length() != 11 )
            return false;

        if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999")){
            return false;
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {

                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else
                dig10 = (char) (r + 48);

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else
                dig11 = (char) (r + 48);

            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return (true);
            else
                return (false);
        } catch (Exception erro) {
            return (false);
        }

    }
    static public boolean checkCnpj(String str_cnpj) {

        int soma = 0, dig;
        String cnpj_calc = str_cnpj.substring(0,12);

        if ( str_cnpj.length() != 14 )
            return false;

        char[] chr_cnpj = str_cnpj.toCharArray();

		/* Primeira parte */
        for( int i = 0; i < 4; i++ )
            if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )
                soma += (chr_cnpj[i] - 48 ) * (6 - (i + 1)) ;
        for( int i = 0; i < 8; i++ )
            if ( chr_cnpj[i+4]-48 >=0 && chr_cnpj[i+4]-48 <=9 )
                soma += (chr_cnpj[i+4] - 48 ) * (10 - (i + 1)) ;
        dig = 11 - (soma % 11);

        cnpj_calc += ( dig == 10 || dig == 11 ) ?
                "0" : Integer.toString(dig);

        soma = 0;
        for ( int i = 0; i < 5; i++ )
            if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )
                soma += (chr_cnpj[i] - 48 ) * (7 - (i + 1)) ;
        for ( int i = 0; i < 8; i++ )
            if ( chr_cnpj[i+5]-48 >=0 && chr_cnpj[i+5]-48 <=9 )
                soma += (chr_cnpj[i+5] - 48 ) * (10 - (i + 1)) ;
        dig = 11 - (soma % 11);
        cnpj_calc += ( dig == 10 || dig == 11 ) ?
                "0" : Integer.toString(dig);

        return str_cnpj.equals(cnpj_calc);
    }

    public static Bitmap getBitmapByURL(String address) {
        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        InputStream stream = null;
        try {
            stream = url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (stream != null) {
            return BitmapFactory.decodeStream(stream);
        }

        return null;
    }

    public static String readTextFromResource(Context context, int resourceID)
    {
        String sHtml = "";
        InputStream raw = context.getResources().openRawResource(resourceID);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        int i;

        try
        {
            i = raw.read();

            while (i != -1)
            {
                stream.write(i);
                i = raw.read();
            }

            raw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        sHtml = stream.toString();

        return sHtml;
    }

    private static final String FILE_NAME = "notificacao.json";

    /**
     * @return List<Map<String, Object>>, lista de notificações push salvas
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getNotificationList(Context context)
    {
        List<Map<String, Object>> resultados = new ArrayList<Map<String, Object>>();
        try {
            System.gc();
            Runtime.getRuntime().gc();

            String content = null;
            File file = new File(context.getFilesDir() , FILE_NAME); // Pass getCacheDir() and "MyFile" to read file
            if(file.exists()){
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }
                content = buffer.toString();

                Gson gson = new Gson();
                resultados = (List<Map<String, Object>>)gson.fromJson(content, List.class);
                input.close();
            }
        } catch (Exception e) {
            Util.showException(null, e);
        } finally{

        }
        return resultados;
    }

    /**
     * Salva a lista de notificacaoes push
     * @param context
     * @param arrayFields
     */
    public static void saveNotificationList(Context context, List<Map<String, Object>> arrayFields)
    {
        try {
            System.gc();
            Runtime.getRuntime().gc();

            Gson gson = new Gson();
            String content = gson.toJson(arrayFields);

            // Create a file in the Internal Storage
            FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        }
        catch (Exception e) {
            Util.showException(null, e);
        } finally{

        }
    }

    public static boolean validateEmail(String emailStr) {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        return (emailStr.matches(emailRegex));
    }


    public static int randInt() {
        Random rand = new Random();
        int randomNum = rand.nextInt((50000 - 0) + 1) + 0;
        return randomNum;
    }
}
