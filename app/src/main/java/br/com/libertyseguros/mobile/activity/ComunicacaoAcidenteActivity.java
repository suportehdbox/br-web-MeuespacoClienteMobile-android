package br.com.libertyseguros.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.SectionAdapter;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.model.Event;

//ANDROID STUDIO
//import com.google.analytics.tracking.android.EasyTracker;
//import br.com.libertyseguros.mobile.common.GoogleAnalyticsManager;

/**
 * @author Evandro
 */
public class ComunicacaoAcidenteActivity extends LibertyMobileApp {
	
	private ListView 		listViewMeusSinistros;
	private List<String>	listItensMeusSinistros;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		try {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_comunicacao_acidente);

			// Set up nav bar with title only
			setUpNavigationBarTitleOnly(getString(R.string.title_activity_comunicacao_acidente));

//			//<< ANDROID STUDIO
//			GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_comunicacao_acidente));
			// Obtain the shared Tracker instance.
//			AnalyticsApplication application = (AnalyticsApplication) getApplication();
//			mTracker = analyticsApplication.getDefaultTracker();
			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());
//		//>>


			ListView		listViewNovoSinistro 		= (ListView) findViewById(R.id.list_novo_sinistro);
			SectionAdapter sectionAdapterNovoSinistro 	= new SectionAdapter(this);

			// << Prepara os itens para exibição pelo listView:

			// Novo Sinistro
			String 					sectionNovoSinistro		= getString(R.string.abrir_novo_sinistro);
			List<String> 			listItensNovoSinistro	= new ArrayList<String>();
			listItensNovoSinistro.add(getString(R.string.automovel));
			//		listItensNovoSinistro.add(getString(R.string.title_residencia));
			ArrayAdapter<String>	adapterNovoSinistro = new ArrayAdapter<String>(this, R.layout.adapter_item_img, listItensNovoSinistro);

			sectionAdapterNovoSinistro.addSection(sectionNovoSinistro, adapterNovoSinistro);

			listViewNovoSinistro.setAdapter(sectionAdapterNovoSinistro);
			// >>

			// << Eventos de click's do listView (novo Sinistro)
			listViewNovoSinistro.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {

					switch (position) {
						case 1:
							callNovoSinistroActivity(Constants.LOB_AUTO);
							break;
						case 2:
							// callNovoSinistroActivity(Constants.LOB_HOME);
							break;
					}
				}
			});
			// >>


			listViewMeusSinistros = (ListView) findViewById(R.id.list_meus_sinistros);
			SectionAdapter sectionAdapterMeusSinistros = new SectionAdapter(this);

			// << Prepara os itens para exibi�����o pelo listView:

			// Meus Sinistros
			String 	sectionMeusSinistros = getString(R.string.claims_title);
			listItensMeusSinistros = new ArrayList<String>();

			// Pega a qtd. de sinistros
			ArrayList<Event> events = EventHelper.getAllForList(getApplicationContext());
			int qtdSinistros = events.size();

			StringBuilder meusSinistros = new StringBuilder();
			meusSinistros.append(getString(R.string.automovel));

			if(qtdSinistros > 0)
				meusSinistros.append("   ").append(qtdSinistros);

			listItensMeusSinistros.add(meusSinistros.toString());

			//		listItensMeusSinistros.add(getString(R.string.title_residencia));

			ArrayAdapter<String>	adapterMeusSinistros = new ArrayAdapter<String>(this, R.layout.adapter_item, listItensMeusSinistros);

			sectionAdapterMeusSinistros.addSection(sectionMeusSinistros, adapterMeusSinistros);

			listViewMeusSinistros.setAdapter(sectionAdapterMeusSinistros);
			// >>

			// << Eventos de click's do listView
			listViewMeusSinistros.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long duration) {

					switch (position) 
					{
					case 1:
						callActivity(ClaimListActivity.class, new Bundle());
						break;
					case 2:
//						params.putString(LMTipoOperacao.class.getName(), LMTipoOperacao.LMTipoOperacaoRead.toString());
//						callActivity(ComunicacaoAcidenteMeusSinistrosActivity.class, params);
						break;
					}
				}
			});
			// >>
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void callNovoSinistroActivity(String lineOfBusiness) {

		try {
			Bundle params = new Bundle();

			// Create an Event with the current line of business and status of draft
			Event event = new Event();
			event.setEventType(lineOfBusiness);

			event.setEventStatus(Constants.EVENT_STATUS_DRAFT);

			// TODO N���o tem user logado!!
			//        <<
			//        // Get the current user info to prefill
			//        User user = UserHelper.getCurrent(getApplicationContext());
			//        Contact contact = user.getContact();
			//        if (contact == null) {
			//            contact = new Contact();
			//        }
			//
			//        if (Constants.LOB_AUTO.equals(lineOfBusiness)) {
			//            event.setPolicyNumber(user.getAutoPolicy().getPolicyNumber());
			//        }
			//        else {
			//            event.setPolicyNumber(user.getHomePolicy().getPolicyNumber());
			//        }
			//        >>

			// Store the event in the database
			Long id = EventHelper.insert(getApplicationContext(), event);
			event.setId(id);

			// Set the event as the current working event
			setCurrentEvent(event);

			params.putBoolean(Constants.EXTRA_NAME_INITIAL_VIEW, true);

			callActivity(ComunicacaoAcidenteDetalheActivity.class, params);
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Respons��vel por chamar os activitys
	 * @param activity
	 * @param params 
	 */
	private void callActivity(Class<?> activity, Bundle params) {
		try {
			Intent it;
			it = new Intent(this, activity);    	
			it.putExtras(params);
			startActivityForResult(it, 2);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_comunicacao_acidente, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	
	@Override
	protected void onResume() {
		
		try {
			super.onResume();

			//<< ANDROID STUDIO
			Log.i("Google Analytics: ", getString(R.string.title_activity_comunicacao_acidente));
			mTracker.setScreenName(getString(R.string.title_activity_comunicacao_acidente));
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
			//>>

			// Pega a qtd. de sinistros
			ArrayList<Event> events = EventHelper.getAllForList(getApplicationContext());
			int qtdSinistros = events.size();
			
			StringBuilder meusSinistros = new StringBuilder();
			meusSinistros.append(getString(R.string.automovel));

			if(qtdSinistros > 0)
				meusSinistros.append("   ").append(qtdSinistros);

			listItensMeusSinistros.set(0, meusSinistros.toString());
			
			listViewMeusSinistros.invalidateViews();
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
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
}
