package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class CityLocation extends WSObject implements android.os.Parcelable
{
	
	private City _city;
	public City getcity(){
		return _city;
	}
	public void setcity(City value){
		_city = value;
	}
	private Point _point;
	public Point getpoint(){
		return _point;
	}
	public void setpoint(Point value){
		_point = value;
	}
	private Boolean _carAccess;
	public Boolean getcarAccess(){
		return _carAccess;
	}
	public void setcarAccess(Boolean value){
		_carAccess = value;
	}
	private String _zipRangeStart;
	public String getzipRangeStart(){
		return _zipRangeStart;
	}
	public void setzipRangeStart(String value){
		_zipRangeStart = value;
	}
	private String _zipRangeEnd;
	public String getzipRangeEnd(){
		return _zipRangeEnd;
	}
	public void setzipRangeEnd(String value){
		_zipRangeEnd = value;
	}
	private Boolean _capital;
	public Boolean getcapital(){
		return _capital;
	}
	public void setcapital(Boolean value){
		_capital = value;
	}
	
	public static CityLocation loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		CityLocation result = new CityLocation();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setcity(City.loadFrom(WSHelper.getElement(root,"city")));
		this.setpoint(Point.loadFrom(WSHelper.getElement(root,"point")));
		this.setcarAccess(WSHelper.getBoolean(root,"carAccess",false));
		this.setzipRangeStart(WSHelper.getString(root,"zipRangeStart",false));
		this.setzipRangeEnd(WSHelper.getString(root,"zipRangeEnd",false));
		this.setcapital(WSHelper.getBoolean(root,"capital",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("CityLocation");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_city!=null)
			WSHelper.addChildNode(e, "city",null,_city);
		if(_point!=null)
			WSHelper.addChildNode(e, "point",null,_point);
		WSHelper.addChild(e,"carAccess",(_carAccess ? "true" : "false"),false);
		if(_zipRangeStart!=null)
			WSHelper.addChild(e,"zipRangeStart",String.valueOf(_zipRangeStart),false);
		if(_zipRangeEnd!=null)
			WSHelper.addChild(e,"zipRangeEnd",String.valueOf(_zipRangeEnd),false);
		WSHelper.addChild(e,"capital",(_capital ? "true" : "false"),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_city);
		out.writeValue(_point);
		out.writeValue(_carAccess);
		out.writeValue(_zipRangeStart);
		out.writeValue(_zipRangeEnd);
		out.writeValue(_capital);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_city=(City)in.readValue(null);
		_point=(Point)in.readValue(null);
		_carAccess=(Boolean)in.readValue(null);
		_zipRangeStart=(String)in.readValue(null);
		_zipRangeEnd=(String)in.readValue(null);
		_capital=(Boolean)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<CityLocation> CREATOR = new android.os.Parcelable.Creator<CityLocation>()
	{
		public CityLocation createFromParcel(android.os.Parcel in)
		{
			CityLocation tmp = new CityLocation();
			tmp.readFromParcel(in);
			return tmp;
		}
		public CityLocation[] newArray(int size)
		{
			return new CityLocation[size];
		}
	}
	;
	
}
