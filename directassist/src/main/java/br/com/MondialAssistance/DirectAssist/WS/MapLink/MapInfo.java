package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class MapInfo extends WSObject implements android.os.Parcelable
{
	
	private String _url;
	public String geturl(){
		return _url;
	}
	public void seturl(String value){
		_url = value;
	}
	private Extent _extent;
	public Extent getextent(){
		return _extent;
	}
	public void setextent(Extent value){
		_extent = value;
	}
	
	public static MapInfo loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		MapInfo result = new MapInfo();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.seturl(WSHelper.getString(root,"url",false));
		this.setextent(Extent.loadFrom(WSHelper.getElement(root,"extent")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("MapInfo");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_url!=null)
			WSHelper.addChild(e,"url",String.valueOf(_url),false);
		if(_extent!=null)
			WSHelper.addChildNode(e, "extent",null,_extent);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_url);
		out.writeValue(_extent);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_url=(String)in.readValue(null);
		_extent=(Extent)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<MapInfo> CREATOR = new android.os.Parcelable.Creator<MapInfo>()
	{
		public MapInfo createFromParcel(android.os.Parcel in)
		{
			MapInfo tmp = new MapInfo();
			tmp.readFromParcel(in);
			return tmp;
		}
		public MapInfo[] newArray(int size)
		{
			return new MapInfo[size];
		}
	}
	;
	
}
