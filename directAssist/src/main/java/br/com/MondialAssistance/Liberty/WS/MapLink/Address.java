package br.com.MondialAssistance.Liberty.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Address extends WSObject implements android.os.Parcelable
{
	
	private String _street;
	public String getstreet(){
		return _street;
	}
	public void setstreet(String value){
		_street = value;
	}
	private String _houseNumber;
	public String gethouseNumber(){
		return _houseNumber;
	}
	public void sethouseNumber(String value){
		_houseNumber = value;
	}
	private String _zip;
	public String getzip(){
		return _zip;
	}
	public void setzip(String value){
		_zip = value;
	}
	private String _district;
	public String getdistrict(){
		return _district;
	}
	public void setdistrict(String value){
		_district = value;
	}
	private City _city;
	public City getcity(){
		return _city;
	}
	public void setcity(City value){
		_city = value;
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
		this.setstreet(WSHelper.getString(root,"street",false));
		this.sethouseNumber(WSHelper.getString(root,"houseNumber",false));
		this.setzip(WSHelper.getString(root,"zip",false));
		this.setdistrict(WSHelper.getString(root,"district",false));
		this.setcity(City.loadFrom(WSHelper.getElement(root,"city")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Address");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_street!=null)
			WSHelper.addChild(e,"street",String.valueOf(_street),false);
		if(_houseNumber!=null)
			WSHelper.addChild(e,"houseNumber",String.valueOf(_houseNumber),false);
		if(_zip!=null)
			WSHelper.addChild(e,"zip",String.valueOf(_zip),false);
		if(_district!=null)
			WSHelper.addChild(e,"district",String.valueOf(_district),false);
		if(_city!=null)
			WSHelper.addChildNode(e, "city",null,_city);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_street);
		out.writeValue(_houseNumber);
		out.writeValue(_zip);
		out.writeValue(_district);
		out.writeValue(_city);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_street=(String)in.readValue(null);
		_houseNumber=(String)in.readValue(null);
		_zip=(String)in.readValue(null);
		_district=(String)in.readValue(null);
		_city=(City)in.readValue(null);
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
