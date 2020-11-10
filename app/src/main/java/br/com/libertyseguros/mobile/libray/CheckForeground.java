package br.com.libertyseguros.mobile.libray;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Class that verify if installer is in foreground
 */
public class CheckForeground {

	private boolean appFound;
	private boolean appOpen;
	private ActivityManager acManager;
	private List<RunningTaskInfo> runningTasks;

	public boolean isAppInForeground(String installerPackage, Context context) {

		appFound = false;

		acManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		runningTasks = acManager.getRunningTasks(Integer.MAX_VALUE);

		if(runningTasks != null && runningTasks.size() > 0){
			if (runningTasks.get(0).topActivity.toString().contains(
					"ComponentInfo{" + installerPackage + "")) {
				appFound = true;
			}
		}
		//Log.v((Config.TAG, "App in Foreground? "+appFound);
		return appFound;
	}

	public boolean isAppIsOpen(String installerPackage, Context context) {

		appOpen = false;

		acManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		runningTasks = acManager.getRunningTasks(Integer.MAX_VALUE);

		if(runningTasks != null && runningTasks.size() > 0){
			for(int ind = 0; ind < runningTasks.size(); ind++){
				if (runningTasks.get(ind).topActivity.toString().contains(
						"ComponentInfo{" + installerPackage + "")) {
					appOpen = true;
				}
			}
		}
		//Log.v((Config.TAG, "App in Foreground? "+appOpen);
		return appOpen;
	}

}
