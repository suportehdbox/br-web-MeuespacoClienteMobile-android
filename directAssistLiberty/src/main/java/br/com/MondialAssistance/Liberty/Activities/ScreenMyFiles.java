package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Vector;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAutomaker;
import br.com.MondialAssistance.DirectAssist.BLL.BLLAutomotive;
import br.com.MondialAssistance.DirectAssist.BLL.BLLProperty;
import br.com.MondialAssistance.DirectAssist.MDL.AutomotiveCase;
import br.com.MondialAssistance.DirectAssist.MDL.Case;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.DirectAssist.Util.Client;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenMyFiles extends Activity implements Runnable {

	private int LobID;

	private ProgressDialog progress;
	private TextView viewScreenName;
	private ListView listFiles;
	private Thread thread;
	private RelativeLayout casesHeaderBackButton;

	private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_myfiles);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);
		AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
		mTracker = analyticsApplication.getDefaultTracker(getApplication());

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View) findViewById(R.id.mycases_header);
		casesHeaderBackButton = (RelativeLayout) headerView.findViewById(R.id.btnBack);
		/*casesHeaderBackButton
				.setBackgroundResource(R.drawable.back_arrow);*/
	//	casesHeaderBackButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		LobID = intent.getExtras().getInt("LOBID");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenMyFiles);
		/*
		 * added paading of 30dp dynamically since the text is overlapping with
		 * the left side header button
		 */
		Resources resources = this.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = 30 / (metrics.densityDpi / 160f);
		//viewScreenName.setPadding((int) dp, 0, 0, 0);
		listFiles = (ListView) findViewById(R.id.listFiles);

		progress = ProgressDialog.show(ScreenMyFiles.this,
				getText(R.string.Wait), getText(R.string.SearchInformation),
				false, false);

		thread = new Thread(this);
		thread.start();
	}

	private void Events() {
		casesHeaderBackButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		listFiles.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
									int position, long id) {

				CtrlListViewListMyFilesAdapter adapterListViewListMyFiles = (CtrlListViewListMyFilesAdapter) adapter
						.getAdapter();
				int FileNumber = adapterListViewListMyFiles.getItem(position)
						.getCaseNumber();

				Intent intent = new Intent(ScreenMyFiles.this,
						ScreenFileDetails.class);
				intent.putExtra("FILENUMBER", FileNumber);

				startActivity(intent);
			}
		});
	}

	public void run() {

		try {
			final CtrlListViewListMyFilesAdapter adapterListMyFiles = new CtrlListViewListMyFilesAdapter(
					ScreenMyFiles.this);

			switch (LobID) {
			case Utility.AUTOMOTIVE:
				BLLAutomotive automotive = new BLLAutomotive();
				Vector<AutomotiveCase> automotiveCases = automotive
						.getListCases(ScreenMyFiles.this,
								Client.getDeviceUID(getBaseContext()), 1,
								ClientParams.ClientID);

				if (automotive.getAction().getResultCode().equals(0))
					adapterListMyFiles.setAutomotiveCases(automotiveCases);
				else if (!automotive.getAction().getResultCode().equals(100)) {

					runOnUiThread(new Runnable() {
						public void run() {

							setMessage(getString(R.string.Error),
									getString(R.string.ErrorMessage), false);
						}
					});
					return;
				}
				break;
			case Utility.AUTOMAKER:
				BLLAutomaker automaker = new BLLAutomaker();
				Vector<AutomotiveCase> automakerCases = automaker.getListCases(
						ScreenMyFiles.this,
						Client.getDeviceUID(getBaseContext()), 1,
						ClientParams.ClientID);

				if (automaker.getAction().getResultCode().equals(0))
					adapterListMyFiles.setAutomakerCases(automakerCases);
				else if (!automaker.getAction().getResultCode().equals(100)) {

					runOnUiThread(new Runnable() {
						public void run() {

							setMessage(getString(R.string.Error),
									getString(R.string.ErrorMessage), false);
						}
					});
					return;
				}
				break;
			case Utility.PROPERTY:
				BLLProperty property = new BLLProperty();
				Vector<Case> propertyCases = property.getListCases(
						ScreenMyFiles.this,
						Client.getDeviceUID(getBaseContext()), 1,
						ClientParams.ClientID);

				if (property.getAction().getResultCode().equals(0))
					adapterListMyFiles.setPropertyCases(propertyCases);
				else if (!property.getAction().getResultCode().equals(100)) {

					runOnUiThread(new Runnable() {
						public void run() {

							setMessage(getString(R.string.Error),
									getString(R.string.ErrorMessage), false);
						}
					});
					return;
				}
				break;
			}

			if (adapterListMyFiles.getCount() != 0) {

				runOnUiThread(new Runnable() {
					public void run() {

						listFiles.setAdapter(adapterListMyFiles);
					}
				});

			} else {

				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(ScreenMyFiles.this,
								R.string.NotFoundData, Toast.LENGTH_SHORT)
								.show();
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

	private void setMessage(final String title, final String message,
			final boolean finishActivity) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(ScreenMyFiles.this);
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

		Log.i("Google Analytics: ", "Assistência Automotiva: Consulta");
		mTracker.setScreenName("Assistência Automotiva: Consulta");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CustomApplication.activityPaused();
	}
}