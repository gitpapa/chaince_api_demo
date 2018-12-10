/**
 * chsi
 * Created on 2018-12-06
 */
package com.chaince;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangwd <a href="mailto:mail_zwd@163">zhang Wendong</a>
 * @version $Id$
 */
public class ChainceApiTest {
    static final String API_KEY = "YOUR_API_KEY";
    static final String API_SECRET_KEY = "YOUR_API_SECRET_KEY";
    private ChainceApiClient client;

    @Before
    public void init(){
        client = new ChainceApiClient(API_KEY, API_SECRET_KEY);
    }

    /**
     * Market
     */
    @Test
    public void getPairsTest(){
        String resultJson = client.getPairs();
        System.out.println(resultJson);
    }

    /**
     * Tickers
     * all
     */
    @Test
    public void getTickers(){
        String resultJson = client.getTickers();
        System.out.println(resultJson);
    }

    /**
     * Tickers
     * by code
     */
    @Test
    public void getTickersByCode(){
        String code = "ceteos";
        String resultJson = client.getTickersByCode(code);
        System.out.println(resultJson);
    }

    /**
     * Kline
     * by code
     */
    @Test
    public void getKlineByCode(){
        String code = "ceteos";
        Map<String ,Object> params = new HashMap<String, Object>();
        //one of "m1", "m5", "m30", "h1", "h4", "d", "w",default: "d"
        //String level = "m1";(选填，默认"d")
        //long first = 1533715680;(选填)
        //long last = 1533716680;(选填)
        //int limit = 10;(选填，默认2)
        params.put("level","h4");
        params.put("first",1533715680);
        params.put("limit",10);
        String resultJson = client.getKlineByCode(code,params);
        System.out.println(resultJson);
    }

    /**
     * Order
     * submit order by code
     */
    @Test
    public void submitOrderBycode(){
        String code = "ceteos";
        //参数
        //"direction": "bid"（必填，"ask" 或"bid"）
        //"type": "limit"（必填，暂时只支持limit）
        //"price": 0.1（必填，(0, 100000)）
        //"quantity": 1（必填，(0, 100000)）
        //"client_order_id": "my-order-1"（选填，最长32，不能有 , ; ' "）
        Map<String ,Object> params = new HashMap<String, Object>();
        params.put("direction","bid");
        params.put("type","limit");
        params.put("price",0.0003);
        params.put("quantity",0.1);
        //params.put("client_order_id","test_01");
        String resultJson = client.submitOrderByCode(code,params);
        System.out.println(resultJson);
    }

    /**
     * Order
     * active by code
     */
    @Test
    public void getActiveOrderByCode(){
        String code = "ceteos";
        Map<String ,Object> params = new HashMap<String, Object>();
        //参数
        //"direction": "bid"（选填，"ask" 或"bid"）
        //"limit": "limit"（选填，默认100）
        //params.put("limit",100);
        //params.put("direction","bid");
        String resultJson = client.getActiveOrderBycode(code,params);
        System.out.println(resultJson);
    }

    /**
     * Order
     * today Orders by code
     */
    @Test
    public void getTodayOrdersByCode(){
        String code = "ceteos";
        Map<String ,Object> params = new HashMap<String, Object>();
        //参数
        //"direction": "bid"（选填，"ask" 或"bid"）
        //"limit": "limit"（选填，默认100）
        //params.put("limit",100);
        //params.put("direction","bid");
        String resultJson = client.getTodayOrdersByCode(code,params);
        System.out.println(resultJson);
    }

    /**
     * Order
     * cancel Orders by code
     */
    @Test
    public void cancelOrdersByCode(){
        String code = "ceteos";
        Map<String ,Object> params = new HashMap<String, Object>();
        //参数
        //"order_id":16（选填，五选一）
        //"order_ids":[12, 13]（选填，五选一）
        //"client_order_id":"my-order-1"（选填，五选一）
        //"client_order_ids":["my-order-1", "my-order-2"]（选填，五选一）
        //"direction":"bid" or "ask"（选填，五选一）
        params.put("order_id",228725);
        String resultJson = client.cancelOrdersByCode(code,params);
        System.out.println(resultJson);
    }

    /**
     * Order
     * get Orders by code and id
     */
    @Test
    public void getOrdersByCodeAndId(){
        String code = "ceteos";
        Map<String ,Object> params = new HashMap<String, Object>();
        //参数
        //"order_ids":16（选填，二选一）
        //"client_order_ids":[12, 13]（选填，二选一）
        params.put("order_ids","228724");
        String resultJson = client.getOrdersByCodeAndId(code,params);
        System.out.println(resultJson);
    }

    /**
     * account
     * accounts by pair code
     */
    @Test
    public void getAccountPositionByPairCode() {
        String code = "ceteos";
        String resultJson = client.getAccountPositionByPairCode(code);
        System.out.println(resultJson);
    }

    /**
     * account
     * accounts by currency code
     */
    @Test
    public void getAccountPositionByCurrencyCode() {
        String code = "cet";
        String resultJson = client.getAccountPositionByCurrencyCode(code);
        System.out.println(resultJson);
    }

    /**
     * pairs
     * get orderbook by pair code
     */
    @Test
    public void getOrderBookByPairCode() {
        String code = "ceteos";
        Map<String ,Object> params = new HashMap<String, Object>();
        //"levels":10(1-100，默认10)
        params.put("levels",10);
        String resultJson = client.getOrderBookByPairCode(code,params);
        System.out.println(resultJson);
    }

    /**
     * pairs
     * get trades by pair code
     */
    @Test
    public void getTradesByPairCode() {
        String code = "ceteos";
        Map<String ,Object> params = new HashMap<String, Object>();
        //"limit":10(1-500，默认100)
        params.put("limit",100);
        String resultJson = client.getTradesByPairCode(code,params);
        System.out.println(resultJson);
    }

    /**
     * pairs
     * get ticker by pair code
     */
    @Test
    public void getTickerByPairCode() {
        String code = "ceteos";
        String resultJson = client.getTickerByPairCode(code);
        System.out.println(resultJson);
    }

}
