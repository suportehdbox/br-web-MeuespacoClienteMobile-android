package br.com.MondialAssistance.Liberty.MDL;

public class Address {
	
	private String StreetName;
	private String HouseNumber;
	private String District;
	private String Complement;
	private String City;
	private String State;
	private Double Latitude;
	private Double Longitude;
	private String Zip;
	private String Reference;

	public String getStreetName(){
		return StreetName;
	}
	public void setStreetName(String value){
		StreetName = value;
	}
	
	public String getHouseNumber(){
		return HouseNumber;
	}
	public void setHouseNumber(String value){
		HouseNumber = value;
	}
	
	public String getDistrict(){
		return District;
	}
	public void setDistrict(String value){
		District = value;
	}
	
	public String getComplement(){
		return Complement;
	}
	public void setComplement(String value){
		Complement = value;
	}

	public String getCity(){
		return City;
	}
	public void setCity(String value){
		City = value;
	}

	public String getState(){
		return State;
	}
	public void setState(String value){
		State = value;
	}

	public Double getLatitude(){
		return Latitude;
	}
	public void setLatitude(Double value){
		Latitude = value;
	}

	public Double getLongitude(){
		return Longitude;
	}
	public void setLongitude(Double value){
		Longitude = value;
	}
	
	public String getZip() {
		return Zip;
	}
	public void setZip(String zip) {
		Zip = zip;
	}
	
	public String getReference() {
		return Reference;
	}
	public void setReference(String reference) {
		Reference = reference;
	}
	
	@Override
	public String toString() {
		return getStreetName() + ((getHouseNumber() == null) ? "" : "," + getHouseNumber()) + ((getComplement() == null) ? "" : ", " + getComplement()) + System.getProperty("line.separator") + 
	          (getDistrict() == null ? "" : getDistrict() + System.getProperty("line.separator")) + 
	           getCity() + "/" + getState();
	}
}