package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.MondialAssistance.Liberty.BLL.BLLClient;
import br.com.MondialAssistance.Liberty.MDL.ListItem;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenServices extends Activity {
	
	private int LobID;
	private String CauseID;
	
	private ListView listServices;
	private TextView viewScreenName;
	private Button btnCallForAssistance24h;
	private RelativeLayout headerBackButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_services);
		
//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);
		
		Initialize();
		Events();
	}
	
	private void Initialize(){
		View headerView = (View)findViewById(R.id.screenService_Header);
		headerBackButton = (RelativeLayout)headerView.findViewById(R.id.btnBack);
		//headerBackButton.setBackgroundResource(R.drawable.assistencia_nova_localmap_btn_nova);	
		//headerBackButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		CauseID = intent.getExtras().getString("CAUSEID");
		LobID = intent.getExtras().getInt("LOBID");
		
		viewScreenName = (TextView)findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenService);
		
		btnCallForAssistance24h = (Button)findViewById(R.id.btnCallForAssistance24h);
		
		BLLClient client = new BLLClient();
		ArrayList<ListItem> items = client.getServices(LobID, CauseID, (CauseID == null ? getResources().getStringArray(R.array.ServiceAll) : getResources().getStringArray(R.array.Service)));
		
		CtrlListViewListItemsDefaultAdapter adapterListItemsDefault = new CtrlListViewListItemsDefaultAdapter(this, items);
		
		listServices = (ListView)findViewById(R.id.listServices);
		listServices.setAdapter(adapterListItemsDefault);
	}
	
	private void Events(){
		headerBackButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnCallForAssistance24h.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UtilityScreen.getAssistPhoneNumber(ScreenServices.this, LobID)));
				startActivity(intent);
			}
		});

		listServices.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

				CtrlListViewListItemsDefaultAdapter adapterListItemsDefault = (CtrlListViewListItemsDefaultAdapter) adapter.getAdapter();
				ListItem item = adapterListItemsDefault.getItem(position);

				Intent intent = new Intent();
				intent.putExtra("SERVICE_ID", item.getCod());
				intent.putExtra("SERVICE_NAME", item.getName());

				setResult(1, intent);

				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		GoogleAnalytics.stopAnalyticsTracker();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CustomApplication.activityPaused();
	}
}