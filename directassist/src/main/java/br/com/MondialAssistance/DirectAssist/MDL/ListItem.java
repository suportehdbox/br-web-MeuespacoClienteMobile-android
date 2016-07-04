package br.com.MondialAssistance.DirectAssist.MDL;

import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

public class ListItem {

	private int ID;
	private String Cod;
	private String Name;
	private String Description;
	private Drawable Icon;
	private int orientationLine = LinearLayout.HORIZONTAL;
	
	public int getID() {
		return ID;
	}
	public void setID(int id) {
		ID = id;
	}
	
	public String getCod() {
		return Cod;
	}
	public void setCod(String cod) {
		Cod = cod;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	public Drawable getIcon() {
		return Icon;
	}
	public void setIcon(Drawable icon) {
		Icon = icon;
	}
	
	public int getOrientationLine() {
		return orientationLine;
	}
	public void setOrientationLine(int orientationLine) {
		this.orientationLine = orientationLine;
	}
}
