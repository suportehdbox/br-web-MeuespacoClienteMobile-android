package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import br.com.MondialAssistance.Liberty.BLL.BLLAutomaker;
import br.com.MondialAssistance.Liberty.BLL.BLLAutomotive;
import br.com.MondialAssistance.Liberty.BLL.BLLProperty;
import br.com.MondialAssistance.Liberty.MDL.AutomotivePolicy;
import br.com.MondialAssistance.Liberty.MDL.PropertyPolicy;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.Util.ErrorHelper;
import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenSearchPolicies extends Activity implements Runnable {

	private static final int SCREEN_POLICY_DETAILS = 1;

	private int LobID;
	private Thread thread;
	private String screenAction;

	private TextView viewScreenName;
	private Button btnSearch;
	private ProgressDialog progress;
	private TextView viewSearchFields1;
	private TextView viewDocument;
	private EditText editSearchFields1;
	private EditText editDocument;
	private TableRow rowDocument;
	private RelativeLayout cancelHeaderButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_search_policies);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View) findViewById(R.id.screenSearchpol_Header);
		cancelHeaderButton = (RelativeLayout) headerView.findViewById(R.id.btnBack);
		//cancelHeaderButton.setBackgroundResource(R.drawable.btn_cancelar);
		//cancelHeaderButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		LobID = intent.getExtras().getInt("LOBID");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenSearchPolicies);

		// viewSearchFields1 = (TextView)findViewById(R.id.viewSearchFields1);
		// viewSearchFields1.setText(getText("SearchFields1"));
		editSearchFields1 = (EditText) findViewById(R.id.editSearchFields1);
		editSearchFields1.setHint(getText("SearchFields1"));
		editSearchFields1.setHintTextColor(getResources().getColor(
				R.color.ColorMenu));
		// viewDocument = (TextView)findViewById(R.id.viewDocument);
		// viewDocument.setText(getText("Document"));
		editDocument = (EditText) findViewById(R.id.editDocument);
		editDocument.setHint(getText(getText("Document")));
		editDocument.setHintTextColor(getResources().getColor(
				R.color.ColorMenu));
		rowDocument = (TableRow) findViewById(R.id.rowDocument);
		rowDocument
				.setVisibility((UtilityScreen
						.isFindPolicyDismissUserDocument(ScreenSearchPolicies.this) == true) ? TableRow.VISIBLE
						: TableRow.INVISIBLE);

		btnSearch = (Button) findViewById(R.id.btnSearch);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void Events() {
		cancelHeaderButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				start("SEARCH");
			}
		});

		editSearchFields1.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {

					switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:

							if (UtilityScreen
									.isFindPolicyDismissUserDocument(ScreenSearchPolicies.this) == true)
								editDocument.requestFocus();
							else
								start("SEARCH");

							return true;
					}
				}
				return false;
			}
		});

		editDocument.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {

					switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							start("SEARCH");

							return true;
					}
				}
				return false;
			}
		});
	}

	public void start(String action) {

		progress = ProgressDialog.show(ScreenSearchPolicies.this,
				getText(R.string.Wait),
				(action == "SEARCH") ? getText(R.string.SearchInformation)
						: getText(R.string.SendingInformation), false, false);
		screenAction = action;

		thread = new Thread(ScreenSearchPolicies.this);
		thread.start();

	}

	public void run() {
		try {

			if (screenAction.equals("SEARCH")) {

				Intent intent;

				switch (LobID) {
				case Utility.AUTOMOTIVE:

					BLLAutomotive automotive = new BLLAutomotive();
					final AutomotivePolicy automotivePolicy;

					automotivePolicy = automotive.getPolicy(
							ScreenSearchPolicies.this, editSearchFields1
									.getText().toString().toUpperCase()
									.replace("-", ""), editDocument.getText()
									.toString().toUpperCase(),
							ClientParams.ClientID);

					if (automotive.getAction().getResultCode() == 0 && automotivePolicy.getPolicyID() != null){

						intent = new Intent(ScreenSearchPolicies.this,
								ScreenPolicyDetails.class);

						intent.putExtra("LOBID", LobID);
						intent.putExtra("POLICY",
								automotivePolicy.getPolicyID());
						intent.putExtra("NAME",
								automotivePolicy.getInsuredName());
						intent.putExtra("MODEL", automotivePolicy.getVehicle()
								.getModel());
						intent.putExtra("YEARMODEL", automotivePolicy
								.getVehicle().getVehicleYear());
						intent.putExtra("LICENSENUMBER", automotivePolicy
								.getVehicle().getLicenseNumber());

						startActivityForResult(intent, SCREEN_POLICY_DETAILS);

					} else {

						runOnUiThread(new Runnable() {
							public void run() {

								setMessage(getString(R.string.NotFound),
										getString(R.string.NotFoundMessage),
										false);
							}
						});
					}
					break;

				case Utility.AUTOMAKER:

					BLLAutomaker automaker = new BLLAutomaker();
					final AutomotivePolicy automakerAutomotivePolicy;

					automakerAutomotivePolicy = automaker.getPolicy(
							ScreenSearchPolicies.this, editSearchFields1
									.getText().toString().toUpperCase(),
							editDocument.getText().toString().toUpperCase(),
							ClientParams.ClientID);

					if (automaker.getAction().getResultCode() == 0 && automakerAutomotivePolicy.getPolicyID() != null) {

						intent = new Intent(ScreenSearchPolicies.this,
								ScreenPolicyDetails.class);

						intent.putExtra("LOBID", LobID);
						intent.putExtra("POLICY",
								automakerAutomotivePolicy.getPolicyID());
						intent.putExtra("NAME",
								automakerAutomotivePolicy.getInsuredName());
						intent.putExtra("MODEL", automakerAutomotivePolicy
								.getVehicle().getModel());
						intent.putExtra("YEARMODEL", automakerAutomotivePolicy
								.getVehicle().getVehicleYear());
						intent.putExtra("LICENSENUMBER",
								automakerAutomotivePolicy.getVehicle()
										.getLicenseNumber());

						startActivityForResult(intent, SCREEN_POLICY_DETAILS);

					} else {

						runOnUiThread(new Runnable() {
							public void run() {

								setMessage(getString(R.string.NotFound),
										getString(R.string.NotFoundMessage),
										false);
							}
						});
					}
					break;

				case Utility.PROPERTY:

					BLLProperty property = new BLLProperty();
					final PropertyPolicy propertyPolicy;

					propertyPolicy = property.getPolicy(
							ScreenSearchPolicies.this, editSearchFields1
									.getText().toString().toUpperCase(),
							editDocument.getText().toString().toUpperCase(),
							ClientParams.ClientID);

					if (property.getAction().getResultCode() == 0 && propertyPolicy.getPolicyID() != null){

						intent = new Intent(ScreenSearchPolicies.this,
								ScreenPolicyDetails.class);

						intent.putExtra("LOBID", LobID);
						intent.putExtra("POLICY", propertyPolicy.getPolicyID());
						intent.putExtra("NAME", propertyPolicy.getInsuredName());
						intent.putExtra("ADDRESS", propertyPolicy.getAddress()
								.toString());

						startActivityForResult(intent, SCREEN_POLICY_DETAILS);

					} else {

						runOnUiThread(new Runnable() {
							public void run() {

								setMessage(getString(R.string.NotFound),
										getString(R.string.NotFoundMessage),
										false);
							}
						});
					}

					break;
				}

			}

		} catch (Exception e) {

			ErrorHelper.setErrorMessage(e);

			runOnUiThread(new Runnable() {
				public void run() {

					setMessage(getString(R.string.Error),
							getString(R.string.ErrorMessage), false);
				}
			});

		} finally {
			progress.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Bundle params = (data == null) ? null : data.getExtras();

		if (params != null) {

			switch (requestCode) {
			case SCREEN_POLICY_DETAILS:
				Intent intent = new Intent();

				intent.putExtras(params);
				setResult(1, intent);

				finish();

				break;
			}
		}
	}

	private void setMessage(final String title, final String message,
			final boolean finishActivity) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScreenSearchPolicies.this);
		if (title != null)
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

	private int getText(String value) {

		if (value.equals("SearchFields1")) {

			switch (LobID) {
			case Utility.AUTOMOTIVE:
				return R.string.SearchFieldsAutomotive1;
			case Utility.AUTOMAKER:
				return R.string.SearchFieldsAutomaker1;
			case Utility.PROPERTY:
				return R.string.SearchFieldsProperty1;
			}

		} else if (value.equals("Document")) {

			return R.string.Document;

		} else if (value.equals("Policy")) {

			return R.string.Policy;

		} else if (value.equals("ResultSearchFields2")) {

			switch (LobID) {
			case Utility.AUTOMOTIVE:
				return R.string.ResultSearchFieldsAutomotive2;
			case Utility.AUTOMAKER:
				return R.string.ResultSearchFieldsAutomaker2;
			case Utility.PROPERTY:
				return R.string.ResultSearchFieldsProperty2;
			}

		} else if (value.equals("ResultSearchFields3")) {

			switch (LobID) {
			case Utility.AUTOMOTIVE:
				return R.string.ResultSearchFieldsAutomotive3;
			case Utility.AUTOMAKER:
				return R.string.ResultSearchFieldsAutomaker3;
			case Utility.PROPERTY:
				return R.string.ResultSearchFieldsProperty3;
			}
		}
		return 0;
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