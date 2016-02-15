package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.textwatcher.CPFTextWatcher;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

public class LoginRecuperacaoActivity extends Activity {

	private EditText editUsuarioLogin;
	private EditText editSenhaLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_login_recuperacao);
			Util.setTitleNavigationBar(this, R.string.title_activity_login_recuperacao);

			editUsuarioLogin = (EditText)findViewById(R.id.editUsuarioLogin);
			editSenhaLogin = (EditText)findViewById(R.id.editSenhaLogin);

			Intent it = getIntent();
			if (it == null) return;		

			// Tela de endere???o
			Button btnLogin = (Button)findViewById(R.id.btnLogin);
			btnLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callRecuperar();
				}
			});

			editUsuarioLogin.addTextChangedListener(new CPFTextWatcher());
			Util.setMaxTextEdit(editUsuarioLogin, 14);
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_login_recuperacao, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	private void callRecuperar() {
		try {
			
			if (editUsuarioLogin.getText().toString().compareTo("") == 0 || editSenhaLogin.getText().toString().compareTo("") == 0) 
			{
				Util.showAlert(this, getString(R.string.por_favor_informe_o_cpf_e_email), this.getTitle().toString());
				return;
			}
			
			String cpfValido = editUsuarioLogin.getText().toString().replace(".", "");
			cpfValido = cpfValido.replace("-", "");
			
			if(!Util.checkCPF(cpfValido)){
				Util.showAlert(this, getString(R.string.cpf_invalido), this.getTitle().toString());
				return;
			}

			// Inst???ncia que trabalha o retorno do webservice
			WebserviceInterface esqueciMinhaSenhaSeguradoWsInterface = new WebserviceInterface(this) {
				@Override
				public void callBackWebService(String response) {

					if(null != response) {
						finalizaRecuperacaoSegurado(response);
					}
				}

				@Override
				public void callWebServicesFailWithError(LibertyException error) {
					// EPO: Exibe menssagem de erro para o usuário
					Util.showException(context, error);						
				}
			};
			
			// Chama o ws  
			CallWebServices.callEsqueciMinhaSenhaSegurado(this, editSenhaLogin.getText().toString(), cpfValido, esqueciMinhaSenhaSeguradoWsInterface);	

		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	private void finalizaRecuperacaoSegurado(String response) {
		try {
			String sRetorno = CallWebServices.retEsqueciMinhaSenhaSegurado(response);
			if (!sRetorno.equals("")) {
				Util.showAlert(this, sRetorno, this.getTitle().toString());
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * A??????o do bot???o Voltar da barra de navega??????o (navigation_bar.xml)
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
	  CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  super.onPause();
	  CustomApplication.activityPaused();
	}
}
