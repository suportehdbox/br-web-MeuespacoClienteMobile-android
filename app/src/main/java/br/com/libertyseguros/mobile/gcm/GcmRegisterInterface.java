package br.com.libertyseguros.mobile.gcm;

import br.com.libertyseguros.mobile.common.LibertyException;

/**
 * Classe abstrata para callback do GcmRegister
 *
 * @author Evandro
 */
public abstract class GcmRegisterInterface {

	// método chamado no pos do GCMRegisterTask
	abstract public void callBackGcmRegister(String response);

	// método chamado no response do invoker
	public void callBackGcmRegisterFailWithError(LibertyException error) {
	}

}
