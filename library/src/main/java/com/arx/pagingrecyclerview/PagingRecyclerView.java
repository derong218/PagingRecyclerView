package com.arx.pagingrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

/**
 * @author Zeng Derong (derong218@gmail.com)
 * on  2019-8-3 14:09
 */
public class PagingRecyclerView extends RecyclerView {
    public static final String TAG = PagingRecyclerView.class.getSimpleName();
    private static final int MIN_VELOCITY_X_TO_CHANGE_PAGE = 500;
    private int mVelocityX;
    /**
     * 每页大小
     */
    private int pageSize = 3;

    /**
     * 当前页
     */
    private int page = -1;
    /**
     * 有多少个item
     */
    private int itemCount = 0;
    /**
     * 有多少页
     */
    private int pageCount;

    private final ArrayList<OnPageChangeListener> mOnPageChangeListeners = new ArrayList<>();

    public PagingRecyclerView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public PagingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PagingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagingRecyclerView);
            if (typedArray != null) {
                setPageSize(typedArray.getInt(R.styleable.PagingRecyclerView_pageSize, 5));
                typedArray.recycle();
            }
        }
        initListener();
    }

    private void initListener() {
        addOnScrollListener(new OnScrollListener() {
            private int sumDx = 0;
            private boolean fromUser = false;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://空闲状态，没有滑动
                        if (fromUser) {
                            fromUser = false;
                            int prePage = page;
                            if (mVelocityX > MIN_VELOCITY_X_TO_CHANGE_PAGE || sumDx > (getActuallyWidth() / pageSize / 2)) {
                                page++;
                                //超过最大页码
                                if (page > pageCount - 1) {
                                    page = pageCount - 1;
                                }
                            } else if (mVelocityX < -MIN_VELOCITY_X_TO_CHANGE_PAGE || sumDx < -(getActuallyWidth() / pageSize / 2)) {
                                page--;
                                //超过最大页码
                                if (page < 0) {
                                    page = 0;
                                }
                            }
                            mVelocityX = 0;
                            if (prePage != page) {
                                dispatchPageChange();
                            }
                        }
                        fixPagePosition();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://开始滑动
                        sumDx = 0;
                        fromUser = true;
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://放开手指了，惯性滑动,重写RecyclerView fling方法屏蔽
                        sumDx = 0;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                sumDx += dx;
            }
        });
    }

    private void fixPagePosition() {
        LinearLayoutManager l = (LinearLayoutManager) getLayoutManager();
        if (l != null) {
            int findFirstVisibleItemPosition = l.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = l.findLastVisibleItemPosition();
            //即将要根据page改变的第一个和最后一个的位置
            int firstItemPos = (page) * pageSize;
            int lastItemPos = (page + 1) * pageSize - 1;
            if (lastItemPos > itemCount) {
                lastItemPos = itemCount - 1;
            }

            //对齐第page页的第一个
            if (firstItemPos <= findFirstVisibleItemPosition) {
                smoothScrollToPosition(firstItemPos);
            }

            //对齐第page页的最后一个
            if (lastItemPos >= findLastVisibleItemPosition) {
                smoothScrollToPosition(lastItemPos);
            }
        }

    }

    /**
     * 页码改变了通知
     */
    private void dispatchPageChange() {
        for (int i = mOnPageChangeListeners.size() - 1; i >= 0; i--) {
            mOnPageChangeListeners.get(i).onPageSelected(page);
        }
    }

    /**
     * 页数改变了通知
     */
    private void dispatchPageCountChange() {
        for (int i = mOnPageChangeListeners.size() - 1; i >= 0; i--) {
            mOnPageChangeListeners.get(i).onPageCount(pageCount);
        }
    }

    @Override
    public void onChildAttachedToWindow(@NonNull View child) {
        //根据页数均分
        child.getLayoutParams().width = getActuallyWidth() / pageSize;
    }

    private int getActuallyWidth() {
        return getWidth() - getPaddingStart() - getPaddingRight();
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        mVelocityX = velocityX;
        return super.fling(0, 0);
    }

    /**
     * 设置每页条目大小
     *
     * @param pageSize 每页条目大小 ，不能为0
     */
    public void setPageSize(int pageSize) {
        if (pageSize == 0) {
            throw new IllegalArgumentException("page size must be greater than zero(每页条目数必须大于0)");
        }
        this.pageSize = pageSize;
    }

    /**
     * 获取每页条目大小
     *
     * @return 获取条目大小,-1 为空
     */
    public int getPageSize() {
        return pageSize;
    }

    public void setCurrentPage(int page) {
        if (this.page != page && page < pageCount) {
            this.page = page;
            dispatchPageChange();
            fixPagePosition();
        }
    }

    public interface OnPageChangeListener {
        /**
         * 页码改变了
         *
         * @param page 当前页
         */
        void onPageSelected(int page);

        /**
         * 页数改变了
         *
         * @param pageCount 页数
         */
        void onPageCount(int pageCount);
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (!mOnPageChangeListeners.contains(listener)) {
            mOnPageChangeListeners.add(listener);
        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    if (positionStart == 0) {
                        if (page != 0) {
                            page = 0;
                            dispatchPageChange();
                        }
                    }
                    addItemCount(itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    addItemCount(-itemCount);
                    int newPage = (PagingRecyclerView.this.itemCount - 1) / pageSize;
                    if (page != newPage) {
                        page = newPage;
                        dispatchPageChange();
                    }
                }
            });
        }

    }

    private void addItemCount(int itemCount) {
        this.itemCount += itemCount;
        pageCount = (this.itemCount - 1) / pageSize + 1;
        dispatchPageCountChange();
    }

    /**
     * @return 第几页 第一页为0
     */
    public int getPage() {
        return page;
    }

    /**
     * @return 有多少个
     */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * @return 页数
     */
    public int getPageCount() {
        return pageCount;
    }
}
