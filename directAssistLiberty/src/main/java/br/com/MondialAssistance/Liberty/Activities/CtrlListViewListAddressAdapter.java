package br.com.MondialAssistance.Liberty.Activities;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.MDL.AddressLocation;

public class CtrlListViewListAddressAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Vector<AddressLocation> addressLocations;
	
	public CtrlListViewListAddressAdapter(Context context, Vector<AddressLocation> items) {
		
		addressLocations = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return addressLocations.size();
	}

	public AddressLocation getItem(int position) {
		return addressLocations.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.ctrl_listview_listaddress, null);
		
		TextView viewStreetName = (TextView)convertView.findViewById(R.id.viewStreetName);
		TextView viewDistrict = (TextView)convertView.findViewById(R.id.viewDistrict);
		TextView viewCity = (TextView)convertView.findViewById(R.id.viewCity);
		TextView viewState = (TextView)convertView.findViewById(R.id.viewState);
		TextView viewZipCode = (TextView)convertView.findViewById(R.id.viewZipCode);
		TextView viewLatitude = (TextView)convertView.findViewById(R.id.viewLatitude);
		TextView viewLongitude = (TextView)convertView.findViewById(R.id.viewLongitude);
		
		viewStreetName.setText(getItem(position).getStreetName());
		viewDistrict.setText(getItem(position).getDistrict());
		viewCity.setText(getItem(position).getCity());
		viewState.setText(getItem(position).getState());
		viewZipCode.setText(getItem(position).getZip());
		viewLatitude.setText(getItem(position).getLatitude().toString());
		viewLongitude.setText(getItem(position).getLongitude().toString());
		
		return convertView;
	}
}
 