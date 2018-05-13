package com.sfzd5.amtbtv.util;

import com.sfzd5.amtbtv.xmlbean.CategoryListResult;
import com.sfzd5.amtbtv.xmlbean.MediaListResult;
import com.sfzd5.amtbtv.xmlbean.ProgramListResult;
import com.sfzd5.amtbtv.xmlbean.SubCategoryListItem;
import com.sfzd5.amtbtv.xmlbean.SubCategoryListResult;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AmtbQuery {
    public static CategoryListResult queryCategoryListResult(){
        return query("http://www.amtb.tw/app/unicast2xml.asp?act=1", CategoryListResult.class);
    }

    public static SubCategoryListResult querySubCategoryListResult(int amtbid){
        return query("http://www.amtb.tw/app/unicast2xml.asp?act=2&amtbid="+String.valueOf(amtbid), SubCategoryListResult.class);
    }

    public static List<ProgramListResult> queryProgramListResult(int amtbid){
        List<ProgramListResult> programListResults = new ArrayList<>();
        SubCategoryListResult subCategoryListResult = querySubCategoryListResult(amtbid);
        if(subCategoryListResult!=null){
            for(SubCategoryListItem item : subCategoryListResult.getSubCategoryList().getSubCategoryListItemList()){
                ProgramListResult programListResult = queryProgramListResult(amtbid, item.getSubamtbid());
                if(programListResult!=null)
                    programListResults.add(programListResult);
            }
        }
        return programListResults;
    }

    public static ProgramListResult queryProgramListResult(int amtbid, int subamtbid){
        //
        String url = "http://www.amtb.tw/app/unicast2xml.asp?act=3&amtbid=" + String.valueOf(amtbid) + "&subamtbid=" + String.valueOf(subamtbid);
        if(amtbid==60&&subamtbid==146) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String xmlStr = new String(response.body().bytes(), "BIG5");
                xmlStr = xmlStr.replace("&nbsp", " ").replace("&", "&amp;");
                response.close();
                XStream xStream = new XStream(new DomDriver());
                xStream.processAnnotations(ProgramListResult.class);
                ProgramListResult programListResult = (ProgramListResult) xStream.fromXML(xmlStr);
                if (programListResult != null)
                    programListResult.setSubamtbid(subamtbid);
                return programListResult;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            ProgramListResult programListResult = query(url, ProgramListResult.class);
            if (programListResult != null)
                programListResult.setSubamtbid(subamtbid);
            return programListResult;
        }
    }

    public static MediaListResult queryMediaListResult(int amtbid, int subamtbid, int lectureid, int volid){
        return query("http://www.amtb.tw/app/unicast2xml.asp?act=4&amtbid="+String.valueOf(amtbid)+"&subamtbid="+String.valueOf(subamtbid)+"&lectureid="+String.valueOf(lectureid)+"&volid="+String.valueOf(volid), MediaListResult.class);
    }
    public static MediaListResult queryMediaListResult(int amtbid, int subamtbid, int lectureid){
        return query("http://www.amtb.tw/app/unicast2xml.asp?act=4&amtbid="+String.valueOf(amtbid)+"&subamtbid="+String.valueOf(subamtbid)+"&lectureid="+String.valueOf(lectureid), MediaListResult.class);
    }

    public static <T> T query(String urlStr, Class<T> clazz) {
        try {
            URL url = new URL(urlStr);
            XStream xStream = new XStream(new DomDriver());
            xStream.processAnnotations(clazz);
            return (T) xStream.fromXML(url);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (StreamException se){
            //se.printStackTrace();
            String msg2 = se.getCause().getMessage();
            System.out.println(msg2);
        }
        return null;
    }
}
