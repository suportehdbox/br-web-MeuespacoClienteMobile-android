package br.com.MondialAssistance.Liberty.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class CityLocationInfo extends WSObject implements android.os.Parcelable
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
	private java.util.Vector<CityLocation> _cityLocation = new java.util.Vector<CityLocation>();
	public java.util.Vector<CityLocation> getcityLocation(){
		return _cityLocation;
	}
	public void setcityLocation(java.util.Vector<CityLocation> value){
		_cityLocation = value;
	}
	
	public static CityLocationInfo loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		CityLocationInfo result = new CityLocationInfo();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		this.setrecordCount(WSHelper.getInteger(root,"recordCount",false));
		this.setpageCount(WSHelper.getInteger(root,"pageCount",false));
		list = WSHelper.getElementChildren(root, "cityLocation");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_cityLocation.addElement(CityLocation.loadFrom(nc));
			}
		}
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("CityLocationInfo");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"recordCount",String.valueOf(_recordCount),false);
		WSHelper.addChild(e,"pageCount",String.valueOf(_pageCount),false);
		if(_cityLocation!=null)
			WSHelper.addChildArray(e,"cityLocation",null, _cityLocation);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_recordCount);
		out.writeValue(_pageCount);
		out.writeTypedList(_cityLocation);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_recordCount=(Integer)in.readValue(null);
		_pageCount=(Integer)in.readValue(null);
		in.readTypedList(_cityLocation, CityLocation.CREATOR );
	}
	public static final android.os.Parcelable.Creator<CityLocationInfo> CREATOR = new android.os.Parcelable.Creator<CityLocationInfo>()
	{
		public CityLocationInfo createFromParcel(android.os.Parcel in)
		{
			CityLocationInfo tmp = new CityLocationInfo();
			tmp.readFromParcel(in);
			return tmp;
		}
		public CityLocationInfo[] newArray(int size)
		{
			return new CityLocationInfo[size];
		}
	}
	;
	
}
