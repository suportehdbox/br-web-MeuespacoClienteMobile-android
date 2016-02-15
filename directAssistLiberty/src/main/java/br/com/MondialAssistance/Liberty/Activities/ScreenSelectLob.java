package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenSelectLob extends Activity {

	/*
	 * private Button btnCallMyPhoneNumber;
	 * 
	 * //private ListView listLob;
	 */
	private TextView viewScreenName;
	private DisplayMetrics displayMetrics;
	// private ImageView banner_View;

	private LinearLayout automativeNewCase;
	private LinearLayout automativeCallAssistance;
	private RelativeLayout automativeMyCases;

	//LIBERTY SEGUROS
//	private LinearLayout propertyNewCase;
//	private LinearLayout propertyCallAssistance;
//	private RelativeLayout propertyMyCases;

	private View header_View;
	private int LobID;
	private Intent intent;
	private Button callAssistance;
	private RelativeLayout mainBackHeaderButton;

	private Tracker mTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_select_lob);
		// automotive_View = (View)findViewById(R.id.automative_layout);
		// property_View = (View)findViewById(R.id.property_layout);
		header_View = (View) findViewById(R.id.mainScreen_header);
//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);
		AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
		mTracker = analyticsApplication.getDefaultTracker(getApplication());

		Initialize();
		handleEventClicks();
		// Events();
	}

	private void handleEventClicks() {

		mainBackHeaderButton.setOnClickListener(new View.OnClickListener() {

		 	public void onClick(View v) { // TODO Auto-generated method stub
		 		finish();
			}
		});

		automativeNewCase.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startNewCase(Utility.AUTOMOTIVE);
			}
		});

		//LIBERTY SEGUROS
//		propertyNewCase.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				startNewCase(Utility.PROPERTY);
//
//			}
//		});

		callAssistance.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startCallAssistance(Utility.AUTOMOTIVE);
			}
		});
		automativeMyCases.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewMyCases(Utility.AUTOMOTIVE);
			}
		});

		//LIBERTY SEGUROS
//		propertyMyCases.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				viewMyCases(Utility.PROPERTY);
//			}
//		});

		/*
		 * propertyCallAssistance.setOnClickListener(new View.OnClickListener()
		 * {
		 * 
		 * public void onClick(View v) { // TODO Auto-generated method stub
		 * startCallAssistance(Utility.PROPERTY);
		 * 
		 * } });
		 */

		// TODO Auto-generated method stub
		if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_LOW) {
			/*
			 * banner_View.setOnClickListener(new OnClickListener() { public
			 * void onClick(View v) {
			 * 
			 * Intent intent = new Intent(Intent.ACTION_VIEW,
			 * Uri.parse(ClientParams.BannerURL)); startActivity(intent); } });
			 */
		}
	}

	private void Initialize() {
		automativeNewCase = (LinearLayout) findViewById(R.id.automative_layout);

		//LIBERTY SEGUROS
//		propertyNewCase = (LinearLayout) findViewById(R.id.property_layout);

		mainBackHeaderButton = (RelativeLayout) findViewById(R.id.btnBack);
		
		ImageView backIv = (ImageView) findViewById(R.id.backImageView);
		//LIBERTY SEGUROS
//		backIv.setVisibility(View.GONE);
		backIv.setVisibility(View.VISIBLE);
		mainBackHeaderButton.setVisibility(View.VISIBLE);

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(R.string.TitleScreenFileDetails);
		viewScreenName.setBackgroundResource(R.drawable.white_line);

		//LIBERTY SEGUROS
//		ImageView propertyHeadingIv = (ImageView) propertyNewCase.findViewById(R.id.imageheading_Iv_prop);
//		propertyHeadingIv.setImageResource(R.drawable.icon_home);

		ImageView automativeHeadingIv = (ImageView) automativeNewCase.findViewById(R.id.imageheading_Iv_auto);
		automativeHeadingIv.setImageResource(R.drawable.icon_car);
		callAssistance = (Button) findViewById(R.id.callforAssistance_button);

		/*
		 * TextView automativeHeadingTv =
		 * (TextView)findViewById(R.id.autoheading_tv); TextView
		 * propertyHeadingTv = (TextView)findViewById(R.id.residenceheading_tv);
		 * automativeHeadingTv.setText(R.string.Automotive);
		 * propertyHeadingTv.setText(R.string.Property);
		 */

		automativeMyCases = (RelativeLayout) findViewById(R.id.myAutocases_layout);

		//LIBERTY SEGUROS
//		propertyMyCases = (RelativeLayout) findViewById(R.id.myResidencecases_layout);

		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		/*
		 * if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_LOW){
		 * banner_View = (ImageView)findViewById(R.id.banner_ImageView); }
		 */

		/*
		 * viewScreenName = (TextView)findViewById(R.id.viewScreenName);
		 * //viewScreenName.setText(R.string.app_name);
		 * viewScreenName.setText(R.string.TitleScreenFileDetails);
		 * btnCallMyPhoneNumber =
		 * (Button)findViewById(R.id.btnCallMyPhoneNumber);
		 */

		/*
		 * ImageView img = new ImageView(this); ArrayList<ListItem> items = new
		 * ArrayList<ListItem>(); ListItem lob;
		 * 
		 * if (UtilityScreen.isAutomotiveServiceEnabled(ScreenSelectLob.this) ==
		 * true){
		 * 
		 * lob = new ListItem(); img.setImageResource(R.drawable.automotive);
		 * lob.setIcon(img.getDrawable());
		 * lob.setName(getText(R.string.Automotive).toString());
		 * lob.setID(Utility.AUTOMOTIVE);
		 * 
		 * items.add(lob); }
		 * 
		 * if (UtilityScreen.isAutomakerServiceEnabled(ScreenSelectLob.this) ==
		 * true){
		 * 
		 * lob = new ListItem(); img.setImageResource(R.drawable.automaker);
		 * lob.setIcon(img.getDrawable());
		 * lob.setName(getText(R.string.Automaker).toString());
		 * lob.setID(Utility.AUTOMAKER);
		 * 
		 * items.add(lob); }
		 * 
		 * if (UtilityScreen.isPropertyServiceEnabled(ScreenSelectLob.this) ==
		 * true){
		 * 
		 * lob = new ListItem(); img.setImageResource(R.drawable.property);
		 * lob.setIcon(img.getDrawable());
		 * lob.setName(getText(R.string.Property).toString());
		 * lob.setID(Utility.PROPERTY);
		 * 
		 * items.add(lob); }
		 */

		// CtrlListViewListLobAdapter adapter = new
		// CtrlListViewListLobAdapter(this, items);
		// final LayoutInflater inflater =
		// (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		// listLob = (ListView)findViewById(R.id.listLob);
		// LinearLayout root =
		// (LinearLayout)findViewById(R.id.automativeLayout);
		// View automativeView =
		// (View)inflater.inflate(R.layout.lob_list_headerview,null);
		// //(View)findViewById(R.layout.lob_list_headerview)ss;
		// ImageView automativeImageView =
		// (ImageView)automativeView.findViewById(R.id.lob_Header_IV);
		// automativeImageView.setImageResource(R.drawable.automaker);
		// TextView automativeTextView =
		// (TextView)automativeView.findViewById(R.id.lob_Header_TV);
		// automativeTextView.setText(getResources().getString(R.string.Automotive));
		// listLob.addHeaderView(automativeView);

		// listLob.setAdapter(adapter);
	}

	private void startNewCase(int newCaseLobId) {
		if (!Utility.isConnected(ScreenSelectLob.this)) {

			Toast.makeText(ScreenSelectLob.this,
					getString(R.string.NotConnection), Toast.LENGTH_SHORT)
					.show();

		} else {

			intent = new Intent(ScreenSelectLob.this, ScreenNewAssistance.class);
			intent.putExtra("LOBID", newCaseLobId);

			startActivity(intent);
		}
	}

	private void startCallAssistance(int assistanceLobId) {
		intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ UtilityScreen.getAssistPhoneNumber(ScreenSelectLob.this,
						assistanceLobId)));
		startActivity(intent);
	}

	private void viewMyCases(int casesLobId) {
		if (!Utility.isConnected(ScreenSelectLob.this)) {

			Toast.makeText(ScreenSelectLob.this,
					getString(R.string.NotConnection), Toast.LENGTH_SHORT)
					.show();

		} else {

			intent = new Intent(ScreenSelectLob.this, ScreenMyFiles.class);
			intent.putExtra("LOBID", casesLobId);

			startActivity(intent);
		}
	}

	/*
	 * private void Events(){
	 * 
	 * btnCallMyPhoneNumber.setOnClickListener(new OnClickListener() { public
	 * void onClick(View v) {
	 * 
	 * Intent intent = new Intent(ScreenSelectLob.this,
	 * ScreenRegisterPhoneNumber.class); intent.putExtra("openLob", false);
	 * 
	 * startActivity(intent); } });
	 * 
	 * listLob.setOnItemClickListener(new OnItemClickListener() { public void
	 * onItemClick(AdapterView<?> adapter, View view, int position, long id) {
	 * 
	 * CtrlListViewListLobAdapter listViewListLob =
	 * (CtrlListViewListLobAdapter)adapter.getAdapter(); ListItem item =
	 * listViewListLob.getItem(position);
	 * 
	 * Intent intent = new Intent(ScreenSelectLob.this, ScreenMainMenu.class);
	 * intent.putExtra("LOBNAME", item.getName()); intent.putExtra("LOBID",
	 * item.getID());
	 * 
	 * startActivity(intent); } }); }
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		GoogleAnalytics.stopAnalyticsTracker();
//		System.exit(1);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("Google Analytics: ", "Assistência Automotiva");
		mTracker.setScreenName("Assistência Automotiva");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CustomApplication.activityPaused();
	}
}