package br.com.libertyseguros.mobile.common;

import java.util.Comparator;
import java.util.Map;


public class SortComparatorMap implements Comparator<Object> {

	private String keySort;
	private TypeSortComparatorMap typeSort;
	
	public enum TypeSortComparatorMap {
		TypeSortComparatorMap_String,
		TypeSortComparatorMap_Double
	}
	
	public SortComparatorMap(String keySort, TypeSortComparatorMap typeSort) {
		this.keySort = keySort;
		this.typeSort = typeSort;
	}
	
	public int compare(Object objA, Object objB) {
		int retCompare = 0;
		
		String compA = "";
		String compB = "";
		
		Map<String, Object> campo1 = (Map<String, Object>)objA;
		Map<String, Object> campo2 = (Map<String, Object>)objB;

		if (this.typeSort == TypeSortComparatorMap.TypeSortComparatorMap_String) {
			compA = (String)campo1.get(keySort);
			compB = (String)campo2.get(keySort);
			retCompare = compA.compareTo(compB);
		}
		else if (this.typeSort == TypeSortComparatorMap.TypeSortComparatorMap_Double) {
			compA = (String)campo1.get(keySort);
			compB = (String)campo2.get(keySort);
			double doubleCompA = Double.parseDouble(compA);
			double doubleCompB = Double.parseDouble(compB);
			if (doubleCompA == doubleCompB) retCompare = 0;
			if (doubleCompA > doubleCompB) retCompare = 1;
			if (doubleCompA < doubleCompB) retCompare = -1;
		}

		return retCompare;
	}

}
