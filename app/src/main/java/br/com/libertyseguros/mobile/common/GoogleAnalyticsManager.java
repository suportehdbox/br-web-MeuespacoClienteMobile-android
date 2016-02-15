//package br.com.libertyseguros.mobile.common;
//
//import android.content.Context;
//
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;
//
//import br.com.libertyseguros.mobile.constants.Constants;
//
//public class GoogleAnalyticsManager {
//
//	private static Tracker analyticsTracker;
//
//	public static void setAnalyticsTracker (Context context, String name) {
//
//		if (analyticsTracker == null)
//			analyticsTracker = GoogleAnalytics.getInstance(context).getTracker(Constants.GOOGLE_ANALYTICS_UA);
//
//		analyticsTracker.set(Fields.SCREEN_NAME, name);
//		analyticsTracker.send(MapBuilder
//				.createAppView()
//				.build()
//				);
//	}
//}
