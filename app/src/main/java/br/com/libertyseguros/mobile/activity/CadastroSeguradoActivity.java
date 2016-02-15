package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.ComponenteCadastro;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

public class CadastroSeguradoActivity extends Activity implements DialogInterface.OnClickListener {

	private ComponenteCadastro 	editApoliceCadastro; 		// APOLICE
	private ComponenteCadastro 	editCPFCadastro;			// CPF
	private ComponenteCadastro 	editEmailCadastro;			// E-MAIL
	private ComponenteCadastro 	editConfirmarEmailCadastro;	// COMFIRMA E-MAIL
	private ComponenteCadastro 	editSenhaCadastro;			// SENHA
	private ComponenteCadastro 	editConfirmarSenhaCadastro;	// CONFIRMA SENHA
	private CheckBox 		   	cbx_cadastro;				// TERMOS E CONDICOES
	private TextView 			tvw_cbx_cadastro;

	private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_cadastro_segurado);
			Util.setTitleNavigationBar(this, R.string.title_activity_cadastro_segurado);

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

//			this.editNomeClienteCadastro = (EditText)findViewById(R.id.editNomeClienteCadastro);
			this.editApoliceCadastro 	= new ComponenteCadastro(this, R.id.editApoliceCadastro, InputType.TYPE_CLASS_NUMBER, R.string.apolice, false);
			this.editCPFCadastro 		= new ComponenteCadastro(this, R.id.editCPFCadastro, InputType.TYPE_CLASS_NUMBER, R.string.cpf, false);
			this.editEmailCadastro 		= new ComponenteCadastro(this, R.id.editEmailCadastro, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, R.string.email, false);
			this.editConfirmarEmailCadastro = new ComponenteCadastro(this, R.id.editConfirmarEmailCadastro, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, R.string.confirme_o_email, false);
			this.editSenhaCadastro 		= new ComponenteCadastro(this, R.id.editSenhaCadastro, InputType.TYPE_TEXT_VARIATION_PASSWORD, R.string.senha, true);
			this.editConfirmarSenhaCadastro = new ComponenteCadastro(this, R.id.editConfirmarSenhaCadastro, InputType.TYPE_TEXT_VARIATION_PASSWORD, R.string.confirme_a_senha, true);

			this.editApoliceCadastro.setMaxLength(20);

			this.editCPFCadastro.setMaxLength(14);
//        	this.editCPFCadastro.addTextChangedListener(new CPFTextWatcher());

			//CHECKBOX TERMOS E CONDIÇ˜OES
			cbx_cadastro = (CheckBox)findViewById(R.id.cbx_cadastro);

			tvw_cbx_cadastro =(TextView)findViewById(R.id.tvw_cbx_cadastro);

			// LINK TERMOS E CONDIÇ˜OES
			Button btnPoliticaPrivacidade = (Button)findViewById(R.id.btnPoliticaPrivacidade);
			btnPoliticaPrivacidade.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Chamar o termo da Pol. Privac.:
					callPoliticaPrivacidadeActivity();
				}
			});

			// Enviar cadastro
			Button btnEnviarCadastro = (Button)findViewById(R.id.btn_registrar);
			btnEnviarCadastro.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// << SN 11947
					if (DeviceUtils.isConnected(CadastroSeguradoActivity.this)) {
						gravarCadastro();
					} else {
						Util.showToast(CadastroSeguradoActivity.this, getString(R.string.NotConnection));
					}
					// >>
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

	private boolean validateFieldsBlanks() {

		boolean isFildsOK = true;

		try {

			//APOLICE
			editApoliceCadastro.clearErro();
			if(editApoliceCadastro.getText().compareTo("") == 0)
			{
				editApoliceCadastro.showErro(getString(R.string.informe_o_numero_da_apolice));
				isFildsOK = false;
			}
			else if (editApoliceCadastro.getText().length() < 7)
			{
				editApoliceCadastro.showErro(getString(R.string.numero_invalido_de_apolice));
				isFildsOK = false;
			}

			//CPF ou CNPJ:
			editCPFCadastro.clearErro();
			if(editCPFCadastro.getText().compareTo("") == 0)
			{
				editCPFCadastro.showErro(getString(R.string.informe_o_cpf));
				isFildsOK = false;
			} else
			{
				String cpfCnpjValidar = editCPFCadastro.getText().replace(".", "");
				cpfCnpjValidar = cpfCnpjValidar.replace("-", "");
				cpfCnpjValidar = cpfCnpjValidar.replace("/", "");
				cpfCnpjValidar = cpfCnpjValidar.replaceAll("\\.", "");


				if(cpfCnpjValidar.length() == 14) {
					if (!Util.checkCnpj(cpfCnpjValidar)) {
						editCPFCadastro.showErro(getString(R.string.cnpj_invalido));
						isFildsOK = false;
					}
				} else  if (!Util.checkCPF(cpfCnpjValidar)) {
					editCPFCadastro.showErro(getString(R.string.cpf_invalido));
					isFildsOK = false;
				}
			}

			//E-MAIL
			editEmailCadastro.clearErro();
			if(editEmailCadastro.getText().compareTo("") == 0)
			{
				editEmailCadastro.showErro(getString(R.string.informe_o_email));
				isFildsOK = false;
			}
			else if (!Util.validateEmail(editEmailCadastro.getText()))
			{
				editEmailCadastro.showErro(getString(R.string.email_invalido));
				isFildsOK = false;
			}
			else  if (editEmailCadastro.getText().compareTo(editConfirmarEmailCadastro.getText()) != 0)
			{
				editEmailCadastro.showErro(getString(R.string.os_emails_informados_sao_diferentes));
				isFildsOK = false;
			}

			// COMFIRMA E-MAIL
			editConfirmarEmailCadastro.clearErro();
			if(editConfirmarEmailCadastro.getText().compareTo("") == 0)
			{
				editConfirmarSenhaCadastro.showErro(getString(R.string.informe_o_email_confirmacao));
				isFildsOK = false;
			}

			//SENHA
			editSenhaCadastro.clearErro();
			if(editSenhaCadastro.getText().compareTo("") == 0)
			{
				editSenhaCadastro.showErro(getString(R.string.informe_a_senha));
				isFildsOK = false;
			}
			else if (editSenhaCadastro.getText().length() < 8) {
				editSenhaCadastro.showErro(getString(R.string.sua_senha_deve_possuir_no_minimo_8_caracteres));
				isFildsOK = false;
			}
			else if (!validarSenha(editSenhaCadastro.getText()))
			{
				editSenhaCadastro.showErro(getString(R.string.senha_invalida));
				isFildsOK = false;
			}
			else if (editSenhaCadastro.getText().compareTo(editConfirmarSenhaCadastro.getText()) != 0)
			{
				editSenhaCadastro.showErro(getString(R.string.as_senhas_informadas_sao_diferentes));
				isFildsOK = false;
			}

			//CONFIRMA SENHA
			editConfirmarSenhaCadastro.clearErro();
			if(editConfirmarSenhaCadastro.getText().compareTo("") == 0)
			{
				editConfirmarSenhaCadastro.showErro(getString(R.string.informe_a_senha_confirmacao));
				isFildsOK = false;
			}

			if (!cbx_cadastro.isChecked()) {
				cbx_cadastro.setButtonDrawable(R.drawable.cbx_cadastro_vazio);
				cbx_cadastro.invalidate();

				tvw_cbx_cadastro.setVisibility(View.VISIBLE);
				tvw_cbx_cadastro.invalidate();

				isFildsOK = false;
			}else{
				cbx_cadastro.setButtonDrawable(R.drawable.cbx_cadastro);
				cbx_cadastro.setTextColor(getResources().getColor(R.color.nofoco));
				cbx_cadastro.invalidate();

				tvw_cbx_cadastro.setVisibility(View.GONE);
				tvw_cbx_cadastro.invalidate();
			}

			return isFildsOK;

		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	private void gravarCadastro() {
		try {
			// << SN 11947
			if (DeviceUtils.isConnected(CadastroSeguradoActivity.this)) {
				if (!validateFieldsBlanks()) return;
				callGravarCadastro();
			} else {
				Util.showToast(CadastroSeguradoActivity.this, getString(R.string.NotConnection));
			}
			// >>
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}


	private void callGravarCadastro() {
		try {
			// Inst???ncia que trabalha o retorno do webservice
			WebserviceInterface gravarCadastroWsInterface = new WebserviceInterface(this) {

				@Override
				public void callBackWebService(String response) {

					if(null != response) {
						retornoGravarCadastro(response);	

					} else {
						// caso n��o tenha response �� porque aconteceu erro
					}
				}

				@Override
				public void callWebServicesFailWithError(LibertyException error) {
					// << EPO: Exibe menssagem de erro para o usu��rio
					Util.showException(context, error);						
					// >>
				}
			};
			
			String cpfEnvio = editCPFCadastro.getText().toString().replace(".", "");
			cpfEnvio = cpfEnvio.replace("-", "");


			// Chama o ws  
			CallWebServices.callCadastrarUsuario(this,
//					editNomeClienteCadastro.getText().toString(),
					editSenhaCadastro.getText(),
					editEmailCadastro.getText(),
					cpfEnvio,
					editApoliceCadastro.getText(),
					gravarCadastroWsInterface);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void retornoGravarCadastro(String response) {
		try {
			String retorno = CallWebServices.retCadastrarUsuario(response);
			if (retorno.compareTo("") != 0) {
				Util.showAlert(this, retorno, null);
			}
			else {
				Util.showAlert(this, getString(R.string.cadastro_efetuado_com_sucesso), null);

				AlertDialog.Builder builder = new AlertDialog.Builder(CadastroSeguradoActivity.this);
				builder.setTitle(getString(R.string.aviso));
				builder.setMessage(getString(R.string.cadastro_efetuado_com_sucesso));
				builder.setCancelable(false);
				builder.setNeutralButton(getString(R.string.btn_ok), CadastroSeguradoActivity.this);
				AlertDialog alert = builder.create();
				alert.show();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private boolean validarSenha(String senha) {
		try {
			int iMatch = 0;

			//Verificando numeros
			iMatch = 0;
			String regexNumber = "[??0-9]";
			for (int iCont = 0; iCont < senha.length(); iCont++) {
				if (senha.substring(iCont, iCont + 1).matches(regexNumber)) {
					iMatch++;
				}
			}
			if (iMatch == 0) return false;

			//Verificando caracteres minusculos
			int iMatchLower = 0;
			String regexCaracteresLower = "[??a-z]";
			for (int iCont = 0; iCont < senha.length(); iCont++) {
				if (senha.substring(iCont, iCont + 1).matches(regexCaracteresLower)) {
					iMatchLower++;
				}
			}
//			if (iMatch == 0) return false;

			//Verificando caracteres maiusculos
			String regexCaracteresUpper = "[??A-Z]";
			int iMatchUpper = 0;
			for (int iCont = 0; iCont < senha.length(); iCont++) {
				if (senha.substring(iCont, iCont + 1).matches(regexCaracteresUpper)) {
					iMatchUpper++;
				}
			}
//			if (iMatch == 0) return false;

			// Terá pelo menos 1 letra Maiusculas ou Minusculas
			if (iMatchLower == 0 && iMatchUpper == 0) return false;

			//Verificando caracteres especiais
			String regexCaracteresSpecial = "[??!@#$%&*()_]";
			iMatch = 0;
			for (int iCont = 0; iCont < senha.length(); iCont++) {
				if (senha.substring(iCont, iCont + 1).matches(regexCaracteresSpecial)) {
					iMatch++;
				}
			}
			if (iMatch == 0) return false;

			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		try {
			// Finish the activity
			finish();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
    /**
     * A????o do bot??o Voltar da barra de navega????o (navigation_bar.xml).
     * @param v
     */
    public void voltar(View v)
    {
    	onBackPressed();
    }


	private void callPoliticaPrivacidadeActivity() {
		try {
			Intent it = new Intent(this, PoliticaPrivacidadeActivity.class);
			startActivity(it);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
//		EasyTracker.getInstance(this).activityStart(this);	//Medir o tempo dentro da tela
	}
	
	@Override
	public void onStop() {
		super.onStop();
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

		Log.i("Google Analytics: ", "Cadastro Segurado");
		mTracker.setScreenName("Cadastro Segurado");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

	  CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  super.onPause();

	  CustomApplication.activityPaused();
	}
}
