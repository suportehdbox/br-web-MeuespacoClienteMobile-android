 package br.com.libertyseguros.mobile.webservice;

/**
 * Emun para endereco dos servidores 
 * @author Evandro
 */
public enum AddressServer {
	
	/** Desenvolvimento */ 
	ADDRESS_SERVER_SEGURADO_DESENV("http://10.136.187.93/Liberty.Portal.Segurado.ControllerUI/ControllerUI.asmx"),
	
	/** Aceite visibilidade Interna*/
	ADDRESS_SERVER_SEGURADO_ACEITE_INTERNO("http://10.158.138.21:10443/WSPortalSegurado"),      // http://VWKIUBR-SPAAP01:9442/WSPortalSegurado

	/** Aceite visibilidade Externa*/
	ADDRESS_SERVER_SEGURADO_ACEITE_EXTERNO("https://act-dmz.libertyseguros.com.br/LibertyPortalSegurado"), 
	
	/** Pré-produção */ 

	
	/** Produção  visibilidade Externa */ 
	ADDRESS_SERVER_SEGURADO_PRODUCAO_EXTERNO("https://meuespaco.libertyseguros.com.br/WSPortalSegurado"), //200.143.36.133
	
	/** Produção visibilidade Interna*/ 
	ADDRESS_SERVER_SEGURADO_PRODUCAO_INTERNO("http://novomeuespaco.libertyseguros.com.br/WSPortalSegurado"); // br-lihi-spi 440
	String url;
	
	AddressServer(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
}
