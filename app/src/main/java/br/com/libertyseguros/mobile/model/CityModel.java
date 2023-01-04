package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.beans.CitiesBeans;
import br.com.libertyseguros.mobile.libray.DBCustom;

public class CityModel {
    private DBCustom dbCustom;

    private Activity context;


    public CityModel(Context ctx){
        context = (Activity) ctx;
        dbCustom = new DBCustom(context);;

    }
    public ArrayList<CitiesBeans> getCities(String state){


        Cursor mCursor = dbCustom.selectBD("Select city, citycode From city where state == '" + state + "'  ORDER BY city");
        ArrayList<CitiesBeans> arrayReturn  = new ArrayList<>();

        if(mCursor.getCount() > 0) {
            int count = 0;
            while (mCursor.moveToNext()) {

                CitiesBeans notificationBeans = new CitiesBeans();
                notificationBeans.setCityCode(mCursor.getInt(mCursor.getColumnIndex("citycode")));
                notificationBeans.setCity(mCursor.getString(mCursor.getColumnIndex("city")));
                notificationBeans.setState(state);

                arrayReturn.add(notificationBeans);

                count++;
            }

        }

        mCursor.close();

        dbCustom.close();

        return arrayReturn;
    }


    public void updateCitySelectedAndFinish(CitiesBeans city){
        VehicleAccidentModel.vasb.setCityBeans(city);
        context.finish();
    }
}
