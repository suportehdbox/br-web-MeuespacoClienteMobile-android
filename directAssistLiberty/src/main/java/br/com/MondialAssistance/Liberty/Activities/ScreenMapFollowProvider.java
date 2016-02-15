package br.com.MondialAssistance.Liberty.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.MondialAssistance.Liberty.BLL.BLLAddressFinder;
import br.com.MondialAssistance.Liberty.BLL.BLLDirectAssist;
import br.com.MondialAssistance.Liberty.MDL.AddressLocation;
import br.com.MondialAssistance.Liberty.MDL.ServiceDispatchCase;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.Util.ErrorHelper;
import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenMapFollowProvider extends FragmentActivity implements Runnable {

	private static final int ACTION_LOCATION_SOURCE_SETTINGS = 1;

	private GoogleMap mMap;
	private Marker mMarker;
	private Boolean didZoom = false;

	private TextView viewScreenName;
	private ZoomControls zoomControls;
	private Location mLocation;

	private Thread thread;
	private boolean refresh;

	private int fileNumber;

    private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_map_follow_provider);

        setUpMapIfNeeded();
//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);
        AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
        mTracker = analyticsApplication.getDefaultTracker(getApplication());

		Initialize();
		Events();
	}

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        } else {
            setUpMap();
        }
    }


    private void setUpMap() {
        mMap.clear();
    }

	private void Initialize() {

		Intent intent = getIntent();
		fileNumber = intent.getExtras().getInt("FILENUMBER");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenMapLocation);

		zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);

		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider == null || provider.length() == 0
				|| !provider.contains(LocationManager.GPS_PROVIDER)) {

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(R.string.Location);
			dialog.setMessage(R.string.LocationActivate);
			dialog.setPositiveButton(R.string.Yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivityForResult(intent,
									ACTION_LOCATION_SOURCE_SETTINGS);
						}
					});
			dialog.setNegativeButton(R.string.No,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							finish();
						}
					});
			dialog.show();

		} else {
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMyLocationEnabled(true);
            mLocation = mMap.getMyLocation();
            LatLng myLocation = null;
            if (mLocation != null) {
                myLocation = new LatLng(mLocation.getLatitude(),
                        mLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                        17.0f));
            }

            GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

                public void onMyLocationChange(Location location) {

                    mLocation = location;

                    Double changeLat = (location.getLatitude() - mLocation.getLatitude()) * 100000;
                    Double changeLng = (location.getLongitude() - mLocation.getLongitude()) * 100000;
                    if (!didZoom) {
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f));
                        didZoom = true;
                    }
                    if (changeLat < 1 && changeLng < 1 ) {
                        return;
                    }

                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    if (mMarker != null) {
                        mMarker.remove();
                    }
                    mMarker = mMap.addMarker(new MarkerOptions().position(loc));
                    if(mMap != null){
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f));
                        mMap.clear();

                        mLocation = location;
                    }
                }
            };
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);

            refresh = true;
            start();
		}
	}


    private void Events() {

        zoomControls.setOnZoomInClickListener(new OnClickListener() {
            public void onClick(View v) {
                float zoom = mMap.getCameraPosition().zoom;
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom + 1.0f));
            }
        });
        zoomControls.setOnZoomOutClickListener(new OnClickListener() {
            public void onClick(View v) {
                float zoom = mMap.getCameraPosition().zoom;
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom - 1.0f));
            }
        });

    }

	public void start() {

		thread = new Thread(this);
		thread.start();
	}

    public void run() {

        try {

            while (refresh) {

                if (!isFinishing()) {

                    final BLLAddressFinder addressFinder = new BLLAddressFinder();
                    final BLLDirectAssist directAssist = new BLLDirectAssist();

                    final ServiceDispatchCase serviceDispatchCase;
                    final AddressLocation addressMyLocation;

                    LatLng point = getPoint();

                    double latitude = point.latitude;
                    double longitude = point.longitude;

                    addressMyLocation = addressFinder.getAddress(latitude, longitude);
                    serviceDispatchCase = directAssist.getDispatchStatus(ScreenMapFollowProvider.this, fileNumber, 1, ClientParams.ClientID);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (!isFinishing()) {

                                mMap.clear();

                                if ((directAssist.getAction().getResultCode() != null ||
                                        directAssist.getAction().getResultCode().equals(0)) &&
                                        serviceDispatchCase.getProvider().getLicenseNumber() != null) {

                                    Drawable marker = getResources().getDrawable(R.drawable.map_tracking);

                                    AddOverlay(getPoint(Double.valueOf(serviceDispatchCase.getProvider().getLocation().getLatitude()),
                                                    Double.valueOf(serviceDispatchCase.getProvider().getLocation().getLongitude())),
                                            marker,
                                            serviceDispatchCase.getProvider().getLicenseNumber(),
                                            serviceDispatchCase.getProvider().getLicenseNumber(),
                                            null,
                                            Utility.FOLLOW_ON_THE_MAP,
                                            true);

                                    mMap.animateCamera(CameraUpdateFactory.newLatLng(getPoint(Double.valueOf(serviceDispatchCase.getProvider().getLocation().getLatitude()),
                                            Double.valueOf(serviceDispatchCase.getProvider().getLocation().getLongitude()))));
//									controller.animateTo(getPoint(Double.valueOf(serviceDispatchCase.getProvider().getLocation().getLatitude()),
//				                                                  Double.valueOf(serviceDispatchCase.getProvider().getLocation().getLongitude())));
                                }

                                Drawable markerMyLocation = getResources().getDrawable(R.drawable.map_pin);

                                AddOverlay(getPoint(),
                                        markerMyLocation,
                                        addressMyLocation,
                                        (new StringBuilder().append(addressMyLocation.getStreetName())
                                                .append(addressMyLocation.getHouseNumber() == null ? "" : ", " + addressMyLocation.getHouseNumber())).toString(),
                                        (new StringBuilder().append(addressMyLocation.getDistrict() == null ? "" : addressMyLocation.getDistrict()).append(" - ")
                                                .append(addressMyLocation.getCity()).append("/")
                                                .append(addressMyLocation.getState())).toString(),
                                        Utility.FOLLOW_ON_THE_MAP,
                                        true);
                            }
                        }
                    });

                    if (!isFinishing()) {
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {
                            //Caso a thread esteja em execucao e a tela for fechada, ira gerar um erro
                        }
                    }
                }
            }

        } catch (final Exception e) {

            ErrorHelper.setErrorMessage(e);

            runOnUiThread(new Runnable() {
                public void run() {

                    setMessage(getString(R.string.Error), getString(R.string.ErrorMessage), true);
                }
            });
        }
    }

    private void AddOverlay(LatLng loc, Drawable defaultMarker, Object data, String title, String snippet, int typeScreenDetails, boolean showBallow) {

        Marker tempMarker = mMap.addMarker(new MarkerOptions().position(loc).title(title).snippet(snippet));

        if (showBallow) {
            tempMarker.showInfoWindow();
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f));

    }

    private LocationManager getLocationManager(){
        return (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }

    private LatLng getPoint() {

        if (mLocation != null) {

            LatLng point = new LatLng((int)(mLocation.getLatitude()),
                    (int)(mLocation.getLongitude()));
            return point;

        } else
            return null;
    }

    private LatLng getPoint(double latitude, double longitude) {

        LatLng point = new LatLng(latitude, longitude);
        return point;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACTION_LOCATION_SOURCE_SETTINGS:

                GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

                    public void onMyLocationChange(Location location) {
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        mMarker = mMap.addMarker(new MarkerOptions().position(loc));
                        if(mMap != null){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f));
                            mMap.clear();

                            mLocation = location;
                        }
                    }
                };
                mMap.setOnMyLocationChangeListener(myLocationChangeListener);

                refresh = true;
                start();
                break;
        }
    }

	private void setMessage(final String title, final String message,
			final boolean finishActivity) {

		try {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					ScreenMapFollowProvider.this);
			dialog.setTitle(title);
			dialog.setMessage(message);
			dialog.setNeutralButton(R.string.OK,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							if (finishActivity)
								finish();
						}
					});
			dialog.show();
		} catch (Exception e) {
			// Caso a thread esteja em execucao e a tela for fechada, ira gerar
			// um erro
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		refresh = false;
//		GoogleAnalytics.stopAnalyticsTracker();
	}


    @Override
    protected void onResume() {
        super.onResume();

        Log.i("Google Analytics: ", "Assistência Automotiva");
        mTracker.setScreenName("Assistência Automotiva");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        CustomApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomApplication.activityPaused();
    }
}
