package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.nicedistractions.shortstacks.R;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StacksActivity extends SherlockFragmentActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stacks);
		
		// TODO: assess and remove
		// clears DB, and then adds some items
		//clearAllItems();
		//addItem();
	}
	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.menu_activity_stacks, menu);
        
        return true;
    }
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	//////////////////////////////////
    /**
     * Generates and adds a random item.
     */
    private void addItem() {
        // place your content inside a ContentValues object.
        final ContentValues cv = new ContentValues();
        cv.put(Stacks.NAME, "My Item");
        cv.put(Stacks.NOTES, "Ipsum lorem dolor sit amet");
        
        
        
        // the URI of the newly created item is returned. Feel free to do whatever
        // you wish with this URI, as this is the public interface to the content.
        final Uri newItem = getContentResolver().insert(Stacks.CONTENT_URI, cv);
        if (newItem == null) {
            Toast.makeText(this, "Error inserting item. insert() returned null", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Deletes the selected item from the database.
     */
    private void deleteItem(Uri item) {
        // the second two arguments are null here, as the row is specified using the URI
        final int count = getContentResolver().delete(item, null, null);
        Toast.makeText(this, count + " rows deleted", Toast.LENGTH_SHORT).show();

    }

    /**
     * Deletes all the items from the database.
     */
    private void clearAllItems() {
        // Specify the dir URI, along with null in the where and selectionArgs
        // to delete everything.
        deleteItem(Stacks.CONTENT_URI);

    }
}
