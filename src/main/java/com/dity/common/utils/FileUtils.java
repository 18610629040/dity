package com.dity.common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	
	public static String toUtf8String(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
	
    // 以文件流的方式复制文件
    public void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++)
                out.write(buffer[i]);
        }
        in.close();
        out.close();
    }

    // 利用PrintStream写文件
    public void PrintStream() {
        try {
            // FileOutputStream out=new FileOutputStream("c:/test.txt");
            FileOutputStream out = new FileOutputStream("/opt/socketConf.txt");
            PrintStream p = new PrintStream(out);
            for (int i = 0; i < 10; i++)
                p.println("This is " + i + " line");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 利用StringBuffer写文件
    public void StringBuffer() throws IOException {
        File file = new File("/root/sms.log");
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file, true);
        for (int i = 0; i < 10000; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("这是第" + i + "行:前面介绍的各种方法都不关用,为什么总是奇怪的问题 ");
            out.write(sb.toString().getBytes("utf-8"));
        }
        out.close();
    }

    // 文件重命名
    public void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (newfile.exists())// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
            else {
                oldfile.renameTo(newfile);
            }
        }
    }

    // 利用FileInputStream读取文件
    public String FileInputStreamDemo(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((fis.read(buf)) != -1) {
            sb.append(new String(buf));
            buf = new byte[1024];// 重新生成，避免和上次读取的数据重复
        }
        return sb.toString();
    }

    // 利用BufferedReader读取
    // 在IO操作，利用BufferedReader和BufferedWriter效率会更高一点
    public static String BufferedReaderString(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        temp = br.readLine();
        while (temp != null) {
            sb.append(temp + " ");
            temp = br.readLine();
            System.out.println(temp);
        }
        return sb.toString();
    }

    public static List<String> BufferedReaderList(String path) throws IOException {
        File file = new File(path);
        List<String> list = new ArrayList<String>();
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String temp = "";
        while (temp != null) {
            temp = br.readLine();
            if (temp != null) {
                // System.out.println(temp);
                list.add(temp);
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        BufferedReaderList("c://222");
    }
}