package com.sfzd5.amtbtv.model;

public class Live extends Card {
    public String mediaUrl;
    public String listUrl;

    @Override
    public String getCardPic() {
        return "http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/" + cardPic;
    }

    @Override
    public String getBgPic() {
        return "http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/" + bgPic;
    }
}
