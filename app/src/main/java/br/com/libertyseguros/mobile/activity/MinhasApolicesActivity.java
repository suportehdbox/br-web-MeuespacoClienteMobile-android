package br.com.libertyseguros.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.Serializable;
import java.util.Map;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.MinhasApolicesAdapter;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.DadosLoginSegurado;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

public class MinhasApolicesActivity extends LibertyMobileApp {

	private ListView lvwList;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_minhas_apolices);
			Util.setTitleNavigationBar(this, R.string.title_activity_minhas_apolices);

//		//<< ANDROID STUDIO
//		GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_minhas_apolices));
			// Obtain the shared Tracker instance.
//			AnalyticsApplication application = (AnalyticsApplication) getApplication();
//			mTracker = application.getDefaultTracker();
			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());
//		//>>

			lvwList = (ListView) findViewById(R.id.lvwMinhasApolices);

			lvwList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
					@SuppressWarnings("unchecked")
					Map<String, Object> itemSel = (Map<String, Object>)lvwList.getAdapter().getItem(position);

					Bundle params = new Bundle();

					params.putSerializable(Constants.LM_EXTRA_ITEM, (Serializable) itemSel);

					Intent it = new Intent(MinhasApolicesActivity.this, MinhasApolicesDetalheActivity.class);
					it.putExtras(params);
					startActivity(it);				
				}
			});
			
			// EPO: alteração caso já tenha consultado uma primeira vez guarda em memória durante a execução:
			
			if(null != getDadosLoginSegurado() && null != getDadosLoginSegurado().getApolices() && !getDadosLoginSegurado().getApolices().isEmpty()){
				carregaMinhasApolices();
			} else {
				if (DeviceUtils.isConnected(MinhasApolicesActivity.this)) {
					callMinhasApolices();
				} else {
					Util.showToast(MinhasApolicesActivity.this, getString(R.string.NotConnection));
				}
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_minhas_apolices, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	private void callMinhasApolices() {

		// Inst�ncia que trabalha o retorno do webservice
		WebserviceInterface clubeLibertyWsInterface = new WebserviceInterface(this) {

			@Override
			public void callBackWebService(String response) {

				if(null != response) {
					
					try {
						getDadosLoginSegurado().setApolices(CallWebServices.retGetMeusSegurosLiberty(response));
					
						if (getDadosLoginSegurado().getApolices().size() > 0) {
							carregaMinhasApolices();	
						}
					} catch (LibertyException e) {
			            /*
			             Caso retGetMeusSegurosLiberty falhe por 'SESSAO_INVALIDA' e escolheu manter logado
			             Chama o loginToken em background e depois chama "callGetMeusSegurosLiberty" novamente
			             */
			            if (LibertyExceptionEnum.LM_ERRO_SESSAO_INVALIDA.equals(e.getLibertyExceptionEnum())) {
			            	
			            	if(ValidationUtils.isStringEmpty(getDadosLoginSegurado().getTokenAutenticacao())){			            		
			            		// Se não escolheu manter logado quando a sessão expira deve logar novamente
			            		Util.showAlert(context, "Por favor efetue o login novamente", "Sessão expirada!");
			            	} else {
			            		callLogonSeguradoToken();			            		
			            	}
			            }
			            
					} catch (Exception e) {
						Util.showException(context, e);
					}
				}
			}

			@Override
			public void callWebServicesFailWithError(LibertyException error) {
				// EPO: Exibe menssagem de erro para o usuário
				Util.showException(context, error);						
			}
		};

		// Chama o ws  
		CallWebServices.callGetMeusSegurosLiberty(this, getDadosLoginSegurado().getCpf(), "1", clubeLibertyWsInterface);
	}
	
	@Override
	protected void callLogonSeguradoToken() {
			
		try{
			// Instancia que trabalha o retorno do webservice
			WebserviceInterface loginTokenWsInterface = new WebserviceInterface(this) {
				@Override
				public void callBackWebService(String response) {
	
					if(null != response) {
						DadosLoginSegurado dadosLoginSegurado = CallWebServices.retLogonSeguradoToken(response);
						if (null != dadosLoginSegurado && !ValidationUtils.isStringEmpty(dadosLoginSegurado.getTokenAutenticacao())) {
							// atualiza dados do segurado
							dadosLoginSegurado.setLogado(true);
							dadosLoginSegurado.setApolices(getDadosLoginSegurado().getApolices());
							dadosLoginSegurado.setApolicesAnteriores(getDadosLoginSegurado().getApolicesAnteriores());
							setDadosLoginSegurado(dadosLoginSegurado);
							
							// chama meus seguros novamente
							callMinhasApolices();
						}
					}
				}
	
				@Override
				public void callWebServicesFailWithError(LibertyException error) {
					// EPO: Exibe menssagem de erro para o usuário
					Util.showException(context, error);						
				}
			};
			
			if(!ValidationUtils.isStringEmpty(getDadosLoginSegurado().getTokenAutenticacao())){
	        	// Chama o ws  
				CallWebServices.callLogonSeguradoToken(this, getDadosLoginSegurado().getTokenAutenticacao(), getDadosLoginSegurado().getCpf(), loginTokenWsInterface);
	        }
			

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void carregaMinhasApolices() {
			
		// associa o array � listview
		MinhasApolicesAdapter adapter = new MinhasApolicesAdapter(this, getDadosLoginSegurado().getApolices());
		lvwList.setAdapter(adapter);
	}
	
	/**
	 * Método que chama a activity de Apolices Anteriores
	 * 
	 * @param v
	 */
	public void callApolicesAnteriores(View v)
	{
		Intent it = new Intent(MinhasApolicesActivity.this, MinhasApolicesAnterioresActivity.class);
		startActivity(it);	
	}

	@Override
	protected void onResume() {
		super.onResume();

		//<< ANDROID STUDIO
		Log.i("Google Analytics: ", getString(R.string.title_activity_minhas_apolices));
		mTracker.setScreenName(getString(R.string.title_activity_minhas_apolices));
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		//>>
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
}
