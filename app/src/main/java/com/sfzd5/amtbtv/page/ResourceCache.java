package com.sfzd5.amtbtv.page;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2018/3/1.
 */

public class ResourceCache {
    private final SparseArray<View> mCachedViews = new SparseArray<View>();

    public <ViewType extends View> ViewType getViewById(View view, int resId) {
        View child = mCachedViews.get(resId, null);
        if (child == null) {
            child = view.findViewById(resId);
            mCachedViews.put(resId, child);
        }
        return (ViewType) child;
    }
}
