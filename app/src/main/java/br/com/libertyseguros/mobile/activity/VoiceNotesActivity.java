/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.VoiceNoteHelper;
import br.com.libertyseguros.mobile.model.VoiceNote;

/**
 * Displays the form to be used to record and listen to voice activity_notes.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class VoiceNotesActivity extends LibertyMobileApp implements OnCompletionListener, OnInfoListener, DialogInterface.OnClickListener {

	private boolean isSubmitted = false;

	/**
	 * Thread which runs separately from main thread and can be used to update the progress bar and meter while
	 * recording or playing back audio.
	 * 
	 * @author N0053575 (Heidi Sturm)
	 */
	private class UpdateThread extends Thread
	{
		private int delay = 0;

		private Handler handler;

		private boolean running = false;

		/**
		 * Constructor
		 * 
		 * @param handler
		 *            the handler in the main thread to send messages to from this thread
		 * @param delay
		 *            the delay, in milliseconds, to be used between sending messages to the handler
		 */
		public UpdateThread(Handler handler, int delay)
		{
			//Log.v(TAG, ">>>  UpdateThread(Handler handler, int delay)");

			this.handler = handler;
			this.delay = delay;

			//Log.v(TAG, "<<<  UpdateThread(Handler handler, int delay)");
		}

		/**
		 * Stops the repeating process
		 */
		public void done()
		{
			//Log.v(TAG, ">>>  done()");

			this.running = false;

			//Log.v(TAG, "<<<  done()");
		}

		/**
		 * Run method is invoked automatically when the thread starts. While the thread is running, the handler will be
		 * sent a message every <code>delay</code> milliseconds.
		 * 
		 * @see Thread#run()
		 */
		@Override
		public void run()
		{
			//Log.v(TAG, ">>>  run()");

			running = true;

			// repeat until told to stop
			while (running)
			{
				try
				{
					// sleep the specified number of milliseconds between each call
					Thread.sleep(delay);
				}
				catch (InterruptedException e)
				{
					// This is not a fatal error, just log and continue
					Util.showException(null, e);
				}

				// Make the call to the handler to run the updates in the main thread
				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
			}

			//Log.v(TAG, "<<<  run()");
		}
	}

//	private static final String INSTRUCTIONS_HTML = "file:///android_asset/html/voice_note_instructions.html";
	private static final String INSTRUCTIONS_HTML = "file:///android_res/raw/voice_note_instructions.html";

	private static final int MAX_RECORDING_TIME = 90;

	private static final String STATUS_PLAYING = "PLAYING";

	private static final String STATUS_RECORDING = "RECORDING";

	private static final String TAG = VoiceNotesActivity.class.getName();

	private Button leftButton;

	private UpdateThread meterThread;

	private MediaPlayer player;

	private UpdateThread progressThread;

	private MediaRecorder recorder;

	private Button rightButton;

	private VoiceNote voiceNote = null;

	private double movingAverage = 0.0;

	private static final double MA_FILTER = 0.6;

	private ProgressBar meterBar;

	/**
	 * @return the max recorder amplitude
	 */
	public double getAmplitude()
	{
		try {
			double maxAmplitude = 0.0;
			if (recorder != null)
			{
				int maxAmplitudeInt = recorder.getMaxAmplitude();
				//Log.v("Untouched Max Amplitude : ", String.valueOf(maxAmplitudeInt));
				maxAmplitude = maxAmplitudeInt / 1800.0;
				//Log.v("Divided Max Amplitude : ", String.valueOf(maxAmplitude));
			}

			return maxAmplitude;
		} catch (Exception e) {
			Util.showException(this, e);
			return 0;
		}
	}

	/**
	 * The moving average helps to create a more smooth movement of the sound meter.
	 * (Example: no sound doesn't immediately make the bar disappear) 
	 * 
	 * @return the moving average amplitude
	 */
	public double getAmplitudeMovingAverage()
	{
		try {
			double amp = getAmplitude();
			movingAverage = MA_FILTER * amp + (1.0 - MA_FILTER) * movingAverage;
			//Log.v("Moving Average: ", String.valueOf(movingAverage));
			return movingAverage;
		} catch (Exception e) {
			Util.showException(this, e);
			return 0;
		}
	}

	/**
	 * Responds to the click of a button in an alert dialog. As all of the alert dialogs on this page are informational
	 * only, clicking a button simply closes the dialog.
	 * 
	 * @see DialogInterface.OnClickListener#onClick(DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		//Log.v(TAG, ">>> onClick()");

		// Do nothing, all alerts are informational only

		//Log.v(TAG, "<<< onClick()");

	}

	/**
	 * Invoked when the player reaches the end of a recording. Resets the view and stops the progress updates.
	 * 
	 * @see OnCompletionListener#onCompletion(MediaPlayer)
	 */
	@Override
	public void onCompletion(MediaPlayer mp)
	{
		try {
			//Log.v(TAG, ">>> onCompletion()");

			// Stop the timers
			progressThread.done();
			if (meterThread != null)
			{
				meterThread.done();
			}

			// Set the state to the default
			setStateDefault(true);

			//Log.v(TAG, "<<< onCompletion()");
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * Initializes the view, setting the layout to the voice note layout. Also builds the navigation bar and adds
	 * listeners to the buttons in the view.
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
			//Log.v(TAG, ">>> onCreate(Bundle savedInstanceState)");

			// Create the view with the voice note
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_voice_notes);

			// Set up nav bar with title only
			setUpNavigationBarTitleOnly(getString(R.string.voice_note));

			// populate the web view
			WebView webView = (WebView) findViewById(R.id.voice_notes_webview);
			webView.getSettings().setJavaScriptEnabled(false);
			webView.loadUrl(INSTRUCTIONS_HTML);

			// Setting background to transparent here as it does not work in the xml for some reason
			webView.setBackgroundColor(0);

			// Set up the buttons
			this.leftButton = (Button) findViewById(R.id.voice_note_left_button);
			this.rightButton = (Button) findViewById(R.id.voice_note_right_button);

			this.meterBar = (ProgressBar) findViewById(R.id.voice_note_meter_bar);

			// determine if we are working on a new or existing voice note
			Bundle extras = getIntent().getExtras();
			boolean isNew = extras.getBoolean(EXTRA_NAME_NEW);

			if (isNew)
			{
				voiceNote = new VoiceNote();
			}
			else
			{
				Long id = extras.getLong(EXTRA_NAME_ID);
				voiceNote = VoiceNoteHelper.get(getApplicationContext(), id);
			}

			if (voiceNote.getLengthOfVoiceNote() != null && voiceNote.getLengthOfVoiceNote() > 0)
			{
				setStateDefault(true);
			}
			else
			{
				setStateDefault(false);
			}

			player = new MediaPlayer();
			player.setOnCompletionListener(VoiceNotesActivity.this);

			if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()))
			{
				Button deleteButton = (Button) findViewById(R.id.voice_note_right_button);
				deleteButton.setClickable(false);
				deleteButton.setBackgroundResource(R.drawable.btn_record_red_disabled);
				isSubmitted = true;
			}

			//Log.v(TAG, "<<< onCreate(Bundle savedInstanceState)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Listens for messages from the recorder and performs necessary actions when the recorder stops
	 * 
	 * @see OnInfoListener#onInfo(MediaRecorder, int, int)
	 */
	@Override
	public void onInfo(MediaRecorder mr, int what, int extra)
	{
		try {
			//Log.v(TAG, ">>> onInfo()");

			// listen for the recorder stopping
			if (MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED == what)
			{
				recorderStopped();
			}

			//Log.v(TAG, "<<< onInfo()");
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * @return the listener to be used for the delete recording button
	 */
	private OnClickListener getDeleteRecordingListener()
	{
		return new OnClickListener()
		{
			/**
			 * Resets the screen to the default view and deletes the current recording.
			 * 
			 * @see OnClickListener#onClick(View)
			 */
			@Override
			public void onClick(View v)
			{
				//Log.v(TAG, ">>> DeleteRecordingListener.onClick(View v)");

				if (!isSubmitted)
				{
					// Set the state to default
					setStateDefault(false);

					// Delete the recording
					File file = new File(voiceNote.getPathToVoiceNote());
					file.delete();

					// Clear the database
					VoiceNoteHelper.delete(getApplicationContext(), voiceNote.getId());

					voiceNote = new VoiceNote();

				}
				//Log.v(TAG, "<<< DeleteRecordingListener.onClick(View v)");
			}
		};
	}

	/**
	 * @return the Handler to handle progress bar updates
	 */
	private Handler getMeterHandler()
	{
		return new Handler()
		{
			/**
			 * Updates the meter
			 * 
			 * @see Handler#handleMessage(Message)
			 */
			@Override
			public void handleMessage(Message msg)
			{
				//Log.v(TAG, ">>> handleMessage(Message msg)");

				updateMeterDisplay(false);

				//Log.v(TAG, "<<< handleMessage(Message msg)");
			}
		};
	}

	/**
	 * @return the listener to be used for the pause playing button
	 */
	private OnClickListener getPausePlayingListener()
	{
		return new OnClickListener()
		{
			/**
			 * Resets the screen to the paused playing view and pauses the player and timers.
			 * 
			 * @see OnClickListener#onClick(View)
			 */
			@Override
			public void onClick(View v)
			{
				//Log.v(TAG, ">>> PausePlayingListener.onClick(View v)");

				// Pause the player
				player.pause();

				// Stop the timers
				progressThread.stop();

				//Meter thread is not started for playing the file
				//meterThread.stop();

				// Set the state to paused
				setStatePausedPlaying();

				//Log.v(TAG, "<<< PausePlayingListener.onClick(View v)");
			}
		};
	}

	/**
	 * @return the Handler to handle progress bar updates
	 */
	private Handler getProgressHandler(String state)
	{
		try {
			Handler returnHandler;

			if (STATUS_RECORDING.equals(state))
			{
				returnHandler = new Handler()
				{
					/**
					 * Updates the progress bar by 1
					 * 
					 * @see Handler#handleMessage(Message)
					 */
					@Override
					public void handleMessage(Message msg)
					{
						//Log.v(TAG, ">>> handleMessage(Message msg)");

						if (progressThread != null && progressThread.isAlive())
						{
							ProgressBar progress = (ProgressBar) findViewById(R.id.voice_note_progress_bar);

							int currentTime = progress.getProgress() + 1;

							if (currentTime <= MAX_RECORDING_TIME)
							{
								updateCurrentTime(currentTime, MAX_RECORDING_TIME);
								updateMeterDisplay(false);
							}
							else
							{
								progressThread.done();
							}
						}

						//Log.v(TAG, "<<< handleMessage(Message msg)");
					}
				};
			}
			else
			{
				returnHandler = new Handler()
				{
					/**
					 * Updates the progress bar according to the current position of the player
					 * 
					 * @see Handler#handleMessage(Message)
					 */
					@Override
					public void handleMessage(Message msg)
					{
						//Log.v(TAG, ">>> handleMessage(Message msg)");

						if (player.isPlaying())
						{
							int position = player.getCurrentPosition() / 1000;
							int duration = player.getDuration() / 1000;
							updateCurrentTime(position, duration);
						}

						//Log.v(TAG, "<<< handleMessage(Message msg)");
					}
				};
			}

			return returnHandler;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	/**
	 * @return the listener to be used for the resume playing button
	 */
	private OnClickListener getResumePlayingListener()
	{
		return new OnClickListener()
		{
			/**
			 * Resets the screen to the playing view and starts the player and the timers.
			 * 
			 * @see OnClickListener#onClick(View)
			 */
			@Override
			public void onClick(View v)
			{
				//Log.v(TAG, ">>> ResumePlayingListener.onClick(View v)");

				// Set the state to playing
				setStatePlaying();

				// Start the player
				player.start();

				// Monitor the player to update the progress
				progressThread = new UpdateThread(getProgressHandler(STATUS_PLAYING), 1000);
				progressThread.start();

				//Log.v(TAG, "<<< ResumePlayingListener.onClick(View v)");
			}
		};
	}

	/**
	 * @return the listener to be used for the start playing button
	 */
	private OnClickListener getStartPlayingListener()
	{
		return new OnClickListener()
		{
			/**
			 * Resets the screen to the playing view and starts the player and the timers
			 * 
			 * @see OnClickListener#onClick(View)
			 */
			@Override
			public void onClick(View v)
			{
				try
				{
					if (isSubmitted)
					{
						Button stopButton = (Button) findViewById(R.id.voice_note_right_button);
						stopButton.setClickable(true);
						stopButton.setBackgroundResource(R.drawable.btn_record_red);
					}
					// Set up the player
					player.reset();
					FileInputStream inputStream = openFileInput(voiceNote.getPathToVoiceNote());
					player.setDataSource(inputStream.getFD());
					player.prepare();

					// Set the state to playing and start the player
					setStatePlaying();
					player.start();

					// Monitor the player to update the progress
					progressThread = new UpdateThread(getProgressHandler(STATUS_PLAYING), 1000);
					progressThread.start();
				}
				catch (IllegalArgumentException e)
				{
					Log.e(TAG, "IllegalArgumentException", e);
					showPlayErrorAlert();
				}
				catch (IllegalStateException e)
				{
					Log.e(TAG, "IllegalStateException", e);
					showPlayErrorAlert();
				}
				catch (IOException e)
				{
					Log.e(TAG, "IOException", e);
					showPlayErrorAlert();
				}
			}
		};
	}

	/**
	 * @return the listener to be used for the start recording button
	 */
	private OnClickListener getStartRecordingListener()
	{
		return new OnClickListener()
		{
			/**
			 * Resets the screen to the recording view and starts the recorder and the timers
			 * 
			 * @see OnClickListener#onClick(View)
			 */
			@Override
			public void onClick(View v)
			{
				//Log.v(TAG, ">>> StartRecordingListener.onClick(View v)");

				try
				{
					prepareRecorder();

					// Start recording
					recorder.start();

					// Set the state to recording
					setStateRecording();

					// Monitor the recording to update the timers
					progressThread = new UpdateThread(getProgressHandler(STATUS_RECORDING), 1000);
					progressThread.start();

					meterThread = new UpdateThread(getMeterHandler(), 150);
					meterThread.start();
				}
				catch (IllegalStateException e)
				{
					Log.e(TAG, "IllegalStateException starting the recorder", e);
					showRecordErrorAlert();
				}
				catch (IOException e)
				{
					Log.e(TAG, "IOException starting the recorder", e);
					showRecordErrorAlert();
				}

				//Log.v(TAG, "<<< StartRecordingListener.onClick(View v)");
			}
		};
	}

	/**
	 * @return the listener to be used for the stop playing button
	 */
	private OnClickListener getStopPlayingListener()
	{
		return new OnClickListener()
		{
			/**
			 * Resets the screen to the default view and stops the player
			 * 
			 * @see OnClickListener#onClick(View)
			 */
			@Override
			public void onClick(View v)
			{
				//Log.v(TAG, ">>> StopPlayingListener.onClick(View v)");

				if (isSubmitted)
				{
					Button stopButton = (Button) findViewById(R.id.voice_note_right_button);
					stopButton.setClickable(false);
					stopButton.setBackgroundResource(R.drawable.btn_record_red_disabled);
				}
				// Stop and reset the player
				player.stop();
				player.seekTo(0);

				// Stop the timers
				progressThread.done();

				//Meter thread is not started for playing the file
				//meterThread.done();

				// Set the state to the default
				setStateDefault(true);

				//Log.v(TAG, "<<< StopPlayingListener.onClick(View v)");
			}
		};
	}

	/**
	 * @return the listener to be used for the stop recording button
	 */
	private OnClickListener getStopRecordingListener()
	{
		return new OnClickListener()
		{
			/**
			 * Resets the screen to the default view and stops the recording.
			 * 
			 * @see OnClickListener#onClick(View)
			 */
			@Override
			public void onClick(View v)
			{
				//Log.v(TAG, ">>> StopRecordingListener.onClick(View v)");

				// Stop the recorder
				recorder.stop();
				recorder.release();
				recorder = null;

				recorderStopped();

				//Log.v(TAG, "<<< StopRecordingListener.onClick(View v)");
			}
		};
	}

	/**
	 * Completes the necessary step to prepare the recorder for recording
	 * 
	 * @throws IOException
	 */
	private void prepareRecorder() throws IOException
	{
		try {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setMaxDuration(90000);
			recorder.setOnInfoListener(VoiceNotesActivity.this);

			// if the event already has a voice note, use it, otherwise create one
			String path = voiceNote.getPathToVoiceNote();

			if (path == null || path.length() < 1)
			{
				// Add a timestamp to make it unique
				path = Calendar.getInstance().getTimeInMillis() + VoiceNoteHelper.FILE_NAME;

				voiceNote.setPathToVoiceNote(path);
			}

			FileOutputStream outputStream = openFileOutput(path, MODE_PRIVATE);
			recorder.setOutputFile(outputStream.getFD());
			recorder.prepare();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Performs the actions necessary after the recorder has stopped
	 */
	private void recorderStopped()
	{
		try {
			// Stop the timers
			if (progressThread != null)
			{
				progressThread.done();
			}
			if (meterThread != null)
			{
				meterThread.done();
			}

			// Save the info
			try
			{
				// Get the length of the recording
				player.reset();
				String path = voiceNote.getPathToVoiceNote();
				FileInputStream inputStream = openFileInput(path);
				player.setDataSource(inputStream.getFD());

				player.prepare();

				voiceNote.setLengthOfVoiceNote(player.getDuration() / 1000);

				// Insert or update the data to the database
				if (voiceNote.getId() == null)
				{
					long id = VoiceNoteHelper.insert(getApplicationContext(), voiceNote, getCurrentEvent().getId());
					voiceNote.setId(id);
				}
				else
				{
					VoiceNoteHelper.update(getApplicationContext(), voiceNote);
				}
			}
			catch (IllegalArgumentException e)
			{
				// This is not a fatal error, just log and continue
				Log.e(TAG, "IllegalArgumentException preparing player", e);
			}
			catch (IllegalStateException e)
			{
				// This is not a fatal error, just log and continue
				Log.e(TAG, "IllegalStateException preparing player", e);
			}
			catch (IOException e)
			{
				// This is not a fatal error, just log and continue
				Log.e(TAG, "IOException preparing player", e);
			}

			// Set the state to default
			setStateDefault(true);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets up the screen in the default state (left button play, right button record if no voice note is present,
	 * delete recording if voice note is present)
	 * 
	 * @param voiceNotePresent
	 *            whether or not a voice note is currently present
	 */
	private void setStateDefault(boolean voiceNotePresent)
	{
		try {
			//Log.v(TAG, ">>> setStateDefault(boolean voiceNotePresent)");

			// left button is play and enabled if voice note is present
			this.updateButton(leftButton, getString(R.string.voice_note_play), getStartPlayingListener(), voiceNotePresent);

			// right button is record/edit recording and enabled; timers are at 0 and either voicenote length or maximum
			// length of voicenote if none is yet present
			if (voiceNotePresent)
			{
				this.updateCurrentTime(0, voiceNote.getLengthOfVoiceNote());
				this.updateButton(rightButton, getString(R.string.voice_note_delete_recording),
						getDeleteRecordingListener(), true);
			}
			else
			{
				this.updateCurrentTime(0, MAX_RECORDING_TIME);
				this.updateButton(rightButton, getString(R.string.voice_note_record), getStartRecordingListener(), true);
			}

			// Clear the meter
			this.updateMeterDisplay(true);

			//Log.v(TAG, "<<< setStateDefault(boolean voiceNotePresent)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets up the screen in the paused state (left button resume playing, right button stop playing)
	 */
	private void setStatePausedPlaying()
	{
		try {
			//Log.v(TAG, ">>> setStatePausedPlaying()");

			// left button is Resume Playing
			this.updateButton(leftButton, getString(R.string.voice_note_resume_playing), getResumePlayingListener(), true);

			// right button is Stop (Playing)
			this.updateButton(rightButton, getString(R.string.voice_note_stop), getStopPlayingListener(), true);

			// Clear the meter
			this.updateMeterDisplay(true);

			//Log.v(TAG, "<<< setStatePausedPlaying()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets up the screen in the playing state (left button pause playing, right button stop playing)
	 */
	private void setStatePlaying()
	{
		try {
			//Log.v(TAG, ">>> setStatePlaying()");

			// left button is Pause (Playing)
			this.updateButton(leftButton, getString(R.string.voice_note_pause), getPausePlayingListener(), true);

			// right button is Stop (Playing)
			this.updateButton(rightButton, getString(R.string.voice_note_stop), getStopPlayingListener(), true);

			//Log.v(TAG, "<<< setStatePlaying()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets up the screen in the recording state (left button pause recording, right button stop recording)
	 */
	private void setStateRecording()
	{
		try {
			//Log.v(TAG, ">>> setStateRecording()");

			// left button is Pause (Recording)
			this.updateButton(leftButton, getString(R.string.voice_note_play), null, false);

			// right button is Stop (Recording)
			this.updateButton(rightButton, getString(R.string.voice_note_stop), getStopRecordingListener(), true);

			//Log.v(TAG, "<<< setStateRecording()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Displays a message to the user that playback failed
	 */
	private void showPlayErrorAlert()
	{
		try {
			displayInfoAlert(VoiceNotesActivity.this, null, getString(R.string.error_unable_to_play),
					getString(R.string.btn_ok), VoiceNotesActivity.this);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Displays a message to the user that recording failed
	 */
	private void showRecordErrorAlert()
	{
		try {
			displayInfoAlert(VoiceNotesActivity.this, null, getString(R.string.error_unable_to_record),
					getString(R.string.btn_ok), VoiceNotesActivity.this);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Updates a button with a given title and on click action
	 * 
	 * @param button
	 *            the button to update
	 * @param title
	 *            the new title for the button
	 * @param listener
	 *            the listener that will be notified when the button is clicked
	 * @param enabled
	 *            whether or not the button is enabled
	 */
	private void updateButton(Button button, String title, OnClickListener listener, boolean enabled)
	{
		try {
			//Log.v(TAG, ">>> updateButton(Button button, String title, OnClickListener listener, boolean enabled)");

			button.setText(title);
			button.setEnabled(enabled);
			button.setOnClickListener(listener);

			//Log.v(TAG, "<<< updateButton(Button button, String title, OnClickListener listener, boolean enabled)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Updates the progress indicators on the screen with the given current and total times
	 * 
	 * @param currentTime
	 *            the time to display in the minimum time label
	 * @param totalTime
	 *            the time to display in the maximum time label
	 */
	private void updateCurrentTime(int currentTime, int totalTime)
	{
		try {
			//Log.v(TAG, ">>> updateCurrentTime(int currentTime, int totalTime)");

			// Update the min label
			TextView currentTimeLabel = (TextView) findViewById(R.id.current_time_label);
			currentTimeLabel.setText((String.format("%2d:%02d", (currentTime / 60), (currentTime % 60))).trim());

			// Update the max label
			TextView totalTimeLabel = (TextView) findViewById(R.id.total_time_label);
			totalTimeLabel.setText((String.format("%2d:%02d", (totalTime / 60), (totalTime % 60))).trim());

			// Update the progress bar
			ProgressBar recordingProgress = (ProgressBar) findViewById(R.id.voice_note_progress_bar);
			recordingProgress.setMax(totalTime);
			recordingProgress.setProgress(currentTime);

			//Log.v(TAG, "<<< updateCurrentTime(int currentTime, int totalTime)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * If clear is true, the meter will be hidden, otherwise it will randomly display or hide.
	 * 
	 * @param clear
	 *            whether or not the meter should be cleared
	 */
	private void updateMeterDisplay(boolean clear)
	{
		try {
			//Log.v(TAG, ">>> updateMeterDisplay(float currentPowerLevel)");

			if (!clear)
			{
				meterBar.setProgress((int) getAmplitudeMovingAverage());
			}
			else
			{
				meterBar.setProgress(0);
			}

			/*
	        int currentPowerLevel = 0;

	        if (!clear)
	        {
	            currentPowerLevel = (int) (Math.random() * 2);
	        }

	        ImageView meter = (ImageView) findViewById(R.id.voice_note_meter);

	        if (currentPowerLevel % 2 == 0)
	        {
	            meter.setBackgroundDrawable(null);
	        }
	        else
	        {
	            meter.setBackgroundResource(R.drawable.audio_meter);
	        }
			 */

			//Log.v(TAG, "<<< updateMeterDisplay(float currentPowerLevel)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Actions to perform when the screen is going to become inactive. If the player or recorder are running, they will
	 * be stopped.
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause()
	{
		try {
			//Log.v(TAG, ">>> onPause()");

			super.onPause();

			// click the stop button if playing or recording
			if (progressThread != null && progressThread.running)
			{
				rightButton.performClick();
			}

			//Log.v(TAG, "<<< onPause()");
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
