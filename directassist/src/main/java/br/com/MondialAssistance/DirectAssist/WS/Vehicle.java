package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Vehicle extends WSObject implements android.os.Parcelable
{
	
	private Integer _Id;
	public Integer getId(){
		return _Id;
	}
	public void setId(Integer value){
		_Id = value;
	}
	private String _Chassi;
	public String getChassi(){
		return _Chassi;
	}
	public void setChassi(String value){
		_Chassi = value;
	}
	private String _LicenseNumber;
	public String getLicenseNumber(){
		return _LicenseNumber;
	}
	public void setLicenseNumber(String value){
		_LicenseNumber = value;
	}
	private String _Make;
	public String getMake(){
		return _Make;
	}
	public void setMake(String value){
		_Make = value;
	}
	private String _Model;
	public String getModel(){
		return _Model;
	}
	public void setModel(String value){
		_Model = value;
	}
	private String _Color;
	public String getColor(){
		return _Color;
	}
	public void setColor(String value){
		_Color = value;
	}
	private String _Plate;
	public String getPlate(){
		return _Plate;
	}
	public void setPlate(String value){
		_Plate = value;
	}
	private String _VehicleYear;
	public String getVehicleYear(){
		return _VehicleYear;
	}
	public void setVehicleYear(String value){
		_VehicleYear = value;
	}
	
	public static Vehicle loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Vehicle result = new Vehicle();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setId(WSHelper.getInteger(root,"Id",false));
		this.setChassi(WSHelper.getString(root,"Chassi",false));
		this.setLicenseNumber(WSHelper.getString(root,"LicenseNumber",false));
		this.setMake(WSHelper.getString(root,"Make",false));
		this.setModel(WSHelper.getString(root,"Model",false));
		this.setColor(WSHelper.getString(root,"Color",false));
		this.setPlate(WSHelper.getString(root,"Plate",false));
		this.setVehicleYear(WSHelper.getString(root,"VehicleYear",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Vehicle");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"Id",String.valueOf(_Id),false);
		if(_Chassi!=null)
			WSHelper.addChild(e,"Chassi",String.valueOf(_Chassi),false);
		if(_LicenseNumber!=null)
			WSHelper.addChild(e,"LicenseNumber",String.valueOf(_LicenseNumber),false);
		if(_Make!=null)
			WSHelper.addChild(e,"Make",String.valueOf(_Make),false);
		if(_Model!=null)
			WSHelper.addChild(e,"Model",String.valueOf(_Model),false);
		if(_Color!=null)
			WSHelper.addChild(e,"Color",String.valueOf(_Color),false);
		if(_Plate!=null)
			WSHelper.addChild(e,"Plate",String.valueOf(_Plate),false);
		if(_VehicleYear!=null)
			WSHelper.addChild(e,"VehicleYear",String.valueOf(_VehicleYear),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_Id);
		out.writeValue(_Chassi);
		out.writeValue(_LicenseNumber);
		out.writeValue(_Make);
		out.writeValue(_Model);
		out.writeValue(_Color);
		out.writeValue(_Plate);
		out.writeValue(_VehicleYear);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_Id=(Integer)in.readValue(null);
		_Chassi=(String)in.readValue(null);
		_LicenseNumber=(String)in.readValue(null);
		_Make=(String)in.readValue(null);
		_Model=(String)in.readValue(null);
		_Color=(String)in.readValue(null);
		_Plate=(String)in.readValue(null);
		_VehicleYear=(String)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Vehicle> CREATOR = new android.os.Parcelable.Creator<Vehicle>()
	{
		public Vehicle createFromParcel(android.os.Parcel in)
		{
			Vehicle tmp = new Vehicle();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Vehicle[] newArray(int size)
		{
			return new Vehicle[size];
		}
	}
	;
	
}
