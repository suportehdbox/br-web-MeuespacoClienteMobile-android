package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Classe adapter de adapter_edittext.xml
 *  
 * @author Evandro
 */
public class RadioButtonAdapter extends BaseAdapter {

	private Context 		context;
	private List<String> 	strings;
	
	public RadioButtonAdapter(Context context, List<String> strings) {
		super();
		this.context = context;
		this.strings = strings;
	}

	@Override
	public int getCount() {
		return strings.size();
	}

	@Override
	public Object getItem(int location) {
		return strings.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		
    	String entry = strings.get(i);

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_checked, null);
        }

        TextView txt = (TextView) view;
        txt.setText(entry);
        txt.setTextColor(Color.BLACK);

        return view;
	}

}
