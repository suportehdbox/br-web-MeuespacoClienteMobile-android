package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Automaker extends SoapWebService{
	
	
	public Automaker(){
		this.setUrl(BaseWS.getAutomakerURL());
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
	
	public GetPolicyResult GetPolicy(String chassi, String document, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetPolicy","http://tempuri.org/","http://tempuri.org/GetPolicy",null);
		Element request = req.method;
		addParameter(request, "chassi",chassi);
		addParameter(request, "document",document);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return GetPolicyResult.loadFrom(response);
	}
	
	public GetPolicyResult GetCompletePolicy(String chassi, String document, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetCompletePolicy","http://tempuri.org/","http://tempuri.org/GetCompletePolicy",null);
		Element request = req.method;
		addParameter(request, "chassi",chassi);
		addParameter(request, "document",document);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return GetPolicyResult.loadFrom(response);
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
	
	public ListAccreditedGarages ListNearestEstablishment(Double latitude, Double longitude, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("ListNearestEstablishment","http://tempuri.org/","http://tempuri.org/ListNearestEstablishment",null);
		Element request = req.method;
		addParameter(request, "latitude",latitude);
		addParameter(request, "longitude",longitude);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ListAccreditedGarages.loadFrom(response);
	}
	
}
