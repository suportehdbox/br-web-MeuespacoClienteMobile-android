package br.com.MondialAssistance.Liberty.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAddressFinder;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAutomaker;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAutomotive;
import br.com.MondialAssistance.DirectAssist.MDL.AccreditedGarage;
import br.com.MondialAssistance.DirectAssist.MDL.AddressLocation;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenAccreditedEstablishment extends FragmentActivity implements Runnable {

	private int SCREEN_SEARCH_ADDRESS_RESULT = 0;

	Boolean didZoom = false;
	private GoogleMap mMap;

	private RadioButton radMap;
	private RadioButton radList;
	private ProgressDialog progress;
	private ProgressDialog progressLocation;
	private ZoomControls zoomControls;
	private TextView viewScreenName;
	private Location mLocation;
	private ListView listAccreditedEstablishment;
	private TableLayout tableControls;
	private TableRow rowMap;
	private TableRow rowList;
	private Button btnSearch;
	
	private int LobID;
	private Vector<AccreditedGarage> accreditedGarages;
	private AddressLocation addressLocation;
	private LatLng point;
	
	private Thread thread;
	
	private Double minLat;
	private Double maxLat;
	private Double minLon;
	private Double maxLon;
	private Double lat;
	private Double lon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_accredited_establishment);

        setUpMapIfNeeded();
//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);
		
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
        try {
            LobID = intent.getExtras().getInt("LOBID");
        } catch (Exception e) {
            LobID = Utility.AUTOMOTIVE;
        }
		viewScreenName = (TextView)findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenAccreditedEstablishment);
		
		radMap = (RadioButton)findViewById(R.id.radMap);
		radList = (RadioButton)findViewById(R.id.radList);
		listAccreditedEstablishment = (ListView)findViewById(R.id.listAccreditedEstablishment);
		btnSearch = (Button)findViewById(R.id.btnSearch);

		zoomControls = (ZoomControls)findViewById(R.id.zoomControls1);
		
		rowList = (TableRow)findViewById(R.id.rowList);
		rowMap = (TableRow)findViewById(R.id.rowMap);
		
		tableControls = (TableLayout)findViewById(R.id.tableControls);
		tableControls.removeAllViews();
		tableControls.addView(rowMap);
		tableControls.invalidate();
		
		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider == null || provider.length() == 0 || !provider.contains(LocationManager.GPS_PROVIDER)){
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(R.string.Location);
			dialog.setMessage(R.string.LocationActivate);
			dialog.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); 
					startActivityForResult(intent, Utility.ACTION_LOCATION_SOURCE_SETTINGS); 
				}
			});
			dialog.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					finish();
				}
			});
			dialog.show();
			
		} else {

            myLocationStart();
		}
	}

    private void myLocationStart() {
        if (mMap != null) {
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMyLocationEnabled(true);
            mLocation = mMap.getMyLocation();
            LatLng myLocation = null;
            if (mLocation != null) {
                myLocation = new LatLng(mLocation.getLatitude(),
                        mLocation.getLongitude());
            }
            if (mMap != null && myLocation != null) {
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
                    } else {
                        if (changeLat < 1 && changeLng < 1) {
                            return;
                        }
                    }

                    thread = new Thread(ScreenAccreditedEstablishment.this);
                    thread.start();


                    if (progressLocation != null) {
                        progressLocation.dismiss();
                    }
                }
            };
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
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
		
		radMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				tableControls.removeAllViews();
				tableControls.addView(rowMap);
				tableControls.invalidate();
			}
		});
		
		radList.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				tableControls.removeAllViews();
				tableControls.addView(rowList);
				tableControls.invalidate();
			}
		});
		
		listAccreditedEstablishment.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				
				CtrlListviewListAccreditedEstablishmentAdapter adapterListAccreditedEstablishment = (CtrlListviewListAccreditedEstablishmentAdapter)adapter.getAdapter();
				
				Intent intent = new Intent(ScreenAccreditedEstablishment.this, ScreenAccreditedEstablishmentDetails.class);
				
				intent.putExtra("GARAGE", adapterListAccreditedEstablishment.getItem(position).getGarageName());
				intent.putExtra("ADDRESS", adapterListAccreditedEstablishment.getItem(position).getAddress().toString());
				intent.putExtra("DISTANCE", adapterListAccreditedEstablishment.getItem(position).getDistance().toString() + " km");
				intent.putExtra("PHONE", adapterListAccreditedEstablishment.getItem(position).getPhone().toString());
				intent.putExtra("PHONE_AREA", adapterListAccreditedEstablishment.getItem(position).getPhone().getAreaCode());
				intent.putExtra("PHONE_NUMBER", adapterListAccreditedEstablishment.getItem(position).getPhone().getPhoneNumber());
				
				startActivity(intent);
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(ScreenAccreditedEstablishment.this, ScreenSearchAddress.class);
				intent.putExtra("TYPE", Utility.ACCREDITED_ESTABLISHMENT_DETAILS);
				
				startActivityForResult(intent, Utility.SCREEN_SEARCH_ADDRESS);
			}
		});
	}
	
	private void start() {
		
		progress = ProgressDialog.show(ScreenAccreditedEstablishment.this, 
	               getText(R.string.Wait), 
	               getText(R.string.SearchInformation),
	               false, 
	               false);

		thread = new Thread(ScreenAccreditedEstablishment.this);
		thread.start();
	}

    public void run() {

        try {

//                if(mLocation == null){
//                    return;
//                    mLocation = new Location("dummyprovider");
//
//                    mLocation.setLatitude(-23.3);
//                    mLocation.setLongitude(-46.6);
//                }

            final LatLngBounds.Builder builder = new LatLngBounds.Builder();

            accreditedGarages = new Vector<AccreditedGarage>();
            double latitude = 0;
            double longitude = 0;

            if (SCREEN_SEARCH_ADDRESS_RESULT == 0 && mLocation != null) {

                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();

            } else if (addressLocation != null){

                latitude = addressLocation.getLatitude();
                longitude = addressLocation.getLongitude();
            }

            switch (LobID) {
                case Utility.AUTOMOTIVE:
                    BLLAutomotive automotive = new BLLAutomotive();
                    accreditedGarages = automotive.getListAccreditedEstablishment(ScreenAccreditedEstablishment.this, latitude, longitude, ClientParams.ClientID);

                    if (automotive.getAction().getResultCode() != 0) {

                        runOnUiThread(new Runnable() {
                            public void run() {

                                setMessage(getString(R.string.Error), getString(R.string.ErrorMessage), false);
                            }
                        });
                    }

                    break;
                case Utility.AUTOMAKER:
                    BLLAutomaker automaker = new BLLAutomaker();
                    accreditedGarages = automaker.getListNearestEstablishment(ScreenAccreditedEstablishment.this, latitude, longitude, ClientParams.ClientID);

                    if (automaker.getAction().getResultCode() != 0) {

                        runOnUiThread(new Runnable() {
                            public void run() {

                                setMessage(getString(R.string.Error), getString(R.string.ErrorMessage), false);
                            }
                        });
                    }

                    break;
                case Utility.PROPERTY:
                    //Sem funcao
                    break;
            }

            minLat = Double.MAX_VALUE;
            maxLat = Double.MIN_VALUE;
            minLon = Double.MAX_VALUE;
            maxLon = Double.MIN_VALUE;

            if (accreditedGarages.size() != 0) {

                final CtrlListviewListAccreditedEstablishmentAdapter adapterListAccreditedEstablishment = new CtrlListviewListAccreditedEstablishmentAdapter(ScreenAccreditedEstablishment.this, accreditedGarages);


                runOnUiThread(new Runnable() {
                    public void run() {

                        Drawable marker = getResources().getDrawable(R.drawable.contact_pin);
                        listAccreditedEstablishment.setAdapter(adapterListAccreditedEstablishment);

                        mMap.clear();


                        for (AccreditedGarage accreditedGarage : accreditedGarages) {
                            LatLng loc = getPoint(accreditedGarage.getAddress().getLatitude(),
                                    accreditedGarage.getAddress().getLongitude());
                            builder.include(loc);
                            AddOverlay(loc,
                                    marker,
                                    accreditedGarage,
                                    accreditedGarage.getGarageName(),
                                    accreditedGarage.getDistance() + " km",
                                    Utility.ACCREDITED_ESTABLISHMENT_DETAILS,
                                    false);


                            lat = (accreditedGarage.getAddress().getLatitude());
                            lon = (accreditedGarage.getAddress().getLongitude());

                            maxLat = Math.max(lat, maxLat);
                            minLat = Math.min(lat, minLat);
                            maxLon = Math.max(lon, maxLon);
                            minLon = Math.min(lon, minLon);
                        }
                    }
                });
            }

            if (SCREEN_SEARCH_ADDRESS_RESULT == 0) {

                BLLAddressFinder addressFinder = new BLLAddressFinder();
                addressLocation = addressFinder.getAddress(mLocation.getLatitude(),
                        mLocation.getLongitude());
            }

            point = getPoint(addressLocation.getLatitude(),
                    addressLocation.getLongitude());

            runOnUiThread(new Runnable() {
                public void run() {

//						Drawable markerMyLocation = getResources().getDrawable(R.drawable.map_tracking);
//
//						AddOverlay(point,
//								   markerMyLocation,
//								   addressLocation,
//								  (new StringBuilder().append(addressLocation.getStreetName())
//	                                                  .append(addressLocation.getHouseNumber() == null ? "" : ", " + addressLocation.getHouseNumber())).toString(),
//	                              (new StringBuilder().append(addressLocation.getDistrict() == null ? "" : addressLocation.getDistrict()).append(" - ")
//				                                      .append(addressLocation.getCity()).append("/")
//				                                      .append(addressLocation.getState())).toString(),
//								   Utility.ADDRESS,
//								   true);
//
//
//						lat = (addressLocation.getLatitude());
//					    lon = (addressLocation.getLongitude());
//
//						maxLat = Math.max(lat, maxLat);
//				        minLat = Math.min(lat, minLat);
//				        maxLon = Math.max(lon, maxLon);
//				        minLon = Math.min(lon, minLon);

                    LatLngBounds bounds = builder.build();
                    int padding = 5;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    mMap.animateCamera(cu);

                }
            });

            SCREEN_SEARCH_ADDRESS_RESULT = 0;


        } catch (Exception e) {

            ErrorHelper.setErrorMessage(e);

            runOnUiThread(new Runnable() {
                public void run() {

                    setMessage(getString(R.string.Error), getString(R.string.ErrorMessage), true);
                }
            });

        } finally {
            if(progress != null) {
                progress.dismiss();
            }
        }
    }
	
	private void setMessage(final String title, final String message, final boolean finishActivity) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(ScreenAccreditedEstablishment.this);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNeutralButton(R.string.OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				if (finishActivity)
					finish();
			}
		});
		dialog.show();
	}

    private void AddOverlay(LatLng loc, Drawable defaultMarker, Object data, String title, String snippet, int typeScreenDetails, boolean showBallow) {

        Bitmap bitmap = ((BitmapDrawable) defaultMarker).getBitmap();
        Marker tempMarker = mMap.addMarker(new MarkerOptions().position(loc).title(title).snippet(snippet).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        if (showBallow) {
            tempMarker.showInfoWindow();
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f));

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
            case Utility.ACTION_LOCATION_SOURCE_SETTINGS:

                myLocationStart();

                break;
            case Utility.SCREEN_SEARCH_ADDRESS:

                Bundle params = (data == null) ? null : data.getExtras();

                if (params != null) {

                    addressLocation = new AddressLocation();

                    addressLocation.setStreetName(params.getString("ADDRESS"));
                    addressLocation.setDistrict(params.getString("DISTRICT"));
                    addressLocation.setCity(params.getString("CITY"));
                    addressLocation.setState(params.getString("STATE"));
                    addressLocation.setZip(params.getString("ZIP"));
                    addressLocation.setLatitude(params.getDouble("LATITUDE"));
                    addressLocation.setLongitude(params.getDouble("LONGITUDE"));

                    SCREEN_SEARCH_ADDRESS_RESULT = resultCode;

                    start();
                }
                break;
        }
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		GoogleAnalytics.stopAnalyticsTracker();
	}
    @Override
    protected void onResume() {
        super.onResume();
        CustomApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomApplication.activityPaused();
    }
}