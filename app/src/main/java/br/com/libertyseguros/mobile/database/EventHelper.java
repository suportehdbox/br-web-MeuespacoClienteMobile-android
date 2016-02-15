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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.Event;
import br.com.libertyseguros.mobile.model.User;
import br.com.libertyseguros.mobile.model.Vehicle;

/**
 * Provides functionality for creating the Event table and creating, reading, updating and deleting Event objects from
 * the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public final class EventHelper
{
    private static final String ANYONE_INJURED_COLUMN = "ANYONE_INJURED";

    private static final int ANYONE_INJURED_INDEX = 12;

    private static final String SUBMITTED_USER_ID_COLUMN = "SUBMITTED_USER";

    private static final int SUBMITTED_USER_ID_INDEX = 13;

    private static final String CLAIM_NUMBER_COLUMN = "CLAIM_NUMBER";

    private static final int CLAIM_NUMBER_INDEX = 14;

    private static final String CREATE_DATE_TIME_COLUMN = "CREATE_DATE_TIME";

    private static final int CREATE_DATE_TIME_INDEX = 1;

    private static final String EVENT_STATUS_COLUMN = "EVENT_STATUS";

    private static final int EVENT_STATUS_INDEX = 2;

    private static final String EVENT_SUB_TYPE_COLUMN = "EVENT_SUB_TYPE";

    private static final String EVENT_SUB_TYPE_DETAILS_COLUMN = "EVENT_SUB_TYPE_DETAILS";

    private static final int EVENT_SUB_TYPE_DETAILS_INDEX = 10;

    private static final int EVENT_SUB_TYPE_INDEX = 3;

    private static final String EVENT_TYPE_COLUMN = "EVENT_TYPE";

    private static final int EVENT_TYPE_INDEX = 4;

    private static final int ID_INDEX = 0;

    private static final String INCIDENT_DATE_TIME_COLUMN = "INCIDENT_DATE_TIME";

    private static final int INCIDENT_DATE_TIME_INDEX = 5;

    private static final String INCIDENT_LOCATION_ID_COLUMN = "INCIDENT_LOCATION_ID";

    private static final int INCIDENT_LOCATION_ID_INDEX = 9;

    private static final String NOTES_COLUMN = "NOTES";

    private static final int NOTES_INDEX = 6;

    private static final String SORT_VALUE_COLUMN = "SORT_VALUE";

    private static final int SORT_VALUE_INDEX = 8;

    private static final String SUBMIT_DATE_TIME_COLUMN = "SUBMIT_DATE_TIME";

    private static final int SUBMIT_DATE_TIME_INDEX = 7;

    private static final String TAG = EventHelper.class.getName();

    private static final String VEHICLE_INVOLVED_ID_COLUMN = "VEHICLE_INVOLVED_ID";

    private static final int VEHICLE_INVOLVED_ID_INDEX = 11;

    // private static final String EVENT_LIST_VIEW_NAME = "EVENT_LIST_VIEW";

    /**
     * The name of the id column in the Event table
     */
    protected static final String ID_COLUMN = "_id";

    /**
     * The name of the Event table
     */
    protected static final String TABLE_NAME = "EVENT";

    /**
     * Converts an Event to a set of ContentValues
     * 
     * @param event
     *            the event to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(Event event)
    {
        // Log.v(TAG, ">>> buildContentValues(Event event)");

        ContentValues values = new ContentValues();
        values.put(EVENT_STATUS_COLUMN, event.getEventStatus());

        Timestamp eventTime = event.getEventDateTime();
        if (eventTime != null)
        {
            values.put(INCIDENT_DATE_TIME_COLUMN, eventTime.getTime());
        }
        values.put(NOTES_COLUMN, event.getNotes());
        values.put(EVENT_SUB_TYPE_COLUMN, event.getEventSubType());
        values.put(EVENT_SUB_TYPE_DETAILS_COLUMN, event.getEventSubTypeDetails());
        values.put(EVENT_TYPE_COLUMN, event.getEventType());
        values.put(CREATE_DATE_TIME_COLUMN, event.getCreateDateTime().getTime());
        values.put(CLAIM_NUMBER_COLUMN, event.getClaimNumber());

        if (event.isAnyoneInjured())
        {
            values.put(ANYONE_INJURED_COLUMN, 1);
        }
        else
        {
            values.put(ANYONE_INJURED_COLUMN, 0);
        }

        Vehicle vehicle = event.getVehicleInvolved();
        if (vehicle != null)
        {
            values.put(VEHICLE_INVOLVED_ID_COLUMN, event.getVehicleInvolved().getId());
        }
        else
        {
            values.put(VEHICLE_INVOLVED_ID_COLUMN, "");
        }

        Timestamp submitTime = event.getSubmitDateTime();
        if (submitTime != null)
        {
            values.put(SUBMIT_DATE_TIME_COLUMN, submitTime.getTime());
        }
        values.put(SORT_VALUE_COLUMN, event.getSortValue());

        Address address = event.getLocation();
        if (address != null)
        {
            values.put(INCIDENT_LOCATION_ID_COLUMN, address.getId());
        }

        User user = event.getSubmittedUser();
        if (user != null)
        {
            values.put(SUBMITTED_USER_ID_COLUMN, user.getId());
        }

        // Log.v(TAG, ">>> buildContentValues(Event event)");

        return values;
    }

    /**
     * Converts a result set to an Event object
     * 
     * @param cursor
     *            the result set to convert
     * @return the convert Event
     */
    private static Event buildEvent(Context context, Cursor cursor, boolean complete)
    {
        // Log.v(TAG, ">>> buildEvent(Cursor cursor)");

        Event event = new Event();

        event.setCreateDateTime(new Timestamp(cursor.getLong(CREATE_DATE_TIME_INDEX)));
        event.setEventStatus(cursor.getString(EVENT_STATUS_INDEX));
        event.setId(cursor.getLong(ID_INDEX));
        event.setEventSubType(cursor.getString(EVENT_SUB_TYPE_INDEX));
        event.setEventSubTypeDetails(cursor.getString(EVENT_SUB_TYPE_DETAILS_INDEX));
        event.setEventType(cursor.getString(EVENT_TYPE_INDEX));
        event.setClaimNumber(cursor.getString(CLAIM_NUMBER_INDEX));

        Long submitDateTime = cursor.getLong(SUBMIT_DATE_TIME_INDEX);
        if (submitDateTime != null && submitDateTime > 0)
        {
            event.setSubmitDateTime(new Timestamp(submitDateTime));
        }

        if (cursor.getInt(ANYONE_INJURED_INDEX) > 0)
        {
            event.setAnyoneInjured(true);
        }
        else
        {
            event.setAnyoneInjured(false);
        }

        Long eventDateTime = cursor.getLong(INCIDENT_DATE_TIME_INDEX);
        if (eventDateTime != null && eventDateTime > 0)
        {
            event.setEventDateTime(new Timestamp(eventDateTime));
        }

        if (complete)
        {
            completeLoad(context, cursor, event);
        }
        // Log.v(TAG, "<<< buildEvent(Cursor cursor)");

        return event;
    }

    /**
     * This method will load all of the children of an event.
     * 
     * @param context
     * @param cursor
     * @param event
     */
    private static void completeLoad(Context context, Cursor cursor, Event event)
    {
        event.setNotes(cursor.getString(NOTES_INDEX));

        event.setSortValue(cursor.getInt(SORT_VALUE_INDEX));

        // Get the associated Address
        Address address = AddressHelper.get(context, cursor.getLong(INCIDENT_LOCATION_ID_INDEX));
        event.setLocation(address);

        event.setWitnesses(WitnessHelper.getByEvent(context, event.getId().longValue()));

        event.setPoliceInfos(BoletimOcorrenciaHelper.getByEvent(context, event.getId().longValue()));

        event.setDrivers(DriverHelper.getByEvent(context, event.getId().longValue()));

        event.setPhotos(EventPhotoHelper.getByEvent(context, event.getId().longValue()));

        // Get the associated Vehicle
        Vehicle vehicle = VehicleHelper.get(context, cursor.getLong(VEHICLE_INVOLVED_ID_INDEX));
        event.setVehicleInvolved(vehicle);

        User submittedUser = UserHelper.get(context, cursor.getLong(SUBMITTED_USER_ID_INDEX));
        event.setSubmittedUser(submittedUser);
    }

    /**
     * Deletes an Event from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the Event to delete
     */
    public static void delete(Context context, long id)
    {
        // Log.v(TAG, ">>> delete(Context context, Event event)");

        removeFilesForEvent(context, id);
        String whereClause = ID_COLUMN + "=" + id;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.delete(TABLE_NAME, whereClause);
        databaseHelper.close();

        // Log.v(TAG, "<<< delete(Context context, Event event)");
    }

    /**
     * Gets the Event with the given id from the database
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the record to return
     * @return
     */
    public static Event get(Context context, long id)
    {
        // Log.v(TAG, ">>> get(Context context, long id)");

        Event event = null;

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

                event = buildEvent(context, cursor, true);
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Event with id: " + id, e);
            }
        }

        // Log.v(TAG, "<<< get(Context context, long id)");

        return event;
    }

    /**
     * Gets all Events from the database
     * 
     * @param context
     *            the context within which to work
     * @return
     */
    public static ArrayList<Event> getAll(Context context)
    {
        // Log.v(TAG, ">>> getAll(Context context)");

        ArrayList<Event> events = new ArrayList<Event>();

        try
        {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, null, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Get the info for each event returned
                while (cursor.moveToNext())
                {
                    Event event = new Event();

                    event = buildEvent(context, cursor, true);

                    events.add(event);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting all Events", e);
            }
        }

        // Log.v(TAG, "<<< getAll(Context context)");

        return events;
    }

    /**
     * Gets all Events from the database needed for the Claim List. These events are only popuplated with the fields
     * needed to display on the claims list page.
     * 
     * @param context
     *            the context within which to work
     * @return
     */
    public static ArrayList<Event> getAllForList(Context context)
    {
        // Log.v(TAG, ">>> getAll(Context context)");

        ArrayList<Event> events = new ArrayList<Event>();

        try
        {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(true, TABLE_NAME, null, null, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Get the info for each event returned
                while (cursor.moveToNext())
                {
                    Event event = new Event();

                    event = buildEvent(context, cursor, false);
                    Address address = AddressHelper.get(context, cursor.getLong(INCIDENT_LOCATION_ID_INDEX));
                    event.setLocation(address);
                    events.add(event);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG.substring(TAG.length() - 23 ), Log.ERROR))
            {
                Log.e(TAG, "Error getting all Events", e);
            }
        }

        // Log.v(TAG, "<<< getAll(Context context)");

        return events;
    }

    /**
     * Provides the querty string necessary to creat the Event table
     * 
     * @return the SQL string necessary to create the User table
     */
    protected static String getCreateTableSQL()
    {
        // Log.v(TAG, ">>> getCreateTableSQL()");

		String createTableSQL 
			= "CREATE TABLE " + TABLE_NAME + " (" 
												+ ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
												+ CREATE_DATE_TIME_COLUMN + " INTEGER, " 
												+ EVENT_STATUS_COLUMN + " TEXT, " 
												+ EVENT_SUB_TYPE_COLUMN + " TEXT, "
												+ EVENT_TYPE_COLUMN + " TEXT, " 
												+ INCIDENT_DATE_TIME_COLUMN + " INTEGER, " 
												+ NOTES_COLUMN + " BLOB, "
												+ SUBMIT_DATE_TIME_COLUMN + " INTEGER, " 
												+ SORT_VALUE_COLUMN + " INTEGER, " 
												+ INCIDENT_LOCATION_ID_COLUMN + " INTEGER REFERENCES " + AddressHelper.TABLE_NAME + "(" + AddressHelper.ID_COLUMN + "), "
												+ EVENT_SUB_TYPE_DETAILS_COLUMN + " TEXT, "
												+ VEHICLE_INVOLVED_ID_COLUMN + " INTEGER REFERENCES " + VehicleHelper.TABLE_NAME + "(" + VehicleHelper.ID_COLUMN + "), " 
												+ ANYONE_INJURED_COLUMN + " INTEGER, "
												+ SUBMITTED_USER_ID_COLUMN + " INTEGER REFERENCES " + UserHelper.TABLE_NAME + "(" + UserHelper.ID_COLUMN + "), "
												+ CLAIM_NUMBER_COLUMN + " TEXT)";

        // Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }

    /**
     * Provides the query string necessary to create the triggers for the Event table which will ensure that when an
     * Event is deleted, the corresponding Address, Drivers, EventPhotos, PoliceOfficers, PoliceReports, and Witnesses
     * will also be deleted.
     * 
     * @return the SQL string necessary to create the triggers for the Event table
     */
    protected static String getCreateTriggersSQL()
    {
        // Log.v(TAG, ">>> getCreateTriggersSQL()");

        String createTriggerSQL =
            "CREATE TRIGGER DELETE_EVENT BEFORE DELETE ON " + TABLE_NAME + " FOR EACH ROW BEGIN " + "DELETE FROM "
                + AddressHelper.TABLE_NAME + " WHERE " + AddressHelper.ID_COLUMN + " = old."
                + INCIDENT_LOCATION_ID_COLUMN + "; " + "DELETE FROM " + DriverHelper.TABLE_NAME + " WHERE "
                + DriverHelper.EVENT_ID_COLUMN + " = old." + ID_COLUMN + "; " + "DELETE FROM "
                + EventPhotoHelper.TABLE_NAME + " WHERE " + EventPhotoHelper.EVENT_ID_COLUMN + " = old." + ID_COLUMN
                + "; " + "DELETE FROM " + BoletimOcorrenciaHelper.TABLE_NAME + " WHERE "
                + BoletimOcorrenciaHelper.EVENT_ID_COLUMN + " = old." + ID_COLUMN + "; " + "DELETE FROM "
                + WitnessHelper.TABLE_NAME + " WHERE " + WitnessHelper.EVENT_ID_COLUMN + " = old." + ID_COLUMN + "; "
                + "DELETE FROM " + VoiceNoteHelper.TABLE_NAME + " WHERE " + VoiceNoteHelper.EVENT_ID_COLUMN + " = old."
                + ID_COLUMN + "; " + "DELETE FROM " + VehicleHelper.TABLE_NAME + " WHERE " + VehicleHelper.ID_COLUMN
                + " = old." + VEHICLE_INVOLVED_ID_COLUMN + "; " + "DELETE FROM " + ContactHelper.TABLE_NAME + " WHERE "
                + ContactHelper.ID_COLUMN + " = old." + SUBMITTED_USER_ID_COLUMN + "; END";

        // Log.v(TAG, "<<< getCreateTriggersSQL()");

        return createTriggerSQL;
    }

    /**
     * Inserts the Event into the database
     * 
     * @param context
     *            the context within which to work
     * @param event
     *            the event to add to the database
     * @return the id of the new row in the database
     */
    public static long insert(Context context, Event event)
    {
        // Log.v(TAG, ">>> insert(Context context, Event event)");

        // First insert the associated address
        Address address = event.getLocation();
        if (address != null)
        {
            long id = AddressHelper.insert(context, address);
            address.setId(id);
        }

        // set the create date time to now
        Date now = new Date();
        Timestamp createTime = new Timestamp(now.getTime());
        event.setCreateDateTime(createTime);

        ContentValues values = buildContentValues(event);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        // Log.v(TAG, "<<< insert(Context context, Event event)");

        return id;
    }

    /**
     * Inserts or updates a address associated with an event
     * 
     * @param context
     *            the context within which to work
     * @param event
     *            the event to add the address to
     */
    private static void insertOrUpdateAddress(Context context, Event event)
    {
        Address address = event.getLocation();
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
     * Inserts or updates a vehicles associated with an event
     * 
     * @param context
     *            the context within which to work
     * @param event
     *            the event to add the vehicle to
     */
    private static void insertOrUpdateVehicle(Context context, Event event)
    {
        Vehicle vehicle = event.getVehicleInvolved();
        if (vehicle != null)
        {
            Long vehicleId = vehicle.getId();
            if (vehicleId != null && vehicleId > 0)
            {
                VehicleHelper.update(context, vehicle);
            }
            else
            {
                vehicleId = VehicleHelper.insert(context, vehicle, null);
                vehicle.setId(vehicleId);
            }
        }
    }

    /**
     * This method will remove any activity_pictures or voice activity_notes that have been saved as part of this claim.
     * 
     * @param id
     *            the event_id from which to remove the files
     */
    public static void removeFilesForEvent(Context context, long id)
    {
        VoiceNoteHelper.removeVoiceNotesForEvent(context, id);
        EventPhotoHelper.removePhotosForEvent(context, id);
    }

    /**
     * Updates an existing Event in the database
     * 
     * @param context
     *            the context within which to work
     * @param event
     *            the event data to update
     */
    public static void update(Context context, Event event)
    {
        // Log.v(TAG, ">>> update(Context context, Event event)");

        // First insert or update the associated activity_location
        insertOrUpdateAddress(context, event);

        // Then insert or update the associated contact
        // insertOrUpdateContact(context, event);

        // Then insert or update the associated vehicle
        insertOrUpdateVehicle(context, event);

        ContentValues values = buildContentValues(event);

        String whereClause = ID_COLUMN + "=" + event.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        // Log.v(TAG, "<<< update(Context context, Event event)");
    }

    // /**
    // * Provides query string for view to enable better performance on the Claim List page.
    // *
    // * @return query string
    // */
    // protected static String getCreateViewSQL()
    // {
    // String createViewSQL =
    // "create view " + EVENT_LIST_VIEW_NAME + " as (select e." + ID_COLUMN + ", e." + EVENT_TYPE_COLUMN + ", e."
    // + EVENT_SUB_TYPE_COLUMN + ", e." + EVENT_STATUS_COLUMN + ", e." + INCIDENT_DATE_TIME_COLUMN + ", a."
    // + AddressHelper.getCityColumn() + ", a." + AddressHelper.getStateColumn() + " from " + TABLE_NAME
    // + " e, " + AddressHelper.TABLE_NAME + " a where a." + ID_COLUMN + " = e." + INCIDENT_LOCATION_ID_COLUMN
    // + ")";
    //
    // return createViewSQL;
    //
    // }

    /**
     * Private constructor to prevent instantiation
     */
    private EventHelper()
    {
    }
}
