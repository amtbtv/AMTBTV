package com.sfzd5.amtbtv;

import android.app.Application;

import com.sfzd5.amtbtv.model.JsonResult;
import com.sfzd5.amtbtv.util.CacheOKHttp;
import com.sfzd5.amtbtv.util.HistoryManager;
import com.sfzd5.amtbtv.util.TW2CN;

public class TVApplication extends Application {

    private static TVApplication sApp;

    // This class will be instantiated once when the application is started, no need to design it
    // as a singleton on purpose.
    public static TVApplication getInstance() {
        return sApp;
    }

    public JsonResult data = null;
    public CacheOKHttp http = null;
    public HistoryManager historyManager = null;
    public String curChannel = "";
    public int curCardId = 0;
    public int curFileIdx;
    public TW2CN tw2cn;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        tw2cn = TW2CN.getInstance(this);
        http = new CacheOKHttp(this);
        historyManager = new HistoryManager(this);
    }
}
