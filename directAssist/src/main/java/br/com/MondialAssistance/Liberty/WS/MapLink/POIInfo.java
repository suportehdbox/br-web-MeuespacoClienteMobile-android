package br.com.MondialAssistance.Liberty.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class POIInfo extends WSObject implements android.os.Parcelable
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
	private java.util.Vector<POILocation> _poiLocations = new java.util.Vector<POILocation>();
	public java.util.Vector<POILocation> getpoiLocations(){
		return _poiLocations;
	}
	public void setpoiLocations(java.util.Vector<POILocation> value){
		_poiLocations = value;
	}
	
	public static POIInfo loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		POIInfo result = new POIInfo();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		this.setrecordCount(WSHelper.getInteger(root,"recordCount",false));
		this.setpageCount(WSHelper.getInteger(root,"pageCount",false));
		list = WSHelper.getElementChildren(root, "poiLocations");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_poiLocations.addElement(POILocation.loadFrom(nc));
			}
		}
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("POIInfo");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"recordCount",String.valueOf(_recordCount),false);
		WSHelper.addChild(e,"pageCount",String.valueOf(_pageCount),false);
		if(_poiLocations!=null)
			WSHelper.addChildArray(e,"poiLocations",null, _poiLocations);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_recordCount);
		out.writeValue(_pageCount);
		out.writeTypedList(_poiLocations);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_recordCount=(Integer)in.readValue(null);
		_pageCount=(Integer)in.readValue(null);
		in.readTypedList(_poiLocations, POILocation.CREATOR );
	}
	public static final android.os.Parcelable.Creator<POIInfo> CREATOR = new android.os.Parcelable.Creator<POIInfo>()
	{
		public POIInfo createFromParcel(android.os.Parcel in)
		{
			POIInfo tmp = new POIInfo();
			tmp.readFromParcel(in);
			return tmp;
		}
		public POIInfo[] newArray(int size)
		{
			return new POIInfo[size];
		}
	}
	;
	
}
