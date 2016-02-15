package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Provider extends WSObject implements android.os.Parcelable
{
	
	private String _ProviderCode;
	public String getProviderCode(){
		return _ProviderCode;
	}
	public void setProviderCode(String value){
		_ProviderCode = value;
	}
	private String _LicenseNumber;
	public String getLicenseNumber(){
		return _LicenseNumber;
	}
	public void setLicenseNumber(String value){
		_LicenseNumber = value;
	}
	private Location _Location;
	public Location getLocation(){
		return _Location;
	}
	public void setLocation(Location value){
		_Location = value;
	}
	
	public static Provider loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Provider result = new Provider();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setProviderCode(WSHelper.getString(root,"ProviderCode",false));
		this.setLicenseNumber(WSHelper.getString(root,"LicenseNumber",false));
		this.setLocation(Location.loadFrom(WSHelper.getElement(root,"Location")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Provider");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_ProviderCode!=null)
			WSHelper.addChild(e,"ProviderCode",String.valueOf(_ProviderCode),false);
		if(_LicenseNumber!=null)
			WSHelper.addChild(e,"LicenseNumber",String.valueOf(_LicenseNumber),false);
		if(_Location!=null)
			WSHelper.addChildNode(e, "Location",null,_Location);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_ProviderCode);
		out.writeValue(_LicenseNumber);
		out.writeValue(_Location);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_ProviderCode=(String)in.readValue(null);
		_LicenseNumber=(String)in.readValue(null);
		_Location=(Location)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Provider> CREATOR = new android.os.Parcelable.Creator<Provider>()
	{
		public Provider createFromParcel(android.os.Parcel in)
		{
			Provider tmp = new Provider();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Provider[] newArray(int size)
		{
			return new Provider[size];
		}
	}
	;
	
}
