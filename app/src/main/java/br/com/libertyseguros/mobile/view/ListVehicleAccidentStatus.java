package br.com.libertyseguros.mobile.view;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ListVehicleAccidentStatusAdpater;
import br.com.libertyseguros.mobile.controller.ListVehicleAccidentStatusController;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;

public class ListVehicleAccidentStatus extends BaseActionBar implements ListVehicleAccidentStatusAdpater.ListListener{

    private ListVehicleAccidentStatusController listVehicleAccidentController;

    private ListView lvVehicleAccident;

    private Dialog dialogMessageTwoButton;

    private Dialog dialogMessage;

    private TextView tvMessageDialog;

    private LinearLayout llProgress;

    private boolean value;

    private TextView tvTitle;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_list_vehicle_accident_status);

        setTitle(getString(R.string.title_action_bar_9));

        mFirebaseAnalytics.setCurrentScreen(ListVehicleAccidentStatus.this, "Acompanhar Sinistro", null);

        llProgress = (LinearLayout) findViewById(R.id.ll_loading);

        lvVehicleAccident = (ListView) findViewById(R.id.lv_vehicle_accident);
        lvVehicleAccident.setClickable(false);


        listVehicleAccidentController = new ListVehicleAccidentStatusController(new OnConnectionResult() {
            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (listVehicleAccidentController.getTypeError() == 1) {
                                dialogMessageTwoButton.show();
                            } else {
                                tvMessageDialog.setText(listVehicleAccidentController.getMessage().getMessage());
                                dialogMessage.show();
                            }
                        }catch(Exception ex){
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
                            lvVehicleAccident.setAdapter(new ListVehicleAccidentStatusAdpater(ListVehicleAccidentStatus.this, listVehicleAccidentController.getVehicleAccidentStatusBeans(), ListVehicleAccidentStatus.this));
                            showLoading(false);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                    }
                });
            }
        });

        configDialog();


        Bundle extras = getIntent().getExtras();

        String morePolicy = null;

        if(extras != null) {
            morePolicy =   extras.getString("morePolicy");
            String va = extras.getString("vehicleAccident");

        }

        tvTitle = (TextView) findViewById(R.id.tv_title);

        showLoading(true);

        listVehicleAccidentController.getList(ListVehicleAccidentStatus.this);

    }

    /**
     * Config Dialogs
     */
    private void configDialog(){
        dialogMessage = new Dialog(this,R.style.AppThemeDialog);
        dialogMessage.setCancelable(false);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                finish();

            }
        });


        dialogMessageTwoButton = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogMessageTwoButton.setCancelable(false);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();

                finish();
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);
                listVehicleAccidentController.getList(ListVehicleAccidentStatus.this);

            }
        });

    }

    /* Show progress loading
    *
    * @param v
    * @param m
    */
    private void showLoading(boolean v) {
        this.value = v;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    llProgress.setVisibility(View.VISIBLE);
                    lvVehicleAccident.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.GONE);
                } else {
                    llProgress.setVisibility(View.GONE);
                    lvVehicleAccident.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void OnClickAddPhoto(View v, int position){

        listVehicleAccidentController.goToUploadPictures(position);
    }

}
