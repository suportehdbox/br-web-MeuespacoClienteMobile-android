package br.com.MondialAssistance.DirectAssist.MDL;

public class AccreditedGarage {
	
	private String GarageName;
	private Address Address;
	private Phone Phone;
	private Double Distance;
	
	public String getGarageName(){
		return GarageName;
	}
	public void setGarageName(String value){
		GarageName = value;
	}
	
	public Address getAddress(){
		return Address;
	}
	public void setAddress(Address value){
		Address = value;
	}
	
	public Phone getPhone(){
		return Phone;
	}
	public void setPhone(Phone value){
		Phone = value;
	}
	
	public Double getDistance(){
		return Distance;
	}
	public void setDistance(Double value){
		Distance = value;
	}
	
	@Override
	public String toString() {
		return getGarageName() + System.getProperty("line.separator") + 
			   getDistance() + " km";
	}
}