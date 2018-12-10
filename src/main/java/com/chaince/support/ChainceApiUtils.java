/**
 * chsi
 * Created on 2018-12-06
 */
package com.chaince.support;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhangwd <a href="mailto:mail_zwd@163">zhang Wendong</a>
 * @version $Id$
 */
public class ChainceApiUtils {
    public static boolean needSignature(String uri){
        if(StringUtils.equals(uri,"/market/pairs")){
            return false;
        }
        if(StringUtils.equals(uri,"/ticlers")){
            return false;
        }
        if(StringUtils.containsIgnoreCase(uri,"/ticlers/")){
            return false;
        }
        if(StringUtils.containsIgnoreCase(uri,"/klines/")){
            return false;
        }
       return true;
    }
}
