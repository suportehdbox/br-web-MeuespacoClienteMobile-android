package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AutomakerAsync extends SoapWebService {
	
	
	
	public AutomakerAsync(){
		this.setUrl("/mobile/v5/Automaker.asmx");
	}
	
	
	public class ListCasesResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ListAutomotiveCasesResult)__result);
		}
		
		protected void onResult(ListAutomotiveCasesResult result){}
		
	}
	
	
	class ListCasesRequest extends ServiceRequest
	{
		String deviceUID;
		Integer manufacturerID;
		Long clientID;
		String token;
		
		ListCasesRequest(String deviceUID,Integer manufacturerID,Long clientID,String token, ListCasesResultHandler handler)
		{
			super(handler);
			this.deviceUID = deviceUID;
			this.manufacturerID = manufacturerID;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("ListCases","http://tempuri.org/","http://tempuri.org/ListCases",null);
			Element request = req.method;
			addParameter(request, "deviceUID",deviceUID);
			addParameter(request, "manufacturerID",manufacturerID);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = ListAutomotiveCasesResult.loadFrom(response);
		}
		
	}
	
	public void ListCases(String deviceUID,Integer manufacturerID,Long clientID,String token, ListCasesResultHandler handler)
	{
		ListCasesRequest r = new ListCasesRequest(deviceUID,manufacturerID,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class ListPoliciesResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ListPoliciesResult)__result);
		}
		
		protected void onResult(ListPoliciesResult result){}
		
	}
	
	
	class ListPoliciesRequest extends ServiceRequest
	{
		String deviceUID;
		Integer manufacturerID;
		Long clientID;
		String token;
		
		ListPoliciesRequest(String deviceUID,Integer manufacturerID,Long clientID,String token, ListPoliciesResultHandler handler)
		{
			super(handler);
			this.deviceUID = deviceUID;
			this.manufacturerID = manufacturerID;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("ListPolicies","http://tempuri.org/","http://tempuri.org/ListPolicies",null);
			Element request = req.method;
			addParameter(request, "deviceUID",deviceUID);
			addParameter(request, "manufacturerID",manufacturerID);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = ListPoliciesResult.loadFrom(response);
		}
		
	}
	
	public void ListPolicies(String deviceUID,Integer manufacturerID,Long clientID,String token, ListPoliciesResultHandler handler)
	{
		ListPoliciesRequest r = new ListPoliciesRequest(deviceUID,manufacturerID,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class GetPolicyResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((GetPolicyResult)__result);
		}
		
		protected void onResult(GetPolicyResult result){}
		
	}
	
	
	class GetPolicyRequest extends ServiceRequest
	{
		String chassi;
		String document;
		Long clientID;
		String token;
		
		GetPolicyRequest(String chassi,String document,Long clientID,String token, GetPolicyResultHandler handler)
		{
			super(handler);
			this.chassi = chassi;
			this.document = document;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("GetPolicy","http://tempuri.org/","http://tempuri.org/GetPolicy",null);
			Element request = req.method;
			addParameter(request, "chassi",chassi);
			addParameter(request, "document",document);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = GetPolicyResult.loadFrom(response);
		}
		
	}
	
	public void GetPolicy(String chassi,String document,Long clientID,String token, GetPolicyResultHandler handler)
	{
		GetPolicyRequest r = new GetPolicyRequest(chassi,document,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class GetCompletePolicyResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((GetPolicyResult)__result);
		}
		
		protected void onResult(GetPolicyResult result){}
		
	}
	
	
	class GetCompletePolicyRequest extends ServiceRequest
	{
		String chassi;
		String document;
		Long clientID;
		String token;
		
		GetCompletePolicyRequest(String chassi,String document,Long clientID,String token, GetCompletePolicyResultHandler handler)
		{
			super(handler);
			this.chassi = chassi;
			this.document = document;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("GetCompletePolicy","http://tempuri.org/","http://tempuri.org/GetCompletePolicy",null);
			Element request = req.method;
			addParameter(request, "chassi",chassi);
			addParameter(request, "document",document);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = GetPolicyResult.loadFrom(response);
		}
		
	}
	
	public void GetCompletePolicy(String chassi,String document,Long clientID,String token, GetCompletePolicyResultHandler handler)
	{
		GetCompletePolicyRequest r = new GetCompletePolicyRequest(chassi,document,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class CreateCaseResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((CreateCaseResult)__result);
		}
		
		protected void onResult(CreateCaseResult result){}
		
	}
	
	
	class CreateCaseRequest extends ServiceRequest
	{
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
		String token;
		
		CreateCaseRequest(String contractNumber,Integer phoneAreaCode,Integer phoneNumber,String fileCause,String serviceCode,Integer problemCode,String fileCity,String fileState,String address,String addressNumber,Double latitude,Double longitude,String token, CreateCaseResultHandler handler)
		{
			super(handler);
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
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
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
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = CreateCaseResult.loadFrom(response);
		}
		
	}
	
	public void CreateCase(String contractNumber,Integer phoneAreaCode,Integer phoneNumber,String fileCause,String serviceCode,Integer problemCode,String fileCity,String fileState,String address,String addressNumber,Double latitude,Double longitude,String token, CreateCaseResultHandler handler)
	{
		CreateCaseRequest r = new CreateCaseRequest(contractNumber,phoneAreaCode,phoneNumber,fileCause,serviceCode,problemCode,fileCity,fileState,address,addressNumber,latitude,longitude,token,handler);
		r.executeAsync();
	}
	
	
	public class ListNearestEstablishmentResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ListAccreditedGarages)__result);
		}
		
		protected void onResult(ListAccreditedGarages result){}
		
	}
	
	
	class ListNearestEstablishmentRequest extends ServiceRequest
	{
		Double latitude;
		Double longitude;
		Long clientID;
		String token;
		
		ListNearestEstablishmentRequest(Double latitude,Double longitude,Long clientID,String token, ListNearestEstablishmentResultHandler handler)
		{
			super(handler);
			this.latitude = latitude;
			this.longitude = longitude;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("ListNearestEstablishment","http://tempuri.org/","http://tempuri.org/ListNearestEstablishment",null);
			Element request = req.method;
			addParameter(request, "latitude",latitude);
			addParameter(request, "longitude",longitude);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = ListAccreditedGarages.loadFrom(response);
		}
		
	}
	
	public void ListNearestEstablishment(Double latitude,Double longitude,Long clientID,String token, ListNearestEstablishmentResultHandler handler)
	{
		ListNearestEstablishmentRequest r = new ListNearestEstablishmentRequest(latitude,longitude,clientID,token,handler);
		r.executeAsync();
	}
	
	
}
