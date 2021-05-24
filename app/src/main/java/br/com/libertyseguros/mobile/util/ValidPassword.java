package br.com.libertyseguros.mobile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPassword {

	/**
	 * Check password valid
	 * @return boolean false to password invalid, true password valid
	 */
	public boolean checkPassword(String userName, String password){
		if(userName.contains(" ")){
			String[] names = userName.split("");
			for (String name : names) {
				if(password.contains(name))
					return false;
			}
		}else{
			if(password.contains(userName))
				return false;
		}


	    Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?.]{6,}$");
	    Matcher matcher = pattern.matcher(password);

	    return matcher.matches();
	}

	public boolean checkPassword(String password){
	    Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?.]{8,}$");
	    Matcher matcher = pattern.matcher(password);

	    return matcher.matches();
	}
}

