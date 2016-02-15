package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Case extends WSObject implements android.os.Parcelable
{
	
	private Integer _CaseNumber;
	public Integer getCaseNumber(){
		return _CaseNumber;
	}
	public void setCaseNumber(Integer value){
		_CaseNumber = value;
	}
	private String _CreationDate;
	public String getCreationDate(){
		return _CreationDate;
	}
	public void setCreationDate(String value){
		_CreationDate = value;
	}
	
	public static Case loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Case result = new Case();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setCaseNumber(WSHelper.getInteger(root,"CaseNumber",false));
		this.setCreationDate(WSHelper.getString(root,"CreationDate",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Case");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"CaseNumber",String.valueOf(_CaseNumber),false);
		if(_CreationDate!=null)
			WSHelper.addChild(e,"CreationDate",String.valueOf(_CreationDate),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_CaseNumber);
		out.writeValue(_CreationDate);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_CaseNumber=(Integer)in.readValue(null);
		_CreationDate=(String)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Case> CREATOR = new android.os.Parcelable.Creator<Case>()
	{
		public Case createFromParcel(android.os.Parcel in)
		{
			Case tmp = new Case();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Case[] newArray(int size)
		{
			return new Case[size];
		}
	}
	;
	
}
