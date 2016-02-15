/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.database.EventHelper;

/**
 * Provides the view for inputing detail information when the claim type other was selected.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class DateTimePickerActivity extends LibertyMobileApp implements DialogInterface.OnClickListener, OnTimeChangedListener, OnDateChangedListener, DialogInterface.OnCancelListener
{

	private int day;

	private int hour;

	private int minute;

	private int month;

	private boolean validating = false;

	private int year;

	private Timestamp eventTimestamp;

	private AlertDialog alertDialog;
	
	private View layout;

	/**
	 * This method will return true if the current time is different that it was onCreate
	 * 
	 * @return
	 */
	private boolean isFormChanged()
	{
		try {
			boolean returnVal = false;
			Timestamp currentTimestamp =
					new Timestamp(this.year - 1900, this.month, this.day, this.hour, this.minute, 0, 0);
			if (!currentTimestamp.equals(eventTimestamp))
			{
				returnVal = true;
			}

			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * @param dialog
	 * @see DialogInterface.OnCancelListener#onCancel(DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog)
	{
		try {
			if (!isFormChanged())
			{
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Responds to the user's selection in an alert dialog, in this case the selection of the Ok button after setting
	 * the incident date and time. The selected date and time are saved to the database as the event date and time for
	 * the current claim being worked.
	 * 
	 * @param dialog
	 *            the dialog to which the user responded
	 * @param which
	 *            the selection the user made
	 * @see DialogInterface.OnClickListener#onClick(DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		try {
			// OK (neutral) = -3
			// No on Confirm = -2
			// Yes on Confirm = -1
			switch (which)
			{
			case -3:
				// Set the date and time of the current event to the user selected date and time
				getCurrentEvent().setEventDateTime(
						new Timestamp(this.year - 1900, this.month, this.day, this.hour, this.minute, 0, 0));

				// Save the data to the database
				EventHelper.update(getApplicationContext(), getCurrentEvent());

				setResult(RESULT_OK);
				finish();
				break;
			case -2:
				if (alertDialog.isShowing())
				{
					// Cancel on Date/Time dialog was pressed. Do the same thing as when the back button is pressed.
					onCancel(dialog);
				}
				else
				{
					// No on the confirmation dialog was pressed
					// The dialog has already been cancelled, we need to redisplay it
					alertDialog.show();
				}
				break;
			case -1:
				// User does not want to save changes, finish
				finish();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * Initializes the view, setting the layout to the claim type layout, with a dialog theme and the background
	 * blurred. Also adds listeners to the buttons in the view.
	 * 
	 * @param savedInstanceState
	 *            if the activity is being re-initialized after previously being shut down then this Bundle contains the
	 *            data it most recently supplied in {@link Activity.onSaveInstanceState(Bundle)}. Note: Otherwise it is
	 *            null.
	 * @see com.lmig.pm.internet.mobile.android.libertymutual.LibertyMutualActivity#onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {

			super.onCreate(savedInstanceState);

			// Get the previously selected date and time if available
			if(getCurrentEvent() != null)
				eventTimestamp = getCurrentEvent().getEventDateTime();

			if (eventTimestamp == null)
			{
				eventTimestamp = new Timestamp(new Date().getTime());
				eventTimestamp.setSeconds(0);
				eventTimestamp.setNanos(0);
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(eventTimestamp);

			// Initialize the selections
			this.year = cal.get(Calendar.YEAR);
			this.month = cal.get(Calendar.MONTH);
			this.day = cal.get(Calendar.DAY_OF_MONTH);
			this.hour = cal.get(Calendar.HOUR_OF_DAY);
			this.minute = cal.get(Calendar.MINUTE);

			// Build the dialog
			AlertDialog.Builder builder;

			LayoutInflater inflater = (LayoutInflater) DateTimePickerActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.activity_date_time_picker, (ViewGroup) findViewById(R.id.date_time_root));

			// Preset the date and time
			DatePicker date = (DatePicker) layout.findViewById(R.id.date_picker);
			date.init(this.year, this.month, this.day, DateTimePickerActivity.this);

			TimePicker time = (TimePicker) layout.findViewById(R.id.time_picker);
			time.setCurrentHour(this.hour);
			time.setCurrentMinute(this.minute);
			time.setOnTimeChangedListener(DateTimePickerActivity.this);

			// Show the dialog
			builder = new AlertDialog.Builder(DateTimePickerActivity.this);
			builder.setView(layout);
			builder.setNeutralButton(R.string.btn_ok, this);
			builder.setNegativeButton(R.string.btn_cancelar, this);

			alertDialog = builder.create();
			alertDialog.setTitle(getString(R.string.add_time));
			alertDialog.setOnCancelListener(DateTimePickerActivity.this);
			alertDialog.show();

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Listens for a change to a date picker. The selected date will be checked to make sure it and the currently
	 * selected time are not after todays date and time. If the selected date is valid, the year, month and day will be
	 * set as the current year month and day. Otherwise, the date will be set to today.
	 * 
	 * @param picker
	 *            The view associated with this listener.
	 * @param selectedYear
	 *            The year that was set.
	 * @param selectedMonth
	 *            The month that was set (0-11) for compatibility with Calendar.
	 * @param selectedDay
	 *            The day of the month that was set.
	 * @see OnDateChangedListener#onDateChanged(DatePicker, int, int, int)
	 */
	@Override
	public void onDateChanged(DatePicker picker, int selectedYear, int selectedMonth, int selectedDay)
	{
		try {

			// don't do these checks if we are in the process of making changes due to validation
			if (!validating)
			{
				validating = true;

				// don't let the user pick a future date
				Calendar selectedDate = Calendar.getInstance();
				selectedDate.set(selectedYear, selectedMonth, selectedDay);

				Calendar currentDate = Calendar.getInstance();

				if (selectedDate.after(currentDate))
				{
					// The user tried to pick a date beyond today, reset the selected date to today
					picker.updateDate(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
							currentDate.get(Calendar.DAY_OF_MONTH));

					selectedDate.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
							currentDate.get(Calendar.DAY_OF_MONTH));
				}

				// The user may have selected a valid date but the time could still be after the current time, so add
				// the time to the selected date and revalidate
				selectedDate.set(Calendar.HOUR_OF_DAY, this.hour);
				selectedDate.set(Calendar.MINUTE, this.minute);

				if (selectedDate.after(currentDate))
				{
					// The user selected an invalid time, but a valid date. Set the picker to yesterday and assume
					// the time was correct
					currentDate.add(Calendar.DAY_OF_MONTH, -1);

					picker.updateDate(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
							currentDate.get(Calendar.DAY_OF_MONTH));

					this.year = currentDate.get(Calendar.YEAR);
					this.month = currentDate.get(Calendar.MONTH);
					this.day = currentDate.get(Calendar.DAY_OF_MONTH);
				}
				else
				{
					// The selection is valid, set the current selected date to the datepicker date
					this.year = selectedYear;
					this.month = selectedMonth;
					this.day = selectedDay;
				}

				validating = false;
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * Listens for a change to a time picker. The selected time will be checked to make sure it and the currently
	 * selected date are not after todays date and time. If the selected time is valid, the hour and minute will be set
	 * as the current hour and minute. Otherwise, the time will be set to the current time.
	 * 
	 * @param picker
	 *            The view associated with this listener.
	 * @param selectedHour
	 *            The hour that was set.
	 * @param selectedMinute
	 *            The minute that was set.
	 * @see OnTimeChangedListener#onTimeChanged(TimePicker, int, int)
	 */
	@Override
	public void onTimeChanged(TimePicker picker, int selectedHour, int selectedMinute)
	{
		try {

			// don't do these checks if we are in the process of making changes due to validation
			if (!validating)
			{
				validating = true;

				// don't let the user pick a future date
				Calendar selectedDate = Calendar.getInstance();
				selectedDate.set(this.year, this.month, this.day, selectedHour, selectedMinute);

				Calendar currentDate = Calendar.getInstance();

				if (selectedDate.after(currentDate))
				{
					// The user selected an invalid time. Set the picker to the time right now
					picker.setCurrentHour(currentDate.get(Calendar.HOUR_OF_DAY));
					picker.setCurrentMinute(currentDate.get(Calendar.MINUTE));

					this.hour = currentDate.get(Calendar.HOUR_OF_DAY);
					this.minute = currentDate.get(Calendar.MINUTE);
				}
				else
				{
					// Set the current selected time to the timepicker time
					this.hour = selectedHour;
					this.minute = selectedMinute;
				}

				validating = false;
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		try {
			
			super.onConfigurationChanged(newConfig);
			
			// Ajusta o layout antes de exibir
			LinearLayout dateTimeRoot = (LinearLayout) layout.findViewById(R.id.date_time_root);
			if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				dateTimeRoot.setOrientation(LinearLayout.VERTICAL);
			} else {
				dateTimeRoot.setOrientation(LinearLayout.HORIZONTAL);
			}
		
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
}