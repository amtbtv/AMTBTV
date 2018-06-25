package com.sfzd5.amtbtv.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/1.
 */

public class CacheOKHttp {
    OkHttpClient client;
    public CacheOKHttp(Context context){
        File cacheDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cacheDir = new File(context.getExternalCacheDir(), "tvcache");
        } else {
            cacheDir = new File(context.getCacheDir(), "tvcache");
        }
        //缓存文件夹
        File cacheFile = new File(cacheDir.toString(),"tvcache");
        //缓存大小为10M
        int cacheSize = 20 * 1024 * 1024;
        //创建缓存对象
        Cache cache = new Cache(cacheFile,cacheSize);

        client = new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    public void asyncTakeFile(String filename, final CacheResult fileCacheResult, final boolean isTxt)
    {
        asyncTakeFile(filename, fileCacheResult, isTxt, "http://amtb.sfzd5.com/pic/");
    }
    /**
     * 将下载并图片存入文件缓存
     */
    public void asyncTakeFile(String filename, final CacheResult fileCacheResult, final boolean isTxt, String baseUrl)
    {
        String url = baseUrl + filename;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fileCacheResult.tackFile(null, null, isTxt);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(isTxt){
                    fileCacheResult.tackFile(response.body().string(), null , isTxt);
                } else {
                    fileCacheResult.tackFile(null, BitmapFactory.decodeStream(response.body().byteStream()), isTxt);
                }
            }
        });
    }
}
