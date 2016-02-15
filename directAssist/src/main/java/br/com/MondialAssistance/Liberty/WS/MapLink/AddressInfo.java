package br.com.MondialAssistance.Liberty.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AddressInfo extends WSObject implements android.os.Parcelable
{
	
	private Integer _recordCount;
	public Integer getrecordCount(){
		return _recordCount;
	}
	public void setrecordCount(Integer value){
		_recordCount = value;
	}
	private Integer _pageCount;
	public Integer getpageCount(){
		return _pageCount;
	}
	public void setpageCount(Integer value){
		_pageCount = value;
	}
	private java.util.Vector<AddressLocation> _addressLocation = new java.util.Vector<AddressLocation>();
	public java.util.Vector<AddressLocation> getaddressLocation(){
		return _addressLocation;
	}
	public void setaddressLocation(java.util.Vector<AddressLocation> value){
		_addressLocation = value;
	}
	
	public static AddressInfo loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		AddressInfo result = new AddressInfo();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		this.setrecordCount(WSHelper.getInteger(root,"recordCount",false));
		this.setpageCount(WSHelper.getInteger(root,"pageCount",false));
		list = WSHelper.getElementChildren(root, "addressLocation");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_addressLocation.addElement(AddressLocation.loadFrom(nc));
			}
		}
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("AddressInfo");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"recordCount",String.valueOf(_recordCount),false);
		WSHelper.addChild(e,"pageCount",String.valueOf(_pageCount),false);
		if(_addressLocation!=null)
			WSHelper.addChildArray(e,"addressLocation",null, _addressLocation);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_recordCount);
		out.writeValue(_pageCount);
		out.writeTypedList(_addressLocation);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_recordCount=(Integer)in.readValue(null);
		_pageCount=(Integer)in.readValue(null);
		in.readTypedList(_addressLocation, AddressLocation.CREATOR );
	}
	public static final android.os.Parcelable.Creator<AddressInfo> CREATOR = new android.os.Parcelable.Creator<AddressInfo>()
	{
		public AddressInfo createFromParcel(android.os.Parcel in)
		{
			AddressInfo tmp = new AddressInfo();
			tmp.readFromParcel(in);
			return tmp;
		}
		public AddressInfo[] newArray(int size)
		{
			return new AddressInfo[size];
		}
	}
	;
	
}
