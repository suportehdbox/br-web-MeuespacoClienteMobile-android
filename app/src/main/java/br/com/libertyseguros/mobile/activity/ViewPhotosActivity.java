/*
 * Copyright (c) 2011, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.ImageUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventPhotoHelper;
import br.com.libertyseguros.mobile.model.EventPhoto;

/**
 * @author N0053575 (Heidi Sturm)
 * 
 * Update 08/08/2013 @author Evandro  
 */
public class ViewPhotosActivity extends LibertyMobileApp
{
	private long eventPhotoId;

	private ImageView imageView;

	private Button previousButton;

	private Button nextButton;

	private Button playButton;

	private EventPhoto eventPhoto;

	private List<EventPhoto> eventPhotos;

	/**
	 * The logging tag
	 */
	private static final String TAG = ViewPhotosActivity.class.getName();

	/**
	 * Displays the confirm delete dialog
	 * 
	 * @param v
	 *            The button pressed
	 */
	public void deletePhoto(View v)
	{
		try {
			showDialog(CONFIRM_DELETE_DIALOG);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will delete the currently displayed photo. It will then switch the view to the next photo... unless
	 * the last photo was deleted. In that case it shows the previously deleted photo.
	 */
	private void deletePhotoConfirmed()
	{
		try {
			EventPhotoHelper.delete(getApplicationContext(), eventPhotoId, getApplicationSpecificFileDirectory());
			long nextId = getNextPhotoId();
			long prevId = getPreviousPhotoId();
			boolean lastPhoto = isLastPhoto();
			removePhotoFromList(eventPhotoId);
			if (eventPhotos.size() == 0)
			{
				finish();
				return;
			}
			if (lastPhoto)
			{
				showPhoto(prevId);
			}
			else
			{
				showPhoto(nextId);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will return the id of the next photo
	 * 
	 * @return id of the next photo
	 */
	private long getNextPhotoId()
	{
		try {
			Iterator<EventPhoto> iterator = eventPhotos.iterator();
			boolean useNextPhoto = false;
			long id = -1;
			while (iterator.hasNext())
			{
				EventPhoto photo = iterator.next();
				id = photo.getId();
				if (useNextPhoto)
				{
					break;
				}
				if (photo.getId() == eventPhotoId)
				{
					useNextPhoto = true;
				}
			}
			return id;
		} catch (Exception e) {
			Util.showException(this, e);
			return 0;
		}
	}

	/**
	 * This method will return the id of the previous photo.
	 * 
	 * @return id of the previous photo.
	 */
	private long getPreviousPhotoId()
	{
		try {
			Iterator<EventPhoto> iterator = eventPhotos.iterator();
			long lastId = -1;
			while (iterator.hasNext())
			{
				EventPhoto photo = iterator.next();
				if (photo.getId() == eventPhotoId)
				{
					break;
				}
				lastId = photo.getId();
			}
			return lastId;
		} catch (Exception e) {
			Util.showException(this, e);
			return 0;
		}
	}

	/**
	 * This method will start the image view intent
	 * 
	 * @param v
	 *            The ImageView that was clicked.
	 */
	private void imageClicked(View v)
	{
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			String fileName =
					"file:///" + Environment.getExternalStorageDirectory().getAbsolutePath() + eventPhoto.getPhotoPath();
			Uri uri = Uri.parse(fileName);
			intent.setDataAndType(uri, "image/*");
			startActivity(intent);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method returns true if the current photo is the last photo in the list
	 * 
	 * @return true if current photo is the last one, else false
	 */
	private boolean isLastPhoto()
	{
		try {
			EventPhoto photo = eventPhotos.get(eventPhotos.size() - 1);
			if (photo.getId() == eventPhotoId)
			{
				return true;
			}
			return false;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_view_photos);

			eventPhotos = EventPhotoHelper.getByEvent(getApplicationContext(), getCurrentEvent().getId());

			Bundle extras = getIntent().getExtras();
			if (extras != null)
			{
				eventPhotoId = extras.getInt(Constants.EVENT_PHOTO_ID);
			}
			else
			{
				eventPhotoId = eventPhotos.get(0).getId().intValue();
			}

			imageView = (ImageView) findViewById(R.id.iv_main_photo);
			previousButton = (Button) findViewById(R.id.btn_prev_pht);
			nextButton = (Button) findViewById(R.id.btn_next_pht);
			playButton = (Button) findViewById(R.id.btn_play_pht);
			Button delButton = (Button) findViewById(R.id.btn_delete_pht);
			
			// Caso o aviso já tenha sido enviado não remove a imagem
			String status = getCurrentEvent().getEventStatus();
			if (Constants.EVENT_STATUS_SUBMITTED.equals(status))
			{
				delButton.setVisibility(Button.GONE);
			}

			// This title gets over ridden in the next method.
			setUpNavigationBarTitleOnly("View Photos");
			setupButtonsAndTitle();
			setupImage();
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		try {
			Dialog dialog;
			switch (id)
			{
			case CONFIRM_DELETE_DIALOG:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.delete));
				builder.setMessage(getString(R.string.confirm_photo_delete)).setCancelable(false)
				.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						deletePhotoConfirmed();
					}
				}).setNegativeButton(getString(R.string.btn_cancelar), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});
				dialog = builder.create();
				break;
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

	/**
	 * This method will remove an eventPhoto from the eventPhotos list
	 * 
	 * @param id
	 *            the id of the photo to remove
	 */
	private void removePhotoFromList(long id)
	{
		try {
			Iterator<EventPhoto> iterator = eventPhotos.iterator();
			int index = 0;
			while (iterator.hasNext())
			{
				EventPhoto photo = iterator.next();
				if (photo.getId() == id)
				{
					break;
				}
				index++;
			}
			eventPhotos.remove(index);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will enable/disable the previous and next buttons
	 * 
	 * @param current
	 *            The number of the image currently displayed 1 based.
	 * @param last
	 *            The count of total images.
	 */
	private void setPrevAndNextButtons(int current, int last)
	{
		try {
			if (current == 1)
			{
				previousButton.setEnabled(false);
			}
			else
			{
				previousButton.setEnabled(true);
			}

			if (current == last)
			{
				nextButton.setEnabled(false);
			}
			else
			{
				nextButton.setEnabled(true);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Sets the correct number in the title, and enables/disables the buttons.
	 */
	private void setupButtonsAndTitle()
	{
		try {
			Iterator<EventPhoto> photoIterator = eventPhotos.iterator();
			int current = 1;
			int last = 1;
			int i = 1;
			while (photoIterator.hasNext())
			{
				EventPhoto p = photoIterator.next();
				if (p.getId() == eventPhotoId)
				{
					current = i;
				}
				last = i;
				i++;
			}

			updateNavigationBarTitle(current + getString(R.string.de) + last);
			setPrevAndNextButtons(current, last);

			// TODO: implement the play button.
			playButton.setEnabled(false);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Displays the current image on the screen.
	 */
	private void setupImage()
	{
		try {
			eventPhoto = EventPhotoHelper.get(getApplicationContext(), eventPhotoId);

			String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + eventPhoto.getPhotoPath();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fullPath, options);

			int srcWidth = options.outWidth;
			int srcHeight = options.outHeight;

			int orientation = -1;
			try
			{
				ExifInterface exifInterface = new ExifInterface(fullPath);
				orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, orientation);
			}
			catch (IOException e)
			{
				// Do nothing, just go with the default orientation, determined from the image ratio
				Log.i(TAG, "Could not determine orientation from Exif data");
			}

			int tmp;

			// We just need to determine if the image is going to change orientation from landscape to portrait or vice
			// versa.
			switch (orientation)
			{
			case ExifInterface.ORIENTATION_NORMAL:
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				tmp = srcWidth;
				srcWidth = srcHeight;
				srcHeight = tmp;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				tmp = srcWidth;
				srcWidth = srcHeight;
				srcHeight = tmp;
				break;
			default:
				Log.w(TAG, "undefined orientation:  " + orientation);
				Log.w(TAG, "Defined values in ExifInterface:");
				Log.w(TAG, ExifInterface.ORIENTATION_FLIP_HORIZONTAL + " = ORIENTATION_FLIP_HORIZONTAL");
				Log.w(TAG, ExifInterface.ORIENTATION_FLIP_VERTICAL + " = ORIENTATION_FLIP_VERTICAL");
				Log.w(TAG, ExifInterface.ORIENTATION_TRANSPOSE + " = ORIENTATION_TRANSPOSE");
				Log.w(TAG, ExifInterface.ORIENTATION_TRANSVERSE + " = ORIENTATION_TRANSVERSE");
				Log.w(TAG, ExifInterface.ORIENTATION_UNDEFINED + " = ORIENTATION_UNDEFINED");
				break;
			}


			Display display = getWindowManager().getDefaultDisplay();
			//Target height is 80% of display height
			int targetHeight = (int) (display.getHeight() * .8);
			double widthFactor = (display.getWidth() * 1f) / (srcWidth * 1f);
			double heightFactor = (targetHeight * 1f) / (srcHeight * 1f);


			int targetWidth = display.getWidth();
			if (heightFactor < widthFactor)
			{
				targetWidth = (int) (srcWidth * heightFactor);
			}

			Bitmap bm =
					ImageUtils.scaleImage(
							Environment.getExternalStorageDirectory().getAbsolutePath() + eventPhoto.getPhotoPath(), targetWidth,
							orientation);

			if (bm == null)
			{
				Util.showToast(this, getString(R.string.erro_ao_ler_imagem));
				finish();
			}

			imageView.setImageBitmap(bm);
			imageView.setId((int) eventPhotoId);
			imageView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// Log.v(TAG, ">>> onClick()");

					imageClicked(v);

					// Log.v(TAG, "<<< onClick()");

				}
			});
			// Log.v(TAG, "<<< onCreate()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will show the next photo
	 * 
	 * @param v
	 *            the Button clicked.
	 */
	public void showNextPhoto(View v)
	{
		try {
			long id = getNextPhotoId();
			showPhoto(id);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will display a photo on the screen
	 * 
	 * @param photoId
	 *            the id of the EventPhoto to display
	 */
	private void showPhoto(long photoId)
	{
		try {
			eventPhotoId = photoId;
			setupButtonsAndTitle();
			setupImage();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will display the previous photo
	 * 
	 * @param v
	 *            The Button that was clicked.
	 */
	public void showPreviousPhoto(View v)
	{
		try {
			long lastId = getPreviousPhotoId();

			showPhoto(lastId);
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
