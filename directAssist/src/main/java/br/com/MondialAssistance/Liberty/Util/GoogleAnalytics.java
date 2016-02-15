///*
// * DirectAssist for Android
// *
// * Created by Danilo de Souza Salvador on 12/2011
// * Copyright 2012 Mondial Assistance. All rights reserved
// *
// * */
//
//package br.com.MondialAssistance.DirectAssist.Util;
//
//import android.content.Context;
//
//import com.google.android.apps.analytics.GoogleAnalyticsTracker;
//
//public class GoogleAnalytics {
//
//	private final static String GOOGLE_ANALYTICS_UA = "UA-28043272-1";
//	private static GoogleAnalyticsTracker analyticsTracker;
//
//	public static void setAnalyticsTracker (Context context, int clientID) {
//
//		if (analyticsTracker == null)
//			analyticsTracker = GoogleAnalyticsTracker.getInstance();
//
//		analyticsTracker.startNewSession(GOOGLE_ANALYTICS_UA, context);
//
//		analyticsTracker.setCustomVar(clientID,
//				                      context.getClass().getName(),
//				                      Client.getDeviceUID(context));
//		analyticsTracker.trackPageView(context.getPackageName());
//
//		analyticsTracker.dispatch();
//	}
//
//	public static void stopAnalyticsTracker() {
//
//		if (analyticsTracker != null)
//			analyticsTracker.stopSession();
//	}
//}