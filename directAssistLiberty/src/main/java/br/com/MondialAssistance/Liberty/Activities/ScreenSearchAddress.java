package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

import br.com.MondialAssistance.Liberty.BLL.BLLAddressFinder;
import br.com.MondialAssistance.Liberty.MDL.AddressLocation;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.Util.ErrorHelper;
import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenSearchAddress extends Activity implements Runnable {

	private static final int SCREEN_ADDRESS = 1;

	private TextView viewScreenName;
	private EditText editSearch;
	private ImageButton btnSearch;
	private RadioButton radItemZipCode;
	private RadioButton radItemStreet;
	private ListView listAddress;
	private ProgressDialog progress;
	private RelativeLayout cancelHeaderButton;
	private Thread thread;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_search_address);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View) findViewById(R.id.screenSearchAdd_Header);
		cancelHeaderButton = (RelativeLayout) headerView.findViewById(R.id.btnBack);
		//cancelHeaderButton.setBackgroundResource(R.drawable.back_arrow);
		//cancelHeaderButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		type = intent.getExtras().getInt("TYPE");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenSearchAddress);

		editSearch = (EditText) findViewById(R.id.editSearch);
		editSearch.setInputType(InputType.TYPE_CLASS_NUMBER);

		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		radItemZipCode = (RadioButton) findViewById(R.id.radItemZipCode);
		radItemStreet = (RadioButton) findViewById(R.id.radItemStreet);
		listAddress = (ListView) findViewById(R.id.listAddress);
	}

	private void Events() {
		cancelHeaderButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				finish();
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				start();
			}
		});

		editSearch.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {

					switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:

							start();
							return true;
					}
				}
				return false;
			}
		});

		listAddress.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
									int position, long id) {

				CtrlListViewListAddressAdapter adapterListAddress = (CtrlListViewListAddressAdapter) adapter
						.getAdapter();
				AddressLocation addressLocation = adapterListAddress
						.getItem(position);
				Intent intent;

				switch (type) {
					case Utility.ADDRESS_DETAILS:

						intent = new Intent();

						intent.putExtra("ADDRESS", addressLocation.getStreetName());
						intent.putExtra("DISTRICT", addressLocation.getDistrict());
						intent.putExtra("CITY", addressLocation.getCity());
						intent.putExtra("STATE", addressLocation.getState());
						intent.putExtra("ZIP", addressLocation.getZip());
						intent.putExtra("LATITUDE", addressLocation.getLatitude());
						intent.putExtra("LONGITUDE", addressLocation.getLongitude());

						setResult(1, intent);
						finish();

						break;
					case Utility.ACCREDITED_ESTABLISHMENT_DETAILS:

						intent = new Intent();

						intent.putExtra("ADDRESS", addressLocation.getStreetName());
						intent.putExtra("DISTRICT", addressLocation.getDistrict());
						intent.putExtra("CITY", addressLocation.getCity());
						intent.putExtra("STATE", addressLocation.getState());
						intent.putExtra("ZIP", addressLocation.getZip());
						intent.putExtra("LATITUDE", addressLocation.getLatitude());
						intent.putExtra("LONGITUDE", addressLocation.getLongitude());

						setResult(1, intent);

						finish();
						break;
				}
			}
		});

		radItemZipCode
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						if (isChecked)
							editSearch
									.setInputType(InputType.TYPE_CLASS_NUMBER);
					}
				});

		radItemStreet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked)
					editSearch.setInputType(InputType.TYPE_CLASS_TEXT);
			}
		});
	}

	private void start() {

		Input(false);

		progress = ProgressDialog.show(ScreenSearchAddress.this,
				getText(R.string.Wait), getText(R.string.SearchInformation),
				false, false);

		thread = new Thread(ScreenSearchAddress.this);
		thread.start();
	}

	public void run() {

		try {
			BLLAddressFinder addressFinder = new BLLAddressFinder();
			Vector<AddressLocation> addressLocations = null;
			final CtrlListViewListAddressAdapter adapter;

			if (radItemZipCode.isChecked()) {

				String zipCode = editSearch.getText().toString()
						.replace("-", "");

				addressLocations = new Vector<AddressLocation>();
				addressLocations = addressFinder.FindAddressByZipCode(zipCode);

			} else if (radItemStreet.isChecked()) {

				String street[] = editSearch.getText().toString().split(",");

				if (street.length <= 1) {

					runOnUiThread(new Runnable() {
						public void run() {

							setMessage(null,
									getString(R.string.HelperSearchStreet),
									false);
						}
					});

					return;

				} else {

					addressLocations = new Vector<AddressLocation>();
					addressLocations = addressFinder.FindAddressByStreet(
							street[0].trim(), street[1].trim());
				}
			}

			if (addressLocations.size() != 0) {

				adapter = new CtrlListViewListAddressAdapter(
						ScreenSearchAddress.this, addressLocations);

				runOnUiThread(new Runnable() {
					public void run() {

						listAddress.setAdapter(adapter);
					}
				});

			} else {

				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(ScreenSearchAddress.this,
								R.string.HelperSearchStreetNotResult,
								Toast.LENGTH_SHORT).show();
					}
				});
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
			case SCREEN_ADDRESS:

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
				ScreenSearchAddress.this);
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

	private void Input(boolean show) {

		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		if (show)
			inputMethodManager.showSoftInput(editSearch, 0);
		else
			inputMethodManager.hideSoftInputFromWindow(
					editSearch.getWindowToken(), 0);
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
