/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.RadioButtonAdapter;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.common.util.FieldUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.model.DadosLoginSegurado;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

/**
 * Provides the view for inputing AutoPolicyNumber when the PolicyNumber was selected.
 *
 * @author Evandro
 */
public class ComunicacaoAcidenteApolicesActivity extends LibertyMobileApp implements DialogInterface.OnClickListener
{
	private ListView radioGroupApolices;

	private String currentlySelectedPolicy;

	private EditText autoPolicy;

	private boolean  wantSave;

	private List<String> listNumApolices;

	boolean isInSubmittedClaim;

	@Override
	public void onCreate(Bundle savedInstanceState){

		try {
			super.onCreate(savedInstanceState);

			if (savedInstanceState != null){
				currentlySelectedPolicy = savedInstanceState.getString("selectedValue");
			} else {
				currentlySelectedPolicy = getCurrentEvent().getClaimNumber();
			}

			isInSubmittedClaim = Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus());

			if (getDadosLoginSegurado().getLogado() && !isInSubmittedClaim) {

				// se user logado: exibe a lista de apolices
				setContentView(R.layout.activity_comunicacao_acidente_apolice);
				radioGroupApolices = (ListView) findViewById(R.id.radioGroupApolices);

				if(null != getDadosLoginSegurado() && null != getDadosLoginSegurado().getApolices() && !getDadosLoginSegurado().getApolices().isEmpty()){
					carregaMinhasApolices();
				}else{
					// << SN 11947
					if (DeviceUtils.isConnected(ComunicacaoAcidenteApolicesActivity.this)) {
						callMinhasApolices();
					} else {
						Util.showToast(ComunicacaoAcidenteApolicesActivity.this, getString(R.string.NotConnection));
					}
					// >>
				}

			}else{

				// Senão logado: permite o user editar o numero da apolice
				setContentView(R.layout.activity_comunicacao_acidente_numero_apolice);
				autoPolicy = (EditText) findViewById(R.id.et_claim_number);

				if (isInSubmittedClaim){
					// Caso já tenha submetido o comunicado de sinistro:
					FieldUtils.disableEditText(autoPolicy);
					autoPolicy.setClickable(false);

					Button btn_cancelar = (Button)findViewById(R.id.btn_cancelar);
					btn_cancelar.setVisibility(View.GONE);
					Button btn_salvar = (Button)findViewById(R.id.btn_salvar);
					btn_salvar.setVisibility(View.GONE);
				}
				else{
					// Caso não tenha submetido o comunicado de sinistro:
					autoPolicy.setClickable(true);
				}

				autoPolicy.setText(getCurrentEvent().getClaimNumber());
			}

			setUpNavigationBarTitleOnly(getString(R.string.selecao_da_apolice));

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void carregaMinhasApolices() {

		// transforma o list<object> em List<String>

		listNumApolices = new ArrayList<String>();

		for (Object objectApolice : getDadosLoginSegurado().getApolices())
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> dadosApolice = (Map<String, Object>) objectApolice;
			String apolice = (String) dadosApolice.get("NumeroApolice");
			String tipo = (String) dadosApolice.get("TipoSeguro");

			if (tipo.equals("AUTO")) {
				apolice = apolice.substring(0, 10);
				listNumApolices.add(apolice);
			}
		}

		radioGroupApolices.setAdapter(new RadioButtonAdapter(this, listNumApolices));
		radioGroupApolices.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		if (currentlySelectedPolicy != null && !currentlySelectedPolicy.equals("")) {
			int itemSel = listNumApolices.indexOf(currentlySelectedPolicy);
			if (itemSel != -1) {
				radioGroupApolices.setItemChecked(itemSel, true);
			}
		}
	}

	private void callMinhasApolices() {

		// Inst�ncia que trabalha o retorno do webservice
		WebserviceInterface meusSegurosWsInterface = new WebserviceInterface(this) {

			@Override
			public void callBackWebService(String response) {

				if(null != response) {
					try{
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
		CallWebServices.callGetMeusSegurosLiberty(this, getDadosLoginSegurado().getCpf(), "1", meusSegurosWsInterface);

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

	/**
	 * This method will return true if the required fields are populated, otherwise false
	 *
	 * @return minRequirementsMet
	 */
	private boolean checkMinReqs(){

		try {
			boolean minRequirementsMet = true;

			String missingField = null;

			if (!getDadosLoginSegurado().getLogado() && !validateAutoPolicy()){
				minRequirementsMet = false;
				missingField = getString(R.string.auto_policy_number_label);
			}

			if (!minRequirementsMet){

				// Tell the user they haven't met the minimum requirements
				displayInfoAlert(	ComunicacaoAcidenteApolicesActivity.this,
						getString(R.string.por_favor_insira),
						missingField,
						getString(R.string.btn_ok),
						ComunicacaoAcidenteApolicesActivity.this);
			}

			return minRequirementsMet;

		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * This method will return true if the currently selected values are different than the values stored in the
	 * database.
	 *
	 * @return returnVal
	 */
	private boolean isFormChanged(){

		try {
			boolean returnVal = false;
			String origType = getCurrentEvent().getClaimNumber();
			if (origType != null){

				if(getDadosLoginSegurado().getLogado()){
					if (!origType.equals(currentlySelectedPolicy)){
						returnVal = true;
					}
				}else{
					if (!origType.equals(autoPolicy.getText().toString())){
						returnVal = true;
					}
				}
			}else if (currentlySelectedPolicy != null){
				returnVal = true;
			}

			return returnVal;

		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		try {
			if (isFormChanged() && !isInSubmittedClaim) {

				if(wantSave) {
					salvaEfecha(null);
				} else{
					// Ask the user if they want to discard their changes
					displayConfirmAlert(ComunicacaoAcidenteApolicesActivity.this,
							getString(R.string.aviso),
							getString(R.string.deseja_salvar_alteracoes),
							getString(R.string.btn_ok),
							getString(R.string.btn_cancelar),
							ComunicacaoAcidenteApolicesActivity.this);
				}
			} else {
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param dialog
	 * @param which
	 * @see DialogInterface.OnClickListener#onClick(DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		try {
			// The user selected yes
			if (which == -1) {
				wantSave = true;
				salvaEfecha(null);
			} else if (which == -2) {
				// Finish the activity
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param outState
	 * @see android.app.Activity#onSaveInstanceState(Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		try {
			// Log.v(TAG, ">>> onSaveInstanceState()");

			outState.putString("selectedValue", currentlySelectedPolicy);

			super.onSaveInstanceState(outState);

			// Log.v(TAG, "<<< onSaveInstanceState()");
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}


	/**
	 * Valida o número da apólice
	 *
	 * @return true caso válido - false caso contrário
	 */
	private boolean validateAutoPolicy() {

		boolean isAutoPoliceValid = false;

		if (!ValidationUtils.isStringEmpty(autoPolicy.getText().toString())
				&& (autoPolicy.getText().toString().length() == 10)) {
			isAutoPoliceValid = true;
		}

		return isAutoPoliceValid;
	}

	/**
	 * This method will persist the selected data to the database.
	 */
	private void saveForm(){
		try {

			if(!getDadosLoginSegurado().getLogado()){
				currentlySelectedPolicy = autoPolicy.getText().toString();
			}
			else {
				int itemSel = radioGroupApolices.getCheckedItemPosition();
				currentlySelectedPolicy = listNumApolices.get(itemSel);
			}
			getCurrentEvent().setClaimNumber(currentlySelectedPolicy);

			EventHelper.update(this, getCurrentEvent());
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Quando clicka em salvar (button_bar.xml).
	 * Quando clicka em voltar e deseja salvar.
	 *
	 * @param v
	 */
	public void salvaEfecha(View v)
	{
		try {
			if (checkMinReqs())
			{
				saveForm();
				setResult(RESULT_OK);
				finish();
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
}
