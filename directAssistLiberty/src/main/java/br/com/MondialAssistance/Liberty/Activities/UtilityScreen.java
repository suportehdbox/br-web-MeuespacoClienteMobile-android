package br.com.MondialAssistance.Liberty.Activities;

import android.content.Context;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.DirectAssist.Util.Utility;

public class UtilityScreen {
	
	public static String getAssistPhoneNumber(Context context, int lobID) {
	
	switch (lobID) {
			case Utility.AUTOMOTIVE:
				return ClientParams.AssistPhoneNumberAutomotive;
			case Utility.AUTOMAKER:
				return ClientParams.AssistPhoneNumberAutomotive;
			case Utility.PROPERTY:
				return ClientParams.AssistPhoneNumberProperty;
			default:
				return "0";
		}
	}
	
	public static boolean isAutomotiveServiceEnabled(Context context) {
		return ClientParams.AutomotiveServiceEnabled;
	}
	
	public static boolean isAutomakerServiceEnabled(Context context) {
		return ClientParams.AutomakerServiceEnabled;
	}
	
	public static boolean isPropertyServiceEnabled(Context context) {
		return ClientParams.PropertyServiceEnabled;
	}
	
	public static boolean isFindPolicyDismissUserDocument(Context context) {
		return ClientParams.findPolicyUserDocument;
	}
}
