package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Location extends WSObject implements android.os.Parcelable
{
	
	private String _Latitude;
	public String getLatitude(){
		return _Latitude;
	}
	public void setLatitude(String value){
		_Latitude = value;
	}
	private String _Longitude;
	public String getLongitude(){
		return _Longitude;
	}
	public void setLongitude(String value){
		_Longitude = value;
	}
	
	public static Location loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Location result = new Location();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setLatitude(WSHelper.getString(root,"Latitude",false));
		this.setLongitude(WSHelper.getString(root,"Longitude",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Location");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_Latitude!=null)
			WSHelper.addChild(e,"Latitude",String.valueOf(_Latitude),false);
		if(_Longitude!=null)
			WSHelper.addChild(e,"Longitude",String.valueOf(_Longitude),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_Latitude);
		out.writeValue(_Longitude);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_Latitude=(String)in.readValue(null);
		_Longitude=(String)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Location> CREATOR = new android.os.Parcelable.Creator<Location>()
	{
		public Location createFromParcel(android.os.Parcel in)
		{
			Location tmp = new Location();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Location[] newArray(int size)
		{
			return new Location[size];
		}
	}
	;
	
}
