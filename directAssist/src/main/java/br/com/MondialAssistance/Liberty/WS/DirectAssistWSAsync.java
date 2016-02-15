package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class DirectAssistWSAsync extends SoapWebService {
	
	
	
	public DirectAssistWSAsync(){
		this.setUrl("/mobile/v5/directassistws.asmx");
	}
	
	
	public class SavePolicyResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ActionResult)__result);
		}
		
		protected void onResult(ActionResult result){}
		
	}
	
	
	class SavePolicyRequest extends ServiceRequest
	{
		String deviceUID;
		Integer manufacturerID;
		String policyID;
		Long clientID;
		String token;
		
		SavePolicyRequest(String deviceUID,Integer manufacturerID,String policyID,Long clientID,String token, SavePolicyResultHandler handler)
		{
			super(handler);
			this.deviceUID = deviceUID;
			this.manufacturerID = manufacturerID;
			this.policyID = policyID;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("SavePolicy","http://tempuri.org/","http://tempuri.org/SavePolicy",null);
			Element request = req.method;
			addParameter(request, "deviceUID",deviceUID);
			addParameter(request, "manufacturerID",manufacturerID);
			addParameter(request, "policyID",policyID);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = ActionResult.loadFrom(response);
		}
		
	}
	
	public void SavePolicy(String deviceUID,Integer manufacturerID,String policyID,Long clientID,String token, SavePolicyResultHandler handler)
	{
		SavePolicyRequest r = new SavePolicyRequest(deviceUID,manufacturerID,policyID,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class DeletePolicyResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ActionResult)__result);
		}
		
		protected void onResult(ActionResult result){}
		
	}
	
	
	class DeletePolicyRequest extends ServiceRequest
	{
		String deviceUID;
		Integer manufacturerID;
		String policyID;
		Long clientID;
		String token;
		
		DeletePolicyRequest(String deviceUID,Integer manufacturerID,String policyID,Long clientID,String token, DeletePolicyResultHandler handler)
		{
			super(handler);
			this.deviceUID = deviceUID;
			this.manufacturerID = manufacturerID;
			this.policyID = policyID;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("DeletePolicy","http://tempuri.org/","http://tempuri.org/DeletePolicy",null);
			Element request = req.method;
			addParameter(request, "deviceUID",deviceUID);
			addParameter(request, "manufacturerID",manufacturerID);
			addParameter(request, "policyID",policyID);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = ActionResult.loadFrom(response);
		}
		
	}
	
	public void DeletePolicy(String deviceUID,Integer manufacturerID,String policyID,Long clientID,String token, DeletePolicyResultHandler handler)
	{
		DeletePolicyRequest r = new DeletePolicyRequest(deviceUID,manufacturerID,policyID,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class SaveAccessLogResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ActionResult)__result);
		}
		
		protected void onResult(ActionResult result){}
		
	}
	
	
	class SaveAccessLogRequest extends ServiceRequest
	{
		Integer manufacturerID;
		String deviceUID;
		Integer applicationID;
		String applicationVersion;
		Integer actionCode;
		String actionParameter;
		String actionResult;
		Long clientID;
		Double latitude;
		Double longitude;
		String token;
		
		SaveAccessLogRequest(Integer manufacturerID,String deviceUID,Integer applicationID,String applicationVersion,Integer actionCode,String actionParameter,String actionResult,Long clientID,Double latitude,Double longitude,String token, SaveAccessLogResultHandler handler)
		{
			super(handler);
			this.manufacturerID = manufacturerID;
			this.deviceUID = deviceUID;
			this.applicationID = applicationID;
			this.applicationVersion = applicationVersion;
			this.actionCode = actionCode;
			this.actionParameter = actionParameter;
			this.actionResult = actionResult;
			this.clientID = clientID;
			this.latitude = latitude;
			this.longitude = longitude;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
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
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = ActionResult.loadFrom(response);
		}
		
	}
	
	public void SaveAccessLog(Integer manufacturerID,String deviceUID,Integer applicationID,String applicationVersion,Integer actionCode,String actionParameter,String actionResult,Long clientID,Double latitude,Double longitude,String token, SaveAccessLogResultHandler handler)
	{
		SaveAccessLogRequest r = new SaveAccessLogRequest(manufacturerID,deviceUID,applicationID,applicationVersion,actionCode,actionParameter,actionResult,clientID,latitude,longitude,token,handler);
		r.executeAsync();
	}
	
	
	public class CheckCoveragesResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((CoverageResult)__result);
		}
		
		protected void onResult(CoverageResult result){}
		
	}
	
	
	class CheckCoveragesRequest extends ServiceRequest
	{
		Integer fileNumber;
		String contractNumber;
		Integer phoneAreaCode;
		Integer phoneNumber;
		String fileCause;
		String serviceCode;
		Integer problemCode;
		String fileCity;
		String fileState;
		String address;
		String addressNumber;
		Double latitude;
		Double longitude;
		String reference;
		String token;
		
		CheckCoveragesRequest(Integer fileNumber,String contractNumber,Integer phoneAreaCode,Integer phoneNumber,String fileCause,String serviceCode,Integer problemCode,String fileCity,String fileState,String address,String addressNumber,Double latitude,Double longitude,String reference,String token, CheckCoveragesResultHandler handler)
		{
			super(handler);
			this.fileNumber = fileNumber;
			this.contractNumber = contractNumber;
			this.phoneAreaCode = phoneAreaCode;
			this.phoneNumber = phoneNumber;
			this.fileCause = fileCause;
			this.serviceCode = serviceCode;
			this.problemCode = problemCode;
			this.fileCity = fileCity;
			this.fileState = fileState;
			this.address = address;
			this.addressNumber = addressNumber;
			this.latitude = latitude;
			this.longitude = longitude;
			this.reference = reference;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
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
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = CoverageResult.loadFrom(response);
		}
		
	}
	
	public void CheckCoverages(Integer fileNumber,String contractNumber,Integer phoneAreaCode,Integer phoneNumber,String fileCause,String serviceCode,Integer problemCode,String fileCity,String fileState,String address,String addressNumber,Double latitude,Double longitude,String reference,String token, CheckCoveragesResultHandler handler)
	{
		CheckCoveragesRequest r = new CheckCoveragesRequest(fileNumber,contractNumber,phoneAreaCode,phoneNumber,fileCause,serviceCode,problemCode,fileCity,fileState,address,addressNumber,latitude,longitude,reference,token,handler);
		r.executeAsync();
	}
	
	
	public class CreateServiceDispatchResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ActionResult)__result);
		}
		
		protected void onResult(ActionResult result){}
		
	}
	
	
	class CreateServiceDispatchRequest extends ServiceRequest
	{
		Integer fileNumber;
		String contractNumber;
		Integer phoneAreaCode;
		Integer phoneNumber;
		String fileCause;
		String serviceCode;
		Integer problemCode;
		String fileCity;
		String fileState;
		String address;
		String addressNumber;
		Double latitude;
		Double longitude;
		String reference;
		String district;
		String scheduleStartDate;
		String scheduleEndDate;
		String token;
		
		CreateServiceDispatchRequest(Integer fileNumber,String contractNumber,Integer phoneAreaCode,Integer phoneNumber,String fileCause,String serviceCode,Integer problemCode,String fileCity,String fileState,String address,String addressNumber,Double latitude,Double longitude,String reference,String district,String scheduleStartDate,String scheduleEndDate,String token, CreateServiceDispatchResultHandler handler)
		{
			super(handler);
			this.fileNumber = fileNumber;
			this.contractNumber = contractNumber;
			this.phoneAreaCode = phoneAreaCode;
			this.phoneNumber = phoneNumber;
			this.fileCause = fileCause;
			this.serviceCode = serviceCode;
			this.problemCode = problemCode;
			this.fileCity = fileCity;
			this.fileState = fileState;
			this.address = address;
			this.addressNumber = addressNumber;
			this.latitude = latitude;
			this.longitude = longitude;
			this.reference = reference;
			this.district = district;
			this.scheduleStartDate = scheduleStartDate;
			this.scheduleEndDate = scheduleEndDate;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
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
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = ActionResult.loadFrom(response);
		}
		
	}
	
	public void CreateServiceDispatch(Integer fileNumber,String contractNumber,Integer phoneAreaCode,Integer phoneNumber,String fileCause,String serviceCode,Integer problemCode,String fileCity,String fileState,String address,String addressNumber,Double latitude,Double longitude,String reference,String district,String scheduleStartDate,String scheduleEndDate,String token, CreateServiceDispatchResultHandler handler)
	{
		CreateServiceDispatchRequest r = new CreateServiceDispatchRequest(fileNumber,contractNumber,phoneAreaCode,phoneNumber,fileCause,serviceCode,problemCode,fileCity,fileState,address,addressNumber,latitude,longitude,reference,district,scheduleStartDate,scheduleEndDate,token,handler);
		r.executeAsync();
	}
	
	
	public class GetDispatchStatusResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((GetServiceDispatch)__result);
		}
		
		protected void onResult(GetServiceDispatch result){}
		
	}
	
	
	class GetDispatchStatusRequest extends ServiceRequest
	{
		Integer caseNumber;
		Integer requestID;
		String token;
		
		GetDispatchStatusRequest(Integer caseNumber,Integer requestID,String token, GetDispatchStatusResultHandler handler)
		{
			super(handler);
			this.caseNumber = caseNumber;
			this.requestID = requestID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("GetDispatchStatus","http://tempuri.org/","http://tempuri.org/GetDispatchStatus",null);
			Element request = req.method;
			addParameter(request, "caseNumber",caseNumber);
			addParameter(request, "requestID",requestID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = GetServiceDispatch.loadFrom(response);
		}
		
	}
	
	public void GetDispatchStatus(Integer caseNumber,Integer requestID,String token, GetDispatchStatusResultHandler handler)
	{
		GetDispatchStatusRequest r = new GetDispatchStatusRequest(caseNumber,requestID,token,handler);
		r.executeAsync();
	}
	
	
}
