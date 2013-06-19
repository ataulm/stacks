package uk.co.ataulmunim.android.view;

import com.nicedistractions.shortstacks.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/*
 * FIXME TODO: IMPORTANT: It appears that using custom enums in styles
 * is broken on some versions of Android. You may have to change
 * the type of the attr from enum to string, which will unfortunately
 * remove the ability to use autocomplete and guarantee you've typed
 * the entry correctly. You'll also have to do a string comparison
 * instead of a switch in the parseAttributes() method.
 */
public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs); //I'll explain this method later
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
    }
	
    protected void parseAttributes(Context context, AttributeSet attrs) {
    	TypefaceLoader loader = TypefaceLoader.getInstance(context);
    	TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
    	
        //The value 0 is a default, but shouldn't ever be used since the attr is an enum
        int typeface = values.getInt(R.styleable.FontTextView_typeface, 0);
        values.recycle();
        
        setTypeface(loader.getTypeface(typeface));
    }
    
}

/**
 * Loads all the Typefaces that it knows about.
 * <p>
 * To add Typefaces to this class:
 * Update the "typeface" attribute "res/values/attrs.xml" with a new enum (name
 * and value).
 * Add a constant here for external classes to refer to types. This constant
 * should match the value in "res/values/attrs.xml".
 * Add a private variable for the new Typeface.
 * Update the private constructor so it loads the new Typeface.
 * Update the switch statement in {@link FontTextView#getTypeface()} so it
 * serves the correct Typeface.
 * Don't forget to add the font asset to the "fonts" directory under "assets".
 * 
 * @author ataulm
 *
 */
class TypefaceLoader {
	public static final int ROBOTO_LIGHT = 0;
	public static final int ROBOTO_THIN = 1;
		
	private static TypefaceLoader instance;
	private Typeface robotoLight, robotoThin;	

	public static TypefaceLoader getInstance(Context context) {
		if (instance == null) instance = new TypefaceLoader(context);
		return instance;
	}
	
	public Typeface getTypeface(int type) {
		switch (type) {
			case ROBOTO_LIGHT:
				return robotoLight;
			case ROBOTO_THIN:
				return robotoThin;
		}
		return null;
	}
	
	private TypefaceLoader(Context context) {
		robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/ROBOTO-LIGHT.TTF");
		robotoThin = Typeface.createFromAsset(context.getAssets(), "fonts/ROBOTO-THIN.TTF");
	}	
}