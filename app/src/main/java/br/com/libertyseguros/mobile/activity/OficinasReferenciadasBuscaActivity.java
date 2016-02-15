package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.constants.Constants;

public class OficinasReferenciadasBuscaActivity extends Activity {

	private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_oficinas_referenciadas_busca);
			Util.setTitleNavigationBar(this, R.string.title_activity_oficinas_referenciadas_busca);

//		//<< ANDROID STUDIO
//		GoogleAnalyticsManager.setAnalyticsTracker(this, "Oficinas Referenciadas: Buscar por CEP");
			// Obtain the shared Tracker instance.
//			AnalyticsApplication application = (AnalyticsApplication) getApplication();
//			mTracker = application.getDefaultTracker();
			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());
//		//>>

			Button btnBuscaCEP = (Button)findViewById(R.id.btnBuscaCEP);
			btnBuscaCEP.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// << SN 11947
					if (DeviceUtils.isConnected(OficinasReferenciadasBuscaActivity.this)) {
						callCEP();
					} else {
						Util.showToast(OficinasReferenciadasBuscaActivity.this, getString(R.string.NotConnection));
					}
					// >>
				}
			});
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_oficinas_referenciadas_busca, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	private void callCEP() {
		try {
			EditText editBuscaCEP = (EditText)findViewById(R.id.editBuscaCEP);

			if (editBuscaCEP.getText().toString().compareTo("") != 0) {
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.LM_EXTRA_CEP, editBuscaCEP.getText().toString());
				data.putExtras(bundle);

				setResult(Activity.RESULT_OK, data);
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * A��o do bot�o Voltar da barra de navega��o (navigation_bar.xml)
	 * @param v
	 */
	public void voltar(View v)
	{
		try {
			onBackPressed();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}
	
	@Override
	protected void onResume() {
	  super.onResume();
		//<< ANDROID STUDIO
		Log.i("Google Analytics: ", "Oficinas Referenciadas: Buscar por CEP");
		mTracker.setScreenName("Oficinas Referenciadas: Buscar por CEP");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		//>>

	  CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  super.onPause();
	  CustomApplication.activityPaused();
	}
}
