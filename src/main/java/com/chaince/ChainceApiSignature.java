/**
 * chsi
 * Created on 2018-12-06
 */
package com.chaince;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
/**
 * @author zhangwd <a href="mailto:mail_zwd@163">zhang Wendong</a>
 * @version $Id$
 */
public class ChainceApiSignature {

    final Logger log = LoggerFactory.getLogger(getClass());
    static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    static final ZoneId ZONE_GMT = ZoneId.of("Z");
    static final String JWT_HEADER = "{\"alg\":\"Ed25519\",\"typ\":\"JWT\"}";

    /**
     * 创建签名并组装Bearer放到param中
     * payload:{"key": "cnc6666666666666", "iat": 1599999999}
     * 加密算法：
     * @param apiKey
     * @param apiSecretKey
     * @param headerMap
     */
    public void createSignature(String apiKey, String apiSecretKey, Map<String, String> headerMap) {
        try{
            String headerBase64 = Base64.getEncoder().encodeToString(JWT_HEADER.getBytes()).replace("=","");
            //参数格式：{"iat":1599999999,"key":"cnc6666666666666"}
            //iat精确到秒的时间戳
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"iat\":").append(epochNow());
            sb.append(",\"key\":\"").append(apiKey);
            sb.append("\"}");
            String paramJson = sb.toString();
            String paramBase64 = Base64.getEncoder().encodeToString(paramJson.getBytes()).replace("=","");
            //拼接payload
            StringBuffer payloadSb = new StringBuffer();
            payloadSb.append(headerBase64);
            payloadSb.append(".");
            payloadSb.append(paramBase64);
            String payload = payloadSb.toString();
            EdDSAPrivateKeySpec edDSAPrivateKeySpec;
            EdDSAParameterSpec edDSAParameterSpec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
            EdDSAEngine signEngine = new EdDSAEngine(MessageDigest.getInstance(EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519).getHashAlgorithm()));
            edDSAPrivateKeySpec = new EdDSAPrivateKeySpec(apiSecretKey.getBytes(),edDSAParameterSpec);
            PrivateKey privateKey = new EdDSAPrivateKey(edDSAPrivateKeySpec);
            signEngine.initSign(privateKey);
            signEngine.update(payload.getBytes());
            byte[] signature = signEngine.sign();
            String signatureStr = Base64.getEncoder().encodeToString(signature).replace("=","");
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("Bearer ");
            strBuffer.append(headerBase64);
            strBuffer.append(".");
            strBuffer.append(paramBase64);
            strBuffer.append(".");
            strBuffer.append(signatureStr);
            headerMap.put("Authorization",strBuffer.toString().replace("=",""));
        }catch (Exception e){
            e.printStackTrace();
        }

        if (log.isDebugEnabled()) {
            log.debug("请求参数:");
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                log.debug("  key: " + entry.getKey() + ", value: " + entry.getValue());
            }
        }
    }
    /**
     * Return epoch seconds
     */
    private long epochNow() {
        return Instant.now().getEpochSecond();
    }

    private String gmtNow() {
        return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
    }
}
