package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.DirectAssist.MDL.ListItem;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.DirectAssist.Util.Utility;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenMainMenu extends Activity {

	private int LobID;
	private String LobName;

	private TextView viewScreenName;
	private ListView listMainMenu;
	private ImageView imgBanner;

	private DisplayMetrics displayMetrics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_mainmenu);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);

		Initialize();
		Events();
	}

	private void Initialize() {

		Intent intent = getIntent();
		LobID = intent.getExtras().getInt("LOBID");
		LobName = intent.getExtras().getString("LOBNAME");

		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		viewScreenName.setText(LobName);

		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_LOW)
			imgBanner = (ImageView) findViewById(R.id.imgBanner);

		ImageView img = new ImageView(this);
		ArrayList<ListItem> items = new ArrayList<ListItem>();
		ListItem lob;

		lob = new ListItem();
		img.setImageResource((LobID == Utility.PROPERTY) ? R.drawable.pr_newfile
				: R.drawable.au_newfile);
		lob.setIcon(img.getDrawable());
		lob.setName(this.getText(R.string.RequestAssistance).toString());
		lob.setCod("REQASSIST");
		items.add(lob);

		lob = new ListItem();
		img.setImageResource((LobID == Utility.PROPERTY) ? R.drawable.pr_myfiles
				: R.drawable.au_myfiles);
		lob.setIcon(img.getDrawable());
		lob.setName(this.getText(R.string.MyFiles).toString());
		lob.setCod("MYFILES");
		items.add(lob);

		if (LobID != Utility.PROPERTY) {

			lob = new ListItem();
			img.setImageResource(R.drawable.au_mechanicnetwork);
			lob.setIcon(img.getDrawable());
			lob.setName(this.getText(R.string.AccreditedEstablishment)
					.toString());
			lob.setCod("ESTABLISHMENT");
			items.add(lob);
		}

		lob = new ListItem();
		img.setImageResource(R.drawable.call);
		lob.setIcon(img.getDrawable());
		lob.setName(this.getText(R.string.CallForAssistance24h).toString());
		lob.setCod("PHONEASSIST");
		items.add(lob);

		CtrlListviewListMainMenuAdapter adapter = new CtrlListviewListMainMenuAdapter(
				this, items);

		listMainMenu = (ListView) findViewById(R.id.listMainMenu);
		listMainMenu.setAdapter(adapter);
	}

	private void Events() {

		if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_LOW) {
			imgBanner.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(ClientParams.BannerURL));
					startActivity(intent);
				}
			});
		}

		listMainMenu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
									int position, long id) {

				CtrlListviewListMainMenuAdapter adapterListMainMenu = (CtrlListviewListMainMenuAdapter) adapter
						.getAdapter();
				ListItem item = adapterListMainMenu.getItem(position);

				Intent intent;

				if (item.getCod().equals("PHONEASSIST")) {
					//Log.e("", "I m n phone assist");
					intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
							+ UtilityScreen.getAssistPhoneNumber(
							ScreenMainMenu.this, LobID)));
					startActivity(intent);

				} else if (item.getCod().equals("MYFILES")) {
					//Log.e("", "I m n my files");
					if (!Utility.isConnected(ScreenMainMenu.this)) {

						Toast.makeText(ScreenMainMenu.this,
								getString(R.string.NotConnection),
								Toast.LENGTH_SHORT).show();

					} else {

						intent = new Intent(ScreenMainMenu.this,
								ScreenMyFiles.class);
						intent.putExtra("LOBID", LobID);

						startActivity(intent);
					}

				} else if (item.getCod().equals("ESTABLISHMENT")) {
					//Log.e("", "I m n my establishment");
					if (!Utility.isConnected(ScreenMainMenu.this)) {

						Toast.makeText(ScreenMainMenu.this,
								getString(R.string.NotConnection),
								Toast.LENGTH_SHORT).show();

					} else {

						intent = new Intent(ScreenMainMenu.this,
								ScreenAccreditedEstablishment.class);
						intent.putExtra("LOBID", LobID);

						startActivity(intent);
					}

				} else if (item.getCod().equals("REQASSIST")) {

					//Log.e("", "I m n my reqassist");
					if (!Utility.isConnected(ScreenMainMenu.this)) {

						Toast.makeText(ScreenMainMenu.this,
								getString(R.string.NotConnection),
								Toast.LENGTH_SHORT).show();

					} else {

						intent = new Intent(ScreenMainMenu.this,
								ScreenNewAssistance.class);
						intent.putExtra("LOBID", LobID);

						startActivity(intent);
					}

				}
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