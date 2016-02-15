/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.FieldUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.AddressLocation;

/**
 * Displays the time and activity_location info. If this is a home claim and the user's info has been filled in, the user's
 * contact address will be used as the activity_location.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class AddressLocationActivity extends LibertyMobileApp implements OnClickListener, DialogInterface.OnClickListener
{
	private static final int LOCATION_INTENT = 0;

	private EditText city;

	private EditText street;

	private EditText zip;

	// << EPO
	private boolean  wantSave;
	private Address address;
	// >>

	/**
	 * This method will build a activity_location name from the data currently displayed on the page.
	 * 
	 * @return activity_location name
	 */
	private String getLocationNameFromView()
	{
		try {
			StringBuffer location = new StringBuffer();
			if (!ValidationUtils.isStringEmpty(street.getText().toString()))
			{
				location.append(street.getText().toString());
			}

			if (!ValidationUtils.isStringEmpty(city.getText().toString()))
			{
				if (location.length() != 0)
				{
					location.append(", ");
				}
				location.append(city.getText().toString());
			}

			if (!ValidationUtils.isStringEmpty(zip.getText().toString()))
			{
				if (location.length() != 0)
				{
					location.append(", ");
				}
				location.append(zip.getText().toString());
			}

			if (location.length() > 0)
			{
				return location.toString();
			}

			return null;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	/**
	 * Checks to see if the user has made any changes to the form since it was last saved
	 * 
	 * @return true if the form has changed, false if it has not
	 */
	private boolean isFormChanged()
	{
		try {
			boolean returnVal = false;

			// << EPO
			//        // Create the model objects from the entered data
			//        Address address = getCurrentEvent().getLocation();
			//
			//        if (address == null)
			//        {
			//            address = new Address();
			//        }
			// >>

			Address newAddress = new Address();
			newAddress.setCity(city.getText().toString());
			newAddress.setStreetAddress(street.getText().toString());
			newAddress.setZipCode(zip.getText().toString());
			//        newAddress.setState(state.getText().toString());

			if (!newAddress.isEqualTo(address))
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
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @see Activity#onActivityResult(int, int, Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == LOCATION_INTENT && resultCode == Activity.RESULT_OK) {
				if (data != null)
				{
					AddressLocation location = (AddressLocation) data.getSerializableExtra(AddressLocation.LOCATION);
				    double latitude = data.getDoubleExtra(AddressLocation.LATITUDE, 0);
				    double longitude = data.getDoubleExtra(AddressLocation.LONGITUDE, 0);
				    latitude = latitude / 1000000.0;
				    longitude = longitude / 1000000.0;

				    address.setCity(location.getCity());
					address.setLatitude(new Double(latitude));
					address.setLongitude(new Double(longitude));
					address.setStreetAddress(location.getStreet());
					address.setZipCode(location.getPostalCode());
//					address.setState(activity_location.getState());

					getCurrentEvent().setLocation(address);
					EventHelper.update(getApplicationContext(), getCurrentEvent());

					prefillLocation();
				}
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}

		// Log.v(TAG, "<<< onActivityResult()");

	}

	/**
	 * Responds to the user's selection in an alert dialog, in this case confirming that the user wants to cancel their
	 * changes.
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
			// Log.v(TAG, ">>> onClick(DialogInterface dialog, int which)");

			// << EPO

			// The user selected yes
			//        if (which == -1)
			//        {
			//            // Finish the activity
			//            finish();
			//        }

			if (which == -1) {
				wantSave = true;
				salvaEfecha(null);
			} else if (which == -2) {
				// Finish the activity
				finish();
			}
			// >>
		} catch (Exception e) {
			Util.showException(this, e);
		}

		// Log.v(TAG, "<<< onClick(DialogInterface dialog, int which)");
	}

	/**
	 * Responds to the click of a button in the view and performs the action associated with the button that was
	 * clicked. If the right done button was pushed, the event is saved. If the time button was pushed, the date/time
	 * picker dialog is produced. If the find activity_location button was pushed, the map view activity is started.
	 * 
	 * @param v
	 *            the View that was clicked, in this case a button
	 * @see OnClickListener#onClick(View)
	 */
	@Override
	public void onClick(View v)
	{
		try {

			if (v.equals(findViewById(R.id.find_on_map_button)))
			{
				Bundle params = new Bundle();
				Intent it = new Intent(getApplicationContext(), LocationActivity.class);

				params.putDouble(AddressLocation.LATITUDE, address.getLatitude() * 1000000.0);
				params.putDouble(AddressLocation.LONGITUDE, address.getLongitude() * 1000000.0);
				it.putExtras(params);

				startActivityForResult(it, LOCATION_INTENT);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Initializes the view, setting the layout to the time activity_location layout. Also builds the navigation bar, adds
	 * listeners to the buttons in the view and customizes the view depending on the line of business of the event being
	 * worked and the status of the event.
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

			// Create the view with the time activity_location layout
			super.onCreate(savedInstanceState);

			setContentView(R.layout.address_location);

			setUpNavigationBarTitleOnly(getString(R.string.add_location));

			Button locationButton = (Button) findViewById(R.id.find_on_map_button);

			street = (EditText) findViewById(R.id.street_address);
			city = (EditText) findViewById(R.id.city);
			//        state = (EditText) findViewById(R.id.state);
			zip = (EditText) findViewById(R.id.zip_code);

			// fix a tabbing issue
			street.setNextFocusDownId(R.id.city);
			city.setNextFocusDownId(R.id.zip_code);

			// << EPO 
			address = getCurrentEvent().getLocation();

			if (address == null) {
				address = new Address();
			}

			// >>

			prefillLocation();

			// The view should only be editable if the event status is draft
			if (Constants.EVENT_STATUS_DRAFT.equals(getCurrentEvent().getEventStatus()))
			{
				if (Constants.LOB_AUTO.equals(getCurrentEvent().getEventType()))
				{
					locationButton.setOnClickListener(AddressLocationActivity.this);
				}
				else
				{
					locationButton.setVisibility(View.GONE);
				}
			}
			else
			{
				locationButton.setVisibility(View.GONE);

				FieldUtils.disableEditText(street);
				FieldUtils.disableEditText(city);
				//            FieldUtils.disableEditText(state);
				FieldUtils.disableEditText(zip);
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Catches the press of the device back button by the user and saves the form before leaving the Activity
	 * 
	 * @param keyCode
	 *            The value in event.getKeyCode().
	 * @param event
	 *            Description of the key event.
	 * @return true if the form cannot be saved to prevent this event from being propagated further, or false to
	 *         indicate it should continue to be propagated.
	 * @see Activity#onKeyDown(int, KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try {

			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
			{
				if (Constants.EVENT_STATUS_DRAFT.equals(getCurrentEvent().getEventStatus()) && isFormChanged())
				{
					// << EPO
					//                // Ask the user if they want to discard their changes
					//                displayConfirmAlert(AddressLocationActivity.this, getString(R.string.go_back_without_saving),
					//                    getString(R.string.go_back_without_saving_confirmation), getString(R.string.btn_ok),
					//                    getString(R.string.no), AddressLocationActivity.this);

					if(wantSave) {
						salvaEfecha(null);
					}
					else{
						// Ask the user if they want to discard their changes
						displayConfirmAlert(AddressLocationActivity.this,
								getString(R.string.aviso),
								getString(R.string.deseja_salvar_alteracoes),
								getString(R.string.btn_ok),
								getString(R.string.btn_cancelar),
								AddressLocationActivity.this);

						return false;
					}

					// >>

					return false;
				}
			}

			return super.onKeyDown(keyCode, event);
			
		} catch (Exception e) {
			Util.showException(this, e);
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * Looks for an existing activity_location on the event and prefills the page with that data, if present
	 */
	private void prefillLocation()
	{
		try {
			// << EPO
			//        // Prefill the Address if already present
			//        Address address = getCurrentEvent().getLocation();
			// >>

			if (address != null)
			{
				// use the existing address info to prefill the screen
				street.setText(address.getStreetAddress());
				city.setText(address.getCity());
				//            state.setText(address.getState());
				zip.setText(address.getZipCode());
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * Saves the current form data to the database
	 * 
	 * @return true if the form is saved, false if the form cannot be saved due to missing info
	 */
	private boolean saveForm()
	{
		try {
			// Log.v(TAG, ">>> saveForm(boolean errorOnEmpty)");

			boolean returnVal = true;

			boolean needComma = false;

			StringBuffer missingInfo = new StringBuffer();

			// << EPO
			//        // Check for required fields or an empty form
			//        Address address = getCurrentEvent().getLocation();
			//        if (address == null)
			//        {
			//            address = new Address();
			//        }
			// >>

			address.setStreetAddress(street.getText().toString());
			address.setCity(city.getText().toString());
			//        address.setState(state.getText().toString());
			address.setZipCode(zip.getText().toString());

			//  << EPO 
			//        if (getCurrentEvent().getEventDateTime() == null && address.isEmpty() && errorOnEmpty)
			if (address.isEmpty()) 
			{
				returnVal = false;
				missingInfo.append(getString(R.string.address_label));
				missingInfo.append(", ");
				missingInfo.append(getMinAddressReqsList(true, true, true, false));
			} 
			else 
			{
				//            if (getCurrentEvent().getEventDateTime() == null) {
				//                returnVal = false;
				//                missingInfo.append(getString(R.string.incident_date_time_label_detail));
				//                needComma = true;
				//            }
				// >>
				if (!address.minRequirementsMet())
				{
					returnVal = false;

					if (needComma) {
						missingInfo.append(", ");
					}

					missingInfo.append(getMinAddressReqsList(	ValidationUtils.isStringEmpty(address.getStreetAddress()),
							ValidationUtils.isStringEmpty(address.getCity()),
							ValidationUtils.isStringEmpty(address.getState()), false));
				}
			}
			if (returnVal)
			{
				// Save the data
				getCurrentEvent().setLocation(address);
				EventHelper.update(getApplicationContext(), getCurrentEvent());
			}
			else
			{
				// << EPO
				address.reset();
				// >>

				// alert the user they haven't met the minimum requirements
				displayInfoAlert(	AddressLocationActivity.this, 
						getString(R.string.por_favor_insira),
						missingInfo.toString(), 
						getString(R.string.btn_ok), 
						AddressLocationActivity.this);
			}

			// Log.v(TAG, "<<< saveForm()");

			return returnVal;

		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}

	}

	/**
	 * Quando clicka em salvar (button_bar.xml).
	 * Quando clicka em voltar e deseja salvar.
	 * 
	 * @param v
	 */
	public void salvaEfecha(View v)
	{
		try {

			if (saveForm())
			{
				setResult(RESULT_OK);
				finish();
			}
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