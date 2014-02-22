package com.ataulm.stacks.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.ataulm.stacks.R;
import com.ataulm.stacks.fragment.StackViewFragment;


public class StacksCursorAdapter extends SimpleCursorAdapter {

	public static final String TAG = StacksCursorAdapter.class.getSimpleName();
    private final View.OnClickListener listener;

    /**
	 * Standard constructor, with additional flag (0) for no requery.
	 * 
	 * @param context
	 * @param layout
	 * @param c
	 * @param from
	 * @param to
	 */
	public StacksCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to,
            StackViewFragment listener) {
		super(context, layout, c, from, to, 0);
        this.listener = listener;
	}
	
	/**
	 * Get a View that displays the data at the specified position in the data set.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = super.getView(position, convertView, parent);
		ViewHolder holder = (ViewHolder) row.getTag();
		
		if (holder == null) {
			holder = new ViewHolder(row);
			row.setTag(holder);
            holder.miniOverflow.setOnClickListener(listener);
		}
        holder.miniOverflow.setTag(R.id.tag_stack_position, position);

		return row;
	}
	
    /** 
     * The ViewHolder pattern allows reduction in number of `findViewById()`
     * calls we need to make, by storing a reference to the required View here.
     * <p>
     * When we retrieve a row in `getView()`, we check if it has an associated
     * tag using {@link View#getTag()}. If not, we set the ViewHolder as the
     * tag, otherwise, we retrieve the existing ViewHolder, which contains the
     * references to the Views we're after.
     *  
     * @author ataulm
     *
     */
    class ViewHolder {
    	ImageView miniOverflow = null;
    	
    	ViewHolder(View row) {
    		miniOverflow = (ImageView) row.findViewById(R.id.moreoverflow_btn);
    	}
    }
}
