package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import br.com.MondialAssistance.DirectAssist.WS.BaseWS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AddressFinder extends SoapWebService{
	
	
	public AddressFinder(){
		this.setUrl(BaseWS.getMaplinkAddressFinderURL());
	}
	
	
	public POIInfo findPOI(String name, City city, ResultRange resultRange) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("findPOI","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/findPOI",null);
		Element request = req.method;
		addParameter(request, "name",name);
		WSHelper.addChildNode(request, "city",null,city);
		WSHelper.addChildNode(request, "resultRange",null,resultRange);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return POIInfo.loadFrom(response);
	}
	
	public AddressInfo findAddress(Address address, AddressOptions ao) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("findAddress","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/findAddress",null);
		Element request = req.method;
		WSHelper.addChildNode(request, "address",null,address);
		WSHelper.addChildNode(request, "ao",null,ao);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return AddressInfo.loadFrom(response);
	}
	
	public AddressLocation getAddress(Point point, Double tolerance) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("getAddress","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/getAddress",null);
		Element request = req.method;
		WSHelper.addChildNode(request, "point",null,point);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		addParameter(request, "tolerance",tolerance);
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return AddressLocation.loadFrom(response);
	}
	
	public Point getXY(Address address) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("getXY","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/getXY",null);
		Element request = req.method;
		WSHelper.addChildNode(request, "address",null,address);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return Point.loadFrom(response);
	}
	
	public MapInfo getXYRadiusWithMap(Address address, MapOptions mo, Integer radius) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("getXYRadiusWithMap","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/getXYRadiusWithMap",null);
		Element request = req.method;
		WSHelper.addChildNode(request, "address",null,address);
		WSHelper.addChildNode(request, "mo",null,mo);
		addParameter(request, "radius",radius);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return MapInfo.loadFrom(response);
	}
	
	public CityLocationInfo findCity(City cidade, AddressOptions ao) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("findCity","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/findCity",null);
		Element request = req.method;
		WSHelper.addChildNode(request, "cidade",null,cidade);
		WSHelper.addChildNode(request, "ao",null,ao);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return CityLocationInfo.loadFrom(response);
	}
	
	public Point GetCrossStreetXY(City cidade, String firstStreet, String secondStreet) throws Exception 
	{
		SoapRequest req;
		req = buildSoapRequest("GetCrossStreetXY","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/GetCrossStreetXY",null);
		Element request = req.method;
		WSHelper.addChildNode(request, "cidade",null,cidade);
		addParameter(request, "firstStreet",firstStreet);
		addParameter(request, "secondStreet",secondStreet);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		return Point.loadFrom(response);
	}
	
	public java.util.Vector<AddressLocation> GetCrossStreetAddress(Point point) throws Exception 
	{
		NodeList list;
		int i;
		SoapRequest req;
		req = buildSoapRequest("GetCrossStreetAddress","http://webservices.maplink2.com.br","http://webservices.maplink2.com.br/GetCrossStreetAddress",null);
		Element request = req.method;
		WSHelper.addChildNode(request, "point",null,point);
		addParameter(request, "token",BaseWS.getTokenMapLink());
		SoapResponse sr = getSoapResponse(req);
		Element response = (Element)sr.body.getFirstChild().getFirstChild();
		java.util.Vector<AddressLocation> retVal = new java.util.Vector<AddressLocation>();
		list = response.getChildNodes();
		for(i=0;i<list.getLength();i++)
		{
			Element e = (Element)list.item(i);
			retVal.addElement(AddressLocation.loadFrom(e));
		}
		return retVal;
	}
	
}
