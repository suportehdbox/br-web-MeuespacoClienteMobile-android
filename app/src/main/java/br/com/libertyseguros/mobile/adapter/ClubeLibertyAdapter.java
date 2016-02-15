package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;

public class ClubeLibertyAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> itens;

	public ClubeLibertyAdapter(Context context, ArrayList<String> itens) {
		super();
		this.context = context;
		this.itens = itens;
	}

	@Override
	public int getCount() {
		return itens.size();
	}

	@Override
	public Object getItem(int location) {
		return itens.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		
		String entry = itens.get(i);

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.itemlistview_clubeliberty, null);
        }

        TextView categoria = (TextView)view.findViewById(R.id.textCategoria);
		categoria.setText(entry);
		
        return view;
	}


}
