package br.com.MondialAssistance.Liberty.Activities;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.MondialAssistance.DirectAssist.MDL.AutomotiveCase;
import br.com.MondialAssistance.DirectAssist.MDL.Case;

import br.com.MondialAssistance.Liberty.R;

public class CtrlListViewListMyFilesAdapter extends BaseAdapter {

	private Vector<Case> cases;
	private LayoutInflater inflater;
	
	public CtrlListViewListMyFilesAdapter(Context context){
		
		cases = new Vector<Case>();
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setAutomotiveCases(Vector<AutomotiveCase> automotiveCases){
				
		Case caseItem;
		
		for (AutomotiveCase automotiveCase : automotiveCases) {
			
			caseItem = new Case();
			
			caseItem.setCaseNumber(automotiveCase.getCaseNumber());
			caseItem.setCreationDate(automotiveCase.getCreationDate());
			
			cases.add(caseItem);
		}
	}
	
	public void setAutomakerCases(Vector<AutomotiveCase> automakerCases){
		
		Case caseItem;
		
		for (AutomotiveCase automakerCase : automakerCases) {
			
			caseItem = new Case();
			
			caseItem.setCaseNumber(automakerCase.getCaseNumber());
			caseItem.setCreationDate(automakerCase.getCreationDate());
			
			cases.add(caseItem);
		}
	}
	
	public void setPropertyCases(Vector<Case> propertyCases){
		
		cases = propertyCases;
	}
	
	public int getCount() {
		return cases.size();
	}

	public Case getItem(int position) {
		return cases.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.ctrl_listview_listmyfiles, null);
		
		TextView viewFileNumber = (TextView)convertView.findViewById(R.id.viewFileNumber);
		TextView viewFileDate = (TextView)convertView.findViewById(R.id.viewFileDate);
		
		viewFileNumber.setText(getItem(position).getCaseNumber().toString());
		viewFileDate.setText(getItem(position).getCreationDate());
		
		return convertView;
	}
}