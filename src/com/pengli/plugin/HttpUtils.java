package com.pengli.plugin;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Created by pengli on 16/6/2.
 *
 */
public class HttpUtils {

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String text = URLEncoder.encode("解释","UTF-8");
//        String url = "http://fanyi.youdao.com/openapi.do?keyfrom=pengli&key=991847317&type=data&doctype=json&version=1.1&q="+text;
//        System.out.println("newUrl = [" + url + "]");
//        String result = HttpUtils.sendGet(url);
//        System.out.println("args = [" + result + "]");
//    }

    public static String sendGet(String url){
        String result = "";
        BufferedReader in = null;
        try {
            URL real_url = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) real_url.openConnection();
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


}
