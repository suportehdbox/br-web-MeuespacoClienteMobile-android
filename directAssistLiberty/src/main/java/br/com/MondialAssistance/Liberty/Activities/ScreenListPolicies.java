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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAutomaker;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAutomotive;
import br.com.MondialAssistance.DirectAssist.BLL.BLLDirectAssist;
import br.com.MondialAssistance.DirectAssist.BLL.BLLProperty;
import br.com.MondialAssistance.DirectAssist.MDL.AutomotivePolicy;
import br.com.MondialAssistance.DirectAssist.MDL.PropertyPolicy;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.DirectAssist.Util.Client;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenListPolicies extends Activity implements Runnable {

	private int LobID;
	private int Position = 0;
	private boolean Edit = false;
	private boolean CloseScreen = false;
	private static final int SEARCH_POLICY = 1;
	private static final int SEARCH_POLICY_ONLYREFRESH = 2;
	private CtrlListViewListPoliciesAdapter adapterListPolicies;

	private Thread thread;
	private ProgressDialog progress;

	private TextView viewScreenName;
	private ListView listPolicies;
	private Button btnAddPolicy;
	private Button btnEdit;
	private RelativeLayout headerCancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_list_policies);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	public void Initialize() {
		View headerView = (View) findViewById(R.id.screenList_Header);
		headerCancelButton = (RelativeLayout) headerView.findViewById(R.id.btnBack);
		/*headerCancelButton
				.setBackgroundResource(R.drawable.back_arrow);
		headerCancelButton.setVisibility(View.VISIBLE);*/
		btnEdit = (Button) findViewById(R.id.btnEdit);
		Intent intent = getIntent();
		LobID = intent.getExtras().getInt("LOBID");
		CloseScreen = intent.getExtras().getBoolean("CLOSE");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenListPolicies);

		// btnEdit = (Button)findViewById(R.id.btnEdit);
		btnAddPolicy = (Button) findViewById(R.id.btnAddPolicy);

		listPolicies = (ListView) findViewById(R.id.listPolicies);

		start();
	}

	public void Events() {
		headerCancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (Edit == false) {

					Edit = true;
					adapterListPolicies.setEdit(true);
					btnEdit.setText(R.string.OK);

				} else {

					Edit = false;
					adapterListPolicies.setEdit(false);
					btnEdit.setText(R.string.Edit);
				}

				adapterListPolicies.notifyDataSetChanged();
			}
		});

		btnAddPolicy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(ScreenListPolicies.this,
						ScreenSearchPolicies.class);
				intent.putExtra("LOBID", LobID);

				startActivityForResult(intent, SEARCH_POLICY_ONLYREFRESH);
			}
		});

		listPolicies.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
									final int position, long id) {

				CtrlListViewListPoliciesAdapter adapterListPolicies = (CtrlListViewListPoliciesAdapter) adapter
						.getAdapter();
				CtrlListViewListPoliciesAdapter.Items item = adapterListPolicies.getItem(position);

				if (Edit == true) {

					AlertDialog.Builder dialog = new AlertDialog.Builder(
							ScreenListPolicies.this);
					dialog.setTitle(R.string.Confirm);
					dialog.setMessage(getString(R.string.HelperDeletePolicy)
							.replace("[POLICY]", item.getField2()));
					dialog.setIcon(android.R.drawable.ic_menu_help);
					dialog.setNegativeButton(R.string.No, null);
					dialog.setPositiveButton(R.string.Yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int which) {

									Position = position;
									start();
								}
							});
					dialog.show();

				} else {

					Intent intent = new Intent();
					intent.putExtra("POLICY", item.getID());
					intent.putExtra("FIELD", item.getInformation());
					setResult(1, intent);

					finish();
				}
			}
		});
	}

	private void start() {

		progress = ProgressDialog.show(ScreenListPolicies.this,
				getText(R.string.Wait), getText(R.string.SearchInformation),
				false, false);

		thread = new Thread(this);
		thread.start();
	}

	public void run() {

		try {

			if (Edit == true) {

				BLLDirectAssist directAssist = new BLLDirectAssist();

				directAssist.DeletePolicy(ScreenListPolicies.this,
						Client.getDeviceUID(ScreenListPolicies.this), 1,
						adapterListPolicies.getItem(Position).getID(),
						ClientParams.ClientID);

				adapterListPolicies.Remove(Position);

				runOnUiThread(new Runnable() {
					public void run() {

						adapterListPolicies.notifyDataSetChanged();
					}
				});

			} else {

				adapterListPolicies = new CtrlListViewListPoliciesAdapter(this);

				switch (LobID) {
				case Utility.AUTOMOTIVE:

					BLLAutomotive automotive = new BLLAutomotive();
					final Vector<AutomotivePolicy> automotivePolicies;

					automotivePolicies = automotive.getListPolicies(
							ScreenListPolicies.this,
							Client.getDeviceUID(getBaseContext()), 1,
							ClientParams.ClientID);

					if (automotive.getAction().getResultCode().equals(0)) {
						adapterListPolicies
								.setDataAutomotive(automotivePolicies);
					}

					break;
				case Utility.AUTOMAKER:

					BLLAutomaker automaker = new BLLAutomaker();
					final Vector<AutomotivePolicy> automakerPolicies;

					automakerPolicies = automaker.getListPolicies(
							ScreenListPolicies.this,
							Client.getDeviceUID(getBaseContext()), 1,
							ClientParams.ClientID);

					if (automaker.getAction().getResultCode().equals(0)) {
						adapterListPolicies
								.setDataAutomotive(automakerPolicies);
					}

					break;
				case Utility.PROPERTY:

					BLLProperty property = new BLLProperty();
					final Vector<PropertyPolicy> propertyPolicies;

					propertyPolicies = property.getListPolicies(
							ScreenListPolicies.this,
							Client.getDeviceUID(getBaseContext()), 1,
							ClientParams.ClientID);

					if (property.getAction().getResultCode().equals(0)) {
						adapterListPolicies.setDataProperty(propertyPolicies);
					}

					break;
				}

				if (adapterListPolicies.getCount() != 0) {

					runOnUiThread(new Runnable() {
						public void run() {

							btnEdit.setVisibility(Button.VISIBLE);
							listPolicies.setAdapter(adapterListPolicies);
						}
					});

				} else {

					Intent intent = new Intent(ScreenListPolicies.this,
							ScreenSearchPolicies.class);
					intent.putExtra("LOBID", LobID);

					startActivityForResult(intent, SEARCH_POLICY);
				}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case SEARCH_POLICY:
			Bundle params = (data == null) ? null : data.getExtras();

			if (params != null) {

				String policyid = params.getString("POLICY");
				String field = params.getString("FIELD");

				if (CloseScreen == true) {

					Intent intent = new Intent();

					intent.putExtra("POLICY", policyid);
					intent.putExtra("FIELD", field);
					setResult(1, intent);

					finish();

				} else {

					start();
				}
			} else {

				setResult(0);
				finish();
			}

			break;
		case SEARCH_POLICY_ONLYREFRESH:

			Edit = false;
			adapterListPolicies.setEdit(false);
			btnEdit.setText(R.string.Edit);

			start();

			break;
		}
	}

	private void setMessage(final String title, final String message,
			final boolean finishActivity) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScreenListPolicies.this);
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