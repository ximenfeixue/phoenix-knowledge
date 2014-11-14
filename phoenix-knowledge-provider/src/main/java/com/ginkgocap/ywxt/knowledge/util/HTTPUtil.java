package com.ginkgocap.ywxt.knowledge.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPUtil {

	private static Logger logger = LoggerFactory.getLogger(HTTPUtil.class);

	public static HttpClient httpClient;

	public static LinkedBlockingQueue<StandByUrl> urls;

	public static String location = "http://192.168.130.119:8090/";

	static {

		httpClient = new DefaultHttpClient();

		httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		
		urls = new LinkedBlockingQueue<StandByUrl>();

	}

	public static String post(String url, Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpPost post = new HttpPost(location + url);
		
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
				HttpResponse response = httpClient.execute(post);
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
	
	public static  String get(String url, Map<String, String> params) {

		HttpGet get = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writeValueAsString(params);
			StringBuffer urlBuffer = new StringBuffer(url + "?json=" + jsonStr);
			urlBuffer.deleteCharAt(urlBuffer.length() - 1);
			get = new HttpGet(urlBuffer.toString());

			logger.info("开始以GET方式访问路径{}", get.getRequestLine().toString());
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				String respJson = EntityUtils.toString(entity);
				logger.info("访问路径{}成功", url);
				return respJson;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}

		return null;
	}


	public static class StandByUrl {

		public static enum METHORD {
			POST, GET
		}

		private String url;

		private Map<String, String> params;

		private METHORD methord;

		public StandByUrl(String url, Map<String, String> params,
				METHORD methord) {
			super();
			this.url = url;
			this.params = params;
			this.methord = methord;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Map<String, String> getParams() {
			return params;
		}

		public void setParams(Map<String, String> params) {
			this.params = params;
		}

		public METHORD getMethord() {
			return methord;
		}

		public void setMethord(METHORD methord) {
			this.methord = methord;
		}

	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/** 移动端 zhangzhen add 2014-11-6 */
	public static String mobilePost(String url, Map<String, String> params) {

		Map<String, Object> result = new HashMap<String, Object>();
		HttpPost post = new HttpPost(url);
		if (params != null) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			try {
				
				for(Entry<String,String> entry : params.entrySet()){
					list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				
				UrlEncodedFormEntity formEntity;
				formEntity = new UrlEncodedFormEntity(list, "utf-8");
				post.setEntity(formEntity);
				logger.info("开始以POST方式访问路径{}，参数为{}", url, formEntity.toString());
				HttpResponse response = httpClient.execute(post);
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
