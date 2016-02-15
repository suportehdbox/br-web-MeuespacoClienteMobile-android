package br.com.MondialAssistance.Liberty.common;

import android.app.Application;

public class CustomApplication extends Application {
	
	private static boolean activityVisible;

	public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void activityResumed() {
		activityVisible = true;
	}

	public static void activityPaused() {
		activityVisible = false;
	}
}
