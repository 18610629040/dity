package com.dity.common.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
    public static Properties pro = new Properties();

    static {
        try {
            pro.load(new InputStreamReader(Object.class.getResourceAsStream("/socket.properties"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // /dmsSocket/src/main/resources/eventPath.properties
    public static void main(String[] args) throws Exception {

        Iterator it = pro.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + ":" + value);
        }

//		System.out.println();
    }

    public static Properties getProperties() {
        Properties pro = new Properties();
        try {
            pro.load(new InputStreamReader(Object.class.getResourceAsStream("/socket.properties"), "UTF-8"));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pro;
    }

    public static Map<String, String> getPropertyMaps() {

        Map<String, String> map = new HashMap<String, String>();

        Iterator it = pro.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            map.put(key.toString(), value.toString());
        }
        return map;
    }
}