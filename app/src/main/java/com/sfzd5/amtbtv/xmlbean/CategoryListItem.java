package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class CategoryListItem {
    private String name;
    private int amtbid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmtbid() {
        return amtbid;
    }

    public void setAmtbid(int amtbid) {
        this.amtbid = amtbid;
    }
}
