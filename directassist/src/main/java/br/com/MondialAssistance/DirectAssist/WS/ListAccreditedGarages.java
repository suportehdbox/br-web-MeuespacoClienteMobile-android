package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ListAccreditedGarages extends WSObject implements android.os.Parcelable
{
	
	private java.util.Vector<AccreditedGarage> _AccreditedGarages = new java.util.Vector<AccreditedGarage>();
	public java.util.Vector<AccreditedGarage> getAccreditedGarages(){
		return _AccreditedGarages;
	}
	public void setAccreditedGarages(java.util.Vector<AccreditedGarage> value){
		_AccreditedGarages = value;
	}
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	
	public static ListAccreditedGarages loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ListAccreditedGarages result = new ListAccreditedGarages();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		NodeList list;
		int i;
		list = WSHelper.getElementChildren(root, "AccreditedGarages");
		if(list != null)
		{
			for(i=0;i<list.getLength();i++)
			{
				Element nc = (Element)list.item(i);
				_AccreditedGarages.addElement(AccreditedGarage.loadFrom(nc));
			}
		}
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ListAccreditedGarages");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_AccreditedGarages!=null)
			WSHelper.addChildArray(e,"AccreditedGarages",null, _AccreditedGarages);
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeTypedList(_AccreditedGarages);
		out.writeValue(_ActionResult);
	}
	void readFromParcel(android.os.Parcel in)
	{
		in.readTypedList(_AccreditedGarages, AccreditedGarage.CREATOR );
		_ActionResult=(ActionResult)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ListAccreditedGarages> CREATOR = new android.os.Parcelable.Creator<ListAccreditedGarages>()
	{
		public ListAccreditedGarages createFromParcel(android.os.Parcel in)
		{
			ListAccreditedGarages tmp = new ListAccreditedGarages();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ListAccreditedGarages[] newArray(int size)
		{
			return new ListAccreditedGarages[size];
		}
	}
	;
	
}
