package com.example.remote.utils;

/**
 * 网络请求工具类
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
public class WebUtils {

    private static Log LOG = LogFactory.getLog(WebUtils.class);
    public static final int cache = 10 * 1024;


    /**
     * get方式访问服务器
     * @param url
     * @return
     */
    public static String getJsonStrFromGetUrl(String url) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            get.addHeader("Content-Type", "application/json");
            LOG.info("try get json, uri is:" + get.getURI());
            String result = "";
            try {
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    /* 读返回数据 */
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity);
                    }
                    if (!StringUtils.isEmpty(result)) {
                        LOG.info("result is:" + (result.length() > 20 ?
                                (result.substring(0, 20) + "...") : result));
                    } else {
                        LOG.info("result is empty.");
                    }
                    return result;
                } else {
                    LOG.info("status error:"
                            + response.getStatusLine().getStatusCode()
                            + EntityUtils.toString(response.getEntity(), "UTF-8"));
                    return "";
                }
            } catch (ClientProtocolException ex) {
                LOG.error("访问服务器失败，原因:" + ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                LOG.error("访问服务器失败，原因:" + ex);
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            LOG.error("访问服务器失败，原因:" + ex);
            ex.printStackTrace();
        }
//        LOG.error("访问服务器发生异常");
        return "";
    }




    /**
     * 下载文件到指定文件
     * @param url
     * @param filepath 文件名称需要带后缀
     * @return
     */
    public static String download(String url, String filepath) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            File file = new File(filepath);
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            /**
             * 根据实际运行效果 设置缓冲区大小
             */
            byte[] buffer=new byte[cache];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer,0,ch);
            }
            is.close();
            fileout.flush();
            fileout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
