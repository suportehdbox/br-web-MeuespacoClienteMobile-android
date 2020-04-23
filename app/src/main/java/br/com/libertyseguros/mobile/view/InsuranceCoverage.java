package br.com.libertyseguros.mobile.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.InsuranceCoverageAdapater;
import br.com.libertyseguros.mobile.beans.InsuranceCoverages;
import br.com.libertyseguros.mobile.controller.InsuraceCoverageController;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;


public class InsuranceCoverage extends BaseActionBar{

    private br.com.libertyseguros.mobile.view.custom.ExpandableListViewNoScroll expListViewCoverage;

    private br.com.libertyseguros.mobile.view.custom.ExpandableListViewNoScroll expListViewService;

    private static ArrayList<InsuranceCoverages> insuranceCoverages;

    private InsuraceCoverageController insuraceCoverageController;

    private InsuranceCoverageAdapater adapterCoverage;

    private InsuranceCoverageAdapater adapterService;

    private TextView tvTitleItem;
    private LinearLayout ll_OtherItens;
    private TextView tvService;
    private TextView tvCoverage;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_insurace_coverage_header);

        setTitle(getResources().getString(R.string.title_action_bar_8));

        tvCoverage = (TextView) findViewById(R.id.tv_title_coverage);
        tvService = (TextView) findViewById(R.id.tv_title_service);

        expListViewCoverage = (br.com.libertyseguros.mobile.view.custom.ExpandableListViewNoScroll) findViewById(R.id.expand_list_coverage);

        expListViewService = (br.com.libertyseguros.mobile.view.custom.ExpandableListViewNoScroll) findViewById(R.id.expand_list_service);

        insuraceCoverageController = new InsuraceCoverageController(new InsuraceCoverageController.OnItemChangeListener() {
            @Override
            public void ItemChanged() {
                updateList();
            }
        });





        tvTitleItem = (TextView) findViewById(R.id.tv_titleItem);


        ll_OtherItens = (LinearLayout) findViewById(R.id.ll_OtherItens);
        ll_OtherItens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insuraceCoverageController.displayItensList(InsuranceCoverage.this);
            }
        });



        updateList();

        expListViewCoverage.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    expListViewCoverage.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });




        expListViewService.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    expListViewService.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });

        expListViewCoverage.setGroupIndicator(null);
        expListViewService.setGroupIndicator(null);
    }


    private void updateList(){
        //updating title of item selected
        tvTitleItem.setText(insuraceCoverageController.getTitleList());
        if(insuraceCoverageController.getNumberItems() <= 1){
            ll_OtherItens.setVisibility(View.GONE);
        }
        //Prepare List of Coverages
        adapterCoverage = insuraceCoverageController.prepareListData(this, 1);

        if(adapterCoverage.getSizeArray() < 1){
            expListViewCoverage.setVisibility(View.GONE);
            tvCoverage.setVisibility(View.GONE);
        }

        expListViewCoverage.setAdapter(adapterCoverage);

        //Prepare List of Services
        adapterService = insuraceCoverageController.prepareListData(this, 2);

        if(adapterService.getSizeArray() < 1){
            expListViewService.setVisibility(View.GONE);
            tvService.setVisibility(View.GONE);
        }

        expListViewService.setAdapter(adapterService);

    }
    public static ArrayList<InsuranceCoverages> getInsuranceCoverages() {
        return insuranceCoverages;
    }

    public static void setInsuranceCoverages(ArrayList<InsuranceCoverages> insuranceCoverages) {
        InsuranceCoverage.insuranceCoverages = insuranceCoverages;
    }
}
