package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class CreateCaseResult extends WSObject implements android.os.Parcelable
{
	
	private Integer _CaseNumber;
	public Integer getCaseNumber(){
		return _CaseNumber;
	}
	public void setCaseNumber(Integer value){
		_CaseNumber = value;
	}
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static CreateCaseResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		CreateCaseResult result = new CreateCaseResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setCaseNumber(WSHelper.getInteger(root,"CaseNumber",false));
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("CreateCaseResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"CaseNumber",String.valueOf(_CaseNumber),false);
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_CaseNumber);
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_CaseNumber=(Integer)in.readValue(null);
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<CreateCaseResult> CREATOR = new android.os.Parcelable.Creator<CreateCaseResult>()
	{
		public CreateCaseResult createFromParcel(android.os.Parcel in)
		{
			CreateCaseResult tmp = new CreateCaseResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public CreateCaseResult[] newArray(int size)
		{
			return new CreateCaseResult[size];
		}
	}
	;
	
}
