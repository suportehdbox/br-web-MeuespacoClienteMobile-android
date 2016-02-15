package br.com.MondialAssistance.Liberty.WS.MapLink;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class ResultRange extends WSObject implements android.os.Parcelable
{
	
	private Integer _pageIndex;
	public Integer getpageIndex(){
		return _pageIndex;
	}
	public void setpageIndex(Integer value){
		_pageIndex = value;
	}
	private Integer _recordsPerPage;
	public Integer getrecordsPerPage(){
		return _recordsPerPage;
	}
	public void setrecordsPerPage(Integer value){
		_recordsPerPage = value;
	}
	
	public static ResultRange loadFrom(Element root) throws Exception
	{
		if(root==null){
			return null;
		}
		ResultRange result = new ResultRange();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setpageIndex(WSHelper.getInteger(root,"pageIndex",false));
		this.setrecordsPerPage(WSHelper.getInteger(root,"recordsPerPage",false));
	}
	
	
	
	public Element toXMLElement(Element root)
	{
		Element e = root.getOwnerDocument().createElement("ResultRange");
		fillXML(e);
		return e;
	}
	
	public void fillXML(Element e)
	{
		WSHelper.addChild(e,"pageIndex",String.valueOf(_pageIndex),false);
		WSHelper.addChild(e,"recordsPerPage",String.valueOf(_recordsPerPage),false);
	}
	public int describeContents(){ return 0; }
	public void writeToParcel(android.os.Parcel out, int flags)
	{
		out.writeValue(_pageIndex);
		out.writeValue(_recordsPerPage);
	}
	void readFromParcel(android.os.Parcel in)
	{
		_pageIndex=(Integer)in.readValue(null);
		_recordsPerPage=(Integer)in.readValue(null);
	}
	public static final android.os.Parcelable.Creator<ResultRange> CREATOR = new android.os.Parcelable.Creator<ResultRange>()
	{
		public ResultRange createFromParcel(android.os.Parcel in)
		{
			ResultRange tmp = new ResultRange();
			tmp.readFromParcel(in);
			return tmp;
		}
		public ResultRange[] newArray(int size)
		{
			return new ResultRange[size];
		}
	}
	;
	
}
