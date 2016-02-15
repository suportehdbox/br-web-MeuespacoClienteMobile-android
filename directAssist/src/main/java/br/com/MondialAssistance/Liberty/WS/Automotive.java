package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Automotive extends SoapWebService{
	
	
	public Automotive(){
		this.setUrl(BaseWS.getAutomotiveURL());
	}
	
	
	public ListPoliciesResult ListPolicies(String deviceUID, Integer manufacturerID, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("ListPolicies","http://tempuri.org/","http://tempuri.org/ListPolicies",null);
		Element request = req.method;
		addParameter(request, "deviceUID",deviceUID);
		addParameter(request, "manufacturerID",manufacturerID);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ListPoliciesResult.loadFrom(response);
	}
	
	public CreateCaseResult CreateCase(String contractNumber, Integer phoneAreaCode, Integer phoneNumber, String fileCause, String serviceCode, Integer problemCode, String fileCity, String fileState, String address, String addressNumber, Double latitude, Double longitude) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("CreateCase","http://tempuri.org/","http://tempuri.org/CreateCase",null);
		Element request = req.method;
		addParameter(request, "contractNumber",contractNumber);
		addParameter(request, "phoneAreaCode",phoneAreaCode);
		addParameter(request, "phoneNumber",phoneNumber);
		addParameter(request, "fileCause",fileCause);
		addParameter(request, "serviceCode",serviceCode);
		addParameter(request, "problemCode",problemCode);
		addParameter(request, "fileCity",fileCity);
		addParameter(request, "fileState",fileState);
		addParameter(request, "address",address);
		addParameter(request, "addressNumber",addressNumber);
		addParameter(request, "latitude",latitude);
		addParameter(request, "longitude",longitude);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return CreateCaseResult.loadFrom(response);
	}
	
	public ListAutomotiveCasesResult ListCases(String deviceUID, Integer manufacturerID, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("ListCases","http://tempuri.org/","http://tempuri.org/ListCases",null);
		Element request = req.method;
		addParameter(request, "deviceUID",deviceUID);
		addParameter(request, "manufacturerID",manufacturerID);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ListAutomotiveCasesResult.loadFrom(response);
	}
	
	public ListAccreditedGarages ListAccreditedEstablishment(Double latitude, Double longitude, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("ListAccreditedEstablishment","http://tempuri.org/","http://tempuri.org/ListAccreditedEstablishment",null);
		Element request = req.method;
		addParameter(request, "latitude",latitude);
		addParameter(request, "longitude",longitude);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ListAccreditedGarages.loadFrom(response);
	}
	
	public GetPolicyResult GetPolicy(String plate, String document, Integer clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetPolicy","http://tempuri.org/","http://tempuri.org/GetPolicy",null);
		Element request = req.method;
		addParameter(request, "plate",plate);
		addParameter(request, "document",document);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return GetPolicyResult.loadFrom(response);
	}
	
	public GetPolicyResult GetSpecialPolicy(String plate, String document, Integer clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetSpecialPolicy","http://tempuri.org/","http://tempuri.org/GetSpecialPolicy",null);
		Element request = req.method;
		addParameter(request, "plate",plate);
		addParameter(request, "document",document);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return GetPolicyResult.loadFrom(response);
	}
	
	public GetPolicyResult GetPolicyByPolicy(String policyID, Integer clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetPolicyByPolicy","http://tempuri.org/","http://tempuri.org/GetPolicyByPolicy",null);
		Element request = req.method;
		addParameter(request, "policyID",policyID);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return GetPolicyResult.loadFrom(response);
	}
	
	public Integer GetContractAdditionalInformation(String policy) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetContractAdditionalInformation","http://tempuri.org/","http://tempuri.org/GetContractAdditionalInformation",null);
		Element request = req.method;
		addParameter(request, "policy",policy);
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return WSHelper.getInteger(response,null,false);
	}
	
}
