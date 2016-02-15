package br.com.MondialAssistance.Liberty.MDL;


public class AddressLocation extends Address {

	private String key;
	private String zipL;
	private String zipR;
	private Boolean carAccess;
	private String dataSource;
	private Point point;
	
	public String getkey(){
		return key;
	}
	public void setkey(String value){
		key = value;
	}
	
	public String getzipL(){
		return zipL;
	}
	public void setzipL(String value){
		zipL = value;
	}
	
	public String getzipR(){
		return zipR;
	}
	public void setzipR(String value){
		zipR = value;
	}
	
	public Boolean getcarAccess(){
		return carAccess;
	}
	public void setcarAccess(Boolean value){
		carAccess = value;
	}
	
	public String getdataSource(){
		return dataSource;
	}
	public void setdataSource(String value){
		dataSource = value;
	}
	
	public Point getpoint(){
		return point;
	}
	public void setpoint(Point value){
		point = value;
	}
	
	@Override
	public String toString() {
		return getStreetName() + ((getHouseNumber() == null) ? "" : "," + getHouseNumber()) + ((getComplement() == null) ? "" : ", " + getComplement()) + System.getProperty("line.separator") + 
	          (getDistrict() == null ? "" : getDistrict() + System.getProperty("line.separator")) + 
	           getCity() + "/" + getState();
	}
}