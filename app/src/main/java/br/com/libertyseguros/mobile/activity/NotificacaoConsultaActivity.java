package br.com.libertyseguros.mobile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.GenericAdapter;
import br.com.libertyseguros.mobile.adapter.ItemAdapter;
import br.com.libertyseguros.mobile.common.Util;

/**
 * @author Evandro
 */
public class NotificacaoConsultaActivity extends LibertyMobileApp implements OnItemClickListener{
	
	private ListView 			listViewNotificacoes;
	private List<ItemAdapter> 	listItensNotificacoes;
	private GenericAdapter 		adapter;
	private List<Map<String, Object>> arrayFields;

	private Tracker mTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		try {
			System.gc();
	        Runtime.getRuntime().gc();
	        
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_notificacao_consulta);

			// Set up nav bar with title only
			setUpNavigationBarTitleOnly(getString(R.string.title_activity_notificacao_consulta));

//			GoogleAnalyticsManager.setAnalyticsTracker(this, getString(R.string.title_activity_notificacao_consulta));
			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());


		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			System.gc();
	        Runtime.getRuntime().gc();
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_comunicacao_acidente, menu);
			return true;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		try {
			boolean returnVal = false;

			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

			switch (item.getItemId()) {
			
				case R.id.menu_item_delete:
					
					arrayFields.remove(info.position);
					
					Util.saveNotificationList(getApplicationContext(), arrayFields);
					
					getUpdatedList();
	
					returnVal = true;
					break;
	
				default:
					returnVal = super.onContextItemSelected(item);
			}

			return returnVal;
			
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}
	
	private void getUpdatedList(){
		
		listViewNotificacoes = (ListView) findViewById(R.id.lvwNotificacao);
		listItensNotificacoes = new ArrayList<ItemAdapter>();
		
		// Inicializar o array com os conteúdos do json salvo
		arrayFields = Util.getNotificationList(getApplicationContext());
		
		// associa o array  listview
		for (Map<String, Object> pushNotification : arrayFields) {
			
			String date = (String) pushNotification.get("date");
			String alert = (String) pushNotification.get("alert");
			
			ItemAdapter itemAdapter = new ItemAdapter(date);
			itemAdapter.setText2(alert);
			listItensNotificacoes.add(itemAdapter);
		}
		
		adapter = new GenericAdapter(this, listItensNotificacoes);
		listViewNotificacoes.setAdapter(adapter);	
		
		registerForContextMenu(listViewNotificacoes);
		
	}
	
	/**
	 * Adds the menu items for a claims list item when the user performs a long click
	 * 
	 * @param menu
	 *            The context menu in which you place the items.
	 * @param v
	 *            The view for which the context menu is being built
	 * @param menuInfo
	 *            Extra information about the item for which the context menu should be shown. This information will
	 *            vary depending on the class of v.
	 * @see android.app.Activity#onCreateContextMenu(ContextMenu, View,
	 *      ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		try {
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.claims_list_menu, menu);

			// Log.v(TAG, "<<< onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	
	@Override
	protected void onResume() {
		try {
			super.onResume();

			// refresh the page on returning to make sure we have the most up to date lists
			getUpdatedList();

			Log.i("Google Analytics: ", "Notificações");
			mTracker.setScreenName("Notificações");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//ANDROID STUDIO
//		EasyTracker.getInstance(this).activityStart(this);	//Medir o tempo dentro da tela
	}
	
	@Override
	public void onStop() {
		super.onStop();
		//ANDROID STUDIO
//		EasyTracker.getInstance(this).activityStop(this);	//Para o tempo dentro da tela
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

}
