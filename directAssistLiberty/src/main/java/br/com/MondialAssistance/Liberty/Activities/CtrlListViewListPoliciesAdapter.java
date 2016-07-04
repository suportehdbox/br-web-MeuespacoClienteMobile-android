package br.com.MondialAssistance.Liberty.Activities;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.MDL.AutomotivePolicy;
import br.com.MondialAssistance.DirectAssist.MDL.PropertyPolicy;

public class CtrlListViewListPoliciesAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Vector<Items> listItems;
	private boolean edit = false;
	
	public void setEdit(boolean value) {
		edit = value;
	}
	
	public CtrlListViewListPoliciesAdapter(Context context){
		
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listItems = new Vector<Items>();
	}
	
	public void setDataAutomotive(Vector<AutomotivePolicy> automotivePolicies){
		
		Items items;
		
		for (AutomotivePolicy automotivePolicy : automotivePolicies) {
			
			items = new Items();
			items.setID(automotivePolicy.getPolicyID());
			items.setField1(automotivePolicy.getVehicle().getPlate());
			items.setField2(automotivePolicy.getVehicle().getModel());
			items.setField3(automotivePolicy.getInsuredName());
			items.setInformation(automotivePolicy.getVehicle().getModel());
			
			
			listItems.add(items);
		}
	}
	
	public void setDataAutomaker(Vector<AutomotivePolicy> automakerPolicies){
		
		Items items;
		
		for (AutomotivePolicy automakerPolicy : automakerPolicies) {
			
			items = new Items();
			items.setID(automakerPolicy.getPolicyID());
			items.setField1(automakerPolicy.getVehicle().getModel());
		  //items.setField2();
			items.setField3(automakerPolicy.getInsuredName());
			items.setInformation(automakerPolicy.getVehicle().getModel());
			
			listItems.add(items);
		}
		
		setDataAutomotive(automakerPolicies);
	}
	
	public void setDataProperty(Vector<PropertyPolicy> propertyPolicies){
		
		Items items;
		
		for (PropertyPolicy propertyPolicy : propertyPolicies) {
			
			items = new Items();
			items.setID(propertyPolicy.getPolicyID());
			items.setField1(propertyPolicy.getPolicyID());
 		  //items.setField2();
			items.setField3(propertyPolicy.getAddress().toString());
			items.setInformation(propertyPolicy.getPolicyID());
			
			listItems.add(items);
		}
	}

	public int getCount() {
		return listItems.size();
	}

	public Items getItem(int position) {
		return listItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void Remove(int position) {
		listItems.remove(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.ctrl_listview_listpolicies, null);
		
		TextView viewField1 = (TextView)convertView.findViewById(R.id.viewField1);
		TextView viewField2 = (TextView)convertView.findViewById(R.id.viewField2);
		TextView viewField3 = (TextView)convertView.findViewById(R.id.viewField3);
		ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
		
		Items item = getItem(position);
		
		viewField1.setText(item.getField1());
		viewField2.setText(item.getField2());
		viewField3.setText(item.getField3());
		
		if (edit == true) 
			imgIcon.setImageResource(R.drawable.delete);
		else 
			imgIcon.setImageResource(R.drawable.arrow);
		
		return convertView;
	}

	public class Items{
		
		private String ID;
		private String field1;
		private String field2;
		private String field3;
		private String Information;
		
		public String getID() {
			return ID;
		}
		public void setID(String id) {
			ID = id;
		}
		
		public String getField1() {
			return field1;
		}
		public void setField1(String field1) {
			this.field1 = field1;
		}
		
		public String getField2() {
			return field2;
		}
		public void setField2(String field2) {
			this.field2 = field2;
		}
		
		public String getField3() {
			return field3;
		}
		public void setField3(String field3) {
			this.field3 = field3;
		}
		
		public String getInformation() {
			return Information;
		}
		public void setInformation(String information) {
			Information = information;
		}
	}
}
