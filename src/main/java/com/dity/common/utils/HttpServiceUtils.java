package com.dity.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpServiceUtils {
    public static Map<String, String> getAttrCol(String str) {
        Map<String, String> rs = new HashMap<String, String>();
        rs.put(str.split(",")[0], str.split(",")[1]);
        return rs;
    }

    public static LinkedHashMap<String, String> getResultToMap(String result) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String[] strs = result.trim().replace("'", "").replace("[", "").replace("]", "").split(",");
        if (strs.length > 0) {
            for (int i = 0; i <= (strs.length / 2 - 1); i++) {
                map.put(strs[i], strs[i + strs.length / 2]);
                System.out.println("map.put(" + strs[i] + "," + strs[i + strs.length / 2] + ")");
            }
        }
        return map;
    }

    public static String wordsFilter(String word, String regs) {
        String[] strs = regs.split(";");
        if (strs.length == 2) {
            return replaceFilter(word, regs);
        }
        if (strs.length == 1) {
            return nullFilter(word, regs);
        } else {
            return regFilter(word, regs);
        }
    }

    public static String replaceFilter(String word, String regs) {
        String[] strs = regs.split(";");
        String str0 = strs[0];
        String str1 = strs[1];
        if (word.contains(str1)) {
            return word;
        } else {
            str1 = str1 == null ? "" : str1;
            return word.replace(str0, str1);
        }
    }
      
	public static String nullFilter(String word, String regs) {
		String[] strs = regs.split(",");
		return word.replace(strs[0], strs[1]);
//        String[] strs = regs.split(";");
//        String str0 = strs[0];
//        return word.replace(str0, "");
	}

    public static String regFilter(String word, String regs) {
        String[] strs = regs.split(";");
        String[] rslist = word.split(strs[0]);
        String tmp = word;
        String rs = "";
        String replace = strs[2];
        if (rslist.length == 1) {
            rs = rslist[0];
            tmp = tmp.replace(rslist[0], "");
        } else {
            rs = rslist[0] + "replace" + rslist[1];
            tmp = tmp.replace(rslist[0], "");
            tmp = tmp.replace(rslist[1], "");
        }
        Pattern p = Pattern.compile(strs[1]);
        Matcher m = p.matcher(tmp);
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                replace = replace.replaceFirst("str", m.group(i));
            }
        }
        rs = rs.replace("replace", replace);
        System.out.println(rs);
        return rs;
    }

    public static void main(String[] args) {
        String word = "签定时间是2017至2018年文档";
        String regs = "签定时间是[0-9]{4}[至][0-9]{4}[年];签定时间是(.*?)至(.*?)年;签定时间大于str,签定时间小于str";
        regFilter(word, regs);
    }
}