package com.sfzd5.amtbtv.xmlbean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class ProgramListItem {
    private String lectureno;
    private String lecturename;
    private String lecturedate;
    private int lecturevol;
    private String lectureaddr;
    private int lectureid;

    public String getLectureno() {
        return lectureno;
    }

    public void setLectureno(String lectureno) {
        this.lectureno = lectureno;
    }

    public String getLecturename() {
        return lecturename;
    }

    public void setLecturename(String lecturename) {
        this.lecturename = lecturename;
    }

    public String getLecturedate() {
        return lecturedate;
    }

    public void setLecturedate(String lecturedate) {
        this.lecturedate = lecturedate;
    }

    public int getLecturevol() {
        return lecturevol;
    }

    public void setLecturevol(int lecturevol) {
        this.lecturevol = lecturevol;
    }

    public String getLectureaddr() {
        return lectureaddr;
    }

    public void setLectureaddr(String lectureaddr) {
        this.lectureaddr = lectureaddr;
    }

    public int getLectureid() {
        return lectureid;
    }

    public void setLectureid(int lectureid) {
        this.lectureid = lectureid;
    }
}
