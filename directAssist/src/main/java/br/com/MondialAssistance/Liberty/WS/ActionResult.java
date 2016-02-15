package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ActionResult extends WSObject implements android.os.Parcelable
{
	
	private Integer _ResultCode;
	public Integer getResultCode(){
		return _ResultCode;
	}
	public void setResultCode(Integer value){
		_ResultCode = value;
	}
	private String _ErrorMessage;
	public String getErrorMessage(){
		return _ErrorMessage;
	}
	public void setErrorMessage(String value){
		_ErrorMessage = value;
	}
	
	public static ActionResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ActionResult result = new ActionResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setResultCode(WSHelper.getInteger(root,"ResultCode",false));
		this.setErrorMessage(WSHelper.getString(root,"ErrorMessage",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ActionResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"ResultCode",String.valueOf(_ResultCode),false);
		if(_ErrorMessage!=null)
			WSHelper.addChild(e,"ErrorMessage",String.valueOf(_ErrorMessage),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_ResultCode);
		out.writeValue(_ErrorMessage);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_ResultCode=(Integer)in.readValue(null);
		_ErrorMessage=(String)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ActionResult> CREATOR = new android.os.Parcelable.Creator<ActionResult>()
	{
		public ActionResult createFromParcel(android.os.Parcel in)
		{
			ActionResult tmp = new ActionResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ActionResult[] newArray(int size)
		{
			return new ActionResult[size];
		}
	}
	;
	
}
