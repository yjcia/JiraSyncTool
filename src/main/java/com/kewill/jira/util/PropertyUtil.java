package com.kewill.jira.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by YanJun on 2016/7/1.
 */
public class PropertyUtil {

    private static Properties prop = null;
    static{
        prop = new Properties();
        try {
            prop.load(PropertyUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getPropValue(String key){
        return String.valueOf(prop.get(key));
    }
}
