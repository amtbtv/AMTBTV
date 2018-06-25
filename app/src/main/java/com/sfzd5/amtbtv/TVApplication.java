package com.sfzd5.amtbtv;

import android.app.Application;

import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.JsonResult;
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.util.CacheOKHttp;
import com.sfzd5.amtbtv.util.HistoryManager;
import com.sfzd5.amtbtv.util.TW2CN;
import com.tencent.bugly.Bugly;

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
    public TW2CN tw2cn;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        //腾讯错误收集和自动升级服务注册
        Bugly.init(getApplicationContext(), "61728fcbac", false);
        tw2cn = TW2CN.getInstance(this);
        http = new CacheOKHttp(this);
        historyManager = new HistoryManager(this);
    }

    public Program findProgram(String channel, String identifier) {
        for(Channel c : data.channels){
            if(c.name.equals(channel)){
                for(Program p : c.programs){
                    if(p.identifier.equals(identifier)){
                        return p;
                    }
                }
            }
        }
        return null;
    }
}
