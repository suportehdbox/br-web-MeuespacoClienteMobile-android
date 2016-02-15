package br.com.libertyseguros.mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ClubeLibertyLocaisAdapter;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

public class ClubeLibertyLocaisActivity extends LibertyMobileApp {

	private ListView lvwList;
	//	private List<Object> clubeLibertyParam;
	private List<Object> clubeLibertyByCategoria;

	private ProgressDialog progress;
	private Handler handler = new Handler();

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_clube_liberty_locais);
			Util.setTitleNavigationBar(this, R.string.title_activity_clube_liberty_locais);

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

			lvwList = (ListView) findViewById(R.id.lvwClubeLiberty);

			Intent it = getIntent();
			if (it == null) return;		

			Bundle parms = it.getExtras();
			String categoria = parms.getString(Constants.LM_EXTRA_CATEGORIA);

			clubeLibertyByCategoria = new ArrayList<Object>();

			List<Object> clubeLiberty = getClubeLiberty();
			for(int i=0; i < clubeLiberty.size(); i++) {
				Map<String, Object> campo = (Map<String, Object>) clubeLiberty.get(i);
				String descCategoria = (String)campo.get("Servico");

				if (descCategoria.equals(categoria)) {
					clubeLibertyByCategoria.add(clubeLiberty.get(i));
				}
			}

			lvwList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

					Map<String, Object> itemSel = (Map<String, Object>)lvwList.getAdapter().getItem(position);

					Bundle params = new Bundle();

					params.putSerializable(Constants.LM_EXTRA_ITEM, (Serializable) itemSel);

					Intent it = new Intent(ClubeLibertyLocaisActivity.this, ClubeLibertyLocaisDetalheActivity.class);
					it.putExtras(params);
					startActivity(it);
				}
			});

			loadLocais();

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_clube_liberty_locais, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
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

	private void loadLocais() {
		try {

			progress = new ProgressDialog(ClubeLibertyLocaisActivity.this);
			progress.setMessage(getString(R.string.aguarde));
			progress.show();

			new  Thread() {
				@Override
				public void run() {
					try {
						carregaImagens();
						concluido();
					} catch (Exception e) {
						Util.showException(null, e);
					}

				}
			}.start();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@SuppressWarnings("unchecked")
	private void carregaImagens() {
		try {

			for(int i=0; i < clubeLibertyByCategoria.size(); i++) {

				Map<String, Object> campo = (Map<String, Object>) clubeLibertyByCategoria.get(i);

				if (!campo.containsKey("ImageBmp")) {
					String urlImagem = (String)campo.get("Logo");
					urlImagem = urlImagem.replace(Constants.LMUrlInterna, Constants.LMUrlExterna);
					urlImagem = urlImagem.replace(Constants.LMUrlInterna2, Constants.LMUrlExterna);

					Bitmap bmp = null;
					try {
						bmp = Util.getBitmapByURL(urlImagem);
					} catch (Exception e) {

					}

					if (bmp != null) {
						campo.put("ImageBmp", bmp);
					}
				}
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void concluido() {
		try {
			handler.post(new Runnable() {
				@Override
				public void run() {
					progress.dismiss();
					carregaList();
				}
			});
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void carregaList() {
		ClubeLibertyLocaisAdapter adapter = new ClubeLibertyLocaisAdapter(this, clubeLibertyByCategoria);
		lvwList.setAdapter(adapter);		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("Google Analytics: ", "Clube Liberty: Categoria");
		mTracker.setScreenName("Clube Liberty: Categoria");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CustomApplication.activityPaused();
	}
}
