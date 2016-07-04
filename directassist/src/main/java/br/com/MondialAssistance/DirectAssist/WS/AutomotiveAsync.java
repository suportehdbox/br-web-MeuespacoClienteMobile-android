package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AutomotiveAsync extends SoapWebService {
	
	
	
	public AutomotiveAsync(){
		this.setUrl("/mobile/v5/Automotive.asmx");
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
	
	
	public class ListAccreditedEstablishmentResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((ListAccreditedGarages)__result);
		}
		
		protected void onResult(ListAccreditedGarages result){}
		
	}
	
	
	class ListAccreditedEstablishmentRequest extends ServiceRequest
	{
		Double latitude;
		Double longitude;
		Long clientID;
		String token;
		
		ListAccreditedEstablishmentRequest(Double latitude,Double longitude,Long clientID,String token, ListAccreditedEstablishmentResultHandler handler)
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
			req = buildSoapRequest("ListAccreditedEstablishment","http://tempuri.org/","http://tempuri.org/ListAccreditedEstablishment",null);
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
	
	public void ListAccreditedEstablishment(Double latitude,Double longitude,Long clientID,String token, ListAccreditedEstablishmentResultHandler handler)
	{
		ListAccreditedEstablishmentRequest r = new ListAccreditedEstablishmentRequest(latitude,longitude,clientID,token,handler);
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
		String plate;
		String document;
		Integer clientID;
		String token;
		
		GetPolicyRequest(String plate,String document,Integer clientID,String token, GetPolicyResultHandler handler)
		{
			super(handler);
			this.plate = plate;
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
			addParameter(request, "plate",plate);
			addParameter(request, "document",document);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = GetPolicyResult.loadFrom(response);
		}
		
	}
	
	public void GetPolicy(String plate,String document,Integer clientID,String token, GetPolicyResultHandler handler)
	{
		GetPolicyRequest r = new GetPolicyRequest(plate,document,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class GetSpecialPolicyResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((GetPolicyResult)__result);
		}
		
		protected void onResult(GetPolicyResult result){}
		
	}
	
	
	class GetSpecialPolicyRequest extends ServiceRequest
	{
		String plate;
		String document;
		Integer clientID;
		String token;
		
		GetSpecialPolicyRequest(String plate,String document,Integer clientID,String token, GetSpecialPolicyResultHandler handler)
		{
			super(handler);
			this.plate = plate;
			this.document = document;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("GetSpecialPolicy","http://tempuri.org/","http://tempuri.org/GetSpecialPolicy",null);
			Element request = req.method;
			addParameter(request, "plate",plate);
			addParameter(request, "document",document);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = GetPolicyResult.loadFrom(response);
		}
		
	}
	
	public void GetSpecialPolicy(String plate,String document,Integer clientID,String token, GetSpecialPolicyResultHandler handler)
	{
		GetSpecialPolicyRequest r = new GetSpecialPolicyRequest(plate,document,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class GetPolicyByPolicyResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((GetPolicyResult)__result);
		}
		
		protected void onResult(GetPolicyResult result){}
		
	}
	
	
	class GetPolicyByPolicyRequest extends ServiceRequest
	{
		String policyID;
		Integer clientID;
		String token;
		
		GetPolicyByPolicyRequest(String policyID,Integer clientID,String token, GetPolicyByPolicyResultHandler handler)
		{
			super(handler);
			this.policyID = policyID;
			this.clientID = clientID;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("GetPolicyByPolicy","http://tempuri.org/","http://tempuri.org/GetPolicyByPolicy",null);
			Element request = req.method;
			addParameter(request, "policyID",policyID);
			addParameter(request, "clientID",clientID);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = GetPolicyResult.loadFrom(response);
		}
		
	}
	
	public void GetPolicyByPolicy(String policyID,Integer clientID,String token, GetPolicyByPolicyResultHandler handler)
	{
		GetPolicyByPolicyRequest r = new GetPolicyByPolicyRequest(policyID,clientID,token,handler);
		r.executeAsync();
	}
	
	
	public class GetContractAdditionalInformationResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((Integer)__result);
		}
		
		protected void onResult(Integer result){}
		
	}
	
	
	class GetContractAdditionalInformationRequest extends ServiceRequest
	{
		String policy;
		
		GetContractAdditionalInformationRequest(String policy, GetContractAdditionalInformationResultHandler handler)
		{
			super(handler);
			this.policy = policy;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("GetContractAdditionalInformation","http://tempuri.org/","http://tempuri.org/GetContractAdditionalInformation",null);
			Element request = req.method;
			addParameter(request, "policy",policy);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = WSHelper.getInteger(response,null,false);
		}
		
	}
	
	public void GetContractAdditionalInformation(String policy, GetContractAdditionalInformationResultHandler handler)
	{
		GetContractAdditionalInformationRequest r = new GetContractAdditionalInformationRequest(policy,handler);
		r.executeAsync();
	}
	
	
}
