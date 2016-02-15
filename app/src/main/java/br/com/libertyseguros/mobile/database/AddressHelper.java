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

/**
 * Provides functionality for creating the Address table and creating, reading, updating and deleting Address objects
 * from the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class AddressHelper
{
	
	/**
	 * The name of the Address table
	 */
	protected static final String TABLE_NAME = "ADDRESS";
	
	private static final int ID_INDEX = 0;
	
	/**
	 * The name of the id column in the Address table
	 */
	protected static final String ID_COLUMN = "_id";

	private static final int CITY_INDEX = 1;
    
	private static final String CITY_COLUMN = "CITY";

	private static final int CROSS_STREET_INDEX = 2;

    private static final String CROSS_STREET_COLUMN = "CROSS_STREET";

    private static final int LATITUDE_INDEX = 3;

    private static final String LATITUDE_COLUMN = "LATITUDE";

    private static final int LONGITUDE_INDEX = 4;

    private static final String LONGITUDE_COLUMN = "LONGITUDE";

    private static final int STATE_INDEX = 5;

    private static final String STATE_COLUMN = "STATE";

    private static final int STREET_INDEX = 6;

    private static final String STREET_COLUMN = "STREET";

    private static final int ZIP_INDEX = 7;

    private static final String ZIP_COLUMN = "ZIP_CODE";
    
    private static final String TAG = AddressHelper.class.getName();

    /**
     * Converts a result set to an Address object
     * 
     * @param cursor
     *            the result set to convert
     * @return the converted Address
     */
    private static Address buildAddress(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildAddress(Cursor cursor)");

        Address address = new Address();
        address.setCity(cursor.getString(CITY_INDEX));
//        address.setCrossStreet(cursor.getString(CROSS_STREET_INDEX));
        address.setId(cursor.getLong(ID_INDEX));
        address.setLatitude(cursor.getDouble(LATITUDE_INDEX));
        address.setLongitude(cursor.getDouble(LONGITUDE_INDEX));
        address.setState(cursor.getString(STATE_INDEX));
        address.setStreetAddress(cursor.getString(STREET_INDEX));
        address.setZipCode(cursor.getString(ZIP_INDEX));

        //Log.v(TAG, "<<< buildAddress(Cursor cursor)");

        return address;
    }

    /**
     * Converts an Address to a set of ContentValues
     * 
     * @param address
     *            the address to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(Address address)
    {
        //Log.v(TAG, ">>> buildContentValues(Address address)");

        ContentValues values = new ContentValues();
        values.put(STREET_COLUMN, address.getStreetAddress());
//        values.put(CROSS_STREET_COLUMN, address.getCrossStreet());
        values.put(CITY_COLUMN, address.getCity());
        values.put(STATE_COLUMN, address.getState());
        values.put(ZIP_COLUMN, address.getZipCode());
        values.put(LATITUDE_COLUMN, address.getLatitude());
        values.put(LONGITUDE_COLUMN, address.getLongitude());

        //Log.v(TAG, ">>> buildContentValues(Address address)");

        return values;
    }

    /**
     * this method will return a copy of the existing address
     * 
     * @return
     */
    public static Address copy(Context context, Address address)
    {
        if (address == null)
        {
            return null;
        }
        Address newAddress = new Address();
        newAddress.setCity(address.getCity());
//        newAddress.setCrossStreet(address.getCrossStreet());
        newAddress.setLatitude(address.getLatitude());
        newAddress.setLongitude(address.getLongitude());
        newAddress.setState(address.getState());
        newAddress.setStreetAddress(address.getStreetAddress());
        newAddress.setZipCode(address.getZipCode());

        long id = insert(context, newAddress);
        newAddress.setId(id);

        return newAddress;
    }

    /**
     * Deletes an Address from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the Address to delete
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
     * Gets the Address with the given id from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the record to return
     * @return
     */
    public static Address get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

        Address address = null;

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

                address = buildAddress(cursor);
            }

            cursor.close();
            databaseHelper.close();

        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Address with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return address;
    }

    /**
     * Provides the query string necessary to create the Address table
     * 
     * @return the SQL string necessary to create the Address table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY_COLUMN
                + " TEXT, " + CROSS_STREET_COLUMN + " TEXT, " + LATITUDE_COLUMN + " REAL, " + LONGITUDE_COLUMN
                + " REAL, " + STATE_COLUMN + " TEXT, " + STREET_COLUMN + " TEXT, " + ZIP_COLUMN + " TEXT)";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Inserts the Address into the database
     * 
     * @param context
     *            the context within which to work
     * @param address
     *            the address to add to the database
     * @return the id of the new row in the database
     */
    public static long insert(Context context, Address address)
    {
        //Log.v(TAG, ">>> insert(Context context, Address address)");

        ContentValues values = buildContentValues(address);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, Address address)");

        return id;
    }

    /**
     * Updates an existing Address in the database
     * 
     * @param context
     *            the context within which to work
     * @param address
     *            the address data to update
     */
    public static void update(Context context, Address address)
    {
        //Log.v(TAG, ">>> update(Context context, Address address)");

        ContentValues values = buildContentValues(address);

        String whereClause = ID_COLUMN + "=" + address.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, Address address)");
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AddressHelper()
    {
    }
}
