package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("list")
public class ProgramList {
    @XStreamImplicit(itemFieldName="item")
    private List<ProgramListItem> programListItemList;

    public List<ProgramListItem> getProgramListItemList() {
        return programListItemList;
    }

    public void setProgramListItemList(List<ProgramListItem> programListItemList) {
        this.programListItemList = programListItemList;
    }
}
