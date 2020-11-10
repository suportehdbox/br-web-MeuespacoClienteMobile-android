package br.com.libertyseguros.mobile.view;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.WorkshopAdapter;
import br.com.libertyseguros.mobile.beans.WorkshopBeans;
import br.com.libertyseguros.mobile.controller.WorkshopController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.libray.ManagerLocation;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.MaskEditText;

public class Workshop extends BaseActionBar implements View.OnClickListener {

    private SupportMapFragment mSupportMapFragment;

    private WorkshopController workshopController;

    private static ManagerLocation managerLocation;

    private RelativeLayout llContent;

    private LinearLayout llLoading;

    private TextView tvMessage;

    private ListView lvWorkshop;

    private EditText etCep;

    private EditText etAddress;

    private Button ivSearch;

    private Button btChangeSearch;

    private Dialog dialogLocation;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private TextView tvMessageDialog;

    private boolean value;

    private String message;

    private GoogleMap map;

    private Toast toast;

    private Dialog dialogWorkshop;


    public void onResume() {
        super.onResume();

        if (WorkshopController.isUpdateWorkshop()) {
            WorkshopController.setUpdateWorkshop(false);
            showLoading(true, getString(R.string.message_waiting_workshop));

            if (WorkshopController.getCep().length() > 0) {
                workshopController.getWorkshop(this, 1, true);
            } else {
                workshopController.getWorkshop(this, 2, true);
            }
        }

        if (WorkshopController.isCloseWorkshop()) {
            WorkshopController.setCloseWorkshop(false);
                finish();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_workshop);
        setTitle(getString(R.string.title_action_bar_1));

        mFirebaseAnalytics.setCurrentScreen(this, "Busca de oficinas", null);

        llContent = (RelativeLayout) findViewById(R.id.ll_content);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        tvMessage = (TextView) findViewById(R.id.tv_message);

        workshopController = new WorkshopController(this, new OnConnectionResult() {
            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //dialogMessageTwoButton.show();
                            workshopController.OpenWorkshopOff(Workshop.this, true, 2);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSucess() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (workshopController.getArrayWorkshop().size() > 0) {
                                showLoading(false, "");
                                WorkshopAdapter adapter = new WorkshopAdapter(Workshop.this, workshopController.getArrayWorkshop(), workshopController.getManagerLocation().getLatLng());
                                lvWorkshop.setAdapter(adapter);
                                pinMap();

                            } else {
                                workshopController.OpenWorkshopOff(Workshop.this, true, 1);
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });


            }
        }, managerLocation);

        if(managerLocation == null){
            workshopController.getManagerLocation().setOnLocationDialog(new ManagerLocation.OnLocationDialog() {
                @Override
                public void click(boolean yes) {
                    controlLocation();
                }
            });
        } else {
            managerLocation = null;
        }


        ivSearch = (Button) findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(this);

        btChangeSearch = (Button) findViewById(R.id.iv_change_search);
        btChangeSearch.setOnClickListener(this);


        lvWorkshop = (ListView) findViewById(R.id.lv_workshop);

        WorkshopBeans wb = new WorkshopBeans();

        ArrayList<WorkshopBeans> arrayList = new ArrayList<>();
        arrayList.add(wb);
        arrayList.add(wb);
        arrayList.add(wb);
        arrayList.add(wb);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapwhere);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                if (ActivityCompat.checkSelfPermission(Workshop.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Workshop.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                map.setMyLocationEnabled(true);

            }
        });

        pinMap();

        showLoading(false, getString(R.string.message_waiting_location));

        configDialog();

        if(workshopController.getManagerLocation().getLatLng() != null){
            showLoading(true, getString(R.string.message_waiting_workshop));
            workshopController.getWorkshop(this, 0, true);
        } else {

            if(workshopController.isManagerNull()){
                showLoading(true, getString(R.string.message_waiting_workshop));
                workshopController.OpenWorkshopOff(this, false, 0);
            } else {
                showLoading(true, getString(R.string.message_waiting_location));

                    if(!workshopController.getManagerLocation().isLocationDisable()){

                        workshopController.getManagerLocation().setOnLocationDialog(new ManagerLocation.OnLocationDialog() {
                            @Override
                            public void click(boolean yes) {
                                controlLocation();
                            }
                        });
                    }
                    else{
                        if(!workshopController.getManagerLocation().isLocation()) {
                            controlLocation();
                        }
                    }
            }

        }


        LoadFile loadFile = new LoadFile();

        /*String dialogWorkshopString = loadFile.loadPref(Config.TAG, this, Config.TAGDIALOGWORKSHOP);

        if(dialogWorkshopString != null){
            if(dialogWorkshopString.equals("")){
                dialogWorkshop.show();
                loadFile.savePref(Config.TAGDIALOGWORKSHOP, "1", Config.TAG, this);
            }
        } else {
            dialogWorkshop.show();
            loadFile.savePref(Config.TAGDIALOGWORKSHOP, "1", Config.TAG, this);
        }*/

        dialogWorkshop.show();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_search:
                dialogLocation.show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Clique");
                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Busca de Oficina");
                bundle.putString(FirebaseAnalytics.Param.CONTENT, "Traçar Rota");

                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                break;
            case R.id.iv_change_search:
                workshopController.changeSearch();

                if(workshopController.isScoreSearch()){
                    btChangeSearch.setText(R.string.workshop_change_search_near);
                }else{
                    btChangeSearch.setText(R.string.workshop_change_search_score);
                }
                showLoading(true, getString(R.string.message_waiting_workshop));

                workshopController.doSearchWorkshops(Workshop.this);
                break;
        }
    }

    protected void onStart() {
        if(!workshopController.isManagerNull()){
            workshopController.getGoogleApiClient().connect();
        }

        super.onStart();
    }

    protected void onStop() {
        if(!workshopController.isManagerNull()){
            workshopController.getGoogleApiClient().disconnect();
        }

        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Log.i(Config.TAG, "PERMISSION_GRANTED - location");

                    if (!workshopController.getManagerLocation().isLocationDisable()) {
                        workshopController.getManagerLocation().setLocation(true);
                        workshopController.getManagerLocation().getDialogLocation().show();
                    } else {
                        workshopController.getManagerLocation().requestLocation();
                        workshopController.getManagerLocation().setLocation(false);
                        workshopController.getManagerLocation().getOnLocationDialog().click(true);
                    }
                } else {
                    //Log.i(Config.TAG, "PERMISSION_DENIED - location");
                    workshopController.getManagerLocation().setLocation(true);
                    workshopController.OpenWorkshopOff(this, false, 0);

                }

                return;
            }
        }

    }

    /**
     * Control return location
     */
    private void controlLocation() {
        if (!workshopController.getManagerLocation().isLocation()) {

            if (workshopController.getManagerLocation().getLatLng() != null) {
                showLoading(true, getString(R.string.message_waiting_workshop));
                workshopController.getWorkshop(this, 0, true);
            } else {
                showLoading(true, getString(R.string.message_waiting_location));

                workshopController.getManagerLocation().setTimeOut();

                workshopController.getManagerLocation().setOnLocationController(new ManagerLocation.OnLocationController() {
                    @Override
                    public void locationChange(double latitude, double longitude) {

                    }

                    @Override
                    public void locationError() {
                        showLoading(true, getString(R.string.message_waiting_workshop));
                        workshopController.OpenWorkshopOff(Workshop.this, false, 0);
                    }

                    @Override
                    public void locationTimeOut() {
                        showLoading(true, getString(R.string.message_waiting_workshop));
                        workshopController.OpenWorkshopOff(Workshop.this, false, 0);
                    }

                    @Override
                    public void location(LatLng latLng) {
                        showLoading(true, getString(R.string.message_waiting_workshop));
                        workshopController.getWorkshop(Workshop.this, 0, true);
                    }
                });
            }
        } else {
            workshopController.OpenWorkshopOff(Workshop.this, false, 0);

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

        runOnUiThread(new Runnable() {
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
     *
     * @param m
     */
    private void showMessage(String m) {
        this.message = m;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llLoading.setVisibility(View.GONE);
                llContent.setVisibility(View.GONE);
                tvMessage.setText(message);
            }
        });

    }

    /**
     * Config dialog message location
     */
    private void configDialog() {

        dialogWorkshop = new Dialog(this, R.style.AppThemeDialog);
        dialogWorkshop.setCancelable(false);
        dialogWorkshop.setContentView(R.layout.dialog_workshop);


        ImageView ivOk = (ImageView) dialogWorkshop.findViewById(R.id.iv_ok);
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWorkshop.dismiss();

            }
        });

        dialogLocation  = new Dialog(this,R.style.AppThemeDialog);

        dialogLocation.setContentView(R.layout.dialog_location_workshop);

        etCep = dialogLocation.findViewById(R.id.et_cep);
        etAddress =  dialogLocation.findViewById(R.id.et_address);
        etCep.setHint(getResources().getString(R.string.hint_address));

        etAddress.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            openSearchLocation();

                            return true;
                        }
                        return false;
                    }
                });

        etCep.addTextChangedListener(MaskEditText.insert("#####-###", etCep));
        etCep.setHint(getResources().getString(R.string.hint_cep));

        Button ivLocation = (Button) dialogLocation.findViewById(R.id.bt_location);
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchLocation();
            }
        });


        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog =  dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk =  dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });


        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workshopController.getArrayWorkshop().size() == 0){
                    finish();
                } else {
                    dialogMessageTwoButton.dismiss();
                    showLoading(false, "");
                }
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true, getString(R.string.message_waiting_workshop));
                workshopController.getWorkshop(Workshop.this, 0, false);
            }
        });

    }

    /**
     * Open Search Location
     */
    private void openSearchLocation(){
        dialogLocation.dismiss();

        if(etCep.getText().length() > 0){
            WorkshopController.setCep(etCep.getText().toString());
            workshopController.getWorkshop(Workshop.this, 1, true);
            showLoading(true, getString(R.string.message_waiting_workshop));

        } else if(etAddress.getText().length() > 0){
            WorkshopController.setAddress(etAddress.getText().toString());
            workshopController.getWorkshop(Workshop.this, 2, true);
            showLoading(true, getString(R.string.message_waiting_workshop));
        } else {
            if(toast != null){
                toast.cancel();
            } else{
                Toast toast = Toast.makeText(Workshop.this, getString(R.string.message_no_cep_address), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    /**
     * Show dialog message
     * @param m
     */
    private void showDialogMessage(String m){

        this.message = m;

         runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMessageDialog.setText(message);
                dialogMessage.show();
            }
        });


    }

    /**
     * Set Manager Location
     * @param value
     */
    public static void setManagerLocation(ManagerLocation value){
       managerLocation = value;
    }

    public void pinMap(){
        for (int ind = 0; ind < workshopController.getArrayWorkshop().size(); ind++){
            LatLng latLng = new LatLng(Double.parseDouble(workshopController.getArrayWorkshop().get(ind).getLatitude()),
                    Double.parseDouble(workshopController.getArrayWorkshop().get(ind).getLongitude()));


            if(ind == 0) {
                map.clear();

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(Config.ZOOMDEFAULT).build();

                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

            }


            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(workshopController.getArrayWorkshop().get(ind).getName()));
        }
    }


}
