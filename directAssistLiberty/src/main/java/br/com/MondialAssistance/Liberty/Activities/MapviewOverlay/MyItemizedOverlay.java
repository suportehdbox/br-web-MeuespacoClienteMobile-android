/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package br.com.MondialAssistance.Liberty.Activities.MapviewOverlay;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import br.com.MondialAssistance.Liberty.Activities.ScreenAccreditedEstablishmentDetails;
import br.com.MondialAssistance.Liberty.Activities.ScreenAddress;
import br.com.MondialAssistance.DirectAssist.MDL.AccreditedGarage;
import br.com.MondialAssistance.DirectAssist.MDL.AddressLocation;
import br.com.MondialAssistance.DirectAssist.Util.Utility;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private boolean details = true;
	private Object description;
	private Activity activity; 
	private Context context;
	private int type;
	
	public boolean getDetails() {
		return details;
	}
	public void setDetails(boolean value) {
		details = value;
	}	
	
	public Object getDescription() {
		return description;
	}

	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView, Activity a, Object desc, int typeScreenDetails) {
		super(boundCenter(defaultMarker), mapView);
		
		context = mapView.getContext();
		type = typeScreenDetails;
		description = desc;
		activity = a;
	}
	
	public void addOverlay(OverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		
		if (details == true) {
		
			Intent intent;
		
			switch (type) {
				case Utility.ACCREDITED_ESTABLISHMENT_DETAILS:
					
					intent = new Intent(context, ScreenAccreditedEstablishmentDetails.class);
					
					intent.putExtra("GARAGE", ((AccreditedGarage)description).getGarageName());
					intent.putExtra("ADDRESS", ((AccreditedGarage)description).getAddress().toString());
					intent.putExtra("DISTANCE", ((AccreditedGarage)description).getDistance().toString() + " km");
					intent.putExtra("PHONE", ((AccreditedGarage)description).getPhone().toString());
					intent.putExtra("PHONE_AREA", ((AccreditedGarage)description).getPhone().getAreaCode());
					intent.putExtra("PHONE_NUMBER", ((AccreditedGarage)description).getPhone().getPhoneNumber());
					
					activity.startActivity(intent);
					break;
				case Utility.ADDRESS_DETAILS:
					
					intent = new Intent(context, ScreenAddress.class);
					
					intent.putExtra("ADDRESS", ((AddressLocation)description).getStreetName());
					intent.putExtra("DISTRICT", ((AddressLocation)description).getDistrict());
					intent.putExtra("COMPLEMENT", ((AddressLocation)description).getComplement());
					intent.putExtra("HOUSENUMBER", ((AddressLocation)description).getHouseNumber());
					intent.putExtra("CITY", ((AddressLocation)description).getCity());
					intent.putExtra("STATE", ((AddressLocation)description).getState());
					intent.putExtra("ZIP", ((AddressLocation)description).getZip());
					intent.putExtra("LATITUDE", ((AddressLocation)description).getLatitude());
					intent.putExtra("LONGITUDE", ((AddressLocation)description).getLongitude());
					
					activity.startActivityForResult(intent, Utility.ADDRESS_DETAILS);
					
					break;
			}
		}
		return true;
	}
}
