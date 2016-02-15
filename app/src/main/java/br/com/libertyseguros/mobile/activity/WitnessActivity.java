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
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.textwatcher.PhoneNumberTextWatcher;
import br.com.libertyseguros.mobile.common.util.FieldUtils;
import br.com.libertyseguros.mobile.common.util.PhoneNumberUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.WitnessHelper;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.Witness;

/**
 * Displays the Witness view. Looks for extra EXTRA_NAME_NEW specifying that this is a new activity_witness or EXTRA_NAME_ID
 * specifying the id of an existing activity_witness and either creates a new activity_witness or retrieves the existing one accordingly.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class WitnessActivity extends LibertyMobileApp implements DialogInterface.OnClickListener
{
	private static final String DELETE = "DELETE";

	// private static final String TAG = WitnessActivity.class.getName();

	private String currentAction;

	private EditText emailAddress;

	private EditText firstName;

	private EditText lastName;

	private EditText note;

	private EditText homePhone;

	private EditText mobilePhone;

	private boolean submitted = false;

	private Witness witness = null;

	//  << EPO
	private boolean isNew = false;

	private boolean wantSave;

	// >>

	/**
	 * Responds to the user's selection in an alert dialog, in this case confirming that the user wants to cancel their
	 * changes or delete a activity_witness.
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
				// If the user clicked delete, delete the activity_witness
				if (DELETE.equals(currentAction))
				{
					WitnessHelper.delete(getApplicationContext(), witness.getId());
					finish();
				}
				else
				{
					// << EPO
					//            // Finish the activity
					//            finish();
					wantSave = true;
					salvaEfecha(null);
				}
			}
			else if (which == -2 &&  !DELETE.equals(currentAction))
			{
				// Finish the activity
				finish();
			}
			// >>

			// Log.v(TAG, "<<< onClick(DialogInterface dialog, int which)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Initializes the view, setting the layout to the activity_witness layout. Also builds the navigation bar and adds listeners
	 * to the buttons in the view. The navigation bar and delete button will differ depending on whether this is a new
	 * activity_witness (specified by {@link EXTRA_NAME_NEW}) or an existing one (specified by {@link EXTRA_NAME_ID})
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

			// Create the view with the activity_witness layout
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_witness);

			// determine if this is a submitted claim
			if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()))
			{
				submitted = true;
			}

			// determine if we are working on a new or existing activity_witness
			Bundle extras = getIntent().getExtras();
			isNew = extras.getBoolean(EXTRA_NAME_NEW);

			// The title will differ based on whether or not the activity_witness is new
			if (!isNew) {
				Long id = extras.getLong(EXTRA_NAME_ID);
				witness = WitnessHelper.get(getApplicationContext(), id);
			}

			// Set up the navigation bar with a title.
			setUpNavigationBarTitleOnly(getString(R.string.witness_title));

			//        Button doneButton = (Button) findViewById(R.id.done_button);
			//        doneButton.setOnClickListener(WitnessActivity.this);

			// Prepare the form
			setUpInputFields();

			// Format Phone Numbers
			mobilePhone.addTextChangedListener(new PhoneNumberTextWatcher());
			homePhone.addTextChangedListener(new PhoneNumberTextWatcher());

			// Prefill the form with any previously entered data
			setUpWitness();

			// Log.v(TAG, "<<< onCreate(Bundle savedInstanceState)");
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
	 * @see android.app.Activity#onKeyDown(int, KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try {
			// Log.v(TAG, ">>> onKeyDown(int keyCode, KeyEvent event)");

			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
			{
				// Ask the user if they want to discard their changes
				if (Constants.EVENT_STATUS_DRAFT.equals(getCurrentEvent().getEventStatus()) && isFormChanged())
				{

					// << EPO caso botão voltar: Salva e volta

					//                displayConfirmAlert(WitnessActivity.this, getString(R.string.go_back_without_saving),
					//                    getString(R.string.go_back_without_saving_confirmation), getString(R.string.btn_ok),
					//                    getString(R.string.no), WitnessActivity.this);
					//            	return false;

					if(wantSave) {
						salvaEfecha(null);
					}
					else{
						// Ask the user if they want to discard their changes
						displayConfirmAlert(WitnessActivity.this,
								getString(R.string.aviso),
								getString(R.string.deseja_salvar_alteracoes),
								getString(R.string.btn_ok),
								getString(R.string.btn_cancelar),
								WitnessActivity.this);

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
				//            ValidationUtils.addValidStringLabel(missingInfo, needComma);
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
			displayInfoAlert(	WitnessActivity.this,
					getString(R.string.por_favor_insira),
					missingInfo.toString(), 
					getString(R.string.btn_ok),
					WitnessActivity.this);

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

			Contact oldContact = witness.getContact();
			Contact newContact = new Contact();
			newContact.setFirstName(firstName.getText().toString());
			newContact.setLastName(lastName.getText().toString());
			newContact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString()));
			newContact.setMobilePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString()));
			newContact.setEmailAddress(emailAddress.getText().toString());
			newContact.setNotes(note.getText().toString());

			// Log.v(TAG, "<<< isFormChanged()");

			return !oldContact.isEqualTo(newContact);
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
					|| ValidationUtils.isStringEmpty(contact.getHomePhone()) || contact.getHomePhone().length() < 10)
			{
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
				// << EPO
				//            if (ValidationUtils.isStringEmpty(contact.getHomePhone()) || contact.getHomePhone().length() < 10)
				//            {
				//                missingPhone = true;
				//            }
				String homePhoneText = PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString());
				String mobilePhoneText = PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString());
				if ((ValidationUtils.isStringEmpty(homePhoneText) || homePhoneText.length() < 11) 
						&& (ValidationUtils.isStringEmpty(mobilePhoneText) || mobilePhoneText.length() < 11) )
				{
					missingPhone = true;
				}
				// >>

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

			// Create the model objects from the entered data
			Contact contact = witness.getContact();
			contact.setFirstName(firstName.getText().toString());
			contact.setLastName(lastName.getText().toString());
			contact.setMobilePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString()));
			contact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString()));
			contact.setEmailAddress(emailAddress.getText().toString());
			contact.setNotes(note.getText().toString());

			if (witness.isEmpty())
			{
				if (errorOnEmpty)
				{
					returnVal = false;

					alertMinRequirements(true, true, true);
				}
			}
			else if (minRequirementsMet(errorOnEmpty, contact))
			{
				// Insert or update the data to the database
				if (witness.getId() == null)
				{
					long id = WitnessHelper.insert(getApplicationContext(), witness, getCurrentEvent().getId());
					witness.setId(id);
				}
				else
				{
					WitnessHelper.update(getApplicationContext(), witness);
				}
			}
			else
			{
				returnVal = false;
			}

			if (!returnVal)
			{
				witness.getContact().reset();
			}
			// Log.v(TAG, "<<< saveForm(boolean errorOnEmpty)");

			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
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
			firstName 	= (EditText) findViewById(R.id.first_name);
			lastName 	= (EditText) findViewById(R.id.last_name);
			mobilePhone = (EditText) findViewById(R.id.mobile_phone);
			homePhone 	= (EditText) findViewById(R.id.home_phone);
			emailAddress= (EditText) findViewById(R.id.email);
			note 		= (EditText) findViewById(R.id.notes);

			if (submitted)
			{
				FieldUtils.disableEditText(firstName);
				FieldUtils.disableEditText(lastName);
				FieldUtils.disableEditTextPhone(homePhone);
				FieldUtils.disableEditTextPhone(mobilePhone);
				FieldUtils.disableEditText(emailAddress);
				FieldUtils.disableEditText(note);
			}

			// Log.v(TAG, "<<< setInputFields()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Gets any previously saved activity_witness info from the database and prefills the page with the info
	 */
	private void setUpWitness()
	{
		try {
			// Log.v(TAG, ">>> setUpWitness()");

			if (witness == null)
			{
				// No user exists, so create one
				witness = new Witness();
				witness.setContact(new Contact());
			}
			else
			{
				// use the existing activity_witness info to prefill the screen
				Contact contact = witness.getContact();
				if (contact == null)
				{
					// the activity_witness doesn't have a contact, so create one
					witness.setContact(new Contact());
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
			}

			if (submitted)
			{
				FieldUtils.disableEditText(firstName);
				FieldUtils.disableEditText(lastName);
				FieldUtils.disableEditTextPhone(homePhone);
				FieldUtils.disableEditTextPhone(mobilePhone);
				FieldUtils.disableEditTextEmail(emailAddress);
				FieldUtils.disableEditText(note);
			}

			// Log.v(TAG, "<<< setUpWitness()");
		} catch (Exception e) {
			Util.showException(this, e);
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

	/**
	 * Adds the menu items for the activity_witness view when the user pushes the device's menu button
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
				inflater.inflate(R.menu.witness_menu, menu);
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
			case R.id.menu_item_delete_witness:
			{
				currentAction = DELETE;

				displayConfirmAlert(WitnessActivity.this, 
						getString(R.string.delete),
						getString(R.string.excluir_contato), 
						getString(R.string.btn_ok), 
						getString(R.string.btn_cancelar),
						WitnessActivity.this);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}
}
