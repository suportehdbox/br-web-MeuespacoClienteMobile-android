package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

public class AtendimentoActivity extends Activity implements DialogInterface.OnClickListener {

	private String phoneNumber = "";

	private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try { 
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_atendimento);

			Util.setTitleNavigationBar(this, R.string.title_activity_atendimento);

			//ANDROID STUDIO
//			GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_atendimento));
			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());
			
			// Atendimento a Capitais
			Button btnAtendimentoCapitais = (Button)findViewById(R.id.btnAtendimentoCapitais);
			btnAtendimentoCapitais.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity de comunicar acidente
					callAtendimento(R.string.box_atendimento_capitais, R.id.btnAtendimentoCapitais);
				}
			});

			// Atendimento a Demais Localidades
			Button btnAtendimentoDemaisLocalidades = (Button)findViewById(R.id.btnAtendimentoDemaisLocalidades);
			btnAtendimentoDemaisLocalidades.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity de comunicar acidente
					callAtendimento(R.string.box_atendimento_demais_localidades, R.id.btnAtendimentoDemaisLocalidades);
				}
			});

			// Atendimento a Auto e Vida
			Button btnAtendimentoAutoVida = (Button)findViewById(R.id.btnAtendimentoAutoVida);
			btnAtendimentoAutoVida.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity de comunicar acidente
					callAtendimento(R.string.box_atendimento_assistencia_24h_auto_vida, R.id.btnAtendimentoAutoVida);
				}
			});

			// Atendimento a Empresas e Resid�ncias
			Button btnAtendimentoEmpRes = (Button)findViewById(R.id.btnAtendimentoEmpRes);
			btnAtendimentoEmpRes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// chama activity de comunicar acidente
					callAtendimento(R.string.box_atendimento_assistencia_24h_empresas_residencias, R.id.btnAtendimentoEmpRes);
				}
			});
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	private void callAtendimento(int titleID, int tipoAtendimentoID) {
		try {

			String title = "";
			String message = "";

			this.phoneNumber = (String)((TextView)findViewById(tipoAtendimentoID)).getText();
			title = getString(titleID);
			message = String.format(getString(R.string.confirm_call_generic_dialog_body), this.phoneNumber);

			// Ask the user if they want to make the call
			Util.displayConfirmAlert(AtendimentoActivity.this, title, message, getString(R.string.yes),
					getString(R.string.no), AtendimentoActivity.this);
		} catch (Exception e) {
			//StackTraceElement called = new Throwable().fillInStackTrace().getStackTrace()[0];
			Util.showException(this, e);
		}		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		try {
			// If the user selected "Yes" button
			if (which == -1)
			{
				// Make a phone call to the current phone number
				try
				{
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + this.phoneNumber));
					startActivity(callIntent);
				}
				catch (ActivityNotFoundException e)
				{
					Util.showAlert(this, getString(R.string.ligacao_falhou), Constants.ERROR);
				}
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
		onBackPressed();
	}

	@Override
	public void onStart() {
		super.onStart();
//		ANDROID STUDIO
//		EasyTracker.getInstance(this).activityStart(this);	//Medir o tempo dentro da tela
	}
	
	@Override
	public void onStop() {
		super.onStop();
//		ANDROID STUDIO
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
