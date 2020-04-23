package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.VehicleAccidentStatusBeans;
import br.com.libertyseguros.mobile.libray.Config;

public class ListVehicleAccidentStatusAdpater extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private VehicleAccidentStatusBeans.Claims list[];

    private ListListener listListener;

    public ListVehicleAccidentStatusAdpater(Context context, VehicleAccidentStatusBeans.Claims list[], ListListener listener){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        listListener = listener;
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
            vi = inflater.inflate(R.layout.item_vehicle_accident_status, null);
        } else {
            vi = view;
        }
        final int position = i;
        TextView tvPolicy = (TextView) vi.findViewById(R.id.tv_policy);
        tvPolicy.setText(list[i].getPolicy() + "");


        TextView tvDate = (TextView) vi.findViewById(R.id.tv_date);

        try{
            if(list[i].getDate() != null){
                tvDate.setText(getDate(list[i].getDate()));
            }
        }catch(Exception ex){
        }




        TextView tvNumber = (TextView) vi.findViewById(R.id.tv_vehicle_accident);
        tvNumber.setText(list[i].getNumber());


        ImageView iv_addPhoto = (ImageView) vi.findViewById(R.id.iv_add_photo);
        iv_addPhoto.setClickable(true);
        iv_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listListener != null){
                    listListener.OnClickAddPhoto(v, position);
                }
            }
        });

        TextView tvStatus = (TextView) vi.findViewById(R.id.tv_type);

        int statusCode = Integer.parseInt(list[i].getStatus());

        if(statusCode == 10 || statusCode == 40){
            tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_7));
        } else if(list[i].getType() == 100){
            tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_5));
        } else {
            tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_4));
        }

        AdjustableImageView ivType = (AdjustableImageView) vi.findViewById(R.id.iv_type);

        switch(statusCode){
            case 10:
                ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.status_10));
                tvStatus.setText(context.getResources().getString(R.string.status_10));
                break;
            case 20:
                ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.status_20));
                tvStatus.setText(context.getResources().getString(R.string.status_20));
                break;
            case 30:
                ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.status_30));
                tvStatus.setText(context.getResources().getString(R.string.status_30));
                break;
            case 40:
                ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.status_40));
                tvStatus.setText(context.getResources().getString(R.string.status_40));
                break;
            case 50:
                ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.status_50));
                tvStatus.setText(context.getResources().getString(R.string.status_50));
                break;
            case 90:
                tvStatus.setText(context.getResources().getString(R.string.status_90));
                break;
            case 100:
                ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.status_100));
                tvStatus.setText(context.getResources().getString(R.string.status_100));
                break;
        }



        return vi;
    }



    /**
     *
     * @param date
     * @return
     */
    public String getDate(String date){
        String value = "";

        Locale locale = new Locale("pt", "BR");

        date = date.replace("T", " ");
        date = date.replace("Z", " ");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateSdf = null;
        try {
            dateSdf = sdf.parse(date);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        SimpleDateFormat formatter ;

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        value = formatter.format(dateSdf);

        return value;

    }



    public interface ListListener{
        void OnClickAddPhoto(View v, int position);
    }

}
