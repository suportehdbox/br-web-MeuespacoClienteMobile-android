/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.Event;

//TODO - Convert this to a Cursor Adapter to improve performance
/**
 * Adapter for displaying a list of claims and selecting a claim from the list
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class ClaimListAdapter extends BaseAdapter
{
    //private static final String TAG = ClaimListAdapter.class.getName();

    private Context context;

    private ArrayList<Event> events;

    /**
     * Constructs a ClaimListAdapter with the given context and events
     * 
     * @param context
     *            the context within which to work
     * @param events
     *            the list of events to display
     */
    public ClaimListAdapter(Context context, ArrayList<Event> events)
    {
        // Log.v(TAG, ">>> ClaimListAdapter(Context context, ArrayList<Event> events)");

        this.context = context;

        this.events = events;

        // Log.v(TAG, "<<< ClaimListAdapter(Context context, ArrayList<Event> events)");
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        // Log.v(TAG, ">>> getCount()");

        // Log.v(TAG, "<<< getCount()");

        return events.size();
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Event getItem(int location)
    {
        // Log.v(TAG, ">>> getItem(int activity_location)");

        // Log.v(TAG, "<<< getItem(int activity_location)");

        return events.get(location);
    }

    /**
     * Gets the database typeId of the event at item <code>position</code>.
     * 
     * @param position
     *            the position of the item whose id will be returned
     * @return the database typeId of the item
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position)
    {
        // Log.v(TAG, ">>> getItemId(int position)");

        Event event = events.get(position);

        // Log.v(TAG, "<<< getItemId(int position)");

        return event.getId();
    }

    /**
     * Returns a view for displaying the event at position <code>position</code>. The event will be displayed as a
     * claim_list_item layout.
     * 
     * @param position
     *            The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView
     *            The old view to reuse, if possible. Note: You should check that this view is non-null and of an
     *            appropriate type before using. If it is not possible to convert this view to display the correct data,
     *            this method can create a new view.
     * @param parent
     *            The parent that this view will eventually be attached to
     * @return the View to be displayed as the list item
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Log.v(TAG, ">>> getView(int position, View convertView, ViewGroup parent)");

        View view = null;
        Event event = events.get(position);

        if (convertView == null)
        {
        	view = LayoutInflater.from(context).inflate(R.layout.claim_list_item, parent, false);
        }
        else
        {
            view = convertView;
        }

        // Show the title row
        setUpTitleRow(view, event);

        // Show the claim type
        TextView type = (TextView) view.findViewById(R.id.claim_type);
        type.setText(event.getEventSubType());

        // Show the claim date
        Timestamp date = event.getEventDateTime();
        TextView time = (TextView) view.findViewById(R.id.claim_time);
        if (date != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy 'às' h:mm a");
            time.setText(sdf.format(date));
        }
        else
        {
            time.setText("");
        }

        // Show the claim activity_location
        Address address = event.getLocation();
        TextView location = (TextView) view.findViewById(R.id.claim_location);
        if (address != null)
        {
            location.setText(address.getCity() + ", " + address.getState());
        }
        else
        {
            location.setText("");
        }
        // Log.v(TAG, "<<< getView(int position, View convertView, ViewGroup parent)");

        return view;
    }

    /**
     * @param events
     *            the events to set
     */
    public void setEvents(ArrayList<Event> events)
    {
        this.events = events;
    }

    /**
     * Builds a title row for a specific event
     * 
     * @param itemLayout
     *            the layout that includes the title view items
     * @param event
     *            the event being displayed
     */
    private void setUpTitleRow(View itemLayout, Event event)
    {
        TextView status = (TextView) itemLayout.findViewById(R.id.claim_status_icon);
        // Show the proper stqtus
        if (Constants.EVENT_STATUS_SUBMITTED.equals(event.getEventStatus()))
        {
            Timestamp sentDate = event.getSubmitDateTime();
            if (sentDate != null)
            {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                status.setText(formatter.format(sentDate));
            }
        }
        else
        {
            status.setText("");
        }
    }
}
