package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ServiceDispatch extends WSObject implements android.os.Parcelable
{
	
	private String _Dispatched;
	public String getDispatched(){
		return _Dispatched;
	}
	public void setDispatched(String value){
		_Dispatched = value;
	}
	private Integer _Scheduled;
	public Integer getScheduled(){
		return _Scheduled;
	}
	public void setScheduled(Integer value){
		_Scheduled = value;
	}
	private String _ScheduleStartDate;
	public String getScheduleStartDate(){
		return _ScheduleStartDate;
	}
	public void setScheduleStartDate(String value){
		_ScheduleStartDate = value;
	}
	private String _ScheduleEndDate;
	public String getScheduleEndDate(){
		return _ScheduleEndDate;
	}
	public void setScheduleEndDate(String value){
		_ScheduleEndDate = value;
	}
	private Integer _DispatchStatus;
	public Integer getDispatchStatus(){
		return _DispatchStatus;
	}
	public void setDispatchStatus(Integer value){
		_DispatchStatus = value;
	}
	private Integer _DispatchChannel;
	public Integer getDispatchChannel(){
		return _DispatchChannel;
	}
	public void setDispatchChannel(Integer value){
		_DispatchChannel = value;
	}
	private String _DispatchDate;
	public String getDispatchDate(){
		return _DispatchDate;
	}
	public void setDispatchDate(String value){
		_DispatchDate = value;
	}
	private java.util.Date _DispatchParseDate;
	public java.util.Date getDispatchParseDate(){
		return _DispatchParseDate;
	}
	public void setDispatchParseDate(java.util.Date value){
		_DispatchParseDate = value;
	}
	private Integer _ArrivalTime;
	public Integer getArrivalTime(){
		return _ArrivalTime;
	}
	public void setArrivalTime(Integer value){
		_ArrivalTime = value;
	}
	private String _Clientlatitude;
	public String getClientlatitude(){
		return _Clientlatitude;
	}
	public void setClientlatitude(String value){
		_Clientlatitude = value;
	}
	private String _ClientLongitude;
	public String getClientLongitude(){
		return _ClientLongitude;
	}
	public void setClientLongitude(String value){
		_ClientLongitude = value;
	}
	
	public static ServiceDispatch loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ServiceDispatch result = new ServiceDispatch();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setDispatched(WSHelper.getString(root,"Dispatched",false));
		this.setScheduled(WSHelper.getInteger(root,"Scheduled",false));
		this.setScheduleStartDate(WSHelper.getString(root,"ScheduleStartDate",false));
		this.setScheduleEndDate(WSHelper.getString(root,"ScheduleEndDate",false));
		this.setDispatchStatus(WSHelper.getInteger(root,"DispatchStatus",false));
		this.setDispatchChannel(WSHelper.getInteger(root,"DispatchChannel",false));
		this.setDispatchDate(WSHelper.getString(root,"DispatchDate",false));
		this.setDispatchParseDate(WSHelper.getDate(root,"DispatchParseDate",false));
		this.setArrivalTime(WSHelper.getInteger(root,"ArrivalTime",false));
		this.setClientlatitude(WSHelper.getString(root,"Clientlatitude",false));
		this.setClientLongitude(WSHelper.getString(root,"ClientLongitude",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ServiceDispatch");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_Dispatched!=null)
			WSHelper.addChild(e,"Dispatched",String.valueOf(_Dispatched),false);
		WSHelper.addChild(e,"Scheduled",String.valueOf(_Scheduled),false);
		if(_ScheduleStartDate!=null)
			WSHelper.addChild(e,"ScheduleStartDate",String.valueOf(_ScheduleStartDate),false);
		if(_ScheduleEndDate!=null)
			WSHelper.addChild(e,"ScheduleEndDate",String.valueOf(_ScheduleEndDate),false);
		WSHelper.addChild(e,"DispatchStatus",String.valueOf(_DispatchStatus),false);
		WSHelper.addChild(e,"DispatchChannel",String.valueOf(_DispatchChannel),false);
		if(_DispatchDate!=null)
			WSHelper.addChild(e,"DispatchDate",String.valueOf(_DispatchDate),false);
		WSHelper.addChild(e,"DispatchParseDate",WSHelper.stringValueOf(_DispatchParseDate),false);
		WSHelper.addChild(e,"ArrivalTime",String.valueOf(_ArrivalTime),false);
		if(_Clientlatitude!=null)
			WSHelper.addChild(e,"Clientlatitude",String.valueOf(_Clientlatitude),false);
		if(_ClientLongitude!=null)
			WSHelper.addChild(e,"ClientLongitude",String.valueOf(_ClientLongitude),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_Dispatched);
		out.writeValue(_Scheduled);
		out.writeValue(_ScheduleStartDate);
		out.writeValue(_ScheduleEndDate);
		out.writeValue(_DispatchStatus);
		out.writeValue(_DispatchChannel);
		out.writeValue(_DispatchDate);
		out.writeValue(_DispatchParseDate);
		out.writeValue(_ArrivalTime);
		out.writeValue(_Clientlatitude);
		out.writeValue(_ClientLongitude);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_Dispatched=(String)in.readValue(null);
		_Scheduled=(Integer)in.readValue(null);
		_ScheduleStartDate=(String)in.readValue(null);
		_ScheduleEndDate=(String)in.readValue(null);
		_DispatchStatus=(Integer)in.readValue(null);
		_DispatchChannel=(Integer)in.readValue(null);
		_DispatchDate=(String)in.readValue(null);
		_DispatchParseDate=(java.util.Date)in.readValue(null);
		_ArrivalTime=(Integer)in.readValue(null);
		_Clientlatitude=(String)in.readValue(null);
		_ClientLongitude=(String)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ServiceDispatch> CREATOR = new android.os.Parcelable.Creator<ServiceDispatch>()
	{
		public ServiceDispatch createFromParcel(android.os.Parcel in)
		{
			ServiceDispatch tmp = new ServiceDispatch();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ServiceDispatch[] newArray(int size)
		{
			return new ServiceDispatch[size];
		}
	}
	;
	
}
