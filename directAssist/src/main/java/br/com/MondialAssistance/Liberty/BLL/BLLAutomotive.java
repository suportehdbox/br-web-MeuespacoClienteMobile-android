/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.Liberty.BLL;

import java.util.Vector;

import android.content.Context;
import br.com.MondialAssistance.Liberty.MDL.AccreditedGarage;
import br.com.MondialAssistance.Liberty.MDL.Action;
import br.com.MondialAssistance.Liberty.MDL.Address;
import br.com.MondialAssistance.Liberty.MDL.AutomotiveCase;
import br.com.MondialAssistance.Liberty.MDL.AutomotivePolicy;
import br.com.MondialAssistance.Liberty.MDL.Case;
import br.com.MondialAssistance.Liberty.MDL.Phone;
import br.com.MondialAssistance.Liberty.MDL.Vehicle;
import br.com.MondialAssistance.Liberty.Util.ErrorHelper;
import br.com.MondialAssistance.Liberty.Util.LogHelper;
import br.com.MondialAssistance.Liberty.WS.Automotive;

import com.neurospeech.wsclient.SoapFaultException;

public class BLLAutomotive {

	private Automotive automotive;
	private Action action;
	
	public Action getAction() {
		return action;
	}
	
	public Vector<AutomotivePolicy> getListPolicies(Context context, String deviceUID, Integer manufacturerID, long clientID) throws Exception {
		
		Vector<AutomotivePolicy> items = new Vector<AutomotivePolicy>();
		
		try {
		
			br.com.MondialAssistance.Liberty.WS.ListPoliciesResult listPolicies;
			AutomotivePolicy automotivePolicy;
			Vehicle vehicle;
			
			automotive = new Automotive();
			
			listPolicies = automotive.ListPolicies(deviceUID, 
												   manufacturerID, 
												   clientID); 

			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getListPolicies()")
			      .append("|deviceUID:" + deviceUID)
			      .append("|manufacturerID:" + manufacturerID)
				  .append("|clientID:" + clientID);
			
			setAction(context, clientID, listPolicies.getActionResult(), params.toString());
			
			
			for (br.com.MondialAssistance.Liberty.WS.AutomotivePolicy item : listPolicies.getAutomotivePolicies()) {
				
				vehicle = new Vehicle();
				vehicle.setId(item.getVehicle().getId());
				vehicle.setChassi(item.getVehicle().getChassi());
				vehicle.setLicenseNumber(item.getVehicle().getLicenseNumber());
				vehicle.setMake(item.getVehicle().getMake());
				vehicle.setModel(item.getVehicle().getModel());
				vehicle.setColor(item.getVehicle().getColor());
				vehicle.setPlate(item.getVehicle().getPlate());
				vehicle.setVehicleYear(item.getVehicle().getVehicleYear());
					
				automotivePolicy = new AutomotivePolicy();
				automotivePolicy.setGroupID(item.getGroupID());
				automotivePolicy.setPolicyID(item.getPolicyID());
				automotivePolicy.setInsuredName(item.getInsuredName());
				automotivePolicy.setContractStartDate(item.getContractStartDate());
				automotivePolicy.setContractEndDate(item.getContractEndDate());
				automotivePolicy.setActiveStatus(item.getActiveStatus());
				automotivePolicy.setVehicle(vehicle);
				
				items.add(automotivePolicy);	
			}
		
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return items;
	}
	
	public Case CreateCase(Context context, String contractNumber, Integer phoneAreaCode, Integer phoneNumber, String fileCause, String serviceCode, Integer problemCode, String fileCity, String fileState, String address, String addressNumber, Double latitude, Double longitude, long clientID) throws Exception {
		
		Case item = new Case();
		
		try {
		
			br.com.MondialAssistance.Liberty.WS.CreateCaseResult caseResult;
			
			automotive = new Automotive();
			
			caseResult = automotive.CreateCase(contractNumber, 
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
	
	public Vector<AutomotiveCase> getListCases(Context context, String deviceUID, Integer manufacturerID, long clientID) throws Exception {
		
		Vector<AutomotiveCase> items = new Vector<AutomotiveCase>();
		
		try {
			
			br.com.MondialAssistance.Liberty.WS.ListAutomotiveCasesResult listAutomotiveCases;
			AutomotiveCase automotiveCase;
			Vehicle vehicle;
			
			automotive = new Automotive();
			
			listAutomotiveCases = automotive.ListCases(deviceUID, 
													   manufacturerID,
													   clientID);
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getListCases()")
			      .append("|deviceUID:" + deviceUID)
			      .append("|manufacturerID:" + manufacturerID)
				  .append("|clientID:" + clientID);
			
			setAction(context, clientID, listAutomotiveCases.getActionResult(), params.toString());
			
					
			for (br.com.MondialAssistance.Liberty.WS.AutomotiveCase item : listAutomotiveCases.getAutomotiveCases()) {
				
				vehicle = new Vehicle();
				vehicle.setId(item.getVehicle().getId());
				vehicle.setChassi(item.getVehicle().getChassi());
				vehicle.setLicenseNumber(item.getVehicle().getLicenseNumber());
				vehicle.setMake(item.getVehicle().getMake());
				vehicle.setModel(item.getVehicle().getModel());
				vehicle.setColor(item.getVehicle().getColor());
				vehicle.setPlate(item.getVehicle().getPlate());
				vehicle.setVehicleYear(item.getVehicle().getVehicleYear());
						
				automotiveCase = new AutomotiveCase();
				automotiveCase.setCaseNumber(item.getCaseNumber());
				automotiveCase.setCreationDate(item.getCreationDate());
				automotiveCase.setVehicle(vehicle);
				
				items.add(automotiveCase);
			}
		
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return items;
	}
	
	public Vector<AccreditedGarage> getListAccreditedEstablishment(Context context, Double latitude, Double longitude, long clientID) throws Exception {
		
		Vector<AccreditedGarage> items = new Vector<AccreditedGarage>();

		try {
		
			br.com.MondialAssistance.Liberty.WS.ListAccreditedGarages listAccreditedGarages;
			AccreditedGarage accreditedGarages;
			Address address;
			Phone phone;
			
			automotive = new Automotive();
			
			listAccreditedGarages = automotive.ListAccreditedEstablishment(latitude,
	   					                                                   longitude,
	   					                                                   clientID);
	
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getListAccreditedEstablishment()")
			      .append("|latitude:" + latitude)
			      .append("|longitude:" + longitude)
				  .append("|clientID:" + clientID);
			
			setAction(context, clientID, listAccreditedGarages.getActionResult(), params.toString());
			
			
			for (br.com.MondialAssistance.Liberty.WS.AccreditedGarage item : listAccreditedGarages.getAccreditedGarages()) {
				
				address = new Address();
				address.setStreetName(item.getAddress().getStreetName());
				address.setHouseNumber(item.getAddress().getHouseNumber());
				address.setDistrict(item.getAddress().getDistrict());
				address.setComplement(item.getAddress().getComplement());
				address.setCity(item.getAddress().getCity());
				address.setState(item.getAddress().getState());
				address.setLatitude(item.getAddress().getLatitude());
				address.setLongitude(item.getAddress().getLongitude());
	
				phone = new Phone();
				phone.setCountryCode(item.getPhone().getCountryCode());
				phone.setAreaCode(item.getPhone().getAreaCode());
				phone.setPhoneNumber(item.getPhone().getPhoneNumber());
				
				accreditedGarages = new AccreditedGarage();
				accreditedGarages.setGarageName(item.getGarageName());
				accreditedGarages.setDistance(item.getDistance());
				accreditedGarages.setAddress(address);
				accreditedGarages.setPhone(phone);
				
				items.add(accreditedGarages);
			}
		
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return items;
	}
	
	public AutomotivePolicy getPolicy(Context context, String plate, String document, long clientID) throws Exception {
		
		AutomotivePolicy automotivePolicy = new AutomotivePolicy();
		
		try {
		
			br.com.MondialAssistance.Liberty.WS.GetPolicyResult getPolicy;
			Vehicle vehicle;
			
			automotive = new Automotive();
	
			getPolicy = automotive.GetPolicy(plate, 
										 	 document, 
					                         Integer.parseInt(String.valueOf(clientID)));
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getPolicy()")
			      .append("|plate:" + plate)
			      .append("|document:" + document)
				  .append("|clientID:" + clientID);
			
			setAction(context, clientID, getPolicy.getActionResult(), params.toString());
			
			
			vehicle = new Vehicle();
			vehicle.setId(getPolicy.getAutomotivePolicy().getVehicle().getId());
			vehicle.setChassi(getPolicy.getAutomotivePolicy().getVehicle().getChassi());
			vehicle.setLicenseNumber(getPolicy.getAutomotivePolicy().getVehicle().getLicenseNumber());
			vehicle.setMake(getPolicy.getAutomotivePolicy().getVehicle().getMake());
			vehicle.setModel(getPolicy.getAutomotivePolicy().getVehicle().getModel());
			vehicle.setColor(getPolicy.getAutomotivePolicy().getVehicle().getColor());
			vehicle.setPlate(getPolicy.getAutomotivePolicy().getVehicle().getPlate());
			vehicle.setVehicleYear(getPolicy.getAutomotivePolicy().getVehicle().getVehicleYear());
			
			automotivePolicy.setGroupID(getPolicy.getAutomotivePolicy().getGroupID());
			automotivePolicy.setPolicyID(getPolicy.getAutomotivePolicy().getPolicyID());
			automotivePolicy.setInsuredName(getPolicy.getAutomotivePolicy().getInsuredName());
			automotivePolicy.setContractStartDate(getPolicy.getAutomotivePolicy().getContractStartDate());
			automotivePolicy.setContractEndDate(getPolicy.getAutomotivePolicy().getContractEndDate());
			automotivePolicy.setActiveStatus(getPolicy.getAutomotivePolicy().getActiveStatus());
			automotivePolicy.setVehicle(vehicle);
		
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return automotivePolicy;
	}
	
	public AutomotivePolicy getSpecialPolicy(Context context, String plate, String document, long clientID) throws Exception {
		
		AutomotivePolicy automotivePolicy = new AutomotivePolicy();
		
		try {
		
			br.com.MondialAssistance.Liberty.WS.GetPolicyResult getPolicy;
			
			automotive = new Automotive();
	
			getPolicy = automotive.GetSpecialPolicy(plate, 
												 	document, 
							                        Integer.parseInt(String.valueOf(clientID)));
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getSpecialPolicy()")
			      .append("|plate:" + plate)
			      .append("|document:" + document)
				  .append("|clientID:" + clientID);
			
			setAction(context, clientID, getPolicy.getActionResult(), params.toString());
			

		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return automotivePolicy;
	}
	
	public AutomotivePolicy getPolicyByPolicy(Context context, String policyID, long clientID) throws Exception {
		
		AutomotivePolicy automotivePolicy = new AutomotivePolicy();
		
		try {
		
			br.com.MondialAssistance.Liberty.WS.GetPolicyResult getPolicy;
			Vehicle vehicle;
			
			automotive = new Automotive();
	
			getPolicy = automotive.GetPolicyByPolicy(policyID, 
							                         Integer.parseInt(String.valueOf(clientID)));
			
			
			StringBuilder params = new StringBuilder();
			
			params.append(this.getClass().getName())
			      .append(".getPolicyByPolicy()")
			      .append("|policyID:" + policyID)
			      .append("|clientID:" + clientID);
			
			setAction(context, clientID, getPolicy.getActionResult(), params.toString());
			

			vehicle = new Vehicle();
			vehicle.setId(getPolicy.getAutomotivePolicy().getVehicle().getId());
			vehicle.setChassi(getPolicy.getAutomotivePolicy().getVehicle().getChassi());
			vehicle.setLicenseNumber(getPolicy.getAutomotivePolicy().getVehicle().getLicenseNumber());
			vehicle.setMake(getPolicy.getAutomotivePolicy().getVehicle().getMake());
			vehicle.setModel(getPolicy.getAutomotivePolicy().getVehicle().getModel());
			vehicle.setColor(getPolicy.getAutomotivePolicy().getVehicle().getColor());
			vehicle.setPlate(getPolicy.getAutomotivePolicy().getVehicle().getPlate());
			vehicle.setVehicleYear(getPolicy.getAutomotivePolicy().getVehicle().getVehicleYear());
			
			automotivePolicy.setGroupID(getPolicy.getAutomotivePolicy().getGroupID());
			automotivePolicy.setPolicyID(getPolicy.getAutomotivePolicy().getPolicyID());
			automotivePolicy.setInsuredName(getPolicy.getAutomotivePolicy().getInsuredName());
			automotivePolicy.setContractStartDate(getPolicy.getAutomotivePolicy().getContractStartDate());
			automotivePolicy.setContractEndDate(getPolicy.getAutomotivePolicy().getContractEndDate());
			automotivePolicy.setActiveStatus(getPolicy.getAutomotivePolicy().getActiveStatus());
			automotivePolicy.setVehicle(vehicle);
			
		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return automotivePolicy;		
	}
	
	public Integer getContractAdditionalInformation(String policy) throws Exception {
		
		Integer result = 0;;

		try {
		
			automotive = new Automotive();
			result = automotive.GetContractAdditionalInformation(policy);

		} catch (SoapFaultException e) {
			throw ErrorHelper.setErrorMessage(e);
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		}
		
		return result;	
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
