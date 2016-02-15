package br.com.libertyseguros.mobile.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

public class ClubeLibertyLocaisDetalheActivity extends LibertyMobileApp implements DialogInterface.OnClickListener {

	private Map<String, Object> itemSel;
	private String phoneNumber = "";
	private String titulo;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_clube_liberty_locais_detalhe);
			Util.setTitleNavigationBar(this, R.string.title_activity_clube_liberty_locais_detalhe);

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

			Intent it = getIntent();
			if (it == null) return;		

			Bundle parms = it.getExtras();
			itemSel = (Map<String, Object>) parms.getSerializable(Constants.LM_EXTRA_ITEM);

			String urlImagem = (String)itemSel.get("Logo");
			urlImagem = urlImagem.replace(Constants.LMUrlInterna, Constants.LMUrlExterna);

			titulo = (String)itemSel.get("Titulo");

			String beneficios = (String)itemSel.get("Beneficio");
			String beneficiosExclusivos = (String)itemSel.get("BeneficioExclusivoMeuEspaco");
			String abrangencia = (String)itemSel.get("Abrangencia");
			String comoUsar = (String)itemSel.get("ComoUsar");

			//Se o Segurado estiver logado altera os beneficios para o beneficios exclusivos
			if (getDadosLoginSegurado() != null) {
				if (getDadosLoginSegurado().getLogado()) {
					if (!beneficiosExclusivos.equals("")) {
						beneficios = beneficiosExclusivos;
					}
				}
			}

			setTitle(titulo);

			String endereco = "";
			String bairro = "";
			String cidade = "";
			String estado = "";
			String cep = "";
			String strLatitude = "";
			String strLongitude = "";

			//Pega informa��es direto do Contato
			List<Object> arrayContatos = (List<Object>)itemSel.get("Contatos");
			if (arrayContatos.size() != 0) {
				Map<String, Object> contato = (Map<String, Object>)arrayContatos.get(0);
				endereco = (String)contato.get("Endereco");
				bairro = (String)contato.get("Bairro");
				cidade = (String)contato.get("Cidade");
				estado = (String)contato.get("Estado");
				cep = (String)contato.get("CEP");
				this.phoneNumber = (String)contato.get("Telefone");
				strLatitude = (String)contato.get("Latitude");
				strLongitude = (String)contato.get("Longitude");
			}

			String sHtml = Util.readTextFromResource(ClubeLibertyLocaisDetalheActivity.this, R.raw.infoclubeliberty);

			sHtml = sHtml.replace("%Titulo", titulo);
			sHtml = sHtml.replace("%Beneficio", beneficios);
			sHtml = sHtml.replace("%Abrangencia", abrangencia);
			sHtml = sHtml.replace("%Logo", urlImagem);
			sHtml = sHtml.replace("%ComoUsar", comoUsar);

			if (!endereco.equals("") && arrayContatos.size() != 0) {
				sHtml = sHtml.replace("%Endereco", endereco);
				sHtml = sHtml.replace("%BairroCidadeEstado", bairro + "-" + cidade + "/" + estado + " - CEP: " + cep);
			}
			else {
				sHtml = sHtml.replace("%Endereco", "* Ofertas somente pelo site");
				sHtml = sHtml.replace("%BairroCidadeEstado", "");			
			}

			//Load dados para o WebView
			WebView webview = (WebView) findViewById(R.id.webClubeLiberty);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setDefaultTextEncodingName("utf-8");
			
			// << EPO - Ajuste encode
//			webview.loadData(URLEncoder.encode(sHtml).replaceAll("\\+"," "), "text/html", "utf-8");
			webview.loadDataWithBaseURL( null, sHtml.replaceAll("\\+"," "), "text/html", "utf-8", null );
			// >>

			// Visualizar o mapa
			Button btnClubeLibertyMapa = (Button)findViewById(R.id.btnClubeLibertyMapa);
			btnClubeLibertyMapa.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callMapa();
				}
			});

			// Chamando o telefone do Clube Liberty
			Button btnClubeLibertyLigar = (Button)findViewById(R.id.btnClubeLibertyLigar);
			btnClubeLibertyLigar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callClubeLiberty();
				}
			});

			boolean bVisivel = false;

			if (this.phoneNumber.compareTo("") == 0) {
				btnClubeLibertyLigar.setVisibility(View.INVISIBLE);
			}
			else {
				bVisivel = true;
			}

			if (strLatitude.compareTo("") == 0 || strLongitude.compareTo("") == 0) {
				btnClubeLibertyMapa.setVisibility(View.INVISIBLE);
			}
			else {
				bVisivel = true;
			}

			if (!bVisivel) {
				LinearLayout lay = (LinearLayout)findViewById(R.id.layClubeLibertyBotoes);
				lay.removeAllViews();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_clube_liberty_locais_detalhe, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}


	private void callClubeLiberty() {
		try {
			String title = "Clube Liberty";
			String message = "";

			message = String.format(getString(R.string.confirm_call_generic_dialog_body), this.phoneNumber);

			// Ask the user if they want to make the call
			Util.displayConfirmAlert(ClubeLibertyLocaisDetalheActivity.this, title, message, getString(R.string.yes),
					getString(R.string.no), ClubeLibertyLocaisDetalheActivity.this);
		} catch (Exception e) {
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
				catch (ActivityNotFoundException activityException)
				{
					Util.showAlert(this, getString(R.string.ligacao_falhou), Constants.ERROR);
				}
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void callMapa() {
		try {
			Bundle params = new Bundle();
			params.putSerializable(Constants.LM_EXTRA_ITEM, (Serializable) itemSel);

			Intent it = new Intent(this, ClubeLibertyLocaisMapaActivity.class);    	
			it.putExtras(params);
			startActivityForResult(it, 2);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Ação do botão Voltar da barra de navegação (navigation_bar.xml)
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

		String screenName = "Clube Liberty: Detalhe - " + titulo;

		Log.i("Google Analytics: ", screenName);
		mTracker.setScreenName(screenName);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

//	  CustomApplication.activityResumed();
	}

}
