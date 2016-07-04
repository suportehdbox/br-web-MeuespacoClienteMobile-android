package br.com.MondialAssistance.Liberty.Activities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.MondialAssistance.DirectAssist.MDL.ListItem;

import br.com.MondialAssistance.Liberty.R;

public class CtrlListViewListItemsDefaultAdapter extends BaseAdapter{

	private ArrayList<ListItem> list;
	private LayoutInflater inflater;
	
	public CtrlListViewListItemsDefaultAdapter(Context context, ArrayList<ListItem> items){
		this.list = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return list.size();
	}

	public ListItem getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.ctrl_listview_listitemsdefault, null);
		
		TextView viewItem = (TextView)convertView.findViewById(R.id.viewItem);
		viewItem.setText(getItem(position).getName());
		
		return convertView;
	}
}