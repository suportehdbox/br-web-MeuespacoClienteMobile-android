/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.Liberty.BLL;

import java.util.ArrayList;

import br.com.MondialAssistance.Liberty.MDL.ListItem;


public class BLLClient {

	public ArrayList<ListItem> getCauses(int LobID, String[] items){
		
		ArrayList<ListItem> causes = new ArrayList<ListItem>();
		ListItem cause;
		
		for (String item : items) {
			
			String[] i = item.split(",");
			
			if (i[0].equals(String.valueOf(LobID))){
			
				cause = new ListItem();
				cause.setID(0);
				cause.setCod(i[1]);
				cause.setName(i[2]);
				
				causes.add(cause);
			}
		}
		return causes;
	}
	
	public ArrayList<ListItem> getProblems(String CauseID, String[] items){
		
		ArrayList<ListItem> problems = new ArrayList<ListItem>();
		ListItem problem;
		
		for (String item : items) {
			
			String[] i = item.split(",");
			
			if (i[0].equals(String.valueOf(CauseID))){
			
				problem = new ListItem();
				problem.setID(Integer.valueOf(i[1]));
				problem.setCod("");
				problem.setName(i[2]);
				
				problems.add(problem);
			}
		}
		return problems;
	}
	
	public ArrayList<ListItem> getServices(int LobID, String CauseID, String[] items){
		
		ArrayList<ListItem> services = new ArrayList<ListItem>();
		ListItem service;
		
		for (String item : items) {
			
			String[] i = item.split(",");
			
			if (i[0].equals(String.valueOf(LobID)) && i[1].equals(CauseID)) {
			
				service = new ListItem();
				service.setID(0);
				service.setCod(i[2]);
				service.setName(i[3]);
				
				services.add(service);
				
			} else if (CauseID == null && i[0].equals(String.valueOf(LobID))) {
				
				service = new ListItem();
				service.setID(0);
				service.setCod(i[1]);
				service.setName(i[2]);
				
				services.add(service);				
			}
		}
		
		return services;
	}
	
	public ListItem getDefaultServices(int LobID, String causeID, String[] services, String[] defaultItems){
		return getDefaultServices(LobID, causeID, causeID, services, defaultItems);
	}
	
	public ListItem getDefaultServices(int LobID, String causeID, String cod, String[] services, String[] defaultItems){
		
		for (String item : defaultItems) {
			
			String[] i = item.split(",");
			
			if (i[0].equals(String.valueOf(cod))){
			
				for (ListItem service : getServices(LobID, causeID, services)) {
					
					if (i[1].equals(service.getCod())) {
						return service;
					}
				}
			}
		}
		return null;
	}
	
	public ArrayList<ListItem> getDispatchStatus(String[] items){
		
		ArrayList<ListItem> dispatchStatus = new ArrayList<ListItem>();
		ListItem dispatchStatusItem;
		
		for (String item : items) {
			
			String[] i = item.split(",");
			
			dispatchStatusItem = new ListItem();
			dispatchStatusItem.setID(0);
			dispatchStatusItem.setCod(i[0]);
			dispatchStatusItem.setName(i[1]);
			
			dispatchStatus.add(dispatchStatusItem);
		}
		return dispatchStatus;
	}
	
	public String getDispatchStatusDescriptionByID(String[] items, int id, boolean isSchedule){
		
		for (String item : items) {
			
			String[] i = item.split(",");
			
			if (i[0].equals(String.valueOf(id)) && !isSchedule) {
				return i[1];
			} else if (i[0].equals(String.valueOf(id)) && i[2].equals("1") && isSchedule) {
				return i[1];
			}
		}
		
		return getDispatchStatusDescriptionByID(items, 0, false);
	}
}
