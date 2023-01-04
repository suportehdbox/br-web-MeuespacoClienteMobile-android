package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.NotificationBeans;
import br.com.libertyseguros.mobile.view.Notification;

public class ListNotification extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private NotificationBeans list[];

    private Notification.OnClickList onClickList;


    public ListNotification(Context context, NotificationBeans list[], Notification.OnClickList onClickList){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.onClickList = onClickList;

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

        AdjustableImageView tvDelete = (AdjustableImageView) vi.findViewById(R.id.iv_delete);
        tvDelete.setTag(i);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickList.onDelete(Integer.parseInt(v.getTag().toString()));
                //remove(Integer.parseInt(v.getTag().toString()));
            }
        });

        TextView tvUndo = (TextView) vi.findViewById(R.id.txt_undo);
        tvUndo.setTag(i);
        tvUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickList.onUndo();
            }
        });



        TextView tvContent = (TextView) vi.findViewById(R.id.txt_content);
        tvContent.setText(Html.fromHtml(list[i].getAlert()));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        tvContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return  Notification.touchListener.onTouch(v, event);
            }
        });

        return vi;
    }


    private String getHtml(String content){
        String html = "<style>body{font-family:Arial;font-size:14px;padding:10px;color:#003b73;}</style><p>" + content + "</p>";

        return html;
    }




}
