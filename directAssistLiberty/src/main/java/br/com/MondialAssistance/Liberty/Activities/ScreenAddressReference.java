package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenAddressReference extends Activity {

	private TextView viewScreenName;
	private EditText editReference;
	private Button btnOK;
	private RelativeLayout addressBackHeaderButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_address_reference);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View)findViewById(R.id.screenAddress_Header);
		addressBackHeaderButton = (RelativeLayout)headerView.findViewById(R.id.btnBack);
		//btnOK = (Button)headerView.findViewById(R.id.btnEdit);
		//btnOK.setVisibility(View.GONE);
		//btnOK.setBackgroundResource(R.drawable.assistencia_novlocalref_btn_save);
		//addressBackHeaderButton.setBackgroundResource(R.drawable.assistencia_nova_localmap_btn_nova);
		//addressBackHeaderButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		String reference = (intent.getExtras() == null) ? "" : intent.getExtras().getString("ADDRESSREFERENCE");

		viewScreenName = (TextView)findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenAddressReference);

		editReference = (EditText)findViewById(R.id.editReference);
		editReference.setText(reference);

		//btnOK = (Button)findViewById(R.id.btnOK);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void Events() {

		addressBackHeaderButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		/*btnOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("ADDRESSREFERENCE", editReference.getText().toString());

				setResult(1, intent);
				btnOK.setVisibility(View.GONE);
				finish();
			}
		});*/
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