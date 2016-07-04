package br.com.MondialAssistance.DirectAssist.MDL;

public class Action {

	private Integer _ResultCode;
	private String _ErrorMessage;
	private int _ActionCode;
	
	public Integer getResultCode(){
		return _ResultCode;
	}
	public void setResultCode(Integer value){
		_ResultCode = value;
	}
	
	public String getErrorMessage(){
		return _ErrorMessage;
	}
	public void setErrorMessage(String value){
		_ErrorMessage = value;
	}
	
	public int getActionCode() {
		return _ActionCode;
	}
	public void setActionCode(int value) {
		_ActionCode = value;
	}
}
