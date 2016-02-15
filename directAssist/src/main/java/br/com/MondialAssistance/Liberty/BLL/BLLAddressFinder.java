/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.Liberty.BLL;

import java.util.Vector;

import br.com.MondialAssistance.Liberty.MDL.Address;
import br.com.MondialAssistance.Liberty.MDL.AddressLocation;
import br.com.MondialAssistance.Liberty.MDL.Point;
import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.WS.MapLink.AddressFinder;

public class BLLAddressFinder {
	
	private AddressFinder addressFinder;

	public AddressLocation getAddress(Double latitude, Double longitude) throws Exception 
	{
		AddressLocation addressLocation = new AddressLocation();
		
		br.com.MondialAssistance.Liberty.WS.MapLink.Point pointWS = new br.com.MondialAssistance.Liberty.WS.MapLink.Point();
		br.com.MondialAssistance.Liberty.WS.MapLink.AddressLocation addressLocationWS;
		Point point;
		
		pointWS.setx(longitude);
		pointWS.sety(latitude);
		
		addressFinder = new AddressFinder();
		addressLocationWS = addressFinder.getAddress(pointWS,
				                                     1.0);
		
		point = new Point();
		point.setx(addressLocationWS.getpoint().getx());
		point.sety(addressLocationWS.getpoint().gety());
		
		addressLocation.setStreetName(Utility.getUnAccent(addressLocationWS.getaddress().getstreet()));
		addressLocation.setHouseNumber(Utility.getUnAccent(addressLocationWS.getaddress().gethouseNumber()));
		addressLocation.setZip(addressLocationWS.getaddress().getzip());
		addressLocation.setDistrict(Utility.getUnAccent(addressLocationWS.getaddress().getdistrict()));
		addressLocation.setCity(Utility.getUnAccent(addressLocationWS.getaddress().getcity().getname()));
		addressLocation.setState(Utility.getUnAccent(addressLocationWS.getaddress().getcity().getstate()));
		addressLocation.setkey(addressLocationWS.getkey());
		addressLocation.setzipL(addressLocationWS.getzipL());
		addressLocation.setzipR(addressLocationWS.getzipR());
		addressLocation.setcarAccess(addressLocationWS.getcarAccess());
		addressLocation.setdataSource(addressLocationWS.getdataSource());
		addressLocation.setLatitude(addressLocationWS.getpoint().gety());
		addressLocation.setLongitude(addressLocationWS.getpoint().getx());
		addressLocation.setpoint(point);
			
		return addressLocation;
	}
	
	public Vector<AddressLocation> FindAddressByStreet(String streetName, String cityName) throws Exception 
	{
		Vector<AddressLocation> addressLocations = new Vector<AddressLocation>();
		AddressLocation addressLocation;
		Point point;
		
		br.com.MondialAssistance.Liberty.WS.MapLink.AddressOptions addressOptions;
		br.com.MondialAssistance.Liberty.WS.MapLink.AddressInfo addressInfo;
		br.com.MondialAssistance.Liberty.WS.MapLink.ResultRange resultRange;
		br.com.MondialAssistance.Liberty.WS.MapLink.Address addressWS;
		br.com.MondialAssistance.Liberty.WS.MapLink.City cityWS;
		
		resultRange = new br.com.MondialAssistance.Liberty.WS.MapLink.ResultRange();
		resultRange.setrecordsPerPage(100);
		resultRange.setpageIndex(1);
		
		addressOptions = new br.com.MondialAssistance.Liberty.WS.MapLink.AddressOptions();
		addressOptions.setresultRange(resultRange);
		addressOptions.setusePhonetic(false);
		addressOptions.setsearchType(2);
		addressOptions.setmatchType(1);
		
		cityWS = new br.com.MondialAssistance.Liberty.WS.MapLink.City();
		cityWS.setname(cityName);
		
		addressWS = new br.com.MondialAssistance.Liberty.WS.MapLink.Address();
		addressWS.setstreet(streetName);
		addressWS.setcity(cityWS);

		addressFinder = new AddressFinder();
		addressInfo = addressFinder.findAddress(addressWS, 
				                                addressOptions); 
		
		for (br.com.MondialAssistance.Liberty.WS.MapLink.AddressLocation item : addressInfo.getaddressLocation()) {
			
			point = new Point();
			point.setx(item.getpoint().getx());
			point.sety(item.getpoint().gety());
			
			addressLocation = new AddressLocation();
			addressLocation.setkey(item.getkey());
			addressLocation.setzipL(item.getzipL());
			addressLocation.setzipR(item.getzipR());
			addressLocation.setcarAccess(item.getcarAccess());
			addressLocation.setdataSource(item.getdataSource());
			addressLocation.setpoint(point);
			addressLocation.setStreetName(Utility.getUnAccent(item.getaddress().getstreet()));
			addressLocation.setHouseNumber(Utility.getUnAccent(item.getaddress().gethouseNumber()));
			addressLocation.setDistrict(Utility.getUnAccent(item.getaddress().getdistrict()));
			addressLocation.setCity(Utility.getUnAccent(item.getaddress().getcity().getname()));
			addressLocation.setState(Utility.getUnAccent(item.getaddress().getcity().getstate()));
			addressLocation.setLatitude(item.getpoint().gety());
			addressLocation.setLongitude(item.getpoint().getx());
			addressLocation.setZip(item.getaddress().getzip());
			
			addressLocations.add(addressLocation);
		}
			
		return addressLocations;
	}
	
	public Vector<AddressLocation> FindAddressByZipCode(String zipCode) throws Exception 
	{
		Vector<AddressLocation> addressLocations = new Vector<AddressLocation>();
		AddressLocation addressLocation;
		Point point;
		
		br.com.MondialAssistance.Liberty.WS.MapLink.AddressOptions addressOptions;
		br.com.MondialAssistance.Liberty.WS.MapLink.AddressInfo addressInfo;
		br.com.MondialAssistance.Liberty.WS.MapLink.ResultRange resultRange;
		br.com.MondialAssistance.Liberty.WS.MapLink.Address addressWS;
		
		resultRange = new br.com.MondialAssistance.Liberty.WS.MapLink.ResultRange();
		resultRange.setrecordsPerPage(100);
		resultRange.setpageIndex(1);
		
		addressOptions = new br.com.MondialAssistance.Liberty.WS.MapLink.AddressOptions();
		addressOptions.setresultRange(resultRange);
		addressOptions.setusePhonetic(false);
		addressOptions.setsearchType(2);
		addressOptions.setmatchType(1);
		
		addressWS = new br.com.MondialAssistance.Liberty.WS.MapLink.Address();
		addressWS.setzip(zipCode);
		
		addressFinder = new AddressFinder();
		addressInfo = addressFinder.findAddress(addressWS, 
				                                addressOptions); 
		
		for (br.com.MondialAssistance.Liberty.WS.MapLink.AddressLocation item : addressInfo.getaddressLocation()) {
			
			point = new Point();
			point.setx(item.getpoint().getx());
			point.sety(item.getpoint().gety());
			
			addressLocation = new AddressLocation();
			addressLocation.setkey(item.getkey());
			addressLocation.setzipL(item.getzipL());
			addressLocation.setzipR(item.getzipR());
			addressLocation.setcarAccess(item.getcarAccess());
			addressLocation.setdataSource(item.getdataSource());
			addressLocation.setpoint(point);
			addressLocation.setStreetName(Utility.getUnAccent(item.getaddress().getstreet()));
			addressLocation.setHouseNumber(Utility.getUnAccent(item.getaddress().gethouseNumber()));
			addressLocation.setDistrict(Utility.getUnAccent(item.getaddress().getdistrict()));
			addressLocation.setCity(Utility.getUnAccent(item.getaddress().getcity().getname()));
			addressLocation.setState(Utility.getUnAccent(item.getaddress().getcity().getstate()));
			addressLocation.setLatitude(item.getpoint().gety());
			addressLocation.setLongitude(item.getpoint().getx());
			addressLocation.setZip(item.getaddress().getzip());
			
			addressLocations.add(addressLocation);
		}
			
		return addressLocations;
	}
	
	public Point getXY(Address address) throws Exception 
	{
		Point point = new Point();
		
		br.com.MondialAssistance.Liberty.WS.MapLink.Address addressWS;
		br.com.MondialAssistance.Liberty.WS.MapLink.Point pointWS;
		br.com.MondialAssistance.Liberty.WS.MapLink.City city;
		
		city = new br.com.MondialAssistance.Liberty.WS.MapLink.City();
		city.setname(address.getCity());
		city.setstate(address.getState());
		
		addressWS = new br.com.MondialAssistance.Liberty.WS.MapLink.Address();
		addressWS.setstreet(address.getStreetName());
		addressWS.sethouseNumber(address.getHouseNumber());
		addressWS.setzip(address.getZip());
		addressWS.setdistrict(address.getDistrict());
		addressWS.setcity(city);
	
		addressFinder = new AddressFinder();
		pointWS = addressFinder.getXY(addressWS);
		
		point.setx(pointWS.getx());
		point.sety(pointWS.gety());
			
		return point;
	}
	
	public java.util.Vector<AddressLocation> getCrossStreetAddress(Double latitudde, Double longitude) throws Exception 
	{
		java.util.Vector<AddressLocation> items = new Vector<AddressLocation>();
		
		br.com.MondialAssistance.Liberty.WS.MapLink.Point pointWS = new br.com.MondialAssistance.Liberty.WS.MapLink.Point();
		java.util.Vector<br.com.MondialAssistance.Liberty.WS.MapLink.AddressLocation> AddressLocationsWS;
		AddressLocation addressLocation;
		Point point;
		
		pointWS.setx(longitude);
		pointWS.sety(latitudde);
		
		addressFinder = new AddressFinder();
		AddressLocationsWS = addressFinder.GetCrossStreetAddress(pointWS);
		
		for (br.com.MondialAssistance.Liberty.WS.MapLink.AddressLocation item : AddressLocationsWS) {
			
			point = new Point();
			point.setx(item.getpoint().getx());
			point.sety(item.getpoint().gety());
			
			addressLocation = new AddressLocation();
			addressLocation.setkey(item.getkey());
			addressLocation.setzipL(item.getzipL());
			addressLocation.setzipR(item.getzipR());
			addressLocation.setcarAccess(item.getcarAccess());
			addressLocation.setdataSource(item.getdataSource());
			addressLocation.setpoint(point);
			
			items.add(addressLocation);
		}
			
		return items;
	}
}