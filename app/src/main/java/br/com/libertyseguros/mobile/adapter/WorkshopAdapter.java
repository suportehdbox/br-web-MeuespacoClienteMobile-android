package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.WorkshopBeans;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.TextViewCustom;

public class WorkshopAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private ArrayList<WorkshopBeans> list;

    private LatLng latLng;

    private Toast toast;

    public WorkshopAdapter(Context context, ArrayList<WorkshopBeans> list, LatLng latLng){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.latLng = latLng;
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
            vi = inflater.inflate(R.layout.item_workshop, null);
        } else {
            vi = view;
        }

        TextView tvDistance = (TextView) vi.findViewById(R.id.tv_distance);
        tvDistance.setText(list.get(i).getDistance() + " " + context.getResources().getString(R.string.km));

        TextView tvName = (TextView) vi.findViewById(R.id.tv_name);
        tvName.setText(list.get(i).getName());


        TextView tv_recommended = (TextView) vi.findViewById(R.id.tv_recommended);
        if(list.get(i).getIndication()){
            tv_recommended.setVisibility(View.VISIBLE);
        }else{
            tv_recommended.setVisibility(View.GONE);
        }
        TextView tv_full = (TextView) vi.findViewById(R.id.tv_full_workshop);
        if(!list.get(i).getAvailable()){
            tv_full.setVisibility(View.VISIBLE);
        }else{
            tv_full.setVisibility(View.GONE);
        }
        TextView tvAddress = (TextView) vi.findViewById(R.id.tv_address);
        tvAddress.setText(list.get(i).getAddress().replace(" - ", "\n"));

        TextViewCustom tvPhone = (TextViewCustom) vi.findViewById(R.id.tv_phone);
        tvPhone.setText(list.get(i).getPhone());
        tvPhone.setTag(i);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String phone = list.get(Integer.parseInt(v.getTag().toString())).getPhone();
                openCall(context, phone);
            }
        });

        ImageViewCustom ivRoute = (ImageViewCustom) vi.findViewById(R.id.ib_route);
        ivRoute.setTag(i);
        ivRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoute(context, Integer.parseInt(v.getTag().toString()));
            }
        });

        TextView tvOperation = (TextView) vi.findViewById(R.id.tv_operation);
        tvOperation.setText(createOperation(i));
        return vi;
    }

    /**
     * Open Intent call
     */
    public void openCall(Context context, String phone){

        String number = phone;
        Uri number1 = Uri.parse("tel:" + number);
        Intent dial = new Intent(Intent.ACTION_DIAL);
        dial.setData(number1);
        context.startActivity(dial);
    }

    /**
     * Open Intent route
     * @param context
     */
    public void openRoute(Context context, int ind){
        try {
            if (list.size() > 0 && latLng != null) {
                // Intent navigationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + list.get(ind).getLatitude() +"," + ".getLongitude() + "&daddr="+ latLng.latitude + "," + latLng.longitude));
                //context.startActivity(Intent.createChooser(navigationIntent, "Select an application"));
               // String uri = "geo:" + latLng.latitude + "," + latLng.latitude + "?q=" + list.get(ind).getLatitude() + "," + list.get(ind).getLongitude();
                //context.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));


                String url = "http://maps.google.com/maps?saddr=" + latLng.latitude + "," + latLng.longitude + "&daddr=" + list.get(ind).getLatitude() + "," + list.get(ind).getLongitude();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }
        }catch(Exception ex){
            ex.printStackTrace();

            if(toast != null){
                toast.cancel();
            }

            toast = Toast.makeText(context, context.getResources().getString(R.string.error_route), Toast.LENGTH_LONG);
            toast.show();

        }
    }

    /**
     * Create Operation hours
     * @param index
     * @return
     */
    private String createOperation(int index){
        String operation = "";

        try{
            if(list.get(index).getWeekStart() != null){
                operation = context.getResources().getString(R.string.operation_part_1) + " " + list.get(index).getWeekStart().substring(0, list.get(index).getWeekStart().length() - 3) + " ";

                operation += context.getResources().getString(R.string.operation_part_2) + " " +  list.get(index).getWeekEnd().substring(0, list.get(index).getWeekEnd().length() - 3);

                if(list.get(index).getSaturdayStart() != null){
                    operation +=  " " + context.getResources().getString(R.string.operation_part_3) + " " +  list.get(index).getSaturdayStart().substring(0, list.get(index).getSaturdayStart().length() - 3);

                    operation += " " + context.getResources().getString(R.string.operation_part_2) + " " +  list.get(index).getSaturdayEnd().substring(0, list.get(index).getSaturdayEnd().length() - 3);
                }
            }

        }catch (Exception ex){
            operation = "";
        }

        return operation;
    }


}
