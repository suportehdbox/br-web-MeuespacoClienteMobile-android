package br.com.libertyseguros.mobile.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.CitiesBeans;


public class ListCitiesAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private ArrayList<CitiesBeans> list = null;

    private ArrayList<CitiesBeans> fullList = null;

    private ListViewClickListener listener;

    public ListCitiesAdapter(Context context, ArrayList<CitiesBeans> list, ListViewClickListener listViewClickListener){
        this.context = context;

        this.fullList = list;
        this.list = new ArrayList<>();
        this.list.addAll(this.fullList);
        inflater = LayoutInflater.from(context);
        listener = listViewClickListener;
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
            vi = inflater.inflate(R.layout.item_city, null);
        } else {
            vi = view;
        }

        TextView tvContent = (TextView) vi.findViewById(R.id.txt_content);

        tvContent.setText(list.get(i).getCity());
        vi.setTag(i);
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(list.get(Integer.parseInt(v.getTag().toString())));
            }
        });


        return vi;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(fullList);
        } else {
            for (CitiesBeans city : fullList) {
                if (city.getCity().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(city);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface ListViewClickListener{
        void onItemClicked(CitiesBeans city);
    }
}
