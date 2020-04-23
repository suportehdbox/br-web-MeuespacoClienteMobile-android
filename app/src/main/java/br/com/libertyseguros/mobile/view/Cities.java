package br.com.libertyseguros.mobile.view;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ListCitiesAdapter;
import br.com.libertyseguros.mobile.controller.CitiesController;
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar;

public class Cities extends BaseActionBar {

    private CitiesController citiesController;

    private ListView lvCities;

    private LinearLayout llContent;

    private LinearLayout llLoading;

    private boolean value;

    private SearchView searchView;
    private String state;


    private ListCitiesAdapter listCitiesAdapter;

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setTitle(getString(R.string.title_action_bar_8));

        setContentView(R.layout.activity_cities);


        mFirebaseAnalytics.setCurrentScreen(Cities.this, "Cidades", null);

        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            state =   extras.getString("state");
        }



        lvCities =  (ListView) findViewById(R.id.lv_cities);
        searchView = (SearchView) findViewById(R.id.searchBar);
//        showLoading(true);

        citiesController = new CitiesController(Cities.this);

        listCitiesAdapter = new ListCitiesAdapter(Cities.this, citiesController.getNotificationBeans(state), citiesController.getClickListener());

        lvCities.setAdapter(listCitiesAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listCitiesAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listCitiesAdapter.filter(newText);
                return false;
            }
        });
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);

    }


    /* Show progress loading
    * @param v
    * @param m
    */
    private void showLoading(boolean v) {
        this.value = v;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    llLoading.setVisibility(View.VISIBLE);
                    llContent.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
