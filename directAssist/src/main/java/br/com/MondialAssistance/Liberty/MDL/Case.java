package br.com.MondialAssistance.Liberty.MDL;

public class Case {

	private Integer CaseNumber;
	private String CreationDate;
	
	public Integer getCaseNumber(){
		return CaseNumber;
	}
	public void setCaseNumber(Integer value){
		CaseNumber = value;
	}
	
	public String getCreationDate(){
		return CreationDate;
	}
	public void setCreationDate(String value){
		CreationDate = value;
	}
}
