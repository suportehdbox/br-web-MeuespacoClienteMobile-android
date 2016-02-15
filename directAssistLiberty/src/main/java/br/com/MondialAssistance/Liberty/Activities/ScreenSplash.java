package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import br.com.MondialAssistance.Liberty.BLL.BLLPhone;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.Util.ErrorHelper;
import br.com.MondialAssistance.Liberty.WS.BaseWS;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import io.fabric.sdk.android.Fabric;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenSplash extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_splash);
		
		//GoogleAnalytics.setAnalyticsTracker(this, Integer.valueOf(getString(R.string.ClientID)));
		
		BaseWS.setTypeAcess(1);
		
		Initialize();
	}
	
	private void Initialize(){
		
		Handler handler = new Handler();
		handler.postDelayed(this, 3000);
	}

	public void run() {
		
		try{
			
			BLLPhone phone = new BLLPhone(getApplicationContext());
			String phoneNumber = phone.getPhone();

			if (phoneNumber == null){

				Intent intent = new Intent(ScreenSplash.this, ScreenRegisterPhoneNumber.class);
				intent.putExtra("openLob", true);

				startActivity(intent);
				finish();
				
			} else if (!phone.getReadMessageModifyPhone() &&
					    phone.modifyPhoneNumberDigit9(phoneNumber)) {
					
				runOnUiThread(new Runnable() {
					public void run() {

						AlertDialog.Builder dialog = new AlertDialog.Builder(ScreenSplash.this);
						dialog.setIcon(android.R.drawable.ic_dialog_alert);
						dialog.setTitle(R.string.Atention);
						dialog.setMessage(R.string.ModifyPhoneNumber);
						dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								
								Intent intent = new Intent(ScreenSplash.this, ScreenRegisterPhoneNumber.class);
								intent.putExtra("openLob", true);
								
								startActivity(intent);
								finish();
							}
						});
						dialog.show();
					}
				});
				
		     } else {
			
				startActivity(new Intent(this, ScreenSelectLob.class));
//				startActivity(new Intent(this, ScreenAccreditedEstablishment.class));
				finish();				
			}			

		} catch (final Exception e) {
			
			runOnUiThread(new Runnable() {
				public void run() {

					Toast.makeText(ScreenSplash.this, ErrorHelper.setErrorMessage(e).getMessage(), Toast.LENGTH_LONG).show();
				}
			});
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		GoogleAnalytics.stopAnalyticsTracker();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CustomApplication.activityPaused();
	}
}