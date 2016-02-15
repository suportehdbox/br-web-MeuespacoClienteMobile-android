package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenSchedule extends Activity {

	protected final static int DATE_BEGIN = 1;
	protected final static int TIME_BEGIN = 2;
	protected final static int TIME_END = 3;

	private DatePickerDialog.OnDateSetListener dateListenerBegin;
	private TimePickerDialog.OnTimeSetListener timeListenerBegin;
	private TimePickerDialog.OnTimeSetListener timeListenerEnd;

	private TextView viewScreenName;
	private Button btnDateBegin;
	private Button btnTimeBegin;
	private Button btnTimeEnd;
	private Button btnConfirm;
	private RelativeLayout scheduleBackHeaderButton;
	private Calendar calendarBegin;
	private Calendar calendarEnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_schedule);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View) findViewById(R.id.screenSchedule_Header);
		scheduleBackHeaderButton = (RelativeLayout) headerView
				.findViewById(R.id.btnBack);
		/*scheduleBackHeaderButton
				.setBackgroundResource(R.drawable.assistencia_nova_localmap_btn_nova);
		scheduleBackHeaderButton.setVisibility(View.VISIBLE);*/
		//btnConfirm = (Button) headerView.findViewById(R.id.btnEdit);
		/*btnConfirm.setBackgroundResource(R.drawable.header_btn_ok);
		btnConfirm.setVisibility(View.VISIBLE);*/
		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(getResources().getString(
				R.string.TitleScreenSchedule));

		Calendar Initial = Calendar.getInstance();
		Calendar End = Calendar.getInstance();

		Intent intent = getIntent();

		Initial.set(Calendar.MINUTE, 0);
		Initial.set(Calendar.HOUR_OF_DAY, Initial.get(Calendar.HOUR_OF_DAY) + 1);
		calendarBegin = (intent.getExtras().get("CALENDARBEGIN") != null) ? (Calendar) intent
				.getExtras().get("CALENDARBEGIN") : Initial;

		End.set(Calendar.MINUTE, 0);
		End.set(Calendar.HOUR_OF_DAY, End.get(Calendar.HOUR_OF_DAY) + 2);
		calendarEnd = (intent.getExtras().get("CALENDAREND") != null) ? (Calendar) intent
				.getExtras().get("CALENDAREND") : End;

		btnDateBegin = (Button) findViewById(R.id.btnDateBegin);
		btnDateBegin.setText(Utility.getDate(calendarBegin));

		btnTimeBegin = (Button) findViewById(R.id.btnTimeBegin);
		btnTimeBegin.setText(Utility.getTime(calendarBegin));

		btnTimeEnd = (Button) findViewById(R.id.btnTimeEnd);
		btnTimeEnd.setText(Utility.getTime(calendarEnd));

		// btnConfirm = (Button)findViewById(R.id.btnConfirm);
	}

	private void Events() {

		scheduleBackHeaderButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnDateBegin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_BEGIN);
			}
		});

		btnTimeBegin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_BEGIN);
			}
		});

		btnTimeEnd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_END);
			}
		});

		/*btnConfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (Validate()) {

					Intent intent = new Intent();

					intent.putExtra("CALENDARBEGIN", calendarBegin);
					intent.putExtra("CALENDAREND", calendarEnd);

					setResult(1, intent);
					btnConfirm.setVisibility(View.GONE);
					finish();
				}
			}
		});
*/
		dateListenerBegin = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				calendarBegin.set(year, monthOfYear, dayOfMonth);
				calendarEnd.set(year, monthOfYear, dayOfMonth);

				btnDateBegin.setText(Utility.getDate(calendarBegin));
				dismissDialog(DATE_BEGIN);
			}
		};

		timeListenerBegin = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				calendarBegin.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendarBegin.set(Calendar.MINUTE, minute);

				btnTimeBegin.setText(Utility.getTime(calendarBegin));
				dismissDialog(TIME_BEGIN);
			}
		};

		timeListenerEnd = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				calendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendarEnd.set(Calendar.MINUTE, minute);

				btnTimeEnd.setText(Utility.getTime(calendarEnd));
				dismissDialog(TIME_END);
			}
		};
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_BEGIN:
			return new DatePickerDialog(this, dateListenerBegin,
					calendarBegin.get(Calendar.YEAR),
					calendarBegin.get(Calendar.MONTH),
					calendarBegin.get(Calendar.DAY_OF_MONTH));

		case TIME_BEGIN:
			return new TimePickerDialog(this, timeListenerBegin,
					calendarBegin.get(Calendar.HOUR_OF_DAY),
					calendarBegin.get(Calendar.MINUTE), true);
		case TIME_END:
			return new TimePickerDialog(this, timeListenerEnd,
					calendarEnd.get(Calendar.HOUR_OF_DAY),
					calendarEnd.get(Calendar.MINUTE), true);
		}
		return null;
	}

	private boolean Validate() {

		if (calendarBegin.after(calendarEnd)) {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					ScreenSchedule.this);
			alertDialog.setTitle(R.string.ScheduleValidadeAlertTitle);
			alertDialog.setMessage(R.string.ScheduleValidadeDateAfter);
			alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
			alertDialog.setNeutralButton(R.string.OK, null);
			alertDialog.show();

			return false;
		}
		return true;
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