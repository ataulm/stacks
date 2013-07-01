package uk.co.ataulmunim.android.stacks.adapter;

import uk.co.ataulmunim.android.stacks.fragment.StackEditFragment;
import uk.co.ataulmunim.android.stacks.fragment.StackViewFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;

public class StacksPagerAdapter extends FragmentPagerAdapter{

	public static final String TAG = StacksPagerAdapter.class.getSimpleName();

	public static final int STACKS_PAGE = 0;
	public static final int EDIT_PAGE = 1;
	
	// Each StacksPagerAdapter is associated with a StackViewFragment and StackEditFragment
	private final Fragment[] content = {new StackViewFragment(), new StackEditFragment()};
	
	public StacksPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	/**
	 * Returns the instance of the Fragment at the specified position
	 */
	@Override
	public Fragment getItem(int position) {
		Log.d(TAG, "Pager requested page: " + position);
		return content[position % content.length];
	}

	@Override
	public int getCount() {
		return content.length;
	}
}
