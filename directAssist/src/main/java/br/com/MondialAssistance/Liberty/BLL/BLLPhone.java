/*
 * DirectAssist for Android
 * 
 * Created by Danilo de Souza Salvador on 12/2011
 * Copyright 2012 Mondial Assistance. All rights reserved
 * 
 * */

package br.com.MondialAssistance.Liberty.BLL;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.MondialAssistance.Liberty.Util.DBHelper;
import br.com.MondialAssistance.Liberty.Util.ErrorHelper;

public class BLLPhone {

	private DBHelper db;
	private Context context;
	private SQLiteDatabase sql;
	
	public BLLPhone(Context applicationContext) {

		context = applicationContext;
	}
	
	public String getPhone() throws Exception {

		String phoneNumber = null;
		
		try {
			db = new DBHelper(context);
			sql = db.getReadableDatabase(); 
			 
			String[] colunas = {"phone_number"};
			Cursor cursor = sql.query("phone", colunas, null, null, null, null, null);
			
			if (cursor.getCount() == 0){
				
				phoneNumber = null;
				
			} else {
	
				int idxColPhoneNumber = cursor.getColumnIndex("phone_number");
				cursor.moveToFirst();
				
				phoneNumber = cursor.getString(idxColPhoneNumber);
			}
			cursor.close();
			
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		} finally {
			if (sql != null)
				sql.close();
		}
		return phoneNumber;
	}
	
	public boolean getReadMessageModifyPhone() throws Exception {
		
		boolean readMessageModifyPhone = false;
		
		try {
			db = new DBHelper(context);
			sql = db.getReadableDatabase(); 
			 
			String[] colunas = {"read_msg_modify_phone"};
			Cursor cursor = sql.query("phone", colunas, null, null, null, null, null);
			
			if (cursor.getCount() != 0){
				
				int idxReadMsgModifyPhone = cursor.getColumnIndex("read_msg_modify_phone");
				cursor.moveToFirst();
				
				readMessageModifyPhone = (cursor.getInt(idxReadMsgModifyPhone) == 1);
			}
			cursor.close();
			
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		} finally {
			if (sql != null)
				sql.close();
		}
		
		return readMessageModifyPhone;
	}
	
	public boolean modifyPhoneNumberDigit9(String phoneNumber) {
		
		String areaCodeList[] = new String[] { "11" };
		String areaCode = phoneNumber.substring(1, 3);
		
		boolean existAreaCodeList = false;
		
		for (String ddd : areaCodeList) {
			
			if (areaCode.equals(ddd)) {
				existAreaCodeList = true;
				break;
			}
		}
		
		if (existAreaCodeList) {
			
			String phone = phoneNumber.substring(5).trim().replace("-", "");
			
			if (phone.length() <= 9)
				return true;
		}
		return false;
	}
	
	public void SavePhone(String phoneNumber, boolean readMsgModifyPhone) throws Exception{
		
		try {
			String phone = getPhone();
			
			db = new DBHelper(context);
			sql = db.getReadableDatabase(); 
			
			ContentValues values = new ContentValues();
			values.put("phone_number", phoneNumber);
			values.put("read_msg_modify_phone", readMsgModifyPhone == true ? "1" : "0");
			
			if (phone == null){
				sql.insert("phone", null, values);
			} else {
				sql.update("phone", values, null, null);
			}
		
		} catch (Exception e) {
			throw ErrorHelper.setErrorMessage(e);
		} finally {
			if (sql != null)
				sql.close();	
		}
	}
	
	public class PhoneNumberMask {
		
		private ArrayList<String> numbers;
		private int position;
		
		public PhoneNumberMask() {
			
			numbers = new ArrayList<String>();
		}
		
		public String LoadPhoneNumber(String phoneNumber) {
			
			if (phoneNumber == null){
				
				position = 0;
				
				numbers.add("("); //  0 <
				numbers.add(" "); //  1
				numbers.add(" "); //  2
				numbers.add(")"); //  3 <
				numbers.add(" "); //  4 <
				numbers.add(" "); //  5
				numbers.add(" "); //  6
				numbers.add(" "); //  7
				numbers.add(" "); //  8
				numbers.add("-"); //  9 <
				numbers.add(" "); // 10
				numbers.add(" "); // 11
				numbers.add(" "); // 12
				numbers.add(" "); // 13
				numbers.add(" "); // 14 (opção para o nono digito)
								
			} else {
				
				for (int i = 0; i < phoneNumber.length(); i++) {
					
					String caracter = String.valueOf(phoneNumber.charAt(i));
					numbers.add(caracter);
					
					if (i != 14 || 
					   (i == 14 && !caracter.equals(" ")))
						position = (numbers.size() - 1);
				}
				
				if ((numbers.size() - 1) < 14)
					numbers.add(" "); // 14 (opção para o nono digito)
			}
			
			return getTextAll();
		}
		
		public String addNumber(String number) {
			
			if ((position) < numbers.size() - 1){
				
				morePosition();
				numbers.set(position, number);
				
				return getText();
			} else
				return getTextAll();
		}
		
		public String lessNumber() {
			
			if (position > 0){
				
				numbers.set(position, " ");
				lessPosition();
				
				return getText();
			} else
				return getTextAll();
		}
		
		private String getText(){
			
			String number = ""; 
			
			if (position == (numbers.size() - 1)) {
				
				numbers.set(9, numbers.get(10));
				numbers.set(10, "-");
				
			} else if (numbers.get(10).equals("-")) {
				
				numbers.set(10, numbers.get(9));
				numbers.set(9, "-");
			}
			
			for (String num : numbers) {
				
				number += num;
			}
			
			return number;
		}
		
		private String getTextAll(){
			
			String number = ""; 
			
			for (String num : numbers) {
				
				number += num;
			}

			return number;
		}		
		
		private void morePosition(){
			
			position++;
			
			if (position == 3 ||
				position == 4 ||
				position == 9)
				morePosition();
		}
		
		private void lessPosition(){
			
			position--;
			
			if (position == 3 ||
		        position == 4 ||
			    position == 9)
			    lessPosition();
		}
		
		public boolean ValidatePhoneNumber(boolean readMsgModifyPhone){
			boolean result = true;
			int pos = 0;
			
			for (String num : numbers) {
				
				if (!readMsgModifyPhone) {
				
					if (pos == 14 && !num.equals(" "))
						return false;
					else if ((pos != 0 &&
						 pos != 3 &&
						 pos != 4 &&
						 pos != 9 &&
						 pos != 14) &&
					    num.equals(" ")) {
						return false;
					}
				} else {
					if ((pos != 0 &&
						 pos != 3 &&
						 pos != 4 &&
						 pos != 9) &&
					    num.equals(" ")) {
						return false;
						}
				}
				pos++;
			}
			return result;
		}
	}
}