package com.ginkgocap.ywxt.knowledge.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Joiner;


public class HttpClientUtil {
	
	private static final String dateTimeFormat = "MM/dd/yyyy HH:mm:ss";
	
	public static String getGintongPost(String url ,String interfaceName,String json) throws java.net.SocketTimeoutException,IOException,Exception{
	        
			//HttpClient httpClient = new DefaultHttpClient();
		    HttpClient httpClient = HttpConnectionManager.getHttpClient();
			
	        HttpPost postRequest = new HttpPost(url+interfaceName);
	        StringEntity input = null;
	        try {
	            input = new StringEntity(json);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	        }
	        input.setContentType("application/json;charset=UTF-8");
	        postRequest.setEntity(input);
	        input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
	        postRequest.setHeader("Accept", "application/json");
	        postRequest.setEntity(input);

	        try {
	        	httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
	        	httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
	            HttpResponse response = httpClient.execute(postRequest);
	            HttpEntity entity = response.getEntity();
	            if(entity != null){
	            	
	                //System.out.println(EntityUtils.toString(entity));
	            	String a = EntityUtils.toString(entity).toString();
		            //System.out.println(a);
		            return a;
	            } else {
	            	System.out.println("no response when post http request");
	            }
	            	
	        }catch(java.net.SocketTimeoutException t) {
	        	t.printStackTrace();
	        	return "";
	        }
	        catch (Exception e) {
	        	System.out.println("请求phoenix-mobile异常");
	            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	        }
			return "";
	        
	}
	


}
