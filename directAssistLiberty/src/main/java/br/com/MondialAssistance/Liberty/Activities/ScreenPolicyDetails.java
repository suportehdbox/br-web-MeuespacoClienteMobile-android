package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.BLL.BLLDirectAssist;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.DirectAssist.Util.Client;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenPolicyDetails extends Activity implements Runnable {

	private TableLayout layoutPolicyDetails;
	private TextView viewScreenName;
	private TextView viewFieldLeg1;
	private TextView viewFieldLeg2;
	private TextView viewFieldLeg3;
	private TextView viewField1;
	private TextView viewField2;
	private TextView viewField3;
	private TextView viewPolicy;
	private TextView viewName;
	private Button btnSave;
	private TableRow row1;
	private TableRow row2;
	private TableRow row3;
	private View line1;
	private View line2;
	private View line3;
	private RelativeLayout policyBackHeaderButton;
	private ProgressDialog progress;
	private Thread thread;

	private int LobID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_policy_details);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {

		Intent intent = getIntent();
		LobID = intent.getExtras().getInt("LOBID");
		View headerView = (View) findViewById(R.id.screenpolicydetails_Header);
		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenPolicyDetails);
		policyBackHeaderButton = (RelativeLayout) headerView.findViewById(R.id.btnBack);
		//policyBackHeaderButton.setVisibility(View.VISIBLE);
		/*policyBackHeaderButton
				.setBackgroundResource(R.drawable.back_arrow);*/
		//policyBackHeaderButton.setVisibility(View.VISIBLE);
		viewPolicy = (TextView) findViewById(R.id.viewPolicy);
		viewPolicy.setText(intent.getStringExtra("POLICY"));

		viewName = (TextView) findViewById(R.id.viewName);
		viewName.setText(intent.getStringExtra("NAME"));

		layoutPolicyDetails = (TableLayout) findViewById(R.id.layoutPolicyDetails);
		viewFieldLeg1 = (TextView) findViewById(R.id.viewFieldLeg1);
		viewFieldLeg2 = (TextView) findViewById(R.id.viewFieldLeg2);
		viewFieldLeg3 = (TextView) findViewById(R.id.viewFieldLeg3);
		viewField1 = (TextView) findViewById(R.id.viewField1);
		viewField2 = (TextView) findViewById(R.id.viewField2);
		viewField3 = (TextView) findViewById(R.id.viewField3);
		btnSave = (Button) findViewById(R.id.btnSave);
		row1 = (TableRow) findViewById(R.id.row1);
		row2 = (TableRow) findViewById(R.id.row2);
		row3 = (TableRow) findViewById(R.id.row3);
		line1 = (View) findViewById(R.id.line1);
		line2 = (View) findViewById(R.id.line2);
		line3 = (View) findViewById(R.id.line3);

		layoutPolicyDetails.removeView(line1);
		layoutPolicyDetails.removeView(line2);
		layoutPolicyDetails.removeView(line3);
		layoutPolicyDetails.removeView(row1);
		layoutPolicyDetails.removeView(row2);
		layoutPolicyDetails.removeView(row3);

		switch (LobID) {
		case Utility.AUTOMOTIVE:

			layoutPolicyDetails.addView(line1);
			viewFieldLeg1.setText(R.string.Model);
			viewField1.setText(intent.getStringExtra("MODEL"));
			layoutPolicyDetails.addView(row1);

			layoutPolicyDetails.addView(line2);
			viewFieldLeg2.setText(R.string.LicenseNumber);
			viewField2.setText(intent.getStringExtra("LICENSENUMBER"));
			layoutPolicyDetails.addView(row2);

			layoutPolicyDetails.addView(line3);
			viewFieldLeg3.setText(R.string.YearModel);
			viewField3.setText(intent.getStringExtra("YEARMODEL"));
			layoutPolicyDetails.addView(row3);

			break;
		case Utility.AUTOMAKER:

			layoutPolicyDetails.addView(line1);
			viewFieldLeg1.setText(R.string.Model);
			viewField1.setText(intent.getStringExtra("MODEL"));
			layoutPolicyDetails.addView(row1);

			layoutPolicyDetails.addView(line2);
			viewFieldLeg2.setText(R.string.LicenseNumber);
			viewField2.setText(intent.getStringExtra("LICENSENUMBER"));
			layoutPolicyDetails.addView(row2);

			layoutPolicyDetails.addView(line3);
			viewFieldLeg3.setText(R.string.YearModel);
			viewField3.setText(intent.getStringExtra("YEARMODEL"));
			layoutPolicyDetails.addView(row3);

			break;
		case Utility.PROPERTY:

			layoutPolicyDetails.addView(line1);
			viewFieldLeg1.setText(R.string.Address);
			viewField1.setText(intent.getStringExtra("ADDRESS"));
			layoutPolicyDetails.addView(row1);

			break;
		}

		layoutPolicyDetails.invalidate();
	}

	private void Events() {
		policyBackHeaderButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				start();
			}
		});
	}

	private void start() {

		progress = ProgressDialog.show(ScreenPolicyDetails.this,
				getText(R.string.Wait), getText(R.string.SendingInformation),
				false, false);

		thread = new Thread(ScreenPolicyDetails.this);
		thread.start();
	}

	public void run() {

		try {

			BLLDirectAssist directAssist = new BLLDirectAssist();
			directAssist.SavePolicy(ScreenPolicyDetails.this, Client
					.getDeviceUID(getBaseContext()), 1, viewPolicy.getText()
					.toString(), ClientParams.ClientID);

			if (directAssist.getAction().getResultCode() != 0) {

				runOnUiThread(new Runnable() {
					public void run() {

						setMessage(getString(R.string.Error),
								getString(R.string.ErrorMessage), false);
					}
				});

			} else {

				Intent intent = new Intent();

				intent.putExtra("POLICY", viewPolicy.getText().toString());
				intent.putExtra("FIELD",
						(LobID == Utility.PROPERTY) ? viewPolicy.getText()
								.toString() : viewField1.getText().toString());
				setResult(1, intent);

				finish();
			}

		} catch (Exception e) {

			ErrorHelper.setErrorMessage(e);

			runOnUiThread(new Runnable() {
				public void run() {

					setMessage(getString(R.string.Error),
							getString(R.string.ErrorMessage), true);
				}
			});

		} finally {
			progress.dismiss();
		}
	}

	private void setMessage(final String title, final String message,
			final boolean finishActivity) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScreenPolicyDetails.this);
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
