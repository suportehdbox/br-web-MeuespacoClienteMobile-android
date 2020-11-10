package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.adapter.ListCitiesAdapter;
import br.com.libertyseguros.mobile.beans.CitiesBeans;
import br.com.libertyseguros.mobile.model.CityModel;

public class CitiesController {

    private CityModel cityModel;
    private ListCitiesAdapter.ListViewClickListener listener;
    public CitiesController(Activity activity){
        cityModel = new CityModel(activity);
    }

    /**
     * Get Cities Beans
     * @return
     */
    public ArrayList<CitiesBeans> getNotificationBeans(String state){
        return cityModel.getCities(state);
    }

    public ListCitiesAdapter.ListViewClickListener getClickListener() {
        if(listener == null) {
            listener = new ListCitiesAdapter.ListViewClickListener() {
                @Override
                public void onItemClicked(CitiesBeans city) {
                    //Log.v(("LibertySeguros", city.getCity());
                    cityModel.updateCitySelectedAndFinish(city);
                }
            };
        }
        return listener;
    }

}
