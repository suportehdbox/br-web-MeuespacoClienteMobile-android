package br.com.libertyseguros.mobile.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.CitiesBeans;
import br.com.libertyseguros.mobile.beans.InsuranceCoverages;


import java.util.HashMap;
import java.util.List;


import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class InsuranceCoverageAdapater extends BaseExpandableListAdapter {

    private Context context;

    private List<String> listDataHeader;

    private HashMap<String, List<String>>listDataChild;

    private ArrayList<InsuranceCoverages> insuranceCoverages;

    public InsuranceCoverageAdapater(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData, ArrayList<InsuranceCoverages> insuranceCoverages) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.insuranceCoverages = insuranceCoverages;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_insurace_coverage, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_coverage, null);
        }



        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        TextView tvValue = (TextView) convertView.findViewById(R.id.tv_value);

        LinearLayout llValue = (LinearLayout) convertView.findViewById(R.id.ll_value);
        LinearLayout llBaseValue = (LinearLayout) convertView.findViewById(R.id.ll_base_value);


        ImageView ivArrow = (ImageView)  convertView.findViewById(R.id.iv_arrow);

        if(insuranceCoverages.get(groupPosition).getType() == 2){
            tvValue.setVisibility(View.GONE);

        } else {

            try{
                tvValue.setText(insuranceCoverages.get(groupPosition).getValue().trim());
            } catch(Exception ex){
                tvValue.setText("");
            }
        }


        LinearLayout llbase = (LinearLayout) convertView.findViewById(R.id.ll_base);

        if (isExpanded) {
            ivArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_up));
            llbase.setBackgroundColor(context.getResources().getColor(R.color.background_action_bar));
            llBaseValue.setBackgroundColor(context.getResources().getColor(R.color.background_action_bar));
            lblListHeader.setTextColor(context.getResources().getColor(R.color.text_default_1));
            tvValue.setTextColor(context.getResources().getColor(R.color.text_default_1));
            convertView.setPadding(0, 0, 0, 0);

        } else {
            ivArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_down));
            llbase.setBackgroundColor(context.getResources().getColor(R.color.background_eplv));
            llBaseValue.setBackgroundColor(context.getResources().getColor(R.color.background_eplv));
            lblListHeader.setTextColor(context.getResources().getColor(R.color.text_default_3));
            tvValue.setTextColor(context.getResources().getColor(R.color.text_default_3));

            convertView.setPadding(0, 0, 0, 20);

        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getSizeArray(){
        return insuranceCoverages.size();
    }

}
