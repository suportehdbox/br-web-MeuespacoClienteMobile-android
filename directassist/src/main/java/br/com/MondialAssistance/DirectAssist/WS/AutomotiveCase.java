package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AutomotiveCase extends Case
{
	
	private Vehicle _Vehicle;
	public Vehicle getVehicle(){
		return _Vehicle;
	}
	public void setVehicle(Vehicle value){
		_Vehicle = value;
	}
	
	public static AutomotiveCase loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		AutomotiveCase result = new AutomotiveCase();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		
		super.load(root);
		
		this.setVehicle(Vehicle.loadFrom(WSHelper.getElement(root,"Vehicle")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("AutomotiveCase");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		super.fillXML(e);
		if(_Vehicle!=null)
			WSHelper.addChildNode(e, "Vehicle",null,_Vehicle);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		super.writeToParcel(out,flags);
		out.writeValue(_Vehicle);
	}
	void readFromParcel(android.os.Parcel in)
	{
		super.readFromParcel(in);
		_Vehicle=(Vehicle)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<AutomotiveCase> CREATOR = new android.os.Parcelable.Creator<AutomotiveCase>()
	{
		public AutomotiveCase createFromParcel(android.os.Parcel in)
		{
			AutomotiveCase tmp = new AutomotiveCase();
			tmp.readFromParcel(in);
			return tmp;
		}
		public AutomotiveCase[] newArray(int size)
		{
			return new AutomotiveCase[size];
		}
	}
	;
	
}
