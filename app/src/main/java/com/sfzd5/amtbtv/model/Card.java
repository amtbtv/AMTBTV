package com.sfzd5.amtbtv.model;

public class Card {
    public String channel;
    public String name;
    //120*131
    protected String cardPic;
    //600*350
    protected String bgPic;

    public String getCardPic() {
        return cardPic;
    }

    public void setCardPic(String cardPic) {
        this.cardPic = cardPic;
    }

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }

}
