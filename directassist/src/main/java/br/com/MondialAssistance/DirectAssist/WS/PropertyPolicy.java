package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class PropertyPolicy extends Policy
{
	
	private Address _Address;
	public Address getAddress(){
		return _Address;
	}
	public void setAddress(Address value){
		_Address = value;
	}
	
	public static PropertyPolicy loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		PropertyPolicy result = new PropertyPolicy();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		
		super.load(root);
		
		this.setAddress(Address.loadFrom(WSHelper.getElement(root,"Address")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("PropertyPolicy");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		super.fillXML(e);
		if(_Address!=null)
			WSHelper.addChildNode(e, "Address",null,_Address);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		super.writeToParcel(out,flags);
		out.writeValue(_Address);
	}
	void readFromParcel(android.os.Parcel in)
	{
		super.readFromParcel(in);
		_Address=(Address)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<PropertyPolicy> CREATOR = new android.os.Parcelable.Creator<PropertyPolicy>()
	{
		public PropertyPolicy createFromParcel(android.os.Parcel in)
		{
			PropertyPolicy tmp = new PropertyPolicy();
			tmp.readFromParcel(in);
			return tmp;
		}
		public PropertyPolicy[] newArray(int size)
		{
			return new PropertyPolicy[size];
		}
	}
	;
	
}
