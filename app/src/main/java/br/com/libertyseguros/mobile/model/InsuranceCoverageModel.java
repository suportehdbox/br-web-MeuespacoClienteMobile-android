    package br.com.libertyseguros.mobile.model;


import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.InsuranceCoverageAdapater;
import br.com.libertyseguros.mobile.beans.InsuranceCoverages;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.PolicyBeansV2;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.util.ValidCNPJ;
import br.com.libertyseguros.mobile.util.ValidCPF;
import br.com.libertyseguros.mobile.util.ValidEmail;
import br.com.libertyseguros.mobile.view.InsuranceCoverage;
import de.hdodenhof.circleimageview.CircleImageView;

public class InsuranceCoverageModel extends BaseModel{

    private  ArrayList<InsuranceCoverages> insuranceCoverages;

    private ArrayList<InsuranceCoverages> insuranceCoveragesTemp;

    private List<String> listDataHeader;

    private HashMap<String, List<String>> listDataChild;


    /**
     * Prepare Array ListView
     * @param context
     * @param type
     * @return InsuranceCoverageAdapater
     */
    public InsuranceCoverageAdapater prepareListData(Context context, int type, int indexCoverages) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        insuranceCoverages = new ArrayList<>(Arrays.asList(itens[indexCoverages].getInsuranceCoverages()));

        insuranceCoveragesTemp = new ArrayList<>();

        for(int ind = 0; ind < insuranceCoverages.size(); ind++){
            if(insuranceCoverages.get(ind).getType() == type){

                listDataHeader.add(insuranceCoverages.get(ind).getDescription());

                List<String> detail = new ArrayList<String>();
                detail.add(insuranceCoverages.get(ind).getDetail());

                try{
                    listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), detail);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                insuranceCoveragesTemp.add(insuranceCoverages.get(ind));
            }

        }

        InsuranceCoverageAdapater adpater = new InsuranceCoverageAdapater(context, listDataHeader, listDataChild, insuranceCoveragesTemp);

        return adpater;

    }

//
//    /**
//     * get ArrayList InsuranceCoverages
//     * @return
//     */
//    public static ArrayList<InsuranceCoverages> getInsuranceCoverages() {
//        return insuranceCoverages;
//    }
//
//    /**
//     * set ArrayList InsuranceCoverages
//     * @param insuranceCoveragesNow
//     */
//    public static void setInsuranceCoverages(ArrayList<InsuranceCoverages> insuranceCoveragesNow) {
//        insuranceCoverages = insuranceCoveragesNow;
//    }


    private static PolicyBeansV2.PolicyItem[] itens;
    public static void setPolicyItens(PolicyBeansV2.PolicyItem[] itensCoverages){
        itens = itensCoverages;
    }
    public static PolicyBeansV2.PolicyItem[] getItens() {
        return itens;
    }


}
