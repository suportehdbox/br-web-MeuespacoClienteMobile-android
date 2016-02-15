/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import java.io.Serializable;

import br.com.MondialAssistance.Liberty.common.CustomApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.GeocodeTask;
import br.com.libertyseguros.mobile.common.LibertyException;
import br.com.libertyseguros.mobile.common.LibertyExceptionEnum;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.AddressLocation;


//import br.com.libertyseguros.mobile.common.CustomApplication;

//import br.com.libertyseguros.mobile.common.AnalyticsApplication;

//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
//import com.google.android.gms.activity_location.LocationClient;

/**
 * LocationActivity
 * 
 * @author Evandroo
 */

public class LocationActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener
{
	private GoogleMap googleMap;
//	private LocationClient mLocationClient;
	private GoogleApiClient googleApiClient;
	private AddressLocation addressLocation;
	private TextView textLocalizacaoAtual;
	private boolean obteveLocal = false;

	private LatLng latLng;
	
	// These settings are the same as the settings for the map. They will in fact give you updates at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(25000) // 25 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_location);
			Util.setTitleNavigationBar(this, R.string.title_activity_location);

			textLocalizacaoAtual = (TextView)findViewById(R.id.textLocalizacaoAtual);

			Button btnConcluido = (Button)findViewById(R.id.btn_salvar);
			btnConcluido.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callFinish();
				}
			});


			Button btnCancelar = (Button)findViewById(R.id.btn_cancelar);
			btnCancelar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(Activity.RESULT_CANCELED);
					finish();
				}
			});

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (googleMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLocation)).getMap();
			// Check if we were successful in obtaining the map.
			if (googleMap != null) {
				googleMap.setMyLocationEnabled(true);
			}
		}
	}
	
	private void setUpLocationClientIfNeeded() {
		if (googleApiClient == null) {
			//<< UPDATE google-play-service
//			mLocationClient = new LocationClient(getApplicationContext(),
//												this, // ConnectionCallbacks
//												this); // OnConnectionFailedListener
			googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
//		}
//		mLocationClient.connect();
		}
		googleApiClient.connect();
		// >>
	}
	/*
	private void setCenterAndZoom(Location activity_location) {
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(activity_location.getLatitude(), activity_location.getLongitude()));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
		googleMap.moveCamera(center);
		googleMap.animateCamera(zoom);
	}*/

	private void setCenterAndZoom(LatLng latLng) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
//				.target(new LatLng(activity_location.getLatitude(), activity_location.getLongitude())) // Sets the center of the map currenty activity_location
				.target(latLng) // Sets the center of the map currenty activity_location
				.zoom(16) // Sets the zoom
//				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {

		try {	
			// Caso não tenha pesquisado
			if(!obteveLocal){
				obteveLocal = true; // executando...
				
				// vai realizar pesquisa de Endereço pela API do GeoCode e pela URL:
				AsyncTask<Location,Void,AddressLocation> asyncTask = new GeocodeTask(this);
				((GeocodeTask)asyncTask).setMaxResults(3);
				try {
					addressLocation = asyncTask.execute(location).get();
				} catch (Exception e) {
					LibertyException lException = new LibertyException(e, LibertyExceptionEnum.LM_ERRO_GEOCODE_CEP);
					Log.e(lException.getLibertyExceptionEnum().name(), lException.getMessage());
					// EPO: Exibe menssagem de erro para o usuário
					Util.showException(this, lException);	
				}

//				<< UPDATE google-play-service
				// posiciona o mapa
				latLng = new LatLng(location.getLatitude(), location.getLongitude());
				setCenterAndZoom(latLng);
//				>>
				
				if (null != addressLocation) {
					// exibe endereço atual
					textLocalizacaoAtual.setText(""+ addressLocation.getAddressFormatted());
					
				}else{
					// TODO !!! caso não obtenha endereço, editar no mapa
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
		
	private void callFinish() {
		try {
			if (addressLocation != null) {
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(AddressLocation.LOCATION, (Serializable) addressLocation);
//				<< UPDATE google-play-service
//				bundle.putDouble(AddressLocation.LATITUDE, mLocationClient.getLastLocation().getLatitude());
//				bundle.putDouble(AddressLocation.LONGITUDE, mLocationClient.getLastLocation().getLongitude());

				bundle.putDouble(AddressLocation.LATITUDE, latLng.latitude);
				bundle.putDouble(AddressLocation.LONGITUDE, latLng.longitude);
//				>>
				data.putExtras(bundle);

				setResult(Activity.RESULT_OK, data);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();

		// Verifica se o GPS esta ligado, caso contrário envia mensagem
		LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showSettingsAlert(this);
		}

		// ativa para pegar Localizacao
		setUpLocationClientIfNeeded();

		CustomApplication.activityResumed();
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
			public void onClick(DialogInterface dialog, int which) {

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
	@Override
	public void onPause() {
		super.onPause();
		// << UPDATE google-play-service
//		if (mLocationClient != null) {
//			mLocationClient.disconnect();
//		}
		LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
		// >>

		CustomApplication.activityPaused();
	}

	/**
	 * Ação do botão Voltar da barra de navegação (navigation_bar.xml).
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

}
