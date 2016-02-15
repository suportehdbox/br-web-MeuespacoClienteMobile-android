/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.adapter.ClaimListAdapter;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;
import br.com.libertyseguros.mobile.model.Event;

/**
 * Displays the list of all previously saved claims and allows the user to add or delete claims or view claim details.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class ClaimListActivity extends LibertyMobileApp implements OnClickListener, DialogInterface.OnClickListener, OnItemClickListener, OnLongClickListener
{
	private static final String ADD = "ADD";

	private static final String CALL = "CALL";

	private static final String DELETE = "DELETE";

	private String currentAction;

	private long idToDelete;

	private String phoneNumber = "";

	private Tracker mTracker;

	/**
	 * Gets the list of all events
	 */
	private void getUpdatedList()
	{
		try {
			// //Log.v(TAG, ">>> getUpdatedList()");

			ArrayList<Event> events = EventHelper.getAllForList(getApplicationContext());

			// Set up the list views to display the contacts and react to selections
			ListView eventList = (ListView) findViewById(R.id.claims_list);
			eventList.setAdapter(new ClaimListAdapter(getApplicationContext(), events));

			eventList.setOnItemClickListener(ClaimListActivity.this);

			// register the list view for long clicks
			registerForContextMenu(eventList);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Responds to the user's selection in an alert dialog, in this case asking the user what type of claim they would
	 * like to begin or whether or not they would like to delete a claim or make a phone call. Listens for the selection
	 * in the add claim dialog and invokes the add claim process or the delete claim dialog and deletes the selected
	 * claim.
	 * 
	 * @param dialog
	 *            the dialog to which the user responded
	 * @param which
	 *            the selection the user made
	 * @see DialogInterface.OnClickListener#onClick(DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		try {
			// Log.v(TAG, ">>> onClick(DialogInterface dialog, int selection)");

			// figure out what the user was doing
			if (ADD.equals(currentAction))
			{
				// invoke the add claim process
				addClaim();
			}
			else if (which == -1)
			{
				if (CALL.equals(currentAction))
				{
					// Make a phone call to the current phone number
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + this.phoneNumber));
					startActivity(callIntent);
				}
				else if (DELETE.equals(currentAction))
				{
					// If the user clicked delete, delete the event
					EventHelper.delete(getApplicationContext(), idToDelete);
					getUpdatedList();
				}
			}

			// Log.v(TAG, "<<< onClick(DialogInterface dialog, int selection)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Responds to the click of a button in the view and performs the action associated with the button that was
	 * clicked. If the the background when the list is empty was pushed, the add new claim process will be started.
	 * 
	 * @param v
	 *            the View that was clicked, in this case a button
	 * @see OnClickListener#onClick(View)
	 */
	@Override
	public void onClick(View v)
	{
		try {
			// Log.v(TAG, ">>> onClick()");

			currentAction = ADD;

			// Ask the user what type of claim they want to add
			selectAddClaim(ClaimListActivity.this, ClaimListActivity.this);

			// Log.v(TAG, "<<< onClick()");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Called when the user selects a menu option after a long click on a list item. Either deletes the selected claim
	 * or goes to the claim detail, depending on the user's selection.
	 * 
	 * @param item
	 *            the item that the user selected
	 * @return true if a valid menu option was selected, otherwise false
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		try {
			boolean returnVal = false;

			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			long id = info.id;

			//        Event event = EventHelper.get(getApplicationContext(), id);

			switch (item.getItemId())
			{
			//            case R.id.menu_item_call_us:
			//                currentAction = CALL;
			//
			//                // Find out the claim type
			//                if (Constants.LOB_AUTO.equals(event.getEventType()))
			//                {
			//                    this.phoneNumber = getString(R.string.auto_claims_number);
			//                    String title = getString(R.string.confirm_call_auto_specialist);
			//
			//                    // Ask the user if they want to make the call
			//                    displayConfirmAlert(ClaimListActivity.this, title, this.phoneNumber, getString(R.string.btn_ok),
			//                        getString(R.string.no), ClaimListActivity.this);
			//                }
			//
			//                break;

			case R.id.menu_item_delete:
				currentAction = DELETE;
				idToDelete = info.id;

				// ask the user if they want to delete the claim
				String msg = null;
				if (Constants.EVENT_STATUS_SUBMITTED.equals(EventHelper.get(getApplicationContext(), id)
						.getEventStatus()))
				{
					msg = getString(R.string.delete_submitted_claim_confirmation);
				}
				else
				{
					msg = getString(R.string.delete_claim_confirmation);
				}
				displayConfirmAlert(ClaimListActivity.this, getString(R.string.delete), msg, getString(R.string.btn_ok),
						getString(android.R.string.no), ClaimListActivity.this);

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

	/**
	 * Initializes the view, setting the layout to the claim list layout. Also builds the navigation bar.
	 * 
	 * @param savedInstanceState
	 *            if the activity is being re-initialized after previously being shut down then this Bundle contains the
	 *            data it most recently supplied in {@link Activity.onSaveInstanceState(Bundle)}. Note: Otherwise it is
	 *            null.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {
			// Log.v(TAG, ">>> onCreate(Bundle savedInstanceState)");

			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_claim_list);

			// Set up nav bar with title
			setUpNavigationBarTitleOnly(getString(R.string.claims_title));

			AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
			mTracker = analyticsApplication.getDefaultTracker(getApplication());

			View background = findViewById(R.id.claim_list_background);
			background.setOnLongClickListener(ClaimListActivity.this);

			// Log.v(TAG, "<<< onCreate(Bundle savedInstanceState)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		try {
			// Log.v(TAG, ">>> onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)");

			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.claims_list_menu, menu);

			// Log.v(TAG, "<<< onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	//    /**
	//     * Adds the menu items for the claims view when the user pushes the device's menu button
	//     * 
	//     * @param menu
	//     *            The options menu in which you place the items.
	//     * @return true so that the menu will be displayed
	//     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	//     */
	//    @Override
	//    public boolean onCreateOptionsMenu(Menu menu)
	//    {
	//        // Log.v(TAG, ">>> onCreateOptionsMenu(Menu menu)");
	//
	//        MenuInflater inflater = getMenuInflater();
	//        inflater.inflate(R.menu.main_menu, menu);
	//
	//        // Hide the item for this view
	//        MenuItem overview = menu.findItem(R.id.menu_item_claims);
	//        overview.setVisible(false);
	//
	//        // Log.v(TAG, "<<< onCreateOptionsMenu(Menu menu)");
	//
	//        return true;
	//    }

	/**
	 * Responds to the selection of an existing claim and starts the claim detail activity setting the selected event as
	 * the current event.
	 * 
	 * @param parent
	 *            The AdapterView where the click happened.
	 * @param view
	 *            The view within the AdapterView that was clicked (this will be a view provided by the adapter)
	 * @param position
	 *            The position of the view in the adapter.
	 * @param id
	 *            The row id of the item that was clicked.
	 * @see OnItemClickListener#onItemClick(AdapterView,
	 *      View,int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		try {
			// Log.v(TAG, ">>> onItemClick(AdapterView<?> adapter, View view, int position, long id)");

			// go to the claim detail view for the selected claim
			viewClaimDetail(id);

			// Log.v(TAG, "<<< onItemClick(AdapterView<?> adapter, View view, int position, long id)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Specifies the actions to be performed when a long click is done on the view (background). The add claim process
	 * will be started.
	 * 
	 * @param v
	 *            The view which was clicked
	 * @see OnLongClickListener#onLongClick(View)
	 */
	@Override
	public boolean onLongClick(View v)
	{
		try {
			// Log.v(TAG, ">>> onLongClick(View v)");

			currentAction = ADD;

			// Ask the user what type of claim they want to add
			selectAddClaim(ClaimListActivity.this, ClaimListActivity.this);

			// Log.v(TAG, "<<< onLongClick(View v)");

			return false;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * Refreshes the view when the activity is resumed. Because activities that open from this activity can update the
	 * data in this activity, it is necessary to update itself each time it displays to make sure it has the most recent
	 * data.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		try {
			super.onResume();

			// refresh the page on returning to make sure we have the most up to date lists
			getUpdatedList();

			Log.i("Google Analytics: ", "Comunicação de Acidente: Consulta");
			mTracker.setScreenName("Comunicação de Acidente: Consulta");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Retrieves the details for the given event and goes to the claim detail view for that event.
	 * 
	 * @param id
	 *            the id of the claim to view
	 */
	private void viewClaimDetail(long id)
	{
		try {
			Event event = EventHelper.get(getApplicationContext(), id);

			setCurrentEvent(event);

			// Go to the claim detail view and tell it that this is not the initial view
			Intent i = new Intent(getApplicationContext(), ComunicacaoAcidenteDetalheActivity.class);
			i.putExtra(ComunicacaoAcidenteDetalheActivity.EXTRA_NAME_INITIAL_VIEW, false);
			startActivity(i);

		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}
	
}
