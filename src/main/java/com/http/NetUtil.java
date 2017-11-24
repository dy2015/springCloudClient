package com.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class NetUtil {
    private static final int DEFAULT_TIMEOUT = 100000;
    
    public static String requestGet(String url) {
        try {
            return requestGet(url, DEFAULT_TIMEOUT);
        } catch (IOException e) {
        	System.out.println("发送GET请求出现异常: " + url);
        }
        return "";
    }
    
    // timeout in milliseconds
    public static String requestGet(String url, int timeout) throws IOException {
        InputStream in = null;
        final int MAXRETRY = 3;
        for (int i = 0; i < MAXRETRY; ++i) {
            try {
                // 打开和URL之间的连接
                URLConnection connection = new URL(url).openConnection();
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                if (timeout > 0) {
                    connection.setConnectTimeout(timeout);
                    connection.setReadTimeout(timeout);
                } else {
                    connection.setConnectTimeout(DEFAULT_TIMEOUT);
                    connection.setReadTimeout(DEFAULT_TIMEOUT);
                }
                // 建立实际的连接
                connection.connect();
                
                // 定义 BufferedReader输入流来读取URL的响应
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                in = connection.getInputStream();
                byte[] buf = new byte[1024];
                int nn = 0;
                while ((nn = in.read(buf)) > 0) {
                    bos.write(buf, 0, nn);
                }
                return new String(bos.toByteArray(),"utf-8");
            } catch (IOException e) {
                if (i != MAXRETRY - 1) {
                    continue;
                }
                // 到达最大retry次数
                throw e;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e2) {
                	System.out.println("发送GET请求出现异常！" + url);
                }
            }
        }
        return "";
    }
    
    
    /** 判断当前URL，是否可能为优酷转义后的URL。false为不可能。 */
    public static boolean isCovertURL(String url) {
        boolean result = false;
        if (url.indexOf("_esc") > -1) {
            result = true;
        }
        return result;
    }
    
    /**
     * 转换URL中的 & 为 $$
     * 
     * @param url
     * @return
     */
    public static String covertUrl(String url) {
        try {
            url = url.replaceAll("%", "percent_esc");
            url = url.replaceAll("&", "and_esc");
            url = url.replaceAll("#", "sharp_esc");
            url = url.replaceAll("=", "equal_esc");
            url = url.replaceAll("\\?", "interrogation_esc");
            url = url.replaceAll("\\+", "add_esc");
            url = url.replaceAll("\\$", "dollar_esc");
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
    }
    
    /**
     * 转换URL中的 $$ 为 & ,covertUrl的反方法 使用前建议调用isCovertURL方法，为true再执行。
     * 
     * @param url
     * @return
     */
    public static String backCovertUrl(String url) {
        try {
            // 新的替换规则
            url = url.replaceAll("and_esc", "&");
            url = url.replaceAll("sharp_esc", "#");
            url = url.replaceAll("equal_esc", "=");
            url = url.replaceAll("interrogation_esc", "?");
            url = url.replaceAll("percent_esc", "%");
            url = url.replaceAll("add_esc", "+");
            url = url.replaceAll("dollar_esc", "\\$");
            
            // 兼容以前老版本的转换
            // 顺序很重要
            url = url.replaceAll("!url!", "&");
            url = url.replaceAll("!35!", "#");
            url = url.replaceAll("!61!", "=");
            url = url.replaceAll("!63!", "?");
            url = url.replaceAll("!215!", "×");
            url = url.replaceAll("!9999!", "%");
            url = url.replaceAll("!20!", "+");
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
    }
    
    /**
     * 自定义的md5加密，注意不要修改key值
     * 
     * @param str
     * @return
     */
    public static String getMD5Str(String str) {
        str = "youkuatm" + str + "atm5!%^&*(#0";
        return md5(str);
    }
    
    
    /**
     * 标准的md5加密
     * 
     * @param str
     * @return
     */
    public static String md5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }
    
    
    public static String doubleEncode(String s, String encode) {
        try {
            s = URLEncoder.encode(s, encode);
            return URLEncoder.encode(s, encode);
        } catch (Exception e) {
            e.printStackTrace();
            return s;
        }
    }
    
    public static int getValueAt(Integer[] array, int position) {
        Arrays.sort(array);
        return array.length >= position ? array[array.length - position] : 0;
    }
    
    /**
     * 获取ip的长整型表示<br>
     * 
     * @param ip_string -- ip字符串
     * @return ip_long -- ip长整型
     */
    public static long getIPLong(String ip) {
        long iplong = 0;
        String[] ipArray = ip.split("\\.");
        int length = ipArray.length;
        for (int i = 0; i < length; i++) {
            long temp = Long.valueOf(ipArray[i]);
            int moveTime = 24 - 8 * i;
            temp = temp << moveTime;
            iplong += temp;
        }
        return iplong;
    }
    
    // req.getHeader("X-Original-To")
    public static String getOriginalIp(HttpServletRequest request) {
        String ip = "";
        Enumeration ips = request.getHeaders("X-Original-To");
        // 遍历取到最后一个X-Forwarded-For
        while (ips.hasMoreElements()) {
            ip = (String) ips.nextElement();
        }
        return ip;
    }
    
    
    public static String requestPost(String url, String param) {
        try {
            return requestPost(url, param, DEFAULT_TIMEOUT);
        } catch (IOException e) {
        	System.out.println("发送Post请求出现异常: " + url);
        }
        return "";
    }
    
    
    public static String requestPost(String url, String param, int timeout) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (timeout > 0) {
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
            } else {
                conn.setConnectTimeout(DEFAULT_TIMEOUT);
                conn.setReadTimeout(DEFAULT_TIMEOUT);
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            //Ec.log("发送Post请求出现异常！"+ url, e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                //Ec.log("Post请求关闭输入输出流异常！"+ url, ex);
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        System.out
                .println(covertUrl("http://yktd.m.cn.miaozhen.com/r.gif?k=2000238&p=6qAku&rt=2&ns=[M_ADIP]&ni=[M_IESID]&na=[M_MAC]&v=[M_LOC]&o=http://dolcegusto.tmall.com/p/rd757180.htm?kid=16008_73288_244598_297306"));
        
    }
}
