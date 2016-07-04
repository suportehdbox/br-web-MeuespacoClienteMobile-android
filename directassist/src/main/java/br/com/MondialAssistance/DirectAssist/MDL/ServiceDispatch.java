package br.com.MondialAssistance.DirectAssist.MDL;

public class ServiceDispatch {

	private String Dispatched;
	private Integer Scheduled;
	private String ScheduleStartDate;
	private String ScheduleEndDate;
	private Integer DispatchStatus;
	private Integer DispatchChannel;
	private String DispatchDate;
	private java.util.Date DispatchParseDate;
	private Integer ArrivalTime;
	private String Clientlatitude;
	private String ClientLongitude;
	
	public String getDispatched(){
		return Dispatched;
	}
	public void setDispatched(String value){
		Dispatched = value;
	}
	
	public Integer getScheduled(){
		return Scheduled;
	}
	public void setScheduled(Integer value){
		Scheduled = value;
	}
	
	public String getScheduleStartDate(){
		return ScheduleStartDate;
	}
	public void setScheduleStartDate(String value){
		ScheduleStartDate = value;
	}
	
	public String getScheduleEndDate(){
		return ScheduleEndDate;
	}
	public void setScheduleEndDate(String value){
		ScheduleEndDate = value;
	}
	
	public Integer getDispatchStatus(){
		return DispatchStatus;
	}
	public void setDispatchStatus(Integer value){
		DispatchStatus = value;
	}
	
	public Integer getDispatchChannel(){
		return DispatchChannel;
	}
	public void setDispatchChannel(Integer value){
		DispatchChannel = value;
	}
	
	public String getDispatchDate(){
		return DispatchDate;
	}
	public void setDispatchDate(String value){
		DispatchDate = value;
	}
	
	public java.util.Date getDispatchParseDate(){
		return DispatchParseDate;
	}
	public void setDispatchParseDate(java.util.Date value){
		DispatchParseDate = value;
	}

	public Integer getArrivalTime(){
		return ArrivalTime;
	}
	public void setArrivalTime(Integer value){
		ArrivalTime = value;
	}
	
	public String getClientlatitude(){
		return Clientlatitude;
	}
	public void setClientlatitude(String value){
		Clientlatitude = value;
	}

	public String getClientLongitude(){
		return ClientLongitude;
	}
	public void setClientLongitude(String value){
		ClientLongitude = value;
	}
}