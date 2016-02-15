/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.Liberty.Util;

import android.content.Context;
import android.telephony.TelephonyManager;
import br.com.MondialAssistance.Liberty.BLL.BLLPhone;

public class Client {
	
	public static long getClientID(Context context, int resID) {
		return Long.valueOf(context.getString(resID));
	}
	
	public static String getPhone(Context context) throws Exception {
		
		BLLPhone phone = new BLLPhone(context);
		return phone.getPhone();
	}
	
	public static Integer getPhoneAreaCode(String phoneNumber) {
		
		return Integer.valueOf(phoneNumber.substring(1, 3));
	}
	
	public static Integer getPhoneNumber(String phoneNumber) {
		
		return Integer.valueOf(phoneNumber.substring(5).trim().replace("-", ""));
	}
	
	public static String getDeviceUID(Context context) {
		
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getDeviceId();
	}
}
