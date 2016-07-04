package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AccreditedGarage extends WSObject implements android.os.Parcelable
{
	
	private String _GarageName;
	public String getGarageName(){
		return _GarageName;
	}
	public void setGarageName(String value){
		_GarageName = value;
	}
	private Address _Address;
	public Address getAddress(){
		return _Address;
	}
	public void setAddress(Address value){
		_Address = value;
	}
	private Phone _Phone;
	public Phone getPhone(){
		return _Phone;
	}
	public void setPhone(Phone value){
		_Phone = value;
	}
	private Double _Distance;
	public Double getDistance(){
		return _Distance;
	}
	public void setDistance(Double value){
		_Distance = value;
	}
	
	public static AccreditedGarage loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		AccreditedGarage result = new AccreditedGarage();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setGarageName(WSHelper.getString(root,"GarageName",false));
		this.setAddress(Address.loadFrom(WSHelper.getElement(root,"Address")));
		this.setPhone(Phone.loadFrom(WSHelper.getElement(root,"Phone")));
		this.setDistance(WSHelper.getDouble(root,"Distance",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("AccreditedGarage");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_GarageName!=null)
			WSHelper.addChild(e,"GarageName",String.valueOf(_GarageName),false);
		if(_Address!=null)
			WSHelper.addChildNode(e, "Address",null,_Address);
		if(_Phone!=null)
			WSHelper.addChildNode(e, "Phone",null,_Phone);
		WSHelper.addChild(e,"Distance",String.valueOf(_Distance),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_GarageName);
		out.writeValue(_Address);
		out.writeValue(_Phone);
		out.writeValue(_Distance);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_GarageName=(String)in.readValue(null);
		_Address=(Address)in.readValue(null);
		_Phone=(Phone)in.readValue(null);
		_Distance=(Double)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<AccreditedGarage> CREATOR = new android.os.Parcelable.Creator<AccreditedGarage>()
	{
		public AccreditedGarage createFromParcel(android.os.Parcel in)
		{
			AccreditedGarage tmp = new AccreditedGarage();
			tmp.readFromParcel(in);
			return tmp;
		}
		public AccreditedGarage[] newArray(int size)
		{
			return new AccreditedGarage[size];
		}
	}
	;
	
}
