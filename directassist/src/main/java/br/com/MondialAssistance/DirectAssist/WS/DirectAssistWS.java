package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class DirectAssistWS extends SoapWebService{
	
	
	public DirectAssistWS(){
		this.setUrl(BaseWS.getDirectAssistURL());
	}
	
	
	public ActionResult SavePolicy(String deviceUID, Integer manufacturerID, String policyID, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("SavePolicy","http://tempuri.org/","http://tempuri.org/SavePolicy",null);
		Element request = req.method;
		addParameter(request, "deviceUID",deviceUID);
		addParameter(request, "manufacturerID",manufacturerID);
		addParameter(request, "policyID",policyID);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ActionResult.loadFrom(response);
	}
	
	public ActionResult DeletePolicy(String deviceUID, Integer manufacturerID, String policyID, Long clientID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("DeletePolicy","http://tempuri.org/","http://tempuri.org/DeletePolicy",null);
		Element request = req.method;
		addParameter(request, "deviceUID",deviceUID);
		addParameter(request, "manufacturerID",manufacturerID);
		addParameter(request, "policyID",policyID);
		addParameter(request, "clientID",clientID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ActionResult.loadFrom(response);
	}
	
	public ActionResult SaveAccessLog(Integer manufacturerID, String deviceUID, Integer applicationID, String applicationVersion, Integer actionCode, String actionParameter, String actionResult, Long clientID, Double latitude, Double longitude) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("SaveAccessLog","http://tempuri.org/","http://tempuri.org/SaveAccessLog",null);
		Element request = req.method;
		addParameter(request, "manufacturerID",manufacturerID);
		addParameter(request, "deviceUID",deviceUID);
		addParameter(request, "applicationID",applicationID);
		addParameter(request, "applicationVersion",applicationVersion);
		addParameter(request, "actionCode",actionCode);
		addParameter(request, "actionParameter",actionParameter);
		addParameter(request, "actionResult",actionResult);
		addParameter(request, "clientID",clientID);
		addParameter(request, "latitude",latitude);
		addParameter(request, "longitude",longitude);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ActionResult.loadFrom(response);
	}
	
	public CoverageResult CheckCoverages(Integer fileNumber, String contractNumber, Integer phoneAreaCode, Integer phoneNumber, String fileCause, String serviceCode, Integer problemCode, String fileCity, String fileState, String address, String addressNumber, Double latitude, Double longitude, String reference) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("CheckCoverages","http://tempuri.org/","http://tempuri.org/CheckCoverages",null);
		Element request = req.method;
		addParameter(request, "fileNumber",fileNumber);
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
		addParameter(request, "reference",reference);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return CoverageResult.loadFrom(response);
	}
	
	public ActionResult CreateServiceDispatch(Integer fileNumber, String contractNumber, Integer phoneAreaCode, Integer phoneNumber, String fileCause, String serviceCode, Integer problemCode, String fileCity, String fileState, String address, String addressNumber, Double latitude, Double longitude, String reference, String district, String scheduleStartDate, String scheduleEndDate) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("CreateServiceDispatch","http://tempuri.org/","http://tempuri.org/CreateServiceDispatch",null);
		Element request = req.method;
		addParameter(request, "fileNumber",fileNumber);
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
		addParameter(request, "reference",reference);
		addParameter(request, "district",district);
		addParameter(request, "scheduleStartDate",scheduleStartDate);
		addParameter(request, "scheduleEndDate",scheduleEndDate);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return ActionResult.loadFrom(response);
	}
	
	public GetServiceDispatch GetDispatchStatus(Integer caseNumber, Integer requestID) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetDispatchStatus","http://tempuri.org/","http://tempuri.org/GetDispatchStatus",null);
		Element request = req.method;
		addParameter(request, "caseNumber",caseNumber);
		addParameter(request, "requestID",requestID);
		addParameter(request, "token",BaseWS.getToken());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return GetServiceDispatch.loadFrom(response);
	}
	
}
