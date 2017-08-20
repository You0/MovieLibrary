package me.com.movielibrary.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Me on 2017/8/8.
 */

public class AutoChangeLineLayout extends FrameLayout{


    public AutoChangeLineLayout(Context context) {
        super(context);
    }

    public AutoChangeLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoChangeLineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int LineLeft =getPaddingLeft();
        int LineRight = getMeasuredWidth();
        int LineTop = 0;
        int lastChildViewHeight = 0;
        final int count = getChildCount();
        for(int i=0;i<count;i++){
            final View childView = getChildAt(i);
            if(childView.getVisibility()!=GONE){
                FrameLayout.LayoutParams params = (LayoutParams) childView.getLayoutParams();
                int childWidth = childView.getMeasuredWidth();
                if(childWidth + LineLeft + params.leftMargin + params.rightMargin > LineRight){
                    i--;
                    LineLeft = getPaddingLeft();;
                    LineTop += childView.getMeasuredHeight() + params.topMargin;
                }else{
                    LineLeft += childWidth + params.leftMargin;
                }
                lastChildViewHeight = childView.getMeasuredHeight();
            }
        }
        LineTop += lastChildViewHeight;
        LineTop += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthMeasureSpec,MeasureSpec.makeMeasureSpec(LineTop,MeasureSpec.AT_MOST));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int LineLeft =getPaddingLeft();
        int LineRight = getMeasuredWidth() - getPaddingRight();
        int LineTop = getPaddingTop();

        final int count = getChildCount();
        for(int i=0;i<count;i++){
            final View childView = getChildAt(i);
            if(childView.getVisibility()!=GONE){
                FrameLayout.LayoutParams params = (LayoutParams) childView.getLayoutParams();

                int childWidth = childView.getMeasuredWidth();
                if(childWidth + LineLeft + params.leftMargin + params.rightMargin > LineRight){
                    i--;
                    LineLeft = getPaddingLeft();;
                    LineTop += childView.getMeasuredHeight() + params.topMargin;
                }else{
                    childView.layout(LineLeft+params.leftMargin ,LineTop,LineLeft+childWidth + params.leftMargin ,LineTop+childView.getMeasuredHeight());
                    LineLeft += childWidth + params.leftMargin;
                }
            }
        }
    }




}
