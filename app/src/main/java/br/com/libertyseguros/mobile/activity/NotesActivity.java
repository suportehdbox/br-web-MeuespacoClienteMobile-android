/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.ValidationUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.database.VoiceNoteHelper;
import br.com.libertyseguros.mobile.model.Event;
import br.com.libertyseguros.mobile.model.VoiceNote;

/**
 * Displays the form to be used to add text activity_notes and access voice activity_notes.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class NotesActivity extends LibertyMobileApp implements OnClickListener, DialogInterface.OnClickListener {

	//    private static final String TAG = NotesActivity.class.getName();

	//  << EPO
	private boolean wantSave;
	// >>

	/**
	 * Responds to the user's selection in an alert dialog, in this case confirming that the user wants to cancel their
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
			// Log.v(TAG, ">>> onClick(DialogInterface dialog, int which)");

			// The user selected yes
			if (which == -1)
			{
				// << EPO
				// Finish the activity
				//            finish();
				wantSave = true;
				salvaEfecha(null);
			}
			else if (which == -2)
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
	 * Responds to the click of a button in the view and performs the action associated with the button that was
	 * clicked. If the user clicked the save button, the form is saved and the Activity is finished. If the user clicked
	 * the voicenote button, the user is forwarded to the Voice Note Activity.
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

			// If the user clicked the add voicenote button, go to the voicenote activity to create a new voicenote
			if (v.equals(findViewById(R.id.add_voice_note_button)))
			{
				Intent i = new Intent(getApplicationContext(), VoiceNotesActivity.class);
				i.putExtra(EXTRA_NAME_NEW, true);
				startActivity(i);
			}
			else
			{
				Intent i = new Intent(getApplicationContext(), VoiceNotesActivity.class);

				ArrayList<VoiceNote> voiceNotes = VoiceNoteHelper.getByEvent(NotesActivity.this, getCurrentEvent().getId());

				if (v.equals(findViewById(R.id.voice_note_0)))
				{
					i.putExtra(EXTRA_NAME_ID, voiceNotes.get(0).getId());
					startActivity(i);
				}
			}

			// Log.v(TAG, "<<< onClick(View v)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Initializes the view, setting the layout to the activity_notes layout. Also builds the navigation bar and adds listeners
	 * to the buttons in the view.
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

			// Create the view with the app info layout
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_notes);

			// Set up nav bar with title
			setUpNavigationBarTitleOnly(getString(R.string.add_your_notes));

			// Set up the buttons with listeners
			Button voiceNoteButton = (Button) findViewById(R.id.add_voice_note_button);
			voiceNoteButton.setOnClickListener(NotesActivity.this);

			// Prefill the form
			EditText note = (EditText) findViewById(R.id.notes);

			// << EPO
			note.setHint(R.string.add_your_notes);
			// >>
			note.setText(getCurrentEvent().getNotes());

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
				if (Constants.EVENT_STATUS_DRAFT.equals(getCurrentEvent().getEventStatus()) && isFormChanged())
				{
					// << EPO caso bot�o voltar: Salva e volta

					//                // Ask the user if they want to discard their changes
					//                displayConfirmAlert(NotesActivity.this, getString(R.string.go_back_without_saving),
					//                    getString(R.string.go_back_without_saving_confirmation), getString(R.string.btn_ok),
					//                    getString(R.string.no), NotesActivity.this);
					//                return false;

					if(wantSave) {
						salvaEfecha(null);
					}
					else {
						// Ask the user if they want to discard their changes
						displayConfirmAlert(NotesActivity.this,
								getString(R.string.aviso),
								getString(R.string.deseja_salvar_alteracoes),
								getString(R.string.btn_ok),
								getString(R.string.btn_cancelar),
								NotesActivity.this);

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
	 * Checks to see if the user has made any changes to the form since it was last saved
	 * 
	 * @return true if the form has changed, false if it has not
	 */
	private boolean isFormChanged()
	{
		try {
			// Log.v(TAG, ">>> isFormChanged()");

			boolean returnVal = false;

			TextView noteView = (TextView) findViewById(R.id.notes);

			String newNote = noteView.getText().toString();
			String oldNote = getCurrentEvent().getNotes();

			if (ValidationUtils.isStringEmpty(newNote) && ValidationUtils.isStringEmpty(oldNote))
			{
				returnVal = false;
			}
			else if (!newNote.equals(oldNote))
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
	 * @return true if the form is saved, false if the form cannot be saved due to missing info
	 */
	private boolean saveForm()
	{
		try {
			// Log.v(TAG, ">>> saveForm()");

			// Create the model objects from the entered data
			TextView note = (TextView) findViewById(R.id.notes);

			// Update the data to the database
			Event event = getCurrentEvent();
			event.setNotes(note.getText().toString());

			EventHelper.update(getApplicationContext(), event);

			// Log.v(TAG, "<<< saveForm()");

			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Sets up the display of any previously recorded voicenotes
	 */
	private void setUpVoiceNotes()
	{
		try {
			// Log.v(TAG, ">>> setUpVoiceNotes()");

			ArrayList<VoiceNote> voiceNotes = VoiceNoteHelper.getByEvent(NotesActivity.this, getCurrentEvent().getId());

			if (voiceNotes == null || voiceNotes.size() < 1)
			{
				findViewById(R.id.tl_voice_note_0).setVisibility(View.GONE);
				findViewById(R.id.rl_add_voice_note_button).setVisibility(View.VISIBLE);
			}
			else
			{
				// TODO - This is a quick fix to solve the scrolling issue and should be re-written to be
				// dynamic
				int voiceNoteCount = voiceNotes.size();
				if (voiceNoteCount > 0)
				{
					VoiceNote voiceNoteOne = voiceNotes.get(0);
					Button voiceNoteOneView = (Button) findViewById(R.id.voice_note_0);

					long length = voiceNoteOne.getLengthOfVoiceNote();

					if (length > 0)
					{
						String title = getString(R.string.voice_note_title);
						String formattedLength = String.format("%2d:%02d", (length / 60), (length % 60));

						createCompoundButton(NotesActivity.this, voiceNoteOneView, title, formattedLength);
					}

					voiceNoteOneView.setOnClickListener(NotesActivity.this);
					findViewById(R.id.tl_voice_note_0).setVisibility(View.VISIBLE);
					findViewById(R.id.rl_add_voice_note_button).setVisibility(View.GONE);
				}
				else
				{
					findViewById(R.id.rl_add_voice_note_button).setVisibility(View.VISIBLE);
				}
			}

			// Log.v(TAG, "<<< setUpVoiceNotes()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will disable the ability to update the activity_notes.
	 */
	private void disableUpdates()
	{
		try {
			EditText notesEditText = (EditText) findViewById(R.id.notes);
			notesEditText.setFocusable(false);

			findViewById(R.id.rl_add_voice_note_button).setVisibility(View.GONE);

			// Log.v(TAG, "<<< setUpVoiceNotes()");
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

			// refresh the page on returning to make sure we have the most up to date data
			setUpVoiceNotes();

			if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()))
			{
				disableUpdates();
			}

			// Log.v(TAG, "<<< onResume()");

			// Log.v(TAG, "<<< setUpVoiceNotes()");
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
