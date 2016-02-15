/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.Liberty.Util;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utility {
	
	public static final int AUTOMOTIVE = 1;
	public static final int AUTOMAKER = 2;
	public static final int PROPERTY = 3;
	
	public static final int ADDRESS = 0;
	public static final int ADDRESS_DETAILS = 1;
	public static final int ACCREDITED_ESTABLISHMENT_DETAILS = 2;
	public static final int FOLLOW_ON_THE_MAP = 3;
	public static final int SCREEN_SEARCH_ADDRESS = 4;
	public static final int ACTION_LOCATION_SOURCE_SETTINGS = 5;
	
	/* ACTION_CODE */
	public static final int CREATE_CASE = 1;
	public static final int LIST_ACCREDITED_GARAGES = 2;
	public static final int FIND_APOLICY = 3;
	public static final int SERVICE_DISPATCH = 4;
	public static final int LIST_POLICIES = 5;
	public static final int SAVE_POLICY = 6;
	public static final int DELETE_POLICY = 7;
	public static final int LIST_DEALERS = 8;
	
	public static final int FORMATDATE_YYYYMMDD = 1;
	public static final int FORMATDATE_DDMMYYYY = 2;
	
	public static final String PHONE_OPERATOR = "21";
	
	public static String getUnAccent(String value){

		String result = value; 
		
		if (result != null) {
		
			result = result.replaceAll("á|à|ã|â|ä", "a");
		    result = result.replaceAll("Á|À|Ã|Â|Ä", "A");
		    result = result.replaceAll("é|è|ê|ë", "e");
		    result = result.replaceAll("É|È|Ê|Ë", "E");
		    result = result.replaceAll("í|ì|î|ï", "i");
		    result = result.replaceAll("Í|Ì|Î|Ï", "I");
		    result = result.replaceAll("ó|ò|õ|ô|ö", "o");
		    result = result.replaceAll("Ó|Ò|Õ|Ô|Ö", "O");
		    result = result.replaceAll("ú|ù|û|ü", "u");
		    result = result.replaceAll("Ú|Ù|Û|Ü", "U");
		    result = result.replaceAll("ç", "c");
		    result = result.replaceAll("Ç", "C");
		    result = result.replaceAll("`", "");
		    result = result.replaceAll("'", "");
		    result = result.replaceAll("~", "");
		}
	    return result;
	}

	public static String getDate(Calendar calendar) {
		
		if (calendar != null) {		
			return new StringBuilder()
	                   		.append(pad(calendar.get(Calendar.DAY_OF_MONTH))).append("/")
	                   		.append(pad(calendar.get(Calendar.MONTH) + 1)).append("/")
	                   		.append(pad(calendar.get(Calendar.YEAR))).toString();
		} else {
			return "";
		}
	}
	
	public static String getTime(Calendar calendar) {
		
		if (calendar != null) {
			return new StringBuilder()
	                   		.append(pad(calendar.get(Calendar.HOUR_OF_DAY))).append(":")
	                   		.append(pad(calendar.get(Calendar.MINUTE))).toString();
		} else {
			return "";
		}
	}
	
	public static String getDateTime(Calendar calendar, int formatDate) {
		
		if (calendar != null) {
			
			switch (formatDate) {
				case FORMATDATE_YYYYMMDD:
					return new StringBuilder()
									.append(pad(calendar.get(Calendar.YEAR))).append("/")
									.append(pad(calendar.get(Calendar.MONTH) + 1)).append("/")
									.append(pad(calendar.get(Calendar.DAY_OF_MONTH))).append(" ")
									.append(pad(calendar.get(Calendar.HOUR_OF_DAY))).append(":")
							   		.append(pad(calendar.get(Calendar.MINUTE))).toString();				
				case FORMATDATE_DDMMYYYY:
					return new StringBuilder()
									.append(pad(calendar.get(Calendar.DAY_OF_MONTH))).append("/")
									.append(pad(calendar.get(Calendar.MONTH) + 1)).append("/")
									.append(pad(calendar.get(Calendar.YEAR))).append(" ")
									.append(pad(calendar.get(Calendar.HOUR_OF_DAY))).append(":")
					           		.append(pad(calendar.get(Calendar.MINUTE))).toString();
				default:
					return "";
			}
			
		} else {
			return "";
		}
	}
	
	public static String getDateByString(String dateTime) {
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTime(sdf.parse(dateTime));
			
			return new StringBuilder()
							.append(pad(calendar.get(Calendar.DAY_OF_MONTH))).append("/")
							.append(pad(calendar.get(Calendar.MONTH) + 1)).append("/")
							.append(pad(calendar.get(Calendar.YEAR))).toString();
			
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getHourByString(String dateTime) {
				
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTime(sdf.parse(dateTime));
			
			return new StringBuilder()
							.append(pad(calendar.get(Calendar.HOUR))).append(":")
							.append(pad(calendar.get(Calendar.MINUTE))).toString();
			
		} catch (Exception e) {
			return "";
		}
	}

	private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}

	public static boolean isConnected(Context context) {

/** << Liberty
 ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
 NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

 return (networkInfo == null) ? false : networkInfo.isConnected();
 >> */
		boolean conectado;
		ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conectivtyManager.getActiveNetworkInfo() != null && conectivtyManager.getActiveNetworkInfo().isAvailable() && conectivtyManager.getActiveNetworkInfo().isConnected()) {
			conectado = true;
		} else {
			conectado = false;
		}
		return conectado;
	}
}