package com.sfzd5.amtbtv.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.sfzd5.amtbtv.model.History;
import com.sfzd5.amtbtv.model.Program;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class HistoryManager {
    public List<History> historyList;
    SharedPreferences sp;
    public HistoryManager(Context context){
        sp = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        String json = sp.getString("historyList", "");
        if(json.isEmpty()){
            historyList = new ArrayList<>();
        } else {
            historyList = JSON.parseArray(json, History.class);
        }
    }

    public History findProgramHistory(Program program){
        for(History history : historyList){
            if(history.channel.equals(program.channel) && history.id == program.id)
                return history;
        }
        return null;
    }

    public void save(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("historyList", JSON.toJSONString(historyList));
        editor.commit();
    }
}
