package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.GeocodeTask;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.SortComparatorMap;
import br.com.libertyseguros.mobile.common.SortComparatorMap.TypeSortComparatorMap;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.AddressLocation;
import br.com.libertyseguros.mobile.webservice.CallWebServices;
import br.com.libertyseguros.mobile.webservice.WebserviceInterface;

//import br.com.libertyseguros.mobile.common.AnalyticsApplication;

//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
//import com.google.android.gms.activity_location.LocationClient;

/**
 * Fluxo de funcionamento:
 * 
 *		Primeira execução pesquisa a partir da coordenada local;
 * 		Depois se volta da activity da lista de ofininas não pesquisa apenas exibe resultado;
 * 		Caso venha da activity de pesquisa por CEP realiza pesquisa com o CEP informado. 
 * 
 * @author evandro
 *
 */
public class OficinasReferenciadasActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener /*, OnInfoWindowClickListener*/, DialogInterface.OnClickListener {

	private GoogleMap mMap;
//	private LocationClient mLocationClient;
	private GoogleApiClient googleApiClient;
	private TextView textLocalizacaoAtualOficinas;
	
	private boolean buscouCep = false; 
	
	/**
	 * Variavel auxiliar para navegação/comportamento da Activity
	 */
	private static LMPesquisa pesquisa = LMPesquisa.LMPesquisa_GPS_LOCAL;

	private Tracker mTracker;

	public enum LMPesquisa {
		LMPesquisa_GPS_LOCAL, 
		LMPesquisa_CEP,
		LMPesquisa_FEITA
	}
	
	// These settings are the same as the settings for the map. They will in fact give you updates at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(25000) // 25 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	// Armazena local(CEP p/ pesquisa de oficinas) mesmo depois de sair da activity 
	private static AddressLocation location;

	// Armazena lista de oficinas 
	private static List<Object> oficinas;
	
	public static AddressLocation getAddressLocation() {
		return OficinasReferenciadasActivity.location;
	}
	
	public static void setAddressLocation(AddressLocation location) {
		OficinasReferenciadasActivity.location = location;
	}
	
	public static List<Object> getOficinas() {
		return OficinasReferenciadasActivity.oficinas;
	}

	public static void setOficinas(List<Object> oficinas) {
		OficinasReferenciadasActivity.oficinas = oficinas;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oficinas_referenciadas);

		Util.setTitleNavigationBar(this, R.string.title_activity_oficinas_referenciadas);

//		//<< ANDROID STUDIO
//		GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_oficinas_referenciadas));
		// Obtain the shared Tracker instance.
//		AnalyticsApplication application = (AnalyticsApplication) getApplication();
//		mTracker = application.getDefaultTracker();
		AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
		mTracker = analyticsApplication.getDefaultTracker(getApplication());
//		//>>

		textLocalizacaoAtualOficinas = (TextView) findViewById(R.id.textLocalizacaoAtualOficinas);
		
		// Tela de endere�o
		Button btnOficinasEndereco = (Button)findViewById(R.id.btn_cancelar);

		//ajusta o texto
		btnOficinasEndereco.setText(R.string.btn_endereco);

		btnOficinasEndereco.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callEndereco();
			}
		});

		// Tela de lista de oficinas
		Button btnOficinasLista = (Button)findViewById(R.id.btn_salvar);

		//ajusta o texto
		btnOficinasLista.setText(R.string.btn_lista);

		btnOficinasLista.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callLista();
			}
		});
		
		/*
		 * 	private ProgressDialog progress;
		progress = new ProgressDialog(OficinasReferenciadasActivity.this);
		progress.setCancelable(false);
		progress.setMessage(getString(R.string.text_verificando_localizacao));
		progress.show();
		* 	progress.dismiss();
		*/
		
		/*
		 * Outra forma de obter CEP
		String serverAddress = "http://maps.google.com/maps/api/geocode/json?address=" + Double.toString(gps.getLatitude()) + "," + Double.toString(gps.getLongitude()) + "&sensor=false";
		PendingIntent pendingResult = createPendingResult(pesquisa.ordinal(), new Intent(), 0);
		Intent intent = new Intent(getApplicationContext(), DownloadIntentService.class);
		intent.putExtra(DownloadIntentService.URL_EXTRA, serverAddress);
		intent.putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, pendingResult);
		startService(intent); 
		*
    	String result = data.getStringExtra(DownloadIntentService.RSS_RESULT_EXTRA);
    	setAddressLocation(Util.parseAddressLocation(result));		 
    	*/
	}

	@Override
	protected void onResume() {
		super.onResume();

		//<< ANDROID STUDIO
		Log.i("Google Analytics: ", getString(R.string.title_activity_oficinas_referenciadas));
		mTracker.setScreenName(getString(R.string.title_activity_oficinas_referenciadas));
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		//>>

		setUpMapIfNeeded();
		
		switch (OficinasReferenciadasActivity.pesquisa) {
		
			case LMPesquisa_GPS_LOCAL:{
				
				// Caso venha da activity do menu principal:
				
				// Verifica se o GPS esta ligado, caso contrário envia mensagem
				LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
				if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					showSettingsAlert(this);
				}

				// ativa para pegar Localizacao
				setUpLocationClientIfNeeded();
			}
				break;

			case LMPesquisa_CEP:
				// Caso venha da activity de pesquisa por CEP: ver codigo em OnActivityResult()
				break;
	
			case LMPesquisa_FEITA:
				
				// caso 
				if (null != getAddressLocation() && getAddressLocation().getAddressFormatted() != null) {
					textLocalizacaoAtualOficinas.setText(getAddressLocation().getAddressFormatted());
				}
				setOficinasNoMapa();
				break;
		}

		CustomApplication.activityResumed();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (googleApiClient == null) {
			//<< UPDATE google-play-service
//			googleApiClient = new LocationClient(getApplicationContext(),
//												this, // ConnectionCallbacks
//												this); // OnConnectionFailedListener
			googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
			// >>
		}
		googleApiClient.connect();
	}
	
	/*
	private void setCenterAndZoom() {		
		if (mLocationClient != null && mLocationClient.isConnected()) {			
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude()));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
			mMap.moveCamera(center);
			mMap.animateCamera(zoom);
		}
	}
	 */

	private void setCenterAndZoom(LatLng latLng) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latLng) // Sets the center of the map currenty activity_location
				.zoom(12) // Sets the zoom
//				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {

		try {	
			// Caso não tenha pesquisado
			if(!buscouCep){
				
				buscouCep = true; // executando...
				
				// vai realizar pesquisa de CEP pela API do GeoCode e pela URL:
				AddressLocation addressLocation = null;
				AsyncTask<Location,Void,AddressLocation> asyncTask = new GeocodeTask(this);

				//TODO melhora de uso do GPS

//				try {
					addressLocation = asyncTask.execute(location).get();
//				} catch (Exception e) {
//					final LibertyException lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_GEOCODE_CEP);
//					Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
//					// EPO: Exibe menssagem de erro para o usuário
//
//					final Context context = this;
////TODO Can't create handler inside thread that has not called Looper.prepare()
//					runOnUiThread(new Runnable() {
//						public void run() {
//							Util.showException(context, lException);
//						}
//					});
////					Util.showException(this, lException);
//
//				}
				
				// posiciona o mapa
				LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
				setCenterAndZoom(latLng );
				
				if (null != addressLocation) {
					// obtendo a localização - exibe endereço atual
					textLocalizacaoAtualOficinas.setText("" + addressLocation.getAddressFormatted());
					
					// obtendo o CEP pesquisa oficinas:
					if (null != addressLocation.getPostalCode()) {
						setAddressLocation(addressLocation);
						
						// Verifica se vai chamar ou não o serviço de lista de oficinas:
						if(null != getOficinas() && getOficinas().size() > 0){
							// Caso já tenha oficinas, não chama
							setOficinasNoMapa();
							// TODO chamar se o CEP for diferente do anterior
						}
						else {
							// caso não tenha a lista, chama
							callOficinas();
						}
					}else{
						//Caso não tenha localização, verifica se foi por causa do GPS:
						// Verifica se o GPS esta ligado, caso contrário envia mensagem
						LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
						if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
						{
							showSettingsAlert(this);
						}else{

							// Caso pesquisado e falhou: Pedir CEP!!
							Util.displayConfirmAlert(OficinasReferenciadasActivity.this,
									getString(R.string.aviso),
									getString(R.string.nao_encontrado_informar_cep),
									getString(R.string.no),
									getString(R.string.yes),
									OficinasReferenciadasActivity.this);
						}
					}
				}else {
					//Caso não tenha localização, verifica se foi por causa do GPS:
					// Verifica se o GPS esta ligado, caso contrário envia mensagem
					LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
					if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
					{
						showSettingsAlert(this);
					}else{

						// Caso pesquisado e falhou: Pedir CEP!!
						Util.displayConfirmAlert(OficinasReferenciadasActivity.this,
								getString(R.string.aviso),
								getString(R.string.nao_encontrado_informar_cep),
								getString(R.string.no),
								getString(R.string.yes),
								OficinasReferenciadasActivity.this);
					}
				}
			}
		
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		//<< UPDATE google-play-service
//		googleApiClient.requestLocationUpdates(REQUEST, this); // LocationListener
		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, REQUEST, this);
		// >>
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}
	
	/* **********************
	 *  Webservice
	 * **********************/

	private void callOficinas() {
		
		try {
			String cep  = getAddressLocation().getPostalCode();
			String raio = "30";
			
			// Inst�ncia que trabalha o retorno do webservice
			WebserviceInterface oficinasReferenciadasWsInterface = new WebserviceInterface(this) {
				
				@Override
				public void callBackWebService(String response) {
					
					if(null != response) {
						carregaOficinas(response);
						setOficinasNoMapa();
					}
				}
				
				@Override
				public void callWebServicesFailWithError(LibertyException error) {
					// EPO: Exibe menssagem de erro para o usuário
					Util.showException(context, error);						
				}
			};
			
			// Chama o ws  
			CallWebServices.callGetOficinasReferenciadas(this, Constants.LM_WS_USUARIO, cep, raio, oficinasReferenciadasWsInterface);
				
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	

	private void carregaOficinas(String response) {
		
		try {
			setOficinas(CallWebServices.retGetOficinasReferenciadas(response));

			if (getOficinas() != null && getOficinas().size() > 0) {
				SortComparatorMap compare = new SortComparatorMap("Distancia", TypeSortComparatorMap.TypeSortComparatorMap_Double);
				Collections.sort(getOficinas(), compare);
			}
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
		
	/**
	 * Metodo que adiciona lista de oficinas como markes no mapa
	 */
	private void setOficinasNoMapa() {
		
		if (getOficinas() != null && getOficinas().size() > 0) {
			
			for (int i = getOficinas().size() - 1; i >= 0; i--) {
				
				Map<String, Object> campo = (Map<String, Object>) getOficinas().get(i);
	
				String strLatitude = (String) campo.get("Latitude");
				String strLongitude = (String) campo.get("Longitude");
				double latitude = Double.parseDouble(strLatitude);
				double longitude = Double.parseDouble(strLongitude);
	
				if (latitude != 0 && longitude != 0) {
					
					LatLng latLng = new LatLng(latitude, longitude);
					
					if(i == 0 && OficinasReferenciadasActivity.pesquisa == LMPesquisa.LMPesquisa_CEP){					
						//posiciona o mapa de acordo com a primeira oficina:
						setCenterAndZoom(latLng);
					}
					
					String nome 	= (String) campo.get("Nome");
					String logradouro = (String) campo.get("Logradouro");
					String endereco = (String) campo.get("Endereco");
					String numero 	= (String) campo.get("Numero");
					String bairro 	= (String) campo.get("Bairro");
					String cidade 	= (String) campo.get("Cidade");
					String uf 		= (String) campo.get("UF");
					String cep 		= (String) campo.get("CEP");
	
					// Creates a marker.
					String snippet = logradouro + ": " + endereco + "," + numero + " - " + bairro + "/" + cep;
					mMap.addMarker(new MarkerOptions().position(latLng).title(nome).snippet(snippet ));
					
				}
			}
		}
	}

	private void callEndereco() {
		try {
			OficinasReferenciadasActivity.pesquisa = LMPesquisa.LMPesquisa_CEP;
			
			Bundle parms = new Bundle();
			Intent it;
			it = new Intent(this, OficinasReferenciadasBuscaActivity.class);
			it.putExtras(parms);
			startActivityForResult(it, pesquisa.ordinal());
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void callLista() {
		try {
			OficinasReferenciadasActivity.pesquisa = LMPesquisa.LMPesquisa_FEITA;
			
			Bundle params = new Bundle();
			params.putSerializable(Constants.LM_EXTRA_OFICINAS, (Serializable) getOficinas());

			Intent it;
			it = new Intent(this, OficinasReferenciadasListaActivity.class);
			it.putExtras(params);
			startActivityForResult(it, pesquisa.ordinal());
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {

			if (LMPesquisa.LMPesquisa_CEP.ordinal() == requestCode) {

				// Caso esteje vindo da activity de pesquisa por CEP:
				if (resultCode == Activity.RESULT_OK) {

					// e informou CEP para a pesquisa:
					String cep = data.getStringExtra(Constants.LM_EXTRA_CEP);
					String addressFormatted = "Próximo ao CEP: " + cep;

					AddressLocation addressLocation = new AddressLocation();
					addressLocation.setPostalCode(cep);
					addressLocation.setAddressFormatted(addressFormatted);

					textLocalizacaoAtualOficinas.setText(addressFormatted);

					setAddressLocation(addressLocation);

					callOficinas();
					// TODO chamar se o CEP for diferente do anterior
				} else {
					// Caso voltou sem informar CEP
					setUpMapIfNeeded();
					if (null != getAddressLocation()
							&& getAddressLocation().getAddressFormatted() != null) {
						textLocalizacaoAtualOficinas.setText(getAddressLocation().getAddressFormatted());
					}
					setOficinasNoMapa();
				}
			}
			
			super.onActivityResult(requestCode, resultCode, data);
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * @param mContext 
     * */
    private void showSettingsAlert(final Activity mContext){
    	
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
 
        // Setting Dialog Title
        alertDialog.setTitle(mContext.getString(R.string.configuracao_de_gps));
 
        // Setting Dialog Message
        alertDialog.setMessage(mContext.getString(R.string.o_gps_nao_esta_ligado));
 
        // On pressing Settings button
        alertDialog.setPositiveButton(mContext.getString(R.string.btn_config), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	
            	OficinasReferenciadasActivity.pesquisa = LMPesquisa.LMPesquisa_GPS_LOCAL;
            	
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ((Activity) mContext).startActivityForResult(intent, Constants.RESULT_ACTION_LOCATION_SOURCE_SETTINGS);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton(mContext.getString(R.string.btn_cancelar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
    
    

	/**
	 * A��o do bot�o Voltar da barra de navega��o (navigation_bar.xml)
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
	public void onBackPressed() {
		OficinasReferenciadasActivity.pesquisa = LMPesquisa.LMPesquisa_GPS_LOCAL;
		super.onBackPressed();
	}


	@Override
	public void onStart() {
		super.onStart();
		//ANDROID STUDIO
//		EasyTracker.getInstance(this).activityStart(this);	//Medir o tempo dentro da tela
	}

	@Override
	public void onStop() {
		super.onStop();
		//ANDROID STUDIO
//		EasyTracker.getInstance(this).activityStop(this);	//Para o tempo dentro da tela
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}

	@Override
	public void onClick(DialogInterface dialogInterface, int btn) {
		try {
			if (btn == Dialog.BUTTON_NEGATIVE){
				// Caso confirme para informar endereço, CEP
				callEndereco();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	public void onPause() {
		super.onPause();
		LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

		CustomApplication.activityPaused();
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
	}


}