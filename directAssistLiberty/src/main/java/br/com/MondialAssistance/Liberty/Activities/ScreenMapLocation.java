package br.com.MondialAssistance.Liberty.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAddressFinder;
import br.com.MondialAssistance.DirectAssist.MDL.AddressLocation;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenMapLocation extends FragmentActivity implements Runnable {

	private boolean EditLocation = false;
	private LatLng point;
	private Thread thread;

	private GoogleMap mMap;
	private Marker mMarker;

	private Button btnAction;
	private Button btnConfirm;
	private RelativeLayout headerNovaButton;
	private Location mLocation;
	private TextView viewScreenName;
	private ProgressDialog progress;
	private ProgressDialog progressLocation;
	private ZoomControls zoomControls;
	private ImageView imgCenterPoint;

    AddressLocation mAddressLocation;
    private boolean didZoom = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_map_location);

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
		View headerView = (View) findViewById(R.id.screenLocation_Header);
		headerNovaButton =(RelativeLayout) headerView.findViewById(R.id.btnBack);		
		//headerNovaButton.setVisibility(View.VISIBLE);
		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenMapLocation);

		// viewHelper = (TextView)findViewById(R.id.viewHelper);

		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnAction = (Button) findViewById(R.id.btnAction);

		imgCenterPoint = (ImageView) findViewById(R.id.imgCenterPoint);

		zoomControls = (ZoomControls) findViewById(R.id.zoomControls1);

		Bundle params = getIntent().getExtras();

		if (params.getInt("TYPE") == 1) {

			Drawable marker = getResources().getDrawable(
					R.drawable.map_tracking);
			AddressLocation addressLocation = new AddressLocation();

			addressLocation.setStreetName(params.getString("ADDRESS"));
			addressLocation.setHouseNumber(params.getString("HOUSENUMBER"));
			addressLocation.setComplement(params.getString("COMPLEMENT"));
			addressLocation.setDistrict(params.getString("DISTRICT"));
			addressLocation.setCity(params.getString("CITY"));
			addressLocation.setState(params.getString("STATE"));
			addressLocation.setZip(params.getString("ZIP"));
			addressLocation.setLatitude(params.getDouble("LATITUDE"));
			addressLocation.setLongitude(params.getDouble("LONGITUDE"));

			AddOverlay(
					getPoint(addressLocation.getLatitude(),
							addressLocation.getLongitude()),
					marker,
					addressLocation,
					(new StringBuilder().append(addressLocation.getStreetName()).append(addressLocation
							.getHouseNumber() == null ? "" : ", "
							+ addressLocation.getHouseNumber())).toString(),
					(new StringBuilder()
							.append(addressLocation.getDistrict() == null ? ""
									: addressLocation.getDistrict())
							.append(" - ").append(addressLocation.getCity())
							.append("/").append(addressLocation.getState()))
							.toString(), Utility.ADDRESS_DETAILS, true);

		} else {

			String provider = Settings.Secure.getString(getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (provider == null || provider.length() == 0
					|| !provider.contains(LocationManager.GPS_PROVIDER)) {

				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle(R.string.Location);
				dialog.setMessage(R.string.LocationActivate);
				dialog.setPositiveButton(R.string.Yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(intent,
										Utility.ACTION_LOCATION_SOURCE_SETTINGS);
							}
						});
				dialog.setNegativeButton(R.string.No,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								finish();
							}
						});
				dialog.show();

			} else {

                startLocationListener();

			}
		}
	}

    private void startLocationListener() {
        if(mMap != null) {
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMyLocationEnabled(true);
            mLocation = mMap.getMyLocation();
            LatLng myLocation = null;
            if (mLocation != null) {
                myLocation = new LatLng(mLocation.getLatitude(),
                        mLocation.getLongitude());
            }
            if(mMap != null && myLocation != null) {
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
                        myAddress();
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

                        if (thread != null && !thread.isAlive()){

                            thread = new Thread(ScreenMapLocation.this);
                            thread.start();
                        }

                        if (progressLocation != null) {
                            progressLocation.dismiss();
                        }
                    }
                }
            };
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        }
    }

    void myAddress() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    BLLAddressFinder addressFinder = new BLLAddressFinder();
                    mAddressLocation = addressFinder.getAddress(mLocation.getLatitude(), mLocation.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

	private void Events() {
		headerNovaButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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


		btnConfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				try {
                    if (EditLocation == false) {

                        AddressLocation addressLocation = mAddressLocation;
                        Intent intent = new Intent();

                        intent.putExtra("ADDRESS", addressLocation.getStreetName());
                        intent.putExtra("HOUSENUMBER", addressLocation.getHouseNumber());
                        intent.putExtra("COMPLEMENT", addressLocation.getComplement());
                        intent.putExtra("DISTRICT", addressLocation.getDistrict());
                        intent.putExtra("CITY", addressLocation.getCity());
                        intent.putExtra("STATE", addressLocation.getState());
                        intent.putExtra("ZIP", addressLocation.getZip());
                        intent.putExtra("LATITUDE", addressLocation.getLatitude());
                        intent.putExtra("LONGITUDE", addressLocation.getLongitude());

                        setResult(1, intent);

                        finish();

                    } else {

                        int centerX = (int) (imgCenterPoint.getX() + imgCenterPoint.getWidth()  / 2);
                        int centerY = (int) (imgCenterPoint.getY() + imgCenterPoint.getHeight() / 2);
                        Point tempPoint = new Point(centerX,centerY);

                        point = mMap.getProjection().fromScreenLocation(tempPoint);
                        start();
                    }
				} catch (Exception e) {

					ErrorHelper.setErrorMessage(e);
					setMessage(getString(R.string.Error),
							getString(R.string.ErrorMessage), false);

				}
			}
		});

		btnAction.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (EditLocation == true) {

                    Intent intent = new Intent(ScreenMapLocation.this,
                            ScreenSearchAddress.class);
                    intent.putExtra("TYPE", Utility.ADDRESS_DETAILS);

                    startActivityForResult(intent,
                            Utility.SCREEN_SEARCH_ADDRESS);

                } else
                    setLayout(true);
            }
        });
	}

	private void start() {

		progress = ProgressDialog.show(ScreenMapLocation.this,
				getText(R.string.Wait), getText(R.string.SearchInformation),
				false, false);

		thread = new Thread(this);
		thread.start();
	}

    public void run() {

        try {
            BLLAddressFinder addressFinder = new BLLAddressFinder();
            final AddressLocation addressLocation;

            final Drawable marker = getResources().getDrawable(R.drawable.map_tracking);
            double latitude = 0;
            double longitude = 0;

            if (EditLocation == true) {

                latitude = point.latitude;
                longitude = point.longitude;

            } else {

                point = getPoint();

                latitude = point.latitude;
                longitude = point.longitude;
            }

            if ((latitude != 0 && longitude != 0) || EditLocation == true) {

                addressLocation = addressFinder.getAddress(latitude, longitude);

                runOnUiThread(new Runnable() {
                    public void run() {

                        AddOverlay(point,
                                marker,
                                addressLocation,
                                (new StringBuilder().append(addressLocation.getStreetName())
                                        .append(addressLocation.getHouseNumber() == null ? "" : ", " + addressLocation.getHouseNumber())).toString(),
                                (new StringBuilder().append(addressLocation.getDistrict() == null ? "" : addressLocation.getDistrict()).append(" - ")
                                        .append(addressLocation.getCity()).append("/")
                                        .append(addressLocation.getState())).toString(),
                                Utility.ADDRESS_DETAILS,
                                true);

                        if (EditLocation == true)
                            setLayout(false);
                    }
                });
            } else {

                runOnUiThread(new Runnable() {

                    public void run() {

                        progressLocation = ProgressDialog.show(ScreenMapLocation.this,
                                getText(R.string.Wait),
                                getText(R.string.WaitLocation),
                                false,
                                true);
                    }
                });
            }

        } catch (final Exception e) {

            ErrorHelper.setErrorMessage(e);

            runOnUiThread(new Runnable() {
                public void run() {

                    setMessage(getString(R.string.Error), getString(R.string.ErrorMessage), false);
                }
            });

        } finally {
            progress.dismiss();
        }
    }

	private void setLayout(boolean editInformations) {
		try{

		if (editInformations == true) {

			btnConfirm.setText(R.string.OK);
			btnAction.setText(R.string.Search);
			// viewHelper.setText(R.string.HelperLabelConfirmPositionMap);

            GoogleMap.OnMyLocationChangeListener myLocationChangeListener = null;

            mMap.clear();

			imgCenterPoint.setVisibility(ImageView.VISIBLE);
			EditLocation = true;

		} else {

			btnConfirm.setText(R.string.Confirm);
			btnAction.setText(R.string.Edit);
			// viewHelper.setText(R.string.HelperLabelMap);

			imgCenterPoint.setVisibility(ImageView.INVISIBLE);
			EditLocation = false;
		}
		}catch (Exception e) {
			// TODO: handle exception
			e.fillInStackTrace();
		}
	}

	private void setMessage(final String title, final String message,
			final boolean finishActivity) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScreenMapLocation.this);
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
		
	}

    private void AddOverlay(LatLng loc, Drawable defaultMarker, Object data, String title, String snippet, int typeScreenDetails, boolean showBallow) {

        mAddressLocation = (AddressLocation)data;
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

	private LocationManager getLocationManager() {
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        Bundle params = (data == null) ? null : data.getExtras();
        Intent intent;

        switch (requestCode) {
            case Utility.SCREEN_SEARCH_ADDRESS:

                if (params != null) {

                    AddressLocation addressLocation = new AddressLocation();

                    addressLocation.setStreetName(params.getString("ADDRESS"));
                    addressLocation.setDistrict(params.getString("DISTRICT"));
                    addressLocation.setCity(params.getString("CITY"));
                    addressLocation.setState(params.getString("STATE"));
                    addressLocation.setZip(params.getString("ZIP"));
                    addressLocation.setLatitude(params.getDouble("LATITUDE"));
                    addressLocation.setLongitude(params.getDouble("LONGITUDE"));

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(getPoint(addressLocation.getLatitude(),
                            addressLocation.getLongitude())));
                }
                break;
            case Utility.ADDRESS_DETAILS:

                if (params != null) {

                    intent = new Intent();
                    intent.putExtras(params);

                    setResult(1, intent);

                    finish();
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
