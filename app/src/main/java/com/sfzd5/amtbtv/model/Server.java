package com.sfzd5.amtbtv.model;

/**
 * Created by Administrator on 2018/2/2.
 */

import com.google.gson.annotations.SerializedName;

public class Server {

    @SerializedName("title") private String mTitle = "";
    @SerializedName("url") private String mUrl = "";

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

}
