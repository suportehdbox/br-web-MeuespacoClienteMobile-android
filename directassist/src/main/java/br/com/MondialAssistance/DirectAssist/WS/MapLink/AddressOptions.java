package br.com.MondialAssistance.DirectAssist.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class AddressOptions extends WSObject implements android.os.Parcelable
{
	
	private Integer _matchType;
	public Integer getmatchType(){
		return _matchType;
	}
	public void setmatchType(Integer value){
		_matchType = value;
	}
	private Boolean _usePhonetic;
	public Boolean getusePhonetic(){
		return _usePhonetic;
	}
	public void setusePhonetic(Boolean value){
		_usePhonetic = value;
	}
	private Integer _searchType;
	public Integer getsearchType(){
		return _searchType;
	}
	public void setsearchType(Integer value){
		_searchType = value;
	}
	private ResultRange _resultRange;
	public ResultRange getresultRange(){
		return _resultRange;
	}
	public void setresultRange(ResultRange value){
		_resultRange = value;
	}
	
	public static AddressOptions loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		AddressOptions result = new AddressOptions();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setmatchType(WSHelper.getInteger(root,"matchType",false));
		this.setusePhonetic(WSHelper.getBoolean(root,"usePhonetic",false));
		this.setsearchType(WSHelper.getInteger(root,"searchType",false));
		this.setresultRange(ResultRange.loadFrom(WSHelper.getElement(root,"resultRange")));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("AddressOptions");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"matchType",String.valueOf(_matchType),false);
		WSHelper.addChild(e,"usePhonetic",(_usePhonetic ? "true" : "false"),false);
		WSHelper.addChild(e,"searchType",String.valueOf(_searchType),false);
		if(_resultRange!=null)
			WSHelper.addChildNode(e, "resultRange",null,_resultRange);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_matchType);
		out.writeValue(_usePhonetic);
		out.writeValue(_searchType);
		out.writeValue(_resultRange);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_matchType=(Integer)in.readValue(null);
		_usePhonetic=(Boolean)in.readValue(null);
		_searchType=(Integer)in.readValue(null);
		_resultRange=(ResultRange)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<AddressOptions> CREATOR = new android.os.Parcelable.Creator<AddressOptions>()
	{
		public AddressOptions createFromParcel(android.os.Parcel in)
		{
			AddressOptions tmp = new AddressOptions();
			tmp.readFromParcel(in);
			return tmp;
		}
		public AddressOptions[] newArray(int size)
		{
			return new AddressOptions[size];
		}
	}
	;
	
}
