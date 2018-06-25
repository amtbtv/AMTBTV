package com.sfzd5.amtbtv.model;

import java.sql.Date;
import java.util.List;

public class Program extends Card {
    public String identifier;
    public String recDate;
    public String recAddress;
    public int picCreated;
    public List<String> files;

    @Override
    public String getCardPic() {
        return identifier + "_card.jpg";
    }

    @Override
    public String getBgPic() {
        return identifier + "_bg.jpg";
    }
}
