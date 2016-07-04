package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class GetPropertyPoliciesResult extends WSObject implements android.os.Parcelable
{
	
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	private PropertyPolicy _PropertyPolicy;
	public PropertyPolicy getPropertyPolicy(){
		return _PropertyPolicy;
	}
	public void setPropertyPolicy(PropertyPolicy value){
		_PropertyPolicy = value;
	}
	
	public static GetPropertyPoliciesResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		GetPropertyPoliciesResult result = new GetPropertyPoliciesResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
		this.setPropertyPolicy(PropertyPolicy.loadFrom(WSHelper.getElement(root,"PropertyPolicy")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("GetPropertyPoliciesResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
		if(_PropertyPolicy!=null)
			WSHelper.addChildNode(e, "PropertyPolicy",null,_PropertyPolicy);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_ActionResult);
		out.writeValue(_PropertyPolicy);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_ActionResult=(ActionResult)in.readValue(null);
		_PropertyPolicy=(PropertyPolicy)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<GetPropertyPoliciesResult> CREATOR = new android.os.Parcelable.Creator<GetPropertyPoliciesResult>()
	{
		public GetPropertyPoliciesResult createFromParcel(android.os.Parcel in)
		{
			GetPropertyPoliciesResult tmp = new GetPropertyPoliciesResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public GetPropertyPoliciesResult[] newArray(int size)
		{
			return new GetPropertyPoliciesResult[size];
		}
	}
	;
	
}
