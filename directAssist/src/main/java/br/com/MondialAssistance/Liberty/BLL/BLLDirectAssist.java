/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.Liberty.BLL;

import android.content.Context;
import br.com.MondialAssistance.Liberty.MDL.Action;
import br.com.MondialAssistance.Liberty.MDL.Case;
import br.com.MondialAssistance.Liberty.MDL.Location;
import br.com.MondialAssistance.Liberty.MDL.Provider;
import br.com.MondialAssistance.Liberty.MDL.ServiceDispatch;
import br.com.MondialAssistance.Liberty.MDL.ServiceDispatchCase;
import br.com.MondialAssistance.Liberty.Util.ErrorHelper;
import br.com.MondialAssistance.Liberty.Util.LogHelper;
import br.com.MondialAssistance.Liberty.WS.DirectAssistWS;

import com.neurospeech.wsclient.SoapFaultException;

public class BLLDirectAssist {

	private DirectAssistWS directAssist;
	private Action action;
	
	public Action getAction() {
		return action;
	}
	
	public void SavePolicy(Context context, String deviceUID, Integer manufacturerID, String policyID, long clientID) throws Exception {

		 try {
			 
			 directAssist = new DirectAssistWS();
			 
			 
			 StringBuilder params = new StringBuilder();
				
		 	 params.append(this.getClass().getName())
			       .append(".SavePolicy()")
			       .append("|deviceUID:" + deviceUID)
			  	   .append("|manufacturerID:" + manufacturerID)
		 		   .append("|policyID:" + policyID)
		 		   .append("|clientID:" + clientID);
		 	 
			 setAction(context,
					   clientID,
					   directAssist.SavePolicy(deviceUID, 
							 				   manufacturerID, 
							 				   policyID, 
							 				   clientID),
	 				   params.toString());
			 
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
	}
	
	public void DeletePolicy(Context context, String deviceUID, Integer manufacturerID, String policyID, long clientID) throws Exception 
	{
		 try {
			 
			 directAssist = new DirectAssistWS();
			 
			 
			 StringBuilder params = new StringBuilder();
				
		 	 params.append(this.getClass().getName())
			       .append(".DeletePolicy()")
			       .append("|deviceUID:" + deviceUID)
			  	   .append("|manufacturerID:" + manufacturerID)
		 		   .append("|policyID:" + policyID)
		 		   .append("|clientID:" + clientID);
			 
			 setAction(context,
					   clientID,
					   directAssist.DeletePolicy(deviceUID, 
						  	 				     manufacturerID, 
							 				     policyID, 
							 				    clientID),
		               params.toString());
			 
			 
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
	}
	
	public void SaveAccessLog(Context context, Integer manufacturerID, String deviceUID, Integer applicationID, String applicationVersion, Integer actionCode, String actionParameter, String actionResult, Double latitude, Double longitude, long clientID) throws Exception {
		
		try {
			 
			 directAssist = new DirectAssistWS();
			 
			 directAssist.SaveAccessLog(manufacturerID, 
					 					deviceUID, 
					 					applicationID, 
					 					applicationVersion, 
					 					actionCode, 
					 					actionParameter, 
					 					actionResult, 
					 					clientID,
					 					latitude, 
					 					longitude);

			 
		} catch (SoapFaultException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void CheckCoverages(Context context, Integer fileNumber, String contractNumber, Integer phoneAreaCode, Integer phoneNumber, String fileCause, String serviceCode, Integer problemCode, String fileCity, String fileState, String address, String addressNumber, Double latitude, Double longitude, String reference, long clientID) throws Exception {
		
		try {
			 
			 directAssist = new DirectAssistWS();
			 
			 
			 StringBuilder params = new StringBuilder();
				
		 	 params.append(this.getClass().getName())
			       .append(".CheckCoverages()")
			       .append("|fileNumber:" + fileNumber)
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
				   .append("|reference:" + reference)
			 	   .append("|clientID:" + clientID);
			 
			 setAction(context,
					   clientID,
					   directAssist.CheckCoverages(fileNumber,
							 					   contractNumber, 
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
						 						   longitude, 
						 						   reference).getActionResult(),
					    params.toString());
			 
			 
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
	}
	
	public void CreateServiceDispatch(Context context, Integer fileNumber, String contractNumber, Integer phoneAreaCode, Integer phoneNumber, String fileCause, String serviceCode, Integer problemCode, String fileCity, String fileState, String address, String addressNumber, Double latitude, Double longitude, String reference, String district, String scheduleStartDate, String scheduleEndDate, long clientID) throws Exception {
		
		try {
			 
			 directAssist = new DirectAssistWS();
			 
			 StringBuilder params = new StringBuilder();
			
		 	 params.append(this.getClass().getName())
			       .append(".CreateServiceDispatch()")
			       .append("|fileNumber:" + fileNumber)
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
				   .append("|reference:" + reference)
				   .append("|district:" + district)
				   .append("|scheduleStartDate:" + scheduleStartDate)
				   .append("|scheduleEndDate:" + scheduleEndDate)
			 	   .append("|clientID:" + clientID);
			
			 setAction(context,
					   clientID,
				 directAssist.CreateServiceDispatch(fileNumber,
						 							contractNumber, 
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
						 							longitude, 
						 							reference, 
						 							district, 
						 							scheduleStartDate, 
						 							scheduleEndDate),
						params.toString());
			 
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
	}
	
	public ServiceDispatchCase getDispatchStatus(Context context, Integer caseNumber, Integer requestID, long clientID) throws Exception {
		
		ServiceDispatchCase serviceDispatchCase = new ServiceDispatchCase();
		
		try {
			br.com.MondialAssistance.Liberty.WS.GetServiceDispatch serviceDispatchResult;
			
			ServiceDispatch serviceDispatch;
			Location location;
			Provider provider;
			Case caseItem;
			
			directAssist = new DirectAssistWS();
			
			serviceDispatchResult = directAssist.GetDispatchStatus(caseNumber, 
								 								   requestID);
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getDispatchStatus()")
			      .append("|caseNumber:" + caseNumber)
			      .append("|requestID:" + requestID)
		 		  .append("|clientID:" + clientID);
			
			setAction(context, clientID, serviceDispatchResult.getActionResult(), params.toString());
			
			serviceDispatch = new ServiceDispatch();
			serviceDispatch.setDispatched(serviceDispatchResult.getServiceDispatch().getDispatched());
			serviceDispatch.setScheduled(serviceDispatchResult.getServiceDispatch().getScheduled());
			serviceDispatch.setScheduleStartDate(serviceDispatchResult.getServiceDispatch().getScheduleStartDate());
			serviceDispatch.setScheduleEndDate(serviceDispatchResult.getServiceDispatch().getScheduleEndDate());
			serviceDispatch.setDispatchStatus(serviceDispatchResult.getServiceDispatch().getDispatchStatus());
			serviceDispatch.setDispatchChannel(serviceDispatchResult.getServiceDispatch().getDispatchChannel());
			serviceDispatch.setDispatchDate(serviceDispatchResult.getServiceDispatch().getDispatchDate());
			serviceDispatch.setDispatchParseDate(serviceDispatchResult.getServiceDispatch().getDispatchParseDate());
			serviceDispatch.setArrivalTime(serviceDispatchResult.getServiceDispatch().getArrivalTime());
			serviceDispatch.setClientlatitude(serviceDispatchResult.getServiceDispatch().getClientlatitude());
			serviceDispatch.setClientLongitude(serviceDispatchResult.getServiceDispatch().getClientLongitude());
			
			location = new Location();
			location.setLatitude(serviceDispatchResult.getProvider().getLocation().getLatitude());
			location.setLongitude(serviceDispatchResult.getProvider().getLocation().getLongitude());
			
			provider = new Provider();
			provider.setProviderCode(serviceDispatchResult.getProvider().getProviderCode());
			provider.setLicenseNumber(serviceDispatchResult.getProvider().getLicenseNumber());
			provider.setLocation(location);
			
			caseItem = new Case();
			caseItem.setCaseNumber(serviceDispatchResult.getCaseResult().getCaseNumber());
			caseItem.setCreationDate(serviceDispatchResult.getCaseResult().getCreationDate());
			
			serviceDispatchCase = new ServiceDispatchCase();
			serviceDispatchCase.setServiceDispatch(serviceDispatch);
			serviceDispatchCase.setProvider(provider);
			serviceDispatchCase.setCase(caseItem);
			
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		return serviceDispatchCase;
	}
	
	private void setAction(Context context, long clientID, br.com.MondialAssistance.Liberty.WS.ActionResult value, String params){
		
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
