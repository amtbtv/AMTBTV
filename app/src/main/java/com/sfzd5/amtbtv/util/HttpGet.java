package com.sfzd5.amtbtv.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Administrator on 2018/2/7.
 */

public class HttpGet {
    public static String get(String urlStr) throws IOException {
        String result = "";
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(6000);
        int statusCode = connection.getResponseCode();

		/* 200 represents HTTP OK */
        if (statusCode == 200) {
            InputStream in = connection.getInputStream();
            result = readStreamToString(in);
            in.close();
        }
        connection.disconnect();
        return result;
    }

    public static boolean down(String urlStr, File saveFile) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);
            int statusCode = connection.getResponseCode();

		/* 200 represents HTTP OK */
            if (statusCode == 200) {
                InputStream in = connection.getInputStream();
                FileOutputStream fos = new FileOutputStream(saveFile);
                byte[] buffer = new byte[1024];
                int len = 0;
                while( (len = in.read(buffer) ) != -1){
                    fos.write(buffer,0,len);
                }
                fos.close();
                in.close();
            }
            connection.disconnect();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inputStream.read(buffer) ) != -1){
            byteArrayOutputStream.write(buffer,0,len);
        }
        String result = byteArrayOutputStream.toString();
        byteArrayOutputStream.close();
        return result;
    }
}
