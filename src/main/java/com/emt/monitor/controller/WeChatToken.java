package com.emt.monitor.controller;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller  
@RequestMapping("/wechat/token")  
public class WeChatToken{  
      
    public static final String TOKEN = "yfkj_xfcamp_token";  
      
    /** 
     * 微信Token验证 
     * @param signature 微信加密签名 
     * @param timestamp 时间戳 
     * @param nonce     随机数 
     * @param echostr   随机字符串 
     * @return 
     * @throws NoSuchAlgorithmException  
     * @throws IOException  
     */  
    @RequestMapping("get")  
    public String getToken(String signature,String timestamp,String nonce,String echostr,HttpServletResponse response) throws NoSuchAlgorithmException, IOException{  
        // 将token、timestamp、nonce三个参数进行字典序排序   
        System.out.println("signature:"+signature);  
        System.out.println("timestamp:"+timestamp);  
        System.out.println("nonce:"+nonce);  
        System.out.println("echostr:"+echostr);  
        System.out.println("TOKEN:"+TOKEN);  
        String[] params = new String[] { TOKEN, timestamp, nonce };  
        Arrays.sort(params);  
        // 将三个参数字符串拼接成一个字符串进行sha1加密  
        String clearText = params[0] + params[1] + params[2];  
        String algorithm = "SHA-1";  
        String sign = new String(    
                org.apache.commons.codec.binary.Hex.encodeHex(MessageDigest.getInstance(algorithm).digest((clearText).getBytes()), true));    
        // 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信    
//        if (signature.equals(sign)) {    
//            response.getWriter().print(echostr);    
//        }    
		return sign;
    }  
}