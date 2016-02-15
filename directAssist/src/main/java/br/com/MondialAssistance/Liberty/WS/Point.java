package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Point extends WSObject implements android.os.Parcelable
{
	
	private Double _x;
	public Double getx(){
		return _x;
	}
	public void setx(Double value){
		_x = value;
	}
	private Double _y;
	public Double gety(){
		return _y;
	}
	public void sety(Double value){
		_y = value;
	}
	
	public static Point loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Point result = new Point();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setx(WSHelper.getDouble(root,"x",false));
		this.sety(WSHelper.getDouble(root,"y",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Point");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"x",String.valueOf(_x),false);
		WSHelper.addChild(e,"y",String.valueOf(_y),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_x);
		out.writeValue(_y);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_x=(Double)in.readValue(null);
		_y=(Double)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Point> CREATOR = new android.os.Parcelable.Creator<Point>()
	{
		public Point createFromParcel(android.os.Parcel in)
		{
			Point tmp = new Point();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Point[] newArray(int size)
		{
			return new Point[size];
		}
	}
	;
	
}
