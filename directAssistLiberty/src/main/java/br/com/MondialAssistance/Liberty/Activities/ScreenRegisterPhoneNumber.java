package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.BLL.BLLPhone;
import br.com.MondialAssistance.DirectAssist.BLL.BLLPhone.PhoneNumberMask;
import br.com.MondialAssistance.DirectAssist.Util.Client;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenRegisterPhoneNumber extends Activity{

	private PhoneNumberMask phoneNumberMask;
	private boolean openLob;

	private TextView viewScreenName;
	private EditText editNumber;
	private ImageButton btnLess;
	private Button btnNumber1;
	private Button btnNumber2;
	private Button btnNumber3;
	private Button btnNumber4;
	private Button btnNumber5;
	private Button btnNumber6;
	private Button btnNumber7;
	private Button btnNumber8;
	private Button btnNumber9;
	private Button btnNumber0;
	private Button btnSharp;
	private Button btnAsterisk;
	private Button btnSave;
	private RelativeLayout registerBackHeaderButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_register_phone_number);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize(){
		View headerView = (View)findViewById(R.id.registerpNo_Header);
		registerBackHeaderButton = (RelativeLayout)headerView.findViewById(R.id.btnBack);
		//registerBackHeaderButton.setBackgroundResource(R.drawable.btn_cancelar);	
		//registerBackHeaderButton.setVisibility(View.VISIBLE);
		try {

			Bundle params = getIntent().getExtras();
			openLob = params.getBoolean("openLob");

			viewScreenName = (TextView)findViewById(R.id.viewScreenName);
			viewScreenName.setText(R.string.TitleScreenRegisterPhoneNumber);

			editNumber = (EditText)findViewById(R.id.editNumber);
			btnLess = (ImageButton)findViewById(R.id.btnLess);
			btnNumber1 = (Button)findViewById(R.id.btnNumber1);
			btnNumber2 = (Button)findViewById(R.id.btnNumber2);
			btnNumber3 = (Button)findViewById(R.id.btnNumber3);
			btnNumber4 = (Button)findViewById(R.id.btnNumber4);
			btnNumber5 = (Button)findViewById(R.id.btnNumber5);
			btnNumber6 = (Button)findViewById(R.id.btnNumber6);
			btnNumber7 = (Button)findViewById(R.id.btnNumber7);
			btnNumber8 = (Button)findViewById(R.id.btnNumber8);
			btnNumber9 = (Button)findViewById(R.id.btnNumber9);
			btnNumber0 = (Button)findViewById(R.id.btnNumber0);
			btnSharp = (Button)findViewById(R.id.btnSharp);
			btnAsterisk = (Button)findViewById(R.id.btnAsterisk);
			btnSave = (Button)findViewById(R.id.btnSave);

			BLLPhone bllPhone = new BLLPhone(null);
			phoneNumberMask = bllPhone.new PhoneNumberMask();

			String phoneNumber = Client.getPhone(getApplicationContext());
			editNumber.setText(phoneNumberMask.LoadPhoneNumber(phoneNumber));

		} catch (Exception e) {
			ErrorHelper.setErrorMessage(e);
		}
	}

	private void Events(){

		registerBackHeaderButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();	
			}
		});
		btnLess.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.lessNumber());
			}
		});

		btnNumber1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("1"));
			}
		});

		btnNumber2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("2"));
			}
		});

		btnNumber3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("3"));
			}
		});

		btnNumber4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("4"));
			}
		});

		btnNumber5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("5"));
			}
		});

		btnNumber6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("6"));
			}
		});

		btnNumber7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("7"));
			}
		});

		btnNumber8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("8"));
			}
		});

		btnNumber9.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("9"));
			}
		});

		btnNumber0.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("0"));
			}
		});

		btnSharp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("#"));
			}
		});

		btnAsterisk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				editNumber.setText(phoneNumberMask.addNumber("*"));
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				BLLPhone phone = new BLLPhone(getApplicationContext());
				boolean readMsgModifyPhone = phone.modifyPhoneNumberDigit9(editNumber.getText().toString());

				if (phoneNumberMask.ValidatePhoneNumber(readMsgModifyPhone) == true) {

					try {

						phone.SavePhone(editNumber.getText().toString(), readMsgModifyPhone);

						if (openLob == true)
							startActivity(new Intent(ScreenRegisterPhoneNumber.this, ScreenSelectLob.class));

						finish();

					} catch (Exception e) {
						Toast.makeText(ScreenRegisterPhoneNumber.this, ErrorHelper.setErrorMessage(e).getMessage(), Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(ScreenRegisterPhoneNumber.this, R.string.NumberInvalid, Toast.LENGTH_SHORT).show();
				}
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