package io.jenkins.plugins.utils;


import com.alibaba.fastjson2.JSONObject;
import io.jenkins.plugins.JobRunListenerSysConfig;
import io.jenkins.plugins.enums.HttpMethod;
import io.jenkins.plugins.model.HttpHeader;
import jenkins.model.GlobalConfiguration;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Author: kun.tang@daocloud.io
 * Date:2024/9/18
 * Time:17:20
 */
@Slf4j
public class HttpClient {


    public static void executeRequest(Map<String, String> requestMap) throws Exception {
        var sysConfig = GlobalConfiguration.all().get(JobRunListenerSysConfig.class);
        log.info("Job Notification sysConfig : {}" , sysConfig);
        assert sysConfig != null;
        String url = sysConfig.getRequestUrl();
        var method = sysConfig.getRequestMethod();
        OkHttpClient client = getUnsafeOkHttpClient();
        RequestBody requestBody = null;
        if (method == HttpMethod.GET) {
            String params = convertMapToRequestParam(requestMap);
            url += "?" + params;
        } else {
            var requestParams = JSONObject.from(requestMap);
            requestBody = RequestBody.create(requestParams.toJSONString(), MediaType.parse("application/json"));
        }

        Request request = buildRequest(url, method, sysConfig.getHttpHeaders(), requestBody);
        assert request != null;
        // 执行请求
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                log.info("Job Notification send msg success : {}" , response.body());
            } else {
                log.error("Job Notification send msg failed: {}" , response.code());
            }
        }catch (Exception e){
            log.info("Job Notification requestMap : {} ,sysConfig : {}" , requestMap,sysConfig);
            log.error("Job Notification send msg client error", e);
        }


    }
    private static OkHttpClient getUnsafeOkHttpClient() throws Exception {
        try {
            // Optionally configure other settings (e.g., timeouts)
           var builder =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static Request buildRequest(String url, HttpMethod method, List<HttpHeader> headers, RequestBody requestBody) {
        if(null != url && !url.isEmpty()){
            Request.Builder requestBuilder = new Request.Builder().url(url);

            // 根据 HttpMethod 枚举设置请求方式
            switch (method) {
                case GET:
                    requestBuilder.get();
                    break;
                case POST:
                    requestBuilder.post(requestBody);
                    break;
                case PUT:
                    requestBuilder.put(requestBody);
                    break;
                case DELETE:
                    if (requestBody != null) {
                        requestBuilder.delete(requestBody);
                    } else {
                        requestBuilder.delete();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown HTTP method: " + method);
            }
            if (null != headers && !headers.isEmpty()) {
                for (HttpHeader header : headers) {
                    requestBuilder.addHeader(header.getHeaderKey(), header.getHeaderValue());
                }
            }
            return requestBuilder.build();
        }
        return null;
    }


    private static String convertMapToRequestParam(Map<String, String> requestMap) throws UnsupportedEncodingException {
        StringBuilder requestParams = new StringBuilder();

        Set<Map.Entry<String, String>> entrySet = requestMap.entrySet();
        boolean isFirst = true;

        for (Map.Entry<String, String> entry : entrySet) {
            if (!isFirst) {
                requestParams.append("&");
            } else {
                isFirst = false;
            }

            String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);

            requestParams.append(key).append("=").append(value);
        }

        return requestParams.toString();
    }

}
