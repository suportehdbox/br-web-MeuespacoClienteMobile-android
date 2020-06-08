package br.com.libertyseguros.mobile.view.fragment;

import android.app.Dialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.receiver.ServiceFirebaseMessagingService;
import br.com.libertyseguros.mobile.util.AnalyticsApplication;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.controller.HomeOffController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.NotificationSnackBar;

public class HomeOff extends SupportMapFragment implements OnMapReadyCallback, View.OnClickListener, NavigationApplication.NavigationListener {

    public static boolean isHomeOff;

    private SupportMapFragment mSupportMapFragment;

    private ImageViewCustom ivWorkshop;

    private HomeOffController homeOffController;

    private View root;

    private GoogleMap map;

    private RelativeLayout rlMap;

    private RelativeLayout rlLogo;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private TextView tvMessage;

    private TextView tvName;

    private TextView tvAddress;

    private TextView tvPhone;

    private TextView tvKm;

    private TextView tvOperation;

    private ImageViewCustom ivRoute;

    private ImageViewCustom ivAssistence;

    private ButtonViewCustom btLogin;

    private ButtonViewCustom btRegister;

    private ImageViewCustom ivClub;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private boolean value;

    private String message;

    private FirebaseAnalytics mFirebaseAnalytics;

    public static HomeOff newInstance(ManagerLocation managerLocation) {

        HomeOff home = new HomeOff();
        home.setManagerLocation(managerLocation);

        return home;
    }


    public HomeOff() {

    }

    public void setManagerLocation(ManagerLocation managerLocation) {


        homeOffController = new HomeOffController(managerLocation, new OnConnectionResult() {
            @Override
            public void onError() {

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dialogMessageTwoButton.show();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess() {
                showLoading(false, "");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (homeOffController.getArrayWorkshop().size() < 1) {
                                showDialogMessage(getString(R.string.message_no_workshop));
                                showMap(false);
                            }

                            for (int ind = 0; ind < homeOffController.getArrayWorkshop().size(); ind++) {
                                LatLng latLng = new LatLng(Double.parseDouble(homeOffController.getArrayWorkshop().get(ind).getLatitude()),
                                        Double.parseDouble(homeOffController.getArrayWorkshop().get(ind).getLongitude()));

                                if (ind == 0) {
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(latLng).zoom(Config.ZOOMDEFAULT).build();

                                    map.animateCamera(CameraUpdateFactory
                                            .newCameraPosition(cameraPosition));

                                    tvName.setText(homeOffController.getArrayWorkshop().get(ind).getName());


                                    tvAddress.setText(homeOffController.getArrayWorkshop().get(ind).getAddress().replace(" - ", "\n"));

                                    tvPhone.setText(homeOffController.getArrayWorkshop().get(ind).getPhone());

                                    tvKm.setText(homeOffController.getArrayWorkshop().get(ind).getDistance() + " " + getString(R.string.distance_unity));

                                    tvOperation.setText(homeOffController.createOperation(ind));
                                }


                                map.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(homeOffController.getArrayWorkshop().get(ind).getName()));
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationApplication nav = (NavigationApplication) getActivity().getApplication();
        nav.setNavigationListener(this);
        ServiceFirebaseMessagingService.setOnNotificationListener(nav.getListener());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        isHomeOff = true;

        root = inflater.inflate(R.layout.fragment_home_off, null, false);

        configDialog();

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();

        mFirebaseAnalytics = application.getFirebaseAnalitycs();
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Home Deslogado", null);

        llContent = (LinearLayout) root.findViewById(R.id.ll_content);

        llLoading = (LinearLayout) root.findViewById(R.id.ll_loading);

        tvMessage = (TextView) root.findViewById(R.id.tv_message);

        tvName = (TextView) root.findViewById(R.id.tv_name);

        tvAddress = (TextView) root.findViewById(R.id.tv_address);

        tvPhone = (TextView) root.findViewById(R.id.tv_phone);
        tvPhone.setOnClickListener(this);


        tvKm = (TextView) root.findViewById(R.id.tv_km);

        ivRoute = (ImageViewCustom) root.findViewById(R.id.ib_route);
        ivRoute.setOnClickListener(this);

        ivAssistence = (ImageViewCustom) root.findViewById(R.id.ib_sinistro);
        ivAssistence.setOnClickListener(this);

        btLogin = (ButtonViewCustom) root.findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

        ivClub = (ImageViewCustom) root.findViewById(R.id.iv_club);
        ivClub.setOnClickListener(this);

        btRegister = (ButtonViewCustom) root.findViewById(R.id.bt_register);
        btRegister.setOnClickListener(this);

        rlLogo = (RelativeLayout) root.findViewById(R.id.rl_logo);
        rlMap = (RelativeLayout) root.findViewById(R.id.rl_map);
        initilizeMap();

        ivWorkshop = (ImageViewCustom) root.findViewById(R.id.iv_workshop);
        ivWorkshop.setOnClickListener(this);

        tvOperation = (TextView) root.findViewById(R.id.tv_operation);

        controlLocation();

        homeOffController.getMangagerLocation().setOnLocationDialog(new ManagerLocation.OnLocationDialog() {
            @Override
            public void click(boolean yes) {
                controlLocation();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();

        android.app.Fragment fragment = getActivity().getFragmentManager()
                .findFragmentById(R.id.mapwhere);
        if (null != fragment) {
            android.app.FragmentTransaction ft = getActivity()
                    .getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }

    private void initilizeMap() {
        mSupportMapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapwhere);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mSupportMapFragment = SupportMapFragment.newInstance();
        fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.getUiSettings().setScrollGesturesEnabled(false);

                }
            });

        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_example) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap map) {

    }

    /**
     * On Click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_workshop:
                homeOffController.openWorkshop(getActivity());
                break;
            case R.id.tv_phone:
                homeOffController.openCall(getActivity());
                break;
            case R.id.ib_route:
                homeOffController.openRoute(getActivity());
                break;
            case R.id.ib_sinistro:
                homeOffController.openAssistance(getContext());
                break;
            case R.id.bt_login:
                homeOffController.openLogin(getActivity());
                break;
            case R.id.bt_register:
                homeOffController.openRegister(getActivity());
                break;
            case R.id.iv_club:
                homeOffController.openClub(getActivity());
                break;
        }
    }

    /**
     * Control return location
     */
    private void controlLocation() {
        if (homeOffController.getMangagerLocation().isLocation()) {
            showMap(false);
        } else {
            showMap(true);

            if (homeOffController.getMangagerLocation().getLatLng() != null) {
                showLoading(true, getString(R.string.message_waiting_workshop));
                homeOffController.getWorkshop(getActivity());
            } else {
                showLoading(true, getString(R.string.message_waiting_location));

                homeOffController.getMangagerLocation().setTimeOut();

                homeOffController.getMangagerLocation().setOnLocationController(new ManagerLocation.OnLocationController() {
                    @Override
                    public void locationChange(double latitude, double longitude) {

                    }

                    @Override
                    public void locationError() {
                        showDialogMessage(getString(R.string.message_location_error));
                    }

                    @Override
                    public void locationTimeOut() {
                        showMap(false);
                        showLoading(false, "");
                        showDialogMessage(getString(R.string.message_location_error));
                    }

                    @Override
                    public void location(LatLng latLng) {
                        showLoading(true, getString(R.string.message_waiting_workshop));
                        homeOffController.getWorkshop(getActivity());
                    }
                });
            }
        }
    }

    /**
     * Show progress loading
     *
     * @param v
     * @param m
     */
    private void showLoading(boolean v, String m) {
        this.value = v;
        this.message = m;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    llLoading.setVisibility(View.VISIBLE);
                    llContent.setVisibility(View.GONE);
                    tvMessage.setText(message);
                } else {
                    llLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    /**
     * Config dialog message location
     */
    private void configDialog() {

        dialogMessage = new Dialog(getActivity(), R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });


        dialogMessageTwoButton = new Dialog(getActivity(), R.style.AppThemeDialog);

        dialogMessageTwoButton.setCancelable(false);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(false, "");
                dialogMessageTwoButton.dismiss();
                showMap(false);
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true, getString(R.string.message_waiting_workshop));
                homeOffController.getWorkshop(getActivity());
            }
        });

    }

    /**
     * Show Map
     *
     * @param v
     */
    private void showMap(boolean v) {
        this.value = v;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    rlLogo.setVisibility(View.GONE);
                    rlMap.setVisibility(View.VISIBLE);
                } else {
                    rlLogo.setVisibility(View.VISIBLE);
                    rlMap.setVisibility(View.GONE);
                }
            }
        });

    }


    /**
     * Show dialog message
     *
     * @param m
     */
    private void showDialogMessage(String m) {

        this.message = m;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMessageDialog.setText(message);
                dialogMessage.show();
            }
        });


    }

    @Override
    public void OnDataPlanChange(boolean freeNavigation) {


        String msg = getString(R.string.free_navigation);
        if (!freeNavigation) {
            //msg = getString(R.string.paid_navigation);
            return;
        }
        CoordinatorLayout Clayout = (CoordinatorLayout) root.findViewById(R.id.snackbarlocation);

        Snackbar snackbar = Snackbar.make(Clayout, msg, Snackbar.LENGTH_SHORT);
        snackbar.setDuration(4000);
        snackbar.show();
    }

    @Override
    public void OnNotificationReceived(String message) {
        CoordinatorLayout Clayout = (CoordinatorLayout) root.findViewById(R.id.snackbarlocation);
        NotificationSnackBar.showNotification(getContext().getApplicationContext(), Clayout, message);
    }

}