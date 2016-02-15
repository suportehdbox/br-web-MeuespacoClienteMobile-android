/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.Witness;

/**
 * Provides functionality for creating the Witness table and creating, reading, updating and deleting Witness objects
 * from the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class WitnessHelper
{
    private static final String CONTACT_ID_COLUMN = "CONTACT_ID";

    private static final int CONTACT_ID_INDEX = 1;

    private static final String ID_COLUMN = "_id";

    private static final int ID_INDEX = 0;

    private static final String TAG = WitnessHelper.class.getName();

    /**
     * The name of the event id column in the Witness table
     */
    protected static final String EVENT_ID_COLUMN = "EVENT_ID";

    /**
     * The name of the Witness table
     */
    protected static final String TABLE_NAME = "WITNESS";

    /**
     * Converts a Witness to a set of ContentValues
     * 
     * @param witness
     *            the activity_witness to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(Witness witness, Long eventId)
    {
        //Log.v(TAG, ">>> buildContentValues(Witness activity_witness, Long eventId)");

        ContentValues values = new ContentValues();

        if (eventId != null)
        {
            values.put(EVENT_ID_COLUMN, eventId);
        }

        // Build the contact part of the activity_witness
        Contact contact = witness.getContact();
        if (contact != null)
        {
            values.put(CONTACT_ID_COLUMN, contact.getId());
        }

        //Log.v(TAG, "<<< buildContentValues(Witness activity_witness, Long eventId)");

        return values;
    }

    /**
     * Converts a result set to a Witness object
     * 
     * @param cursor
     *            the result set to convert
     * @return the converted Witness
     */
    private static Witness buildWitness(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildWitness(Cursor cursor)");

        Witness witness = new Witness();
        witness.setId(cursor.getLong(ID_INDEX));

        // Get the associated contact
        Contact contact = new Contact();
        contact.setId(cursor.getLong(CONTACT_ID_INDEX));
        witness.setContact(contact);

        //Log.v(TAG, "<<< buildWitness(Cursor cursor)");

        return witness;
    }

    /**
     * Builds a Witness object from the results of the database
     * 
     * @param context
     *            the context in which to work
     * @param cursor
     *            the result set to convert
     * @return
     */
    private static Witness createWitnessObject(Context context, Cursor cursor)
    {
        //Log.v(TAG, ">>> createWitnessObject(Context context, Cursor cursor)");

        Witness witness;
        // build the activity_witness specific contact info
        witness = buildWitness(cursor);

        // now get the contact for the activity_witness
        Contact contact = witness.getContact();
        Long contactId = contact.getId();
        if (contactId != null && contactId > 0)
        {
            contact = ContactHelper.get(context, contactId);
            witness.setContact(contact);
        }

        //Log.v(TAG, "<<< createWitnessObject(Context context, Cursor cursor)");

        return witness;
    }

    /**
     * Deletes a row from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the Witness to delete
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
     * Gets a Witness from the database
     * 
     * @param context
     *            the context within which to work
     * @return the Witness or null if no activity_witness was found
     */
    public static Witness get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        Witness witness = null;

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

                witness = createWitnessObject(context, cursor);
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Witness with id " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return witness;
    }

    /**
     * Gets a list Witnesses from the database which belong to the given event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for witnesses
     * @return the Witnesses or an empty array if no activity_witness was found
     */
    public static ArrayList<Witness> getByEvent(Context context, long eventId)
    {
        //Log.v(TAG, ">>> getByEvent(Context context, long eventId)");

        ArrayList<Witness> witnesses = new ArrayList<Witness>();

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Build the activity_witness object for each activity_witness returned
                while (cursor.moveToNext())
                {
                    Witness witness = createWitnessObject(context, cursor);
                    witnesses.add(witness);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Witnesses for Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getByEvent(Context context, long eventId)");

        return witnesses;
    }

    /**
     * Gets the Contact information for the list of Witnesses which belong to the given event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for witnesses
     * @return the Contacts or an empty array if no activity_witness was found
     */
    public static ArrayList<Contact> getContactByEvent(Context context, long eventId)
    {
        //Log.v(TAG, ">>> getContactByEvent(Context context, long eventId)");

        ArrayList<Contact> contacts = new ArrayList<Contact>();

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            String[] columns = new String[] {ID_COLUMN, CONTACT_ID_COLUMN};
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, columns, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Get the contact info for each driver returned
                while (cursor.moveToNext())
                {
                    Contact contact = new Contact();

                    contact = ContactHelper.get(context, cursor.getLong(1));

                    contact.setTypeId(cursor.getLong(0));

                    contacts.add(contact);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Witness Contact by Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getContactByEvent(Context context, long eventId)");

        return contacts;
    }

    /**
     * Provides the query for creating the Witness table
     * 
     * @return the SQL string necessary to create the Witness table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        										+ CONTACT_ID_COLUMN + " INTEGER REFERENCES " 
        										+ ContactHelper.TABLE_NAME + "(" + ContactHelper.ID_COLUMN + "), " 
        										+ EVENT_ID_COLUMN + " INTEGER REFERENCES " 
        										+ EventHelper.TABLE_NAME + "(" + EventHelper.ID_COLUMN + ") NOT NULL)";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Provides the query to create a trigger forcing the deletion of the child Contact associated with a Witness when
     * the Witness is deleted
     * 
     * @return the SQL string necessary to create the triggers for the Witness table
     */
    protected static String getCreateTriggersSQL()
    {
        //Log.v(TAG, ">>> getCreateTriggersSQL()");

        String createTriggerSQL =
            "CREATE TRIGGER DELETE_WITNESS BEFORE DELETE ON " + TABLE_NAME + " FOR EACH ROW BEGIN DELETE FROM "
                + ContactHelper.TABLE_NAME + " WHERE " + ContactHelper.ID_COLUMN + " = old." + CONTACT_ID_COLUMN
                + "; END";

        //Log.v(TAG, "<<< getCreateTriggersSQL()");

        return createTriggerSQL;
    }

    /**
     * Inserts a Witness into the database
     * 
     * @param context
     *            the context within which to work
     * @param witness
     *            the activity_witness to add to the database
     * @param eventId
     *            the id of the event to which this activity_witness belongs
     * @return the id of the new row in the database
     */
    public static long insert(Context context, Witness witness, Long eventId)
    {
        //Log.v(TAG, ">>> insert(Context context, Witness activity_witness, Long eventId)");

        // First insert the associated contact
        Contact contact = witness.getContact();
        if (contact != null)
        {
            long id = ContactHelper.insert(context, contact);
            contact.setId(id);
        }

        // Then insert the activity_witness
        ContentValues values = buildContentValues(witness, eventId);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, Witness activity_witness, Long eventId)");

        return id;
    }

    /**
     * Updates an existing Witness in the database
     * 
     * @param context
     *            the context within which to work
     * @param witness
     *            the activity_witness data to update
     */
    public static void update(Context context, Witness witness)
    {
        //Log.v(TAG, ">>> update(Context context, Witness activity_witness)");

        // First insert or update the associated contact
        Contact contact = witness.getContact();
        if (contact != null)
        {
            Long contactId = contact.getId();
            if (contactId != null && contactId > 0)
            {
                ContactHelper.update(context, contact);
            }
            else
            {
                contactId = ContactHelper.insert(context, contact);
                witness.getContact().setId(contactId);
            }
        }

        // Then update the activity_witness
        ContentValues values = buildContentValues(witness, null);
        String whereClause = ID_COLUMN + "=" + witness.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, Witness activity_witness)");
    }

    /**
     * Private constructor to prevent instantiation
     */
    private WitnessHelper()
    {
    }
}
