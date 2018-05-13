package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class SubCategoryListItem {
    private String subname;
    private int subamtbid;

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }


    public int getSubamtbid() {
        return subamtbid;
    }

    public void setSubamtbid(int subamtbid) {
        this.subamtbid = subamtbid;
    }
}
