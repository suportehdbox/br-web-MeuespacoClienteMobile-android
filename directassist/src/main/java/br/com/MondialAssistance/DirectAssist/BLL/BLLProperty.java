/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.DirectAssist.BLL;

import java.util.Vector;

import android.content.Context;
import br.com.MondialAssistance.DirectAssist.MDL.Action;
import br.com.MondialAssistance.DirectAssist.MDL.Address;
import br.com.MondialAssistance.DirectAssist.MDL.Case;
import br.com.MondialAssistance.DirectAssist.MDL.PropertyPolicy;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.DirectAssist.Util.LogHelper;
import br.com.MondialAssistance.DirectAssist.WS.Property;

import com.neurospeech.wsclient.SoapFaultException;

public class BLLProperty {

	private Property property;
	private Action action;
	
	public Action getAction() {
		return action;
	}
	
	public PropertyPolicy getPolicy(Context context, String zipcode, String document, long clientID) throws Exception {
		
		PropertyPolicy propertyPolicy = new PropertyPolicy();
		
		try {
			br.com.MondialAssistance.DirectAssist.WS.GetPropertyPoliciesResult getPropertyPolicies;
			Address address;
			
			property = new Property();

			getPropertyPolicies = property.GetPolicy(zipcode, 
													 document,  
													 clientID);
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getPolicy()")
				  .append("|zipcode:" + zipcode)
				  .append("|document:" + document)
		 		  .append("|clientID:" + clientID);
			
			setAction(context, clientID, getPropertyPolicies.getActionResult(), params.toString());
			
			
			address = new Address();
			address.setStreetName(getPropertyPolicies.getPropertyPolicy().getAddress().getStreetName());
			address.setHouseNumber(getPropertyPolicies.getPropertyPolicy().getAddress().getHouseNumber());
			address.setDistrict(getPropertyPolicies.getPropertyPolicy().getAddress().getDistrict());
			address.setComplement(getPropertyPolicies.getPropertyPolicy().getAddress().getComplement());
			address.setCity(getPropertyPolicies.getPropertyPolicy().getAddress().getCity());
			address.setState(getPropertyPolicies.getPropertyPolicy().getAddress().getState());
			address.setLatitude(getPropertyPolicies.getPropertyPolicy().getAddress().getLatitude());
			address.setLongitude(getPropertyPolicies.getPropertyPolicy().getAddress().getLongitude());
					
			propertyPolicy.setPolicyID(getPropertyPolicies.getPropertyPolicy().getPolicyID());
			propertyPolicy.setInsuredName(getPropertyPolicies.getPropertyPolicy().getInsuredName());
			propertyPolicy.setContractStartDate(getPropertyPolicies.getPropertyPolicy().getContractStartDate());
			propertyPolicy.setContractStartDate(getPropertyPolicies.getPropertyPolicy().getContractStartDate());
			propertyPolicy.setActiveStatus(getPropertyPolicies.getPropertyPolicy().getActiveStatus());
			propertyPolicy.setAddress(address);
			
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return propertyPolicy;
	}
	
	public Case CreateCase(Context context, String contractNumber, Integer phoneAreaCode, Integer phoneNumber, String fileCause, String serviceCode, Integer problemCode, String fileCity, String fileState, String address, String addressNumber, Double latitude, Double longitude, long clientID) throws Exception {
		
		Case item = new Case();
		
		try {
			br.com.MondialAssistance.DirectAssist.WS.CreateCaseResult caseResult;
			
			property = new Property();
			
			caseResult = property.CreateCase(contractNumber, 
											   phoneAreaCode, 
											   phoneNumber, 
											   fileCause, 
											   serviceCode, 
											   problemCode, 
											   fileCity, 
											   fileState, 
											   address, 
											   addressNumber, 
											   latitude, 
											   longitude);
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".CreateCase()")
				  .append("|contractNumber:" + contractNumber)
				  .append("|phoneAreaCode:" + phoneAreaCode)
				  .append("|phoneNumber:" + phoneNumber)
				  .append("|fileCause:" + fileCause)
				  .append("|serviceCode:" + serviceCode)
				  .append("|problemCode:" + problemCode)
				  .append("|fileCity:" + fileCity)
				  .append("|fileState:" + fileState)
				  .append("|address:" + address)
				  .append("|addressNumber:" + addressNumber)
				  .append("|latitude:" + latitude)
				  .append("|longitude:" + longitude)
		 		  .append("|clientID:" + clientID);
			
			setAction(context, clientID, caseResult.getActionResult(), params.toString());
			
			
			item.setCaseNumber(caseResult.getCaseNumber());
			
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return item;
	}
	
	public Vector<PropertyPolicy> getListPolicies(Context context, String deviceUID, Integer manufacturerID, long clientID) throws Exception {
		
		Vector<PropertyPolicy> items = new Vector<PropertyPolicy>();
		
		try {
			br.com.MondialAssistance.DirectAssist.WS.ListPropertyPoliciesResult listPropertyPolicies;
			PropertyPolicy propertyPolicy;
			Address address;
		
			property = new Property();
			
			listPropertyPolicies = property.ListPolicies(deviceUID, 
							                             manufacturerID, 
							                             clientID); 
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getListPolicies()")
				  .append("|deviceUID:" + deviceUID)
				  .append("|manufacturerID:" + manufacturerID)
		 		  .append("|clientID:" + clientID);
			
			setAction(context, clientID, listPropertyPolicies.getActionResult(), params.toString());
			
			
			for (br.com.MondialAssistance.DirectAssist.WS.PropertyPolicy item : listPropertyPolicies.getPropertyPolicies()) {
				
				address = new Address();
				address.setStreetName(item.getAddress().getStreetName());
				address.setHouseNumber(item.getAddress().getHouseNumber());
				address.setDistrict(item.getAddress().getDistrict());
				address.setComplement(item.getAddress().getComplement());
				address.setCity(item.getAddress().getCity());
				address.setState(item.getAddress().getState());
				address.setLatitude(item.getAddress().getLatitude());
				address.setLongitude(item.getAddress().getLongitude());
				
				propertyPolicy = new PropertyPolicy();
				propertyPolicy.setPolicyID(item.getPolicyID());
				propertyPolicy.setInsuredName(item.getInsuredName());
				propertyPolicy.setContractStartDate(item.getContractStartDate());
				propertyPolicy.setContractStartDate(item.getContractStartDate());
				propertyPolicy.setActiveStatus(item.getActiveStatus());
				propertyPolicy.setAddress(address);
				
				items.add(propertyPolicy);	
			}
			
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return items;
	}
	
	public Vector<Case> getListCases(Context context, String deviceUID, Integer manufacturerID, long clientID) throws Exception {
		
		Vector<Case> items = new Vector<Case>();
		
		try {
			br.com.MondialAssistance.DirectAssist.WS.ListPropertyCasesResult listPropertyCases;
			Case caseItem;
			
			property = new Property();
			
			listPropertyCases = property.ListCases(deviceUID, 
					                               manufacturerID, 
					                               clientID);
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getListCases()")
			      .append("|deviceUID:" + deviceUID)
			      .append("|manufacturerID:" + manufacturerID)
		 		  .append("|clientID:" + clientID);
			
			setAction(context, clientID, listPropertyCases.getActionResult(), params.toString());
			
			
			for (br.com.MondialAssistance.DirectAssist.WS.Case item : listPropertyCases.getCases()) {
				
				caseItem = new Case();
				caseItem.setCaseNumber(item.getCaseNumber());
				caseItem.setCreationDate(item.getCreationDate());
				
				items.add(caseItem);
			}
			
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return items;
	}
	
	private void setAction(Context context, long clientID, br.com.MondialAssistance.DirectAssist.WS.ActionResult value, String params){
		
		try {
			action = new Action();
			
			action.setResultCode(value.getResultCode());
			action.setErrorMessage(value.getErrorMessage());
			
			LogHelper.sendLog(context, clientID, action, params);
			
		} catch (Exception e) {
			ErrorHelper.setErrorMessage(e);
		}
	}
}
