/**
 * chsi
 * Created on 2018-12-06
 */
package com.chaince.support;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.chaince.ChainceApiClient;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClientUtil
 * @author zhangwd <a href="mailto:mail_zwd@163">zhang Wendong</a>
 * @version $Id$
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(ChainceApiClient.class);
    private static final String ENCODING = "UTF-8";
    /**
     * sendPost
     * @param url
     * @param data
     * @param headerMap
     * @param encoding
     * @return
     */
    public static String sendPost(String url,JSONObject data,Map<String, String> headerMap,String encoding) {
        String resultJson = null;
        // 创建Client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost();
        try {
            // 设置请求地址
            httpPost.setURI(new URI(url));
            // 设置请求头
            if (headerMap != null) {
                Header[] allHeader = new BasicHeader[headerMap.size()];
                int i = 0;
                for (Map.Entry<String, String> entry: headerMap.entrySet()){
                    allHeader[i] = new BasicHeader(entry.getKey(), entry.getValue());
                    i++;
                }
                httpPost.setHeaders(allHeader);
            }
            // 设置实体
            String entityStr = JSON.toJSONString(data);
            StringEntity StringEntity = new StringEntity(entityStr);
            httpPost.setEntity(StringEntity);
            StringEntity.setContentType("application/json");

            // 发送请求,返回响应对象
            CloseableHttpResponse response = client.execute(httpPost);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // 获取响应结果
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                log.error("响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送post请求失败", e);
        } finally {
            httpPost.releaseConnection();
        }
        return resultJson;
    }

    /**
     * sendPost
     * @param url
     * @param data
     * @return
     */
    public static String sendPost(String url, JSONObject data) {
        Map<String, String> headerMap = new HashMap<>();
        return sendPost(url,data, headerMap,  ENCODING);
    }

    /**
     * sendPost
     * @param url
     * @param params
     * @return
     */
    public static String sendPost(String url,Map<String,Object> params){
        Map<String, String> headerMap = new HashMap<>();
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
        return sendPost(url,data,headerMap,ENCODING);
    }

    /**
     * sendPost
     * @param url
     * @param headerMap
     * @param data
     * @return
     */
    public static String sendPost(String url, Map<String, String> headerMap, JSONObject data) {
        return sendPost(url, data, headerMap, ENCODING);
    }

    /**
     * sendPost
     * @param url
     * @param params
     * @param headerMap
     * @return
     */
    public static String sendPost(String url,Map<String,Object> params,Map<String,String> headerMap){
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
        return sendPost(url,data,headerMap,ENCODING);
    }

    /**
     * sendGet
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static String sendGet(String url,Map<String,Object> params,Map<String,String> headerMap,String encoding){
        String resultJson = null;
        // 创建client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpGet
        HttpGet httpGet = new HttpGet();
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            //设置Header
            if(headerMap != null){
                for (String key : headerMap.keySet()) {
                   httpGet.addHeader(key,headerMap.get(key).toString());
                }
            }
            // 封装参数
            if(params != null){
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key).toString());
                }
            }
            URI uri = builder.build();
            log.info("请求地址："+ uri);
            // 设置请求地址
            httpGet.setURI(uri);
            // 发送请求，返回响应对象
            CloseableHttpResponse response = client.execute(httpGet);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK){
                // 获取响应数据
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
            }else{
                log.error("响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送get请求失败",e);
        } finally {
            httpGet.releaseConnection();
        }
        return resultJson;
    }

    /**
     * sendGet
     * @param url
     * @param params
     * @param headerMap
     * @return
     */
    public static String sendGet(String url,Map<String,Object> params,Map<String,String> headerMap){
        return sendGet(url,params,headerMap,ENCODING);
    }
    /**
     * sendGet
     * @param url
     * @param params
     * @return
     */
    public static String sendGet(String url,Map<String,Object> params){
        return sendGet(url,params,null,ENCODING);
    }

    /**
     * sendGet
     * @param url
     * @return
     */
    public static String sendGet(String url){
        return sendGet(url,null,null,ENCODING);
    }

    /**
     * sendPut
     * @param url
     * @param params
     * @param headerMap
     * @return
     */
    public static String sendPut(String url,Map<String,Object> params,Map<String,String> headerMap){
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
        return sendPut(url,data,headerMap,ENCODING);
    }

    /**
     * sendPut
     * @param url
     * @param data
     * @param headerMap
     * @param encoding
     * @return
     */
    public static String sendPut(String url,JSONObject data,Map<String, String> headerMap,String encoding) {
        String resultJson = null;
        //HttpClients.createDefault()等价于 HttpClientBuilder.create().build();
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPut httpput = new HttpPut();
        try {
            // 设置请求地址
            httpput.setURI(new URI(url));
            // 设置请求头
            if (headerMap != null) {
                Header[] allHeader = new BasicHeader[headerMap.size()];
                int i = 0;
                for (Map.Entry<String, String> entry: headerMap.entrySet()){
                    allHeader[i] = new BasicHeader(entry.getKey(), entry.getValue());
                    i++;
                }
                httpput.setHeaders(allHeader);
            }
            // 设置实体
            String entityStr = JSON.toJSONString(data);
            StringEntity StringEntity = new StringEntity(entityStr);
            httpput.setEntity(StringEntity);
            StringEntity.setContentType("application/json");

            // 发送请求,返回响应对象
            CloseableHttpResponse response = closeableHttpClient.execute(httpput);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // 获取响应结果
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                log.error("响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送post请求失败", e);
        } finally {
            httpput.releaseConnection();
        }
        return resultJson;
    }
}