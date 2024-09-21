package com.zheng.travel.client.controller.pay.relation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtils {
    private static final ObjectMapper JSON = new ObjectMapper();

    /**
     * get方法
     *
     * @param url
     * @return
     */
    public static JsonNode doGet(String url) {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpget.addHeader("Accept", "application/json");
        try {
            String token = WechatPayUtils.getToken("GET", new URL(url), "");
            httpget.addHeader("Authorization", token);
            CloseableHttpResponse httpResponse = httpClient.execute(httpget);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {

                String jsonResult = EntityUtils.toString(httpResponse.getEntity());
                return JSON.readTree(jsonResult);
            } else {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 封装post
     *
     * @return
     */
    public static Map<String, Object> doPost(String url, String body) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;chartset=utf-8");
        httpPost.addHeader("Accept", "application/json");
        try {
            String token = WechatPayUtils.getToken("POST", new URL(url), body);
            httpPost.addHeader("Authorization", token);

            if (body == null) {
                throw new IllegalArgumentException("data参数不能为空");
            }
            StringEntity stringEntity = new StringEntity(body, "utf-8");
            httpPost.setEntity(stringEntity);

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResult = EntityUtils.toString(httpEntity);
                return JSON.readValue(jsonResult, HashMap.class);
            } else {
                System.err.println("微信支付错误信息" + EntityUtils.toString(httpEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    /**
     * 封装post
     *
     * @return
     */
    public static HashMap<String, Object> doPostWexin(String url, String body) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;chartset=utf-8");
        httpPost.addHeader("Accept", "application/json");
        try {
            String token = WechatPayUtils.getToken("POST", new URL(url), body);
            httpPost.addHeader("Authorization", token);

            if (body == null) {
                throw new IllegalArgumentException("data参数不能为空");
            }
            StringEntity stringEntity = new StringEntity(body, "utf-8");
            httpPost.setEntity(stringEntity);
            log.info("url：{}", url);
            log.info("参数：{}", stringEntity);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResult = EntityUtils.toString(httpEntity);
                return JSON.readValue(jsonResult, new TypeReference<HashMap<String, Object>>() {
                });
            } else {
                System.err.println("微信支付错误信息" + EntityUtils.toString(httpEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }
}
