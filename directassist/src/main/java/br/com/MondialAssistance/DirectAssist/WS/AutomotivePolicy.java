package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AutomotivePolicy extends Policy
{
	
	private Integer _GroupID;
	public Integer getGroupID(){
		return _GroupID;
	}
	public void setGroupID(Integer value){
		_GroupID = value;
	}
	private Vehicle _Vehicle;
	public Vehicle getVehicle(){
		return _Vehicle;
	}
	public void setVehicle(Vehicle value){
		_Vehicle = value;
	}
	
	public static AutomotivePolicy loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		AutomotivePolicy result = new AutomotivePolicy();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		
		super.load(root);
		
		this.setGroupID(WSHelper.getInteger(root,"GroupID",false));
		this.setVehicle(Vehicle.loadFrom(WSHelper.getElement(root,"Vehicle")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("AutomotivePolicy");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		super.fillXML(e);
		WSHelper.addChild(e,"GroupID",String.valueOf(_GroupID),false);
		if(_Vehicle!=null)
			WSHelper.addChildNode(e, "Vehicle",null,_Vehicle);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		super.writeToParcel(out,flags);
		out.writeValue(_GroupID);
		out.writeValue(_Vehicle);
	}
	void readFromParcel(android.os.Parcel in)
	{
		super.readFromParcel(in);
		_GroupID=(Integer)in.readValue(null);
		_Vehicle=(Vehicle)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<AutomotivePolicy> CREATOR = new android.os.Parcelable.Creator<AutomotivePolicy>()
	{
		public AutomotivePolicy createFromParcel(android.os.Parcel in)
		{
			AutomotivePolicy tmp = new AutomotivePolicy();
			tmp.readFromParcel(in);
			return tmp;
		}
		public AutomotivePolicy[] newArray(int size)
		{
			return new AutomotivePolicy[size];
		}
	}
	;
	
}
