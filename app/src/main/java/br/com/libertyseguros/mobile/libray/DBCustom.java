package br.com.libertyseguros.mobile.libray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.util.OnResultQuery;

public class DBCustom extends SQLiteOpenHelper {
	private final Context context;

	private SQLiteDatabase myDataBase;

	public static String DB_PATH;

	public static final String DATABASENAME = BuildConfig.databasename;


	public OnResultQuery onResultQuery;

	/**
	 * Method Construtor
	 * @param context
	 */
	public DBCustom(Context context) {

		super(context, DATABASENAME, null, 2);
		this.context = context;


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			DB_PATH = context.getFilesDir().getAbsolutePath().replace("files", "databases");
		}else {
			DB_PATH = context.getFilesDir().getPath().replace("files", "") + "databases";
		}

		try {
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Create new database
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {
		// TODO Auto-generated method stub
		boolean dbExist = checkDataBase();

		if (!dbExist) {

			this.getReadableDatabase();

			try {

				copyDataBaseAssets(context, DATABASENAME, DB_PATH);

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check database existence
	 * @return
	 */
	public boolean checkDataBase() {

		SQLiteDatabase checkDB = null;
		boolean retorno = false;

		/*try {
			String myPath = DB_PATH + "/" + DATABASENAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
			retorno = true;
		} catch (SQLiteException e) {
			retorno = false;
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return retorno;*/

		File dbfile = new File(DB_PATH + "/" + DATABASENAME);

		return dbfile.exists();

	}

	/**
	 * Open Database
	 * @return
	 * @throws SQLException
	 */
	public SQLiteDatabase openDataBase() throws SQLException {

		try {
			String myPath = DB_PATH + "/" + DATABASENAME;

			File dbfile = new File(myPath);
			dbfile.setWritable(true);

			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLException ex) {
                ex.printStackTrace();

		}

		return myDataBase;
	}

	/**
	 * Close database
	 */
	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();
	}

	/**
	 * Execute command sql insert
	 * @param sql
	 */
	public void insertBD(String sql) {

		openDataBase();
		myDataBase.execSQL(sql);
		myDataBase.close();
		super.close();

	}

	/**
	 * Execute command sql delete
	 * @param sql
	 */
	public void deleteBD(String sql) {

		openDataBase();
		myDataBase.execSQL(sql);
		myDataBase.close();
		super.close();

	}
	/**
	 * Execute command sql update
	 * @param sql
	 */
	public void updateBD(String sql) {

		openDataBase();
		myDataBase.execSQL(sql);
		myDataBase.close();
		super.close();

	}

	/**
	 * Execute command create table
	 * @param sql
	 */
	public void createTable(String sql) {

		openDataBase();
		myDataBase.execSQL(sql);
		myDataBase.close();
		super.close();

	}

	/**
	 * Execute command sql select
	 * @param sql
	 * @return
	 */
	public Cursor selectBD(String sql) {
		//Log.i(Config.TAG, "sql: " + sql);

		openDataBase();

		Cursor mCursor = myDataBase.rawQuery(sql, null);

		return mCursor;
	}


	/**
	 * Method onCreate Database
	 * @param db
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	/**
	 * Method onUpgrade Database
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * Copy database assets folder
	 * @param context
	 * @param DATABASE_NAME
	 * @param DB_PATH
	 * @throws IOException
	 */
	public void copyDataBaseAssets(Context context, String DATABASE_NAME,
								   String DB_PATH) throws IOException {
		try {
			String outFileName = DB_PATH + "/" + DATABASE_NAME;
			OutputStream myOutput = new FileOutputStream(outFileName);
			InputStream myInput = context.getAssets().open(DATABASE_NAME);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0)
			{
				myOutput.write(buffer, 0, length);
			}
			myInput.close();
			myOutput.flush();
			myOutput.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Update database file
	 * @return
	 */
	public boolean updateBD(Context contex) {
		boolean bUpdate = false;

		try {
			String input = "";


			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				input = context.getFilesDir().getAbsolutePath() + "/" + DATABASENAME;
			}else {
				input = context.getFilesDir().getPath() + "/" +  DATABASENAME;
			}

			InputStream bancoInput = new FileInputStream(input);

			String outFileName = DB_PATH + "/" + DATABASENAME;
			OutputStream bancoOutput = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = bancoInput.read(buffer)) > 0) {
				bancoOutput.write(buffer, 0, length);
			}

			bancoOutput.flush();
			bancoOutput.close();
			bancoOutput.close();
			bUpdate = true;
		} catch (Exception ex) {
			bUpdate = false;
		}

		return bUpdate;
	}

	public void setOnResultQuery(OnResultQuery onResultQuery){
		this.onResultQuery = onResultQuery;
	}

}
