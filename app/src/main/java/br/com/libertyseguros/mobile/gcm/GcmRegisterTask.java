/**
 *
 */
package br.com.libertyseguros.mobile.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
import br.com.libertyseguros.mobile.common.Util;

/**
 * @author EvandroO
 *
 */
public class GcmRegisterTask extends AsyncTask<Void, Void, String> {

    /**
     * Sender ID here. This is the project number you got from the API Console.
     */
	private final static String SENDER_ID 	= "578571555423"; //"libertysegurosbrasil2014"
	private Context context;
	private GcmRegisterInterface gcmRegisterInterface;

	private LibertyException lException = null;

	public GcmRegisterTask(Context context, GcmRegisterInterface gcmRegisterInterface){
		this.context = context;
		this.gcmRegisterInterface = gcmRegisterInterface;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
    protected String doInBackground(Void ...objs) {
        String tokenNotificacao = "";
        try {
			// Check device for Play Services APK. If check succeeds, proceed with GCM registration.
			if (checkPlayServices()) {

	        	GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
	            tokenNotificacao = gcm.register(SENDER_ID);

	            Log.i("GCM", "Device registered, registration ID="+ tokenNotificacao);
			} else {
	            Log.i("GCM", "No valid Google Play Services APK found.");
	        }

        } catch (Exception e) {
        	lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_REDE_DADOS);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
			cancel(true);
        }
        return tokenNotificacao;
    }

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {

	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (!GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//	            GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//	        } else {
	            Log.i("CheckPlayServices", "This device is not supported.");
	        }
	        return false;
	    }
	    return true;
	}

	/**
	 * Executa ap�s a chamada
	 */
	@Override
	protected void onPostExecute(String result) {

		if (isCancelled()) {
			result = "";
		}
		if (null == lException) {
			gcmRegisterInterface.callBackGcmRegister(result);
		}
		else{
			gcmRegisterInterface.callBackGcmRegisterFailWithError(lException);
		}
	}

    @Override
    protected void finalize() throws Throwable {
    	Util.callGB();
    	super.finalize();
    }
}