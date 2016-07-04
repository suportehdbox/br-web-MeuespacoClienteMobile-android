package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class City extends WSObject implements android.os.Parcelable
{
	
	private String _name;
	public String getname(){
		return _name;
	}
	public void setname(String value){
		_name = value;
	}
	private String _state;
	public String getstate(){
		return _state;
	}
	public void setstate(String value){
		_state = value;
	}
	
	public static City loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		City result = new City();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setname(WSHelper.getString(root,"name",false));
		this.setstate(WSHelper.getString(root,"state",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("City");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_name!=null)
			WSHelper.addChild(e,"name",String.valueOf(_name),false);
		if(_state!=null)
			WSHelper.addChild(e,"state",String.valueOf(_state),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_name);
		out.writeValue(_state);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_name=(String)in.readValue(null);
		_state=(String)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<City> CREATOR = new android.os.Parcelable.Creator<City>()
	{
		public City createFromParcel(android.os.Parcel in)
		{
			City tmp = new City();
			tmp.readFromParcel(in);
			return tmp;
		}
		public City[] newArray(int size)
		{
			return new City[size];
		}
	}
	;
	
}
