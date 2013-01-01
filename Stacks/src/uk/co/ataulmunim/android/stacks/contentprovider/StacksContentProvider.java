package uk.co.ataulmunim.android.stacks.contentprovider;

import android.net.Uri;
import edu.mit.mobile.android.content.ForeignKeyDBHelper;
import edu.mit.mobile.android.content.GenericDBHelper;
import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.SimpleContentProvider;

public class StacksContentProvider extends SimpleContentProvider {

	// Each ContentProvider must have a globally unique authority. You should
    // specify one here starting from your Application's package string:
    public static final String AUTHORITY = "uk.co.ataulmunim.android.stacks.contentprovider";
    
    public static final String SEARCH_PATH = null;

    public static final Uri SEARCH = ProviderUtils.toContentUri(AUTHORITY,
            getSearchPath(SEARCH_PATH));

    // Every time you update your database schema, you must increment the database version
    private static final int DB_VERSION = 1;
    
    public StacksContentProvider() {
    	super(AUTHORITY, DB_VERSION);
    	
    	final GenericDBHelper stacksHelper = new GenericDBHelper(Stacks.class);
    	final ForeignKeyDBHelper datesHelper = new ForeignKeyDBHelper(Stacks.class, Dates.class,
    			Dates.STACK);
    	final ForeignKeyDBHelper plansHelper = new ForeignKeyDBHelper(Stacks.class, Plans.class,
    			Plans.STACK);
    	
    	addDirAndItemUri(stacksHelper, Stacks.PATH);
    	addChildDirAndItemUri(datesHelper, Stacks.PATH,  Dates.PATH);
    	addChildDirAndItemUri(plansHelper, Stacks.PATH,  Plans.PATH);
    	    	
    	// TODO: add search interface
    }
}
