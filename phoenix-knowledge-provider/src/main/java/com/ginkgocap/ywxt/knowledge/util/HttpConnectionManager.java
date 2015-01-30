package com.ginkgocap.ywxt.knowledge.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("deprecation")
public class HttpConnectionManager {   
  
    private static HttpParams httpParams;  
    private static ClientConnectionManager connectionManager;  
    private static Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);
  
    /** 
     * 最大连接数 
     */  
    public final static int MAX_TOTAL_CONNECTIONS = 800;  
    /** 
     * 获取连接的最大等待时间 
     */  
    public final static int WAIT_TIMEOUT = 60000;  
    /** 
     * 每个路由最大连接数 
     */  
    public final static int MAX_ROUTE_CONNECTIONS = 400;  
    /** 
     * 连接超时时间 
     */  
    public final static int CONNECT_TIMEOUT = 10000;  
    /** 
     * 读取超时时间 
     */  
    public final static int READ_TIMEOUT = 10000;  
  
    static {  
        httpParams = new BasicHttpParams();  
        // 设置最大连接数  
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);  
        // 设置获取连接的最大等待时间  
        ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);  
        // 设置每个路由最大连接数  
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);  
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams,connPerRoute);  
        // 设置连接超时时间  
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);  
        // 设置读取超时时间  
        HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);  
  
        SchemeRegistry registry = new SchemeRegistry();  
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));  
  
        connectionManager = new ThreadSafeClientConnManager(httpParams, registry);  
    }  
  
    public static HttpClient getHttpClient() {  
        return new DefaultHttpClient(connectionManager, httpParams);  
    }  
    
    public static String post(String url, Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpPost post = new HttpPost(url);
		
		if (params != null) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			try {
				ObjectMapper mapper = new ObjectMapper();
				String jsonStr = mapper.writeValueAsString(params);
				list.add(new BasicNameValuePair("json", jsonStr));
				UrlEncodedFormEntity formEntity;
				formEntity = new UrlEncodedFormEntity(list, "utf-8");
				post.setEntity(formEntity);
				logger.info("开始以POST方式访问路径{}，参数为{}", url, formEntity.toString());
				HttpResponse response = getHttpClient().execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					result.put(Constants.status, response.getStatusLine()
							.getStatusCode());
					HttpEntity entity = response.getEntity();
					String respJson = EntityUtils.toString(entity);
					logger.info("访问路径{}成功", url);
					return respJson;
				}

			} catch (UnsupportedEncodingException e) {
				logger.error("编码错误", e);
			} catch (ClientProtocolException e) {
				logger.error("协议错误", e);
			} catch (IOException e) {
				logger.error("IO错误", e);
			} finally {
				post.releaseConnection();
			}
		}

		return null;
	}
  
}  