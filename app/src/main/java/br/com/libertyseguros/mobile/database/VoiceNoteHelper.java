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
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import br.com.libertyseguros.mobile.model.VoiceNote;

/**
 * Provides functionality for creating the VoiceNote table and creating, reading, updating and deleting VoiceNote
 * objects from the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class VoiceNoteHelper
{
    private static final String ID_COLUMN = "_id";

    private static final int ID_INDEX = 0;

    private static final String LENGTH_OF_VOICE_NOTE_COLUMN = "LENGTH_OF_VOICE_NOTE";

    private static final int LENGTH_OF_VOICE_NOTE_INDEX = 2;

    private static final String PATH_TO_VOICE_NOTE_COLUMN = "PATH_TO_VOICE_NOTE";

    private static final int PATH_TO_VOICE_NOTE_INDEX = 1;

    private static final String TAG = VoiceNoteHelper.class.getName();

    public static final String FILE_NAME = "EventVoiceNote.mp4";

    /**
     * The name of the event id column in the Voicenote table
     */
    protected static final String EVENT_ID_COLUMN = "EVENT_ID";

    /**
     * The name of the VoiceNote table
     */
    protected static final String TABLE_NAME = "VOICE_NOTE";

    /**
     * Deletes a row from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the VoiceNote to delete
     */
    public static void delete(Context context, long id)
    {
        //Log.v(TAG, ">>> delete(Context context, long id)");

        String whereClause = ID_COLUMN + "=" + id;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.delete(TABLE_NAME, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< delete(Context context, long id)");
    }

    /**
     * This method will delete all voice activity_notes from the file system, for the event id passed in.
     * 
     * @param id
     *            event id to delete voice activity_notes for.
     */
    public static void removeVoiceNotesForEvent(Context context, long id)
    {
        Iterator<VoiceNote> iterator = getByEvent(context, id).iterator();
        while (iterator.hasNext())
        {
            VoiceNote voiceNote = iterator.next();
            if (voiceNote != null)
            {
                File file = new File(voiceNote.getPathToVoiceNote());
                file.delete();
            }
        }
    }

    /**
     * Gets a VoiceNote from the database
     * 
     * @param context
     *            the context within which to work
     * @return the Voice note or null if no voice note was found
     */
    public static VoiceNote get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        VoiceNote voiceNote = null;

        try
        {
            String whereClause = ID_COLUMN + "=" + id;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // We're retrieving by id, so only have one record to worry about
                cursor.moveToFirst();

                voiceNote = createVoiceNoteObject(context, cursor);
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting VoiceNote with id " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return voiceNote;
    }

    /**
     * Gets a list VoiceNotes from the database which belong to the given event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for voice note
     * @return the Voice activity_notes or an empty arraylist if no voice note was found
     */
    public static ArrayList<VoiceNote> getByEvent(Context context, long eventId)
    {
        //Log.v(TAG, ">>> getByEvent(Context context, long eventId)");

        ArrayList<VoiceNote> voiceNotes = new ArrayList<VoiceNote>();

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId;
            String orderBy = PATH_TO_VOICE_NOTE_COLUMN;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, orderBy);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Build the voiceNote object for each voiceNote returned
                while (cursor.moveToNext())
                {
                    VoiceNote voiceNote = createVoiceNoteObject(context, cursor);
                    voiceNotes.add(voiceNote);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting VoiceNotes for Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getByEvent(Context context, long eventId)");

        return voiceNotes;
    }

    /**
     * Inserts a VoiceNote into the database
     * 
     * @param context
     *            the context within which to work
     * @param voiceNote
     *            the voicenote to add to the database
     * @param eventId
     *            the id of the event to which this voicenote belongs
     * @return the id of the new row in the database
     */
    public static long insert(Context context, VoiceNote voiceNote, Long eventId)
    {
        //Log.v(TAG, ">>> insert(Context context, VoiceNote voiceNote, Long eventId)");

        ContentValues values = buildContentValues(voiceNote, eventId);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, VoiceNote voiceNote, Long eventId)");

        return id;
    }

    /**
     * Updates an existing VoiceNote in the database
     * 
     * @param context
     *            the context within which to work
     * @param voiceNote
     *            the voiceNote data to update
     */
    public static void update(Context context, VoiceNote voiceNote)
    {
        //Log.v(TAG, ">>> update(Context context, VoiceNote voiceNote)");

        ContentValues values = buildContentValues(voiceNote, null);
        String whereClause = ID_COLUMN + "=" + voiceNote.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, VoiceNote voiceNote)");
    }

    /**
     * Converts a VoiceNote to a set of ContentValues
     * 
     * @param voiceNote
     *            the voiceNote to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(VoiceNote voiceNote, Long eventId)
    {
        //Log.v(TAG, ">>> buildContentValues(VoiceNote voiceNote, Long eventId)");

        ContentValues values = new ContentValues();

        if (eventId != null)
        {
            values.put(EVENT_ID_COLUMN, eventId);
        }

        values.put(LENGTH_OF_VOICE_NOTE_COLUMN, voiceNote.getLengthOfVoiceNote());
        values.put(PATH_TO_VOICE_NOTE_COLUMN, voiceNote.getPathToVoiceNote());

        //Log.v(TAG, "<<< buildContentValues(VoiceNote voiceNote, Long eventId)");

        return values;
    }

    /**
     * Builds a VoiceNote object from the results of the database
     * 
     * @param context
     *            the context in which to work
     * @param cursor
     *            the result set to convert
     * @return
     */
    private static VoiceNote createVoiceNoteObject(Context context, Cursor cursor)
    {
        //Log.v(TAG, ">>> createVoiceNoteObject(Context context, Cursor cursor)");

        VoiceNote voiceNote = new VoiceNote();
        voiceNote.setId(cursor.getLong(ID_INDEX));

        voiceNote.setLengthOfVoiceNote(cursor.getInt(LENGTH_OF_VOICE_NOTE_INDEX));
        voiceNote.setPathToVoiceNote(cursor.getString(PATH_TO_VOICE_NOTE_INDEX));

        //Log.v(TAG, "<<< createVoiceNoteObject(Context context, Cursor cursor)");

        return voiceNote;
    }

    /**
     * Provides the query for creating the VoiceNote table
     * 
     * @return the SQL string necessary to create the VoiceNote table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PATH_TO_VOICE_NOTE_COLUMN + " TEXT, " + LENGTH_OF_VOICE_NOTE_COLUMN + " INTEGER, " + EVENT_ID_COLUMN
                + " INTEGER REFERENCES " + EventHelper.TABLE_NAME + "(" + EventHelper.ID_COLUMN + ") NOT NULL)";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private VoiceNoteHelper()
    {
    }
}
