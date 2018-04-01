package com.sfzd5.amtbtv.card;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

import com.sfzd5.amtbtv.card.TextCardPresenter;
import com.sfzd5.amtbtv.model.Card;

/*
* 在这里选择CardView，在首页统一选用IimageViewPresenter
* */
public class CardPresenterSelector extends PresenterSelector {

    private final Context mContext;
    private Presenter presenter = null;

    public CardPresenterSelector(Context context) {
        mContext = context;
    }

    @Override
    public Presenter getPresenter(Object item) {
        if (presenter == null) {
            presenter = new ImageCardViewPresenter(mContext);
        }
        return presenter;
    }
}
