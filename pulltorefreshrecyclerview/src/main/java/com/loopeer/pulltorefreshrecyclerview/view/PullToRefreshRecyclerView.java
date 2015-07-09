package com.loopeer.pulltorefreshrecyclerview.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by loopeer on 2015/7/9.
 */
public class PullToRefreshRecyclerView extends RecyclerView {

    private static final int SPLASH_DISPLAY_LENGTH = 200;

    public static final String TAG = "PullToRefreshRecyclerView";

    public static final int OVER_OFFSET = 20;
    public static final int PULL_OFFSET = 50;

    public enum OverScrollType{BOTTOM, TOP, BOTTOM_TOP};
    private OverScrollType mScrollType;

    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private boolean isPull = true;
    private boolean isTop = true;
    private boolean isBottom = false;
    private int scrollState = 0;
    int startY = 0;

    //TODO test
    private TextView mTextView;

    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setScrollType(OverScrollType scrollType) {
        mScrollType = scrollType;
    }

    private void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
        mScrollType = OverScrollType.BOTTOM_TOP;
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        addOnScrollListener(new OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollState = newState;
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollType == OverScrollType.TOP && dy < 0) {

                }
                int visibleItemCount = getChildCount();
                int totalItemCount = getLayoutManager().getItemCount();
                int firstVisibleItem =
                        ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                isTop = firstVisibleItem == 0;
                isBottom = firstVisibleItem + visibleItemCount == totalItemCount;
            }
        });
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {

        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = y;
            case MotionEvent.ACTION_MOVE:
                float historicalY = y;
                isPull = startY - y < 0;
                int dy = (int) (historicalY - startY) / 3;

                switch (mScrollType) {
                    case TOP:
                        if (dy < 0) return false;
                        if (isPull && isTop && scrollState != SCROLL_STATE_SETTLING) {
                            if (dy < PULL_OFFSET) break;
                            setPadding(paddingLeft, dy, paddingRight, paddingBottom);
                            mTextView.setVisibility(VISIBLE);
                            ObjectAnimator.ofFloat(mTextView, "Y", startY, dy).start();
                            getLayoutManager().scrollToPosition(0);
                            requestLayout();
                        }
                        break;
                    case BOTTOM:
                        if (!isPull && isBottom && scrollState != SCROLL_STATE_SETTLING) {
                            setPadding(paddingLeft, paddingTop, paddingRight, -dy);
                            getLayoutManager().scrollToPosition(getLayoutManager().getItemCount());
                            requestLayout();
                        }
                        break;/*
          case BOTTOM_TOP:
            if (isPull && isTop && scrollState != SCROLL_STATE_SETTLING) {
              if (dy < PULL_OFFSET) break;
              setPadding(paddingLeft, dy, paddingRight, paddingBottom);
              getLayoutManager().scrollToPosition(0);
              requestLayout();
            }
            if (!isPull && isBottom && scrollState != SCROLL_STATE_SETTLING) {
              setPadding(paddingLeft, paddingTop, paddingRight, -dy);
              getLayoutManager().scrollToPosition(getLayoutManager().getItemCount());
              requestLayout();
            }
            break;*/
                }

                return super.dispatchTouchEvent(ev);
            case MotionEvent.ACTION_UP:// »Øµ¯
                if (isPull && isTop) {
                    if (mScrollType == OverScrollType.BOTTOM) break;
                    int top = getPaddingTop();
                    startAnimation(top, false);
                }
                if (!isPull && isBottom) {
                    if (mScrollType == OverScrollType.TOP) break;
                    int bottom = getPaddingBottom();
                    startAnimation(bottom, true);
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setTextView(TextView textView) {
        mTextView = textView;
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = y;
            case MotionEvent.ACTION_MOVE:
                float historicalY = y;
                isPull = startY - y < 0;
                int dy = (int) (historicalY - startY) / 3;

                switch (mScrollType) {
                    case TOP:
                        if (dy < 0) return false;
                        if (isPull && isTop && scrollState != SCROLL_STATE_SETTLING) {
                            if (dy < PULL_OFFSET) break;
                            setPadding(paddingLeft, dy, paddingRight, paddingBottom);
                            getLayoutManager().scrollToPosition(0);
                            requestLayout();
                        }
                        break;
                    case BOTTOM:
                        if (!isPull && isBottom && scrollState != SCROLL_STATE_SETTLING) {
                            setPadding(paddingLeft, paddingTop, paddingRight, -dy);
                            getLayoutManager().scrollToPosition(getLayoutManager().getItemCount());
                            requestLayout();
                        }
                        break;
                }
                return super.onInterceptTouchEvent(e);
            case MotionEvent.ACTION_UP:
                return false;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        return true;
    }

    public void startAnimation(final int padding, final boolean isBottom) {
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(1.0f, 0);
        animator.setDuration(SPLASH_DISPLAY_LENGTH);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();
                int p = (int) (padding * value);
                if (isBottom) {
                    setPadding(paddingLeft, paddingTop, paddingRight, p);
                } else {
                    setPadding(paddingLeft, p, paddingRight, paddingBottom);
                }
            }
        });

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isBottom && getPaddingBottom() < OVER_OFFSET || !isBottom && getPaddingTop() < OVER_OFFSET) {
                            return;
                        }
                    }
                }, 200);
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
            }

            @Override
            public void onAnimationCancel(Animator arg0) {

            }
        });

        animator.start();
    }
}
