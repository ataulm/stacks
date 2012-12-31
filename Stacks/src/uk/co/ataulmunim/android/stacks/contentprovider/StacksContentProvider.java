package uk.co.ataulmunim.android.stacks.contentprovider;

import android.content.ContentValues;
import android.net.Uri;
import edu.mit.mobile.android.content.GenericDBHelper;
import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.SimpleContentProvider;
import edu.mit.mobile.android.content.dbhelper.SearchDBHelper;

public class StacksContentProvider extends SimpleContentProvider {

	// Each ContentProvider must have a globally unique authority. You should
    // specify one here starting from your Application's package string:
    public static final String AUTHORITY = "uk.co.ataulmunim.android.stacks.contentprovider";
    
    public static final String SEARCH_PATH = null;

    public static final Uri SEARCH = ProviderUtils.toContentUri(AUTHORITY,
            getSearchPath(SEARCH_PATH));

    // Every time you update your database schema, you must increment the
    // database version.
    private static final int DB_VERSION = 4;
    
    public StacksContentProvider() {
    	super(AUTHORITY, DB_VERSION);
    	
    	final GenericDBHelper stacksHelper = new GenericDBHelper(Stacks.class);

    	// This adds a mapping between the given content:// URI path and the
        // helper.
    	addDirAndItemUri(stacksHelper, Stacks.PATH);
    	
    	// the above two statements can be repeated to create multiple data
        // stores. Each will have separate tables and URIs.
    	
    	final GenericDBHelper datesHelper = new GenericDBHelper(Dates.class);
    	addDirAndItemUri(datesHelper, Dates.PATH);
    	
    	final GenericDBHelper plansHelper = new GenericDBHelper(Plans.class);
    	addDirAndItemUri(plansHelper, Plans.PATH);
    	
    	// TODO: register the search helpers for Stacks and Dates after deciding on schema
        // this hooks in search
//        final SearchDBHelper searchHelper = new SearchDBHelper();
//        
//        searchHelper.registerDBHelper(messageHelper, Message.CONTENT_URI, Message.TITLE,
//                Message.BODY, Message.TITLE, Message.BODY);
    	
    	
    }
    
    /**
     * 
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

    	getContext().getContentResolver().notifyChange(uri, null);
    	return uri;
    	
    }
    
    
}
