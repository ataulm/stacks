package uk.co.ataulmunim.android.stacks;

import java.util.UUID;

import uk.co.ataulmunim.android.stacks.contentprovider.Dates;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.nicedistractions.shortstacks.R;


/**
 * Contains static methods for CRUD operations in Stacks
 * @author ataulm
 *
 */
public abstract class Crud {
	public static final String LOG_TAG = "CRUD";

	/**
	 * Adds the root Stack.
	 * This is created on first open of the application.
	 * @param context
	 * @return Uri The Uri of the newly added stack, or null if error inserting.
	 */
	public static Uri addDefaultStack(Context context) {
		return Crud.addStack(context, "/home",
				"This is the default stack; all other stacks are added here.",
				Stacks.ROOT_STACK_ID);
	}
	
	/**
	 * Add a new stack
	 * @param context
	 * @param parent
	 */
	public static Uri addStack(Context context, String shortcode, String notes, int parent) {
		Log.d(LOG_TAG, "Trying to add stack");
		
		// place your content inside a ContentValues object.
	    final ContentValues values = new ContentValues();
	    values.put(Stacks.UUID, UUID.randomUUID().toString());
	    values.put(Stacks.SHORTCODE, shortcode);
	    values.put(Stacks.NOTES, notes);
	    values.put(Stacks.PARENT, parent);
	    values.put(Stacks.ACTION_ITEMS, 0);
	    values.put(Stacks.DELETED, 0);
	    
	    // the URI of the newly created item is returned.
	    final Uri addedStack = context.getContentResolver().insert(Stacks.CONTENT_URI, values);
	    
	    if (addedStack == null) Log.e(LOG_TAG, context.getString(R.string.error_insert));
	    else Log.d(LOG_TAG, "Added stack: " + addedStack.toString());
	    
	    return addedStack;
	}
	
	/**
	 * Removes everything from every table
	 * @param context
	 */
	public static void removeAll(Context context) {
		
	}

	/**
	 * Temporary method to add pre-determined stacks
	 * TODO: assess and remove
	 * @param context
	 * @param parent
	 * @return
	 */
	public static Uri genStack(Context context, int parent) {
		return addStack(context, "A new item", "A new item which has been auto-generated", parent);
	}
	
	public static Uri genDates(Context context, int stack, long date) {
		return null;		
	}
	
	/**
	 * Gets the children of the Stack with the passed id, if they are not marked for deletion.
	 * 
	 * Returns a Cursor of _IDs from the Stacks table where the Stacks match the passed parent id,
	 * but are not marked as deleted.
	 * 
	 * @param cr
	 * @param uri
	 * @param projection
	 * @param parentId Comment.BODY + "=?"
	 * @param selectionArgs
	 * @param sortOrder
	 * @return Cursor cursor over the resulting columns
	 */
    public static Cursor getStacksWithParent(ContentResolver cr, int parentId) {
    	return cr.query(Stacks.CONTENT_URI, new String[] { Stacks._ID }, 
    			Stacks.PARENT + "=" + parentId + " AND " + Stacks.DELETED + "= 0", null, null);
    }
	
	/**
	 * Adds a date item associated with a stack.
	 * @param context
	 * @param stack
	 * @param dateInMillis
	 * @param desc
	 * @return
	 */
	public static Uri addDate(Context context, int stack, long dateInMillis, String desc) {
		
		final ContentValues values = new ContentValues();
		values.put(Dates.STACK, stack);
		values.put(Dates.DATE, dateInMillis);
		values.put(Dates.ABOUT, desc);
		
		final Uri addedDate = context.getContentResolver().insert(
				Dates.CONTENT_URI, values);
		
		if (addedDate == null) Log.e(LOG_TAG, context.getString(R.string.error_insert));
		
		return addedDate;
	}
	

}
