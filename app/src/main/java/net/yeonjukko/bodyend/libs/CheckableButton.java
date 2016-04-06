package net.yeonjukko.bodyend.libs;

/**
 * Created by yeonjukko on 16. 3. 11..
 * <p/>
 * Created by MoonJongRak on 2016. 2. 17..
 * <p/>
 * Created by MoonJongRak on 2016. 2. 17..
 * <p/>
 * Created by MoonJongRak on 2016. 2. 17..
 */

/**
 * Created by MoonJongRak on 2016. 2. 17..
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Checkable;

public class CheckableButton extends Button implements Checkable {
    private boolean mChecked;
    private static final int[] CHECKED_STATE_LIST = new int[]{android.R.attr.state_checked};

    private boolean mBroadcasting;

    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public CheckableButton(Context context) {
        super(context);
    }

    public CheckableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_LIST);
        }
        return drawableState;
    }


    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();

            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CheckableButton buttonView, boolean isChecked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Get canvas width and height
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
