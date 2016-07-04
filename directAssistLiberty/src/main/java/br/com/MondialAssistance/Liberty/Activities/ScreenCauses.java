package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.MondialAssistance.Liberty.R;

import br.com.MondialAssistance.DirectAssist.BLL.BLLClient;
import br.com.MondialAssistance.DirectAssist.MDL.ListItem;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenCauses extends Activity {

	private int LobID;
	private RelativeLayout causesBackHeaderButton;
	private ListView listCauses;
	private TextView viewScreenName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_causes);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View) findViewById(R.id.screenCauses_Header);
		causesBackHeaderButton = (RelativeLayout) headerView.findViewById(R.id.btnBack);
		/*causesBackHeaderButton
				.setBackgroundResource(R.drawable.assistencia_nova_localmap_btn_nova);*/
		//causesBackHeaderButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		LobID = intent.getExtras().getInt("LOBID");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenCause);

		BLLClient client = new BLLClient();
		ArrayList<ListItem> items = client.getCauses(LobID, getResources()
				.getStringArray(R.array.Causes));

		CtrlListViewListItemsDefaultAdapter adapterListItemsDefault = new CtrlListViewListItemsDefaultAdapter(
				this, items);

		listCauses = (ListView) findViewById(R.id.listCauses);
		listCauses.setAdapter(adapterListItemsDefault);
	}

	private void Events() {

		causesBackHeaderButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		listCauses.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
									int position, long id) {

				CtrlListViewListItemsDefaultAdapter adapterListItemsDefault = (CtrlListViewListItemsDefaultAdapter) adapter
						.getAdapter();
				ListItem item = adapterListItemsDefault.getItem(position);

				Intent intent = new Intent();
				intent.putExtra("CAUSE_ID", item.getCod());
				intent.putExtra("CAUSE_NAME", item.getName());

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