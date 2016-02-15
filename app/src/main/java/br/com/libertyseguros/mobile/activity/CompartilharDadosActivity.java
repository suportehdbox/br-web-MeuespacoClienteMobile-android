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
import android.view.View.OnClickListener;
import android.widget.EditText;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.textwatcher.PhoneNumberTextWatcher;
import br.com.libertyseguros.mobile.common.util.FieldUtils;
import br.com.libertyseguros.mobile.common.util.PhoneNumberUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.database.UserHelper;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.Policy;
import br.com.libertyseguros.mobile.model.User;

/**
 * Provides the functionality for the share data view
 * 
 * @author Evandro
 * 
 */
public class CompartilharDadosActivity extends LibertyMobileApp implements OnClickListener, DialogInterface.OnClickListener
{

	private EditText firstName;

	private EditText lastName;

	private EditText apolicy;

	private EditText phone;

	private boolean inClaim = false;

	private User user;

	private boolean resumingState;

	private boolean  wantSave;

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

			User newUser = new User();

			Contact newContact = new Contact();
			newContact.setFirstName(firstName.getText().toString());
			newContact.setLastName(lastName.getText().toString());
			newContact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(phone.getText().toString()));

			newUser.setContact(newContact);

			Policy newAutoPolicy = new Policy();
			newAutoPolicy.setPolicyNumber(apolicy.getText().toString());

			newUser.setAutoPolicy(newAutoPolicy);

			// Compara os dois user
			if (!user.getContact().getFirstName().equals(newUser.getContact().getFirstName())) {
				returnVal = true;
			}
			else if (!user.getContact().getLastName().equals(newUser.getContact().getLastName())) {
				returnVal = true;
			}
			else if (!user.getContact().getHomePhone().equals(newUser.getContact().getHomePhone())) {
				returnVal = true;
			}
			else if (!user.getAutoPolicy().getPolicyNumber().equals(newUser.getAutoPolicy().getPolicyNumber())) {
				returnVal = true;
			}

			// Log.v(TAG, "<<< isFormChanged()");

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
			boolean inDraftClaim = inClaim && (getCurrentEvent() != null)
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
				salvaEfecha();
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
	 * Responds to the click of a button in the view and performs the action associated with the button that was
	 * clicked. In this case, toggles between the Contact Info and Policy Info subtabs when the user clicks the contact
	 * or policy toggle button.
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

			// If the user clicked to share their info
			if (v.equals(findViewById(R.id.send_info_button)))
			{
				Contact userContactInfo = new Contact();
				userContactInfo.setFirstName(firstName.getText().toString());
				userContactInfo.setLastName(lastName.getText().toString());
				userContactInfo.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(phone.getText().toString()));

				startUserInfoEmail(userContactInfo, apolicy.getText().toString());
			}
			//  Log.v(TAG, "<<< onClick(View v)");
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
			// Log.v(TAG, ">>> onCreate(Bundle savedInstanceState)");

			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_compartilhar_dados); 

			if (savedInstanceState != null)
			{
				resumingState = savedInstanceState.getBoolean(Constants.LM_RESTORINGSTATE);
			}
			else
			{
				resumingState = false;
			}

			setUpNavigationBarTitleOnly(getString(R.string.compartilhar_dados));

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
						salvaEfecha();
					}
					else{
						// Ask the user if they want to discard their changes
						displayConfirmAlert(CompartilharDadosActivity.this,
								getString(R.string.aviso),
								getString(R.string.deseja_salvar_alteracoes),
								getString(R.string.btn_ok),
								getString(R.string.btn_cancelar),
								CompartilharDadosActivity.this);

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
			// Log.v(TAG, ">>> onResume()");

			super.onResume();

			// Log.v(TAG, "<<< onResume()");
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
			// Log.v(TAG, ">>> onSaveInstanceState()");

			outState.putBoolean(Constants.LM_RESTORINGSTATE, true);

			super.onSaveInstanceState(outState);

			// Log.v(TAG, "<<< onSaveInstanceState()");
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
			// Log.v(TAG, ">>> prefillPage()");

			Contact contact = user.getContact();

			if (contact == null)
			{
				// the user doesn't have a contact, so create one.
				user.setContact(new Contact());
				user.getContact().reset();
			}

			// use the existing contact info to prefill the screen
			firstName.setText(contact.getFirstName());
			lastName.setText(contact.getLastName());
			phone.setText(PhoneNumberUtils.formatPhoneNumberForDisplay(contact.getHomePhone()));

			Policy policy = user.getAutoPolicy();
			if (policy == null)
			{
				// the user doesn't have a contact, so create one.
				user.setAutoPolicy(new Policy());
			}

			apolicy.setText(policy.getPolicyNumber());

			// Log.v(TAG, "<<< prefillPage()");
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
			// Log.v(TAG, ">>> saveForm()");

			boolean returnVal = true;

			Contact contact = user.getContact();

			contact.setFirstName(firstName.getText().toString());
			contact.setLastName(lastName.getText().toString());
			contact.setHomePhone(PhoneNumberUtils.formatPhoneNumberForDatabase(phone.getText().toString()));

			Policy policy = user.getAutoPolicy();
			policy.setPolicyNumber(apolicy.getText().toString());

			// check min requirements if in claim
			if (!inClaim)
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

				// Save the data to the database	
				getCurrentEvent().setSubmittedUser(user);
				EventHelper.update(getApplicationContext(), getCurrentEvent());

			}
			else
			{
				user.getContact().reset();
				returnVal = false;
			}
			// TODO EPO verificar como dar get neste regitro salvo, não deve sobreescrever o current user

			// Log.v(TAG, "<<< saveForm()");

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
			firstName = (EditText) findViewById(R.id.edit_first_name);
			lastName = (EditText) findViewById(R.id.edit_last_name);
			apolicy = (EditText) findViewById(R.id.edit_policy_number);
			phone = (EditText) findViewById(R.id.edit_phone);

			// fix a tabbing issue
			firstName.setNextFocusDownId(R.id.edit_last_name);
			lastName.setNextFocusDownId(R.id.edit_policy_number);
			apolicy.setNextFocusDownId(R.id.edit_phone);
			phone.setNextFocusDownId(R.id.edit_first_name);

			this.inClaim = true;

			if (isInSubmittedClaim())
			{
				// if the event is submitted, disable the fields
				FieldUtils.disableEditText(firstName);
				FieldUtils.disableEditText(lastName);
				FieldUtils.disableEditText(apolicy);
				FieldUtils.disableEditTextPhone(phone);
			}

			// Log.v(TAG, "<<< setInputFields()");
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
			// Log.v(TAG, ">>> setUpListeners()");

			if (!inClaim || isInDraftClaim())
			{
				// Format Phone Numbers
				phone.addTextChangedListener(new PhoneNumberTextWatcher());

				//            // Format Policy Numbers
				//            autoPolicy.addTextChangedListener(new PolicyNumberTextWatcher());
			}

			// Share info
			findViewById(R.id.send_info_button).setOnClickListener(CompartilharDadosActivity.this);

			// Log.v(TAG, "<<< setUpListeners()");
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
			if (isInSubmittedClaim()) {
				// If we are viewing a submitted claim, get the user that was saved at time of submission.
				user = getCurrentEvent().getSubmittedUser();
			} else {
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

	private void salvaEfecha()
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