/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for creating, reading, updating and deleting objects from the database.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class DatabaseHelper
{
    /**
     * Helper class to manage database creation and version management.
     * 
     * @author N0053575 (Heidi Sturm)
     */
    private static class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        //private static final String TAG = DatabaseOpenHelper.class.getName();

        /**
         * Create a helper object to create, open, and/or manage a database
         * 
         * @param context
         *            the context within which to work
         */
        DatabaseOpenHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Creates the database
         * 
         * @see SQLiteOpenHelper#onCreate(SQLiteDatabase)
         */
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // Log.v(TAG, ">>> onCreate(SQLiteDatabase db)");

            // Create the database using the create table sql from each of the helper classes
            // Due to key constraints, order is important here
            db.execSQL(AddressHelper.getCreateTableSQL());
            db.execSQL(ContactHelper.getCreateTableSQL());
            db.execSQL(ContactHelper.getCreateTriggersSQL());
            db.execSQL(PolicyHelper.getCreateTableSQL());
            db.execSQL(PolicyHelper.getCreateTriggersSQL());
            db.execSQL(UserHelper.getCreateTableSQL());
            db.execSQL(UserHelper.getCreateTriggersSQL());
            db.execSQL(VehicleHelper.getCreateTableSQL());
            db.execSQL(DriverHelper.getCreateTableSQL());
            db.execSQL(DriverHelper.getCreateTriggersSQL());
            db.execSQL(EventPhotoHelper.getCreateTableSQL());
            // << EPO
//            db.execSQL(PoliceInformationHelper.getCreateTableSQL());
//            db.execSQL(PoliceInformationHelper.getCreateTriggersSQL());
            db.execSQL(BoletimOcorrenciaHelper.getCreateTableSQL());
            // >>
            db.execSQL(WitnessHelper.getCreateTableSQL());
            db.execSQL(WitnessHelper.getCreateTriggersSQL());
            db.execSQL(EventHelper.getCreateTableSQL());
            db.execSQL(EventHelper.getCreateTriggersSQL());
            db.execSQL(VoiceNoteHelper.getCreateTableSQL());
            // Log.v(TAG, "<<< onCreate(SQLiteDatabase db)");
        }

        /**
         * Upgrades the databse
         * 
         * @see SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // Log.v(TAG, ">>> onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)");

            // Add code necessary to update the database when creating a new version

            // Log.v(TAG, "<<< onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)");
        }
    }

    private static SQLiteDatabase database;

    private static final String DATABASE_NAME = "liberty_mobile_database";

    // when updating, add necessary code to onUpgrade method of DatabaseOpenHelper
    private static final int DATABASE_VERSION = 1;

    //private static final String TAG = DatabaseHelper.class.getName();

    private final Context context;

    private DatabaseOpenHelper databaseOpenHelper;

    /**
     * Constructor - takes the context to allow the database to be opened/created
     * 
     * @param ctx
     *            the Context within which to work
     */
    protected DatabaseHelper(Context ctx)
    {
        // Log.v(TAG, ">>> DatabaseHelper()");

        this.context = ctx;

        // Log.v(TAG, "<<< DatabaseHelper()");
    }

    /**
     * Close the database
     */
    protected void close()
    {
        // Log.v(TAG, ">>> close()");

        databaseOpenHelper.close();

        // Log.v(TAG, "<<< close()");
    }

    /**
     * Convenience method for deleting rows in the database
     * 
     * @param tableName
     *            the table to delete from
     * @param whereClause
     *            the criteria for deleting a row
     */
    protected void delete(String tableName, String whereClause)
    {
        // Log.v(TAG, ">>> delete(String tableName, String whereClause)");

        database.delete(tableName, whereClause, null);

        // Log.v(TAG, "<<< delete(String tableName, String whereClause)");
    }

    /**
     * Queries the database and returns the result set
     * 
     * @param distinct
     *            whether or not the rows returned should be unique
     * @param tableName
     *            the table to query
     * @param columns
     *            the columns to return
     * @param whereClause
     *            the criteria for returning a row, or null if all rows are desired
     * @param orderBy
     *            how to order the rows or null if order is not necessary
     * @return the results of the query
     */
    protected Cursor get(boolean distinct, String tableName, String[] columns, String whereClause, String orderBy)
    {
        // Log.v(TAG,
        // ">>> get(boolean distinct, String tableName, String[] columns, String whereClause, String orderBy)");

        Cursor cursor = database.query(distinct, tableName, columns, whereClause, null, null, null, orderBy, null);

        // Log.v(TAG,
        // "<<< get(boolean distinct, String tableName, String[] columns, String whereClause, String orderBy)");

        return cursor;
    }

    /**
     * Convenience method for inserting a row in the database
     * 
     * @param tableName
     *            the table to insert to
     * @param values
     *            the new values for the table
     * @return the id of the newly added row
     */
    protected long insert(String tableName, ContentValues values)
    {
        // Log.v(TAG, ">>> insert(String tableName, ContentValues values)");

        long id = database.insert(tableName, null, values);

        // Log.v(TAG, "<<< insert(String tableName, ContentValues values)");

        return id;
    }

    /**
     * Open or create the claims database.
     * 
     * @return this the database
     */
    protected DatabaseHelper open()
    {
        // Log.v(TAG, ">>> open()");

        databaseOpenHelper = new DatabaseOpenHelper(context);
        database = databaseOpenHelper.getWritableDatabase();

        // Log.v(TAG, "<<< open()");
        return this;
    }

    /**
     * Convenience method for updating rows in the database
     * 
     * @param tableName
     *            the table to update
     * @param values
     *            the new values for the table
     * @param whereClause
     *            the criteria for updating a row
     */
    protected void update(String tableName, ContentValues values, String whereClause)
    {
        // Log.v(TAG, ">>> update(String tableName, ContentValues values, String whereClause)");

        database.update(tableName, values, whereClause, null);

        // Log.v(TAG, "<<< update(String tableName, ContentValues values, String whereClause)");
    }
}
