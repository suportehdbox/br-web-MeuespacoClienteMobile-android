package br.com.MondialAssistance.Liberty.MDL;

public class Phone {

	private String CountryCode;
	private String AreaCode;
	private String PhoneNumber;
	
	public String getCountryCode(){
		return CountryCode;
	}
	public void setCountryCode(String value){
		CountryCode = value;
	}
	
	public String getAreaCode(){
		return AreaCode;
	}
	public void setAreaCode(String value){
		AreaCode = value;
	}

	public String getPhoneNumber(){
		return PhoneNumber;
	}
	public void setPhoneNumber(String value){
		PhoneNumber = value;
	}
	
	@Override
	public String toString() {
		return ((CountryCode == null) ? "" : "+" + CountryCode + " ") +
			   ((AreaCode == null) ? "" : "(" + AreaCode + ") ") +
				 PhoneNumber;
	}
}