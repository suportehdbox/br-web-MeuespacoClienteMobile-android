package br.com.libertyseguros.mobile.libray;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import br.com.libertyseguros.mobile.R;


public class PolicyCalc {


    private String diffDate;

    private long progressAngle;

    public long getProgressAngle() {
        return progressAngle;
    }

    public void setProgressAngle(long progressAngle) {
        this.progressAngle = progressAngle;
    }


    /**
     * Get Money
     * @param money
     * @param context
     * @return
     */
    public String getMoney(String money, Context context){
        try{
            money = money.replace(".", ",");
            money = context.getString(R.string.money) + " " + money;
        } catch (Exception ex){
            ex.printStackTrace();
        }



        return  money;
    }

    /**
     *
     * @param date
     * @return
     */
    public String getDate(String date, Context context, int type){
        String value = "";

        try {
            Locale locale = new Locale("pt", "BR");

            date = date.replace("T", " ");
            date = date.replace("Z", " ");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateSdf = null;
            try {
                dateSdf = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            SimpleDateFormat formatter;

            if (type == 1) {


                formatter = new SimpleDateFormat("dd-MMMM");
                value = formatter.format(dateSdf);
                value = value.replace("-", " " + context.getString(R.string.in) + " ");

            } else {
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                value = formatter.format(dateSdf);

            }
        }catch (Exception ex){
            value = date;
        }

        return value;

    }


    /**
     *
     * @param date
     * @return
     */
    public String getDateExtends(String date, Context context, int type){
        String value = "";

        Locale locale = new Locale("pt", "BR");

        date = date.replace("T", " ");
        date = date.replace("Z", " ");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateSdf = null;
        try {
            dateSdf = sdf.parse(date);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        SimpleDateFormat formatter ;

        if(type == 1){


            formatter = new SimpleDateFormat("dd-MMMM");
            value = formatter.format(dateSdf);
            value = value.replace("-", " " +  context.getString(R.string.in) +" ");

        } else {
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            value = formatter.format(dateSdf);

        }

        return value;

    }

    private String tableIPVA(int value){

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        String date = "";

        switch(value){

            case 1:
                date = year + "-03-13 11:59:00";
                break;
            case 2:
                date = year + "-03-14 11:59:00";
                break;
            case 3:
                date = year + "-03-17 11:59:00";
                break;
            case 4:
                date = year + "-03-18 11:59:00";
                break;
            case 5:
                date = year + "-03-19 11:59:00";
                break;
            case 6:
                date = year + "-03-20 11:59:00";
                break;
            case 7:
                date = year + "-03-20 11:59:00";
                break;
            case 8:
                date = year + "-03-24 11:59:00";
                break;
            case 9:
                date = year + "-03-25 11:59:00";
                break;
            case 0:
                date = year + "-03-26 11:59:00";
                break;
        }

        return date;
    }

    private String tableLinc(int value){

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        String date = "";

        switch(value){
            case 0:
                date = year + "-12-30 11:59:00";
                break;
            case 1:
                date = year + "-04-30 11:59:00";
                break;
            case 2:
                date = year + "-05-31 11:59:00";
                break;
            case 3:
                date = year + "-06-30 11:59:00";
                break;
            case 4:
                date = year + "-07-31 11:59:00";
                break;
            case 5:
            case 6:
                date = year + "-08-30 11:59:00";
                break;
            case 7:
                date = year + "-09-31 11:59:00";
                break;
            case 8:
                date = year + "-10-30 11:59:00";
                break;
            case 9:
                date = year + "-11-31 11:59:00";
                break;

        }

        return date;
    }

    public long getIPVA(int i){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        dateFormat.format(date);

        String dateEnd = tableIPVA(i);
        Date dateE = null;

        try{
            dateE = dateFormat.parse(dateEnd);
        } catch (ParseException ex){

        }

        long Y = calcData(date, dateE);



        long value = 0;

        if(Y < 0){
            Y = 0;
        } else {
            value = (int) (Y * 100) / 365;
        }

        diffDate = Y + "";

        progressAngle = 180 - value;

        return  value;
    }

    public long getLinc(int i){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        dateFormat.format(date);

        String dateEnd = tableLinc(i);
        Date dateE = null;

        try{
            dateE = dateFormat.parse(dateEnd);
        } catch (ParseException ex){

        }

        long Y = calcData(date, dateE);



        long value = 0;

        if(Y < 0){
            Y = 0;
        } else {
            value = (int) (Y * 100) / 365;
        }

        diffDate = Y + "";


        return  value;
    }



    public long getPoliycDateDiff(String dateStart, String dateEnd){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dateStart = dateStart.replace("T", " ");
        dateStart = dateStart.replace("Z", " ");

        dateEnd = dateEnd.replace("T", " ");
        dateEnd = dateEnd.replace("Z", " ");

        Date dateS = null;
        Date dateE = null;

        try{
             dateS = myFormat.parse(dateStart);
             dateE = myFormat.parse(dateEnd);
        } catch (ParseException ex){

        }

        long X  = calcData(dateS, dateE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();

        long Y = calcData(date, dateE);


        float value = 0;

        if(Y < 0){
            Y = 0;
        } else {
            if(X == 0){
                X = 365;
            }

            value = (int) (Y * 100) / X;


        }

        diffDate = Y + "";

        return (int) value;
    }

    public long calcData(Date date1, Date date2){

        double diff = 0;

        try {

            diff = (date2.getTime() - date1.getTime());
            diff = diff / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return (long) Math.ceil(diff);

    }


    public String getDay(){
        return diffDate;
    }

}
