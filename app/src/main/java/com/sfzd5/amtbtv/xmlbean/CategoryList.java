package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("list")
public class CategoryList {
    @XStreamImplicit(itemFieldName="item")
    private List<CategoryListItem> categoryListItemList;

    public List<CategoryListItem> getCategoryListItemList() {
        return categoryListItemList;
    }

    public void setCategoryListItemList(List<CategoryListItem> categoryListItemList) {
        this.categoryListItemList = categoryListItemList;
    }
}