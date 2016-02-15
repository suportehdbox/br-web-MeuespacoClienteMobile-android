/*
 * Copyright (c) 2011, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author N0053575 (Heidi Sturm)
 */
public final class FileUtils
{
    /**
     * The logging tag
     */
    private static final String TAG = FileUtils.class.getName();

    // This filter only returns directories
    private static FileFilter fileOnlyFilter = new FileFilter()
    {
        public boolean accept(File file)
        {
            return !file.isDirectory();
        }
    };

    /**
     * This method will return the number of bytes in this directory. It calculates this by summing up the bytes in each
     * file in the directory. Sub-directories are not included.
     * 
     * @param directory
     * @return
     */
    public static long bytesInDirectory(String directory)
    {
        long bytes = 0;
        File dir = new File(directory);
        File[] files = dir.listFiles(fileOnlyFilter);
        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                bytes += files[i].length();
            }
        }
        return bytes;
    }

    /**
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destFile)
    {
        // Log.v(TAG, ">>> copyFile(File sourceFile, File destFile)");

        if (!destFile.exists())
        {
            try
            {
                destFile.createNewFile();
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage());
            }
        }

        FileChannel source = null;
        FileChannel destination = null;
        try
        {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        catch (FileNotFoundException e)
        {
            Log.e(TAG, e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
        finally
        {

            try
            {
                if (source != null)
                {
                    source.close();
                }
                if (destination != null)
                {
                    destination.close();
                }
            }
            catch (IOException e)
            {

                Log.e(TAG, e.getMessage());
            }

        }
        // Log.v(TAG, "<<< copyFile(File sourceFile, File destFile)");
    }

    /**
     * This method will delete all files in the directory
     * 
     * @param directoryName
     *            directory to remove files from
     */
    public static void deleteAllFilesInDirectory(String directoryName)
    {
        File directory = new File(directoryName);
        // Get all files in directory
        File[] files = directory.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                // Delete each file
                file.delete();
            }
        }
    }

    /**
     * This method will check to see if a path exists and is writeable.
     * 
     * @param path
     *            path to check
     * @return true if exists and is writeable, else false
     */
    public static boolean dirExistsWriteable(String path)
    {
        // Make sure cache directory exists:
        File externalDirectory = new File(path);
        if (!externalDirectory.exists())
        {
            if (!externalDirectory.mkdirs())
            {
                Log.i(TAG, "Could not mkdirs for activity_pictures");
            }
        }
        if (externalDirectory.exists() && externalDirectory.canWrite())
        {
            return true;
        }
        return false;
    }

    /**
     * This method returns the basename of the string passed in
     * 
     * @param string
     *            File to get basename of
     * @return basename of file
     */
    public static String getBaseName(String string)
    {
        String basename = string;
        if (string == null)
        {
            basename = null;
        }
        else
        {
            int lastSlash = string.lastIndexOf("/");
            if (lastSlash >= 0)
            {
                basename = string.substring(lastSlash + 1);
            }
        }
        return basename;
    }

    /**
     * @param resolver
     * @param uri
     * @return galleryFile
     */
    public static File getFileFromUri(ContentResolver resolver, Uri uri)
    {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String filePath = null;

        Cursor cursor = resolver.query(uri, filePathColumn, null, null, null);
        if (cursor == null)
        {
            // We have a file uri instead of a content uri (usually means a image was selected from the file browser,
            // instead of the gallery
            filePath = uri.getPath();
        }
        else
        {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        File galleryFile = new File(filePath);
        return galleryFile;
    }

    /**
     * returns all the files in this directory as a File array. Does not include sub-directories.
     * 
     * @param directory
     * @return
     */
    public static File[] getPathsInDirectory(String directory)
    {
        File dir = new File(directory);
        return dir.listFiles(fileOnlyFilter);
    }

    /**
     * Tests to see if this uri is an image mime type
     * 
     * @param contentResolver
     *            the content resolver
     * @param uri
     *            uri to test
     * @return true if image, else false
     */
    public static boolean isValidImageUri(ContentResolver contentResolver, Uri uri)
    {
        boolean returnVal = false;

        String mimeType = contentResolver.getType(uri);

        if (mimeType != null)
        {
            if (mimeType.startsWith("image/"))
            {
                returnVal = true;
            }
        }
        else
        {
            // Some content providers to not implement the getType method. In this case we are just going
            // to try to create a bitmap from the file.
            File file = getFileFromUri(contentResolver, uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 32;
            Bitmap bm = BitmapFactory.decodeFile(file.toString(), options);
            if (bm != null)
            {
                returnVal = true;
            }
        }
        return returnVal;
    }

    /**
     * private constructor to prevent instantiation
     */
    private FileUtils()
    {
        super();
    }
}
