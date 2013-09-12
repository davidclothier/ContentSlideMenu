/**
 * David Ropero - @clothierdroid
 */
package com.cafetaso.itemslidemenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cafetaso.elementslidemenu.R;

/**
 * Compound view to manage item of slide menu
 * 
 * @author David Ropero
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ItemSlideMenu extends LinearLayout implements OnClickListener,Checkable {

	private View mLine;
	private ImageView mIcon;
	private TextView mTitle;
	private TextView mInfo;
	private boolean mChecked = false;
	private int mStateCheckedBackgroundColor = Color.parseColor("#414141");
	private int mStateNormalBackgroundColor = Color.parseColor("#5B5B5B");
	private int mLineColor = Color.WHITE;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ItemSlideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context,attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ItemSlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,attrs);
	}

	/**
     * Interface definition for a callback to be invoked when the checked state of this View is
     * changed.
     */
    public static interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param checkableView The view whose state has changed.
         * @param isChecked     The new checked state of checkableView.
         */
        void onCheckedChanged(View checkableView, boolean isChecked);
    }
        
    /**
     * Register a callback to be invoked when the checked state of this view changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }
    
	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean value) {
		if (value != mChecked) {
            mChecked = value;
            applyColor();
            refreshDrawableState();

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	@Override
	public void onClick(View v) {
		toggle();
	}
	
	private void applyColor() {
        if (mChecked) {
        	setBackgroundColor(mStateCheckedBackgroundColor);
			mLine.setBackgroundColor(mLineColor);
		} else {
			setBackgroundColor(mStateNormalBackgroundColor);
			mLine.setBackgroundColor(mStateNormalBackgroundColor);
		}
	}
	
	/**
	 * Configure our custom view
	 * 
	 * @param context
	 */
	private void initView(Context context,AttributeSet attrs) {
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setOrientation(HORIZONTAL);
		
		
		/*
		 *  Inflate and get references
		 */
		if(isInEditMode()) {
			inflater.inflate(android.R.layout.simple_list_item_1, this,true);
			TextView tv = (TextView) findViewById(android.R.id.text1);
			tv.setText("slide menu element (design mode)");
			tv.setGravity(Gravity.CENTER);
			tv.setBackgroundColor(this.mStateNormalBackgroundColor);
			tv.setTextColor(Color.WHITE);
			return;
		} else {
			inflater.inflate(R.layout.element, this, true);			
		}

		setOnClickListener(this);
		
		mLine = (View) findViewById(R.id.line);
		mIcon = (ImageView) findViewById(R.id.icon);
		mTitle = (TextView) findViewById(R.id.title);
		mInfo = (TextView) findViewById(R.id.info);
		
		/*
		 * Apply attributes
		 */
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemSlideMenu);
		
		// Parent layout
		setClickable(true);
		
		if (a.getString(R.styleable.ItemSlideMenu_state_checked_background_color) != null)
			mStateCheckedBackgroundColor = Color.parseColor(a.getString(R.styleable.ItemSlideMenu_state_checked_background_color));

		if (a.getString(R.styleable.ItemSlideMenu_state_normal_background_color) != null)
			mStateNormalBackgroundColor = Color.parseColor(a.getString(R.styleable.ItemSlideMenu_state_normal_background_color));

		if (a.getString(R.styleable.ItemSlideMenu_line_color) != null)
			mLineColor = Color.parseColor(a.getString(R.styleable.ItemSlideMenu_line_color));
		
		// State
		mChecked = a.getBoolean(R.styleable.ItemSlideMenu_checked, false);
		applyColor();		
						
		// Title 
		if (a.getString(R.styleable.ItemSlideMenu_titleText) != null) mTitle.setText(a.getString(R.styleable.ItemSlideMenu_titleText));
		
		// Info
		if (a.getString(R.styleable.ItemSlideMenu_infoText) != null) mInfo.setText(a.getString(R.styleable.ItemSlideMenu_infoText));
		if (a.getString(R.styleable.ItemSlideMenu_infoTextColor) != null) mInfo.setTextColor(Color.parseColor(a.getString(R.styleable.ItemSlideMenu_infoTextColor)));
		if (a.getString(R.styleable.ItemSlideMenu_infoBackColor) != null) {
			GradientDrawable background = (GradientDrawable) mInfo.getBackground();
			background.setColor(Color.parseColor(a.getString(R.styleable.ItemSlideMenu_infoBackColor)));
		}
		
		// Icon
		Drawable drawable = a.getDrawable(R.styleable.ItemSlideMenu_iconDrawable);
		if (drawable != null) mIcon.setImageDrawable(drawable);
		int visible = a.getBoolean(R.styleable.ItemSlideMenu_infoVisible, false) ? View.VISIBLE : View.GONE;
		mInfo.setVisibility(visible);
		
		a.recycle();
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
	}
	
	/**
	 * Set the icon resource (resId)
	 * 
	 * @param icon
	 */
	public void setIcon(int idIcon) {
		mIcon.setBackgroundResource(idIcon);
	}

	/**
	 * Set the title
	 * 
	 * @param title
	 */
	public void setTitle(CharSequence title) {
		mTitle.setText(title);
		invalidate();
		requestLayout();
	}

	/**
	 * Set the info text
	 * 
	 * @param info
	 */
	public void setInfo(CharSequence info) {
		mInfo.setText(info);
		invalidate();
		requestLayout();
	}

	/**
	 * Set the info visibility
	 * 
	 * @param infoVisibility
	 */
	public void setInfoVisibility(boolean infoVisibility) {

		int visibility;

		visibility = infoVisibility ? View.VISIBLE : View.GONE;
		mInfo.setVisibility(visibility);
		invalidate();
		requestLayout();
	}

	/**
	 * Set the typeface for all textview
	 * 
	 * @param typeface
	 */
	public void setCustomTypeface(Typeface typeface) {
		mTitle.setTypeface(typeface);
		mInfo.setTypeface(typeface);
		invalidate();
		requestLayout();
	}

}
