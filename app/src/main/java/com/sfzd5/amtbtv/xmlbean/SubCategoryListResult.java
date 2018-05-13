package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("amtb_unicast_response")
public class SubCategoryListResult {
    private int result;

    private String name;

    @XStreamAlias("list")
    private SubCategoryList subCategoryList;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubCategoryList getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(SubCategoryList subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
