package br.com.libertyseguros.mobile.controller;


import android.content.Context;

import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.NotificationBeans;
import br.com.libertyseguros.mobile.model.BaseModel;
import br.com.libertyseguros.mobile.model.NotificationModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class NotificationController extends BaseModel{

    private NotificationModel notificationModel;

    public NotificationController(OnConnectionResult onConnectionResult){
        notificationModel = new NotificationModel(onConnectionResult);
    }

    /**
     * Get Notification Beans
      * @return
     */
    public  NotificationBeans[] getNotificationBeans(){
        return notificationModel.getNotificationBeans();
    }

    /**
     * Get new notification
     * @param ctx
     */
    public void getNotification(Context ctx){
      notificationModel.getNotification(ctx);
    }
    public void openCanalReport(Context ctx){
        notificationModel.openCanalReport(ctx);
    }



    /**
     * Get message error
     * @return
     */
    public MessageBeans getMessage() {
        return notificationModel.getMessage();
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return notificationModel.getTypeError();


    }

    /**
     * Delete notification in database
     * @param id
     */
    public void deleteNotification(int id){
        notificationModel.deleteNotification(id);
    }

}
