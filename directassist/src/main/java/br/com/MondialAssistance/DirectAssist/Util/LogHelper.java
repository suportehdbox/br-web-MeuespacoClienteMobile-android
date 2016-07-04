/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.DirectAssist.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import br.com.MondialAssistance.DirectAssist.BLL.BLLDirectAssist;
import br.com.MondialAssistance.DirectAssist.MDL.Action;

public class LogHelper {
	
	public static void sendLog(Context context, long clientID, Action action, String actionParameter) {
		
		try {
			
			if (Utility.isConnected(context)) {
			
				BLLDirectAssist directAssist = new BLLDirectAssist();
				
				String bestProvider = ((LocationManager)context.getSystemService(Context.LOCATION_SERVICE)).getBestProvider(new Criteria(), false);
				Location location = ((LocationManager)context.getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(bestProvider);
			
				directAssist.SaveAccessLog(context, 
						                   1, 
						                   Client.getDeviceUID(context), 
						                   2, 
						                   String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.COMPONENT_ENABLED_STATE_DEFAULT).versionCode), 
						                   action.getActionCode(), 
						                   actionParameter, 
						                   action.getResultCode().toString(), 
						                   (location == null) ? 0 : location.getLatitude(), 
				                		   (location == null) ? 0 : location.getLongitude(),
		                				   clientID); 
			}
			
		} catch (Exception e) { }
	}
}
