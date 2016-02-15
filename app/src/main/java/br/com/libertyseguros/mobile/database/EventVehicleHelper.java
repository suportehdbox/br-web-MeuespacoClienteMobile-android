/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.database;

/**
 * Provides functionality for creating the Event_Vehicle table
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class EventVehicleHelper
{
    //private static final String TAG = EventVehicleHelper.class.getName();

    protected static final String EVENT_ID_COLUMN = "EVENT_ID";

    protected static final int EVENT_ID_INDEX = 1;

    protected static final String ID_COLUMN = "_id";

    protected static final int ID_INDEX = 0;

    protected static final String TABLE_NAME = "EVENT_VEHICLE";

    protected static final String VEHICLE_ID_COLUMN = "VEHICLE_ID";

    protected static final int VEHICLE_ID_INDEX = 2;

    /**
     * Provides the query necessary to create the Event_Vehicle table
     * 
     * @return the SQL string necessary to create the Event_Vehicle table
     */
    protected static String getCreateTableSQL()
    {
        // Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
            "CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_ID_COLUMN
                + " INTEGER REFERENCES " + EventHelper.TABLE_NAME + "(" + EventHelper.ID_COLUMN + "), "
                + VEHICLE_ID_COLUMN + " INTEGER REFERENCES " + VehicleHelper.TABLE_NAME + "(" + VehicleHelper.ID_COLUMN
                + "))";

        // Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private EventVehicleHelper()
    {
    }
}
