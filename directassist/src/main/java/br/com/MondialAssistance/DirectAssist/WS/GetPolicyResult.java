package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class GetPolicyResult extends WSObject implements android.os.Parcelable
{
	
	private AutomotivePolicy _AutomotivePolicy;
	public AutomotivePolicy getAutomotivePolicy(){
		return _AutomotivePolicy;
	}
	public void setAutomotivePolicy(AutomotivePolicy value){
		_AutomotivePolicy = value;
	}
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static GetPolicyResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		GetPolicyResult result = new GetPolicyResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setAutomotivePolicy(AutomotivePolicy.loadFrom(WSHelper.getElement(root,"AutomotivePolicy")));
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("GetPolicyResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_AutomotivePolicy!=null)
			WSHelper.addChildNode(e, "AutomotivePolicy",null,_AutomotivePolicy);
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_AutomotivePolicy);
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_AutomotivePolicy=(AutomotivePolicy)in.readValue(null);
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<GetPolicyResult> CREATOR = new android.os.Parcelable.Creator<GetPolicyResult>()
	{
		public GetPolicyResult createFromParcel(android.os.Parcel in)
		{
			GetPolicyResult tmp = new GetPolicyResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public GetPolicyResult[] newArray(int size)
		{
			return new GetPolicyResult[size];
		}
	}
	;
	
}
