package br.com.libertyseguros.mobile.model;

import java.io.Serializable;

public class AddressLocation implements Serializable {
	
	private static final long serialVersionUID = 9107072458243854482L;
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String LOCATION = "activity_location";

	private String city;
	private String street;
	private String postalCode;
	private String addressFormatted;

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}
	
	public String getAddressFormatted()
	{
		return addressFormatted;
	}
	
	public void setAddressFormatted(String addressFormatted)
	{
		this.addressFormatted = addressFormatted;
	}
}