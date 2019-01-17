package com.sfzd5.amtbtv;

import android.app.Application;

import com.sfzd5.amtbtv.model.Channel;
import com.sfzd5.amtbtv.model.LiveChannelListResult;
import com.sfzd5.amtbtv.model.Program;
import com.sfzd5.amtbtv.model.ProgramListResult;
import com.sfzd5.amtbtv.util.CacheOKHttp;
import com.sfzd5.amtbtv.util.HistoryManager;
import com.sfzd5.amtbtv.util.TW2CN;
import com.tencent.bugly.Bugly;

import java.util.HashMap;
import java.util.List;

public class TVApplication extends Application {

    private static TVApplication sApp;

    public static TVApplication getInstance() {
        return sApp;
    }

    public LiveChannelListResult data = null;
    public HashMap<Integer, ProgramListResult> programListResultHashMap = new HashMap<>();
    public HashMap<String, List<String>> filesHashMap = new HashMap<>();
    public CacheOKHttp http = null;
    public HistoryManager historyManager = null;
    public TW2CN tw2cn;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        //腾讯错误收集和自动升级服务注册
        Bugly.init(getApplicationContext(), "de2442c77c", false);
        tw2cn = TW2CN.getInstance(this);
        http = new CacheOKHttp(this);
        historyManager = new HistoryManager(this);
    }

    //查找已下载节目
    public Program findProgram(int amtbId, String identifier) {
        if(programListResultHashMap.containsKey(amtbId)) {
            for(Program p : programListResultHashMap.get(amtbId).programs){
                if(p.identifier.equals(identifier))
                    return p;
            }
        }
        return null;
    }
    public Program findProgram(String channel, String identifier) {
        int amtbId = 0;
        for(Channel c : data.channels){
            if(c.name.equals(channel)){
                amtbId = c.amtbid;
                break;
            }
        }
        if(programListResultHashMap.containsKey(amtbId)) {
            for(Program p : programListResultHashMap.get(amtbId).programs){
                if(p.identifier.equals(identifier))
                    return p;
            }
        }
        return null;
    }
}
