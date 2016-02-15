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
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.Policy;
import br.com.libertyseguros.mobile.model.User;


/**
 * Provides functionality for creating the User table and creating, reading, updating and deleting User objects from the
 * database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class UserHelper
{
	private static final String TAG = UserHelper.class.getName();
	
	public static final String ID_COLUMN = "_id";
	
	private static final int ID_INDEX = 0;
	
    private static final String CONTACT_ID_COLUMN = "CONTACT_ID";

    private static final int CONTACT_ID_INDEX = 1;
    
    private static final String CURRENT_COLUMN = "CURRENT";

    private static final int CURRENT_INDEX = 2;

    private static final String HOME_POLICY_COLUMN = "HOME_POLICY";

    private static final int HOME_POLICY_INDEX = 4;

    private static final String AUTO_POLICY_COLUMN = "AUTO_POLICY";

    private static final int AUTO_POLICY_INDEX = 3;
    
    /**
     * The name of the event id column in the user table
     */
    protected static final String EVENT_ID_COLUMN = "EVENT_ID";
    
    /**
     * Table name
     */
    public static final String TABLE_NAME = "USER";

    /**
     * Converts a User to a set of ContentValues
     * 
     * @param user
     *            the user to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(User user)
    {
        //Log.v(TAG, ">>> buildContentValues(User user)");

        ContentValues values = new ContentValues();

        Contact contact = user.getContact();
        if (contact != null)
        {
            values.put(CONTACT_ID_COLUMN, contact.getId());
        }

        if (user.isCurrent())
        {
            values.put(CURRENT_COLUMN, 1);
        }
        else
        {
            values.put(CURRENT_COLUMN, 0);
        }

        values.put(AUTO_POLICY_COLUMN, user.getAutoPolicy().getId());
//        values.put(HOME_POLICY_COLUMN, user.getHomePolicy().getId());

        //Log.v(TAG, "<<< buildContentValues(User user)");

        return values;
    }

    /**
     * Converts a result set to a User object
     * 
     * @param cursor
     *            the result set to convert
     * @return the converted User
     */
    private static User buildUser(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildUser(Cursor cursor)");

        User user = new User();
        user.setId(cursor.getLong(ID_INDEX));

        // Get the associated contact
        Contact contact = new Contact();
        contact.setId(cursor.getLong(CONTACT_ID_INDEX));
        user.setContact(contact);

        int cur = cursor.getInt(CURRENT_INDEX);
        user.setCurrent(cur == 1);

        //Log.v(TAG, "<<< buildUser(Cursor cursor)");

        return user;
    }

    /**
     * This method will clone the existing user. This includes cloning the associated policies, and contact information.
     * 
     * @return a copy of the user
     */
    public static User copy(Context context, User user)
    {
        User newUser = new User();

        Contact newContact = ContactHelper.copy(context, user.getContact());
        newUser.setContact(newContact);
        newUser.setCurrent(false);

        newUser.setAutoPolicy(PolicyHelper.copy(context, user.getAutoPolicy()));
//        newUser.setHomePolicy(PolicyHelper.copy(context, user.getHomePolicy()));

        long id = UserHelper.insert(context, newUser);
        newUser.setId(id);

        return newUser;
    }

    /**
     * Deletes a row from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the User to delete
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
     * Gets a User from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the user to return
     * @return the User or null if no user was found
     */
    public static User get(Context context, Long id)
    {
        //Log.v(TAG, ">>> get(Context context)");

        User user = null;

        try
        {
            String whereClause = ID_COLUMN + "=" + id;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // The application only allows for one user, so we only have to worry about the first row
                cursor.moveToFirst();

                // build the user specific contact info
                user = buildUser(cursor);

                // now get the contact for the user
                Contact contact = user.getContact();
                Long contactId = contact.getId();
                if (contactId != null && id > 0)
                {
                    contact = ContactHelper.get(context, contactId);
                    user.setContact(contact);
                }

                // now get the policies for the user
                user.setAutoPolicy(PolicyHelper.get(context, cursor.getLong(AUTO_POLICY_INDEX)));
//                user.setHomePolicy(PolicyHelper.get(context, cursor.getLong(HOME_POLICY_INDEX)));
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting User", e);
            }
        }

        //Log.v(TAG, "<<< get(Context context)");

        return user;
    }

    /**
     * Provides the query necessary to create the User table
     * 
     * @return the SQL string necessary to create the User table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

		String createTableSQL = "CREATE TABLE " + TABLE_NAME 
												+ " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
												+ CONTACT_ID_COLUMN + " INTEGER REFERENCES " + ContactHelper.TABLE_NAME + "(" + ContactHelper.ID_COLUMN + "), " 
												+ CURRENT_COLUMN + " INTEGER, "
												+ AUTO_POLICY_COLUMN + " INTEGER REFERENCES " + PolicyHelper.TABLE_NAME + "(" + PolicyHelper.ID_COLUMN + "), " 
												+ HOME_POLICY_COLUMN + " INTEGER REFERENCES " + PolicyHelper.TABLE_NAME + "(" + PolicyHelper.ID_COLUMN + "))";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Provides the query necessary to create the triggers for the User table ensuring that when a User is deleted, the
     * corresponding Contact is also deleted.
     * 
     * @return the SQL string necessary to create the triggers for the User table
     */
    protected static String getCreateTriggersSQL()
    {
        //Log.v(TAG, ">>> getCreateTriggersSQL()");

		String createTriggerSQL = "CREATE TRIGGER DELETE_USER BEFORE DELETE ON "
									+ TABLE_NAME 
									+ " FOR EACH ROW BEGIN DELETE FROM "
									+ ContactHelper.TABLE_NAME
									+ " WHERE "
									+ ContactHelper.ID_COLUMN
									+ " = old."
									+ CONTACT_ID_COLUMN
									+ "; END";

        //Log.v(TAG, "<<< getCreateTriggersSQL()");

        return createTriggerSQL;
    }

    /**
     * Gets the User from the database
     * 
     * @param context
     *            the context within which to work
     * @return the User or null if no user was found
     */
    public static User getCurrent(Context context)
    {
        //Log.v(TAG, ">>> get(Context context)");

        User user = null;

        try
        {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, null, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // The application only allows for one user, so we only have to worry about the first row
                cursor.moveToFirst();

                // build the user specific contact info
                user = buildUser(cursor);

                // now get the contact for the user
                Contact contact = user.getContact();
                Long id = contact.getId();
                if (id != null && id > 0)
                {
                    contact = ContactHelper.get(context, id);
                    user.setContact(contact);
                }

                // now get the policies for the user
                user.setAutoPolicy(PolicyHelper.get(context, cursor.getLong(AUTO_POLICY_INDEX)));
//                user.setHomePolicy(PolicyHelper.get(context, cursor.getLong(HOME_POLICY_INDEX)));
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting User", e);
            }
        }

        //Log.v(TAG, "<<< get(Context context)");

        return user;
    }

    /**
     * Inserts a User into the database
     * 
     * @param context
     *            the context within which to work
     * @param user
     *            the user to add to the database
     * @return the id of the new row in the database
     */
    public static long insert(Context context, User user)
    {
        //Log.v(TAG, ">>> insert(Context context, User user)");

        // First insert the associated contact
        Contact contact = user.getContact();
        if (contact != null)
        {
            long id = ContactHelper.insert(context, contact);
            contact.setId(id);
        }

        // Then insert the associated policies
        long id = PolicyHelper.insert(context, user.getAutoPolicy(), true);
        user.getAutoPolicy().setId(id);

//        id = PolicyHelper.insert(context, user.getHomePolicy(), true);
//        user.getHomePolicy().setId(id);

        // Then insert the user
        ContentValues values = buildContentValues(user);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, User user)");

        return id;
    }

    /**
     * @param context
     * @param policies
     * @param i
     */
    private static void insertOrUpdatePolicy(Context context, Policy policy)
    {
        Long policyId = policy.getId();
        if (policyId != null && policyId > 0)
        {
            PolicyHelper.update(context, policy);
        }
        else
        {
            policyId = PolicyHelper.insert(context, policy, true);
            policy.setId(policyId);
        }
    }

    /**
     * Updates an existing User in the database
     * 
     * @param context
     *            the context within which to work
     * @param user
     *            the user data to update
     */
    public static void update(Context context, User user)
    {
        //Log.v(TAG, ">>> update(Context context, User user)");

        // First insert or update the associated contact
        Contact contact = user.getContact();
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
                user.getContact().setId(contactId);
            }
        }

        // Then insert or update the associated policies

        Policy policy = user.getAutoPolicy();
        insertOrUpdatePolicy(context, policy);
//        policy = user.getHomePolicy();
        insertOrUpdatePolicy(context, policy);

        // Then update the user
        ContentValues values = buildContentValues(user);
        String whereClause = ID_COLUMN + "=" + user.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, User user)");
    }

    /**
     * Private constructor to prevent instantiation
     */
    private UserHelper()
    {
    }
}
