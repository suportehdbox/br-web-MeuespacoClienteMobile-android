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
import android.widget.ToggleButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.model.DadosLoginSegurado;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

public class LoginActivity extends LibertyMobileApp {

	private EditText editUsuarioLogin;
	private EditText editSenhaLogin;
	private ToggleButton toggleManterlogadoLogin;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_login);
			Util.setTitleNavigationBar(this, R.string.title_activity_login);

//			GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_login));
			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());
			
			editUsuarioLogin = (EditText)findViewById(R.id.editUsuarioLogin);
			editSenhaLogin = (EditText)findViewById(R.id.editSenhaLogin);
			
			toggleManterlogadoLogin = (ToggleButton) findViewById(R.id.toggleManterlogadoLogin);
			toggleManterlogadoLogin.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                 if (toggleManterlogadoLogin.isChecked()) {
	                	 showAlertManterLogado();
	                 } 
	            }
	        });

			Button btnLogin = (Button)findViewById(R.id.btnLogin);
			btnLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Utility.isConnected(LoginActivity.this)) {
						callLogin();
					} else {
						Util.showToast(LoginActivity.this, getString(R.string.NotConnection));
					}
				}
			});

			Button btnEsqueciSenha = (Button)findViewById(R.id.btnEsqueciSenha);
			btnEsqueciSenha.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Utility.isConnected(LoginActivity.this)) {
						callEsqueciSenha();
					} else {
						Util.showToast(LoginActivity.this, getString(R.string.NotConnection));
					}
				}
			});
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	private void showAlertManterLogado(){
		Util.showAlert(this, "Ao manter o aplicativo logado não há necessidade de informar login e senha a cada acesso. Contudo, caso terceiros tenham acesso ao seu celular, dados pessoais poderão ser visualizados.", "Login");
	}

	private void callLogin() {
		try {
			if (editUsuarioLogin.getText().toString().compareTo("") == 0 || editSenhaLogin.getText().toString().compareTo("") == 0) {
				Util.showAlert(this, "Por favor informe o login e senha", "Login");
				return;
			}
			// Inst???ncia que trabalha o retorno do webservice
			WebserviceInterface loginWsInterface = new WebserviceInterface(this) {
				@Override
				public void callBackWebService(String response) {

					if(null != response) {

						DadosLoginSegurado dadosLoginSegurado = CallWebServices.retLogonSegurado(response);
						if (null != dadosLoginSegurado) {
							
							getDadosLoginSegurado().setLogado(true);
							credenciaisValido();

						} else {
							credenciaisInvalido();				
						}
					}
				}

				@Override
				public void callWebServicesFailWithError(LibertyException error) {
					// EPO: Exibe menssagem de erro para o usuário
					Util.showException(context, error);						
				}
			};

			boolean manterLogado = toggleManterlogadoLogin.isChecked();
			// Chama o ws  
			CallWebServices.callLogonSegurado(this, editUsuarioLogin.getText().toString(), editSenhaLogin.getText().toString(), manterLogado, loginWsInterface);
			
		}catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void callEsqueciSenha() {
		try {
			Intent it = new Intent(this, LoginRecuperacaoActivity.class);
			startActivity(it);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void credenciaisInvalido() {
		try {
			Util.showAlert(this, getString(R.string.login_ou_senha_invalidos), getString(R.string.title_activity_login));
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void credenciaisValido() {
		try {
			setResult(Activity.RESULT_OK);
			finish();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_login, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	

	/**
	 * A????o do bot???o Voltar da barra de navega????o (navigation_bar.xml)
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

		Log.i("Google Analytics: ", "Login");
		mTracker.setScreenName("Login");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

	}

}
