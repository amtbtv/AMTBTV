package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("list")
public class SubCategoryList {
    @XStreamImplicit(itemFieldName="item")
    private List<SubCategoryListItem> subCategoryListItemList;

    public List<SubCategoryListItem> getSubCategoryListItemList() {
        return subCategoryListItemList;
    }

    public void setSubCategoryListItemList(List<SubCategoryListItem> subCategoryListItemList) {
        this.subCategoryListItemList = subCategoryListItemList;
    }
}
