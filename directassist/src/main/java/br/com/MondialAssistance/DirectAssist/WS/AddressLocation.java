package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AddressLocation extends WSObject implements android.os.Parcelable
{
	
	private String _key;
	public String getkey(){
		return _key;
	}
	public void setkey(String value){
		_key = value;
	}
	private Address _address;
	public Address getaddress(){
		return _address;
	}
	public void setaddress(Address value){
		_address = value;
	}
	private String _zipL;
	public String getzipL(){
		return _zipL;
	}
	public void setzipL(String value){
		_zipL = value;
	}
	private String _zipR;
	public String getzipR(){
		return _zipR;
	}
	public void setzipR(String value){
		_zipR = value;
	}
	private Boolean _carAccess;
	public Boolean getcarAccess(){
		return _carAccess;
	}
	public void setcarAccess(Boolean value){
		_carAccess = value;
	}
	private String _dataSource;
	public String getdataSource(){
		return _dataSource;
	}
	public void setdataSource(String value){
		_dataSource = value;
	}
	private Point _point;
	public Point getpoint(){
		return _point;
	}
	public void setpoint(Point value){
		_point = value;
	}
	
	public static AddressLocation loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		AddressLocation result = new AddressLocation();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setkey(WSHelper.getString(root,"key",false));
		this.setaddress(Address.loadFrom(WSHelper.getElement(root,"address")));
		this.setzipL(WSHelper.getString(root,"zipL",false));
		this.setzipR(WSHelper.getString(root,"zipR",false));
		this.setcarAccess(WSHelper.getBoolean(root,"carAccess",false));
		this.setdataSource(WSHelper.getString(root,"dataSource",false));
		this.setpoint(Point.loadFrom(WSHelper.getElement(root,"point")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("AddressLocation");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_key!=null)
			WSHelper.addChild(e,"key",String.valueOf(_key),false);
		if(_address!=null)
			WSHelper.addChildNode(e, "address",null,_address);
		if(_zipL!=null)
			WSHelper.addChild(e,"zipL",String.valueOf(_zipL),false);
		if(_zipR!=null)
			WSHelper.addChild(e,"zipR",String.valueOf(_zipR),false);
		WSHelper.addChild(e,"carAccess",(_carAccess ? "true" : "false"),false);
		if(_dataSource!=null)
			WSHelper.addChild(e,"dataSource",String.valueOf(_dataSource),false);
		if(_point!=null)
			WSHelper.addChildNode(e, "point",null,_point);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_key);
		out.writeValue(_address);
		out.writeValue(_zipL);
		out.writeValue(_zipR);
		out.writeValue(_carAccess);
		out.writeValue(_dataSource);
		out.writeValue(_point);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_key=(String)in.readValue(null);
		_address=(Address)in.readValue(null);
		_zipL=(String)in.readValue(null);
		_zipR=(String)in.readValue(null);
		_carAccess=(Boolean)in.readValue(null);
		_dataSource=(String)in.readValue(null);
		_point=(Point)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<AddressLocation> CREATOR = new android.os.Parcelable.Creator<AddressLocation>()
	{
		public AddressLocation createFromParcel(android.os.Parcel in)
		{
			AddressLocation tmp = new AddressLocation();
			tmp.readFromParcel(in);
			return tmp;
		}
		public AddressLocation[] newArray(int size)
		{
			return new AddressLocation[size];
		}
	}
	;
	
}
