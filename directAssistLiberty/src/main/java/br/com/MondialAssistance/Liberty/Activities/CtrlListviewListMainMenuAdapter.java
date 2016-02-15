package br.com.MondialAssistance.Liberty.Activities;

import java.util.ArrayList;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.MDL.ListItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CtrlListviewListMainMenuAdapter extends BaseAdapter{

	private ArrayList<ListItem> list;
	private LayoutInflater inflater;
	
	public CtrlListviewListMainMenuAdapter(Context context, ArrayList<ListItem> items){
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

		convertView = inflater.inflate(R.layout.ctrl_listview_listmainmenu, null);
		
		ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
		TextView viewLob = (TextView)convertView.findViewById(R.id.viewLob);
		
		ListItem lob = getItem(position);
		
		imgIcon.setImageDrawable(lob.getIcon());
		viewLob.setText(lob.getName());
		
		return convertView;
	}
}