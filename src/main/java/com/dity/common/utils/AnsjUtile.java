package com.dity.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ansj.library.DicLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

public class AnsjUtile {
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
    	String now = format.format(nowDate);
    	System.out.println(now);
    	
    	format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println(format.parse(now+" 16:00:00"));
	}
	
    public static List<String> toAnalysis(String word, List<String> list, List<String> filterlist) {
        Map<String, String> cond = new LinkedHashMap<String, String>();
        Map<String, String> output = new LinkedHashMap<String, String>();
        int typeNum = Integer.valueOf(list.get(0).split("=")[0]) * 1000;
        for (String str : list) {
            String[] strs = str.split("=")[1].split(",");
            DicLibrary.insert(DicLibrary.DEFAULT, str.split("=")[1].trim());
            if (strs.length > 2) {
                List<String> tmps = new ArrayList<String>();
                tmps = Arrays.asList(strs[2].split(":")[1].split("、"));
                for (String tmp : tmps) {
                    DicLibrary.insert(DicLibrary.DEFAULT, strs[0].trim() + tmp.trim());
                    DicLibrary.insert(DicLibrary.DEFAULT, tmp.trim() + strs[0].trim());
                    if (Integer.valueOf(str.split("=")[0].trim()) < (typeNum + 300) && Integer.valueOf(str.split("=")[0].trim()) > (typeNum + 200)) {
                        cond.put(strs[0].trim() + tmp.trim(), str.split("=")[0].trim());
                        cond.put(tmp.trim() + strs[0].trim(), str.split("=")[0].trim());
                        filterlist.add(tmp.trim() + strs[0].trim()); //重复操作
                        filterlist.add(strs[0].trim() + tmp.trim());
                    } else {
                        output.put(strs[0].trim() + tmp.trim(), str.split("=")[0].trim());
                        output.put(tmp.trim() + strs[0].trim(), str.split("=")[0].trim());
                    }
                }
            }
            if (strs.length == 1) {
                if (Integer.valueOf(str.split("=")[0].trim()) < (typeNum + 300)) {
                    cond.put(str.split("=")[1].split(",")[0].trim(), str.split("=")[0].trim());
                }
                if (Integer.valueOf(str.split("=")[0].trim()) > (typeNum + 300)
                        && (Integer.valueOf(str.split("=")[0].trim()) < (typeNum + 800))) {
                    output.put(str.split("=")[1].split(",")[0].trim(), str.split("=")[0].trim());
                }
            }
        }
        for (String filter : filterlist)
            DicLibrary.insert(DicLibrary.DEFAULT, filter);
        StopRecognition fitler = new StopRecognition();
        fitler.insertStopNatures("u"); // 助词 ‘的 地’
        fitler.insertStopNatures("w"); // 标点符号
        fitler.insertStopNatures("b"); // 区别词 ‘所有’
        String[] rss = ToAnalysis.parse(word).recognition(fitler).toStringWithOutNature().split(",");

        List<String> rsList = new ArrayList<String>();
        String rs = rss[rss.length - 1];
        for (int i = 0; i < rss.length; i++) {
        	String fc = rss[i];
            if (cond.containsKey(fc) && i != rss.length - 1) {
                rsList.add(cond.get(fc));
                rsList.add(rss[i + 1]);
            }
            if (i == rss.length - 1) {
                rsList.add(output.get(fc));
            }
        }
        rsList.add(rs);
        return rsList;
    }

    public static String[] toAnalysis(String word, List<String> stopWordlist) {
        StopRecognition fitler = new StopRecognition();
        for (String s : stopWordlist) {
            fitler.insertStopWords(s.trim());
        }
        String[] rss = ToAnalysis.parse(word).recognition(fitler).toStringWithOutNature().split(",");
        System.out.println(rss.toString());
        return rss;
    }
}