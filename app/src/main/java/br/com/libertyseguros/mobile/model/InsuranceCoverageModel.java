    package br.com.libertyseguros.mobile.model;


    import android.content.Context;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.HashMap;
    import java.util.List;

    import br.com.libertyseguros.mobile.adapter.InsuranceCoverageAdapater;
    import br.com.libertyseguros.mobile.beans.InsuranceCoverages;
    import br.com.libertyseguros.mobile.beans.PolicyBeansV2;

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
