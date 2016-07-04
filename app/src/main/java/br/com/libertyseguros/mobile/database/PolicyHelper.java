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

import java.util.ArrayList;

import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.Policy;
import br.com.libertyseguros.mobile.model.Vehicle;

/**
 * Provides functionality for creating the Policy table and creating, reading, updating and deleting Policy objects from
 * the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class PolicyHelper
{
    private static final String ADDRESS_ID_COLUMN = "ADDRESS_ID";

    private static final int ADDRESS_ID_INDEX = 3;

    private static final int ID_INDEX = 0;

    private static final String POLICY_LOB_COLUMN = "POLICY_LOB";

    private static final int POLICY_LOB_INDEX = 2;

    private static final String POLICY_NUMBER_COLUMN = "POLICY_NUMBER";

    private static final int POLICY_NUMBER_INDEX = 1;

    private static final String TAG = PolicyHelper.class.getName();

    private static final String USER_POLICY_COLUMN = "USER_POLICY";

    /**
     * The name of the id column in the Policy table
     */
    protected static final String ID_COLUMN = "_id";

    /**
     * The name of the Policy table
     */
    protected static final String TABLE_NAME = "POLICY";

    /**
     * Converts an Policy to a set of ContentValues
     * 
     * @param policy
     *            the policy to convert
     * @param isUser
     *            whether or not this is a user level policy
     * @return the converted content values
     */
    private static ContentValues buildContentValues(Policy policy, Boolean isUser)
    {
        //Log.v(TAG, ">>> buildContentValues(Policy policy)");

        ContentValues values = new ContentValues();
        values.put(POLICY_LOB_COLUMN, policy.getPolicyLOB());
        values.put(POLICY_NUMBER_COLUMN, policy.getPolicyNumber());
        if (isUser != null && isUser)
        {
            values.put(USER_POLICY_COLUMN, 1);
        }

        Address address = policy.getPropertyAddress();
        if (address != null)
        {
            values.put(ADDRESS_ID_COLUMN, address.getId());
        }

        //Log.v(TAG, ">>> buildContentValues(Policy policy)");

        return values;
    }

    /**
     * Converts a result set to an Policy object
     * 
     * @param cursor
     *            the result set to convert
     * @return the convert Policy
     */
    private static Policy buildPolicy(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildPolicy(Cursor cursor)");

        Policy policy = new Policy();

        policy.setId(cursor.getLong(ID_INDEX));
        policy.setPolicyLOB(cursor.getString(POLICY_LOB_INDEX));
        policy.setPolicyNumber(cursor.getString(POLICY_NUMBER_INDEX));

        // Get the associated Address
        Address address = new Address();
        address.setId(cursor.getLong(ADDRESS_ID_INDEX));
        policy.setPropertyAddress(address);

        //Log.v(TAG, "<<< buildPolicy(Cursor cursor)");

        return policy;
    }

    /**
     * This method will return a copy of the existing policy
     * 
     * @return
     */
    public static Policy copy(Context context, Policy policy)
    {
        Policy newPolicy = new Policy();
        newPolicy.setPolicyLOB(policy.getPolicyLOB());
        newPolicy.setPolicyNumber(policy.getPolicyNumber());
        newPolicy.setPropertyAddress(AddressHelper.copy(context, policy.getPropertyAddress()));

        // This is intentionally set to null. At this time the only place where the clone is called is when submitting a
        // claim. The vehicle involved with the claim is stored seperately from the policy information.
        newPolicy.setPolicyVehicles(null);

        long id = insert(context, newPolicy, false);
        newPolicy.setId(id);

        return newPolicy;
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
     * Gets the Policy with the given id from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the record to return
     * @return
     */
    public static Policy get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        Policy policy = null;

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

                policy = buildPolicy(cursor);

                if (Constants.LOB_AUTO.equals(policy.getPolicyLOB()))
                {
                    policy.setPolicyVehicles(VehicleHelper.getByPolicy(context, policy.getId()));
                }
                else
                {
                    policy.setPropertyAddress(AddressHelper.get(context, cursor.getLong(ADDRESS_ID_INDEX)));
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Policy with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return policy;
    }

    /**
     * Gets all Policies from the database
     * 
     * @param context
     *            the context within which to work
     * @return
     */
    public static ArrayList<Policy> getAllForUser(Context context)
    {
        //Log.v(TAG, ">>> getAll(Context context)");

        ArrayList<Policy> policies = new ArrayList<Policy>();

        try
        {
            String whereClause = USER_POLICY_COLUMN + "=" + 1;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    Policy policy = buildPolicy(cursor);

                    if (Constants.LOB_AUTO.equals(policy.getPolicyLOB()))
                    {
                        policy.setPolicyVehicles(VehicleHelper.getByPolicy(context, policy.getId()));
                    }
                    else
                    {
                        policy.setPropertyAddress(AddressHelper.get(context, policy.getPropertyAddress().getId()));
                    }
                    policies.add(policy);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting all Policiess", e);
            }
        }

        //Log.v(TAG, "<<< getAll(Context context)");

        return policies;
    }

    /**
     * Provides the query necessary to create the Policy table
     * 
     * @return the SQL string necessary to create the Policy table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + POLICY_NUMBER_COLUMN + " TEXT, " + POLICY_LOB_COLUMN + " TEXT, " + ADDRESS_ID_COLUMN
                + " INTEGER REFERENCES " + AddressHelper.TABLE_NAME + "(" + AddressHelper.ID_COLUMN + "), "
                + USER_POLICY_COLUMN + " INTEGER " + ")";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Provides the query necessary to create the triggers for the Policy table ensuring that when a Policy is deleted,
     * the corresponding Property Address connections are also deleted. Vehicle connections are broken but not deleted
     * because the Vehicle could be tied to an Event.
     * 
     * @return the SQL string necessary to create the triggers for the User table
     */
    protected static String getCreateTriggersSQL()
    {
        //Log.v(TAG, ">>> getCreateTriggersSQL()");

        String createTriggerSQL =
            "CREATE TRIGGER DELETE_POLICY BEFORE DELETE ON " + TABLE_NAME + " FOR EACH ROW BEGIN DELETE FROM "
                + AddressHelper.TABLE_NAME + " WHERE " + AddressHelper.ID_COLUMN + " = old." + ADDRESS_ID_COLUMN
                + "; UPDATE " + VehicleHelper.TABLE_NAME + " SET " + VehicleHelper.POLICY_ID_COLUMN + "=NULL WHERE "
                + VehicleHelper.POLICY_ID_COLUMN + " = old." + ID_COLUMN + "; END";

        //Log.v(TAG, "<<< getCreateTriggersSQL()");

        return createTriggerSQL;
    }

    /**
     * Inserts the Policy into the database
     * 
     * @param context
     *            the context within which to work
     * @param policy
     *            the policy to add to the database
     * @param isUser
     *            whether or not this is a user level policy
     * @return the id of the new row in the database
     */
    public static long insert(Context context, Policy policy, boolean isUser)
    {
        //Log.v(TAG, ">>> insert(Context context, Policy policy)");

        // First insert the associated address if this is a home policy
//        if (Constants.LOB_HOME.equals(policy.getPolicyLOB()))
//        {
//            Address address = policy.getPropertyAddress();
//            if (address != null)
//            {
//                long id = AddressHelper.insert(context, address);
//                address.setId(id);
//            }
//        }

        ContentValues values = buildContentValues(policy, isUser);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        // insert the associated vehicles if this is an auto policy
        if (Constants.LOB_AUTO.equals(policy.getPolicyLOB()))
        {
            ArrayList<Vehicle> vehicles = policy.getPolicyVehicles();

            if (vehicles != null)
            {
                int size = vehicles.size();

                for (int i = 0; i < size; i++)
                {
                    Vehicle vehicle = vehicles.get(i);
                    long vehicleId = VehicleHelper.insert(context, vehicle, id);
                    vehicle.setId(vehicleId);
                }
            }
        }

        //Log.v(TAG, "<<< insert(Context context, Policy policy)");

        return id;
    }

    /**
     * Updates an existing Policy in the database
     * 
     * @param context
     *            the context within which to work
     * @param policy
     *            the policy data to update
     */
    public static void update(Context context, Policy policy)
    {
        //Log.v(TAG, ">>> update(Context context, Policy policy)");

//        // First insert or update the associated activity_location if this is a home policy
//        if (Constants.LOB_HOME.equals(policy.getPolicyLOB()))
//        {
//            updateAddress(context, policy);
//        }
        // or the associated vehicles if this is an auto policy
        if (Constants.LOB_AUTO.equals(policy.getPolicyLOB()))
        {
            updateVehicles(context, policy);
        }

        ContentValues values = buildContentValues(policy, null);

        String whereClause = ID_COLUMN + "=" + policy.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, Policy policy)");
    }

    /**
     * @param context
     * @param policy
     */
    private static void updateAddress(Context context, Policy policy)
    {
        Address address = policy.getPropertyAddress();
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
                address.setId(addressId);
            }
        }
    }

    /**
     * @param context
     * @param policy
     */
    private static void updateVehicles(Context context, Policy policy)
    {
        ArrayList<Vehicle> vehicles = policy.getPolicyVehicles();
        if (vehicles != null)
        {
            int size = vehicles.size();

            for (int i = 0; i < size; i++)
            {
                Vehicle vehicle = vehicles.get(i);
                Long vehicleId = vehicle.getId();
                if (vehicleId != null && vehicleId > 0)
                {
                    VehicleHelper.update(context, vehicle);
                }
                else
                {
                    vehicleId = VehicleHelper.insert(context, vehicle, policy.getId());
                    vehicle.setId(vehicleId);
                }
            }
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private PolicyHelper()
    {
    }
}
