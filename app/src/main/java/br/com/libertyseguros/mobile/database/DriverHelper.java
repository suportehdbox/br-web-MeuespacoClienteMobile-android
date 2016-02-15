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
import br.com.libertyseguros.mobile.model.Driver;
import br.com.libertyseguros.mobile.model.Policy;

/**
 * Provides functionality for creating the Driver table and creating, reading, updating and deleting Driver objects from
 * the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class DriverHelper
{
    private static final String CONTACT_ID_COLUMN = "CONTACT_ID";

    private static final int CONTACT_ID_INDEX = 2;

    private static final String ID_COLUMN = "_id";

    private static final int ID_INDEX = 0;

    private static final String INSURANCE_COMPANY_COLUMN = "INSURANCE_COMPANY";

    private static final int INSURANCE_COMPANY_INDEX = 1;

    private static final String POLICY_ID_COLUMN = "POLICY_ID";

    private static final int POLICY_ID_INDEX = 3;

    private static final String TAG = DriverHelper.class.getName();

    /**
     * The name of the event id column in the Driver table
     */
    protected static final String EVENT_ID_COLUMN = "EVENT_ID";

    /**
     * The name of the Driver table
     */
    protected static final String TABLE_NAME = "DRIVER";

    /**
     * Converts a Driver to a set of ContentValues
     * 
     * @param driver
     *            the driver to convert
     * @param eventId
     *            the id of the event that the driver belongs to
     * @return the converted content values
     */
    private static ContentValues buildContentValues(Driver driver, Long eventId)
    {
        //Log.v(TAG, ">>> buildContentValues(Driver driver, Long eventId)");

        ContentValues values = new ContentValues();
        values.put(INSURANCE_COMPANY_COLUMN, driver.getInsuranceCompany());

        if (eventId != null)
        {
            values.put(EVENT_ID_COLUMN, eventId);
        }

        // Build the contact part of the driver
        Contact contact = driver.getContact();
        if (contact != null)
        {
            values.put(CONTACT_ID_COLUMN, contact.getId());
        }

        // Build the policy part of the driver
        Policy policy = driver.getPolicy();
        if (policy != null)
        {
            values.put(POLICY_ID_COLUMN, policy.getId());
        }
        //Log.v(TAG, "<<< buildContentValues(Driver driver, Long eventId)");

        return values;
    }

    /**
     * Converts a result set to a Driver object
     * 
     * @param cursor
     *            the result set to convert
     * @return the converted Driver
     */
    private static Driver buildDriver(Context context, Cursor cursor)
    {
        //Log.v(TAG, ">>> buildDriver(Cursor cursor)");

        Driver driver = new Driver();
        driver.setId(cursor.getLong(ID_INDEX));
        driver.setInsuranceCompany(cursor.getString(INSURANCE_COMPANY_INDEX));

        // Get the associated contact
        Contact contact = ContactHelper.get(context, cursor.getLong(CONTACT_ID_INDEX));
        driver.setContact(contact);

        // Get the associated policy
        Policy policy = PolicyHelper.get(context, cursor.getLong(POLICY_ID_INDEX));
        driver.setPolicy(policy);

        //Log.v(TAG, "<<< buildDriver(Cursor cursor)");

        return driver;
    }

    /**
     * Builds a Driver object from the results of the database
     * 
     * @param context
     *            the context in which to work
     * @param cursor
     *            the result set to convert
     * @return
     */
    private static Driver createDriverObject(Context context, Cursor cursor)
    {
        //Log.v(TAG, ">>> createDriverObject(Context context, Cursor cursor)");

        Driver driver;
        // build the driver specific contact info
        driver = buildDriver(context, cursor);

        //Log.v(TAG, "<<< createDriverObject(Context context, Cursor cursor)");

        return driver;
    }

    /**
     * Deletes a Driver from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the Driver to delete
     */
    public static void delete(Context context, long id)
    {
        //Log.v(TAG, ">>> delete(Context context, Driver driver)");

        // then delete the driver
        String whereClause = ID_COLUMN + "=" + id;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.delete(TABLE_NAME, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< delete(Context context, Driver driver)");
    }

    /**
     * Gets a Driver from the database
     * 
     * @param context
     *            the context within which to work
     * @return the Driver or null if no driver was found
     */
    public static Driver get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        Driver driver = null;

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

                driver = createDriverObject(context, cursor);
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Driver with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return driver;
    }

    /**
     * Gets the list of Drivers which belong to the given event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for drivers
     * @return the Contacts or an empty array if no driver was found
     */
    public static ArrayList<Driver> getByEvent(Context context, long eventId)
    {
        //Log.v(TAG, ">>> getContactByEvent(Context context, long eventId)");

        ArrayList<Driver> drivers = new ArrayList<Driver>();

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Get the contact info for each driver returned
                while (cursor.moveToNext())
                {
                    Driver driver = buildDriver(context, cursor);

                    drivers.add(driver);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Driver Contact Info with Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getContactByEvent(Context context, long eventId)");

        return drivers;
    }

    /**
     * Gets the Contact information for the list of Drivers which belong to the given event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for drivers
     * @return the Contacts or an empty array if no driver was found
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
                Log.e(TAG, "Error getting Driver Contact Info with Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getContactByEvent(Context context, long eventId)");

        return contacts;
    }

    /**
     * Provides the query to create the Driver table
     * 
     * @return the SQL string necessary to create the Driver table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INSURANCE_COMPANY_COLUMN + " TEXT, " + CONTACT_ID_COLUMN + " INTEGER REFERENCES "
                + ContactHelper.TABLE_NAME + "(" + ContactHelper.ID_COLUMN + "), " + POLICY_ID_COLUMN
                + " INTEGER REFERENCES " + PolicyHelper.TABLE_NAME + "(" + PolicyHelper.ID_COLUMN + "), "
                + EVENT_ID_COLUMN + " INTEGER REFERENCES " + EventHelper.TABLE_NAME + "(" + EventHelper.ID_COLUMN
                + "))";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Provides the query to create a trigger forcing the deletion of the child Contact associated with a Driver when
     * the Driver is deleted
     * 
     * @return the SQL string necessary to create the triggers for the Driver table
     */
    protected static String getCreateTriggersSQL()
    {
        //Log.v(TAG, ">>> getCreateTriggersSQL()");

        String createTriggerSQL =
            "CREATE TRIGGER DELETE_DRIVER BEFORE DELETE ON " + TABLE_NAME + " FOR EACH ROW BEGIN DELETE FROM "
                + ContactHelper.TABLE_NAME + " WHERE " + ContactHelper.ID_COLUMN + " = old." + CONTACT_ID_COLUMN
                + "; DELETE FROM " + PolicyHelper.TABLE_NAME + " WHERE " + PolicyHelper.ID_COLUMN + " = old."
                + POLICY_ID_COLUMN + "; END";

        //Log.v(TAG, "<<< getCreateTriggersSQL()");

        return createTriggerSQL;
    }

    /**
     * Inserts a Driver into the database
     * 
     * @param context
     *            the context within which to work
     * @param driver
     *            the driver to add to the database
     * @param eventId
     *            the id of the event that the driver belongs to
     * @return the id of the new row in the database
     */
    public static long insert(Context context, Driver driver, Long eventId)
    {
        //Log.v(TAG, ">>> insert(Context context, Driver driver, Long eventId)");

        // First insert the associated contact
        Contact contact = driver.getContact();
        if (contact != null)
        {
            long id = ContactHelper.insert(context, contact);
            contact.setId(id);
        }

        // Then the associated policy
        Policy policy = driver.getPolicy();
        if (policy != null)
        {
            long id = PolicyHelper.insert(context, policy, false);
            policy.setId(id);
        }

        // Then insert the driver
        ContentValues values = buildContentValues(driver, eventId);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, Driver driver, Long eventId)");

        return id;
    }

    /**
     * Updates an existing Driver in the database
     * 
     * @param context
     *            the context within which to work
     * @param driver
     *            the driver data to update
     */
    public static void update(Context context, Driver driver)
    {
        //Log.v(TAG, ">>> update(Context context, Driver driver)");

        // First insert or update the associated contact
        Contact contact = driver.getContact();
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
                driver.getContact().setId(contactId);
            }
        }

        // Then insert or update the associated policy
        Policy policy = driver.getPolicy();
        if (policy != null)
        {
            Long policyId = policy.getId();
            if (policyId != null && policyId > 0)
            {
                PolicyHelper.update(context, policy);
            }
            else
            {
                policyId = PolicyHelper.insert(context, policy, false);
                driver.getPolicy().setId(policyId);
            }
        }

        // Then update the driver
        ContentValues values = buildContentValues(driver, null);
        String whereClause = ID_COLUMN + "=" + driver.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, Driver driver)");
    }

    /**
     * Private constructor to prevent instantiation
     */
    private DriverHelper()
    {
    }
}
