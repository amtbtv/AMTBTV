package com.sfzd5.amtbtv.util;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.model.Result;

public class AmtbApi<T extends Result> extends AsyncTask<Class<T>, Integer, T> {

    //{"channels":[{"name":"\u963f\u5f4c\u9640\u7d93","amtbid":"1"},{"name":"\u7121\u91cf\u58fd\u7d93","amtbid":"2"}]}
    private static final String liveChannelsUrl  = "http://amtbapi.hwadzan.com/amtbtv/channels/live,mp4";

    //{"files":["02-012-0001.mp4","02-012-0002.mp4"]}
    private static final String filesUrl  = "http://amtbapi.hwadzan.com/amtbtv/%s/mp4";

    //{"programs":[{"name":"\u7121\u91cf\u58fd\u7d93\u5927\u610f","identifier":"02-002","recDate":"1992.12","recAddress":"\u7f8e\u570b","picCreated":"1","mp4":"1","mp3":"1"}]}
    private static final String programsUrl  = "http://amtbapi.hwadzan.com/amtbtv/%d/mp4";

    //最近视频，暂时先不使用此功能
    private static final String newMediasUrl = "http://amtbapi.hwadzan.com/amtbtv/newmedias/mp4?limit=20";

    public static String takeLiveChannelsUrl(){
        return liveChannelsUrl;
    }

    public static String takeFilesUrl(String identifier){
        return String.format(filesUrl, identifier);
    }

    public static String takeProgramsUrl(int amtbid){
        return String.format(programsUrl, amtbid);
    }



    //图片地址
    //http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/02-037_bg.jpg
    //http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/02-037_card.jpg

    AmtbApiCallBack<T> amtbApiCallBack;
    String url;

    public AmtbApi(String url, AmtbApiCallBack<T> amtbApiCallBack){
        this.url = url;
        this.amtbApiCallBack = amtbApiCallBack;
    }

    @Override
    protected void onPostExecute(T obj) {
        amtbApiCallBack.callBack(obj);
        super.onPostExecute(obj);
    }

    @Override
    protected T doInBackground(Class<T>... parms) {
        String json = TVApplication.getInstance().http.take(url);
        if (!json.isEmpty()) {
            T val = new Gson().fromJson(json, parms[0]);
            return val;
        }
        return null;
    }

}
