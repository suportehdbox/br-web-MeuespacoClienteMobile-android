/**
 * Task que utiliza api Geocoder para obter endereço, CEP
 */
package br.com.libertyseguros.mobile.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import java.util.List;

import br.com.libertyseguros.mobile.model.AddressLocation;

/**
 * @author Evandro
 * 
 */
public class GeocodeTask extends AsyncTask<Location, Void, AddressLocation> {

//	private Context context;
	private Geocoder geocoder;
	
	private int maxResults;

	/**
	 * 
	 * @param context
	 */
	public GeocodeTask(Context context) {
//		this.context = context;
		geocoder = new Geocoder(context);
		maxResults = 10; // valor default
	}

	public void setMaxResults(int maxResults){
		this.maxResults = maxResults;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * retorna
	 */
	@Override
	protected AddressLocation doInBackground(Location... params) {

		AddressLocation addressLocation = new AddressLocation();

		try {

			Location Location = params[0];
			double latitude = Location.getLatitude();
			double longitude = Location.getLongitude();

			// Caso não consiga Location não pode pesquiar CEP
			if (latitude != 0 || longitude != 0) {

				// -- Primeira tentativa obter CEP via api geocoder:
				
				List<Address> addressList = geocoder.getFromLocation(latitude, longitude, maxResults);					
				
				for (Address addressItem : addressList) {

					StringBuilder addressFormatted = new StringBuilder();
					addressFormatted.append(addressItem.getAddressLine(0));
					addressFormatted.append(", ");
					addressFormatted.append(addressItem.getAddressLine(1));
					addressFormatted.append(null != addressItem.getAddressLine(2) ? ", "+addressItem.getAddressLine(2) : "");
					addressLocation.setAddressFormatted(addressFormatted.toString());
					
					addressLocation.setStreet(addressItem.getAddressLine(0));

					if (null != addressItem.getAdminArea()) {
						addressLocation.setCity(addressItem.getAdminArea());
					}
					
					if (null != addressItem.getPostalCode()) {
						String cep = addressItem.getPostalCode().replace("-","");
						String cepFormatted = cep + "00000000".substring(cep.length());
						addressLocation.setPostalCode(cepFormatted);
//						addressFormatted.append(", ");
//						addressFormatted.append(cepFormatted);
//						addressLocation.setAddressFormatted(addressFormatted.toString());
						break;
					}
				}
			}
				
		} catch (Exception e) {
//			Util.showException(context, e);
		}
	
		return addressLocation;
	}
	
				// -- Caso não consiga obter o CEP: segunda tentativa via HTTP lookup to the maps API
				
				/*
				
				if (null == addressLocation.getPostalCode()) {
				
					HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng="+ latitude + "," + longitude+ "&sensor=true");
					HttpClient client = new DefaultHttpClient();
					HttpResponse response;
					StringBuilder stringBuilder = new StringBuilder();

					try {
						response = client.execute(httpGet);
						HttpEntity entity = response.getEntity();
						InputStream stream = entity.getContent();
						int b;
						while ((b = stream.read()) != -1) {
							stringBuilder.append((char) b);
						}
					} catch (ClientProtocolException e) {
					} catch (IOException e) {
					}
					
					JSONObject jsonObject = new JSONObject();
				    try {
				        jsonObject = new JSONObject(stringBuilder.toString());
				    } catch (JSONException e) {
				        e.printStackTrace();
				    }

					if (jsonObject.getString("status").equals("OK")) {

						JSONObject jsonResult = jsonObject.getJSONArray("results").getJSONObject(0);
						
						String formatted_address = jsonResult.getString("formatted_address");
						if (null != formatted_address) {
							addressLocation.setAddressFormatted(formatted_address);
						}
						
						JSONArray addressComponentsArray = jsonResult.getJSONArray("address_components");
						
						JSONObject jsonAddress = addressComponentsArray.getJSONObject(0);
						JSONObject jsonAddress1 = addressComponentsArray.getJSONObject(1);
						JSONObject jsonAddress2 = addressComponentsArray.getJSONObject(2);
						JSONObject jsonAddress3 = addressComponentsArray.getJSONObject(3);
						JSONObject jsonAddress4 = addressComponentsArray.getJSONObject(4);
						
//		                if(types.equalsIgnoreCase("street_number")){
//		                    street_number = addressComponents.getString("short_name");
//		                }else if(types.equalsIgnoreCase("postal_code")){
//		                    postal_code = addressComponents.getString("long_name");
//		                }else if(types.equalsIgnoreCase("country")){
//		                    country = addressComponents.getString("long_name");
//		                }else if(types.equalsIgnoreCase("route")){
//		                    route = addressComponents.getString("long_name");
//		                }
						

						// if(null != jsonObject.getString("postal_code")){
						// String postal_code =
						// jsonObject.getString("postal_code");
						// addressLocation.setPostalCode(cep);
						// }
						// addressLocation.setAddress( address);
						// addressLocation.setCity(city);
						// addressLocation.setCountry(country);
					}
				 */


	@Override
	protected void onPostExecute(AddressLocation result) {
		super.onPostExecute(result);
	}
}
