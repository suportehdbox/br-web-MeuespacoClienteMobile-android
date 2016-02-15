package br.com.MondialAssistance.Liberty.MDL;

public class Provider {

	private String ProviderCode;
	private String LicenseNumber;
	private Location Location;
	
	public String getProviderCode(){
		return ProviderCode;
	}
	public void setProviderCode(String value){
		ProviderCode = value;
	}
	
	public String getLicenseNumber(){
		return LicenseNumber;
	}
	public void setLicenseNumber(String value){
		LicenseNumber = value;
	}

	public Location getLocation(){
		return Location;
	}
	public void setLocation(Location value){
		Location = value;
	}
	
}
