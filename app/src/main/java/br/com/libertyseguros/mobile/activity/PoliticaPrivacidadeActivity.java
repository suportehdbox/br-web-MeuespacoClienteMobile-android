package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;

public class PoliticaPrivacidadeActivity extends Activity {

	private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_politica_privacidade);
			Util.setTitleNavigationBar(this, R.string.title_activity_politica_privacidade);

//			GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_politica_privacidade));
			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

			// populate the web view
			WebView webview = (WebView) findViewById(R.id.webPoliticaPrivacidade);
			webview.getSettings().setJavaScriptEnabled(true);
			//webview.getSettings().setDefaultTextEncodingName("utf-8");
			
			// << EPO - Ajuste encode:
			WebSettings settings = webview.getSettings();
			settings.setDefaultTextEncodingName("utf-8");
			String sHtml = Util.readTextFromResource(PoliticaPrivacidadeActivity.this, R.raw.infopoliticaprivacidade);
//			webview.loadData(URLEncoder.encode(sHtml).replaceAll("\\+"," "), "text/html", "utf-8");
			webview.loadDataWithBaseURL( null, sHtml.replaceAll("\\+"," "), "text/html", "utf-8", null );
			// >>
		} 
		catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		try {
//			// Inflate the menu; this adds items to the action bar if it is present.
//			getMenuInflater().inflate(R.menu.activity_politica_privacidade, menu);
//			return true;
//		} catch (Exception e) {
//			Util.showException(this, e);
//			return false;
//		}
		return true;
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
	public void onStart() {
		super.onStart();
		//ANDROID STUDIO
//		EasyTracker.getInstance(this).activityStart(this);	//Medir o tempo dentro da tela
	}
	
	@Override
	public void onStop() {
		super.onStop();
		//ANDROID STUDIO
//		EasyTracker.getInstance(this).activityStop(this);	//Para o tempo dentro da tela
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}

	@Override
	protected void onResume() {
	  super.onResume();

		Log.i("Google Analytics: ", "Atendimento");
		mTracker.setScreenName("Atendimento");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  	super.onPause();
	  	CustomApplication.activityPaused();
	}
}
