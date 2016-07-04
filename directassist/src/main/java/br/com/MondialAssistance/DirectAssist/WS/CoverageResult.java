package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class CoverageResult extends WSObject implements android.os.Parcelable
{
	
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static CoverageResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		CoverageResult result = new CoverageResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("CoverageResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<CoverageResult> CREATOR = new android.os.Parcelable.Creator<CoverageResult>()
	{
		public CoverageResult createFromParcel(android.os.Parcel in)
		{
			CoverageResult tmp = new CoverageResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public CoverageResult[] newArray(int size)
		{
			return new CoverageResult[size];
		}
	}
	;
	
}
