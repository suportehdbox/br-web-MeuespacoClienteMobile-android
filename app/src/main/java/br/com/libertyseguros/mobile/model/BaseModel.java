package br.com.libertyseguros.mobile.model;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.libray.LoadFile;


public class BaseModel {
    public LoadFile loadFile;

    public Gson gson;

    public InfoUser infoUser;

    public BaseModel(){
        loadFile = new LoadFile();

        gson = new Gson();

        infoUser = new InfoUser();
    }


    public boolean verifyConnection(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        if(cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isAvailable() &&
                cm.getActiveNetworkInfo().isConnected()){
            return true;
        } else {
            return false;
        }
    }

    public String getMD5(Context context){
        PackageInfo info;
        try {

            info = context.getPackageManager().getPackageInfo(
                    "br.com.libertyseguros.mobile", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //Log.e("Hash key", something);
                System.out.println("Hash key" + something);
            }

        } catch (PackageManager.NameNotFoundException e1) {
            //Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            //Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            //Log.e("exception", e.toString());
        }

        return "";
    }

    /**
     * Get Postal from latitude and longitude
     * @param latLng
     * @return
     */
    public String getAddress(LatLng latLng, Context context){
        String value = "";

        try{
            Geocoder geocoder;
            List<Address> addresses;
            //Locale myLocale = new Locale("pt", "BR");
            geocoder = new Geocoder(context, Locale.UK);
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);

            int index = getIndexAddress(addresses, latLng.latitude, latLng.longitude);

            String city = addresses.get(index).getLocality();
            String state = addresses.get(index).getAdminArea();
            String country = addresses.get(index).getCountryName();
            String postalCode = addresses.get(index).getPostalCode();
            value = postalCode;
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return value;
    }

    /**
     * Get Postal from Address
     * @param name
     * @param context
     * @return
     */
    public String getAddress(String name, Context context){
        String value = "";

        try{
            Geocoder geocoder;
            List<Address> addresses;
            //Locale myLocale = new Locale("pt", "BR");
            geocoder = new Geocoder(context, Locale.UK);
            addresses = geocoder.getFromLocationName(name, 10);

            int index = getIndexAddressName(addresses);

            String city = addresses.get(index).getLocality();
            String state = addresses.get(index).getAdminArea();
            String country = addresses.get(index).getCountryName();
            String postalCode = addresses.get(index).getPostalCode();
            value = postalCode;
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return value;
    }

    private int getIndexAddressName(List<Address> addresses){
        List<Address> addressesPost = addresses;

        int index = 0;

        for (int ind = 0; ind < addressesPost.size(); ind++) {
            if(addressesPost.get(ind).getPostalCode() != null) {
                if (addressesPost.get(ind).getPostalCode().contains("-")) {
                    index = ind;
                    ind = addressesPost.size();
                }
            }
         }

        return index;
    }

    private int getIndexAddress(List<Address> addresses, double lat1,double lng1){
        List<Address> addressesPost = addresses;
        float dist = -1;

        int index = 0;

        for(int ind = 0; ind < addressesPost.size(); ind++){

            if(addressesPost.get(ind).getPostalCode() != null) {

                if (addressesPost.get(ind).getPostalCode().contains("-")) {
                    float distTemp = getDistanceFromCurrentPosition(lat1, lng1,
                            addressesPost.get(ind).getLatitude(), addressesPost.get(ind).getLongitude());

                    if (dist == -1) {
                        dist = distTemp;
                        index = ind;
                    } else {
                        if (dist > distTemp) {
                            dist = distTemp;
                            index = ind;
                        }
                    }
                }
            }
        }

        return index;

    }

    private float getDistanceFromCurrentPosition(double lat1,double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;

        double dLat = Math.toRadians(lat2 - lat1);

        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        int meterConversion = 1609;

        return new Float(dist * meterConversion).floatValue();

    }

    public void openCanalReport(Context context){
        String url = context.getString(R.string.url_canal_report_prod);
        if(!BuildConfig.prod){
            url = context.getString(R.string.url_canal_report_act);
        }
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
