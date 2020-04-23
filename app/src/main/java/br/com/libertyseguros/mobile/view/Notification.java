package br.com.libertyseguros.mobile.view;


import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ListNotification;
import br.com.libertyseguros.mobile.beans.NotificationBeans;
import br.com.libertyseguros.mobile.controller.NotificationController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;

public class Notification extends BaseActionBar {


    private NotificationController notificationController;

    public static  SwipeToDismissTouchListener<ListViewAdapter> touchListener;

    public static  ListView lvNotification;

    private ArrayList<NotificationBeans> beans;

    private ListNotification adapter;

    private boolean value;

    private LinearLayout llLoading;

    public static boolean blocked;

    private LinearLayout llMessageError;

    private OnClickList onClickList;

    private int position;


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_notification);

        getSupportActionBar().setTitle(getString(R.string.title_action_bar_10));


        mFirebaseAnalytics.setCurrentScreen(Notification.this, "Notificações", null);

        LoadFile lf = new LoadFile();
        lf.savePref(Config.TAGNOTIFICAITONNEW, "0", Config.TAG, this);

        blocked = true;

        onClickList = new OnClickList() {
            @Override
            public void onUndo() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        touchListener.undoPendingDismiss();
                    }
                });
            }

            @Override
            public void onDelete(int pos) {
                position = pos;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notificationController.deleteNotification(position);
                        touchListener.undoPendingDismiss();
                        init(lvNotification);

                        if(notificationController.getNotificationBeans().length == 0){
                            lvNotification.setVisibility(View.GONE);
                            llMessageError.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };

        notificationController = new NotificationController(new OnConnectionResult() {
            @Override
            public void onError() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try {
                            showLoading(false);
                            if (notificationController.getNotificationBeans() == null) {
                                lvNotification.setVisibility(View.GONE);
                                llMessageError.setVisibility(View.VISIBLE);
                            } else {
                                init(lvNotification);
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        }
                    });
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess() {
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        try{
                            showLoading(false);
                            init(lvNotification);
                        } catch(Exception ex){
                            ex.printStackTrace();
                        }
                        }
                    });
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        llMessageError = (LinearLayout) findViewById(R.id.ll_message_error);

        lvNotification = (ListView) findViewById(R.id.lv_notification);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        beans = new ArrayList<>();


        showLoading(true);
        notificationController.getNotification(this);

    }

    private void init(ListView listView) {
         adapter = new ListNotification(this, notificationController.getNotificationBeans(), onClickList);
        listView.setAdapter(adapter);



             touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {

                                return true;

                            }


                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                //adapter.remove(position);
                              //

                            }
                        });


        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getId() == R.id.iv_delete) {
                    notificationController.deleteNotification(notificationController.getNotificationBeans()[position].getId());
                } else if (view.getId() == R.id.txt_undo) {
                    touchListener.undoPendingDismiss();
                }else if (touchListener.existPendingDismisses()) {
                     touchListener.undoPendingDismiss();
                }
            }
        });


    }



    /* Show progress loading
   * @param v
   * @param m
   */
    private void showLoading(boolean v) {
        this.value = v;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    llLoading.setVisibility(View.VISIBLE);
                    lvNotification.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    lvNotification.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    public interface OnClickList{
        public void onUndo();
        public void  onDelete(int id);
    }


}
