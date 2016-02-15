package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import java.util.Timer;
import java.util.TimerTask;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;


public class LibertyMobile extends Activity   {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			if(null != getIntent().getExtras() && getIntent().getExtras().getBoolean(Constants.LM_EXTRA_PUSH)){
				
				// -- Caso venha de acesso pela notificação:
				gotoMenuPrincipal(true);
			}
			else {
				
				// -- Caso venha de inicialização normal:
				
				//Codigo para utilizar este activity como tela de splash 
				new Timer().schedule(new TimerTask() {
					
					@Override
					public void run() {
						
						gotoMenuPrincipal(false);
					}
				}, 6000);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}


	/**
	 * Chama a activity Principal 
	 * @param b, caso informa parametro nescessário para comportamento da activity.
	 */
	private void gotoMenuPrincipal(boolean b) {
		Intent intent = new Intent();
		Bundle parms = new Bundle();
		parms.putBoolean(Constants.LM_EXTRA_PUSH, b);
		intent.putExtras(parms);
		intent.setClass(LibertyMobile.this, MenuPrincipalActivity.class);
		startActivity(intent);
		
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
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
