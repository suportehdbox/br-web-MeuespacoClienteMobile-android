package br.com.libertyseguros.mobile.webservice;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.libertyseguros.mobile.common.JsonHelper;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.DadosLoginSegurado;
//import br.com.MondialAssistance.DirectAssist.Util.Client;

public class CallWebServices {
	
	// vari??vel para o retorno
//	private final WeakReference<ListView> reference;
	
	
	public CallWebServices() {
		super();
//		this.reference = new WeakReference<ListView>(listView);
	}

	//public static Header[] headerListHttp;
	public static String userErrorMessagesMsg;
	
	private static String trace = "true";

	public static String aspnetsession;
	
	public static AddressServer getAddressServerSegurado()
	{
		AddressServer addressServerSegurado = null;
		
		switch (Constants.execucao)
		{
		case LMTipoExecucaoDesenv: 
			addressServerSegurado = AddressServer.ADDRESS_SERVER_SEGURADO_DESENV;
			break;
		case LMTipoExecucaoAceiteInterno: 
			addressServerSegurado = AddressServer.ADDRESS_SERVER_SEGURADO_ACEITE_INTERNO;
			break;
		case LMTipoExecucaoAceiteExterno: 
			addressServerSegurado = AddressServer.ADDRESS_SERVER_SEGURADO_ACEITE_EXTERNO;
			break;
		case LMTipoExecucaoProducaoInterno:
			addressServerSegurado = AddressServer.ADDRESS_SERVER_SEGURADO_PRODUCAO_INTERNO;
			break;
		case LMTipoExecucaoProducaoExterno:
			addressServerSegurado = AddressServer.ADDRESS_SERVER_SEGURADO_PRODUCAO_EXTERNO;
			break;
		}
		return addressServerSegurado;
	}
	
	public static void callLogonSegurado(Context context, String user, String password, boolean manterLogado, WebserviceInterface logonSeguradoWsInterface) {

		//<< ANDROID STUDIO
//		String idDevice = Client.getDeviceUID(context);
		String idDevice = Util.getDeviceUID(context);
		//>>

		// prepara os parametros
		StringBuilder xmlRequest = new StringBuilder();
		xmlRequest.append("<AutenticarRequest>");
		xmlRequest.append(	"<Sistema>PortalSegurado</Sistema>");
		xmlRequest.append(	"<Usuario>");
		xmlRequest.append(		user);
		xmlRequest.append(	"</Usuario>");
		xmlRequest.append(	"<Senha>");
		xmlRequest.append(		password);
		xmlRequest.append(	"</Senha>");
		xmlRequest.append(	"<ManterLogado>");
		xmlRequest.append(		(manterLogado ? "true" : "false"));
		xmlRequest.append(	"</ManterLogado>");
		xmlRequest.append(	"<IdDevice>");
		xmlRequest.append(		idDevice);
		xmlRequest.append(	"</IdDevice><TipoSO>2</TipoSO>");
		xmlRequest.append("</AutenticarRequest>");
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", user);
		args.put("ativarTrace", trace);
		args.put("xmlRequest", "<![CDATA[" + xmlRequest.toString() + "]]>");

		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "AutenticarMobileApp", args, true, logonSeguradoWsInterface).execute();
	}

	public static DadosLoginSegurado retLogonSegurado(String result)  {
		DadosLoginSegurado dadosLoginSegurado = null;
		if (!ValidationUtils.isStringEmpty(result)) {
			Map<String, Object> resultados = (Map<String, Object>) JsonHelper.jsonToArrayList(result);
			if (null != resultados) {
				Map<String, Object> transactionMessages = (Map<String, Object>)resultados.get("TransactionMessages");
				if (getTransactionMessages(transactionMessages).compareTo("") == 0) {
					dadosLoginSegurado = DadosLoginSegurado.getInstance();					
					dadosLoginSegurado.setCpf((String)resultados.get("CPF")); 					
					dadosLoginSegurado.setTokenAutenticacao((String)resultados.get("TokenAutenticacao")); 
				}
			}
		}
		
		return dadosLoginSegurado;		
	}		

	public static void callEnviarToken(Context context, String cpfCnpj, String tokenNotificacao, WebserviceInterface enviarTokenWsInterface){
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", cpfCnpj);
		args.put("ativarTrace", trace);
		args.put("tipoSO", Integer.valueOf(2));
		args.put("TokenNotificacao", tokenNotificacao);
		
		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "GravarTokenNotificacaoMobileApp", args, true, enviarTokenWsInterface).execute();
		//new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "GravarTokenNotificacaoMobileApp", args, enviarTokenWsInterface).execute();
	}
	
	public static boolean retEnviarToken(String result)  {
		
	    boolean returnToken = false;	
	    if (!ValidationUtils.isStringEmpty(result)) {
			Map<String, Object> resultados = (Map<String, Object>) JsonHelper.jsonToArrayList(result);
			if (null != resultados) {
				Map<String, Object> transactionMessages = (Map<String, Object>)resultados.get("TransactionMessages");
				if (getTransactionMessages(transactionMessages).compareTo("") == 0) {
					returnToken = true;//(Boolean)resultados.get("Sucesso");
				}
			}
		}
		return returnToken;
	}
	
	public static void callLogonSeguradoToken(Context context, String tokenAutenticacao, String usuarioId, WebserviceInterface logonSeguradoWsInterface) {

		//<< ANDROID STUDIO
//		String idDevice = Client.getDeviceUID(context);
		String idDevice = Util.getDeviceUID(context);
		//>>
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", usuarioId);
		args.put("ativarTrace", trace);
		args.put("IdDevice", idDevice);
		args.put("tokenAutenticacao", tokenAutenticacao);

		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "AutenticarTokenMobileApp", args, true, logonSeguradoWsInterface).execute();
	}

	@SuppressWarnings("unchecked")
	public static DadosLoginSegurado retLogonSeguradoToken(String result)  {
		DadosLoginSegurado dadosLoginSegurado = null;
		if (!ValidationUtils.isStringEmpty(result)) {
			Map<String, Object> resultados = (Map<String, Object>) JsonHelper.jsonToArrayList(result);
			if (null != resultados) {
				Map<String, Object> transactionMessages = (Map<String, Object>)resultados.get("TransactionMessages");
				if (getTransactionMessages(transactionMessages).compareTo("") == 0) {
					dadosLoginSegurado = DadosLoginSegurado.getInstance();
					
					dadosLoginSegurado.setCpf((String)resultados.get("CPF")); 
					if(ValidationUtils.isStringEmpty(dadosLoginSegurado.getCpf())){
						dadosLoginSegurado.setCpf((String)resultados.get("Email"));
					}
					
					dadosLoginSegurado.setTokenAutenticacao((String)resultados.get("TokenAutenticacao")); 
				}
			}
		}
		
		return dadosLoginSegurado;		
	}
	
	public static void callEsqueciMinhaSenhaSegurado(Context context, String email, String cpf, WebserviceInterface esqueciMinhaSenhaSeguradoWsInterface) {
		
		// prepara os parametros
		StringBuilder xmlRequest = new StringBuilder();
		xmlRequest.append("<SolicitarNovaSenhaDeAcessoRequest>");
		xmlRequest.append(	"<Email>");
		xmlRequest.append(		email);
		xmlRequest.append(	"</Email>");
		xmlRequest.append(	"<CPF>");
		xmlRequest.append(		cpf);
		xmlRequest.append(	"</CPF>");
		xmlRequest.append("</SolicitarNovaSenhaDeAcessoRequest>");

		String xmlRequestString = xmlRequest.toString();
		xmlRequestString = xmlRequestString.replace("<", "&lt;");
	    xmlRequestString = xmlRequestString.replace(">", "&gt;");

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", Constants.LM_WS_USUARIO);
		args.put("ativarTrace", trace);
		args.put("xmlRequest", xmlRequestString);
		
		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "SolicitarNovaSenhaDeAcessoMobileApp", args, esqueciMinhaSenhaSeguradoWsInterface).execute();
	}

	public static String retEsqueciMinhaSenhaSegurado(String result)  {
		String sRetorno = "";
		if (!ValidationUtils.isStringEmpty(result)) {
			Map<String, Object> transactionMessages = (Map<String, Object>) JsonHelper.jsonToArrayList(result);
			Map<String, Object> mapTransactionMessages = (Map<String, Object>)transactionMessages.get("TransactionMessages");
			sRetorno = getTransactionMessages(mapTransactionMessages);
		}
		return sRetorno;
	}
	
//	public static void callCadastrarUsuario(Context context, String nome, String senha, String email, String cpf, String numeroApolice, String fraseLembrete, String codigoImagemAcesso, WebserviceInterface cadastrarUsuarioWsInterface) {
	public static void callCadastrarUsuario(Context context, /*String nome,*/ String senha, String email, String cpf, String numeroApolice, WebserviceInterface cadastrarUsuarioWsInterface) {

		// prepara os parametros
		StringBuilder xmlRequest = new StringBuilder();
		xmlRequest.append("<CadastrarUsuarioRequest>");
		xmlRequest.append(	"<Nome></Nome>");
		xmlRequest.append(	"<Senha>");
		xmlRequest.append(		senha);
		xmlRequest.append(	"</Senha>");
		xmlRequest.append(	"<Email>");
		xmlRequest.append(		email);
		xmlRequest.append(	"</Email>");
		xmlRequest.append(	"<CPF>");
		xmlRequest.append(		cpf);
		xmlRequest.append(	"</CPF>");
		xmlRequest.append(	"<NumeroApolice>");
		xmlRequest.append(		numeroApolice);
		xmlRequest.append(	"</NumeroApolice>");
		xmlRequest.append(	"<FraseLembrete></FraseLembrete>");
		xmlRequest.append(	"<CodigoImagemAcesso></CodigoImagemAcesso>");
		xmlRequest.append("</CadastrarUsuarioRequest>");

		String xmlRequestString = xmlRequest.toString();
		xmlRequestString = xmlRequestString.replace("<", "&lt;");
	    xmlRequestString = xmlRequestString.replace(">", "&gt;");

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", email);
		args.put("ativarTrace", trace);
		args.put("xmlRequest", xmlRequestString);

		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "CadastrarUsuarioMobileApp", args, cadastrarUsuarioWsInterface).execute();
	}
	
	public static String retCadastrarUsuario(String result) {
		String retorno = "ERRO!";
	
		if (!ValidationUtils.isStringEmpty(result)) {
			Map<String, Object> resultados = (Map<String, Object>) JsonHelper.jsonToArrayList(result);
			Map<String, Object> transactionMessages = (Map<String, Object>)resultados.get("TransactionMessages");
			retorno = getTransactionMessages(transactionMessages);
		}
		
		return retorno;
	}
	
	public static void callGetMeusSegurosLiberty(Context context, String cpfCnpj, String filtro, WebserviceInterface meusSegurosLibertyWsInterface) {
		
		// prepara os parametros
		StringBuilder xmlRequest = new StringBuilder();
		
		xmlRequest.append("<LoadRequest>");
		xmlRequest.append(	"<emailLogin></emailLogin>");
		xmlRequest.append(	"<CPFCNPJ>");
		xmlRequest.append(		cpfCnpj);
		xmlRequest.append(	"</CPFCNPJ>");
		xmlRequest.append(	"<TipoPesquisaVigencia>");
		xmlRequest.append(		filtro);
		xmlRequest.append(	"</TipoPesquisaVigencia>");
		xmlRequest.append("</LoadRequest>");
		
		String xmlRequestString = xmlRequest.toString();
		xmlRequestString = xmlRequestString.replace("<", "&lt;");
	    xmlRequestString = xmlRequestString.replace(">", "&gt;");
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", 	cpfCnpj);
		args.put("ativarTrace", trace);
		args.put("xmlRequest", 	xmlRequestString);

		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "MeusSegurosLibertyMobileApp", args, meusSegurosLibertyWsInterface).execute();
	}
	
	public static List<Object> retGetMeusSegurosLiberty(String result) throws LibertyException {
		
		List<Object> meusSeguros = new ArrayList<Object>();
		if (!ValidationUtils.isStringEmpty(result)) {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> resultados = (Map<String, Object>) JsonHelper.jsonToArrayList(result);
			
			//<< Retorna erro, de acordo com o caso chamara a autenticacao e depois chamara o servico "callGetMeusSegurosLiberty" novamente 
			@SuppressWarnings("unchecked")
			Map<String, Object> transactionMessages = (Map<String, Object>)resultados.get("TransactionMessages");
			String userErrorMessagesMsg = getTransactionMessages(transactionMessages);			

			if(userErrorMessagesMsg.compareTo("SESSAO_INVALIDA") == 0){
               LibertyException exception = new LibertyException("Servidor enviou "+ userErrorMessagesMsg);
               exception.setLibertyExceptionEnum(LibertyExceptionEnum.LM_ERRO_SESSAO_INVALIDA);
               throw exception;
            }
			//>>
			
			@SuppressWarnings("unchecked")
			Map<String, Object> resultadosSeguros = (Map<String, Object>)resultados.get("Seguros");
			
			List<Object> auto 			= (List<Object> )resultadosSeguros.get("SeguroDeAuto");
			List<Object> residencial 	= (List<Object> )resultadosSeguros.get("SeguroResidencial");
			List<Object> pessoais 		= (List<Object> )resultadosSeguros.get("SegurosPessoais");
			List<Object> outrosSeguros 	= (List<Object> )resultadosSeguros.get("OutrosSegurosLiberty");
			List<Object> empresarial 	= (List<Object> )resultadosSeguros.get("SeguroEmpresarial");

			if (auto != null) {
				for (Object obj: auto) {
					Map<String, Object> objItem = (Map<String, Object>)obj;
					objItem.put("TipoSeguro", "AUTO");
					meusSeguros.add(objItem);
				}
			}

			if (residencial != null) {
				for (Object obj: residencial) {
					Map<String, Object> objItem = (Map<String, Object>)obj;
					objItem.put("TipoSeguro", "RESIDENCIA");
					meusSeguros.add(obj);
				}
			}
			
			if (pessoais != null) {
				for (Object obj: pessoais) {
					Map<String, Object> objItem = (Map<String, Object>)obj;
					objItem.put("TipoSeguro", "PESSOAL");
					meusSeguros.add(obj);
				}
			}
			
			if (outrosSeguros != null) {
				for (Object obj: outrosSeguros) {
					Map<String, Object> objItem = (Map<String, Object>)obj;
					objItem.put("TipoSeguro", "OUTROS");
					meusSeguros.add(obj);
				}
			}
			
			if (empresarial != null) {
				for (Object obj: empresarial) {
					Map<String, Object> objItem = (Map<String, Object>)obj;
					objItem.put("TipoSeguro", "EMPRESARIAL");
					meusSeguros.add(obj);
				}
			}
		}
		
		return meusSeguros;
	}
	
	public static void callGetCoberturasApolice(Context context, String email, String numeroContrato, String codigoEmissao, String codigoItem, String codigoCIA, WebserviceInterface coberturasApoliceWsInterface) {
		
		// prepara os parametros
		StringBuilder xmlRequest = new StringBuilder();
		xmlRequest.append("<ObterCoberturasRequest>");
		xmlRequest.append(	"<NumeroContrato>");
		xmlRequest.append(		numeroContrato);
		xmlRequest.append(	"</NumeroContrato>");
		xmlRequest.append(	"<CodigoEmissao>");
		xmlRequest.append(		codigoEmissao);
		xmlRequest.append(	"</CodigoEmissao>");
		xmlRequest.append(	"<CodigoItem>");
		xmlRequest.append(		codigoItem);
		xmlRequest.append(	"</CodigoItem>");
		xmlRequest.append(	"<CodigoCia>");
		xmlRequest.append(		codigoCIA);
		xmlRequest.append(	"</CodigoCia>");
		xmlRequest.append("</ObterCoberturasRequest>");
		
		String xmlRequestString = xmlRequest.toString();
		xmlRequestString = xmlRequestString.replace("<", "&lt;");
	    xmlRequestString = xmlRequestString.replace(">", "&gt;");

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", email);
		args.put("ativarTrace", trace);
		args.put("xmlRequest", xmlRequestString);

		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "ObterCoberturasMobileApp", args, coberturasApoliceWsInterface).execute();
	}
	
	public static List<Object> retGetCoberturasApolice(String result) throws LibertyException {
		
		List<Object> coberturas = new ArrayList<Object>();
		if (!ValidationUtils.isStringEmpty(result)) {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> resultados = (Map<String, Object>) JsonHelper.jsonToArrayList(result);
			
			//<< Retorna erro, de acordo com o caso chamara a autenticacao e depois chamara o servico "callGetCoberturasApolice" novamente 
			@SuppressWarnings("unchecked")
			Map<String, Object> transactionMessages = (Map<String, Object>)resultados.get("TransactionMessages");
			String userErrorMessagesMsg = getTransactionMessages(transactionMessages);			

			if(userErrorMessagesMsg.compareTo("SESSAO_INVALIDA") == 0){
               LibertyException exception = new LibertyException("Servidor enviou "+ userErrorMessagesMsg);
               exception.setLibertyExceptionEnum(LibertyExceptionEnum.LM_ERRO_SESSAO_INVALIDA);
               throw exception;
            }
			//>>
			
			coberturas = (ArrayList<Object>) JsonHelper.jsonToArrayList("Coberturas", result);
		}
		return coberturas;
	}
		
	public  static void callCriarSessao(Context context, String usuarioId, WebserviceInterface criarSessaoWsInterface){

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", 	usuarioId);
		args.put("ativarTrace", trace);

		// Chama o ws
		new WebserviceInvokerTask(context, getAddressServerSegurado(), "http://tempuri.org", "NovoClubeLibertyController.asmx", "CriarSessao", args, criarSessaoWsInterface).execute();
	}

//	public static String retCriarSessao(String result) throws LibertyException {
//
//		if (!ValidationUtils.isStringEmpty(result)) {
//
//
//		}
//	}

	public static void callGetOficinasReferenciadas(Context context, String email, String cep, String raio, WebserviceInterface oficinasReferenciadasWsInterface) {
		
		// Seleciona o server
		AddressServer server = null;
		
		switch (Constants.execucao)
		{
			case LMTipoExecucaoDesenv: 
			case LMTipoExecucaoAceiteInterno: 
			case LMTipoExecucaoProducaoInterno:
				server = AddressServer.ADDRESS_SERVER_SEGURADO_PRODUCAO_INTERNO;
				break;
			case LMTipoExecucaoAceiteExterno: 
			case LMTipoExecucaoProducaoExterno:
				server = AddressServer.ADDRESS_SERVER_SEGURADO_PRODUCAO_EXTERNO;
				break;
		}
		
		// prepara os parametros
		StringBuilder xmlRequest = new StringBuilder();
		xmlRequest.append("<OficinaRequest>");
		xmlRequest.append("<CEP>");
		xmlRequest.append(cep);
		xmlRequest.append("</CEP>");
		xmlRequest.append("<Raio>");
		xmlRequest.append(raio);
		xmlRequest.append("</Raio>");
		xmlRequest.append("</OficinaRequest>");
		
		String xmlRequestString = xmlRequest.toString();
		xmlRequestString = xmlRequestString.replace("<", "&lt;");
	    xmlRequestString = xmlRequestString.replace(">", "&gt;");
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("usuarioId", email);
		args.put("ativarTrace", trace);
		args.put("xmlRequest", xmlRequestString);

		// Chama o ws
		new WebserviceInvokerTask(context, server, "http://tempuri.org/LibertyPortalSegurado", "ControllerUI.asmx", "ConsultarOficinasMobileApp", args, oficinasReferenciadasWsInterface).execute();
		
		/*
		 * Para teste:
		 * 
		 * https://meuespaco.libertyseguros.com.br/WSPortalSegurado/ControllerUI.asmx?op=ConsultarOficinasMobileApp
		 * 
		 * <OficinaRequest><CEP>04578000</CEP><Raio>30</Raio></OficinaRequest>
		 */
	}

	public static List<Object> retGetOficinasReferenciadas(String result)  {
		List<Object> oficinas = new ArrayList<Object>();
		if (!ValidationUtils.isStringEmpty(result)) {
			oficinas = JsonHelper.jsonToArrayList("Oficinas", result);
		}
		return oficinas;
	}


	private static String getTransactionMessages(Map<String, Object> transactionMessages) {
		String sRetorno = "ERRO";

		if (null != transactionMessages && transactionMessages.size() != 0) {
			
			String strUserErrorMessages = "";
			String strWarningMessages = "";
			
			List<Object> userErrorMessages = (List<Object>)transactionMessages.get("UserErrorMessages");
			List<Object> warningMessages = (List<Object>)transactionMessages.get("WarningMessages");
			
			sRetorno = "";

			if (userErrorMessages != null) {
				if (userErrorMessages.size() != 0) strUserErrorMessages = (String)userErrorMessages.get(0);
				if (strUserErrorMessages.compareTo("") != 0) {
					sRetorno += (sRetorno.compareTo("") != 0 ? " " : "") + strUserErrorMessages;
				}
			}

			if (warningMessages != null) {
				if (warningMessages.size() != 0) strWarningMessages = (String)warningMessages.get(0); 
				if (strWarningMessages.compareTo("") != 0) {
					sRetorno += (sRetorno.compareTo("") != 0 ? " " : "") + strWarningMessages;
				}
			}
		}
		
		return sRetorno;
	}
	
}