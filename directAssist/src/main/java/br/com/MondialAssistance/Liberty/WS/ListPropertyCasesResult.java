package br.com.MondialAssistance.Liberty.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ListPropertyCasesResult extends WSObject implements android.os.Parcelable
{
	
	private java.util.Vector<Case> _Cases = new java.util.Vector<Case>();
	public java.util.Vector<Case> getCases(){
		return _Cases;
	}
	public void setCases(java.util.Vector<Case> value){
		_Cases = value;
	}
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static ListPropertyCasesResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ListPropertyCasesResult result = new ListPropertyCasesResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		list = WSHelper.getElementChildren(root, "Cases");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_Cases.addElement(Case.loadFrom(nc));
			}
		}
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ListPropertyCasesResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_Cases!=null)
			WSHelper.addChildArray(e,"Cases",null, _Cases);
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeTypedList(_Cases);
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		in.readTypedList(_Cases, Case.CREATOR );
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ListPropertyCasesResult> CREATOR = new android.os.Parcelable.Creator<ListPropertyCasesResult>()
	{
		public ListPropertyCasesResult createFromParcel(android.os.Parcel in)
		{
			ListPropertyCasesResult tmp = new ListPropertyCasesResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ListPropertyCasesResult[] newArray(int size)
		{
			return new ListPropertyCasesResult[size];
		}
	}
	;
	
}
