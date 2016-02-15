/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.textwatcher.PhoneNumberTextWatcher;
import br.com.libertyseguros.mobile.common.util.FieldUtils;
import br.com.libertyseguros.mobile.common.util.PhoneNumberUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.UserHelper;
import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.User;

/**
 * Provides the functionality for the My Info view
 * 
 * @author N0053575 (Heidi Sturm)
 * 
 */
public class MyInfoActivity extends LibertyMobileApp implements DialogInterface.OnClickListener
{
	// private static final String TAG = MyInfoActivity.class.getName();

	private EditText firstName;

	private EditText lastName;

	private EditText homePhone;

	private EditText mobilePhone;

	private boolean inClaim = false;

	private User user;

	private boolean resumingState;

	// << EPO
	private EditText emailAddress;	// Email
	private EditText streetAddress;	// Endereco
	private EditText zipCode; 		// CEP
	private EditText city;			// Cidade
	private boolean  wantSave;
	// >>

	/**
	 * Checks the form to see if the minimum requirements have been met
	 * 
	 * @param minRequirementsMet
	 * @return minRequirementsMet
	 */
	private boolean checkMinReqs()
	{
		try {
			boolean minRequirementsMet = true;

			StringBuffer missingInfo = new StringBuffer();

			boolean needComma = false;

			if (ValidationUtils.isStringEmpty(firstName.getText().toString()))
			{
				missingInfo.append(getString(R.string.first_name_label));
				needComma = true;
				minRequirementsMet = false;
			}
			if (ValidationUtils.isStringEmpty(lastName.getText().toString()))
			{
				ValidationUtils.addComma(missingInfo, needComma);

				missingInfo.append(getString(R.string.last_name_label));
				needComma = true;
				minRequirementsMet = false;
			}
			String homePhoneText = PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString());
			String mobilePhoneText = PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString());
			if ((ValidationUtils.isStringEmpty(homePhoneText) || homePhoneText.length() < 11) 
					&& (ValidationUtils.isStringEmpty(mobilePhoneText) || mobilePhoneText.length() < 11) )
			{
				ValidationUtils.addAnd(missingInfo, needComma);

				missingInfo.append(getString(R.string.phone_label));

				ValidationUtils.addValidStringLabel(missingInfo, needComma);
				needComma = true;
				minRequirementsMet = false;
			}

			if (!minRequirementsMet)
			{
				// Tell the user they haven't met the minimum requirements
				displayInfoAlert(	MyInfoActivity.this,
						getString(R.string.por_favor_insira),
						missingInfo.toString(), 
						getString(R.string.btn_ok),
						MyInfoActivity.this);
			}
			return minRequirementsMet;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Checks to see if the user has made any changes to the form since it was last saved
	 * 
	 * @return true if the form has changed, false if it has not
	 */
	private boolean isFormChanged(){
		
		try {
			
			boolean returnVal = false;

			// Create the model objects from the entered data
			Contact newContact = new Contact();
			newContact.setFirstName(firstName.getText().toString());
			newContact.setLastName(lastName.getText().toString());
			newContact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString()));
			newContact.setMobilePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString()));

			// << EPO
			newContact.setEmailAddress(emailAddress.getText().toString());

			Address newAddress = new Address();
			newAddress.setStreetAddress(streetAddress.getText().toString());
			newAddress.setCity(city.getText().toString());
			newAddress.setZipCode(zipCode.getText().toString());
			newContact.setAddress(newAddress);
			// >>


			Contact contact = user.getContact();

			if (!contact.isEqualTo(newContact))
			{
				returnVal = true;
			}
			// << EPO
			else if (!contact.getAddress().isEqualTo(newAddress))
			{
				returnVal = true;
			}
			// >>

			return returnVal;
			
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Checks to see if we're in the context of a draft claim
	 * 
	 * @return true if the context is a draft claim, false if not
	 */
	private boolean isInDraftClaim()
	{
		try {
			boolean inDraftClaim =
					inClaim && (getCurrentEvent() != null)
					&& (Constants.EVENT_STATUS_DRAFT.equals(getCurrentEvent().getEventStatus()));
			return inDraftClaim;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Checks to see if we're in the context of a submitted claim
	 * 
	 * @return true if the context is a submitted claim, false if not
	 */
	private boolean isInSubmittedClaim()
	{
		try {
			boolean inSubmittedClaim =
					inClaim && (getCurrentEvent() != null)
					&& (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()));
			return inSubmittedClaim;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Responds to the user's selection in an alert dialog, in this case confirming that the user wants to save their
	 * changes
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
			// Log.v(TAG, ">>> onClick()");

			// The user selected yes 
			if (which == -1) {
				wantSave = true;
				salvaEfecha(null);
			} else if (which == -2) {
				// Finish the activity
				finish();
			}

			// Log.v(TAG, "<<< onClick()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Initializes the view, setting the layout to the my info layout. Also builds the navigation bar, adds listeners to
	 * the buttons in the view and looks for any existing user info in the database. If there is existing info in the
	 * database, the form is prefilled with that data.
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

			setContentView(R.layout.activity_my_info);

			if (savedInstanceState != null)
			{
				resumingState = savedInstanceState.getBoolean(Constants.LM_RESTORINGSTATE);
			}
			else
			{
				resumingState = false;
			}

			setUpNavigationBarTitleOnly(getString(R.string.my_info_title));

			// Get the input fields
			setUpInputFields();

			// Add listeners to all input objects
			setUpListeners();

			setUpUser();

			if (!resumingState)
			{
				// refresh the page on returning to make sure we have the most up to date lists
				// We don't want to overide information the user entered if we are just being recreated from an orientation
				// change
				prefillPage();
			}
			// Log.v(TAG, "<<< onCreate(Bundle savedInstanceState)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	//    /**
	//     * Adds the menu items for the my info view when the user pushes the device's menu button
	//     * 
	//     * @param menu
	//     *            The options menu in which you place the items.
	//     * @return true so that the menu is displayed
	//     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	//     */
	//    @Override
	//    public boolean onCreateOptionsMenu(Menu menu)
	//    {
	//        // Log.v(TAG, ">>> onCreateOptionsMenu(Menu menu)");
	//
	//        if (!inClaim)
	//        {
	//            MenuInflater inflater = getMenuInflater();
	//            inflater.inflate(R.menu.main_menu, menu);
	//
	//            // Hide the item for this view
	//            MenuItem overview = menu.findItem(R.id.menu_item_my_info);
	//            overview.setVisible(false);
	//        }
	//        // Log.v(TAG, "<<< onCreateOptionsMenu(Menu menu)");
	//
	//        return true;
	//    }

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
				// << EPO caso bot�o voltar: Salva e volta

				if (!isInSubmittedClaim() && isFormChanged())
				{
					if(wantSave) {
						salvaEfecha(null);
					}
					else{
						// Ask the user if they want to discard their changes
						displayConfirmAlert(MyInfoActivity.this,
								getString(R.string.aviso),
								getString(R.string.deseja_salvar_alteracoes),
								getString(R.string.btn_ok),
								getString(R.string.btn_cancelar),
								MyInfoActivity.this);

						return false;
					}
				}

				// >>
			}

			// Log.v(TAG, "<<< onKeyDown(int keyCode, KeyEvent event)");

			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
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

			super.onResume();

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param outState
	 * @see android.app.Activity#onSaveInstanceState(Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		try {

			outState.putBoolean(Constants.LM_RESTORINGSTATE, true);

			super.onSaveInstanceState(outState);

		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * Gets any previously saved user info from the database and prefills the page with the info
	 */
	private void prefillPage()
	{
		try {

			Contact contact;

			contact = user.getContact();

			if (contact == null)
			{
				// the user doesn't have a contact, so create one.
				user.setContact(new Contact());
			}

			// use the existing contact info to prefill the screen
			firstName.setText(contact.getFirstName());
			lastName.setText(contact.getLastName());
			mobilePhone.setText(PhoneNumberUtils.formatPhoneNumberForDisplay(contact.getMobilePhone()));
			homePhone.setText(PhoneNumberUtils.formatPhoneNumberForDisplay(contact.getHomePhone()));

			// << EPO
			emailAddress.setText(contact.getEmailAddress());

			Address address = contact.getAddress();

			streetAddress.setText(address.getStreetAddress());
			city.setText(address.getCity());
			zipCode.setText(address.getZipCode());
			// >>

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Saves the current form data to the database
	 */
	private boolean saveForm()
	{
		try {

			boolean returnVal = true;

			Contact contact = user.getContact();

			contact.setFirstName(firstName.getText().toString());
			contact.setLastName(lastName.getText().toString());
			contact.setMobilePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(mobilePhone.getText().toString()));
			contact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(homePhone.getText().toString()));

			// << EPO
			contact.setEmailAddress(emailAddress.getText().toString());

			Address address = contact.getAddress();
			address.setStreetAddress(streetAddress.getText().toString());
			address.setCity(city.getText().toString());
			address.setZipCode(zipCode.getText().toString());
			//        contact.setAddress(address);
			// >>

			// check min requirements if in claim
			if (!inClaim || checkMinReqs())
			{
				// Insert or update the data to the database
				if (user.getId() == null)
				{
					long id = UserHelper.insert(getApplicationContext(), user);
					user.setId(id);
				}
				else
				{
					UserHelper.update(getApplicationContext(), user);
				}
			}
			else
			{
				user.getContact().reset();
				returnVal = false;
			}

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

			// Contact Info
			firstName = (EditText) findViewById(R.id.first_name);
			lastName = (EditText) findViewById(R.id.last_name);
			mobilePhone = (EditText) findViewById(R.id.mobile_phone);
			homePhone = (EditText) findViewById(R.id.home_phone);

			// << EPO
			emailAddress 	= (EditText) findViewById(R.id.email);
			streetAddress 	= (EditText) findViewById(R.id.street_address);
			city 			= (EditText) findViewById(R.id.city);
			zipCode 		= (EditText) findViewById(R.id.zip_code);
			// >>

			// fix a tabbing issue
			firstName.setNextFocusDownId(R.id.last_name);
			lastName.setNextFocusDownId(R.id.home_phone);
			homePhone.setNextFocusDownId(R.id.email);

			// << EPO
			emailAddress.setNextFocusDownId(R.id.street_address);
			streetAddress.setNextFocusDownId(R.id.city);
			city.setNextFocusDownId(R.id.zip_code);
			zipCode.setNextFocusDownId(R.id.first_name);
			// >>

			// If we're on a claim, we need to hide the section for the other claim type
			Bundle extras = getIntent().getExtras();
			if (extras != null)
			{
				String lob = extras.getString(EXTRA_NAME_LOB);

				if (Constants.LOB_AUTO.equals(lob))
				{
					this.inClaim = true;
				}

				if (isInSubmittedClaim())
				{
					// if the event is submitted, disable the fields
					FieldUtils.disableEditText(firstName);
					FieldUtils.disableEditText(lastName);
					FieldUtils.disableEditTextPhone(homePhone);
					FieldUtils.disableEditTextPhone(mobilePhone);
					FieldUtils.disableEditText(emailAddress);
					FieldUtils.disableEditText(streetAddress);
					FieldUtils.disableEditTextPhone(city);
					FieldUtils.disableEditTextPhone(zipCode);
				}
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets up all form fields with the appropriate listeners
	 */
	private void setUpListeners()
	{
		try {

			if (!inClaim || isInDraftClaim())
			{
				// Format Phone Numbers
				mobilePhone.addTextChangedListener(new PhoneNumberTextWatcher());
				homePhone.addTextChangedListener(new PhoneNumberTextWatcher());
			}

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets up the user
	 */
	private void setUpUser()
	{
		try {
			if (isInSubmittedClaim())
			{
				// If we are viewing a submitted claim, get the user that was saved at time of submission.
				user = getCurrentEvent().getSubmittedUser();
			}
			else
			{
				// If claim is draft, grab the current user information.
				user = UserHelper.getCurrent(getApplicationContext());
				if (user == null)
				{
					// No user exists, so create one
					user = new User();
				}
			}
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
