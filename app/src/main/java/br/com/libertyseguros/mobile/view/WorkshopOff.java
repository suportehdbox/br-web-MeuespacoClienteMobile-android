package br.com.libertyseguros.mobile.view;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.WorkshopController;
import br.com.libertyseguros.mobile.controller.WorkshopOffController;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.MaskEditText;

public class WorkshopOff extends BaseActionBar implements View.OnClickListener{

    private WorkshopOffController workshopOffController;

    private ButtonViewCustom btLocation;

    private EditText et_cep;

    private EditText et_address;

    private Toast toast;


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_workshop_off);

        setTitle(getString(R.string.title_action_bar_1));

        mFirebaseAnalytics.setCurrentScreen(this, "Busca de Oficinas", null);

        workshopOffController = new WorkshopOffController(this);

        et_cep = (EditText) findViewById(R.id.et_cep);
        et_cep.addTextChangedListener(MaskEditText.insert("#####-###", et_cep));
        et_cep.setHint(getResources().getString(R.string.hint_cep));

        et_address = (EditText) findViewById(R.id.et_address);
        et_address.setHint(getResources().getString(R.string.hint_address));
        et_address.setOnEditorActionListener(
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



        btLocation = (ButtonViewCustom) findViewById(R.id.bt_location);
        btLocation.setOnClickListener(this);


        try{

            String id = "";

            id = getIntent().getExtras().getString("changemessage");

            TextView tvMessage = (TextView) findViewById(R.id.tv_message);

            if(id.equals("1") || id.equals("0")){
                tvMessage.setText(getString(R.string.message_no_workshop));
            } else if(id.equals("2")){
                tvMessage.setText(getString(R.string.message_no_workshop));

            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * Override method onClick
     * * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_location:
                openSearchLocation();
                break;
        }
    }

    /**
     * Open Search Location
     */
    private void openSearchLocation(){
        if(et_cep.getText().length() > 0 || et_address.getText().length() > 0){
            WorkshopController.setCep(et_cep.getText().toString());
            WorkshopController.setAddress(et_address.getText().toString());
            WorkshopController.setUpdateWorkshop(true);
            finish();
        } else {
            if(toast != null){
                toast.cancel();
            }

            toast = Toast.makeText(this, getString(R.string.message_no_cep_address), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Override method onBackPressed
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        WorkshopController.setCloseWorkshop(true);

    }

    /**
     * Override method onOptionsItemSelected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            WorkshopController.setCloseWorkshop(true);
            super.onBackPressed();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
