/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import br.com.libertyseguros.mobile.model.EventPhoto;

/**
 * Provides functionality for creating the EventPhoto table and creating, reading, updating and deleting EventPhoto
 * objects from the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class EventPhotoHelper
{
    private static final String FULL_SIZE_IMAGE_PATH_COLUMN = "FULL_SIZE_IMAGE_PATH";

    private static final int FULL_SIZE_IMAGE_PATH_INDEX = 1;

    private static final int ID_INDEX = 0;

    private static final String IMAGE_POSITION_COLUMN = "IMAGE_POSITION";

    private static final int IMAGE_POSITION_INDEX = 2;

    private static final String IMAGE_SECTION_COLUMN = "IMAGE_SECTION";

    private static final int IMAGE_SECTION_INDEX = 3;

    private static final String TAG = EventPhotoHelper.class.getName();

    private static final String THUMBNAIL_IMAGE_PATH_COLUMN = "THUMBNAIL_IMAGE_PATH";

    private static final int THUMBNAIL_IMAGE_PATH_INDEX = 4;

    /**
     * The name of the event id column in the Event Photo table
     */
    protected static final String EVENT_ID_COLUMN = "EVENT_ID";

    /**
     * The name of the id column in the EventPhoto table
     */
    protected static final String ID_COLUMN = "_id";

    /**
     * The name of the Event Photo table
     */
    protected static final String TABLE_NAME = "EVENT_PHOTO";

    /**
     * Converts an EventPhoto to a set of ContentValues
     * 
     * @param eventPhoto
     *            the eventPhoto to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(EventPhoto eventPhoto, Long eventId)
    {
        //Log.v(TAG, ">>> buildContentValues(EventPhoto eventPhoto)");

        ContentValues values = new ContentValues();

        if (eventId != null)
        {
            values.put(EVENT_ID_COLUMN, eventId);
        }

        values.put(IMAGE_POSITION_COLUMN, eventPhoto.getImagePosition());
        values.put(IMAGE_SECTION_COLUMN, eventPhoto.getImageSection());
        values.put(FULL_SIZE_IMAGE_PATH_COLUMN, eventPhoto.getPhotoPath());
        values.put(THUMBNAIL_IMAGE_PATH_COLUMN, eventPhoto.getThumbnailPath());

        //Log.v(TAG, ">>> buildContentValues(EventPhoto eventPhoto)");

        return values;
    }

    /**
     * Converts a result set to an EventPhoto object
     * 
     * @param cursor
     *            the result set to convert
     * @return the convert EventPhoto
     */
    private static EventPhoto buildEventPhoto(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildEventPhoto(Cursor cursor)");

        EventPhoto eventPhoto = new EventPhoto();
        eventPhoto.setId(cursor.getLong(ID_INDEX));
        eventPhoto.setImagePosition(cursor.getInt(IMAGE_POSITION_INDEX));
        eventPhoto.setImageSection(cursor.getInt(IMAGE_SECTION_INDEX));
        eventPhoto.setPhotoPath(cursor.getString(FULL_SIZE_IMAGE_PATH_INDEX));
        eventPhoto.setThumbnailPath(cursor.getString(THUMBNAIL_IMAGE_PATH_INDEX));

        //Log.v(TAG, "<<< buildEventPhoto(Cursor cursor)");

        return eventPhoto;
    }

    /**
     * Deletes an Event Photo from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the row to delete
     */
    public static void delete(Context context, long id, String fileDirectory)
    {
        //Log.v(TAG, ">>> delete(Context context, long id)");

        File imageFile =
            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileDirectory + id + ".jpg");
        if (!imageFile.delete())
        {
            Log.e(TAG, "Deletion of image file failed:  " + imageFile.toString());
        }
        imageFile =
            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileDirectory + id
                + "-thumbnail.png");
        if (!imageFile.delete())
        {
            Log.e(TAG, "Deletion of image file failed:  " + imageFile.toString());
        }
        String whereClause = ID_COLUMN + "=" + id;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.delete(TABLE_NAME, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< delete(Context context, long id)");
    }

    /**
     * Gets the EventPhotowith the given id from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the record to return
     * @return
     */
    public static EventPhoto get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        EventPhoto eventPhoto = null;

        try
        {
            String whereClause = ID_COLUMN + "=" + id;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // we are retrieving by id, so there should only be one record
                cursor.moveToFirst();

                eventPhoto = buildEventPhoto(cursor);
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Event Photo with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return eventPhoto;
    }

    /**
     * Gets a list EventPhotos from the database which belong to the given event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for event photos
     * @return the EventPhotos or an empty ArrayList if no event was found
     */
    public static ArrayList<EventPhoto> getByEvent(Context context, long eventId)
    {
        //Log.v(TAG, ">>> getByEvent(Context context, long eventId)");

        ArrayList<EventPhoto> eventPhotos = new ArrayList<EventPhoto>();

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId;
            String orderBy = IMAGE_SECTION_COLUMN + ", " + IMAGE_POSITION_COLUMN;
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, orderBy);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Build the EventPhoto object for each event photo returned
                while (cursor.moveToNext())
                {
                    EventPhoto eventPhoto = buildEventPhoto(cursor);
                    eventPhotos.add(eventPhoto);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Photos with Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getByEvent(Context context, long eventId)");

        return eventPhotos;
    }

    /**
     * Gets an EventPhoto from the database for a particular Event, Section and Position
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for event photos
     * @param section
     *            the section within to search
     * @return the EventPhotos or an empty ArrayList if no event was found
     */
    public static ArrayList<EventPhoto> getByEventSection(Context context, Long eventId, int section)
    {
        //Log.v(TAG, ">>> getByEventSectionPosition(Context context, Long eventId, int section)");

        ArrayList<EventPhoto> eventPhotos = new ArrayList<EventPhoto>();

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId + " and " + IMAGE_SECTION_COLUMN + " = " + section;

            String orderBy = IMAGE_POSITION_COLUMN;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, orderBy);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Build the EventPhoto object for each event photo returned
                while (cursor.moveToNext())
                {
                    EventPhoto photo = buildEventPhoto(cursor);
                    eventPhotos.add(photo);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Photos with Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getByEventSectionPosition(Context context, Long eventId, int section, int position)");

        return eventPhotos;
    }

    /**
     * Gets an EventPhoto from the database for a particular Event, Section and Position
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for event photos
     * @param section
     *            the section within to search
     * @param position
     *            the position within the section
     * @return an EventPhoto or null if not found. If multiples are found, the first is returned.
     */
    public static EventPhoto getByEventSectionPosition(Context context, Long eventId, int section, int position)
    {
        //Log.v(TAG, ">>> getByEventSectionPosition(Context context, Long eventId, int section, int position)");

        EventPhoto photo = null;

        try
        {
            String whereClause =
                EVENT_ID_COLUMN + "=" + eventId + " and " + IMAGE_SECTION_COLUMN + " = " + section + " and "
                    + IMAGE_POSITION_COLUMN + " = " + position;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Build the EventPhoto object for each event photo returned
                while (cursor.moveToNext())
                {
                    photo = buildEventPhoto(cursor);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Photos with Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getByEventSectionPosition(Context context, Long eventId, int section, int position)");

        return photo;
    }

    /**
     * Provides the query string necessary to create the Event Photo table
     * 
     * @return the SQL string necessary to create the EventPhoto table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FULL_SIZE_IMAGE_PATH_COLUMN + " TEXT, " + IMAGE_POSITION_COLUMN + " INTEGER, " + IMAGE_SECTION_COLUMN
                + " INTEGER, " + THUMBNAIL_IMAGE_PATH_COLUMN + " TEXT, " + EVENT_ID_COLUMN + " INTEGER REFERENCES "
                + EventHelper.TABLE_NAME + "(" + EventHelper.ID_COLUMN + ") NOT NULL)";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Gets the most recent (max) EventPhoto from the database for a particular Event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for event photos
     * @return an EventPhoto or null if not found.
     */
    public static EventPhoto getMaxByEvent(Context context, Long eventId)
    {
        //Log.v(TAG, ">>> getByEventSectionPosition(Context context, Long eventId, int section, int position)");

        EventPhoto photo = null;

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId;

            String orderBy = ID_COLUMN;
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, orderBy);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Build the EventPhoto object for each event photo returned
                while (cursor.moveToNext())
                {
                    photo = buildEventPhoto(cursor);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Photos with Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getByEventSectionPosition(Context context, Long eventId, int section, int position)");

        return photo;
    }

    /**
     * Inserts the EventPhoto into the database
     * 
     * @param context
     *            the context within which to work
     * @param eventPhoto
     *            the eventPhoto to add to the database
     * @return the id of the new row in the database
     */
    public static long insert(Context context, EventPhoto eventPhoto, Long eventId)
    {
        //Log.v(TAG, ">>> insert(Context context, EventPhoto eventPhoto)");

        ContentValues values = buildContentValues(eventPhoto, eventId);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, EventPhoto eventPhoto)");

        return id;
    }

    /**
     * This method will remove the photos from the sdcard
     * 
     * @param context
     *            the application context
     * @param id
     *            the id of the event
     */
    public static void removePhotosForEvent(Context context, long id)
    {
        Iterator<EventPhoto> iterator = getByEvent(context, id).iterator();
        while (iterator.hasNext())
        {
            EventPhoto eventPhoto = iterator.next();
            File imageFile =
                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + eventPhoto.getPhotoPath());
            imageFile.delete();
        }
    }

    /**
     * Updates an existing EventPhoto in the database
     * 
     * @param context
     *            the context within which to work
     * @param eventPhoto
     *            the eventPhoto data to update
     */
    public static void update(Context context, EventPhoto eventPhoto)
    {
        //Log.v(TAG, ">>> update(Context context, EventPhoto eventPhoto)");

        ContentValues values = buildContentValues(eventPhoto, null);

        String whereClause = ID_COLUMN + "=" + eventPhoto.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, EventPhoto eventPhoto)");
    }

    /**
     * Private constructor to prevent instantiation
     */
    private EventPhotoHelper()
    {

    }
}
