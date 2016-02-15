/*
 * Copyright (c) 2011, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.common.util.DeviceUtils;
import br.com.libertyseguros.mobile.common.util.FileUtils;
import br.com.libertyseguros.mobile.common.util.ImageUtils;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventPhotoHelper;
import br.com.libertyseguros.mobile.model.EventPhoto;

/**
 * @author N0053575 (Heidi Sturm)
 * 
 * Update 08/08/2013 @author Evandro
 */
public class PicturesActivity extends LibertyMobileApp implements OnClickListener, DialogInterface.OnClickListener
{
	/**
	 * The logging tag
	 */
	private static final String TAG = PicturesActivity.class.getName();

	public static final String EXTRA_NAME_INITIAL_VIEW = "com.lmig.pm.internet.mobile.android.libertymutual.INITIAL";

	private static final int VEHICLE = 0;

	private static final int OTHER_VEHICLE = 1;

	private static final int SURROUNDINGS = 2;

	private static final int DOCUMENTS = 3;

	private static int numberOfGalleries = 4;

	private static final int IMPORT_PICTURE_INTENT = 0;

	private static final int CAMERA_INTENT = 1;

	private static final int PHOTO_FROM_CAMERA = 0;

	private static final int PHOTO_FROM_GALLERY = 1;

	private static final int THUMB_SIZE = 72;

	private static float scale;

	private AlertDialog photoImportAlert;

	private ArrayList<LinearLayout> imageLayouts;

	private int currentGalleryNumber;

	private EventPhoto eventPhoto;

	private View vehiclePhotoButton;

	private View otherVehiclePhotoButton;

	private View surroundingPhotoButton;

	private View documentsPhotoButton;

	/**
	 * This method handles the clicking of the add photo button
	 * 
	 * @param v
	 *            The view that was clicked
	 */
	public void addDocumentPhoto(View v)
	{
		try {
			addPhoto(DOCUMENTS);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method handles the clicking of the add photo button
	 * 
	 * @param v
	 *            The view that was clicked
	 */
	public void addOtherVehiclePhoto(View v)
	{
		try {
			addPhoto(OTHER_VEHICLE);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will add a photo to a particular gallery
	 * 
	 * @param galleryType
	 *            the gallery to add the photo too
	 */
	public void addPhoto(int galleryType)
	{
		try {
			currentGalleryNumber = galleryType;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.aviso));
			builder.setSingleChoiceItems(getPictureOptions(), -1, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int item)
				{
					photoImportAlert.dismiss();
					switch (item)
					{
					case PHOTO_FROM_CAMERA:
						takePictureForGallery();
						break;
					case PHOTO_FROM_GALLERY:
						copyPhotoFromGallery();
						break;
					default:
						break;
					}
				}
			});
			photoImportAlert = builder.create();
			photoImportAlert.show();
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method handles the clicking of the add photo button
	 * 
	 * @param v
	 *            The view that was clicked
	 */
	public void addSurroundingsPhoto(View v)
	{
		try {
			addPhoto(SURROUNDINGS);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method handles the clicking of the add photo button
	 * 
	 * @param v
	 *            The view that was clicked
	 */
	public void addVehiclePhoto(View v)
	{
		try {
			addPhoto(VEHICLE);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will start the intent to select a photo from the gallery
	 */
	private void copyPhotoFromGallery()
	{
		try {
			Intent intent = new Intent();
			intent.setType("image/jpeg");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, getString(R.string.aviso)), IMPORT_PICTURE_INTENT);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	// << EPO
	//    /**
	//     * Sets the addphoto button to disabled
	//     * 
	//     * @param galleryType
	//     *            The section
	//     */
	//    private void disableAddPhotoButton(final int galleryType)
	//    {
	//        switch (galleryType)
	//        {
	//            case VEHICLE:
	//                vehiclePhotoButton.setBackgroundResource(R.drawable.add_photo_btn_disabled);
	//                vehiclePhotoButton.setClickable(false);
	//                break;
	//            case OTHER_VEHICLE:
	//                otherVehiclePhotoButton.setBackgroundResource(R.drawable.add_photo_btn_disabled);
	//                otherVehiclePhotoButton.setClickable(false);
	//                break;
	//            case SURROUNDINGS:
	//                surroundingPhotoButton.setBackgroundResource(R.drawable.add_photo_btn_disabled);
	//                surroundingPhotoButton.setClickable(false);
	//                break;
	//            case DOCUMENTS:
	//                documentsPhotoButton.setBackgroundResource(R.drawable.add_photo_btn_disabled);
	//                documentsPhotoButton.setClickable(false);
	//                break;
	//            default:
	//                break;
	//        }
	//    }
	// >>

	/**
	 * Sets the addphoto button to visible.
	 * 
	 * @param galleryType
	 *            The section
	 */
	private void displayAddPhotoButton(final int galleryType)
	{
		try {
			switch (galleryType)
			{
			case VEHICLE:
				vehiclePhotoButton.setVisibility(View.VISIBLE);
				break;
			case OTHER_VEHICLE:
				otherVehiclePhotoButton.setVisibility(View.VISIBLE);
				break;
			case SURROUNDINGS:
				surroundingPhotoButton.setVisibility(View.VISIBLE);
				break;
			case DOCUMENTS:
				documentsPhotoButton.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will handle the click of a gallery item
	 * 
	 * @param v
	 *            view
	 */
	private void galleryItemClick(View v)
	{
		try {
			Intent intent = new Intent(getApplicationContext(), ViewPhotosActivity.class);
			intent.putExtra(Constants.EVENT_PHOTO_ID, v.getId());
			startActivity(intent);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * this method gets a file on the internal storage to save an image to.
	 * 
	 * @return the file to save the image to.
	 */
	private File getCaptureDirectory()
	{
		try {
			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state))
			{
				// API 8 shortcut
				// /Android/data/<package_name>/files/
				String path =
						Environment.getExternalStorageDirectory().getAbsolutePath() + getApplicationSpecificFileDirectory();
				return new File(path);
			}
			return null;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	/**
	 * This method will place a thumbnail with rounded corners of the image in the layout provided
	 * 
	 * @param layout
	 *            The layout to add the thumbnail to
	 * @param photo
	 *            The image to create the thumbnail from.
	 */
	private void getImageThumbnail(LinearLayout layout, EventPhoto photo)
	{
		try {
			Bitmap roundedBitmap = null;
			if (photo.getThumbnailPath() != null)
			{
				roundedBitmap =
						BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()
								+ photo.getThumbnailPath());
			}
			else
			{
				roundedBitmap = ImageUtils.createThumbnailImage(photo, THUMB_SIZE, scale, this);
				if (roundedBitmap == null)
				{
					Util.showToast(this, getString(R.string.erro_ao_ler_imagem));
					finish();
				}
				else
				{
					String thumbnailPath = getApplicationSpecificFileDirectory() + photo.getId() + "-thumbnail.png";
					try
					{
						ImageUtils.saveBitmapToFile(roundedBitmap, Environment.getExternalStorageDirectory()
								.getAbsolutePath() + thumbnailPath, Bitmap.CompressFormat.PNG);
						photo.setThumbnailPath(thumbnailPath);
						EventPhotoHelper.update(getApplicationContext(), photo);
					}
					catch (FileNotFoundException e)
					{
						Util.showException(null, e);
					}
				}
			}

			if (roundedBitmap != null)
			{
				ImageView i = new ImageView(getApplicationContext());
				i.setId(photo.getId().intValue());
				i.setImageBitmap(roundedBitmap);
				int thumbSize = (int) (THUMB_SIZE * scale);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(thumbSize, thumbSize);
				int sideMargins = (int) (4 * scale);
				int topBottomMargins = (int) (2 * scale);
				lp.setMargins(sideMargins, topBottomMargins, sideMargins, topBottomMargins);
				i.setLayoutParams(lp);
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				i.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						// Log.v(TAG, ">>> onClick()");

						galleryItemClick(v);

						// Log.v(TAG, "<<< onClick()");
					}
				});
				layout.addView(i);
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Convenience method for getting the list of picture options used when the user selects to add a photo.
	 * 
	 * @return the valid options for adding a photo
	 */
	private CharSequence[] getPictureOptions()
	{
		try {
			// Log.v(TAG, ">>> getPictureOptions()");

			CharSequence[] getPictureOptions =
					new CharSequence[] {getString(R.string.take_pictures), getString(R.string.choose_from_library)};

			// Log.v(TAG, "<<< getPictureOptions()");

			return getPictureOptions;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	/**
	 * This method will store the picture taken from the camera into the sdcard.
	 * 
	 * @param resultCode
	 *            code returned from the camera intent
	 */
	private void handleCameraIntent(int resultCode)
	{
		try {
			// Log.v(TAG, ">>> handleCameraIntent(int resultCode)");
			if (resultCode != RESULT_CANCELED)
			{
				if (eventPhoto != null)
				{
					File photoFile =
							new File(Environment.getExternalStorageDirectory().getAbsolutePath()
									+ getApplicationSpecificFileDirectory() + eventPhoto.getId() + ".jpg");
					if (photoFile.exists())
					{
						eventPhoto.setPhotoPath(getApplicationSpecificFileDirectory() + eventPhoto.getId() + ".jpg");
						EventPhotoHelper.update(getApplicationContext(), eventPhoto);
						ArrayList<EventPhoto> photoList = getCurrentEvent().getPhotos();
						if (photoList == null)
						{
							photoList = new ArrayList<EventPhoto>();
						}
						photoList.add(eventPhoto);
						getCurrentEvent().setPhotos(photoList);
					}
					else
					{
						// Camera Intent was cancelled.
						EventPhotoHelper.delete(getApplicationContext(), eventPhoto.getId(),
								getApplicationSpecificFileDirectory());
					}
				}
				else
				{
					Util.showToast(PicturesActivity.this, getString(R.string.camera_error));
					Log.e(TAG, "eventPhoto was null.  Not sure how that is possible, as we instantiate a new one right before we create the camera intent:  "
									+ eventPhoto);
					// This seems like a bug that I have only seen on the Droid X. The workaround is to get the most recent
					// event photo and delete it.
					eventPhoto = EventPhotoHelper.getMaxByEvent(getApplicationContext(), getCurrentEvent().getId());
					Log.e(TAG, "Deleting Photo:  " + eventPhoto.getId());
					EventPhotoHelper.delete(getApplicationContext(), eventPhoto.getId(), getApplicationSpecificFileDirectory());
				}
			}
			else
			{
				if (eventPhoto != null)
				{
					EventPhotoHelper.delete(getApplicationContext(), eventPhoto.getId(),
							getApplicationSpecificFileDirectory());
				}
			}

			// Log.v(TAG, "<<< handleCameraIntent(int resultCode)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will copy a photo from the gallery into the application
	 * 
	 * @param resultCode
	 *            code from the select image from gallery intent
	 * @param data
	 *            the Intent that contains the selection.
	 */
	private void handleImportIntent(int resultCode, Intent data)
	{
		try {
			if (resultCode != RESULT_CANCELED)
			{
				if (FileUtils.isValidImageUri(getContentResolver(), data.getData()))
				{
					eventPhoto = new EventPhoto();
					eventPhoto.setImagePosition(getCurrentEvent().getCountOfPhotosInSection(currentGalleryNumber));
					eventPhoto.setImageSection(currentGalleryNumber);
					long id = EventPhotoHelper.insert(getApplicationContext(), eventPhoto, getCurrentEvent().getId());
					eventPhoto.setId(id);

					File galleryFile = FileUtils.getFileFromUri(getContentResolver(), data.getData());
					File destinationFile =
							new File(Environment.getExternalStorageDirectory().getAbsolutePath()
									+ getApplicationSpecificFileDirectory() + eventPhoto.getId() + ".jpg");

					eventPhoto.setPhotoPath(getApplicationSpecificFileDirectory() + eventPhoto.getId() + ".jpg");
					EventPhotoHelper.update(getApplicationContext(), eventPhoto);
					ArrayList<EventPhoto> photoList = getCurrentEvent().getPhotos();
					if (photoList == null)
					{
						photoList = new ArrayList<EventPhoto>();
					}
					photoList.add(eventPhoto);
					getCurrentEvent().setPhotos(photoList);
					FileUtils.copyFile(galleryFile, destinationFile);
				}
				else
				{
					Util.showToast(PicturesActivity.this, "Arquivo selecionado não é um arquivo de imagem válido.");
				}
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will load any existing activity_pictures into the galleries
	 */
	private void initGalleries()
	{
		try {
			for (int i = 0; i < numberOfGalleries; i++)
			{
				initGallery(imageLayouts.get(i), i);
			}

			// Log.v(TAG, "Galleries took " + timeToLoadGalleries + " milliseconds to load.");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will initialize a gallery
	 * 
	 * @param layout
	 *            The layout that the image views will get added to
	 * @param galleryType
	 */
	private void initGallery(LinearLayout layout, final int galleryType)
	{
		try {
			ArrayList<EventPhoto> photos = EventPhotoHelper.getByEventSection(getApplicationContext(), getCurrentEvent().getId(), galleryType);
			Iterator<EventPhoto> photoIterator = photos.iterator();

			layout.removeAllViews();

			int countOfPhotos = 0;

			while (photoIterator.hasNext())
			{
				countOfPhotos++;
				EventPhoto photo = photoIterator.next();
				getImageThumbnail(layout, photo);
			}

			setAddPhotoButtonVisibility(galleryType, countOfPhotos);
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode)
			{
				case CAMERA_INTENT:
					handleCameraIntent(resultCode);
					break;
				case IMPORT_PICTURE_INTENT:
					handleImportIntent(resultCode, data);
					break;
				default:
					break;
			}
			setResult(RESULT_OK);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see DialogInterface.OnClickListener#onClick(DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface arg0, int arg1)
	{

	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		try {
			// Log.v(TAG, ">>> onClick()");

			Log.i(TAG, "View was clicked: " + v);

			// Log.v(TAG, "<<< onClick()");
		} catch (Exception e) {
			Util.showException(this, e);
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
			// Log.v(TAG, ">>> onCreate()");

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_pictures);

			imageLayouts = new ArrayList<LinearLayout>();
			LinearLayout ll = (LinearLayout) findViewById(R.id.vehicle_layout);
			imageLayouts.add(ll);
			ll = (LinearLayout) findViewById(R.id.other_vehicle_layout);
			imageLayouts.add(ll);
			ll = (LinearLayout) findViewById(R.id.surroundings_layout);
			imageLayouts.add(ll);
			ll = (LinearLayout) findViewById(R.id.documents_layout);
			imageLayouts.add(ll);

			setUpNavigationBarTitleOnly(getString(R.string.take_pictures_title));

			vehiclePhotoButton 		= (View) findViewById(R.id.add_vehicle_photo_btn);
			otherVehiclePhotoButton = (View) findViewById(R.id.add_other_vehicle_photo_btn);
			surroundingPhotoButton	= (View) findViewById(R.id.add_surroundings_photo_btn);
			documentsPhotoButton 	= (View) findViewById(R.id.add_documents_photo_btn);

			scale = getApplicationContext().getResources().getDisplayMetrics().density;

			numberOfGalleries = 4;

			eventPhoto = (EventPhoto) getLastNonConfigurationInstance();
			// Log.v(TAG, "<<< onCreate()");
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume(){
		
		try {
			super.onResume();

			ArrayList<EventPhoto> eventPhotos = EventPhotoHelper.getByEvent(getApplicationContext(), getCurrentEvent().getId());
			getCurrentEvent().setPhotos(eventPhotos);
			
			initGalleries();
			
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * @return
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return eventPhoto;
	}

	/**
	 * Sets the addphoto button to gone.
	 * 
	 * @param galleryType
	 *            The section
	 */
	private void removeAddPhotoButton(final int galleryType)
	{
		try {
			switch (galleryType)
			{
			case VEHICLE:
				vehiclePhotoButton.setVisibility(View.GONE);
				break;
			case OTHER_VEHICLE:
				otherVehiclePhotoButton.setVisibility(View.GONE);
				break;
			case SURROUNDINGS:
				surroundingPhotoButton.setVisibility(View.GONE);
				break;
			case DOCUMENTS:
				documentsPhotoButton.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will set the add photo button to visible if there are less than 4 photos in the section and gone if
	 * there are more than 3. If there are less than 4 and the claim is submitted, it will use the disabled graphic.
	 * 
	 * @param galleryType
	 *            which section are we looking at.
	 * @param countOfPhotos
	 *            How many photos this section has
	 */
	private void setAddPhotoButtonVisibility(final int galleryType, int countOfPhotos){
		
		try {
			String status = getCurrentEvent().getEventStatus();

			if (countOfPhotos > 3 || Constants.EVENT_STATUS_SUBMITTED.equals(status)){
				removeAddPhotoButton(galleryType);
			}else{
				displayAddPhotoButton(galleryType);
			}
			
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will take a picture for the current gallery
	 */
	private void takePictureForGallery()
	{
		try {
			if (DeviceUtils.hasCamera(this))
			{
				File imageDirectory = getCaptureDirectory();
				if (imageDirectory == null)
				{
					Util.showToast(PicturesActivity.this, getString(R.string.camera_no_external_media));
				}
				else
				{
					eventPhoto = new EventPhoto();
					eventPhoto.setImagePosition(getCurrentEvent().getCountOfPhotosInSection(currentGalleryNumber));
					eventPhoto.setImageSection(currentGalleryNumber);
					long id = EventPhotoHelper.insert(getApplicationContext(), eventPhoto, getCurrentEvent().getId());
					eventPhoto.setId(id);
					Log.i(TAG,
							"Adicionando foto #" + eventPhoto.getImagePosition() + " to section " + eventPhoto.getImageSection());
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(imageDirectory.toString() + "/" + id + ".jpg")));
					startActivityForResult(intent, CAMERA_INTENT);
				}
			}
			else
			{
				displayInfoAlert(this, 
						getString(R.string.aviso), 
						getString(R.string.no_camera_text),
						getString(R.string.btn_ok), 
						PicturesActivity.this);
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
