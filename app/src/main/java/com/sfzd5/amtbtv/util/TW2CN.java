package com.sfzd5.amtbtv.util;

import android.content.Context;
import android.content.res.Resources;

import com.sfzd5.amtbtv.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/8.
 */

public class TW2CN {
    private Map<Character, Character> ts ;
    private Map<Character, Character> st ;
    static private TW2CN instance;
    private boolean isTW;
    private TW2CN(){
        Locale locale = Locale.getDefault();
        String country = locale.getLanguage().toUpperCase();
        isTW = !country.equals("ZH");
    }

    public String toLocal(String str){
        if(isTW)
            return s2t(str);
        else
            return t2s(str);
    }

    public boolean getIsTW(){return isTW;}

    public static TW2CN getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new TW2CN();
                instance.ts = new HashMap<Character, Character>();
                instance.st = new HashMap<Character, Character>();
                Resources resources=context.getResources();
                InputStream is=resources.openRawResource(R.raw.ts);
                StringBuffer sBuffer = new StringBuffer();
                InputStreamReader inputreader = new InputStreamReader(is);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line = null;
                while((line =  buffreader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;
                    }
                    char[] chararry = line.toCharArray();
                    if (chararry.length != 2) {
                        continue;
                    }
                    if (chararry[0] != chararry[1]) {
                        instance.ts.put(chararry[0], chararry[1]);
                        instance.st.put(chararry[1], chararry[0]);
                    }
                }
                buffreader.close();
                inputreader.close();
                is.close();
            } catch (Exception e) {
                throw new IllegalStateException("Can not create new instance of JChineseConvertor : " + e.getMessage(), e);
            }
        }
        return instance;
    }

    /*
    繁体字转简体字
     */
    public String t2s(String str) {
        char[] result = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char tchar = str.charAt(i);
            Character schar = ts.get(tchar);
            if (schar != null) {
                result[i] = schar;
            } else {
                result[i] = tchar;
            }
        }
        return new String(result);
    }


    /*
    简体字转繁体字
     */
    public String s2t(String str) {
        char[] result = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char tchar = str.charAt(i);
            Character schar = st.get(tchar);
            if (schar != null) {
                result[i] = schar;
            } else {
                result[i] = tchar;
            }
        }
        return new String(result);
    }
}
