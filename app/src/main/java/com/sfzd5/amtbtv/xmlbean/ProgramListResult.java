package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("amtb_unicast_response")
public class ProgramListResult {
    private int result;

    private String name;
    private String subname;

    private int subamtbid;

    @XStreamAlias("list")
    private ProgramList programList;

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

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public ProgramList getProgramList() {
        return programList;
    }

    public void setProgramList(ProgramList programList) {
        this.programList = programList;
    }

    public int getSubamtbid() {
        return subamtbid;
    }

    public void setSubamtbid(int subamtbid) {
        this.subamtbid = subamtbid;
    }
}
