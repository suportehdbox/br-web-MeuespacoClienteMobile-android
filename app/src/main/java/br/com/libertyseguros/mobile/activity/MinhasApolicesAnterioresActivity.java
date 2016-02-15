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

public class MinhasApolicesAnterioresActivity extends LibertyMobileApp {

	private ListView lvwList;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_minhas_apolices_anteriores);
			Util.setTitleNavigationBar(this, R.string.title_activity_minhas_apolices_anteriores);

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

			lvwList = (ListView) findViewById(R.id.lvwMinhasApolicesAnteriores);

			lvwList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
					Map<String, Object> itemSel = (Map<String, Object>)lvwList.getAdapter().getItem(position);

					Bundle params = new Bundle();

					params.putSerializable(Constants.LM_EXTRA_ITEM, (Serializable) itemSel);

					Intent it = new Intent(MinhasApolicesAnterioresActivity.this, MinhasApolicesDetalheActivity.class);
					it.putExtras(params);
					startActivity(it);				
				}
			});
			
			// EPO: alteração caso já tenha consultado uma primeira vez guarda em memória durante a execução:
			
			if(null != getDadosLoginSegurado() && null != getDadosLoginSegurado().getApolicesAnteriores() && !getDadosLoginSegurado().getApolicesAnteriores().isEmpty()){
				carregaMinhasApolicesAnteriores();
			} else {
				// << SN 11947
				if (DeviceUtils.isConnected(MinhasApolicesAnterioresActivity.this)) {
					callMinhasApolicesAnteriores();
				} else {
					Util.showToast(MinhasApolicesAnterioresActivity.this, getString(R.string.NotConnection));
				}
				// >>
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

	private void callMinhasApolicesAnteriores() {


		// Inst�ncia que trabalha o retorno do webservice
		WebserviceInterface meusSegurosAnterioresWsInterface = new WebserviceInterface(this) {

			@Override
			public void callBackWebService(String response) {

				if(null != response) {
					
					try {
						getDadosLoginSegurado().setApolicesAnteriores(CallWebServices.retGetMeusSegurosLiberty(response));
						if (getDadosLoginSegurado().getApolicesAnteriores().size() > 0) {
							carregaMinhasApolicesAnteriores();	
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
		CallWebServices.callGetMeusSegurosLiberty(this, getDadosLoginSegurado().getCpf(), "2", meusSegurosAnterioresWsInterface);

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
							callMinhasApolicesAnteriores();
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
	
	private void carregaMinhasApolicesAnteriores() {
			
		// associa o array � listview
		MinhasApolicesAdapter adapter = new MinhasApolicesAdapter(this, getDadosLoginSegurado().getApolicesAnteriores());
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

		Log.i("Google Analytics: ", "Minhas Apólices: Anteriores");
		mTracker.setScreenName("Minhas Apólices: Anteriores");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

//	  CustomApplication.activityResumed();
	}
}
