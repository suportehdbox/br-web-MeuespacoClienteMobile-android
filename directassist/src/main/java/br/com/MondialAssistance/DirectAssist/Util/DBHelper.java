/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.DirectAssist.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private static final String DATABASE = "DIRECTASSIST";
	private static final int VERSION = 2;
	
	public DBHelper(Context context) {
		super(context, DATABASE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String TablePhone = "CREATE TABLE phone (phone_number VARCHAR PRIMARY KEY, read_msg_modify_phone INT(1) DEFAULT 0);";
		db.execSQL(TablePhone);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//String TablePhone = "DROP TABLE phone";
		String TablePhone = "ALTER TABLE phone ADD read_msg_modify_phone INT(1) DEFAULT 0;";
		db.execSQL(TablePhone);
		//onCreate(db);
	}
}
