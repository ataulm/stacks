package uk.co.ataulmunim.android.stacks;

import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

import com.nicedistractions.shortstacks.R;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StacksCursorAdapter extends SimpleCursorAdapter {

	public static final String LOG_TAG = "StacksCursorAdapter";
	/**
	 * Saves Plans data to avoid requerying the content provider when the user scrolls the list.
	 */
	private SparseArray<String> cachedPlans;
	
	/**
	 * Standard constructor, with additional flag to register content observer.
	 * 
	 * @param context
	 * @param layout
	 * @param c
	 * @param from
	 * @param to
	 */
	public StacksCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to, 0);
	}
	
	/**
	 * Get a View that displays the data at the specified position in the data set.
	 * In this override, the 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = super.getView(position, convertView, parent);
		ViewHolder holder = (ViewHolder) row.getTag();
		
		int stackId = getCursor().getInt(getCursor().getColumnIndex(Stacks._ID));		
		
		if (holder == null) {
			holder = new ViewHolder(row);
			row.setTag(holder);
		}
		
		// Actionable items
		if (holder.actionItems.getText().toString() != "" &&
				Integer.parseInt(holder.actionItems.getText().toString()) < 1) {
			holder.actionItems.setVisibility(View.GONE);
		}
		else holder.actionItems.setVisibility(View.VISIBLE);
		
		// Plans
		if (cachedPlans != null && cachedPlans.get(stackId) != null) {
			holder.plans.setText(cachedPlans.get(stackId));
		}
		else holder.plans.setText("");
		
		return row;
	}
	
	/**
	 * Updates the cache of Plans from the Fragment so
	 * {@link StacksCursorAdapter#getView(int, View, ViewGroup)} can update rows with plans.
	 * 
	 * @param cachedPlans
	 */
	public void setCachedPlans(SparseArray<String> cachedPlans) {
		this.cachedPlans = cachedPlans;
	}
	
    /**
     * The ViewHolder pattern allows reduction in number of findViewById() calls we need to do
     * @author ataulm
     *
     */
    class ViewHolder {
    	TextView plans = null;
    	TextView actionItems = null;
    	
    	ViewHolder(View row) {
    		this.plans = (TextView) row.findViewById(R.id.listitem_plans);
    		this.actionItems = (TextView) row.findViewById(R.id.listitem_actionable_items);
    	}
    }
}
