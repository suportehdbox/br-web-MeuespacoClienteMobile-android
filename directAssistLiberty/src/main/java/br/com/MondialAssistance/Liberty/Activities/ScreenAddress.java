package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenAddress extends Activity {

	private TextView viewScreenName;
	private TextView editAddress;
	private EditText editHouseNumber;
	//private EditText editComplement;
	private EditText editDistrict;
	private TextView editCity;
	private TextView editState;
	//private EditText editZip;
	private Button btnOK;
	private RelativeLayout screenAddBackButton;
	private double Latitude;
	private double Longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_address);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View) findViewById(R.id.address_Header);
		screenAddBackButton = (RelativeLayout)headerView.findViewById(R.id.btnBack);		
		//screenAddBackButton.setVisibility(View.VISIBLE);
		viewScreenName = (TextView)findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenAddress);
		Intent intent = getIntent();
		editAddress = (TextView)findViewById(R.id.editAddress);
		editAddress.setText(intent.getStringExtra("ADDRESS"));

		editHouseNumber = (EditText)findViewById(R.id.editHouseNumber);
		editHouseNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
		editHouseNumber.setText(intent.getStringExtra("HOUSENUMBER"));

		/*editComplement = (EditText)findViewById(R.id.editComplement);
		editComplement.setText(intent.getStringExtra("COMPLEMENT"));

		editDistrict = (EditText)findViewById(R.id.editDistrict);
		editDistrict.setText(intent.getStringExtra("DISTRICT"));*/

		editCity = (TextView)findViewById(R.id.editCity);
		editCity.setText(intent.getStringExtra("CITY"));

		editState = (TextView)findViewById(R.id.editState);
		editState.setText(intent.getStringExtra("STATE"));

		//editZip = (EditText)findViewById(R.id.editZip);
		//editZip.setText(intent.getStringExtra("ZIP"));

		//btnOK = (Button)findViewById(R.id.btnOK);
		btnOK = (Button)findViewById(R.id.btnAddEdit);		
		btnOK.setVisibility(View.VISIBLE);
		Latitude = intent.getDoubleExtra("LATITUDE", 0);
		Longitude = intent.getDoubleExtra("LONGITUDE", 0);
		//Log.e("","lattitude and longitude =="+Latitude+"---- "+Longitude);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void Events() {
		screenAddBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				//screenAddBackButton.setVisibility(View.GONE);
			}
		});
		btnOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btnOK.setVisibility(View.GONE);
				Close();
			}
		});

		/*editComplement.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {

					switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:

							Close();

							return true;
					}
				}
				return false;
			}
		});*/
	}
	
	private void Close(){

		Intent intent = new Intent();

		intent.putExtra("ADDRESS", editAddress.getText().toString());
		intent.putExtra("HOUSENUMBER", editHouseNumber.getText().toString());
		//intent.putExtra("COMPLEMENT", editComplement.getText().toString());
		//intent.putExtra("DISTRICT", editDistrict.getText().toString());
		intent.putExtra("CITY", editCity.getText().toString());
		intent.putExtra("STATE", editState.getText().toString());
		//intent.putExtra("ZIP", editZip.getText().toString());
		intent.putExtra("LATITUDE", Latitude);
		intent.putExtra("LONGITUDE", Longitude);

		setResult(1, intent);

		finish();
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