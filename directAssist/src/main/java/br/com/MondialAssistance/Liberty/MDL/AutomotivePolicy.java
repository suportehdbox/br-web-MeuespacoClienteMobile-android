package br.com.MondialAssistance.Liberty.MDL;

public class AutomotivePolicy extends Policy {

	private Integer GroupID;
	private Vehicle Vehicle;
	
	public Integer getGroupID(){
		return GroupID;
	}
	public void setGroupID(Integer value){
		GroupID = value;
	}

	public Vehicle getVehicle(){
		return Vehicle;
	}
	public void setVehicle(Vehicle value){
		Vehicle = value;
	}
}
