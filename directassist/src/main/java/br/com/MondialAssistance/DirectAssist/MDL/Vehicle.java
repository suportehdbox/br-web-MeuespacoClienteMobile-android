package br.com.MondialAssistance.DirectAssist.MDL;

public class Vehicle {

	private Integer Id;
	private String Chassi;
	private String LicenseNumber;
	private String Make;
	private String Model;
	private String Color;
	private String Plate;
	private String VehicleYear;
	
	public Integer getId(){
		return Id;
	}
	public void setId(Integer value){
		Id = value;
	}
	
	public String getChassi(){
		return Chassi;
	}
	public void setChassi(String value){
		Chassi = value;
	}
	
	public String getLicenseNumber(){
		return LicenseNumber;
	}
	public void setLicenseNumber(String value){
		LicenseNumber = value;
	}
	
	public String getMake(){
		return Make;
	}
	public void setMake(String value){
		Make = value;
	}
	
	public String getModel(){
		return Model;
	}
	public void setModel(String value){
		Model = value;
	}
	
	public String getColor(){
		return Color;
	}
	public void setColor(String value){
		Color = value;
	}
	
	public String getPlate(){
		return Plate;
	}
	public void setPlate(String value){
		Plate = value;
	}
	
	public String getVehicleYear(){
		return VehicleYear;
	}
	public void setVehicleYear(String value){
		VehicleYear = value;
	}
}
