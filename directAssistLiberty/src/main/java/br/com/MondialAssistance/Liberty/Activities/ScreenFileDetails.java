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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.BLL.BLLClient;
import br.com.MondialAssistance.DirectAssist.BLL.BLLDirectAssist;
import br.com.MondialAssistance.DirectAssist.MDL.ServiceDispatchCase;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.DirectAssist.Util.ErrorHelper;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenFileDetails extends Activity implements Runnable {

	private LinearLayout layoutLicenseNumber;
	private LinearLayout layoutArrivalTime;
	private TextView viewLicenseNumber;
	private TextView viewArrivalTime;
	private TextView viewScreenName;
	private TextView viewFileNumber;
	private ProgressDialog progress;
	private TextView viewStatus;
	private Button btnFollowMap;
	private Button btnRefresh;
	private Thread thread;
    private RelativeLayout fileDetailsHeaderBackButton;
	private int fileNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_file_details);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {

		Intent intent = getIntent();
		fileNumber = intent.getExtras().getInt("FILENUMBER");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenListPolicies);
		View headerView = (View) findViewById(R.id.fileDetails_Header);
		fileDetailsHeaderBackButton = (RelativeLayout) headerView
				.findViewById(R.id.btnBack);
		layoutLicenseNumber = (LinearLayout) findViewById(R.id.layoutLicenseNumber);
		layoutArrivalTime = (LinearLayout) findViewById(R.id.layoutArrivalTime);
		viewFileNumber = (TextView) findViewById(R.id.viewFileNumber);
		viewStatus = (TextView) findViewById(R.id.viewStatus);
		viewArrivalTime = (TextView) findViewById(R.id.viewArrivalTime);
		viewLicenseNumber = (TextView) findViewById(R.id.viewLicenseNumber);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnFollowMap = (Button) findViewById(R.id.btnFollowMap);

		progress = ProgressDialog.show(ScreenFileDetails.this,
				getText(R.string.Wait), getText(R.string.SearchInformation),
				false, false);

		thread = new Thread(this);
		thread.start();
	}

	private void Events() {
		fileDetailsHeaderBackButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				progress = ProgressDialog.show(ScreenFileDetails.this,
						getText(R.string.Wait),
						getText(R.string.SearchInformation), false, false);

				thread = new Thread(ScreenFileDetails.this);
				thread.start();
			}
		});

		btnFollowMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(ScreenFileDetails.this,
						ScreenMapFollowProvider.class);
				intent.putExtra("FILENUMBER", fileNumber);

				startActivity(intent);
			}
		});
	}

	public void run() {

		try {

			BLLDirectAssist directAssist = new BLLDirectAssist();
			BLLClient client = new BLLClient();

			final ServiceDispatchCase serviceDispatchCase;
			final String statusDescription;

			serviceDispatchCase = directAssist.getDispatchStatus(
					ScreenFileDetails.this, fileNumber, 1,
					ClientParams.ClientID);

			if (directAssist.getAction().getResultCode().equals(0)) {

				statusDescription = client.getDispatchStatusDescriptionByID(
						getResources().getStringArray(R.array.DispatchStatus),
						serviceDispatchCase.getServiceDispatch()
								.getDispatchStatus(),
						(serviceDispatchCase.getServiceDispatch()
								.getScheduled() == 1 ? true : false));

				runOnUiThread(new Runnable() {
					public void run() {

						btnFollowMap.setVisibility(Button.INVISIBLE);
						layoutArrivalTime.setVisibility(LinearLayout.INVISIBLE);
						layoutLicenseNumber
								.setVisibility(LinearLayout.INVISIBLE);

						viewFileNumber.setText(serviceDispatchCase.getCase()
								.getCaseNumber().toString());
						viewStatus.setText(statusDescription);

						if (serviceDispatchCase.getServiceDispatch()
								.getScheduled() == 0) {

							if (serviceDispatchCase.getServiceDispatch()
									.getDispatchStatus() != 0) {

								/* TEMPO DE CHEGADA DO PRESTADOR */
								if (serviceDispatchCase.getServiceDispatch() != null
										&& serviceDispatchCase
												.getServiceDispatch()
												.getArrivalTime() != null) {

									layoutArrivalTime
											.setVisibility(LinearLayout.VISIBLE);
									viewArrivalTime.setText(serviceDispatchCase
											.getServiceDispatch()
											.getArrivalTime().toString()
											+ " minutos");
								}

								/* PLACA DO VEICULO DO PRESTADOR */
								if (serviceDispatchCase.getProvider() != null
										&& serviceDispatchCase.getProvider()
												.getLicenseNumber() != null) {

									layoutLicenseNumber
											.setVisibility(LinearLayout.VISIBLE);
									viewLicenseNumber
											.setText(serviceDispatchCase
													.getProvider()
													.getLicenseNumber());
								}

								/*
								 * ACOMPANHAMENTO DA CHEDADA DO PRESTADOR EM UM
								 * MAPA
								 */
								if (serviceDispatchCase.getProvider()
										.getLocation().getLatitude() != null
										&& serviceDispatchCase.getProvider()
												.getLocation().getLongitude() != null)
									btnFollowMap.setVisibility(Button.VISIBLE);
							}

						} else {

							String scheduleStartDate = (serviceDispatchCase
									.getServiceDispatch()
									.getScheduleStartDate() == null ? ""
									: serviceDispatchCase.getServiceDispatch()
											.getScheduleStartDate());
							String scheduleEndDate = (serviceDispatchCase
									.getServiceDispatch().getScheduleEndDate() == null ? ""
									: serviceDispatchCase.getServiceDispatch()
											.getScheduleEndDate());

							/* AGENDAMENTO */
							if (!scheduleStartDate.equals("")
									&& !scheduleEndDate.equals("")) {

								layoutArrivalTime
										.setVisibility(LinearLayout.VISIBLE);
								viewArrivalTime
										.setText(String
												.format("%s %s %s %s %s",
														getString(R.string.Between),
														Utility.getDateByString(scheduleStartDate),
														Utility.getHourByString(scheduleStartDate),
														getString(R.string.And),
														Utility.getHourByString(scheduleEndDate)));

							} else if (!scheduleStartDate.equals("")
									&& scheduleEndDate.equals("")) {

								layoutArrivalTime
										.setVisibility(LinearLayout.VISIBLE);
								viewArrivalTime.setText(scheduleStartDate);

							}
						}
					}
				});

			} else {

				runOnUiThread(new Runnable() {
					public void run() {
						setMessage(getString(R.string.Error),
								getString(R.string.ErrorMessage), false);
					}
				});
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
				ScreenFileDetails.this);
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