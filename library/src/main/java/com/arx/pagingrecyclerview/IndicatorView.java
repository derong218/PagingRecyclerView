package com.arx.pagingrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.google.android.material.tabs.TabLayout;

/**
 * @author Zeng Derong (derong218@gmail.com)
 * on  2019-8-3 16:15
 */
public class IndicatorView extends TabLayout {

    public static final String TAG = IndicatorView.class.getSimpleName();
    public static final int DEFAULT_INDICATOR_WIDTH = 20;
    public static final int DEFAULT_INDICATOR_HEIGHT = 20;
    public static final int DEFAULT_INDICATOR_RADIUS = 10;
    int indicatorPaddingStart;
    int indicatorPaddingTop;
    int indicatorPaddingEnd;
    int indicatorPaddingBottom;
    private int indicatorWidth;
    private int indicatorHeight;
    private int indicatorRadius;
    private int indicatorColor;
    private int indicatorSelectedColor;

    public IndicatorView(Context context) {
        super(context);
        init(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setTabIndicatorFullWidth(false);
        indicatorWidth = DensityUtil.dp2px(context, DEFAULT_INDICATOR_WIDTH);
        indicatorHeight = DensityUtil.dp2px(context, DEFAULT_INDICATOR_HEIGHT);
        indicatorRadius = DensityUtil.dp2px(context, DEFAULT_INDICATOR_RADIUS);
        indicatorColor = getResources().getColor(R.color.indicatorColorDefault);
        indicatorSelectedColor = getResources().getColor(R.color.indicatorSelectedColorDefault);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
            indicatorPaddingStart =
                    indicatorPaddingTop =
                            indicatorPaddingEnd =
                                    indicatorPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorPadding, 0);
            indicatorPaddingStart = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorPaddingStart, indicatorPaddingStart);
            indicatorPaddingTop = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorPaddingTop, indicatorPaddingTop);
            indicatorPaddingEnd = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorPaddingEnd, indicatorPaddingEnd);
            indicatorPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorPaddingBottom, indicatorPaddingBottom);
            indicatorWidth = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorWidth, indicatorWidth);
            indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorHeight, indicatorWidth);
            indicatorRadius = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_indicatorRadius, indicatorWidth / 2);
            indicatorColor = typedArray.getColor(R.styleable.IndicatorView_indicatorColor, indicatorColor);
            indicatorSelectedColor = typedArray.getColor(R.styleable.IndicatorView_indicatorSelectedColor, indicatorSelectedColor);
            typedArray.recycle();
        }

        setTabMode(TabLayout.MODE_FIXED);
        setSelectedTabIndicatorColor(Color.TRANSPARENT);
        setTabIndicatorFullWidth(false);
    }

    public void setupWithPagingRecyclerView(@NonNull final PagingRecyclerView pagingRecyclerView) {
        pagingRecyclerView.addOnPageChangeListener(new PagingRecyclerView.OnPageChangeListener() {
            @Override
            public void onPageSelected(int page) {
                TabLayout.Tab tabAt = getTabAt(page);
                if (tabAt != null) {
                    tabAt.select();
                }
            }

            @Override
            public void onPageCount(int pageCount) {
                removeAllTabs();
                for (int i = 0; i < pageCount; i++) {
                    final TabLayout.Tab tab = newTab();
                    ViewCompat.setPaddingRelative(
                            tab.view, indicatorPaddingStart, indicatorPaddingTop, indicatorPaddingEnd, indicatorPaddingBottom);
                    //设置自定义view
                    final LayoutInflater inflater = LayoutInflater.from(pagingRecyclerView.getContext());
                    View customView = inflater.inflate(R.layout.indicator_item, tab.view, false);
                    ViewGroup.LayoutParams params = customView.getLayoutParams();
                    params.width = indicatorWidth;
                    params.height = indicatorHeight;
                    customView.setLayoutParams(params);
                    tab.setCustomView(customView);

                    //selector
                    StateListDrawable drawable = new StateListDrawable();
                    GradientDrawable drawableSelected = getGradientDrawable(indicatorWidth, indicatorHeight, indicatorRadius);
                    drawableSelected.setColor(indicatorSelectedColor);
                    drawable.addState(new int[]{android.R.attr.state_selected}, drawableSelected);
                    GradientDrawable normalDrawable = getGradientDrawable(indicatorWidth, indicatorHeight, indicatorRadius);
                    normalDrawable.setColor(indicatorColor);
                    drawable.addState(new int[]{}, normalDrawable);
                    tab.setIcon(drawable);
                    addTab(tab, i == pagingRecyclerView.getPage());
                }

            }
        });
        addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagingRecyclerView.setCurrentPage(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Ignore
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Ignore
            }
        });
    }

    private GradientDrawable getGradientDrawable(int width, int height, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(width, height);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }
}
