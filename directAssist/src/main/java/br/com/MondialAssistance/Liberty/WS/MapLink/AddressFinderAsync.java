package br.com.MondialAssistance.Liberty.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AddressFinderAsync extends SoapWebService {
	
	
	
	public AddressFinderAsync(){
		this.setUrl("/webservices/v3/addressfinder/addressfinder.asmx");
	}
	
	
	public class findPOIResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((POIInfo)__result);
		}
		
		protected void onResult(POIInfo result){}
		
	}
	
	
	class findPOIRequest extends ServiceRequest
	{
		String name;
		City city;
		ResultRange resultRange;
		String token;
		
		findPOIRequest(String name,City city,ResultRange resultRange,String token, findPOIResultHandler handler)
		{
			super(handler);
			this.name = name;
			this.city = city;
			this.resultRange = resultRange;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("findPOI","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/findPOI",null);
			Element request = req.method;
			addParameter(request, "name",name);
			WSHelper.addChildNode(request, "city",null,city);
			WSHelper.addChildNode(request, "resultRange",null,resultRange);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = POIInfo.loadFrom(response);
		}
		
	}
	
	public void findPOI(String name,City city,ResultRange resultRange,String token, findPOIResultHandler handler)
	{
		findPOIRequest r = new findPOIRequest(name,city,resultRange,token,handler);
		r.executeAsync();
	}
	
	
	public class findAddressResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((AddressInfo)__result);
		}
		
		protected void onResult(AddressInfo result){}
		
	}
	
	
	class findAddressRequest extends ServiceRequest
	{
		Address address;
		AddressOptions ao;
		String token;
		
		findAddressRequest(Address address,AddressOptions ao,String token, findAddressResultHandler handler)
		{
			super(handler);
			this.address = address;
			this.ao = ao;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("findAddress","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/findAddress",null);
			Element request = req.method;
			WSHelper.addChildNode(request, "address",null,address);
			WSHelper.addChildNode(request, "ao",null,ao);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = AddressInfo.loadFrom(response);
		}
		
	}
	
	public void findAddress(Address address,AddressOptions ao,String token, findAddressResultHandler handler)
	{
		findAddressRequest r = new findAddressRequest(address,ao,token,handler);
		r.executeAsync();
	}
	
	
	public class getAddressResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((AddressLocation)__result);
		}
		
		protected void onResult(AddressLocation result){}
		
	}
	
	
	class getAddressRequest extends ServiceRequest
	{
		Point point;
		String token;
		Double tolerance;
		
		getAddressRequest(Point point,String token,Double tolerance, getAddressResultHandler handler)
		{
			super(handler);
			this.point = point;
			this.token = token;
			this.tolerance = tolerance;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("getAddress","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/getAddress",null);
			Element request = req.method;
			WSHelper.addChildNode(request, "point",null,point);
			addParameter(request, "token",token);
			addParameter(request, "tolerance",tolerance);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = AddressLocation.loadFrom(response);
		}
		
	}
	
	public void getAddress(Point point,String token,Double tolerance, getAddressResultHandler handler)
	{
		getAddressRequest r = new getAddressRequest(point,token,tolerance,handler);
		r.executeAsync();
	}
	
	
	public class getXYResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((Point)__result);
		}
		
		protected void onResult(Point result){}
		
	}
	
	
	class getXYRequest extends ServiceRequest
	{
		Address address;
		String token;
		
		getXYRequest(Address address,String token, getXYResultHandler handler)
		{
			super(handler);
			this.address = address;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("getXY","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/getXY",null);
			Element request = req.method;
			WSHelper.addChildNode(request, "address",null,address);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = Point.loadFrom(response);
		}
		
	}
	
	public void getXY(Address address,String token, getXYResultHandler handler)
	{
		getXYRequest r = new getXYRequest(address,token,handler);
		r.executeAsync();
	}
	
	
	public class getXYRadiusWithMapResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((MapInfo)__result);
		}
		
		protected void onResult(MapInfo result){}
		
	}
	
	
	class getXYRadiusWithMapRequest extends ServiceRequest
	{
		Address address;
		MapOptions mo;
		Integer radius;
		String token;
		
		getXYRadiusWithMapRequest(Address address,MapOptions mo,Integer radius,String token, getXYRadiusWithMapResultHandler handler)
		{
			super(handler);
			this.address = address;
			this.mo = mo;
			this.radius = radius;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("getXYRadiusWithMap","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/getXYRadiusWithMap",null);
			Element request = req.method;
			WSHelper.addChildNode(request, "address",null,address);
			WSHelper.addChildNode(request, "mo",null,mo);
			addParameter(request, "radius",radius);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = MapInfo.loadFrom(response);
		}
		
	}
	
	public void getXYRadiusWithMap(Address address,MapOptions mo,Integer radius,String token, getXYRadiusWithMapResultHandler handler)
	{
		getXYRadiusWithMapRequest r = new getXYRadiusWithMapRequest(address,mo,radius,token,handler);
		r.executeAsync();
	}
	
	
	public class findCityResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((CityLocationInfo)__result);
		}
		
		protected void onResult(CityLocationInfo result){}
		
	}
	
	
	class findCityRequest extends ServiceRequest
	{
		City cidade;
		AddressOptions ao;
		String token;
		
		findCityRequest(City cidade,AddressOptions ao,String token, findCityResultHandler handler)
		{
			super(handler);
			this.cidade = cidade;
			this.ao = ao;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("findCity","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/findCity",null);
			Element request = req.method;
			WSHelper.addChildNode(request, "cidade",null,cidade);
			WSHelper.addChildNode(request, "ao",null,ao);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = CityLocationInfo.loadFrom(response);
		}
		
	}
	
	public void findCity(City cidade,AddressOptions ao,String token, findCityResultHandler handler)
	{
		findCityRequest r = new findCityRequest(cidade,ao,token,handler);
		r.executeAsync();
	}
	
	
	public class GetCrossStreetXYResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((Point)__result);
		}
		
		protected void onResult(Point result){}
		
	}
	
	
	class GetCrossStreetXYRequest extends ServiceRequest
	{
		City cidade;
		String firstStreet;
		String secondStreet;
		String token;
		
		GetCrossStreetXYRequest(City cidade,String firstStreet,String secondStreet,String token, GetCrossStreetXYResultHandler handler)
		{
			super(handler);
			this.cidade = cidade;
			this.firstStreet = firstStreet;
			this.secondStreet = secondStreet;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			SoapRequest req;
			req = buildSoapRequest("GetCrossStreetXY","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/GetCrossStreetXY",null);
			Element request = req.method;
			WSHelper.addChildNode(request, "cidade",null,cidade);
			addParameter(request, "firstStreet",firstStreet);
			addParameter(request, "secondStreet",secondStreet);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			__result = Point.loadFrom(response);
		}
		
	}
	
	public void GetCrossStreetXY(City cidade,String firstStreet,String secondStreet,String token, GetCrossStreetXYResultHandler handler)
	{
		GetCrossStreetXYRequest r = new GetCrossStreetXYRequest(cidade,firstStreet,secondStreet,token,handler);
		r.executeAsync();
	}
	
	
	public class GetCrossStreetAddressResultHandler extends ResultHandler
	{
		
		protected final void onServiceResult()
		{
			onResult((java.util.Vector<AddressLocation>)__result);
		}
		
		protected void onResult(java.util.Vector<AddressLocation> result){}
		
	}
	
	
	class GetCrossStreetAddressRequest extends ServiceRequest
	{
		Point point;
		String token;
		
		GetCrossStreetAddressRequest(Point point,String token, GetCrossStreetAddressResultHandler handler)
		{
			super(handler);
			this.point = point;
			this.token = token;
		}
		
		@Override
		public void executeRequest() throws Exception
		{
			NodeList list;
			int i;
			SoapRequest req;
			req = buildSoapRequest("GetCrossStreetAddress","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/GetCrossStreetAddress",null);
			Element request = req.method;
			WSHelper.addChildNode(request, "point",null,point);
			addParameter(request, "token",token);
			SoapResponse sr = getSoapResponse(req);
			Element response = (Element)sr.body.getFirstChild().getFirstChild();
			java.util.Vector<AddressLocation> retVal = new java.util.Vector<AddressLocation>();
			list = response.getChildNodes();
			for(i=0;i<list.getLength();i++)
			{
				Element e = (Element)list.item(i);
				retVal.addElement(AddressLocation.loadFrom(e));
			}
			__result = retVal;
		}
		
	}
	
	public void GetCrossStreetAddress(Point point,String token, GetCrossStreetAddressResultHandler handler)
	{
		GetCrossStreetAddressRequest r = new GetCrossStreetAddressRequest(point,token,handler);
		r.executeAsync();
	}
	
	
}
