package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ListAutomotiveCasesResult extends WSObject implements android.os.Parcelable
{
	
	private java.util.Vector<AutomotiveCase> _AutomotiveCases = new java.util.Vector<AutomotiveCase>();
	public java.util.Vector<AutomotiveCase> getAutomotiveCases(){
		return _AutomotiveCases;
	}
	public void setAutomotiveCases(java.util.Vector<AutomotiveCase> value){
		_AutomotiveCases = value;
	}
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static ListAutomotiveCasesResult loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ListAutomotiveCasesResult result = new ListAutomotiveCasesResult();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		list = WSHelper.getElementChildren(root, "AutomotiveCases");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_AutomotiveCases.addElement(AutomotiveCase.loadFrom(nc));
			}
		}
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ListAutomotiveCasesResult");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_AutomotiveCases!=null)
			WSHelper.addChildArray(e,"AutomotiveCases",null, _AutomotiveCases);
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeTypedList(_AutomotiveCases);
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		in.readTypedList(_AutomotiveCases, AutomotiveCase.CREATOR );
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ListAutomotiveCasesResult> CREATOR = new android.os.Parcelable.Creator<ListAutomotiveCasesResult>()
	{
		public ListAutomotiveCasesResult createFromParcel(android.os.Parcel in)
		{
			ListAutomotiveCasesResult tmp = new ListAutomotiveCasesResult();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ListAutomotiveCasesResult[] newArray(int size)
		{
			return new ListAutomotiveCasesResult[size];
		}
	}
	;
	
}
