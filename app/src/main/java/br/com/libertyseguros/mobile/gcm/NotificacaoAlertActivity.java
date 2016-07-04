package br.com.libertyseguros.mobile.gcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Menu;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

public class NotificacaoAlertActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_notificacao_alert);
			
			//-- Pega mensagem da notificacão
			String message = getIntent().getExtras().getString(Constants.LM_EXTRA_PUSH);
			
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(this.getString(R.string.app_name));
			alertDialog.setMessage(message);
			alertDialog.setButton(this.getString(R.string.btn_ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			alertDialog.setOnDismissListener(
					new OnDismissListener() {
						public void onDismiss(DialogInterface arg0) {
							finish();
						}
					});
			
			alertDialog.show();
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
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