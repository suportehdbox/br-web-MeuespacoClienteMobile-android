package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ListPropertyPoliciesResult extends WSObject implements android.os.Parcelable
{
	
	private java.util.Vector<PropertyPolicy> _PropertyPolicies = new java.util.Vector<PropertyPolicy>();
	public java.util.Vector<PropertyPolicy> getPropertyPolicies(){
		return _PropertyPolicies;
	}
	public void setPropertyPolicies(java.util.Vector<PropertyPolicy> value){
		_PropertyPolicies = value;
	}
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static ListPropertyPoliciesResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ListPropertyPoliciesResult result = new ListPropertyPoliciesResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		list = WSHelper.getElementChildren(root, "PropertyPolicies");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_PropertyPolicies.addElement(PropertyPolicy.loadFrom(nc));
			}
		}
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ListPropertyPoliciesResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_PropertyPolicies!=null)
			WSHelper.addChildArray(e,"PropertyPolicies",null, _PropertyPolicies);
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeTypedList(_PropertyPolicies);
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		in.readTypedList(_PropertyPolicies, PropertyPolicy.CREATOR );
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ListPropertyPoliciesResult> CREATOR = new android.os.Parcelable.Creator<ListPropertyPoliciesResult>()
	{
		public ListPropertyPoliciesResult createFromParcel(android.os.Parcel in)
		{
			ListPropertyPoliciesResult tmp = new ListPropertyPoliciesResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ListPropertyPoliciesResult[] newArray(int size)
		{
			return new ListPropertyPoliciesResult[size];
		}
	}
	;
	
}
