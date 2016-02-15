package br.com.libertyseguros.mobile.adapter;


public class ItemAdapter {
	
	/**
	 * Texto exibido no item do Adapter
	 */
	private String text;
	
	/**
	 * Texto secundário pode ser também exibido abaixo do texto no item do Adapter
	 */
	private String text2;
	
	/**
	 * Icones para o item do Adapter.
	 * index 0 - Left
	 * index 1 - Top
	 * index 2 - Right
	 * index 3 - Bottom
	 */
	private int[] icons;
	private Integer number;
	
	public ItemAdapter(String text, int[] icons, Integer number) {
		super();
		this.text = text;
		this.icons =  null == icons ? new int[4] : icons;
		this.number = number;
	}

	public ItemAdapter(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText2() {
		return text2;
	}
	
	public void setText2(String text2) {
		this.text2 = text2;
	}

	public int[] getIcon() {
		return icons;
	}

	public void setIcon(int[] icons) {
		this.icons = icons;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
}