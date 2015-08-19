 package com.letv.cdn.manager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: lichao
 * Date: 11-8-13
 * Time: 下午3:57
 */
public class Env {
    
    private static final Logger log = LoggerFactory.getLogger(Env.class);
    
    private static Properties prop = getProperties();
    
    public static String get(String key) {
        return prop.getProperty(key);
    }

    public static Properties getProperties() {
        try {
            return (Properties) SpringUtil.getBean("env");
        } catch (Exception e) {
            Properties properties = new Properties();
            try {
                File file = new File(Env.class.getClassLoader().getResource("env.properties").getPath());
                InputStream inputStream = new FileInputStream(file);
                properties.load(inputStream);
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            log.info("加载配置文件：");
            for (Object key : properties.keySet()) {
                log.info("key:{}\tvalue:{}", key, properties.get(key));
            }
            log.info("加载配置文件完毕。");
            return properties;
        }
    }
    
    public static void reLoad() {
        log.info("重新加载配置文件");
        prop = getProperties();
    }

}
