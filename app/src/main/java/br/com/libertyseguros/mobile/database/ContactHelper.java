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
import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.Contact;

/**
 * Provides functionality for creating the Contact table and creating, reading, updating and deleting Contact objects
 * from the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class ContactHelper
{
    private static final String ADDRESS_ID_COLUMN = "ADDRESS_ID";

    private static final int ADDRESS_ID_INDEX = 7;

    private static final String EMAIL_ADDRESS_COLUMN = "EMAIL_ADDRESS";

    private static final int EMAIL_ADDRESS_INDEX = 1;

    private static final String FIRST_NAME_COLUMN = "FIRST_NAME";

    private static final int FIRST_NAME_INDEX = 2;

    private static final String HOME_PHONE_COLUMN = "HOME_PHONE";

    private static final int HOME_PHONE_INDEX = 3;

    private static final int ID_INDEX = 0;

    private static final String LAST_NAME_COLUMN = "LAST_NAME";

    private static final int LAST_NAME_INDEX = 4;

    private static final String MOBILE_PHONE_COLUMN = "MOBILE_PHONE";

    private static final int MOBILE_PHONE_INDEX = 5;

    private static final String NOTES_COLUMN = "NOTES";

    private static final int NOTES_INDEX = 6;

    private static final String TAG = ContactHelper.class.getName();

    /**
     * The name of the id column in the Contact table
     */
    protected static final String ID_COLUMN = "_id";

    /**
     * The name of the Contact table
     */
    protected static final String TABLE_NAME = "CONTACT";

    /**
     * Populates the given Contact object with the data in the cursor
     * 
     * @param cursor
     *            the result set to convert
     * @return the converted contact
     */
    private static Contact buildContact(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildContact(Cursor cursor)");

        Contact contact = new Contact();
        contact.setEmailAddress(cursor.getString(EMAIL_ADDRESS_INDEX));
        contact.setFirstName(cursor.getString(FIRST_NAME_INDEX));
        contact.setHomePhone(cursor.getString(HOME_PHONE_INDEX));
        contact.setId(cursor.getLong(ID_INDEX));
        contact.setLastName(cursor.getString(LAST_NAME_INDEX));
        contact.setMobilePhone(cursor.getString(MOBILE_PHONE_INDEX));
        contact.setNotes(cursor.getString(NOTES_INDEX));

        // Get the associated address
        Address address = new Address();
        address.setId(cursor.getLong(ADDRESS_ID_INDEX));
        contact.setAddress(address);

        //Log.v(TAG, "<<< buildContact(Cursor cursor)");

        return contact;
    }

    /**
     * Converts a Contact to a set of ContentValues
     * 
     * @param contact
     *            the contact to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(Contact contact)
    {
        //Log.v(TAG, ">>> buildContentValues(Contact contact)");

        ContentValues values = new ContentValues();
        values.put(FIRST_NAME_COLUMN, contact.getFirstName());
        values.put(LAST_NAME_COLUMN, contact.getLastName());
        values.put(EMAIL_ADDRESS_COLUMN, contact.getEmailAddress());
        values.put(HOME_PHONE_COLUMN, contact.getHomePhone());
        values.put(MOBILE_PHONE_COLUMN, contact.getMobilePhone());
        values.put(NOTES_COLUMN, contact.getNotes());

        Address address = contact.getAddress();

        if (null != address)
        {
            values.put(ADDRESS_ID_COLUMN, address.getId());
        }

        //Log.v(TAG, "<<< buildContentValues(Contact contact)");

        return values;
    }

    /**
     * This method will copy and existing contact;
     * 
     * @return
     */
    public static Contact copy(Context context, Contact contact)
    {
        Contact newContact = new Contact();
        newContact.setAddress(AddressHelper.copy(context, contact.getAddress()));
        newContact.setEmailAddress(contact.getEmailAddress());
        newContact.setFirstName(contact.getFirstName());
        newContact.setHomePhone(contact.getHomePhone());
        newContact.setLastName(contact.getLastName());
        newContact.setMobilePhone(contact.getMobilePhone());
        newContact.setNotes(contact.getNotes());
        newContact.setTypeId(contact.getTypeId());

        long id = insert(context, newContact);
        newContact.setId(id);

        return newContact;
    }

    /**
     * Deletes a Contact from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the Contact to delete
     */
    public static void delete(Context context, long id)
    {
        //Log.v(TAG, ">>> delete(Context context, Contact contact)");

        String whereClause = ID_COLUMN + "=" + id;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.delete(TABLE_NAME, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< delete(Context context, Contact contact)");
    }

    /**
     * Gets a Contact from the database
     * 
     * @param context
     *            the context within which to work
     * @return the Contact or null if no contact was found
     */
    public static Contact get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        Contact contact = null;

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

                // build the contact
                contact = buildContact(cursor);

                // now get the address for the contact
                Address address = contact.getAddress();
                Long addressId = address.getId();
                if (addressId != null && addressId > 0)
                {
                    address = AddressHelper.get(context, addressId);
                    contact.setAddress(address);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Contact with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return contact;
    }

    /**
     * Provides the query string necessary to create the Contact table
     * 
     * @return the SQL string necessary to create the Contact table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE CONTACT (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL_ADDRESS_COLUMN
                + " TEXT, " + FIRST_NAME_COLUMN + " TEXT, " + HOME_PHONE_COLUMN + " TEXT, " + LAST_NAME_COLUMN
                + " TEXT, " + MOBILE_PHONE_COLUMN + " TEXT, " + NOTES_COLUMN + " BLOB, " + ADDRESS_ID_COLUMN
                + " INTEGER REFERENCES " + AddressHelper.TABLE_NAME + "(" + AddressHelper.ID_COLUMN + "))";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Provides the query string necessary to create the triggers for the contact table which will ensure that whenever
     * a contact is deleted the corresponding address is also deleted.
     * 
     * @return the SQL string necessary to create the triggers for the Contact table
     */
    protected static String getCreateTriggersSQL()
    {
        //Log.v(TAG, ">>> getCreateTriggersSQL()");

        String createTriggerSQL =
            "CREATE TRIGGER DELETE_CONTACT BEFORE DELETE ON " + TABLE_NAME + " FOR EACH ROW BEGIN DELETE FROM "
                + AddressHelper.TABLE_NAME + " WHERE " + AddressHelper.ID_COLUMN + " = old." + ADDRESS_ID_COLUMN
                + "; END";

        //Log.v(TAG, "<<< getCreateTriggersSQL()");

        return createTriggerSQL;
    }

    /**
     * Inserts a Contact into the database
     * 
     * @param context
     *            the context within which to work
     * @param contact
     *            the contact to add to the database
     * @return the id of the new row in the database
     */
    public static Long insert(Context context, Contact contact)
    {
        //Log.v(TAG, ">>> insert(Context context, Contact contact)");

        // First insert the associated address
        Address address = contact.getAddress();
        if (address != null)
        {
            long id = AddressHelper.insert(context, address);
            address.setId(id);
        }

        // Then insert the contact
        ContentValues values = buildContentValues(contact);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, Contact contact)");

        return id;
    }

    /**
     * Updates an existing Contact in the database
     * 
     * @param context
     *            the context within which to work
     * @param contact
     *            the contact data to update
     */
    public static void update(Context context, Contact contact)
    {
        //Log.v(TAG, ">>> update(Context context, Contact contact)");

        // First insert or update the associated address
        Address address = contact.getAddress();
        if (address != null)
        {
            Long addressId = address.getId();
            if (addressId != null && addressId > 0)
            {
                AddressHelper.update(context, address);
            }
            else
            {
                addressId = AddressHelper.insert(context, address);
                contact.getAddress().setId(addressId);
            }
        }

        // Then update the contact
        ContentValues values = buildContentValues(contact);
        String whereClause = ID_COLUMN + "=" + contact.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, Contact contact)");
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ContactHelper()
    {
    }
}
