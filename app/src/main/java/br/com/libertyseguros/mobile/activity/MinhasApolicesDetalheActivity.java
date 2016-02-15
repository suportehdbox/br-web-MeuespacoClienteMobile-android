package br.com.libertyseguros.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.DadosLoginSegurado;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

public class MinhasApolicesDetalheActivity extends LibertyMobileApp {

	private Map<String, Object> itemSel;
	private DadosLoginSegurado dadosLoginSegurado;

	private Tracker mTracker;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_minhas_apolices_detalhe);
			Util.setTitleNavigationBar(this, R.string.title_activity_minhas_apolices_detalhe);

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

			dadosLoginSegurado = getDadosLoginSegurado();

			Intent it = getIntent();
			if (it == null) return;

			Bundle parms = it.getExtras();
			itemSel = (Map<String, Object>) parms.getSerializable(Constants.LM_EXTRA_ITEM);

			Button btnItemParcelasMinhasApolices = (Button)findViewById(R.id.btnItemParcelasMinhasApolices);
			btnItemParcelasMinhasApolices.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle params = new Bundle();

					params.putSerializable(Constants.LM_EXTRA_ITEM, (Serializable) itemSel);

					Intent it = new Intent(MinhasApolicesDetalheActivity.this, MinhasApolicesParcelasActivity.class);
					it.putExtras(params);
					startActivity(it);	
				}
			});

			if (DeviceUtils.isConnected(MinhasApolicesDetalheActivity.this)) {
				callMinhasCoberturas();
			} else {
				Util.showToast(MinhasApolicesDetalheActivity.this, getString(R.string.NotConnection));
			}
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_minhas_apolices_detalhe, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	private void callMinhasCoberturas() {

		try {
			// Inst�ncia que trabalha o retorno do webservice
			WebserviceInterface clubeLibertyWsInterface = new WebserviceInterface(this) {

				@Override
				public void callBackWebService(String response) {

					if(null != response) {
						
						try {
							List<Object> coberturas = CallWebServices.retGetCoberturasApolice(response);
							carregaApolice(coberturas);
							
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
			CallWebServices.callGetCoberturasApolice(this,dadosLoginSegurado.getCpf(),
													(String)itemSel.get("NumeroContrato"), 
													(String)itemSel.get("NumeroEmissao"),
													(String)itemSel.get("CodigoItem"),
													(String)itemSel.get("CodigoCIA"),
													clubeLibertyWsInterface);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void carregaApolice(List<Object> coberturas) {
		
		try {

			String strCoberturas = "";
			for (Object obj: coberturas) {
				Map<String, Object> objItem = (Map<String, Object>)obj;
				strCoberturas += (strCoberturas.compareTo("") == 0 ? "" : ", ");
				strCoberturas += (String)objItem.get("Nome");
			}

			String numeroApolice = (String)itemSel.get("NumeroApolice");
			String vigencia = (String)itemSel.get("Vigencia");

			boolean parcelaPendente = false;
			List<Object> parcelas = (List<Object>)itemSel.get("Parcelas");
			for (Object obj: parcelas) {
				Map<String, Object> objItem = (Map<String, Object>)obj;
				boolean quitada = Boolean.parseBoolean((String)objItem.get("Quitada"));
				if (!quitada) { 
					parcelaPendente = true;
					break;
				}
			}

			//Atualizando controles		
			TextView textItemNroApoliceMinhasApolices = (TextView)findViewById(R.id.textItemNroApoliceMinhasApolices);
			TextView textItemVigenciaMinhasApolices = (TextView)findViewById(R.id.textItemVigenciaMinhasApolices);
			TextView textItemCoberturasMinhasApolices = (TextView)findViewById(R.id.textItemCoberturasMinhasApolices);
			TextView textItemStatusPagamentoMinhasApolices = (TextView)findViewById(R.id.textItemStatusPagamentoMinhasApolices);
			TextView textItemStatusParcelaMinhasApolices = (TextView)findViewById(R.id.textItemStatusParcelaMinhasApolices);

			textItemNroApoliceMinhasApolices.setText(numeroApolice);
			textItemVigenciaMinhasApolices.setText(vigencia);
			textItemCoberturasMinhasApolices.setText(strCoberturas);

			if (!parcelaPendente) {
				textItemStatusPagamentoMinhasApolices.setText(getString(R.string.btn_ok));
				textItemStatusParcelaMinhasApolices.setText(getString(R.string.btn_ok));
			}
			else {
				textItemStatusPagamentoMinhasApolices.setText(getString(R.string.pendente));
				textItemStatusParcelaMinhasApolices.setText(getString(R.string.pendente));
			}
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
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
							callMinhasCoberturas();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("Google Analytics: ", "Minhas Apólices: Detalhe");
		mTracker.setScreenName("Minhas Apólices: Detalhe");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

//	  CustomApplication.activityResumed();
	}

}
