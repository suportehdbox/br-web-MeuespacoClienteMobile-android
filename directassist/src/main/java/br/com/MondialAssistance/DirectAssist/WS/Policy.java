package br.com.MondialAssistance.DirectAssist.WS;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class Policy extends WSObject implements android.os.Parcelable
{
	
	private String _PolicyID;
	public String getPolicyID(){
		return _PolicyID;
	}
	public void setPolicyID(String value){
		_PolicyID = value;
	}
	private String _InsuredName;
	public String getInsuredName(){
		return _InsuredName;
	}
	public void setInsuredName(String value){
		_InsuredName = value;
	}
	private String _ContractStartDate;
	public String getContractStartDate(){
		return _ContractStartDate;
	}
	public void setContractStartDate(String value){
		_ContractStartDate = value;
	}
	private String _ContractEndDate;
	public String getContractEndDate(){
		return _ContractEndDate;
	}
	public void setContractEndDate(String value){
		_ContractEndDate = value;
	}
	private Integer _ActiveStatus;
	public Integer getActiveStatus(){
		return _ActiveStatus;
	}
	public void setActiveStatus(Integer value){
		_ActiveStatus = value;
	}
	
	public static Policy loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		Policy result = new Policy();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setPolicyID(WSHelper.getString(root,"PolicyID",false));
		this.setInsuredName(WSHelper.getString(root,"InsuredName",false));
		this.setContractStartDate(WSHelper.getString(root,"ContractStartDate",false));
		this.setContractEndDate(WSHelper.getString(root,"ContractEndDate",false));
		this.setActiveStatus(WSHelper.getInteger(root,"ActiveStatus",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("Policy");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		if(_PolicyID!=null)
			WSHelper.addChild(e,"PolicyID",String.valueOf(_PolicyID),false);
		if(_InsuredName!=null)
			WSHelper.addChild(e,"InsuredName",String.valueOf(_InsuredName),false);
		if(_ContractStartDate!=null)
			WSHelper.addChild(e,"ContractStartDate",String.valueOf(_ContractStartDate),false);
		if(_ContractEndDate!=null)
			WSHelper.addChild(e,"ContractEndDate",String.valueOf(_ContractEndDate),false);
		WSHelper.addChild(e,"ActiveStatus",String.valueOf(_ActiveStatus),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_PolicyID);
		out.writeValue(_InsuredName);
		out.writeValue(_ContractStartDate);
		out.writeValue(_ContractEndDate);
		out.writeValue(_ActiveStatus);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_PolicyID=(String)in.readValue(null);
		_InsuredName=(String)in.readValue(null);
		_ContractStartDate=(String)in.readValue(null);
		_ContractEndDate=(String)in.readValue(null);
		_ActiveStatus=(Integer)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<Policy> CREATOR = new android.os.Parcelable.Creator<Policy>()
	{
		public Policy createFromParcel(android.os.Parcel in)
		{
			Policy tmp = new Policy();
			tmp.readFromParcel(in);
			return tmp;
		}
		public Policy[] newArray(int size)
		{
			return new Policy[size];
		}
	}
	;
	
}
