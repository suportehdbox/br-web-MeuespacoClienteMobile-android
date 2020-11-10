package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URLEncoder;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.NotificationBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.DBCustom;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class NotificationModel extends BaseModel{
    private Activity context;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private MessageBeans message;

    private Gson gson;

    private DBCustom dbCustom;

    private NotificationBeans notification[];

    private InfoUser infoUser;

    public NotificationModel(OnConnectionResult onConnectionResult){
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        infoUser = new InfoUser();

    }

    /**
     * Get Notification Beans
      * @return
     */
    public  NotificationBeans[] getNotificationBeans(){
        return notification;
    }

    /**
     * Get new notification
     * @param ctx
     */
    public void getNotification(Context ctx){
        context = (Activity) ctx;
        infoUser.getUserInfo(ctx);
        dbCustom = new DBCustom(context);
        conn = new Connection(context);

        createConnection();

        String param = "";

        try{
            param = "?DeviceId=" + URLEncoder.encode(Config.getDeviceUID(context), "UTF-8");
        } catch(Exception ex){
            ex.printStackTrace();
        }


        conn.startConnection("Notificacao", param, 2, true);
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

                    selectNotification(infoUser.getCpfCnpj(context));
                }catch(Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "NotificationModel: " + result);

                try {
                    if(!result.contains("message")){
                        notification = gson.fromJson(result, NotificationBeans[].class);
/**
                        notification = new NotificationBeans[1];

                        NotificationBeans nb = new NotificationBeans();
                        nb.setDate("10101000");
                        nb.setAlert("teste teste teste <a href=\"http://www.google.com\">Google</a>");

                        notification[0] = nb;
*/
                        for(int ind = 0; ind < notification.length; ind++){
                            insertNotification(notification[ind].getDate(), notification[ind].getAlert(), infoUser.getCpfCnpj(context));
                        }

                        selectNotification(infoUser.getCpfCnpj(context));

                        onConnectionResult.onSucess();

                    } else {
                        selectNotification(infoUser.getCpfCnpj(context));

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

    /**
     * Insert notificaiton in database
     * @param date
     * @param alert
     * @param cpfCnpj
     */
    public void insertNotification(String date, String alert, String cpfCnpj){
        dbCustom.insertBD("Insert into notifications (date, alert, cpfCnpj) VALUES ('" + date + "', '" + alert + "', '" + cpfCnpj + "')");
    }

    /**
     * Delete notification in database
     * @param id
     */
    public void deleteNotification(int id){
        dbCustom.deleteBD("Delete From notifications where id='"+ getNotificationBeans()[id].getId() + "'");

        remove(id);
    }

    /**
     * Select Notification in datanase
     * @param cpfCnpj
     */
    public void selectNotification(String cpfCnpj){

        Cursor mCursor = dbCustom.selectBD("Select * From notifications where cpfCnpj == '" + cpfCnpj + "'");

        if(mCursor.getCount() > 0) {
            notification = new NotificationBeans[mCursor.getCount()];

            int count = 0;
            while (mCursor.moveToNext()) {

                NotificationBeans notificationBeans = new NotificationBeans();
                notificationBeans.setId(mCursor.getInt(mCursor.getColumnIndex("id")));
                notificationBeans.setAlert(mCursor.getString(mCursor.getColumnIndex("alert")));
                notificationBeans.setDate(mCursor.getString(mCursor.getColumnIndex("date")));

                notification[count] = notificationBeans;

                count++;
            }

        }

        mCursor.close();

        dbCustom.close();
    }

    public void remove(int position) {
        NotificationBeans listTemp[] = new NotificationBeans[notification.length - 1];

        boolean remove = false;
        for(int ind = 0; ind < notification.length; ind++){


            if(ind == position){
                remove = true;
            } else {
                if(!remove){
                    listTemp[ind] = notification[ind];
                } else {
                    listTemp[ind -1] = notification[ind];

                }
            }
        }

        notification = listTemp;


    }
}
