package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class VolListItem {

    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public class Item{

        private int volid;
        private String volno;

        public int getVolid() {
            return volid;
        }

        public void setVolid(int volid) {
            this.volid = volid;
        }

        public String getVolno() {
            return volno;
        }

        public void setVolno(String volno) {
            this.volno = volno;
        }
    }
}
