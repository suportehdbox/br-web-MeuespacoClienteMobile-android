/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.textwatcher.PhoneNumberTextWatcher;
import br.com.libertyseguros.mobile.common.util.FieldUtils;
import br.com.libertyseguros.mobile.common.util.PhoneNumberUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.DriverHelper;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.Driver;
import br.com.libertyseguros.mobile.model.Policy;
import br.com.libertyseguros.mobile.model.Vehicle;

/**
 * Displays the Other Driver view. Looks for extra EXTRA_NAME_NEW specifying that this is a new driver or EXTRA_NAME_ID
 * specifying the id of an existing driver and either creates a new driver or retrieves the existing one accordingly.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class OtherDriverActivity extends LibertyMobileApp implements /*OnClickListener,*/ DialogInterface.OnClickListener {

	private static final String DELETE = "DELETE";

	// private static final String TAG = OtherDriverActivity.class.getName();

	private EditText color;

	private String currentAction;

	private Driver driver = null;

	private EditText emailAddress;

	private EditText firstName;

	private EditText insuranceCompany;

	private EditText lastName;

	private EditText make;

	private EditText model;

	private EditText note;

	private EditText homePhone;

	private EditText mobilePhone;

	private EditText policyNumber;

	private EditText registrationNumber;

	private boolean submitted = false;

	private EditText year;

	// << EPO
	private boolean isNew = false;

	private boolean wantSave;
	// >>

	/**
	 * Responds to the user's selection in an alert dialog, in this case confirming that the user wants to cancel their
	 * changes or delete a driver.
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

			// The user selected yes
			if (which == -1)
			{
				// If the user clicked delete, delete the driver
				if (DELETE.equals(currentAction))
				{
					DriverHelper.delete(getApplicationContext(), driver.getId());
					// << EPO
					// Finish the activity
					finish();
				} 
				else 
				{
					wantSave = true;
					salvaEfecha(null);
				}
			} 
			else if (which == -2 &&  !DELETE.equals(currentAction))
			{
				// Finish the activity
				finish();
			}

			// Log.v(TAG, "<<< onClick(DialogInterface dialog, int which)");
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}
	// >>   

	// << EPO
	//    /**
	//     * Responds to the click of a button in the view and performs the action associated with the button that was
	//     * clicked. If the user clicked the done button, the form is saved and the Activity is finished. If the user clicked
	//     * the delete button, the user is asked if they want to delete the current driver.
	//     * 
	//     * @param v
	//     *            the View that was clicked, in this case a button
	//     * @see android.view.View.OnClickListener#onClick(android.view.View)
	//     */
	//    @Override
	//    public void onClick(View v)
	//    {
	//        // Log.v(TAG, ">>> onClick(View v)");
	//
	//        // If the user clicked save, save the form and finish
	//        if (v.equals(findViewById(R.id.done_button)))
	//        {
	//            if (saveForm(true))
	//            {
	//                finish();
	//            }
	//        }
	//        // If the user clicked delete, confirm the deletion
	//        else if (v.equals(findViewById(R.id.delete_driver_button)))
	//        {
	//            currentAction = DELETE;
	//
	//            displayConfirmAlert(OtherDriverActivity.this, 
	//            					getString(R.string.delete),
	//        						getString(R.string.excluir_contato), 
	//        						getString(R.string.btn_ok), 
	//        						getString(R.string.no),
	//        						OtherDriverActivity.this);
	//        }
	//
	//        // Log.v(TAG, "<<< onClick(View v)");
	//    }
	// >>

	/**
	 * Initializes the view, setting the layout to the other driver layout. Also builds the navigation bar and adds
	 * listeners to the buttons in the view. The navigation bar and delete button will differ depending on whether this
	 * is a new driver (specified by {@link EXTRA_NAME_NEW}) or an existing one (specified by {@link EXTRA_NAME_ID})
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
			// Log.v(TAG, ">>> onCreate(Bundle savedInstanceState)");

			// Create the view with the other driver layout
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_other_driver);

			// determine if this is a submitted claim
			if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()))
			{
				submitted = true;
			}
			// determine if we are working on a new or existing driver
			Bundle extras = getIntent().getExtras();
			isNew = extras.getBoolean(EXTRA_NAME_NEW);

			// << EPO
			//        // The title and delete button will differ based on whether or not the driver is new
			//        Button deleteButton = (Button) findViewById(R.id.delete_driver_button);
			//
			//        if (isNew) {
			//            deleteButton.setVisibility(View.GONE);
			//        } else {
			//            Long id = extras.getLong(EXTRA_NAME_ID);
			//            driver = DriverHelper.get(getApplicationContext(), id);
			//
			//            deleteButton.setOnClickListener(OtherDriverActivity.this);
			//
			//            if (submitted)
			//            {
			//                deleteButton.setVisibility(View.GONE);
			//            }
			//        }

			if (!isNew) {
				Long id = extras.getLong(EXTRA_NAME_ID);
				driver = DriverHelper.get(getApplicationContext(), id);
			}
			// >>

			// Set up the navigation bar with a title.
			setUpNavigationBarTitleOnly(getString(R.string.other_driver_title));

			// << EPO
			//        Button doneButton = (Button) findViewById(R.id.done_button);
			//        doneButton.setOnClickListener(OtherDriverActivity.this);
			// >>

			// Prepare the form
			setUpInputFields();

			// Format Phone Numbers
			mobilePhone.addTextChangedListener(new PhoneNumberTextWatcher());
			homePhone.addTextChangedListener(new PhoneNumberTextWatcher());

			// Prefill the form with any previously entered data
			setUpDriver();

			// Log.v(TAG, "<<< onCreate(Bundle savedInstanceState)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Catches the press of the device back button by the user and asks the user if they want to save the form before
	 * leaving the Activity
	 * 
	 * @param keyCode
	 *            The value in event.getKeyCode().
	 * @param event
	 *            Description of the key event.
	 * @return true if the form cannot be saved to prevent this event from being propagated further, or false to
	 *         indicate it should continue to be propagated.
	 * @see android.app.Activity#onKeyDown(int, KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try {
			// Log.v(TAG, ">>> onKeyDown(int keyCode, KeyEvent event)");

			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
			{
				if (Constants.EVENT_STATUS_DRAFT.equals(getCurrentEvent().getEventStatus()) && isFormChanged())
				{

					// << EPO caso bot�o voltar: Salva e volta

					//                // Ask the user if they want to discard their changes
					//                displayConfirmAlert(OtherDriverActivity.this, getString(R.string.go_back_without_saving),
					//                    getString(R.string.go_back_without_saving_confirmation), getString(R.string.btn_ok),
					//                    getString(R.string.no), OtherDriverActivity.this);
					//                return false;
					//
					if(wantSave) {
						salvaEfecha(null);
					}
					else{
						// Ask the user if they want to discard their changes
						displayConfirmAlert(OtherDriverActivity.this,
								getString(R.string.aviso),
								getString(R.string.deseja_salvar_alteracoes),
								getString(R.string.btn_ok),
								getString(R.string.btn_cancelar),
								OtherDriverActivity.this);

						return false;
					}

					// >>       
				}
			}

			// Log.v(TAG, "<<< onKeyDown(int keyCode, KeyEvent event)");

			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Alerts the user that the minimum requirements of the form have not been met.
	 * 
	 * @param firstMissing
	 *            whether or not the first name field is empty
	 * @param lastMissing
	 *            whether or not the last name field is empty
	 * @param phoneMissing
	 *            whether or not the phone number field is empty
	 */
	private void alertMinRequirements(boolean firstMissing, boolean lastMissing, boolean phoneMissing)
	{
		try {
			// Log.v(TAG, ">>> alertMinRequirements(boolean firstMissing, boolean lastMissing, boolean phoneMissing)");

			StringBuffer missingInfo = new StringBuffer();

			boolean needComma = false;

			if (firstMissing)
			{
				missingInfo.append(getString(R.string.first_name_label));

				needComma = true;
			}
			if (lastMissing)
			{
				if (needComma)
				{
					missingInfo.append(", ");
				}

				missingInfo.append(getString(R.string.last_name_label));

				needComma = true;
			}
			if (phoneMissing)
				// << EPO
				//        {
				//            if (needComma)
				//            {
				//                missingInfo.append(", ");
				//            }
				//            ValidationUtils.addValidStringLabel(missingInfo, !needComma);
				//
				//            missingInfo.append(getString(R.string.phone_label));
				//        }
			{
				ValidationUtils.addAnd(missingInfo, needComma);

				missingInfo.append(getString(R.string.phone_label));

				ValidationUtils.addValidStringLabel(missingInfo, needComma);
				needComma = true;
			}
			// >>

			// Tell the user they haven't met the minimum requirements
			displayInfoAlert(	OtherDriverActivity.this,
					getString(R.string.por_favor_insira),
					missingInfo.toString(), 
					getString(R.string.btn_ok),
					OtherDriverActivity.this);

			// Log.v(TAG, "<<< alertMinRequirements(boolean firstMissing, boolean lastMissing, boolean phoneMissing)");
		} catch (Exception e) {
			Util.showException(this, e);
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
			// Log.v(TAG, ">>> isFormChanged()");

			boolean returnVal = false;

			// Create the model objects from the entered data
			Driver newDriver = new Driver();
			newDriver.setInsuranceCompany(insuranceCompany.getText().toString());

			if (!driver.isEqualTo(newDriver))
			{
				returnVal = true;
			}
			else
			{
				Policy newPolicy = new Policy(Constants.LOB_AUTO);
				newPolicy.setPolicyNumber(policyNumber.getText().toString());
				if (!newPolicy.isEqualTo(driver.getPolicy()))
				{
					returnVal = true;
				}
				else
				{
					Contact newContact = new Contact();
					newContact.setFirstName(firstName.getText().toString());
					newContact.setLastName(lastName.getText().toString());
					newContact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString()));
					newContact.setMobilePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString()));
					newContact.setEmailAddress(emailAddress.getText().toString());
					newContact.setNotes(note.getText().toString());

					if (!driver.getContact().isEqualTo(newContact))
					{
						returnVal = true;
					}
					else
					{
						returnVal = isVehicleChanged();
					}
				}
			}
			// Log.v(TAG, "<<< isFormChanged()");

			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Checks to see if the user has made any changes to the vehicle specific fields since it was last saved
	 * 
	 * @return
	 */
	private boolean isVehicleChanged()
	{
		try {
			boolean vehicleChanged = false;
			Vehicle vehicle = new Vehicle();
			vehicle.setYear(year.getText().toString());
			vehicle.setMake(make.getText().toString());
			vehicle.setModel(model.getText().toString());
			vehicle.setColor(color.getText().toString());
			vehicle.setRegistrationNumber(registrationNumber.getText().toString());

			ArrayList<Vehicle> driverVehicles = driver.getPolicy().getPolicyVehicles();

			if (!vehicle.isEmpty() && (driverVehicles == null || driverVehicles.size() == 0))
			{
				vehicleChanged = true;
			}
			else if (driverVehicles != null && driverVehicles.size() > 0 && !vehicle.isEqualTo(driverVehicles.get(0)))
			{
				vehicleChanged = true;
			}

			return vehicleChanged;
			// Log.v(TAG, "<<< isVehicleChanged()");
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Checks to see if the minimum requirements are met and, if not, alerts the user as to what needs to be completed
	 * 
	 * @param errorOnEmpty
	 *            whether or not to error if all fields are empty
	 * @param contact
	 *            the contact to check for minimum requirements
	 * @return
	 */
	private boolean minRequirementsMet(boolean errorOnEmpty, Contact contact)
	{
		try {
			// Log.v(TAG, ">>> minRequirementsMet(boolean errorOnEmpty, Contact contact)");

			boolean returnVal = true;

			// Check for required fields
			if (ValidationUtils.isStringEmpty(contact.getFirstName()) || ValidationUtils.isStringEmpty(contact.getLastName())
					|| (
							(ValidationUtils.isStringEmpty(contact.getHomePhone()) || contact.getHomePhone().length() < 11) 
							&& (ValidationUtils.isStringEmpty(contact.getMobilePhone()) || contact.getMobilePhone().length() < 11))
					) {

				returnVal = false;
				boolean missingFirst = false;
				boolean missingLast = false;
				boolean missingPhone = false;

				if (ValidationUtils.isStringEmpty(contact.getFirstName()))
				{
					missingFirst = true;
				}
				if (ValidationUtils.isStringEmpty(contact.getLastName()))
				{
					missingLast = true;
				}
				String homePhoneText = PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString());
				String mobilePhoneText = PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString());
				if ((ValidationUtils.isStringEmpty(homePhoneText) || homePhoneText.length() < 11) 
						&& (ValidationUtils.isStringEmpty(mobilePhoneText) || mobilePhoneText.length() < 11) )
				{
					missingPhone = true;
				}

				alertMinRequirements(missingFirst, missingLast, missingPhone);
			}

			// Log.v(TAG, "<<< minRequirementsMet(boolean errorOnEmpty, Contact contact)");

			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Saves the current form data to the database
	 * 
	 * @param errorOnEmpty
	 *            whether or not to display an error message for missing information if the form is completely empty. If
	 *            true, the form will not save and an error will be presented. If false, the empty form will not be
	 *            saved and no error will display.
	 * @return true if the form is saved, false if the form cannot be saved due to missing info
	 */
	private boolean saveForm(boolean errorOnEmpty)
	{
		try {
			// Log.v(TAG, ">>> saveForm(boolean errorOnEmpty)");

			boolean returnVal = true;

			// check for minimum requirements
			// Create the model objects from the entered data
			driver.setInsuranceCompany(insuranceCompany.getText().toString());
			Policy policy = driver.getPolicy();
			policy.setPolicyLOB(Constants.LOB_AUTO);
			policy.setPolicyNumber(policyNumber.getText().toString());

			Contact contact = driver.getContact();
			contact.setFirstName(firstName.getText().toString());
			contact.setLastName(lastName.getText().toString());
			contact.setMobilePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString()));
			contact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString()));
			contact.setEmailAddress(emailAddress.getText().toString());
			contact.setNotes(note.getText().toString());

			ArrayList<Vehicle> vehicles = policy.getPolicyVehicles();
			Vehicle vehicle;

			if (vehicles != null && vehicles.size() > 0)
			{
				vehicle = driver.getPolicy().getPolicyVehicles().get(0);
			}
			else
			{
				vehicle = new Vehicle();
				vehicles.add(vehicle);
			}

			vehicle.setYear(year.getText().toString());
			vehicle.setMake(make.getText().toString());
			vehicle.setModel(model.getText().toString());
			vehicle.setColor(color.getText().toString());
			vehicle.setRegistrationNumber(registrationNumber.getText().toString());

			if (driver.isEmpty())
			{
				if (errorOnEmpty)
				{
					alertMinRequirements(true, true, true);
					returnVal = false;
				}
			}
			else if (minRequirementsMet(errorOnEmpty, contact))
			{
				// Insert or update the data to the database
				if (driver.getId() == null)
				{
					long id = DriverHelper.insert(getApplicationContext(), driver, getCurrentEvent().getId());
					driver.setId(id);
				}
				else
				{
					DriverHelper.update(getApplicationContext(), driver);
				}
			}
			else
			{
				returnVal = false;
			}

			if (!returnVal)
			{
				driver.getContact().reset();
			}
			// Log.v(TAG, "<<< saveForm(boolean errorOnEmpty)");

			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Gets any previously saved driver info from the database and prefills the page with the info
	 */
	private void setUpDriver()
	{
		try {
			// Log.v(TAG, ">>> setUpDriver()");

			if (driver == null)
			{
				// No driver exists, so create one
				driver = new Driver();
				driver.setContact(new Contact());
				driver.setPolicy(new Policy(Constants.LOB_AUTO));
				ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
				driver.getPolicy().setPolicyVehicles(vehicles);
			}
			else
			{
				// use the existing driver info to prefill the screen
				insuranceCompany.setText(driver.getInsuranceCompany());

				Contact contact = driver.getContact();
				if (contact == null)
				{
					// the driver doesn't have a contact, so create one
					driver.setContact(new Contact());
				}
				else
				{
					// use the existing contact info to prefill the screen
					firstName.setText(contact.getFirstName());
					lastName.setText(contact.getLastName());
					mobilePhone.setText(PhoneNumberUtils.formatPhoneNumberForDisplay(contact.getMobilePhone()));
					homePhone.setText(PhoneNumberUtils.formatPhoneNumberForDisplay(contact.getHomePhone()));
					emailAddress.setText(contact.getEmailAddress());
					note.setText(contact.getNotes());
				}
				Policy policy = driver.getPolicy();
				if (policy == null)
				{
					// the driver doesn't have a policy, so create one
					driver.setPolicy(new Policy());
					ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
					driver.getPolicy().setPolicyVehicles(vehicles);
				}
				else
				{
					policyNumber.setText(driver.getPolicy().getPolicyNumber());

					ArrayList<Vehicle> vehicles = driver.getPolicy().getPolicyVehicles();

					if (vehicles == null || vehicles.size() < 1)
					{
						// the driver doesn't have a vehicle, so create one
						vehicles = new ArrayList<Vehicle>();
						driver.getPolicy().setPolicyVehicles(vehicles);
					}
					else
					{
						Vehicle vehicle = vehicles.get(0);

						// use the existing vehicle info to prefill the screen
						year.setText(vehicle.getYear());
						make.setText(vehicle.getMake());
						model.setText(vehicle.getModel());
						color.setText(vehicle.getColor());
						registrationNumber.setText(vehicle.getRegistrationNumber());
					}
				}
			}

			if (submitted)
			{
				FieldUtils.disableEditText(firstName);
				FieldUtils.disableEditText(lastName);
				FieldUtils.disableEditTextPhone(homePhone);
				FieldUtils.disableEditTextPhone(mobilePhone);
				FieldUtils.disableEditText(emailAddress);
				FieldUtils.disableEditText(insuranceCompany);
				FieldUtils.disableEditText(policyNumber);
				FieldUtils.disableEditText(note);

				FieldUtils.disableEditText(year);
				FieldUtils.disableEditText(make);
				FieldUtils.disableEditText(model);
				FieldUtils.disableEditText(color);
				FieldUtils.disableEditText(registrationNumber);
			}

			// Log.v(TAG, "<<< setUpDriver()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Populates all of the input field objects from the form
	 */
	private void setUpInputFields()
	{
		try {
			// Log.v(TAG, ">>> setInputFields()");

			// Contact Info
			firstName 		= (EditText) findViewById(R.id.first_name);
			lastName 		= (EditText) findViewById(R.id.last_name);
			mobilePhone 	= (EditText) findViewById(R.id.mobile_phone);
			homePhone 		= (EditText) findViewById(R.id.home_phone);
			emailAddress 	= (EditText) findViewById(R.id.email);
			insuranceCompany= (EditText) findViewById(R.id.insurance_company);
			policyNumber 	= (EditText) findViewById(R.id.policy_number);
			note 			= (EditText) findViewById(R.id.notes);

			// << EPO
			//        // This view doesn't use the Mobile or Home phone number fields, so hide them
			//        TableLayout mobilePhone = (TableLayout) findViewById(R.id.mobile_phone_section);
			//        mobilePhone.setVisibility(View.GONE);
			//
			//        TableLayout homePhone = (TableLayout) findViewById(R.id.home_phone_section);
			//        homePhone.setVisibility(View.GONE);
			// >>


			// Policy Info
			year 	= (EditText) findViewById(R.id.vehicle_year);
			make 	= (EditText) findViewById(R.id.vehicle_make);
			model	= (EditText) findViewById(R.id.vehicle_model);
			color 	= (EditText) findViewById(R.id.vehicle_color);
			registrationNumber = (EditText) findViewById(R.id.vehicle_license_plate);

			if (submitted)
			{
				FieldUtils.disableEditText(firstName);
				FieldUtils.disableEditText(lastName);
				FieldUtils.disableEditTextPhone(homePhone);
				FieldUtils.disableEditTextPhone(mobilePhone);
				FieldUtils.disableEditText(emailAddress);
				FieldUtils.disableEditText(insuranceCompany);
				FieldUtils.disableEditText(policyNumber);
				FieldUtils.disableEditText(year);
				FieldUtils.disableEditText(make);
				FieldUtils.disableEditText(model);
				FieldUtils.disableEditText(color);
				FieldUtils.disableEditText(registrationNumber);
				FieldUtils.disableEditText(note);
			}

			// Log.v(TAG, "<<< setInputFields()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Adds the menu items for the OtherDriver view when the user pushes the device's menu button
	 * 
	 * @param menu
	 *            The options menu in which you place the items.
	 * @return true so that the menu will be displayed
	 * @see android.app.Activity#onCreateOptionsMenu(Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		try {
			// Log.v(TAG, ">>> onCreateOptionsMenu(Menu menu)");

			if (!isNew && !submitted) 
			{
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.other_driver_menu, menu);
			}
			return true;

			// Log.v(TAG, "<<< onCreateOptionsMenu(Menu menu)");
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		try {
			switch (item.getItemId()) 
			{
			case R.id.menu_item_delete_other_driver:
			{
				currentAction = DELETE;

				displayConfirmAlert(OtherDriverActivity.this, 
						getString(R.string.delete),
						getString(R.string.excluir_contato), 
						getString(R.string.btn_ok), 
						getString(R.string.btn_cancelar),
						OtherDriverActivity.this);
			}
			return true;
			default:
				return false;
			}
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
			if (saveForm(true))
			{
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
	
	@Override
	protected void onResume() {
	  super.onResume();
	}

	@Override
	protected void onPause() {
	  super.onPause();
	}
}
