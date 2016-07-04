package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class POILocation extends WSObject implements android.os.Parcelable
{
	
	private String _name;
	public String getname(){
		return _name;
	}
	public void setname(String value){
		_name = value;
	}
	private String _district;
	public String getdistrict(){
		return _district;
	}
	public void setdistrict(String value){
		_district = value;
	}
	private Boolean _carAccess;
	public Boolean getcarAccess(){
		return _carAccess;
	}
	public void setcarAccess(Boolean value){
		_carAccess = value;
	}
	private String _dataSource;
	public String getdataSource(){
		return _dataSource;
	}
	public void setdataSource(String value){
		_dataSource = value;
	}
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
	
	public static POILocation loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		POILocation result = new POILocation();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setname(WSHelper.getString(root,"name",false));
		this.setdistrict(WSHelper.getString(root,"district",false));
		this.setcarAccess(WSHelper.getBoolean(root,"carAccess",false));
		this.setdataSource(WSHelper.getString(root,"dataSource",false));
		this.setcity(City.loadFrom(WSHelper.getElement(root,"city")));
		this.setpoint(Point.loadFrom(WSHelper.getElement(root,"point")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("POILocation");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_name!=null)
			WSHelper.addChild(e,"name",String.valueOf(_name),false);
		if(_district!=null)
			WSHelper.addChild(e,"district",String.valueOf(_district),false);
		WSHelper.addChild(e,"carAccess",(_carAccess ? "true" : "false"),false);
		if(_dataSource!=null)
			WSHelper.addChild(e,"dataSource",String.valueOf(_dataSource),false);
		if(_city!=null)
			WSHelper.addChildNode(e, "city",null,_city);
		if(_point!=null)
			WSHelper.addChildNode(e, "point",null,_point);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_name);
		out.writeValue(_district);
		out.writeValue(_carAccess);
		out.writeValue(_dataSource);
		out.writeValue(_city);
		out.writeValue(_point);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_name=(String)in.readValue(null);
		_district=(String)in.readValue(null);
		_carAccess=(Boolean)in.readValue(null);
		_dataSource=(String)in.readValue(null);
		_city=(City)in.readValue(null);
		_point=(Point)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<POILocation> CREATOR = new android.os.Parcelable.Creator<POILocation>()
	{
		public POILocation createFromParcel(android.os.Parcel in)
		{
			POILocation tmp = new POILocation();
			tmp.readFromParcel(in);
			return tmp;
		}
		public POILocation[] newArray(int size)
		{
			return new POILocation[size];
		}
	}
	;
	
}
