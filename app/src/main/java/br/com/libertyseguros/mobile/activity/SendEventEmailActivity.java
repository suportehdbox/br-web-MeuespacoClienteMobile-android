/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.FileUtils;
import br.com.libertyseguros.mobile.common.util.ImageUtils;
import br.com.libertyseguros.mobile.common.util.MailUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.database.UserHelper;
import br.com.libertyseguros.mobile.model.EventPhoto;
import br.com.libertyseguros.mobile.model.User;
import br.com.libertyseguros.mobile.model.Vehicle;

/**
 * Displays the splash page to the user
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class SendEventEmailActivity extends LibertyMobileApp implements OnClickListener, DialogInterface.OnClickListener {

	/** Nested class that performs attachments */
	final class AttachProgressThread extends Thread
	{
		private Handler mHandler;

		/**
		 * @param h
		 */
		AttachProgressThread(Handler h)
		{
			mHandler = h;
		}

		/**
		 * @see Thread#run()
		 */
		public void run()
		{
			int[] sectionCounts = new int[4];
			for (int i = 0; i < sectionCounts.length; i++)
			{
				sectionCounts[i] = 0;
			}
			ArrayList<EventPhoto> photos = getCurrentEvent().getPhotos();

			Collections.sort(photos);

			Iterator<EventPhoto> photoIterator = photos.iterator();
			int count = 0;

			while (photoIterator.hasNext())
			{
				MailUtils.copyImage(getCurrentEvent().getEventType(), sectionCounts,
						getApplicationSpecificCacheDirectory(), photoIterator.next());
				int percent = (int) (((++count * 1.0) / (photos.size() * 1.0)) * 100.0);
				Message msg = mHandler.obtainMessage();
				msg.arg1 = percent;
				mHandler.sendMessage(msg);
			}
		}
	}

	/** Nested class that performs scaling */
	final class ScaleProgressThread extends Thread
	{
		private Handler mHandler;

		/**
		 * @param h
		 */
		ScaleProgressThread(Handler h)
		{
			mHandler = h;
		}

		/**
		 * @see Thread#run()
		 */
		public void run()
		{
			File[] images =
					FileUtils.getPathsInDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()
							+ getApplicationSpecificCacheDirectory());

			for (int i = 0; i < images.length; i++)
			{
				try
				{
					ExifInterface exifInterface = new ExifInterface(images[i].getAbsolutePath());
					int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
					Bitmap scaledBitmap = null;
					if (ImageUtils.getMaximumBitmapDimension(images[i].getAbsolutePath()) > 1000)
					{
						scaledBitmap = ImageUtils.scaleImage(images[i].getAbsolutePath());
					}
					else
					{
						scaledBitmap = BitmapFactory.decodeFile(images[i].getAbsolutePath());
					}
					if (scaledBitmap != null)
					{
						ImageUtils.saveBitmapToFile(scaledBitmap, images[i].getAbsolutePath(),
								Bitmap.CompressFormat.JPEG);
						if (orientation != -1)
						{
							// If the original image had an orientation, set it on the scaled version.
							exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,
									new Integer(orientation).toString());
							exifInterface.saveAttributes();
						}
					}
					else
					{
						// we want to exit if we got a null image back.
						i = images.length;
						setScaleError(true);
					}
				}
				catch (FileNotFoundException e)
				{
					Util.showException(null, e);
				}
				catch (IOException e)
				{
					Util.showException(null, e);
				}

				int percent = (int) ((((i + 1) * 1.0) / (images.length * 1.0)) * 100.0);
				Message msg = mHandler.obtainMessage();
				msg.arg1 = percent;
				mHandler.sendMessage(msg);
			}
		}
	}

	private boolean scaleError = false;

	public static final int CALL_AUTO_DIALOG = 1;

	public static final int CALL_HOME_DIALOG = 2;

	public static final int COPY_PROGRESS_DIALOG = 3;

	public static final int SCALING_PROGRESS_DIALOG = 4;

	public static final int ERROR_DIALOG = 5;

//	private static final String SEND_EVENT_EMAIL_HTML = "file:///android_asset/html/infoemailsinistro.html";
	private static final String SEND_EVENT_EMAIL_HTML = "file:///android_res/raw/infoemailsinistro.html";

	private static final String TAG = SendEventEmailActivity.class.getName();

	private ProgressDialog progressDialog;

	private Dialog errorDialog;

	private AttachProgressThread attachProgressThread;

	private ScaleProgressThread scaleProgressThread;

	// Define the Handler that receives messages from the thread and update the progress
	private final Handler scaleHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			int total = msg.arg1;
			progressDialog.setProgress(total);
			if (total >= 100)
			{
				dismissDialog(SCALING_PROGRESS_DIALOG);
				continueEmailProcess();
			}
		}
	};

	// Define the Handler that receives messages from the thread and update the progress
	private final Handler attachHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			int total = msg.arg1;
			progressDialog.setProgress(total);
			if (total >= 100)
			{
				dismissDialog(COPY_PROGRESS_DIALOG);
				showDialog(SCALING_PROGRESS_DIALOG);
			}
		}
	};

	// private long attachmentSize;

	/**
	 * Prompts the user to verify that they want to continue.
	 * 
	 * @param v
	 */
	public void continueButtonPressed(View v)
	{
		try {
			// Log.v(TAG, ">>> continueButtonPressed(View v)");

			// reload the current event to make sure we have all of the data
			setCurrentEvent(EventHelper.get(getApplicationContext(), getCurrentEvent().getId()));

			// Remove cache files from previous email
			FileUtils.deleteAllFilesInDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()
					+ getApplicationSpecificCacheDirectory());

			if (!eventHasPhotos())
			{
				continueEmailProcess();
			}
			else
			{
				showDialog(COPY_PROGRESS_DIALOG);
			}

			// Log.v(TAG, ">>> continueButtonPressed(View v)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	public void ligarButtonPressed(View v)
	{
		try {
			showDialog(CALL_AUTO_DIALOG);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}


	/**
	 * 
	 */
	private void continueEmailProcess()
	{
		try {
			if (!isScaleError())
			{
				if (eventHasPhotos())
				{
					long bytes =
							FileUtils.bytesInDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()
									+ getApplicationSpecificCacheDirectory());
					Log.i(TAG, "After scaling, photos are " + bytes + " bytes.");
				}
				// We need to make a copy of the vehicle involved, if appropriate, so that it can't be deleted later
				if (Constants.LOB_AUTO.equals(getCurrentEvent().getEventType()))
				{
					Vehicle vehicle = getCurrentEvent().getVehicleInvolved();
					if (vehicle != null)
					{
						// If we clear the id, a new vehicle will be created
						vehicle.setId(null);
						getCurrentEvent().setVehicleInvolved(vehicle);
					}
				}

				// We need to make a copy of the user
				User user = UserHelper.getCurrent(getApplicationContext());
				if (user != null)
				{
					User submittedUser = UserHelper.copy(getApplicationContext(), user);
					getCurrentEvent().setSubmittedUser(submittedUser);
				}

				// Mark the claim as sent
				getCurrentEvent().setEventStatus(Constants.EVENT_STATUS_SUBMITTED);
				getCurrentEvent().setSubmitDateTime(new Timestamp(new Date().getTime()));

				EventHelper.update(getApplicationContext(), getCurrentEvent());

				//Chama tela para envio de e-mail
				startEventEmail(getCurrentEvent());

				//setResult(RESULT_OK);
				//finish();
			}
			else
			{
				showDialog(ERROR_DIALOG);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @return
	 */
	private boolean eventHasPhotos()
	{
		try {
			boolean returnVal = true;
			if (getCurrentEvent().getPhotos() == null)
			{
				returnVal = false;
			}
			else if (getCurrentEvent().getPhotos().size() == 0)
			{
				returnVal = false;
			}
			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * @return the scaleError
	 */
	public boolean isScaleError()
	{
		try {
			return scaleError;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Responds to the click of a button in the view and performs the action associated with the button that was
	 * clicked. If the left navigation button was clicked, the activity is finished. If the continue button was clicked,
	 * the My Info activity is started.
	 * 
	 * @param v
	 *            the View that was clicked, in this case a button
	 * @see OnClickListener#onClick(View)
	 */
	@Override
	public void onClick(View v)
	{
		// Log.v(TAG, ">>> onClick(View v)");

		// Log.v(TAG, "<<< onClick(View v)");
	}

	/**
	 * Initializes the view, setting the layout to the splash page layout.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {
			// Log.v(TAG, ">>> onCreate(Bundle savedInstanceState)");

			// Create the view
			super.onCreate(savedInstanceState);

			setContentView(R.layout.send_event_email);

			// Set up nav bar with title only
			setUpNavigationBarTitleOnly(getString(R.string.title_activity_send_email));

			// populate the web view
			WebView webView = (WebView) findViewById(R.id.send_event_email_webview);
			//webView.getSettings().setJavaScriptEnabled(true);

			// << EPO
			//        // Allow javascript to open an alertDialog
			//        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
			//        jsInterface.setLob(getCurrentEvent().getEventType());
			//        webView.addJavascriptInterface(jsInterface, "androidInterface");
			// >>

			// Setting background to transparent here as it does not work in the xml for some reason
			webView.setBackgroundColor(0);

			webView.loadUrl(SEND_EVENT_EMAIL_HTML);

			// << EPO - Ajuste encode:
			WebSettings settings = webView.getSettings();
			settings.setDefaultTextEncodingName("utf-8");
			String sHtml = Util.readTextFromResource(SendEventEmailActivity.this, R.raw.infoemailsinistro);
			webView.loadDataWithBaseURL(null, sHtml.replaceAll("\\+", " "), "text/html", "utf-8", null);
			// >>

		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		try {
			Dialog dialog;
			AlertDialog.Builder builder;

			String title = null;
			String message = null;
			String phoneNumber = null;

			switch (id)
			{
			case CALL_AUTO_DIALOG:
				builder = new AlertDialog.Builder(this);

				phoneNumber = getString(R.string.btn_atendimento_assistencia_24h_auto_vida);
				title = getString(R.string.confirm_call_auto_specialist);
				message = phoneNumber;

				builder.setMessage(message).setCancelable(false).setTitle(title)
				.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse("tel:" + R.string.btn_atendimento_assistencia_24h_auto_vida));
						startActivity(callIntent);
					}
				}).setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						// dismiss dialog
						dialog.dismiss();
					}
				});
				dialog = builder.create();
				break;
			case COPY_PROGRESS_DIALOG:
				progressDialog = new ProgressDialog(SendEventEmailActivity.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setMessage(getString(R.string.preparando_imagens));
				progressDialog.setCancelable(false);
				dialog = progressDialog;
				break;
			case SCALING_PROGRESS_DIALOG:
				progressDialog = new ProgressDialog(SendEventEmailActivity.this);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setMessage(getString(R.string.preparando_imagens));
				progressDialog.setCancelable(false);
				dialog = progressDialog;
				break;
			case ERROR_DIALOG:
				builder = new AlertDialog.Builder(this);

				title = getString(R.string.error_dialog_title);
				message = getString(R.string.error_dialog_msg);

				builder.setMessage(message).setCancelable(false).setTitle(title)
				.setNeutralButton(R.string.btn_ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						// dismiss dialog
						dialog.dismiss();
						finish();
					}
				});
				errorDialog = builder.create();
				return errorDialog;
			default:
				dialog = null;
				break;
			}
			return dialog;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		try {
			switch (id)
			{
			case COPY_PROGRESS_DIALOG:
				progressDialog.setProgress(0);
				attachProgressThread = new AttachProgressThread(attachHandler);
				attachProgressThread.start();
				break;
			case SCALING_PROGRESS_DIALOG:
				progressDialog.setProgress(0);
				scaleProgressThread = new ScaleProgressThread(scaleHandler);
				scaleProgressThread.start();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param scaleError
	 *            the scaleError to set
	 */
	public void setScaleError(boolean scaleError)
	{
		try {
			this.scaleError = scaleError;
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			if (requestCode == EMAIL_INFO_INTENT) {
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

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}
}
