package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class ListVehicleAccidentAdpater extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private VehicleAccidentSendBeans list[];

    public ListVehicleAccidentAdpater(Context context, VehicleAccidentSendBeans list[]){
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
            vi = inflater.inflate(R.layout.item_policy, null);
        } else {
            vi = view;
        }

        TextView tvPolicy = (TextView) vi.findViewById(R.id.tv_policy);
        tvPolicy.setText(list[i].getPolicy() + "");

        LinearLayout ll_cointainer_policy = (LinearLayout) vi.findViewById(R.id.ll_cointainer_policy);

        ll_cointainer_policy.removeAllViews();

        View item_detail = inflater.inflate(R.layout.item_detail_policy, null);
        ll_cointainer_policy.addView(item_detail);

        //inflate from item
        TextView tvType = (TextView) item_detail.findViewById(R.id.tv_title_description);

        TextView tvDescription = (TextView) item_detail.findViewById(R.id.tv_description);

        ImageView ivType = (ImageView) item_detail.findViewById(R.id.iv_description);

        tvDescription.setText(list[i].getDescription());


        if (list[i].getBranch().equals(Config.auto)) {
            tvType.setText(context.getString(R.string.car));
            ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.bt_veichle_image));
        } else if (list[i].getBranch().equals(Config.life)) {
            tvType.setText(context.getString(R.string.insured));
            ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_people));
        } else {
            tvType.setText(context.getString(R.string.address));
            ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_home));
        }

        return vi;
    }


}
