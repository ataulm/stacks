package uk.co.ataulmunim.android.stacks.contentprovider;

import android.net.Uri;
import edu.mit.mobile.android.content.ContentItem;
import edu.mit.mobile.android.content.ForeignKeyManager;
import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.UriPath;
import edu.mit.mobile.android.content.column.BooleanColumn;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.DatetimeColumn;
import edu.mit.mobile.android.content.column.IntegerColumn;
import edu.mit.mobile.android.content.column.TextColumn;


@UriPath(Stacks.PATH)
public class Stacks implements ContentItem {
	
	/**
     * Defines the path component of the content URI.
     */
	public static final String PATH = "stack";
    
    // Column definitions /////////////////////////////////////////////////////////////////////////

	// ContentItem contains one column definition for the BaseColumns._ID which defines the primary
	// key, so it doesn't need to be declared explicitly
    // ============================================================================================
    // Column Name		type		description
    // ============================================================================================
    // _id				integer		primary key (autogenerated) 
    // --------------------------------------------------------------------------------------------
    // name				string		name/title of the stack
    // --------------------------------------------------------------------------------------------    
    // created			datetime	created date (autogenerated)
    // --------------------------------------------------------------------------------------------
    // modified			datetime	last time this item (or associated record in other tables)
    //								modified
    // --------------------------------------------------------------------------------------------
    // notes			string		a longer description about the item 
    // --------------------------------------------------------------------------------------------
    // parent			int			id of the parent item (corresponds to the parent's _id value)
    // --------------------------------------------------------------------------------------------
    // actionitems		int			number of actionable items in the below tree (leaf nodes)
    // --------------------------------------------------------------------------------------------
    // starred			boolean		flag to indicate starred or not.    
    // --------------------------------------------------------------------------------------------
    // deleted			datetime	timestamp when Stack was marked for	deletion
    // --------------------------------------------------------------------------------------------    
    // localsort		integer		list position of this stack in parent stack
    // ============================================================================================
    
    /**
	 * A single line summary of the Stack (title, task or list item) 
	 */
	@DBColumn(type = TextColumn.class, notnull = true)
    public static final String NAME = "name";
	
	/**
	 * Automatically set current date value on creation of the Stack.
	 */
	@DBColumn(type = DatetimeColumn.class, defaultValue = DatetimeColumn.NOW_IN_MILLISECONDS)
    public static final String CREATED_DATE = "created";
	
	/**
	 * Last time this Stack, or associated items in other tables, have been
	 * modified. Used to check whether to sync.
	 */
	@DBColumn(type = DatetimeColumn.class, defaultValue = DatetimeColumn.NOW_IN_MILLISECONDS)
    public static final String MODIFIED_DATE = "modified";
	
	/**
	 * A description about the Stack
	 */
	@DBColumn(type = TextColumn.class)
    public static final String NOTES = "notes";
	
	/**
	 * The _ID of this Stack's parent, where parent is the direct ancestor in
	 * the tree.
	 */
	@DBColumn(type = IntegerColumn.class, notnull = true)
    public static final String PARENT = "parent";
	
	/**
	 * The number of action items in this Stack and its children. An action
	 * item is a leaf node.
	 */
	@DBColumn(type = IntegerColumn.class)
    public static final String ACTION_ITEMS = "actionitems";
	
	/**
	 * Flag to indicate whether this Stack has been starred by the user. If
	 * true, it will be unavailable to the user.
	 */
	@DBColumn(type = BooleanColumn.class, defaultValue = "0")
    public static final String STARRED = "starred";
	
	/**
	 * Indicates whether this Stack has been marked for deletion.
	 * The date specifies the time it was marked for deletion, and no date
	 * indicates that the Stack has not been marked for deletion.
	 */
	@DBColumn(type = DatetimeColumn.class, defaultValue = "0")
    public static final String DELETED = "deleted";
	
	/**
	 * The list position of this Stack in its parent Stack.
	 */
	@DBColumn(type = IntegerColumn.class)
    public static final String LOCAL_SORT = "localsort";
	
	// End of Column definitions //////////////////////////////////////////////////////////////////
	
	/**
     * The SimpleContentProvider constructs content URIs based on your provided
     * path and authority.
     */
    public static final Uri CONTENT_URI =
    		ProviderUtils.toContentUri(StacksContentProvider.AUTHORITY, PATH);
    
    public static final String CONTENT_TYPE =
    		"vnd.android.cursor.item/vnd." + StacksContentProvider.AUTHORITY + "." + PATH;
    
    // _ID of the root stack, which is created on first-open.
    public static final int ROOT_STACK_ID = 1;
	
	// This is a helpful tool connecting back to the "child" of this object. This is similar
    // to Django's relation manager, although we need to define it ourselves.
    public static final ForeignKeyManager DATES = new ForeignKeyManager(Dates.class);
    public static final ForeignKeyManager PLANS = new ForeignKeyManager(Plans.class);
}
