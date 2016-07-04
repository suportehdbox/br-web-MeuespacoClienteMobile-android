/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.DirectAssist.Util;

import android.util.Log;

import com.neurospeech.wsclient.SoapFaultException;

public class ErrorHelper {
	
	public static Exception setErrorMessage(Exception exception){	
		
		Log.e("DIRECTASSIST", "Exception: " + exception.getMessage());
		return exception;
	}

	public static Exception setErrorMessage(SoapFaultException exception){

		Log.e("DIRECTASSIST", "SoapFaultException: " + exception.getMessage());
		return new Exception(exception.getFaultString());
	}
}
