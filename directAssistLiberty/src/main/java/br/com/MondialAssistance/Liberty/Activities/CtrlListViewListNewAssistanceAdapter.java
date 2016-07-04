package br.com.MondialAssistance.Liberty.Activities;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.MDL.ListItem;

public class CtrlListViewListNewAssistanceAdapter extends BaseAdapter{

	private ArrayList<ListItem> list;
	private LayoutInflater inflater;
    private ListItem selectedItem;
	public CtrlListViewListNewAssistanceAdapter(Context context, ArrayList<ListItem> items){
		this.list = items;
		Log.e("","size of the list is  --"+list.size());
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
	public void setSelectedItem( ListItem item ){
		selectedItem = item;
		
	}
	public ListItem getSelectedItem(){
		
		return selectedItem;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ListItem item = getItem(position);
		convertView = inflater.inflate(R.layout.ctrl_listview_listnewassistance, null);

		//final RelativeLayout layoutLine = (RelativeLayout)convertView.findViewById(R.id.layoutLine);
		Log.e("","Name of item is -- "+position+"--"+item.getName());
		TextView viewLegField1 = (TextView)convertView.findViewById(R.id.viewLegField1);
		//TextView viewField1 = (TextView)convertView.findViewById(R.id.viewField1);
        final CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBoxId);
        viewLegField1.setText(item.getName());
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					//layoutLine.setOrientation(item.getOrientationLine());
					checkBox.isChecked();
					setSelectedItem(list.get(position));
					//Log.e("","NAme of the selected cause is "+list.get(position).getName());
				}
			}
		});
		

		//viewLegField1.setText(item.getName());
		//viewField1.setText(String.valueOf(item.getDescription()));
		

		return convertView;
	}
}
