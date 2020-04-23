package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.SalesmanBeans;

public class SalesmanAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private ArrayList<SalesmanBeans> list;

    public SalesmanAdapter(Context context, ArrayList<SalesmanBeans> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
            return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi = null;

        if (view == null) {
            vi = inflater.inflate(R.layout.item_salesman, null);
        } else {
            vi = view;
        }




        TextView tvPolicy = (TextView) vi.findViewById(R.id.tv_policy);
        tvPolicy.setText(list.get(i).getPolicy() + "");

        TextView tvPhone = (TextView) vi.findViewById(R.id.tv_phone);
        tvPhone.setText(list.get(i).getPhone());

        TextView tvName = (TextView) vi.findViewById(R.id.tv_name);
        tvName.setText(list.get(i).getDescription());

        TextView tvEmail = (TextView) vi.findViewById(R.id.tv_email);
        tvEmail.setText(list.get(i).getEmail());
        tvEmail.setTag(i);
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(v.getTag().toString());

                sendEmail(list.get(index).getEmail());
            }
        });


        LinearLayout llContent = (LinearLayout) vi.findViewById(R.id.ll_cb);
        llContent.setTag(i);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(v.getTag().toString());

                openCall(list.get(index).getPhone());
            }
        });
        return vi;
    }

    /**
     * Open Intent call
     */
    public void openCall(String phone){

        String number = phone;
        Uri number1 = Uri.parse("tel:" + number);
        Intent dial = new Intent(Intent.ACTION_DIAL);
        dial.setData(number1);
        context.startActivity(dial);
    }


    /**
     * Send Email
     */
    public void sendEmail(String email){

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ email});
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

}
