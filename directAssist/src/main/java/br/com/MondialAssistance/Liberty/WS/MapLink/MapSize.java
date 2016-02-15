package br.com.MondialAssistance.Liberty.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class MapSize extends WSObject implements android.os.Parcelable
{
	
	private Integer _width;
	public Integer getwidth(){
		return _width;
	}
	public void setwidth(Integer value){
		_width = value;
	}
	private Integer _height;
	public Integer getheight(){
		return _height;
	}
	public void setheight(Integer value){
		_height = value;
	}
	
	public static MapSize loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		MapSize result = new MapSize();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setwidth(WSHelper.getInteger(root,"width",false));
		this.setheight(WSHelper.getInteger(root,"height",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("MapSize");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"width",String.valueOf(_width),false);
		WSHelper.addChild(e,"height",String.valueOf(_height),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_width);
		out.writeValue(_height);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_width=(Integer)in.readValue(null);
		_height=(Integer)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<MapSize> CREATOR = new android.os.Parcelable.Creator<MapSize>()
	{
		public MapSize createFromParcel(android.os.Parcel in)
		{
			MapSize tmp = new MapSize();
			tmp.readFromParcel(in);
			return tmp;
		}
		public MapSize[] newArray(int size)
		{
			return new MapSize[size];
		}
	}
	;
	
}
