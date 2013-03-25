package uk.co.ataulmunim.android.stacks.contentprovider;

import android.net.Uri;
import edu.mit.mobile.android.content.ContentItem;
import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.UriPath;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.DBForeignKeyColumn;
import edu.mit.mobile.android.content.column.IntegerColumn;

/**
 * Plans is the database table storing information about when to focus on any
 * given Stack at a time.
 * 
 * Each record stores a STACK_id (a foreign key for the _id column in the
 * Stacks table), a DAY (an integer value for each day of the week), and a
 * VALUE column, storing the value for a given key pair <stack_id, day>.
 * 
 * VALUE can be one of 16 integer values and represent when a Stack requires
 * the user's attention on a given day, with values ranging from
 * "SOMETIME_TODAY" to any combination of "EARLY_AM, LATE_AM, EARLY_PM,
 * LATE_PM" (of which there are 15 possible combinations).
 * 
 * Requests for the value of a key pair will return null, or the first instance
 * found; as such, only one record per key pair should be in this table.
 * 
 * @author ataulm
 * 
 * @deprecated This class is deprecated because it won't feature directly in
 * this app anymore - rather, if and when "This Week (I Will)" is finished,
 * each Stack will have the ability to be associated with one and only one Plan
 * in the other app, with a link (Intent) between the two. This class will be
 * entirely removed prior to production, after it's ensured that it's no longer
 * needed. 
 *
 */
@UriPath(Plans.PATH)
@Deprecated
public class Plans implements ContentItem {
	
	// Column definitions /////////////////////////////////////////////////////

	// ContentItem contains one column definition for the BaseColumns._ID which
    // defines the primary key.
	
	/**
	 * The stack item that this plan is associated with - a foreign key mapping
	 * to the _id column in the Stacks table.
	 */
	@DBForeignKeyColumn(parent = Stacks.class, notnull = true)
    public static final String STACK = "stack";
	
	/**
	 * Day that this row represents - can be one of the days of the week where
	 * days are represented as integers (constants provided in this class).
	 */
	@DBColumn(type = IntegerColumn.class, notnull = true)
    public static final String DAY = "day";
	
	/**
	 * Value that this row represents - can be one of the 16 values listed as
	 * constants in this class.
	 */
	@DBColumn(type = IntegerColumn.class, notnull = true)
    public static final String VALUE = "value";
	
	// End of Column definitions //////////////////////////////////////////////////////////////////
	
	// This defines the path component of the content URI.
    // For most instances, it's best to just use the classname here:
    public static final String PATH = "plans";
    
    // The SimpleContentProvider constructs content URIs based on your provided
    // path and authority.
    // This constant is not necessary, but is very handy for doing queries.
    public static final Uri CONTENT_URI = ProviderUtils.toContentUri(
            StacksContentProvider.AUTHORITY, PATH);

    // Day mapping
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;
    
    /**
     * Returns day mapping as String
     * @param code
     * @return
     */
    public static String getDayShort(int code) {
    	switch (code) {
	    	case MONDAY:
	    		return "Mo";
	    	case TUESDAY:
	    		return "Tu";
	    	case WEDNESDAY:
	    		return "We";
	    	case THURSDAY:
	    		return "Th";
	    	case FRIDAY:
	    		return "Fr";
	    	case SATURDAY:
	    		return "Sa";
	    	case SUNDAY:
	    		return "Su";
    		default:
    			return "";
    	}
    }
    
    // Value combinations
    public static final int SOMETIME_TODAY = 1;
    public static final int EARLY_AM = 2;
    public static final int LATE_AM = 3;
    public static final int EARLY_PM = 4;
    public static final int LATE_PM = 5;    
    public static final int EARLY_AM_AND_LATE_AM = 6;
    public static final int EARLY_AM_AND_EARLY_PM = 7;
    public static final int EARLY_AM_AND_LATE_PM = 8;
    public static final int EARLY_PM_AND_LATE_AM = 9;
    public static final int EARLY_PM_AND_LATE_PM = 10;
    public static final int LATE_AM_AND_LATE_PM = 11;    
    public static final int EARLY_AM_AND_LATE_AM_AND_LATE_PM = 12;
    public static final int EARLY_AM_AND_EARLY_PM_AND_LATE_PM = 13;
    public static final int EARLY_PM_AND_LATE_AM_AND_LATE_PM = 14;
    public static final int EARLY_AM_AND_EARLY_PM_AND_LATE_AM = 15;
    public static final int EARLY_AM_AND_EARLY_PM_AND_LATE_AM_AND_LATE_PM = 16;
        
}
