package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class GetServiceDispatch extends WSObject implements android.os.Parcelable
{
	
	private ActionResult _ActionResult;
	public ActionResult getActionResult(){
		return _ActionResult;
	}
	public void setActionResult(ActionResult value){
		_ActionResult = value;
	}
	private Case _CaseResult;
	public Case getCaseResult(){
		return _CaseResult;
	}
	public void setCaseResult(Case value){
		_CaseResult = value;
	}
	private ServiceDispatch _ServiceDispatch;
	public ServiceDispatch getServiceDispatch(){
		return _ServiceDispatch;
	}
	public void setServiceDispatch(ServiceDispatch value){
		_ServiceDispatch = value;
	}
	private Provider _Provider;
	public Provider getProvider(){
		return _Provider;
	}
	public void setProvider(Provider value){
		_Provider = value;
	}
	
	public static GetServiceDispatch loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		GetServiceDispatch result = new GetServiceDispatch();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setActionResult(ActionResult.loadFrom(WSHelper.getElement(root,"ActionResult")));
		this.setCaseResult(Case.loadFrom(WSHelper.getElement(root,"CaseResult")));
		this.setServiceDispatch(ServiceDispatch.loadFrom(WSHelper.getElement(root,"ServiceDispatch")));
		this.setProvider(Provider.loadFrom(WSHelper.getElement(root,"Provider")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("GetServiceDispatch");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_ActionResult!=null)
			WSHelper.addChildNode(e, "ActionResult",null,_ActionResult);
		if(_CaseResult!=null)
			WSHelper.addChildNode(e, "CaseResult",null,_CaseResult);
		if(_ServiceDispatch!=null)
			WSHelper.addChildNode(e, "ServiceDispatch",null,_ServiceDispatch);
		if(_Provider!=null)
			WSHelper.addChildNode(e, "Provider",null,_Provider);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_ActionResult);
		out.writeValue(_CaseResult);
		out.writeValue(_ServiceDispatch);
		out.writeValue(_Provider);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_ActionResult=(ActionResult)in.readValue(null);
		_CaseResult=(Case)in.readValue(null);
		_ServiceDispatch=(ServiceDispatch)in.readValue(null);
		_Provider=(Provider)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<GetServiceDispatch> CREATOR = new android.os.Parcelable.Creator<GetServiceDispatch>()
	{
		public GetServiceDispatch createFromParcel(android.os.Parcel in)
		{
			GetServiceDispatch tmp = new GetServiceDispatch();
			tmp.readFromParcel(in);
			return tmp;
		}
		public GetServiceDispatch[] newArray(int size)
		{
			return new GetServiceDispatch[size];
		}
	}
	;
	
}
