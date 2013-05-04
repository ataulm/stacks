package uk.co.ataulmunim.android.stacks.contentprovider;

import android.net.Uri;
import edu.mit.mobile.android.content.ContentItem;
import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.UriPath;
import edu.mit.mobile.android.content.column.BooleanColumn;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.DBForeignKeyColumn;
import edu.mit.mobile.android.content.column.DatetimeColumn;
import edu.mit.mobile.android.content.column.IntegerColumn;
import edu.mit.mobile.android.content.column.TextColumn;

/**
 * Dates is the database table storing dates associated with Stacks. There is
 * no assumption made about the association with a Stack; the user can append a
 * name for each date associated.
 *  
 * @author ataulm
 *
 */
@UriPath(Dates.PATH)
public class Dates implements ContentItem {
	
	// Column definitions /////////////////////////////////////////////////////

	// ContentItem contains one column definition for the BaseColumns._ID which
    // defines the primary key.
	// ========================================================================
    // Column Name		type		description
    // ========================================================================
    // _id				integer		primary key (autogenerated) 
    // ------------------------------------------------------------------------
    // stack			integer		foreign key from Stacks table for
	//								associated stack
    // ------------------------------------------------------------------------    
    // date				long		date to store (milliseconds since Epoch)
    // ------------------------------------------------------------------------    
    // about			string		single line description about this date
	// ------------------------------------------------------------------------    
    // notification		boolean		should produce notification?		
	// ========================================================================
	
	/**
	 * The stack item that this date is associated with - a foreign key mapping
	 * to the _id column in the Stacks table.
	 * 
	 * This creates a foreign key relationship to the stack. In effect, this is
	 * the child storing the ID of its parent. The ForeignKeyManager will help
	 * access this relationship.
	 */
	@DBForeignKeyColumn(parent = Stacks.class)
    public static final String STACK = "stack";
	
	/**
	 * Date that this row represents
	 */
	@DBColumn(type = DatetimeColumn.class)
    public static final String DATE = "date";
	
	/**
	 * A single line description about the date
	 */
	@DBColumn(type = TextColumn.class)
    public static final String ABOUT = "about";
	
	/**
	 * Flag to indicate whether this date should invoke a notification when it
	 * arrives.
	 */
	@DBColumn(type = BooleanColumn.class, defaultValue = "0")
    public static final String NOTIFICATION = "notification";
	
	// End of Column definitions //////////////////////////////////////////////
	
	// This defines the path component of the content URI.
    // For most instances, it's best to just use the classname here:
    public static final String PATH = "dates";
    
    // The SimpleContentProvider constructs content URIs based on your provided
    // path and authority.
    // This constant is not necessary, but is very handy for doing queries.
    public static final Uri CONTENT_URI = ProviderUtils.toContentUri(
            StacksContentProvider.AUTHORITY, PATH);

}