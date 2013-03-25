package uk.co.ataulmunim.android.view;

import com.nicedistractions.shortstacks.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

public class ExpandView extends FrameLayout implements OnClickListener,
		OnGlobalLayoutListener {
	
	public static final String TAG = "ExpandView";
	public static final String PRIMARY_TAG = "expandinglayout.primary";
	public static final String SECONDARY_TAG = "expandinglayout.secondary";
	
	public static final int PRIMARY_INDEX = 0;
	public static final int SECONDARY_INDEX = 1;
	
	/**
	 * Used to keep an eye on layout changes - the primaryView's height is
	 * required to set the margin of the secondaryView, but only after the
	 * primaryView has finished drawing.
	 */
	protected ViewTreeObserver observer;
	
	
	/**
	 * Initial view of the ExpandView
	 */
	protected View primaryView;
	
	
	/**
	 * Expanded view of the ExpandView, slides out from under the primary
	 * view when the expand() method is called, growing the size of the
	 * ExpandView as it emerges. 
	 */
	protected View secondaryView;
	
	
	/**
	 * Used to set margins for secondary layout so it doesn't overlap primary
	 */
	protected FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
	
	public ExpandView(Context context) {
		super(context);
	}
	
	public ExpandView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ExpandView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    @Override
    public void onFinishInflate() {
    	super.onFinishInflate();
    	View pView = getChildAt(PRIMARY_INDEX);
    	View sView = getChildAt(SECONDARY_INDEX);
    	
    	if (pView != null && sView != null) {
    		setPrimaryView(pView);
    		setSecondaryView(sView);
    	}
    	else {
    		Log.w(TAG, "Unexpected number of children in ExpandView. " +
    				"Each ExpandView specified in XML should have " +
    				"exactly two children, a primary and secondary view " +
    				"specified in that order.");
    	}
    }

	/**
	 * Set the primary view - the one that's always displayed
	 * 
	 * @param view View to be set as primary
	 */
	public void setPrimaryView(View view) {
		FrameLayout.LayoutParams primaryLP = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		primaryView = view;
		primaryView.setTag(PRIMARY_TAG);
		primaryView.setOnClickListener(this);

		setObserverOnPrimary();
		
		if (findViewWithTag(PRIMARY_TAG) != null) {
			if ((String) getChildAt(PRIMARY_INDEX).getTag() == PRIMARY_TAG) {
				super.removeViewAt(PRIMARY_INDEX);
			}
			else {
				warnAboutUnexpectedView();
			}
		}
		
		super.addView(primaryView, PRIMARY_INDEX, primaryLP);
	}
	
	/**
	 * Ensures primaryView has finished drawing before getting height
	 */
	public void setObserverOnPrimary() {
		observer = primaryView.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(this);
	}
	
	/**
	 * Returns the primary view
	 * @return View primaryView
	 */
	public View getPrimaryView() { return primaryView; }
	
	
	/**
	 * Set the secondary view - the one which is revealed when the primary view
	 * is clicked.
	 * 
	 * @param view View to be set as secondary
	 */
	public void setSecondaryView(View view) {
		secondaryView = view;
		secondaryView.setTag(SECONDARY_TAG);
		secondaryView.setOnClickListener(this);
		hide();
		
		if (findViewWithTag(SECONDARY_TAG) != null) {
			if ((String) getChildAt(SECONDARY_INDEX).getTag() == SECONDARY_TAG) {
				super.removeViewAt(SECONDARY_INDEX);
			}
			else {
				warnAboutUnexpectedView();
			}
		}		
		
		super.addView(secondaryView, SECONDARY_INDEX, lp);
	}
	
	
	/**
	 * Returns the secondary view
	 * @return View secondaryView
	 */
	public View getSecondaryView() { return secondaryView; }
	
	
	/**
	 * Reveals the secondary view.
	 */
	public void show() { 
		secondaryView.setVisibility(View.VISIBLE);
		// For "viewception" (ExpandViews as secondaryViews)
		if (secondaryView instanceof ExpandView) {
			// this makes sure that when the hidden ExpandView's primary view
			// is shown, the correct height is returned from its primary! 
			((ExpandView) secondaryView).setObserverOnPrimary();
		}
	}
	
	
	/**
	 * Hides the secondary view.
	 */
	public void hide() { secondaryView.setVisibility(View.GONE); }
	

	/**
	 * Adds the secondary view again after requerying the primaryView's height.
	 * If the secondaryView is null, it'll only requery the height.
	 * 
	 * This can be called manually if any changes have been made which might
	 * cause the primaryView's height to change, like addition/removal of text
	 * or modification of primaryView's children (if it has any).
	 */
	public void refit() {
		lp.setMargins(0, primaryView.getHeight(), 0, 0);
    	
    	// if there's an existing secondary view, update it
		// (otherwise it'll get the most updated primaryView height anyway)
    	if (secondaryView != null) {
    		setSecondaryView(secondaryView);
    	}
	}
	
	
	/**
	 * Calls show()/hide() methods if primary view clicked,
	 * depending on secondary view visibility.
	 * @param view This should be the primary view, other views are ignored.
	 */
	@Override
	public void onClick(View view) {
		if ((String) view.getTag() == PRIMARY_TAG) {
			if (!(secondaryView.getVisibility() == View.VISIBLE)) show();
			else hide();
		}
		else if ((String) view.getTag() == SECONDARY_TAG) {
			// do nothing - (added otherwise it treats it as a list item)
		}
		else {
			Log.w(TAG, "Only the primary view is considered, " +
					"use an alternative OnClickListener for other views.");
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onGlobalLayout() {
		Log.d(TAG, "layout changed.");
    	refit();
    	
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			primaryView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    	} else {
			// there was a grammar mistake in the method prior to Version 15
    		primaryView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    	}	
	}
	
	
	// ////////////////////////////////////////////////////////////////////////
	// Don't use these methods directly, overrode the most common ones
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * Logs a warning - views shouldn't be directly added to this layout,
	 * see setPrimaryView(View) and setSecondaryView(view)
	 */
	@Override
	public void addView(View view) {
		warnAboutUnusedMethod("addView(View)");
	}
	
	
	@Override
	public void addView(View view, int index) {
		warnAboutUnusedMethod("addView(View, int)");
	}
	
	
	@Override
	public void removeView(View view) {
		warnAboutUnusedMethod("removeView(View)");
	}
	
	
	@Override
	public void removeViewAt(int index) {
		warnAboutUnusedMethod("removeView(int)");
	}	
	// ////////////////////////////////////////////////////////////////////////
	
	
	protected void warnAboutUnusedMethod(String methodSignature) {
		Log.w(TAG, getResources().getString(R.string.warning_unused_method) +
				" " + methodSignature);
	}
	
	
	protected void warnAboutUnexpectedView() {
		Log.w(TAG, getResources().getString(R.string.warning_unexpected_view));
	}
}
