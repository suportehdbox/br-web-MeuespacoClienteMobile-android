package br.com.MondialAssistance.Liberty.Activities;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.MondialAssistance.DirectAssist.MDL.AccreditedGarage;

import br.com.MondialAssistance.Liberty.R;

public class CtrlListviewListAccreditedEstablishmentAdapter extends BaseAdapter {

	private Vector<AccreditedGarage> accreditedGarages;
	private LayoutInflater inflater;
	
	public CtrlListviewListAccreditedEstablishmentAdapter(Context context, Vector<AccreditedGarage> items) {
		
		accreditedGarages = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return accreditedGarages.size();
	} 

	public AccreditedGarage getItem(int position) {
		return accreditedGarages.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.ctrl_listview_listaccredited_establishment, null);
		
		TextView viewGarageName = (TextView)convertView.findViewById(R.id.viewGarageName);
		TextView viewDistance = (TextView)convertView.findViewById(R.id.viewDistance);
		
		viewGarageName.setText(getItem(position).getGarageName());
		viewDistance.setText(getItem(position).getDistance().toString() + " km");
		
		return convertView;
	}
}
