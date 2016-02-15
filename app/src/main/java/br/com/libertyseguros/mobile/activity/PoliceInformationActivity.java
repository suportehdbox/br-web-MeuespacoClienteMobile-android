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
import br.com.libertyseguros.mobile.common.util.FieldUtils;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.BoletimOcorrenciaHelper;
import br.com.libertyseguros.mobile.model.BoletimDeOcorrencia;

/**
 * Displays the Police Info view. Looks for extra EXTRA_NAME_NEW specifying that this is a new info or EXTRA_NAME_ID
 * specifying the id of an existing info and either creates a new info or retrieves the existing one accordingly.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class PoliceInformationActivity extends LibertyMobileApp implements /*OnClickListener, */ DialogInterface.OnClickListener
{
	private static final String DELETE = "DELETE";

	private String currentAction;

	private EditText entidade;

	private EditText localidade;

	private EditText note;

	private BoletimDeOcorrencia boletimDeOcorrencia = null;

	private boolean submitted = false;

	// << EPO
	private boolean isNew = false;

	private boolean  wantSave;
	// >>

	/**
	 * Responds to the user's selection in an alert dialog, in this case confirming that the user wants to cancel their
	 * changes or delete a BoletimDeOcorrencia.
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
				// If the user clicked delete, delete the info
				if (DELETE.equals(currentAction))
				{
					BoletimOcorrenciaHelper.delete(getApplicationContext(), boletimDeOcorrencia.getId());
					finish();
				}
				// << EPO
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
			// >>
			// Log.v(TAG, "<<< onClick(DialogInterface dialog, int which)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	// << EPO
	//    /**
	//     * Responds to the click of a button in the view and performs the action associated with the button that was
	//     * clicked. If the user clicked the done button, the form is saved and the Activity is finished. If the user clicked
	//     * the delete button, the user is asked if they want to delete the current police info.
	//     * 
	//     * @param v
	//     *            the View that was clicked, in this case a button
	//     * @see android.view.View.OnClickListener#onClick(android.view.View)
	//     */
	//    @Override
	//    public void onClick(View v)
	//    {
	//        // Log.v(TAG, ">>> onClick(View view)");
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
	//    	else if (v.equals(findViewById(R.id.delete_police_info_button)))
	//        {
	//            currentAction = DELETE;
	//
	//            displayConfirmAlert(PoliceInformationActivity.this, getString(R.string.delete),
	//                getString(R.string.confirm_delete_police_info), getString(R.string.btn_ok), getString(R.string.no),
	//                PoliceInformationActivity.this);
	//        }
	//
	//        // Log.v(TAG, "<<< onClick(View view)");
	//    }
	// >>

	/**
	 * Initializes the view, setting the layout to the police info layout. Also builds the navigation bar and adds
	 * listeners to the buttons in the view. The navigation bar and delete button will differ depending on whether this
	 * is a new police info (specified by {@link EXTRA_NAME_NEW}) or an existing one (specified by {@link EXTRA_NAME_ID}
	 * )
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

			// Create the view with the policeInfo layout
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_police_information);

			setUpNavigationBarTitleOnly(getString(R.string.police_info_heading));

			// determine if this is a submitted claim
			if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()))
			{
				submitted = true;
			}

			// determine if we are working on a new or existing policeInfo
			Bundle extras = getIntent().getExtras();
			isNew = extras.getBoolean(EXTRA_NAME_NEW);

			// << EPO
			// The title and delete button will differ based on whether or not the policeInfo is new
			//        Button deleteButton = (Button) findViewById(R.id.delete_police_info_button);

			//        if (isNew) {
			//            deleteButton.setVisibility(View.GONE);
			//        } else {
			//            Long id = extras.getLong(EXTRA_NAME_ID);
			//            boletimDeOcorrencia = BoletimOcorrenciaHelper.get(getApplicationContext(), id);
			//            deleteButton.setOnClickListener(PoliceInformationActivity.this);
			//            if (submitted){
			//                deleteButton.setVisibility(View.GONE);
			//            }
			//        }
			if (!isNew) {
				Long id = extras.getLong(EXTRA_NAME_ID);
				boletimDeOcorrencia = BoletimOcorrenciaHelper.get(getApplicationContext(), id);
			}
			// >>

			// Set up the navigation bar with a title.

			// << EPO
			//        Button doneButton = (Button) findViewById(R.id.done_button);
			//        doneButton.setOnClickListener(PoliceInformationActivity.this);
			// >>

			// Prepare the form
			setUpInputFields();

			// Prefill the form with any previously entered data
			setUpPoliceInfo();

			// Log.v(TAG, "<<< onCreate(Bundle savedInstanceState)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Catches the press of the device back button by the user and asks the user to save the form before leaving the
	 * Activity
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
					//                displayConfirmAlert(PoliceInformationActivity.this, getString(R.string.go_back_without_saving),
					//                    getString(R.string.go_back_without_saving_confirmation), getString(R.string.btn_ok),
					//                    getString(R.string.no), PoliceInformationActivity.this);
					//
					//                return false;

					if(wantSave) {
						salvaEfecha(null);
					}
					else{
						// Ask the user if they want to discard their changes
						displayConfirmAlert(PoliceInformationActivity.this,
								getString(R.string.aviso),
								getString(R.string.deseja_salvar_alteracoes),
								getString(R.string.btn_ok),
								getString(R.string.btn_cancelar),
								PoliceInformationActivity.this);

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
	 * @param entidade
	 *            whether or not the entidade field was missing data
	 * @param localidade
	 *            whether or not the localidade field was missing data
	 */
	private void alertMinRequirements(boolean entidade, boolean localidade)
	{
		try {
			// Log.v(TAG, ">>> alertMinRequirements(boolean departmentMissing, boolean phoneMissing)");

			StringBuffer missingInfo = new StringBuffer();

			if (entidade)
			{
				missingInfo.append(getString(R.string.entidade));
			}
			if (localidade)
			{
				ValidationUtils.addAnd(missingInfo, entidade);

				missingInfo.append(getString(R.string.localidade));

				ValidationUtils.addValidStringLabelWithGender(missingInfo, entidade, false);
			} else if (entidade)
			{
				ValidationUtils.addValidStringLabelWithGender(missingInfo, false, false);
			}

			// Tell the user they haven't met the minimum requirements
			displayInfoAlert(	PoliceInformationActivity.this,
					getString(R.string.por_favor_insira),
					missingInfo.toString(), 
					getString(R.string.btn_ok),
					PoliceInformationActivity.this);

			// Log.v(TAG, "<<< alertMinRequirements(boolean departmentMissing, boolean phoneMissing)");
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

			BoletimDeOcorrencia newBoletimDeOcorrencia = new BoletimDeOcorrencia();
			newBoletimDeOcorrencia.setEntidade(entidade.getText().toString());
			newBoletimDeOcorrencia.setLocalidade(localidade.getText().toString());
			newBoletimDeOcorrencia.setNotas(note.getText().toString());

			if (!newBoletimDeOcorrencia.isEqualTo(boletimDeOcorrencia))
			{
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
			boletimDeOcorrencia.setEntidade(entidade.getText().toString());
			boletimDeOcorrencia.setLocalidade(localidade.getText().toString());
			boletimDeOcorrencia.setNotas(note.getText().toString());

			// Check for required fields
			if (boletimDeOcorrencia.isEmpty())
			{
				if (errorOnEmpty)
				{
					returnVal = false;
					alertMinRequirements(true, true);
				}
			}
			else if (boletimDeOcorrencia.getEntidade().length() < 1 )
			{
				returnVal = false;
				alertMinRequirements(true, false);
			}
			else if (boletimDeOcorrencia.getLocalidade().length() < 1)
			{
				returnVal = false;
				alertMinRequirements(false, true);
			}
			else
			{
				// Insert or update the data to the database
				if (boletimDeOcorrencia.getId() == null)
				{
					long id =
							BoletimOcorrenciaHelper.insert(getApplicationContext(), boletimDeOcorrencia, getCurrentEvent().getId());
					boletimDeOcorrencia.setId(id);
				}
				else
				{
					BoletimOcorrenciaHelper.update(getApplicationContext(), boletimDeOcorrencia);
				}
			}

			if (!returnVal)
			{
				boletimDeOcorrencia.reset();
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

			entidade 	= (EditText) findViewById(R.id.entidade);
			localidade 	= (EditText) findViewById(R.id.localidade);
			note 		= (EditText) findViewById(R.id.notes);

			if (submitted)
			{
				FieldUtils.disableEditText(entidade);
				FieldUtils.disableEditTextPhone(localidade);
				FieldUtils.disableEditText(note);
			}

			// Log.v(TAG, "<<< setInputFields()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Gets any previously saved policeInfo info from the database and prefills the page with the info
	 */
	private void setUpPoliceInfo()
	{
		try {
			// Log.v(TAG, ">>> setUpPoliceInfo()");

			if (boletimDeOcorrencia == null)
			{
				// No user exists, so create one
				boletimDeOcorrencia = new BoletimDeOcorrencia();
			}
			else
			{
				// use the existing policeInfo info to prefill the screen
				entidade.setText(boletimDeOcorrencia.getEntidade());
				localidade.setText(boletimDeOcorrencia.getLocalidade());
				note.setText(boletimDeOcorrencia.getNotas());
			}

			if (submitted)
			{
				FieldUtils.disableEditTextPhone(localidade);
				FieldUtils.disableEditText(entidade);
				FieldUtils.disableEditText(note);
			}

			// Log.v(TAG, "<<< setUpPoliceInfo()");
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
	 * Adds the menu items for the BoletimDeOcorrencia view when the user pushes the device's menu button
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
				inflater.inflate(R.menu.police_information_menu, menu);
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
			case R.id.menu_item_delete_police:
			{
				currentAction = DELETE;

				displayConfirmAlert(PoliceInformationActivity.this, 
						getString(R.string.delete),
						getString(R.string.excluir_contato), 
						getString(R.string.btn_ok), 
						getString(R.string.btn_cancelar),
						PoliceInformationActivity.this);
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