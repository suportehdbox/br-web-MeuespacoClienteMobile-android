package br.com.libertyseguros.mobile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidEmail {

	/**
	 * Check email valid
	 * @return boolean false to email invalid, true email valid
	 */
	public boolean checkemail(String email){
	    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}
}

