package uk.co.ataulmunim.android.stacks;

import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

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
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_item_stacks, null);
		}
		
		final TextView plansTextView = (TextView) convertView.findViewById(R.id.listitem_plans);
		// TODO: this should be acquired via the cursor using Stacks._ID
		final Uri stackUri = Stacks.CONTENT_URI;
		
		
		if (cachedPlans.get(position) == null) {
			// Use AsyncTask to query ContentProvider for a Stack's planned days.
			new CachePlansTask(plansTextView, position).execute(stackUri);
		} else {
			plansTextView.setText(cachedPlans.get(position));
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
        	// TODO: Store plans in a String:
        	// if (plans.length == 0) plans.append(<day_returned>);
        	// else plans.append(" " + <day_returned>);
        	// Update the cache
        	StacksCursorAdapter.this.cachedPlans.put(position, plans);
        	
        	// TODO: Would this update the correct TextView if the user is scrolling madly?
        	plansTextView.setText(plans);
        }
    }
}
