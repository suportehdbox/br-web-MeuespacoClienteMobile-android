package br.com.libertyseguros.mobile.libray;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class LoadFile {

	/**
	 * Save String in SharedPreferences
	 * @param key
	 * @param info
	 * @param prefName
	 * @param mContext
	 * @return
	 */
	public boolean savePref(String key, String info, String prefName,
			Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(key, info);
		return editor.commit();
	}
	/**
	 * Save Booelean in SharedPreferences
	 * @param key
	 * @param info
	 * @param prefName
	 * @param mContext
	 * @return
	 */
	public boolean savePref(String key, Boolean info, String prefName,
							Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putBoolean(key, info);
		return editor.commit();
	}

	/**
	 * Loading String in SharedPreferences
	 * @param prefName
	 * @param mContext
	 * @param key
	 * @return
	 */
	public String loadPref(String prefName, Context mContext, String key) {
		SharedPreferences prefs = mContext.getSharedPreferences(prefName, 0);

		return prefs.getString(key, null);
	}

	/**
	 * Loading String in SharedPreferences
	 * @param prefName
	 * @param mContext
	 * @param key
	 * @return
	 */
	public Boolean loadBoolPref(String prefName, Context mContext, String key) {
		SharedPreferences prefs = mContext.getSharedPreferences(prefName, 0);

		return prefs.getBoolean(key, false);
	}




	/**
	 * Image Exists
	 * @param context
	 * @param image
	 */
	public boolean isImage(Context context, String image) {
		boolean isImage = false;

		String teste[] = image.split("\\.");

		int id = context.getResources().getIdentifier(image.split("\\.")[0], "drawable", context.getPackageName());

		if(id == 0){
			String filepath = "";

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				filepath = context.getFilesDir().getAbsolutePath();
			}else {
				filepath = context.getFilesDir().getPath();
			}

			filepath = filepath + "/" + image;

			File imagefile = new File(filepath);

			if(imagefile.exists()){
				isImage = true;
				//Log.i(Config.TAG, "Image " + image + " exists sdcard ");
			} else {
				//Log.i(Config.TAG, "Image " + image + " not exists ");
			}
		} else {
			isImage = true;

			//Log.i(Config.TAG, "Image " + image + " exists drawable ");
		}

		return isImage;
	}

	/**
	 * Get Image
	 * @param context
	 * @param image
	 * @return
	 */
	public Drawable getImage(Context context, String image){
		Drawable drawable = null;

		try {
			if (drawable == null) {
				String filepath = "";

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					filepath = context.getFilesDir().getAbsolutePath();
				}else {
					filepath = context.getFilesDir().getPath();
				}

				filepath = filepath + "/" + image;

				File imagefile = new File(filepath);

				FileInputStream fis = null;

				try {
					fis = new FileInputStream(imagefile);
				} catch (FileNotFoundException e) {
				}

				Bitmap bm = BitmapFactory.decodeStream(fis);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				drawable = new BitmapDrawable(context.getResources(), bm);

			}

		} catch(Exception ex){
			//Log.i(Config.TAG, "Image: " + image + " não encontrada no sdcard: ");
		}


		return  drawable;

	}


	/**
	 * Store Image in IO
	 * @param image - bitmap image
	 * @param fileName - name of file
	 * @param quality - quality of image
	 * @param context - current context
	 * @return
	 */
	public File storeImage(Bitmap image, String fileName,
						   int quality, Context context) {

		File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), fileName);

		File tempFile = null;

		if (pictureFile != null) {
			try {

				tempFile = new File(Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES),
						fileName);


				if(tempFile.exists()){
					tempFile.delete();
				}

				FileOutputStream fosTemp = new FileOutputStream(tempFile);
				image.compress(Bitmap.CompressFormat.JPEG, quality,
						fosTemp);

				fosTemp.close();
			} catch (FileNotFoundException e) {
				//Log.d(Config.TAG, "Image File Not Found: " + e.getMessage());
			} catch (IOException e) {
				//Log.d(Config.TAG, "Error on store image file: " + e.getMessage());
			} catch (Exception e) {
				//Log.d(Config.TAG, "Erro on store image file: " + e.getMessage());
			}
		}

		return tempFile;
	}

	/**
	 * Store Image in IO
	 * @param image - bitmap image
	 * @param fileName - name of file
	 * @param quality - quality of image
	 * @return instance of File
	 */
	public File storeImage(Bitmap image, String fileName, int quality) {

		File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), fileName);

		File tempFile = null;

		if (pictureFile != null) {
			try {

//                File pathFile = Environment.getExternalStorageDirectory();

				tempFile = new File(Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES),
						fileName);


				if(tempFile.exists()){
					if(!tempFile.delete()){
						//Log.v((Config.TAG,"Error deleting photo");
					}
				}

				FileOutputStream fosTemp = new FileOutputStream(tempFile);
				image.compress(Bitmap.CompressFormat.JPEG, quality,
						fosTemp);

				fosTemp.close();
			} catch (FileNotFoundException e) {
				//Log.d(Config.TAG, "Image File Not Found: " + e.getMessage());
			} catch (IOException e) {
				//Log.d(Config.TAG, "Error on store image file: " + e.getMessage());
			} catch (Exception e) {
				//Log.d(Config.TAG, "Erro on store image file: " + e.getMessage());
			}
		}

		return tempFile;
	}
	/**
	 * Load Image from IO
	 *
	 * @param imageUri - URi of image
	 * @param context - current context
	 * @param width - width of image
	 * @param height - height of image
	 * @return Bitmap of image
	 */
	public Bitmap loadImage(Uri imageUri, Context context, int width, int height) {

		Bitmap bitmap = null;

		try {

			InputStream stream = new BufferedInputStream(context.getContentResolver().openInputStream(imageUri));

			if(stream != null) {
				stream.mark(stream.available());
				// First decode with inJustDecodeBounds=true to check dimensions
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				bitmap = BitmapFactory.decodeStream(stream, null, options);

				// Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options, width, height);

				// Decode bitmap with inSampleSize set
				options.inJustDecodeBounds = false;
				stream.reset();
				bitmap = BitmapFactory.decodeStream(stream, null, options);

				stream.close();

			}

		} catch (FileNotFoundException e) {
			//Log.d(Config.TAG, "Image File Not Found: " + e.getMessage());
		} catch (IOException e) {
			//Log.d(Config.TAG, "Error loading image file 1: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			//Log.d(Config.TAG, "Erro loading image file 2: " + e.getMessage());
		}

		return bitmap;
	}

	private static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}


}
