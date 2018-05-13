package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("amtb_unicast_response")
public class MediaListResult {
    private int result;
    private String name;
    private String subname;
    private String lecturename;
    private String lectureno;

    @XStreamImplicit(itemFieldName = "vollist")
    private List<VolListItem> volList;

    @XStreamAlias("list")
    private MediaList mediaList;

    private int voltotal;
    private String thisvolno;

    public List<VolListItem> getVolList() {
        return volList;
    }

    public void setVolList(List<VolListItem> volList) {
        this.volList = volList;
    }

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

    public String getLecturename() {
        return lecturename;
    }

    public void setLecturename(String lecturename) {
        this.lecturename = lecturename;
    }

    public String getLectureno() {
        return lectureno;
    }

    public void setLectureno(String lectureno) {
        this.lectureno = lectureno;
    }

    public MediaList getMediaList() {
        return mediaList;
    }

    public void setMediaList(MediaList mediaList) {
        this.mediaList = mediaList;
    }

    public int getVoltotal() {
        return voltotal;
    }

    public void setVoltotal(int voltotal) {
        this.voltotal = voltotal;
    }

    public String getThisvolno() {
        return thisvolno;
    }

    public void setThisvolno(String thisvolno) {
        this.thisvolno = thisvolno;
    }
}
