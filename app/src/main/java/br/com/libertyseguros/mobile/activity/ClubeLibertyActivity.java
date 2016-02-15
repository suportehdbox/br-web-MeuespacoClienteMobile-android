//package br.com.libertyseguros.mobile.activity;
//
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
//import br.com.libertyseguros.mobile.R;
//import br.com.libertyseguros.mobile.adapter.ClubeLibertyAdapter;
//import br.com.libertyseguros.mobile.common.LibertyException;
//import br.com.libertyseguros.mobile.common.SortComparatorString;
//import br.com.libertyseguros.mobile.common.Util;
//import br.com.libertyseguros.mobile.common.util.DeviceUtils;
//import br.com.libertyseguros.mobile.constants.Constants;
//import br.com.libertyseguros.mobile.webservice.CallWebServices;
//import br.com.libertyseguros.mobile.webservice.WebserviceInterface;
//
//public class ClubeLibertyActivity extends LibertyMobileApp {
//
//	private ListView lvwList;
//	private ArrayList<String> categorias;
//
//	private Tracker mTracker;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		try {
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.activity_clube_liberty);
//			Util.setTitleNavigationBar(this, R.string.title_activity_clube_liberty);
//
////			GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_clube_liberty));
//			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
//			mTracker = analyticsApplication.getDefaultTracker(getApplication());
//
//			lvwList = (ListView) findViewById(R.id.lvwClubeLiberty);
//
//			lvwList.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//					String categoria = (String)lvwList.getAdapter().getItem(position);
//
//					Bundle params = new Bundle();
//					params.putString(Constants.LM_EXTRA_CATEGORIA, categoria);
//
//					Intent it = new Intent(ClubeLibertyActivity.this, ClubeLibertyLocaisActivity.class);
//					it.putExtras(params);
//					startActivity(it);
//
//				}
//
//			});
//
//			/*
//			 * Caso seja a primeira consulta ao clubeliberty: chama o web service.
//			 */
//			if(null != getClubeLiberty() && getClubeLiberty().size() > 0)
//			{
//				carregaCategorias();
//			}
//			else
//			{
//				// << SN 11947
//				if (DeviceUtils.isConnected(ClubeLibertyActivity.this)) {
//					callClubeLiberty();
//				} else {
//					Util.showToast(ClubeLibertyActivity.this, getString(R.string.NotConnection));
//				}
//				// >>
//			}
//		}
//		catch (Exception e)
//		{
//			Util.showException(this, e);
//		}
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		try {
//			// Inflate the menu; this adds items to the action bar if it is present.
//			//getMenuInflater().inflate(R.menu.activity_clube_liberty, menu);
//			return true;
//		} catch (Exception e) {
//			Util.showException(this, e);
//			return false;
//		}
//	}
//
//	private void callClubeLiberty() {
//
//		try {
//			// Inst��ncia que trabalha o retorno do webservice
//			WebserviceInterface clubeLibertyWsInterface = new WebserviceInterface(this) {
//
//
//				@Override
//				public void callBackWebService(String response) {
//
//					if(null != response) {
//						carregaClubeLiberty(response);
//
//					}
//				}
//
//				@Override
//				public void callWebServicesFailWithError(LibertyException error) {
//					// EPO: Exibe menssagem de erro para o usuário
//					Util.showException(context, error);
//				}
//			};
//
//			// Chama o ws
//			CallWebServices.callGetClubeLiberty(this, Constants.LM_WS_USUARIO, clubeLibertyWsInterface);
//		} catch (Exception e) {
//			Util.showException(this, e);
//		}
//
//	}
//
//	private void carregaClubeLiberty(String response) {
//		try {
//			setClubeLiberty(CallWebServices.retGetClubeLiberty(response));
//
//			if (getClubeLiberty().size() > 0) {
//				carregaCategorias();
//			}
//		} catch (Exception e) {
//			Util.showException(this, e);
//		}
//	}
//
//	private void carregaCategorias() {
//		//Carregando outro Array somente com as categorias do Clube Liberty
//		categorias = new ArrayList<String>();
//
//
//		List<Object> clubeLiberty = getClubeLiberty();
//		for(int i=0; i < clubeLiberty.size(); i++) {
//			Map<String, Object> campo = (Map<String, Object>) clubeLiberty.get(i);
//			String descCategoria = (String)campo.get("Servico");
//
//			if (!categorias.contains(descCategoria)) {
//				categorias.add(descCategoria);
//			}
//		}
//
//		SortComparatorString compare = new SortComparatorString();
//		java.util.Collections.sort(categorias, compare);
//
//
//		// associa o array �� listview
//		ClubeLibertyAdapter adapter = new ClubeLibertyAdapter(this, categorias);
//		lvwList.setAdapter(adapter);
//	}
//
//	/**
//	 * Ação do botão Voltar da barra de navegação (navigation_bar.xml)
//	 * @param v
//	 */
//	public void voltar(View v)
//	{
//		try {
//			onBackPressed();
//		} catch (Exception e) {
//			Util.showException(this, e);
//		}
//	}
//
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		lvwList.invalidate();
//	}
//
//
//	@Override
//	public void onStart() {
//		super.onStart();
////		ANDROID STUDIO
////		EasyTracker.getInstance(this).activityStart(this);	//Medir o tempo dentro da tela
//	}
//
//	@Override
//	public void onStop() {
//		super.onStop();
////		ANDROID STUDIO
////		EasyTracker.getInstance(this).activityStop(this);	//Para o tempo dentro da tela
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		Util.callGB();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//
//		Log.i("Google Analytics: ", "Clube Liberty de Vantagens");
//		mTracker.setScreenName("Clube Liberty de Vantagens");
//		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//
////	  CustomApplication.activityResumed();
//	}
//
//}
