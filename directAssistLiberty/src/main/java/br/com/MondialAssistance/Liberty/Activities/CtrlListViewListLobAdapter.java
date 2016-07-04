package br.com.MondialAssistance.Liberty.Activities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.MondialAssistance.Liberty.R;

import br.com.MondialAssistance.DirectAssist.MDL.ListItem;

public class CtrlListViewListLobAdapter extends BaseAdapter{

	private ArrayList<ListItem> list;
	private LayoutInflater inflater;
	
	public CtrlListViewListLobAdapter(Context context, ArrayList<ListItem> items){
		this.list = items;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		convertView = inflater.inflate(R.layout.ctrl_listview_listlob, null);
		
		ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
		TextView viewLob = (TextView)convertView.findViewById(R.id.viewLob);
		TextView viewLobID = (TextView)convertView.findViewById(R.id.viewLobID);
		
		ListItem lob = getItem(position);
		
		imgIcon.setImageDrawable(lob.getIcon());
		viewLob.setText(lob.getName());
		viewLobID.setText(String.valueOf(lob.getID()));
		
		return convertView;
	}
}
