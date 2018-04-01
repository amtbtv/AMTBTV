package com.sfzd5.amtbtv.card;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

import com.sfzd5.amtbtv.model.Card;

/*
* 在这里选择CardView，在首页统一选用IimageViewPresenter
* */
public class TextPresenterSelector extends PresenterSelector {

    private final Context mContext;
    private Presenter presenter = null;

    public TextPresenterSelector(Context context) {
        mContext = context;
    }

    @Override
    public Presenter getPresenter(Object item) {
        if (presenter == null) {
            presenter = new TextCardPresenter(mContext);
        }
        return presenter;
    }
}
