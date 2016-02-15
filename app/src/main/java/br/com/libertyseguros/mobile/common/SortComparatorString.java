package br.com.libertyseguros.mobile.common;

import java.util.Comparator;

public class SortComparatorString implements Comparator<String> {
	
	public int compare(String strA, String strB) {
		return strA.compareTo(strB);
	}
}
