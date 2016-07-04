package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ListPoliciesResult extends WSObject implements android.os.Parcelable
{
	
	private java.util.Vector<AutomotivePolicy> _AutomotivePolicies = new java.util.Vector<AutomotivePolicy>();
	public java.util.Vector<AutomotivePolicy> getAutomotivePolicies(){
		return _AutomotivePolicies;
	}
	public void setAutomotivePolicies(java.util.Vector<AutomotivePolicy> value){
		_AutomotivePolicies = value;
	}
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static ListPoliciesResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ListPoliciesResult result = new ListPoliciesResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		list = WSHelper.getElementChildren(root, "AutomotivePolicies");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_AutomotivePolicies.addElement(AutomotivePolicy.loadFrom(nc));
			}
		}
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ListPoliciesResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_AutomotivePolicies!=null)
			WSHelper.addChildArray(e,"AutomotivePolicies",null, _AutomotivePolicies);
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeTypedList(_AutomotivePolicies);
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		in.readTypedList(_AutomotivePolicies, AutomotivePolicy.CREATOR );
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ListPoliciesResult> CREATOR = new android.os.Parcelable.Creator<ListPoliciesResult>()
	{
		public ListPoliciesResult createFromParcel(android.os.Parcel in)
		{
			ListPoliciesResult tmp = new ListPoliciesResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ListPoliciesResult[] newArray(int size)
		{
			return new ListPoliciesResult[size];
		}
	}
	;
	
}
