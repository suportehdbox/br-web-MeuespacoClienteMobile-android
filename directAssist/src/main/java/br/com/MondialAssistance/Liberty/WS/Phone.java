package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Phone extends WSObject implements android.os.Parcelable
{
	
	private String _CountryCode;
	public String getCountryCode(){
		return _CountryCode;
	}
	public void setCountryCode(String value){
		_CountryCode = value;
	}
	private String _AreaCode;
	public String getAreaCode(){
		return _AreaCode;
	}
	public void setAreaCode(String value){
		_AreaCode = value;
	}
	private String _PhoneNumber;
	public String getPhoneNumber(){
		return _PhoneNumber;
	}
	public void setPhoneNumber(String value){
		_PhoneNumber = value;
	}
	
	public static Phone loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Phone result = new Phone();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setCountryCode(WSHelper.getString(root,"CountryCode",false));
		this.setAreaCode(WSHelper.getString(root,"AreaCode",false));
		this.setPhoneNumber(WSHelper.getString(root,"PhoneNumber",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Phone");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_CountryCode!=null)
			WSHelper.addChild(e,"CountryCode",String.valueOf(_CountryCode),false);
		if(_AreaCode!=null)
			WSHelper.addChild(e,"AreaCode",String.valueOf(_AreaCode),false);
		if(_PhoneNumber!=null)
			WSHelper.addChild(e,"PhoneNumber",String.valueOf(_PhoneNumber),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_CountryCode);
		out.writeValue(_AreaCode);
		out.writeValue(_PhoneNumber);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_CountryCode=(String)in.readValue(null);
		_AreaCode=(String)in.readValue(null);
		_PhoneNumber=(String)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Phone> CREATOR = new android.os.Parcelable.Creator<Phone>()
	{
		public Phone createFromParcel(android.os.Parcel in)
		{
			Phone tmp = new Phone();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Phone[] newArray(int size)
		{
			return new Phone[size];
		}
	}
	;
	
}
