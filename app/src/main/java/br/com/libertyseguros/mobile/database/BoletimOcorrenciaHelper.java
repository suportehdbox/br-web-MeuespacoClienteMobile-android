/**
 * LIBERTY SEGUROS SA
 */
package br.com.libertyseguros.mobile.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import br.com.libertyseguros.mobile.model.BoletimDeOcorrencia;

/**
 * Fornece a funcionalidade para criar a tabela BoletimDeOcorrencia e criar, ler, atualizar e excluir 
 * objetos BoletimDeOcorrencia do banco de dados.
 * 
 * @author Evandro
 */
public class BoletimOcorrenciaHelper {

	private static final String TAG = BoletimOcorrenciaHelper.class.getName().substring(0, 23);
	
    private static final String ID_COLUMN = "_id";

    private static final int ID_INDEX = 0;
    
    private static final String ENTIDADE_COLUMN = "ENTIDADE";

    private static final int ENTIDADE_INDEX = 1;
    
    private static final String LOCALIDADE_COLUMN = "LOCALIDADE";
    
    private static final int LOCALIDADE_INDEX = 2;
    
    private static final String NOTAS_COLUMN = "NOTAS";
    
    private static final int NOTAS_INDEX = 3;

    /**
     * The name of the event id column in the BoletimDeOcorrencia table
     */
    protected static final String EVENT_ID_COLUMN = "EVENT_ID";

    /**
     * O nome da tabela de BO.
     */
    protected static final String TABLE_NAME = "BOLETIM_OCORRENCIA";
    
    /**
     * Deleta o BoletimDeOcorrencia da base de dados.
     * 
     * @param context
     *            the context within which to work
     * @param id
     *            the id of the BoletimDeOcorrencia to delete
     */
    public static void delete(Context context, long id)
    {
        //Log.v(TAG, ">>> delete(Context context, long id)");

        // then delete the BoletimDeOcorrencia
        String whereClause = ID_COLUMN + "=" + id;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.delete(TABLE_NAME, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< delete(Context context, long id)");
    }
    
    /**
     * Converts a result set to a BoletimDeOcorrencia object
     * 
     * @param cursor
     *            the result set to convert
     * @return the converted BoletimDeOcorrencia
     */
    private static BoletimDeOcorrencia buildBoletimDeOcorrencia(Cursor cursor)
    {
        //Log.v(TAG, ">>> buildPoliceInformation(Cursor cursor)");

    	BoletimDeOcorrencia boletimDeOcorrencia = new BoletimDeOcorrencia();
        boletimDeOcorrencia.setId(cursor.getLong(ID_INDEX));
        boletimDeOcorrencia.setEntidade(cursor.getString(ENTIDADE_INDEX));
        boletimDeOcorrencia.setLocalidade(cursor.getString(LOCALIDADE_INDEX));
        boletimDeOcorrencia.setNotas(cursor.getString(NOTAS_INDEX));

        //Log.v(TAG, "<<< buildPoliceInformation(Cursor cursor)");

        return boletimDeOcorrencia;
    }
    
    /**
     * Builds a PoliceInformation object from the results of the database
     * 
     * @param context
     *            the context in which to work
     * @param cursor
     *            the result set to convert
     * @return
     */
    private static BoletimDeOcorrencia createBoletimDeOcorrenciaObject(Context context, Cursor cursor) {
        //Log.v(TAG, ">>> createPoliceInformationObject(Context context, Cursor cursor)");

    	BoletimDeOcorrencia boletimDeOcorrencia;
        // build the policeInformation specific contact info
        boletimDeOcorrencia = buildBoletimDeOcorrencia(cursor);

        //Log.v(TAG, "<<< createPoliceInformationObject(Context context, Cursor cursor)");

        return boletimDeOcorrencia;
    }
    
    /**
     * Gets a BoletimDeOcorrencia from the database
     * 
     * @param context
     *            the context within which to work
     * @return the BoletimDeOcorrencia or null if no BoletimDeOcorrencia was found
     */
    public static BoletimDeOcorrencia get(Context context, long id)
    {
        //Log.v(TAG, ">>> get(Context context, long id)");

    	BoletimDeOcorrencia boletimDeOcorrencia = null;

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

                boletimDeOcorrencia = createBoletimDeOcorrenciaObject(context, cursor);
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Boletim De Ocorrencia with id: " + id, e);
            }
        }

        //Log.v(TAG, "<<< get(Context context, long id)");

        return boletimDeOcorrencia;
    }

    /**
     * Gets a list boletimDeOcorrencia from the database which belong to the given event
     * 
     * @param context
     *            the context within which to work
     * @param eventId
     *            the event for which to search for boletins DeOcorrencia
     * @return the BO's or an empty array if no boletimDeOcorrencia was found
     */
    public static ArrayList<BoletimDeOcorrencia> getByEvent(Context context, long eventId)
    {
        //Log.v(TAG, ">>> getByEvent(Context context, long eventId)");

        ArrayList<BoletimDeOcorrencia> boletimDeOcorrenciaList = new ArrayList<BoletimDeOcorrencia>();

        try
        {
            String whereClause = EVENT_ID_COLUMN + "=" + eventId;

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.open();
            Cursor cursor = databaseHelper.get(false, TABLE_NAME, null, whereClause, null);

            if (cursor != null && cursor.getCount() > 0)
            {
                // Build the list of boletimDeOcorrencia object for each boletimDeOcorrencia returned
                while (cursor.moveToNext())
                {
                	BoletimDeOcorrencia boletimDeOcorrencia = createBoletimDeOcorrenciaObject(context, cursor);
                    boletimDeOcorrenciaList.add(boletimDeOcorrencia);
                }
            }

            cursor.close();
            databaseHelper.close();
        }
        catch (SQLException e)
        {
            if (Log.isLoggable(TAG, Log.ERROR))
            {
                Log.e(TAG, "Error getting Boletim De Ocorrencia with Event: " + eventId, e);
            }
        }

        //Log.v(TAG, "<<< getByEvent(Context context, long eventId)");

        return boletimDeOcorrenciaList;
    }
    
    /**
     * Converts a boletimDeOcorrencia to a set of ContentValues
     * 
     * @param boletimDeOcorrencia
     *            the boletimDeOcorrencia to convert
     * @return the converted content values
     */
    private static ContentValues buildContentValues(BoletimDeOcorrencia boletimDeOcorrencia, Long eventId)
    {
        //Log.v(TAG, ">>> buildContentValues(BoletimDeOcorrencia boletimDeOcorrencia, Long eventId)");

        ContentValues values = new ContentValues();

        if (eventId != null)
        {
            values.put(EVENT_ID_COLUMN, eventId);
        }

        values.put(ENTIDADE_COLUMN, boletimDeOcorrencia.getEntidade());
        values.put(LOCALIDADE_COLUMN, boletimDeOcorrencia.getLocalidade());
        values.put(NOTAS_COLUMN, boletimDeOcorrencia.getNotas());

        //Log.v(TAG, "<<< buildContentValues(BoletimDeOcorrencia boletimDeOcorrencia, Long eventId)");

        return values;
    }
    
    /**
     * Inserts a boletimDeOcorrencia into the database
     * 
     * @param context
     *            the context within which to work
     * @param boletimDeOcorrencia
     *            the boletimDeOcorrencia to add to the database
     * @return the id of the new row in the database
     */
    public static long insert(Context context, BoletimDeOcorrencia boletimDeOcorrencia, Long eventId)
    {
        //Log.v(TAG, ">>> insert(Context context, BoletimDeOcorrencia boletimDeOcorrencia, Long eventId)");

        // Then insert the boletimDeOcorrencia
        ContentValues values = buildContentValues(boletimDeOcorrencia, eventId);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        long id = databaseHelper.insert(TABLE_NAME, values);
        databaseHelper.close();

        //Log.v(TAG, "<<< insert(Context context, BoletimDeOcorrencia boletimDeOcorrencia, Long eventId)");

        return id;
    }
    
    /**
     * Provides the query to create the boletimDeOcorrencia Table
     * 
     * @return the SQL string necessary to create the PoliceInformation table
     */
    protected static String getCreateTableSQL()
    {
        //Log.v(TAG, ">>> getCreateTableSQL()");

        String createTableSQL =
        		"CREATE TABLE " + TABLE_NAME + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        											+ ENTIDADE_COLUMN + " TEXT, " 
    												+ LOCALIDADE_COLUMN + " TEXT, " 
    												+ NOTAS_COLUMN + " TEXT, " 
        											+ EVENT_ID_COLUMN + " INTEGER REFERENCES " 
    												+ EventHelper.TABLE_NAME + "(" + EventHelper.ID_COLUMN + ") NOT NULL)";

        //Log.v(TAG, "<<< getCreateTableSQL()");

        return createTableSQL;
    }
    

    /**
     * Updates an existing BoletimDeOcorrencia in the database
     * 
     * @param context
     *            the context within which to work
     * @param boletimDeOcorrencia
     *            the boletimDeOcorrencia data to update
     */
    public static void update(Context context, BoletimDeOcorrencia boletimDeOcorrencia)
    {
        //Log.v(TAG, ">>> update(Context context, PoliceInformation policeInformation)");

        // Then update the BoletimDeOcorrencia
        ContentValues values = buildContentValues(boletimDeOcorrencia, null);
        String whereClause = ID_COLUMN + "=" + boletimDeOcorrencia.getId();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        databaseHelper.update(TABLE_NAME, values, whereClause);
        databaseHelper.close();

        //Log.v(TAG, "<<< update(Context context, PoliceInformation policeInformation)");
    }
    
	/**
     * Private constructor to prevent instantiation
     */
    private BoletimOcorrenciaHelper()
    {
    }
}
