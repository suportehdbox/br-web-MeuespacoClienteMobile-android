package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.NotificationBeans;

public class ListNotificationTemp extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private NotificationBeans list[];


    public ListNotificationTemp(Context context, NotificationBeans list[]){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
            return list.length;
    }

    @Override
    public Object getItem(int i) {
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi = null;

        if (view == null) {
            vi = inflater.inflate(R.layout.item_notification, null);
        } else {
            vi = view;
        }



        //Log.i("teste", "teste: " + getHtml("teste"));

        return vi;
    }


    private String getHtml(String content){
        String html = "<style>body{font-family:Arial;font-size:14px;padding:10px;color:#003b73;}</style><p>" + content + "</p>";

        return html;
    }


    public void remove(int position) {
        NotificationBeans listTemp[] = new NotificationBeans[list.length - 1];

        boolean remove = false;
        for(int ind = 0; ind < list.length; ind++){


            if(ind == position){
                remove = true;
            } else {
                if(!remove){
                   listTemp[ind] = list[ind];
                } else {
                    listTemp[ind -1] = list[ind];

                }
            }
        }

        notifyDataSetChanged();
    }

}
