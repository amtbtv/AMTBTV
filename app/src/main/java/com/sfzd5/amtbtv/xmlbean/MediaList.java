package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("list")
public class MediaList {
    @XStreamImplicit(itemFieldName="item")
    private List<MediaListItem> mediaListItemList;

    public List<MediaListItem> getMediaListItemList() {
        return mediaListItemList;
    }

    public void setMediaListItemList(List<MediaListItem> mediaListItemList) {
        this.mediaListItemList = mediaListItemList;
    }
}
