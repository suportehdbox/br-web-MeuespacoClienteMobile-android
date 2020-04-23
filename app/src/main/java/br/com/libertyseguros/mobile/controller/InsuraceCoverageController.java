package br.com.libertyseguros.mobile.controller;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.InsuranceCoverageAdapater;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.PolicyBeansV2;
import br.com.libertyseguros.mobile.model.BaseModel;
import br.com.libertyseguros.mobile.model.ClubModel;
import br.com.libertyseguros.mobile.model.InsuranceCoverageModel;
import br.com.libertyseguros.mobile.util.OnConnectionResult;

public class InsuraceCoverageController extends BaseModel{

    private InsuranceCoverageModel insuranceCoverageModel;
    private int displayingCoverageItem = 0;
    private OnItemChangeListener listener;
    public InsuraceCoverageController(OnItemChangeListener listener){
        insuranceCoverageModel = new InsuranceCoverageModel();
        this.listener = listener;
    }

    /**
     * Prepare Array ListView
     * @param context
     * @param type
     * @return InsuranceCoverageAdapater
     */
    public InsuranceCoverageAdapater prepareListData(Context context, int type) {

        return insuranceCoverageModel.prepareListData(context, type, displayingCoverageItem);
    }

    public String getTitleList(){
        return InsuranceCoverageModel.getItens()[displayingCoverageItem].getDescription();
    }
    public int getNumberItems(){
        return InsuranceCoverageModel.getItens().length;
    }
    public void displayItensList(Context context){
        if(InsuranceCoverageModel.getItens().length <= 1){
            return;
        }

        String[] items = new String[InsuranceCoverageModel.getItens().length];
        int ind = 0;
        for ( PolicyBeansV2.PolicyItem item :InsuranceCoverageModel.getItens()) {
            items[ind] = item.getDescription();
            ind++;
        }
        final Context cnt = context;
        AlertDialog.Builder build = new AlertDialog.Builder(cnt);
        build.setTitle(cnt.getString(R.string.insurance_coverage_title_popup));
        AlertDialog dialog = build.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                displayingCoverageItem = which;
                listener.ItemChanged();
            }
        }).create();


        dialog.getListView().setDivider(new ColorDrawable(cnt.getResources().getColor(R.color.cardview_light_background))); // set color
        dialog.getListView().setDividerHeight(5); // set height
        dialog.show();
    }

    public interface OnItemChangeListener{
        void ItemChanged();
    }
}

