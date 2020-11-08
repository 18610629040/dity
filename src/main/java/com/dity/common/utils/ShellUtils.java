package com.dity.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {
    public static String executeShell(String shellCommand) throws IOException {
        String result = "";
        try {

//            String shellCommand="/home/felven/word2vec/demo-classes.sh";  
            Process ps = Runtime.getRuntime().exec(shellCommand);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
//	                sb.append(line).append("\n");  
                sb.append(line);
            }
            result = sb.toString();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}