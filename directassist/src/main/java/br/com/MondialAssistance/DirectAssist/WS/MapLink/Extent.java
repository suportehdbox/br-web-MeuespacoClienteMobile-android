package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Extent extends WSObject implements android.os.Parcelable
{
	
	private Double _XMin;
	public Double getXMin(){
		return _XMin;
	}
	public void setXMin(Double value){
		_XMin = value;
	}
	private Double _YMin;
	public Double getYMin(){
		return _YMin;
	}
	public void setYMin(Double value){
		_YMin = value;
	}
	private Double _XMax;
	public Double getXMax(){
		return _XMax;
	}
	public void setXMax(Double value){
		_XMax = value;
	}
	private Double _YMax;
	public Double getYMax(){
		return _YMax;
	}
	public void setYMax(Double value){
		_YMax = value;
	}
	
	public static Extent loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Extent result = new Extent();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setXMin(WSHelper.getDouble(root,"XMin",false));
		this.setYMin(WSHelper.getDouble(root,"YMin",false));
		this.setXMax(WSHelper.getDouble(root,"XMax",false));
		this.setYMax(WSHelper.getDouble(root,"YMax",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Extent");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"XMin",String.valueOf(_XMin),false);
		WSHelper.addChild(e,"YMin",String.valueOf(_YMin),false);
		WSHelper.addChild(e,"XMax",String.valueOf(_XMax),false);
		WSHelper.addChild(e,"YMax",String.valueOf(_YMax),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_XMin);
		out.writeValue(_YMin);
		out.writeValue(_XMax);
		out.writeValue(_YMax);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_XMin=(Double)in.readValue(null);
		_YMin=(Double)in.readValue(null);
		_XMax=(Double)in.readValue(null);
		_YMax=(Double)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Extent> CREATOR = new android.os.Parcelable.Creator<Extent>()
	{
		public Extent createFromParcel(android.os.Parcel in)
		{
			Extent tmp = new Extent();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Extent[] newArray(int size)
		{
			return new Extent[size];
		}
	}
	;
	
}
