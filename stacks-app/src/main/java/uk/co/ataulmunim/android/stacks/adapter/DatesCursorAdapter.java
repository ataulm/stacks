package uk.co.ataulmunim.android.stacks.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Returns views for dates
 * @author ataulm
 *
 */
public class DatesCursorAdapter extends SimpleCursorAdapter {

	public static final String TAG = DatesCursorAdapter.class.getSimpleName();
	
	/**
	 * Standard constructor, with additional flag for no requery.
	 * 
	 * @param context
	 * @param layout
	 * @param c
	 * @param from
	 * @param to
	 */
	public DatesCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to, 0);
	}
	
	/**
	 * Get a View that displays the data at the specified position in the data set.
	 * 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = super.getView(position, convertView, parent);
		ViewHolder holder = (ViewHolder) row.getTag();
		
		// int stackId = getCursor().getInt(getCursor().getColumnIndex(Stacks._ID));		
		
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
		
		return row;
	}
	
    /**
     * The ViewHolder pattern allows reduction in number of findViewById() calls we need to do
     * @author ataulm
     *
     */
    class ViewHolder {
    	TextView actionItems = null;
    	
    	ViewHolder(View row) {
    		//this.actionItems = (TextView) row.findViewById(R.id.listitem_actionable_items);
    	}
    }
}
