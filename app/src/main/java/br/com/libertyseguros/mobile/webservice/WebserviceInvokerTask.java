package br.com.libertyseguros.mobile.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapFault;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;

public class WebserviceInvokerTask extends AsyncTask<Void, Void, String> {

	// para exibir a barra de progresso
	// TODO verificar se j� n�o est� exibindo!!
	private Context context;
	private ProgressDialog progressDialog;
	
	// Propridedades para conex�o
	
	private AddressServer server;
	private String nameSpace;
	private String soapAction;
	private String webserviceName;
	private String methodName;
	private Map<String, Object> params;
	private boolean saveCookie;
	
	// classe a ser implementada para tratar o retorno espec�fico de cada m�todo
	private WebserviceInterface wsInterface;
	
	private LibertyException lException = null;
	
	/**
	 * Construtor que prepara para a chamada do m�todok
	 * @param context
	 * @param server
	 * @param nameSpace
	 * @param webserviceName
	 * @param methodName
	 * @param params
	 * @param saveCookie
	 * @param soapAction
	 * @param wsInterface
	 */
	public WebserviceInvokerTask(Context context, AddressServer server, String nameSpace, String webserviceName, String methodName, Map<String, Object> params, boolean saveCookie, String soapAction, WebserviceInterface wsInterface) {
		super();
		inicializeWs(context, server, nameSpace, webserviceName, methodName, params, saveCookie, soapAction, wsInterface);
	}

	public WebserviceInvokerTask(Context context, AddressServer server, String nameSpace, String webserviceName, String methodName, Map<String, Object> params, boolean saveCookie, WebserviceInterface wsInterface) {
		super();
		inicializeWs(context, server, nameSpace, webserviceName, methodName, params, saveCookie, "", wsInterface);
	}

	public WebserviceInvokerTask(Context context, AddressServer server, String nameSpace, String webserviceName, String methodName, Map<String, Object> params, WebserviceInterface wsInterface) {
		super();
		inicializeWs(context, server, nameSpace, webserviceName, methodName, params, false, "", wsInterface);
	}
	
	private void inicializeWs(Context context, AddressServer server, String nameSpace, String webserviceName, String methodName, Map<String, Object> params, boolean saveCookie, String soapAction, WebserviceInterface wsInterface) {
		this.context = context;
		this.server = server;
		this.nameSpace = nameSpace;
		this.webserviceName = webserviceName;
		this.methodName = methodName;
		this.params = params;
		this.saveCookie = saveCookie;
		this.wsInterface = wsInterface;
		this.soapAction = soapAction;
	}

	/**
	 * Executa antes da chamada
	 */
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getString(R.string.aguarde));
		progressDialog.show();
	}

	@Override
	/**1z
	 * Executa em background (thread separada) a chamada
	 */
	protected String doInBackground(Void... v) {
				
		String result = new String();
		
		try {
			
			//make soap request
			StringBuilder xmlRequest = new StringBuilder();
			xmlRequest.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlRequest.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");
			xmlRequest.append("<soap:Body>\n");
		    if (params.isEmpty()) {
		    	xmlRequest.append("<").append(methodName).append(" xmlns=\"").append(nameSpace).append("\" />\n");
		    } else {
		    	xmlRequest.append("<").append(methodName).append(" xmlns=\"").append(nameSpace).append("\" >\n");
		    	
		    	for (String key : params.keySet()) {	
		    		xmlRequest.append("<").append(key).append(">").append(params.get(key)).append("</").append(key).append(">");
		    	}
		    	
		    	//close envelope
		    	xmlRequest.append("</").append(methodName).append(">\n");		    	
		    }
		    xmlRequest.append("</soap:Body>\n");
		    xmlRequest.append("</soap:Envelope>\n");
            
		    StringEntity entity = new StringEntity(xmlRequest.toString(), HTTP.UTF_8);
            entity.setContentType("text/xml; charset=utf-8");  
            
            HttpPost request = new HttpPost(server.getUrl() + "/" + webserviceName);
            
			// header necessarios:
			if (this.soapAction.equals("")) {
				this.soapAction = nameSpace + (nameSpace.endsWith("/") ? "" : "/") + methodName;
			}			
			request.setHeader("Content-Type","text/xml; charset=uft-8");
			request.setHeader("SOAPAction", soapAction);            
			request.setEntity(entity);
			
			// Parte de cookie:

			/* ANTIGO
			if (CallWebServices.headerListHttp != null) {
				
		        for(Object header : CallWebServices.headerListHttp)
		        {
				    Header headerProperty = (Header) header;
				    if (null != headerProperty && !ValidationUtils.isStringEmpty(headerProperty.getName()) 
		        			&& headerProperty.getName().toUpperCase().compareTo("SET-COOKIE") == 0)
				    {
				    	String key = "Cookie";
			        	String value = headerProperty.getValue().toString();
			        	String [] split = value.split(";");
			        	value = split[0].toString();

			        	request.setHeader(key, value);
			        	break;
		        	}
		        }
			}*/

			request.setHeader("Cookie", CallWebServices.aspnetsession);

			// Send request to ASMX service
			DefaultHttpClient httpClient = new MyHttpClient(this.context);

			HttpResponse response = httpClient.execute(request);

			if (saveCookie) {
				// ANTIGO: CallWebServices.headerListHttp = response.getAllHeaders();

				for (Object header : response.getAllHeaders()) {
					Header headerProperty = (Header) header;
					if (null != headerProperty && !ValidationUtils.isStringEmpty(headerProperty.getName())
							&& headerProperty.getName().toUpperCase().compareTo("SET-COOKIE") == 0) {
						String value = headerProperty.getValue().toString();
						String[] split = value.split(";");
						value = split[0].toString();

						if (value != "" && !value.contains("alive")) {
							CallWebServices.aspnetsession = value;
						}
						break;
					}
				}
			}

			HttpEntity responseEntity = response.getEntity();
			
			String envelopSoapResponse = convertStreamToString(responseEntity);
			
			if (!ValidationUtils.isStringEmpty(envelopSoapResponse)) {
				result = getResponse(envelopSoapResponse);				
			}
			
		}
		catch (SocketException e) {
			lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_REDE_DADOS);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
			cancel(true);
		} 
		catch (SoapFault e) {
			
			lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_REDE_DADOS);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
			cancel(true);
		}
		catch (SSLException e){
			
			lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_SSL);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());			
			cancel(true);
		}		 
		catch (UnsupportedEncodingException e) {

			lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_DECODE);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());		
			cancel(true);
		} 
		catch (ClientProtocolException e) {
			
			lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_REDE_DADOS);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
			cancel(true);
		} 
		catch (IOException e) {
			
			if (e != null) {
				lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_REDE_DADOS);
				Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
			}
			cancel(true);
		} catch (ParserConfigurationException e) {

			lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_DECODE);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());			
			cancel(true);
			
		} catch (SAXException e) {

			lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_DECODE);
			Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());			
			cancel(true);
		} 
		
		Log.i("RESPONSE", result);
		
		return result;
	}
	

	private static String convertStreamToString(HttpEntity entity) {
		try {
			InputStream stream = entity.getContent();
	        InputStreamReader reader = new InputStreamReader(stream);
			StringBuilder sb = new StringBuilder();
			char[] buffer = new char[1024];
			int count = 0;
			while ((count = reader.read(buffer, 0, 1024)) >= 0) {
				sb.append(buffer, 0, count);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	public String getResponse(final String response) throws ParserConfigurationException, SAXException, IOException
	{
//		mudanca para não aconter
//		Default buffer size used in BufferedReader constructor. It would be better to be explicit if an 8k-char buffer is required.
//		BufferedReader 		br 		= new BufferedReader(new StringReader(response));
		BufferedReader 		br 		= new BufferedReader(new StringReader(response), 8192);
		
		InputSource 		is 		= new InputSource(br);
		ResponseParser parser 	= new ResponseParser(methodName + "Result");
		SAXParserFactory 	factory = SAXParserFactory.newInstance();
		SAXParser 			sp 		= factory.newSAXParser();
		XMLReader 			reader	= sp.getXMLReader();
		reader.setContentHandler(parser);
		reader.parse(is);
		return parser.getResponse();
	}
	
	/**
	 * Executa ap�s a chamada
	 */
	@Override
	protected void onPostExecute(String result) {
		
		if(progressDialog != null)
			progressDialog.dismiss();

		if (isCancelled()) {
			result = null;
		}

		if (null == lException) {
			wsInterface.callBackWebService(result);
		}
		else{
			// << EPO: Cada método deve tratar o erro e exibir msg especifica do método chamado
			//Util.showException(context, lException);
			wsInterface.callWebServicesFailWithError(lException);
			// >>
		}
	}
	
    @Override
    protected void finalize() throws Throwable {
    	Util.callGB();
    	super.finalize();
    }

}
