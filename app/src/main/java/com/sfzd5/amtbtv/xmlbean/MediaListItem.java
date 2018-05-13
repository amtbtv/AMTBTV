package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class MediaListItem {
    private String fileno;
    private String filetype;
    private String fileurl;

    public String getFileno() {
        return fileno;
    }

    public void setFileno(String fileno) {
        this.fileno = fileno;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}
