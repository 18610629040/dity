package com.dity.common.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FileModify {
    private static String fileName = "";

    public String read(String filePath) {
        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();
        try {
            // 根据文件路径创建缓冲输入流
            br = new BufferedReader(new FileReader(filePath + fileName));
            // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓
            // 冲对象中
            while ((line = br.readLine()) != null) {
                if (line != "" && !"".equals(line)) {
                    buf.append(line);
                    buf.append(System.getProperty("line.separator"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        return buf.toString();
    }

    public void write(String filePath, String content) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuffer sb = new StringBuffer(4096);
        String temp = null;
        while ((temp = br.readLine()) != null) {
            sb.append(temp).append("\r\n ");
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter("c:/a.txt "));
        bw.write(sb.toString());
        bw.close();
    }

    public void fileAppender(String filePath, String content) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filePath + fileName));
        String line = null;
        // 一行一行的读
        StringBuilder sb = new StringBuilder();
        sb.append(content);
        while ((line = reader.readLine()) != null) {
            String loc = line.split(",")[7];
            sb.append(gdMethod(loc, line));
            sb.append("\r\n");
        }
        reader.close();
        createFile(filePath + fileName + "XY");
        FileOutputStream fos = new FileOutputStream(filePath + fileName + "XY");
        PrintWriter pw = new PrintWriter(fos);
        pw.write(sb.toString());
        pw.flush();
        pw.close();
        //写回去
//		       RandomAccessFile mm = new RandomAccessFile(fileName, "rw");
//		       mm.writeBytes(sb.toString());
//		       mm.close();
    }

    public static int postBody(String urlPath, String data) throws Exception {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //  设置doOutput属性为true表示将使用此urlConnection写入数据
            //  urlConnection.setDoOutput(true);
            //  定义待写入数据的内容类型，我们设置为application/x-www-form-urlencoded		//  类型
            //  urlConnection.setRequestProperty("content-type", 					       //  "application/x-www-form-urlencoded");
            //  得到请求的输出流对象
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            //  把数据写入请求的Body
            out.write(data);
            out.flush();
            out.close();
            // 从服务器读取响应
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String body = IOUtils.toString(inputStream, encoding);
            if (urlConnection.getResponseCode() == 200) {
                return 200;
            } else {
                throw new Exception(body);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public static JSONObject doGet(String url, JSONObject json) {
        HttpClient client = new DefaultHttpClient();
        //	HttpPost post = new HttpPost(url);
        HttpGet get = new HttpGet(url);
        JSONObject response = null;
        String result = "";
        try {
            //		get.setEntity(s);
            HttpResponse res = client.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static String gdMethod(String args, String ct) throws Exception {
        //	String	url= //"http://restapi.amap.com/v3/geocode/geo?key=8b44a4c46af15305e8458c3c58cb30f4&address="+ar//gs+"&city="+fileName;
        String url = "http://restapi.amap.com/v3/geocode/geo?key=b83cdcbc4a85f1a947bc5eab69d04b07&address=" + args + "&city=" + fileName;
        //		  			 	System.out.println("-------");
        //		  			 	System.out.println("-------");
        //		  			 	System.out.println(url);
        //		  			 	System.out.println("-------");
        //		  			 	System.out.println("-------");
        JSONObject params = new JSONObject();
        JSONObject ret = doGet(url, params);
        JSONArray ret2 = (JSONArray) ret.get("geocodes");
        String loc;
        if (ret2 != null) {
            loc = ((JSONObject) ret2.get(0)).get("location").toString();
        } else {
            loc = ",";
        }
        return ct + "," + loc;
    }

    public static List<String> getFileName(String path) {
        List<String> list = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return list;
        }
        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");
            } else {
                list.add(fs.getName());
                System.out.println(fs.getName());
            }
        }
        return list;
    }

    public static boolean createFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {// 判断文件是否存在
            System.out.println("目标文件已存在" + filePath);
            return false;
        }
        if (filePath.endsWith(File.separator)) {// 判断文件是否为目录
            System.out.println("目标文件不能为目录！");
            return false;
        }
        if (!file.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
            // 如果目标文件所在的文件夹不存在，则创建父文件夹
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {// 判断创建目录是否成功
                System.out.println("创建目标文件所在的目录失败！");
                return false;
            }
        }
        try {
            if (file.createNewFile()) {// 创建目标文件
                System.out.println("创建文件成功:" + filePath);
                return true;
            } else {
                System.out.println("创建文件失败！");
                return false;
            }
        } catch (IOException e) {// 捕获异常
            e.printStackTrace();
            System.out.println("创建文件失败！" + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\git\\Desktop\\城市购物店铺数据\\txt\\"; // 文件路径
//		    	           String filePath = "C:\\"; // 文件路径
        FileModify obj = new FileModify();
//		    	           obj.write(filePath, obj.read(filePath)); // 读取修改文件
        List<String> list = obj.getFileName(filePath);
        for (String Str : list) {
            fileName = Str;
            obj.fileAppender(filePath, obj.read(filePath));
        }
        try {
//		    	               obj.fileAppender(filePath, "set a=b \n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}