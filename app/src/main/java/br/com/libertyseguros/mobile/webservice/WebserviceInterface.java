package br.com.libertyseguros.mobile.webservice;

import android.content.Context;

import br.com.libertyseguros.mobile.common.LibertyException;

/**
 * Classe Pai para inst�ncias de tratamentos do retorno dos m�todos web services 
 * @author Evandro
 */
public abstract class WebserviceInterface {
	
	// Refer�ncia do context para atualizar objetos visuais 
	protected Context context;

	/**
	 * Construtor
	 * @param context
	 */
	public WebserviceInterface(Context context) {
		super();
		this.context = context;
	}

	// método chamado no response do invoker
	abstract public void callBackWebService(String response);

	// método chamado no response do invoker
	public void callWebServicesFailWithError(LibertyException error) {
	}
}
