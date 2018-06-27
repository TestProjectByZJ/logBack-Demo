package com.emt.monitor.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


public class MonitorSignUtils {

    private static Logger logger = LoggerFactory.getLogger(MonitorSignUtils.class);
    
    public static final JSONObject reJson(boolean result,String msg) {
    	JSONObject jsonObject = new JSONObject();
    	if(result){
    		jsonObject.put("success", "success");
    	}else{
    		jsonObject.put("success", "error");
    	}
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    /**
     * 签名验证
     *
     * @param str
     * @return
     */
    public static boolean signCheck(String str) {
        try {
            String sign = str.substring(0, str.indexOf("|"));
            String encodeSign = encode(str.substring(str.indexOf("|")+1));
            if (sign.equals(encodeSign)) {
                return true;
            }
            logger.info("sign ==========" + sign + ",encodeSign ==========" + encodeSign);
            return false;
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("signcheck ===========" + str);
            return false;
        }

    }

    /**
     * 加密签名
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        return DigestUtils.sha1Hex(str + StaticUtils.getProperty("collectKey")).toUpperCase();
    }
    
    public static void main(String[] args) {
		String s = "192.168.25.1";
		System.out.println(DigestUtils.sha1Hex(s + StaticUtils.getProperty("collectKey")).toUpperCase());
	}

}
