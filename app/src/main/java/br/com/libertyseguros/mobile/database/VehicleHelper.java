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
import br.com.libertyseguros.mobile.model.Vehicle;

/**
 * Provides functionality for creating the Vehicle table and creating, reading, updating and deleting Vehicle objects
 * from the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class VehicleHelper
{
    private static final String COLOR_COLUMN = "COLOR";

    private static final int COLOR_INDEX = 1;

    private static final int ID_INDEX = 0;

    private static final String MAKE_COLUMN = "MAKE";

    private static final int MAKE_INDEX = 2;

    private static final String MODEL_COLUMN = "MODEL";

    private static final int MODEL_INDEX = 3;

    private static final String REGISTRATION_NUMBER_COLUMN = "REGISTRATION_NUMBER";

    private static final int REGISTRATION_NUMBER_INDEX = 4;

    private static final String REGISTRATION_STATE_COLUMN = "REGISTRATION_STATE";

    private static final int REGISTRATION_STATE_INDEX = 5;

    private static final String TAG = VehicleHelper.class.getName();

    private static final String VIN_COLUMN = "VIN";

    private static final int VIN_INDEX = 7;

    private static final String YEAR_COLUMN = "YEAR";

    private static final int YEAR_INDEX = 6;

    /**
     * The name of the id column in the Vehicle table
     */
    protected static final String ID_COLUMN = "_id";

    /**
     * The name of the column holding the id of the policy that the vehicle is tied too
     */
    protected static final String POLICY_ID_COLUMN = "POLICY_ID";

    /**
     * The name of the Vehicle table
     */
    protected static final String TABLE_NAME = "VEHICLE";

    /**
     * Deletes an Vehicle from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the Vehicle to delete
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
     * Gets the Vehicle with the given id from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the record to return
     * @return
     */
    public static Vehicle get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        Vehicle vehicle = null;

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

                vehicle = buildVehicle(cursor);
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Vehicle with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return vehicle;
    }

    /**
     * Gets the Vehicle with the given id from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the record to return
     * @return
     */
    public static ArrayList<Vehicle> getByPolicy(Context context, long id)
    {
        //Log.v(TAG, ">>> getByPolicy(Context context, long id)");

        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

        try
        {
            String whereClause = POLICY_ID_COLUMN + "=" + id;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    Vehicle vehicle = buildVehicle(cursor);

                    vehicles.add(vehicle);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Vehicles by policy with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< getByPolicy(Context context, long id)");

        return vehicles;
    }

    /**
     * Inserts the Vehicle into the database
     * 
     * @param context
     *            the context within which to work
     * @param vehicle
     *            the vehicle to add to the database
     * @param policyId
     *            the id of the policy to which the vehicle belongs, if applicable
     * @return the id of the new row in the database
     */
    public static long insert(Context context, Vehicle vehicle, Long policyId)
    {
        //Log.v(TAG, ">>> insert(Context context, Vehicle vehicle, Long policyId)");

        ContentValues values = buildContentValues(vehicle, policyId);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, Vehicle vehicle, Long policyId)");

        return id;
    }

    /**
     * Updates an existing Vehicle in the database
     * 
     * @param context
     *            the context within which to work
     * @param vehicle
     *            the vehicle data to update
     */
    public static void update(Context context, Vehicle vehicle)
    {
        //Log.v(TAG, ">>> update(Context context, Vehicle vehicle)");

        ContentValues values = buildContentValues(vehicle, null);

        String whereClause = ID_COLUMN + "=" + vehicle.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, Vehicle vehicle)");
    }

    /**
     * Converts an Vehicle to a set of ContentValues
     * 
     * @param vehicle
     *            the vehicle to convert
     * @param policyId
     *            the id of the policy that this vehicle belongs to, if any
     * @return the converted content values
     */
    private static ContentValues buildContentValues(Vehicle vehicle, Long policyId)
    {
        //Log.v(TAG, ">>> buildContentValues(Vehicle vehicle, Long policyId)");

        ContentValues values = new ContentValues();
        values.put(YEAR_COLUMN, vehicle.getYear());
        values.put(MAKE_COLUMN, vehicle.getMake());
        values.put(COLOR_COLUMN, vehicle.getColor());
        values.put(REGISTRATION_STATE_COLUMN, vehicle.getRegistrationState());
        values.put(MODEL_COLUMN, vehicle.getModel());
        values.put(REGISTRATION_NUMBER_COLUMN, vehicle.getRegistrationNumber());
        values.put(VIN_COLUMN, vehicle.getVehicleIdentificationNumber());

        if (policyId != null)
        {
            values.put(POLICY_ID_COLUMN, policyId);
        }
        //Log.v(TAG, ">>> buildContentValues(Vehicle vehicle, Long policyId)");

        return values;
    }

    /**
     * Converts a result set to an Vehicle object
     * 
     * @param cursor
     *            the result set to convert
     * @return the convert Vehicle
     */
    private static Vehicle buildVehicle(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildVehicle(Cursor cursor)");

        Vehicle vehicle = new Vehicle();
        vehicle.setColor(cursor.getString(COLOR_INDEX));
        vehicle.setMake(cursor.getString(MAKE_INDEX));
        vehicle.setId(cursor.getLong(ID_INDEX));
        vehicle.setModel(cursor.getString(MODEL_INDEX));
        vehicle.setRegistrationNumber(cursor.getString(REGISTRATION_NUMBER_INDEX));
        vehicle.setRegistrationState(cursor.getString(REGISTRATION_STATE_INDEX));
        vehicle.setYear(cursor.getString(YEAR_INDEX));
        vehicle.setVehicleIdentificationNumber(cursor.getString(VIN_INDEX));

        //Log.v(TAG, "<<< buildVehicle(Cursor cursor)");

        return vehicle;
    }

    /**
     * Provides the query necessary to create the Vehicle table
     * 
     * @return the SQL string necessary to create the User table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLOR_COLUMN
                + " TEXT, " + MAKE_COLUMN + " TEXT, " + MODEL_COLUMN + " REAL, " + REGISTRATION_NUMBER_COLUMN
                + " REAL, " + REGISTRATION_STATE_COLUMN + " TEXT, " + YEAR_COLUMN + " TEXT, " + VIN_COLUMN + " TEXT, "
                + POLICY_ID_COLUMN + " INTEGER REFERENCES " + PolicyHelper.TABLE_NAME + "(" + PolicyHelper.ID_COLUMN
                + "))";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private VehicleHelper()
    {
    }
}
