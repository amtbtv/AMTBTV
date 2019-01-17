package com.sfzd5.amtbtv.model;

import com.sfzd5.amtbtv.ServerConst;

public class Live extends Card {
    public String mediaUrl;
    public String listUrl;

    @Override
    public String getCardPic() {
        return ServerConst.getLiveCardPic(cardPic);
    }

    @Override
    public String getBgPic() {
        return ServerConst.getLiveBgPic(bgPic);
    }
}
