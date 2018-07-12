package com.sfzd5.amtbtv.model;

import java.sql.Date;
import java.util.List;

public class Program extends Card {
    public String identifier;
    public String recDate;
    public String recAddress;
    public int picCreated;

    /*
    public String channel;
    public String name;
    //120*131
    private String cardPic;
    //600*350
    private String bgPic;*/


    //http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/02-037_bg.jpg
    //http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/02-037_card.jpg

    @Override
    public String getCardPic() {
        return "http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/" + identifier + "_card.jpg";
    }

    @Override
    public String getBgPic() {
        return "http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/" + identifier + "_bg.jpg";
    }
}
