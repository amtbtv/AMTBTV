package com.sfzd5.amtbtv.util;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.sfzd5.amtbtv.ServerConst;
import com.sfzd5.amtbtv.TVApplication;
import com.sfzd5.amtbtv.model.Result;

public class AmtbApi<T extends Result> extends AsyncTask<Class<T>, Integer, T> {

    public static String takeLiveChannelsUrl(){
        return ServerConst.takeLiveChannelsUrl();
    }

    public static String takeFilesUrl(String identifier){
        return ServerConst.takeFilesUrl(identifier);
    }

    public static String takeProgramsUrl(int amtbid){
        return ServerConst.takeProgramsUrl(amtbid);
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
