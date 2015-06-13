package com.marshalchen.ultimaterecyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Collections;
import java.util.List;

/**
 * An abstract adapter which can be extended for Recyclerview
 */
public abstract class UltimateViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {


    protected View customLoadMoreView = null;

    /**
     * Set the header view of the adapter.
     */
    public void setCustomHeaderView(UltimateRecyclerView.CustomRelativeWrapper customHeaderView) {
        this.customHeaderView = customHeaderView;
    }

    public UltimateRecyclerView.CustomRelativeWrapper getCustomHeaderView() {
        return customHeaderView;
    }

    protected UltimateRecyclerView.CustomRelativeWrapper customHeaderView = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPES.FOOTER) {
            RecyclerView.ViewHolder viewHolder = new UltimateRecyclerviewViewHolder(customLoadMoreView);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        } else if (viewType == VIEW_TYPES.HEADER) {
            if (customHeaderView != null)
                return new UltimateRecyclerviewViewHolder(customHeaderView);
        } else if (viewType == VIEW_TYPES.CHANGED_FOOTER) {
            RecyclerView.ViewHolder viewHolder = new UltimateRecyclerviewViewHolder(customLoadMoreView);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        }
//        else if (viewType==VIEW_TYPES.STICKY_HEADER){
//            return new UltimateRecyclerviewViewHolder(LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.stick_header_item, parent, false));
//        }

        return onCreateViewHolder(parent);

    }


    public abstract UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent);

    /**
     * Using a custom LoadMoreView
     *
     * @param customview
     */
    public void setCustomLoadMoreView(View customview) {
        customLoadMoreView = customview;
    }

    /**
     * Changing the loadmore view
     *
     * @param customview
     */
    public void swipeCustomLoadMoreView(View customview) {
        customLoadMoreView = customview;
        isLoadMoreChanged = true;
    }

    public View getCustomLoadMoreView() {
        return customLoadMoreView;
    }

    public boolean isLoadMoreChanged = false;

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && customLoadMoreView != null) {
            if (isLoadMoreChanged) {
                return VIEW_TYPES.CHANGED_FOOTER;
            } else {
                return VIEW_TYPES.FOOTER;
            }


        } else if (position == 0 && customHeaderView != null) {
            return VIEW_TYPES.HEADER;
        } else
            return VIEW_TYPES.NORMAL;
    }


    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int headerOrFooter = 0;
        if (customHeaderView != null) headerOrFooter++;
        if (customLoadMoreView != null) headerOrFooter++;
        return getAdapterItemCount() + headerOrFooter;
    }

    /**
     * Returns the number of items in the adapter bound to the parent RecyclerView.
     *
     * @return The number of items in the bound adapter
     */
    public abstract int getAdapterItemCount();


    public void toggleSelection(int pos) {
        notifyItemChanged(pos);
    }


    public void clearSelection(int pos) {
        notifyItemChanged(pos);
    }

    public void setSelected(int pos) {
        notifyItemChanged(pos);
    }

    /**
     * Swap the item of list
     *
     * @param list
     * @param from
     * @param to
     */
    public void swapPositions(List<?> list, int from, int to) {
        if (customHeaderView != null) {
            from--;
            to--;
        }
        Collections.swap(list, from, to);
    }


    /**
     * Insert a item to the list of the adapter
     *
     * @param list
     * @param object
     * @param position
     * @param <T>
     */
    public <T> void insert(List<T> list, T object, int position) {
        list.add(position, object);
        if (customHeaderView != null) position++;
        notifyItemInserted(position);
       // notifyDataSetChanged();
        //  notifyItemChanged(position + 1);
    }

    /**
     * Remove a item of  the list of the adapter
     *
     * @param list
     * @param position
     */
    public void remove(List<?> list, int position) {
        list.remove(customHeaderView != null ? position - 1 : position);
        notifyItemRemoved(position);
    }

    /**
     * Clear the list of the adapter
     *
     * @param list
     */
    public void clear(List<?> list) {
        int size = list.size();
        list.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public long getHeaderId(int position) {
        if (customHeaderView != null && position == 0) return -1;
        if (customLoadMoreView != null && position >= getItemCount() - 1) return -1;
        if (getAdapterItemCount() > 0)
            return generateHeaderId(position);
        else return -1;
    }

    public abstract long generateHeaderId(int position);

    public static class UltimateRecyclerviewViewHolder extends RecyclerView.ViewHolder {
        public UltimateRecyclerviewViewHolder(View itemView) {
            super(itemView);
        }

    }

    protected class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
        public static final int CHANGED_FOOTER = 3;
    }

    protected enum AdapterAnimationType {
        AlphaIn,
        SlideInBottom,
        ScaleIn,
        SlideInLeft,
        SlideInRight,
    }

    /**
     * Animations when loading the adapter
     *
     * @param view
     * @param type
     * @return
     */
    protected Animator[] getAdapterAnimations(View view, AdapterAnimationType type) {
        if (type == AdapterAnimationType.ScaleIn) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", .5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", .5f, 1f);
            return new ObjectAnimator[]{scaleX, scaleY};
        } else if (type == AdapterAnimationType.AlphaIn) {
            return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", .5f, 1f)};
        } else if (type == AdapterAnimationType.SlideInBottom) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
            };
        } else if (type == AdapterAnimationType.SlideInLeft) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)
            };
        } else if (type == AdapterAnimationType.SlideInRight) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)
            };
        }
        return null;
    }

}
