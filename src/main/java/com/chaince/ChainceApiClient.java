/**
 * chsi
 * Created on 2018-12-06
 */
package com.chaince;
import com.chaince.support.ChainceApiUtils;
import com.chaince.support.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/**
 * @author zhangwd <a href="mailto:mail_zwd@163">zhang Wendong</a>
 * @version $Id$
 */
public class ChainceApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ChainceApiClient.class);
    static final String API_URL = "https://api.chaince.com";

    final String apiKey;
    final String apiSecretKey;

    public ChainceApiClient(String apiKey, String apiSecretKey) {
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
    }

    /**
     * Market
     * 10 requests / 60 seconds
     * @return
     */
    public String getPairs() {
        String resp = get("/market/pairs",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }else{
            return null;
        }
    }

    /**
     * Tickers
     * all
     * 20 requests / 60 seconds
     * @return
     */
    public String getTickers() {
        String resp = get("/tickers",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }else{
            return null;
        }
    }

    /**
     * Tickers
     * by code
     * 30 requests / 60 seconds, per pair
     * @param code
     * @return
     */
    public String getTickersByCode(String code) {
        String resp = get("/tickers/"+code+"",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }else{
            return null;
        }
    }

    /**
     * Kline
     * by code
     * 30 requests / 60 seconds, per pair
     * @param code
     * @param params(选填)
     * @return
     */
    public String getKlineByCode(String code, Map<String, Object> params) {
        String resp = get("/klines/"+code+"",params);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }else{
            return null;
        }
    }

    /**
     * Order
     * submit order by code
     * 60 requests / 60 seconds, per pair
     * @param code
     * @param params
     * @return
     */
    public String submitOrderByCode(String code,Map<String, Object> params){
        String resp = post("/orders/"+code+"", params);
        return resp;
    }

    /**
     * Order
     * active order by code
     * 5 requests / 60 seconds, per pair
     * @param code
     * @param params
     * @return
     */
    public String getActiveOrderBycode(String code,Map<String, Object> params){
        String resp = get("/orders/"+code+"/active", params);
        return resp;
    }

    /**
     * Order
     * today Orders by code
     * 5 requests / 60 seconds, per pair
     * @param code
     * @param params
     * @return
     */
    public String getTodayOrdersByCode(String code,Map<String, Object> params){
        String resp = get("/orders/"+code+"/today", params);
        return resp;
    }

    /**
     * Order
     * cancel Orders by code
     * 60 requests / 60 seconds, per pair
     * @param code
     * @param params
     * @return
     */
    public String cancelOrdersByCode(String code,Map<String, Object> params){
        String resp = put("/orders/"+code+"/cancel", params);
        return resp;
    }

    /**
     * Order
     * get Orders by code And id
     * 60 requests / 60 seconds, per pair
     * @param code
     * @param params
     * @return
     */
    public String getOrdersByCodeAndId(String code,Map<String, Object> params){
        String resp = get("/orders/"+code+"", params);
        return resp;
    }

    /**
     * Account
     * accounts by pair code
     * 10 requests / 60 seconds, per pair
     * @param code
     */
    public String getAccountPositionByPairCode(String code){
        String resp = get("/accounts/pair/"+code+"",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }
        return null;
    }

    /**
     * Account
     * accounts by currency code
     * 10 requests / 60 seconds, per currency
     * @param code
     */
    public String getAccountPositionByCurrencyCode(String code){
        String resp = get("/accounts/currency/"+code+"",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }
        return null;
    }

    /**
     * pairs
     * get orderbook by pair code
     * 5 requests / 60 seconds, per pair
     * @param code
     */
    public String getOrderBookByPairCode(String code,Map<String, Object> params){
        String resp = get("/pairs/"+code+"/orderbook",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }
        return null;
    }

    /**
     * pairs
     * get trades by pair code
     * 5 requests / 60 seconds, per pair
     * @param code
     */
    public String getTradesByPairCode(String code,Map<String, Object> params){
        String resp = get("/pairs/"+code+"/trades",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }
        return null;
    }

    /**
     * pairs
     * get ticker by pair code
     * 10 requests / 60 seconds, per pair
     * @param code
     */
    public String getTickerByPairCode(String code){
        String resp = get("/pairs/"+code+"/ticker",null);
        if(StringUtils.isNotBlank(resp)){
            return resp;
        }
        return null;
    }

    String get(String uri, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        return callMethod("GET", uri, params);
    }

    String post(String uri, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return callMethod("POST", uri, params);
    }

    String put(String uri, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return callMethod("PUT", uri, params);
    }

    /**
     * 调用API接口，并将数据返回
     * @param method
     * @param uri
     * @param params
     * @return
     */
    String callMethod(String method, String uri,Map<String, Object> params) {
        Map<String,String> headerMap = new HashMap<String, String>();
        //是否需要签名
        if( ChainceApiUtils.needSignature(uri)){
            ChainceApiSignature sign = new ChainceApiSignature();
            sign.createSignature(this.apiKey, this.apiSecretKey, headerMap);
        }
        if("POST".equals(method)){
            return HttpClientUtil.sendPost(API_URL + uri,params,headerMap);
        }else if("PUT".equals(method)){
            return HttpClientUtil.sendPut(API_URL + uri,params,headerMap);
        }else{
            return HttpClientUtil.sendGet(API_URL + uri,params,headerMap);
        }
    }

    static String getHost() {
        String host = null;
        try {
            host = new URL(API_URL).getHost();
        } catch (MalformedURLException e) {
            System.err.println("parse API_URL error,system exit!,please check API_URL:" + API_URL );
            System.exit(0);
        }
        return host;
    }

}
