package uk.co.ataulmunim.android.stacks.adapter;

import uk.co.ataulmunim.android.stacks.fragment.StacksEditFragment;
import uk.co.ataulmunim.android.stacks.fragment.StacksListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class StacksPagerAdapter extends FragmentPagerAdapter{

	public static final String LOG_TAG = "StacksPagerAdapter";

	public static final int STACKS_PAGE = 0;
	public static final int EDIT_PAGE = 1;
	
	// Each StacksPagerAdapter is associated with a StacksListFragment and StacksEditFragment
	private final Fragment[] content = {new StacksListFragment(), new StacksEditFragment()};
	
	public StacksPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	/**
	 * Returns the instance of the Fragment at the specified position
	 */
	@Override
	public Fragment getItem(int position) {
		Log.d(LOG_TAG, "Pager requested page: " + position);
		return content[position % content.length];
	}

	@Override
	public int getCount() {
		return content.length;
	}

}
