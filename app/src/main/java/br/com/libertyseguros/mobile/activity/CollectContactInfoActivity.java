/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.BoletimOcorrenciaHelper;
import br.com.libertyseguros.mobile.database.DriverHelper;
import br.com.libertyseguros.mobile.database.WitnessHelper;
import br.com.libertyseguros.mobile.model.BoletimDeOcorrencia;
import br.com.libertyseguros.mobile.model.Driver;
import br.com.libertyseguros.mobile.model.Witness;

/**
 * Displays the event contacts info for the event specified by extra EVENT_NAME_ID.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class CollectContactInfoActivity extends LibertyMobileApp implements OnClickListener, OnItemClickListener
{
	// private static final String TAG = CollectContactInfoActivity.class.getName();

	private ArrayList<Driver> otherDrivers;

	private ArrayList<BoletimDeOcorrencia> boletimDeOcorrencia;

	private ArrayList<Witness> witnesses;

	/**
	 * Gets any existing contacts and sets them up to display in the appropriate lists
	 */
	private void getContactsForDisplay()
	{
		try {
			// Log.v(TAG, ">>> getContactsForDisplay()");

			// Get all of the contacts for the event id
			Long eventId = getCurrentEvent().getId();
			otherDrivers = DriverHelper.getByEvent(getApplicationContext(), eventId);
			boletimDeOcorrencia = BoletimOcorrenciaHelper.getByEvent(getApplicationContext(), eventId);
			witnesses = WitnessHelper.getByEvent(getApplicationContext(), eventId);

			// Set up the list views to display the contacts and react to selections
			LinearLayout driversList = (LinearLayout) findViewById(R.id.other_driver_list);
			populateDriverList(driversList, otherDrivers);

			LinearLayout policeInfoList = (LinearLayout) findViewById(R.id.police_info_list);
			populatePoliceList(policeInfoList, boletimDeOcorrencia);

			LinearLayout witnessList = (LinearLayout) findViewById(R.id.witness_list);
			populateWitnessList(witnessList, witnesses);

			// Log.v(TAG, ">>> getContactsForDisplay()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Responds to the click of a button in the view and performs the action associated with the button that was
	 * clicked. In this case, listens for the click of one of the add buttons and invokes the activity associated with
	 * the specific button clicked.
	 * 
	 * @param v
	 *            the View that was clicked, in this case a button
	 * @see OnClickListener#onClick(View)
	 */
	@Override
	public void onClick(View v)
	{
		try {
			// Log.v(TAG, ">>> onClick(View v)");

			// Figure out which button was pushed so we know which page to go to
			if (v.equals(findViewById(R.id.add_other_driver_button)))
			{
				// Go to the add other driver view and tell it that this is a new driver
				Intent i = new Intent(getApplicationContext(), OtherDriverActivity.class);
				i.putExtra(EXTRA_NAME_NEW, true);
				startActivity(i);
			}
			else if (v.equals(findViewById(R.id.add_witness_button)))
			{
				// Go to the add activity_witness view and tell it that this is a new activity_witness
				Intent i = new Intent(getApplicationContext(), WitnessActivity.class);
				i.putExtra(EXTRA_NAME_NEW, true);
				startActivity(i);
			}
			else if (v.equals(findViewById(R.id.add_police_info_button)))
			{
				// Go to the add police info view and tell it that this is a new police info
				Intent i = new Intent(getApplicationContext(), PoliceInformationActivity.class);
				i.putExtra(EXTRA_NAME_NEW, true);
				startActivity(i);
			}

			// Log.v(TAG, "<<< onClick(View v)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Initializes the view, setting the layout to the collect contact info layout. Also builds the navigation bar and
	 * adds listeners to the buttons in the view and retreives any existing contacts for the current event and adds them
	 * to the display.
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

			// Create the view with the collect contact info layout
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_collect_contact_info);

			setUpNavigationBarTitleOnly(getString(R.string.contact_info_title));

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Responds to the selection of an existing contact and starts the activity for the contact type sending it the id
	 * of the selected contact (specified by {@link EXTRA_NAME_ID}).
	 * 
	 * @param parent
	 *            The AdapterView where the click happened.
	 * @param view
	 *            The view within the AdapterView that was clicked (this will be a view provided by the adapter)
	 * @param position
	 *            The position of the view in the adapter.
	 * @param id
	 *            The row id of the item that was clicked.
	 * @see OnItemClickListener#onItemClick(AdapterView,
	 *      View,int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		try {
			// Log.v(TAG, ">>> onItemClick(AdapterView<?> adapter, View view, int position, long id)");

			// Find out which list they clicked
			LinearLayout layout = (LinearLayout) view;
			View v = (View) layout.getParent();

			// Display the item they clicked
			if (v.equals(findViewById(R.id.other_driver_list)))
			{
				// Go to the other driver view and tell it to display this driver
				Intent i = new Intent(getApplicationContext(), OtherDriverActivity.class);
				i.putExtra(EXTRA_NAME_ID, id);
				startActivity(i);
			}
			else if (v.equals(findViewById(R.id.witness_list)))
			{
				// Go to the activity_witness view and tell it to display this activity_witness
				Intent i = new Intent(getApplicationContext(), WitnessActivity.class);
				i.putExtra(EXTRA_NAME_ID, id);
				startActivity(i);
			}
			else if (v.equals(findViewById(R.id.police_info_list)))
			{
				// Go to the police info view and tell it to display this police info
				Intent i = new Intent(getApplicationContext(), PoliceInformationActivity.class);
				i.putExtra(EXTRA_NAME_ID, id);
				startActivity(i);
			}

			// Log.v(TAG, "<<< onItemClick(AdapterView<?> adapter, View view, int position, long id)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Refreshes the view when the activity is resumed. Because activities that open from this activity can update the
	 * data in this activity, it is necessary to update itself each time it displays to make sure it has the most recent
	 * data.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		try {
			// Log.v(TAG, ">>> onResume()");

			super.onResume();

			// refresh the page on returning to make sure we have the most up to date lists
			getContactsForDisplay();

			prepareButtons();

			// Log.v(TAG, "<<< onResume()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will populate a linear layout with the contacts in the array list
	 * 
	 * @param layout
	 * @param contacts
	 */
	private void populateDriverList(LinearLayout layout, ArrayList<Driver> drivers)
	{
		try {
			layout.removeAllViews();
			Iterator<Driver> driverIterator = drivers.iterator();
			while (driverIterator.hasNext())
			{
				Driver driver = driverIterator.next();

				populateLayoutFrom(layout, driver.getContact().getFirstName() + " " + driver.getContact().getLastName(),
						driver.getId());
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param layout
	 * @param driver
	 */
	private void populateLayoutFrom(LinearLayout layout, String display, Long id)
	{
		try {
			LinearLayout itemLayout =
					(LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.text_view_list_item, layout,
							false);
			Button button = (Button) itemLayout.findViewById(R.id.btn_list_item);
			button.setId(id.intValue());
			button.setClickable(true);
			button.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// Log.v(TAG, ">>> onClick()");
					showListItem(v);
					// Log.v(TAG, "<<< onClick()");
				}
			});

			button.setText(display);

			layout.addView(itemLayout);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will populate a linear layout with the contacts in the array list
	 * 
	 * @param layout
	 * @param contacts
	 */
	private void populatePoliceList(LinearLayout layout, ArrayList<BoletimDeOcorrencia> boletimDeOcorrenciaList)
	{
		try {
			layout.removeAllViews();
			Iterator<BoletimDeOcorrencia> policeIterator = boletimDeOcorrenciaList.iterator();
			while (policeIterator.hasNext())
			{
				BoletimDeOcorrencia boletimDeOcorrencia = policeIterator.next();

				populateLayoutFrom(layout, boletimDeOcorrencia.getEntidade(), boletimDeOcorrencia.getId());
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will populate a linear layout with the contacts in the array list
	 * 
	 * @param layout
	 * @param contacts
	 */
	private void populateWitnessList(LinearLayout layout, ArrayList<Witness> witnessesList)
	{
		try {
			layout.removeAllViews();
			Iterator<Witness> witnessIterator = witnessesList.iterator();
			while (witnessIterator.hasNext())
			{
				Witness witness = witnessIterator.next();

				populateLayoutFrom(layout, witness.getContact().getFirstName() + " " + witness.getContact().getLastName(),
						witness.getId());
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets up the buttons on the page to show only if appropriate and add listeners if showing
	 */
	private void prepareButtons()
	{
		try {
			// Log.v(TAG, ">>> prepareButtons()");

			String status = getCurrentEvent().getEventStatus();

			Button addOtherDriver = (Button) findViewById(R.id.add_other_driver_button);
			if (otherDrivers.size() > 1 || Constants.EVENT_STATUS_SUBMITTED.equals(status))
			{
				// Only twelve drivers are allowed, so hide the button if there are already twelve
				findViewById(R.id.ll_add_other_driver_button).setVisibility(View.GONE);
			}
			else
			{
				addOtherDriver.setOnClickListener(CollectContactInfoActivity.this);
				findViewById(R.id.ll_add_other_driver_button).setVisibility(View.VISIBLE);
			}

			Button addPoliceInfo = (Button) findViewById(R.id.add_police_info_button);
			if (boletimDeOcorrencia.size() > 1 || Constants.EVENT_STATUS_SUBMITTED.equals(status))
			{
				// Only one police info is allowed, so hide the button if there is already one
				findViewById(R.id.ll_add_police_information_button).setVisibility(View.GONE);
			}
			else
			{
				// Otherwise add the listener to the button
				addPoliceInfo.setOnClickListener(CollectContactInfoActivity.this);
				findViewById(R.id.ll_add_police_information_button).setVisibility(View.VISIBLE);
			}

			Button addWitness = (Button) findViewById(R.id.add_witness_button);
			if (witnesses.size() > 1 || Constants.EVENT_STATUS_SUBMITTED.equals(status))
			{
				// Only two witnesses are allowed, so hide the button if there are already two
				findViewById(R.id.ll_add_witness_button).setVisibility(View.GONE);
			}
			else
			{
				// Otherwise add the listener to the button
				addWitness.setOnClickListener(CollectContactInfoActivity.this);
				findViewById(R.id.ll_add_witness_button).setVisibility(View.VISIBLE);
			}

			// Log.v(TAG, "<<< prepareButtons()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will display the correct contact information depending on the view that was clicked.
	 * 
	 * @param view
	 *            the view clicked
	 */
	public void showListItem(View view)
	{
		try {
			Long id = new Long(view.getId());
			View parent = (View) view.getParent().getParent();

			// Display the item they clicked
			if (parent.equals(findViewById(R.id.other_driver_list)))
			{
				// Go to the other driver view and tell it to display this driver
				Intent i = new Intent(getApplicationContext(), OtherDriverActivity.class);
				i.putExtra(EXTRA_NAME_ID, id);
				startActivity(i);
			}
			else if (parent.equals(findViewById(R.id.witness_list)))
			{
				// Go to the activity_witness view and tell it to display this activity_witness
				Intent i = new Intent(getApplicationContext(), WitnessActivity.class);
				i.putExtra(EXTRA_NAME_ID, id);
				startActivity(i);
			}
			else if (parent.equals(findViewById(R.id.police_info_list)))
			{
				// Go to the police info view and tell it to display this police info
				Intent i = new Intent(getApplicationContext(), PoliceInformationActivity.class);
				i.putExtra(EXTRA_NAME_ID, id);
				startActivity(i);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
    /**
     * Quando voltar precisa marcar RESULT_OK
     */
	public void voltar(View v){
		try {
			
			setResult(RESULT_OK);
			
			super.voltar(v);
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}
}
