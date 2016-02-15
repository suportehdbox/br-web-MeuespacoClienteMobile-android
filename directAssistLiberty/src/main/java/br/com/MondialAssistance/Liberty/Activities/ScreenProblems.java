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

import br.com.MondialAssistance.Liberty.BLL.BLLClient;
import br.com.MondialAssistance.Liberty.MDL.ListItem;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.common.CustomApplication;
//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenProblems extends Activity {

	private String CauseID;
	private RelativeLayout problemHeaderBackButton;
	private ListView listProblems;
	private TextView viewScreenName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_problem);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {
		View headerView = (View) findViewById(R.id.screenProblem_Header);
		problemHeaderBackButton = (RelativeLayout) headerView
				.findViewById(R.id.btnBack);
		/*problemHeaderBackButton
				.setBackgroundResource(R.drawable.assistencia_nova_localmap_btn_nova);*/
		//problemHeaderBackButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		CauseID = intent.getExtras().getString("CAUSEID");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenProblem);

		BLLClient client = new BLLClient();
		ArrayList<ListItem> items = client.getProblems(CauseID, getResources()
				.getStringArray(R.array.Problem));

		CtrlListViewListItemsDefaultAdapter adapterListItemsDefault = new CtrlListViewListItemsDefaultAdapter(
				this, items);

		listProblems = (ListView) findViewById(R.id.listProblems);
		listProblems.setAdapter(adapterListItemsDefault);
	}

	private void Events() {
		problemHeaderBackButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		listProblems.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
									int position, long id) {

				CtrlListViewListItemsDefaultAdapter adapterListItemsDefault = (CtrlListViewListItemsDefaultAdapter) adapter
						.getAdapter();
				ListItem item = adapterListItemsDefault.getItem(position);

				Intent intent = new Intent();
				intent.putExtra("PROBLEM_ID", item.getID());
				intent.putExtra("PROBLEM_NAME", item.getName());

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
