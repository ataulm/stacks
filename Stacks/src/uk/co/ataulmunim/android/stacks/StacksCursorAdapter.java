package uk.co.ataulmunim.android.stacks;

import com.nicedistractions.shortstacks.R;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StacksCursorAdapter extends SimpleCursorAdapter {

	/**
	 * Saves Plans data to avoid requerying the content provider when the user scrolls the list.
	 */
	private SparseArray<String> cachedPlans;
	
	/**
	 * Standard constructor, with additional flag to register content observer.
	 * For use with CursorLoaders only: remember to set cursor to null in onLoaderReset()
	 * 
	 * @param context
	 * @param layout
	 * @param c
	 * @param from
	 * @param to
	 */
	public StacksCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to, 0);
		cachedPlans = new SparseArray<String>();
	}
	
	/**
	 * Get a View that displays the data at the specified position in the data set.
	 * In this override, the 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) View.inflate(mContext, R.layout.list_item_stacks, null);
		
		final String plans = cachedPlans.get(position);
		if (plans == null) {
			// Use AsyncTask to query ContentProvider for a Stack's planned days.
			new CachePlansTask((TextView) convertView.findViewById(R.id.listitem_plans), position);
		} else {
			((TextView) convertView.findViewById(R.id.listitem_plans)).setText(plans);
		}
		
		return convertView;		
	}
	
    private class CachePlansTask extends AsyncTask<Uri, Void, Cursor> {
    	private TextView plansTextView;
    	private int position;
    	
    	public CachePlansTask(TextView plansTextView, int position) {
    		super();
    		this.plansTextView = plansTextView;
    		this.position = position;
    	}
    	
        @Override
        protected Cursor doInBackground(Uri... params) {
            // TODO: query the DB for planned days for the given stack
        	// return queryDatabase(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
        	String plans = "";
        	// TODO: if there are planned days, concatenate them, then store them:
			//     plans = "Mo We Sa";
        	StacksCursorAdapter.this.cachedPlans.put(position, plans);
        	plansTextView.setText(plans);
        }
    }
}
