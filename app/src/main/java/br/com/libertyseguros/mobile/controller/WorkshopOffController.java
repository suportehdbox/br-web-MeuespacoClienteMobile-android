package br.com.libertyseguros.mobile.controller;


import android.app.Activity;
import android.content.Context;

import br.com.libertyseguros.mobile.model.WorkshopOffModel;

public class WorkshopOffController {

    private WorkshopOffModel workshopOffModel;

    public WorkshopOffController(Activity activity){
        workshopOffModel = new WorkshopOffModel(activity);
    }

    public void openWorkshop(Context context){
       // workshopOffModel.openWorkShop(context);
    }
}
