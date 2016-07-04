package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenAccreditedEstablishmentDetails extends Activity {

	private TextView viewScreenName;
	private TextView  viewGarageName;
	private TextView viewAddress;
	private TextView viewDistance;
	private TextView viewPhoneNumber;
	private ImageView imgCall;
	
	private String phoneArea;
	private String phoneNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_accredited_establishment_details);
		
//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);
		
		Initialize();
		Events();
	}

	private void Initialize() {

		Intent intent = getIntent();
				
		viewScreenName = (TextView)findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenAccreditedEstablishmentDetails);
		
		viewGarageName = (TextView)findViewById(R.id.viewGarageName);
		viewGarageName.setText(intent.getExtras().getString("GARAGE"));
		
		viewAddress = (TextView)findViewById(R.id.viewAddress);
		viewAddress.setText(intent.getExtras().getString("ADDRESS"));
		
		viewDistance = (TextView)findViewById(R.id.viewDistance);
		viewDistance.setText(intent.getExtras().getString("DISTANCE"));
		
		viewPhoneNumber = (TextView)findViewById(R.id.viewPhoneNumber);
		viewPhoneNumber.setText(intent.getExtras().getString("PHONE"));
		
		phoneArea = intent.getExtras().getString("PHONE_AREA");
		phoneNumber = intent.getExtras().getString("PHONE_NUMBER");
		
		imgCall = (ImageView)findViewById(R.id.imgCall);
	}

	private void Events() {
		
		imgCall.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				StringBuilder phone = new StringBuilder();

				if (phoneArea != null &&
						phoneArea != "" &&
						phoneArea != "0") {

					phone.append("0");
					phone.append(Utility.PHONE_OPERATOR);
					phone.append(phoneArea);
				}
				phone.append(phoneNumber);

				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.toString()));
				startActivity(intent);
			}
		});
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
