package br.com.libertyseguros.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;

//import br.com.libertyseguros.mobile.common.CustomApplication;

public class ClubeLibertyLocaisMapaActivity extends FragmentActivity {

	private GoogleMap googleMap;
	private Map<String, Object> itemSel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_clube_liberty_locais_mapa);
			Util.setTitleNavigationBar(this, R.string.title_activity_clube_liberty_locais_mapa);

			Intent it = getIntent();
			if (it == null) return;	

			Bundle parms = it.getExtras();
			itemSel = (Map<String, Object>) parms.getSerializable(Constants.LM_EXTRA_ITEM);

			String titulo = (String)itemSel.get("Titulo");

			List<Object> arrayContatos = (List<Object>)itemSel.get("Contatos");
			if (arrayContatos.size() != 0) {
				Map<String, Object> contato = (Map<String, Object>)arrayContatos.get(0);
				String strLatitude = (String)contato.get("Latitude");
				String strLongitude = (String)contato.get("Longitude");
				double latitude = Double.parseDouble(strLatitude);
				double longitude = Double.parseDouble(strLongitude);

				if (latitude != 0 && longitude != 0) {
					
					LatLng latLng = new LatLng(latitude, longitude);
					
					// Creates a marker.
					StringBuilder snippet = new StringBuilder();
					snippet.append((String)contato.get("Endereco"));
					snippet.append(" - ");
					snippet.append((String)contato.get("Bairro"));
					snippet.append("/");
					snippet.append((String)contato.get("CEP"));
					
					setUpMapIfNeeded();
					
					googleMap.addMarker(new MarkerOptions().position(latLng).title(titulo).snippet(snippet.toString()));
					
					setCenterAndZoom(latLng);
				}
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_clube_liberty_locais_mapa, menu);	
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
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
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}
	
	@Override
	protected void onResume() {
	  super.onResume();
//		ANDROID STUDIO
//	  CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  super.onPause();
//		ANDROID STUDIO
//	  CustomApplication.activityPaused();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (googleMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapClubeLiberty)).getMap();
//			// Check if we were successful in obtaining the map.
//			if (googleMap != null) {
//				googleMap.setMyLocationEnabled(true);
//			}
		}
	}
	
	private void setCenterAndZoom(LatLng latLng) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latLng) // Sets the center of the map currenty location
				.zoom(16) // Sets the zoom
//				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
}
