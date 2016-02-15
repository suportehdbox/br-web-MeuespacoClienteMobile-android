package br.com.MondialAssistance.Liberty.MDL;

public class Policy {

	private String PolicyID;
	private String InsuredName;
	private String ContractStartDate;
	private String ContractEndDate;
	private Integer ActiveStatus;
	
	public String getPolicyID(){
		return PolicyID;
	}
	public void setPolicyID(String value){
		PolicyID = value;
	}
	
	public String getInsuredName(){
		return InsuredName;
	}
	public void setInsuredName(String value){
		InsuredName = value;
	}
	
	public String getContractStartDate(){
		return ContractStartDate;
	}
	public void setContractStartDate(String value){
		ContractStartDate = value;
	}
	
	public String getContractEndDate(){
		return ContractEndDate;
	}
	public void setContractEndDate(String value){
		ContractEndDate = value;
	}
	
	public Integer getActiveStatus(){
		return ActiveStatus;
	}
	public void setActiveStatus(Integer value){
		ActiveStatus = value;
	}
	
}
