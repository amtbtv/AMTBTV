package com.sfzd5.amtbtv.card;


import android.content.Context;
import android.support.v17.leanback.widget.BaseCardView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.sfzd5.amtbtv.model.Card;
import com.sfzd5.amtbtv.R;

public class TextCardView extends BaseCardView {

    public TextCardView(Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_card, this);
        setFocusable(true);
    }

    public void updateUi(Card card) {
        TextView primaryText = (TextView) findViewById(R.id.primary_text);
        primaryText.setText(card.name);
    }
}