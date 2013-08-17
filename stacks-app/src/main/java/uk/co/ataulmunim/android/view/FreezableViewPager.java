package uk.co.ataulmunim.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FreezableViewPager extends ViewPager {
	private boolean isFrozen;
	
	public FreezableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isFrozen) {
            return false;
        }
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (isFrozen) {
            return false;
        }
		
		return super.onInterceptTouchEvent(event);
	}
	
	/**
	 * Gets the value of the isFrozen flag.
     *
	 * @return isFrozen  if true, paging is disabled
	 */
	public boolean isFrozen() {
		return isFrozen;
	}
	
	/**
	 * Sets the value of the isFrozen flag.
     *
     * @param freeze  if true, paging will be disabled
     */
	public void setFrozen(boolean freeze) {
		isFrozen = freeze;
	}
}
