package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class MapOptions extends WSObject implements android.os.Parcelable
{
	
	private Boolean _scaleBar;
	public Boolean getscaleBar(){
		return _scaleBar;
	}
	public void setscaleBar(Boolean value){
		_scaleBar = value;
	}
	private MapSize _mapSize;
	public MapSize getmapSize(){
		return _mapSize;
	}
	public void setmapSize(MapSize value){
		_mapSize = value;
	}
	
	public static MapOptions loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		MapOptions result = new MapOptions();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setscaleBar(WSHelper.getBoolean(root,"scaleBar",false));
		this.setmapSize(MapSize.loadFrom(WSHelper.getElement(root,"mapSize")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("MapOptions");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"scaleBar",(_scaleBar ? "true" : "false"),false);
		if(_mapSize!=null)
			WSHelper.addChildNode(e, "mapSize",null,_mapSize);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_scaleBar);
		out.writeValue(_mapSize);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_scaleBar=(Boolean)in.readValue(null);
		_mapSize=(MapSize)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<MapOptions> CREATOR = new android.os.Parcelable.Creator<MapOptions>()
	{
		public MapOptions createFromParcel(android.os.Parcel in)
		{
			MapOptions tmp = new MapOptions();
			tmp.readFromParcel(in);
			return tmp;
		}
		public MapOptions[] newArray(int size)
		{
			return new MapOptions[size];
		}
	}
	;
	
}
