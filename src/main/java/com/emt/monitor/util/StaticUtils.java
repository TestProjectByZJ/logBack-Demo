package com.emt.monitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class StaticUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticUtils.class);

    private static Map<String, String> map = new ConcurrentHashMap<>();

    //一次性加载配置文件中的内容并放入map中
    static {
        Properties properties = new Properties();
        InputStream in = StaticUtils.class.getResourceAsStream("/Static.properties");
        try {
            properties.load(new InputStreamReader(in, "UTF-8"));
            Enumeration en = properties.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = properties.getProperty(key);
                map.put(key, property);
            }
        } catch (Exception e) {
            LOGGER.error(" properties file load error！");
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                LOGGER.error("properties inputStream close error！");
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String urlType) {
        if (map.containsKey(urlType)) {
            return map.get(urlType);
        }
        return "";
    }
}
