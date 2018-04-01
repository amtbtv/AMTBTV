package com.sfzd5.amtbtv.model;

import com.google.gson.annotations.SerializedName;
import com.sfzd5.amtbtv.model.Card;

import java.util.List;

public class CardRow {

    @SerializedName("title") private String mTitle;
    @SerializedName("cards") private List<Card> mCards;

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Card> getCards() {
        return mCards;
    }

}