package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.adapter.StacksPagerAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.nicedistractions.shortstacks.R;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.actionbarsherlock.view.MenuItem;
import android.widget.Toast;

public class StacksActivity extends SherlockFragmentActivity {
	private static int counter = 0;
	StacksPagerAdapter adapter;
	ViewPager pager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stacks);
			
		Log.d("StacksActivity","Number of instances: " + ++counter);
		
		adapter = new StacksPagerAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.menu_activity_stacks, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                Toast.makeText(this, "Entering edit mode", Toast.LENGTH_SHORT).show();
                pager.setCurrentItem(StacksPagerAdapter.EDIT_PAGE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}
