package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Address extends WSObject implements android.os.Parcelable
{
	
	private String _StreetName;
	public String getStreetName(){
		return _StreetName;
	}
	public void setStreetName(String value){
		_StreetName = value;
	}
	private String _HouseNumber;
	public String getHouseNumber(){
		return _HouseNumber;
	}
	public void setHouseNumber(String value){
		_HouseNumber = value;
	}
	private String _District;
	public String getDistrict(){
		return _District;
	}
	public void setDistrict(String value){
		_District = value;
	}
	private String _Complement;
	public String getComplement(){
		return _Complement;
	}
	public void setComplement(String value){
		_Complement = value;
	}
	private String _City;
	public String getCity(){
		return _City;
	}
	public void setCity(String value){
		_City = value;
	}
	private String _State;
	public String getState(){
		return _State;
	}
	public void setState(String value){
		_State = value;
	}
	private Double _Latitude;
	public Double getLatitude(){
		return _Latitude;
	}
	public void setLatitude(Double value){
		_Latitude = value;
	}
	private Double _Longitude;
	public Double getLongitude(){
		return _Longitude;
	}
	public void setLongitude(Double value){
		_Longitude = value;
	}
	
	public static Address loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Address result = new Address();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setStreetName(WSHelper.getString(root,"StreetName",false));
		this.setHouseNumber(WSHelper.getString(root,"HouseNumber",false));
		this.setDistrict(WSHelper.getString(root,"District",false));
		this.setComplement(WSHelper.getString(root,"Complement",false));
		this.setCity(WSHelper.getString(root,"City",false));
		this.setState(WSHelper.getString(root,"State",false));
		this.setLatitude(WSHelper.getDouble(root,"Latitude",false));
		this.setLongitude(WSHelper.getDouble(root,"Longitude",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Address");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_StreetName!=null)
			WSHelper.addChild(e,"StreetName",String.valueOf(_StreetName),false);
		if(_HouseNumber!=null)
			WSHelper.addChild(e,"HouseNumber",String.valueOf(_HouseNumber),false);
		if(_District!=null)
			WSHelper.addChild(e,"District",String.valueOf(_District),false);
		if(_Complement!=null)
			WSHelper.addChild(e,"Complement",String.valueOf(_Complement),false);
		if(_City!=null)
			WSHelper.addChild(e,"City",String.valueOf(_City),false);
		if(_State!=null)
			WSHelper.addChild(e,"State",String.valueOf(_State),false);
		WSHelper.addChild(e,"Latitude",String.valueOf(_Latitude),false);
		WSHelper.addChild(e,"Longitude",String.valueOf(_Longitude),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_StreetName);
		out.writeValue(_HouseNumber);
		out.writeValue(_District);
		out.writeValue(_Complement);
		out.writeValue(_City);
		out.writeValue(_State);
		out.writeValue(_Latitude);
		out.writeValue(_Longitude);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_StreetName=(String)in.readValue(null);
		_HouseNumber=(String)in.readValue(null);
		_District=(String)in.readValue(null);
		_Complement=(String)in.readValue(null);
		_City=(String)in.readValue(null);
		_State=(String)in.readValue(null);
		_Latitude=(Double)in.readValue(null);
		_Longitude=(Double)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Address> CREATOR = new android.os.Parcelable.Creator<Address>()
	{
		public Address createFromParcel(android.os.Parcel in)
		{
			Address tmp = new Address();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Address[] newArray(int size)
		{
			return new Address[size];
		}
	}
	;
	
}
