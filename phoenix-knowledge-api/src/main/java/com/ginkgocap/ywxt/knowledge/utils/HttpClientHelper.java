package com.ginkgocap.ywxt.knowledge.utils;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);

    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private final static String ENCODING = "utf-8";

    public static String post(final String url, final List<BasicNameValuePair> pairs) throws Exception {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        String result = null;
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Connection", "close");
        HttpEntity entity;
        UrlEncodedFormEntity initEntity = new UrlEncodedFormEntity(pairs, ENCODING);
        initEntity.setContentType(CONTENT_TYPE);
        httppost.setEntity(initEntity);
        HttpResponse response = client.execute(httppost);
        entity = response.getEntity();
        if (entity != null) {
            InputStream instream = null;
            try {
                instream = entity.getContent();
                result = IOUtils.toString(instream);
            } catch (IOException ex) {
                throw ex;
            } catch (RuntimeException ex) {
                httppost.abort();
                throw ex;
            } finally {
                instream.close();
            }
        }
        return result;
    }

    public static String get(String url) throws Exception {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        String result = null;
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("Connection", "close");
        HttpResponse response = client.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            /* InputStream instream = null;
            try {
                instream = entity.getContent();
                result = IOUtils.toString(instream);
            } catch (IOException ex) {
                throw ex;
            } catch (RuntimeException ex) {
                httpget.abort();
                throw ex;
            } finally {
                instream.close();
            }*/
            result = EntityUtils.toString(entity , "utf-8").trim();
            httpget.abort();
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    public static String post(final String url, final Map<String, String> params) {
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
                HttpClient httpClient = httpClient();
                HttpResponse response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result.put("status", response.getStatusLine().getStatusCode());
                    HttpEntity entity = response.getEntity();
                    String respJson = EntityUtils.toString(entity);
                    logger.info("request {} success", url);
                    EntityUtils.consume(response.getEntity());
                    return respJson;
                }
                EntityUtils.consume(response.getEntity());
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

    public static  String get(final String url, final Map<String, String> params) {

        HttpGet get = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(params);
            StringBuffer urlBuffer = new StringBuffer(url + "?json=" + jsonStr);
            urlBuffer.deleteCharAt(urlBuffer.length() - 1);
            get = new HttpGet(urlBuffer.toString());

            logger.info("开始以GET方式访问路径{}", get.getRequestLine().toString());
            HttpClient httpClient = httpClient();
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String respJson = EntityUtils.toString(entity);
                logger.info("request {} success", url);
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

    public static String GET(final String url, final Map<String,String> headers)
    {
        HttpGet get = new HttpGet(url);
        try {
            logger.info("httpMethod: GET, url: {}, body: {}", get.getRequestLine().toString());
            for(String key:headers.keySet()){
                get.addHeader(key, headers.get(key));
            }
            HttpClient httpClient = httpClient();
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String respJson = EntityUtils.toString(entity);
                logger.info("request {} success", url);
                //logger.info("Response: {}", respJson);
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

    public static String POST(final String url, final String content, final Map<String,String> headers) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpPost post = new HttpPost(url);
        try {
            for(String key:headers.keySet()){
                post.addHeader(key, headers.get(key));
            }
            if (content != null) {
                StringEntity stringEntry = new StringEntity(content, ContentType.create("application/json", "UTF-8"));
                post.setEntity(stringEntry);
            }
            logger.info("httpMethod: POST, url: {}， body {}", url, content);
            HttpClient httpClient = httpClient();
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result.put("status", response.getStatusLine().getStatusCode());
                String respJson = EntityUtils.toString(response.getEntity(),"utf-8");
                logger.info("request {} success", url);
                EntityUtils.consume(response.getEntity());
                //logger.info("respJson: {}", respJson);
                return respJson;
            }
            EntityUtils.consume(response.getEntity());
        } catch (UnsupportedEncodingException e) {
            logger.error("error:", e);
        } catch (ClientProtocolException e) {
            logger.error("protocol:", e);
        } catch (IOException e) {
            logger.error("IO Error:", e);
        } finally {
            post.releaseConnection();
        }
        return null;
    }

    public static String PUT(final String url, final String params, final Map<String,String> headers) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpPut put = new HttpPut(url);
        if (params != null) {
            try {
            	StringEntity stringEntry = new StringEntity(params, ContentType.APPLICATION_JSON);
                put.setEntity(stringEntry);
                for(String key:headers.keySet()){
                    put.addHeader(key, headers.get(key));
                }
                logger.info("httpMethod: PUT, url: {}，body: {}{}", url, params);
                HttpClient httpClient = httpClient();
                HttpResponse response = httpClient.execute(put);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result.put("status", response.getStatusLine().getStatusCode());
                    HttpEntity entity = response.getEntity();
                    String respJson = EntityUtils.toString(entity);
                    logger.info("request {} success", url);
                    EntityUtils.consume(response.getEntity());
                    //logger.info("respJson: {}", respJson);
                    return respJson;
                }
                EntityUtils.consume(response.getEntity());
            } catch (UnsupportedEncodingException e) {
                logger.error("Encoding Error:", e);
            } catch (ClientProtocolException e) {
                logger.error("Protocol Error:", e);
            } catch (IOException e) {
                logger.error("IO Error", e);
            } finally {
                put.releaseConnection();
            }
        }

        return null;
    }

    public static String DELETE(final String url, final Map<String,String> headers) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpDelete delete = new HttpDelete(url);

        try {
            for(String key:headers.keySet()){
                delete.addHeader(key, headers.get(key));
            }
            HttpClient httpClient = httpClient();
            HttpResponse response = httpClient.execute(delete);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result.put("httpMethod: DELETE, status", response.getStatusLine().getStatusCode());
                HttpEntity entity = response.getEntity();
                String respJson = EntityUtils.toString(entity);
                logger.info("url: {}, body: {}", url);
                EntityUtils.consume(response.getEntity());
                return respJson;
            }
            EntityUtils.consume(response.getEntity());
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding Error:", e);
        } catch (ClientProtocolException e) {
            logger.error("Protocol Error:", e);
        } catch (IOException e) {
            logger.error("IO Error:", e);
        } finally {
            delete.releaseConnection();
        }

        return null;
    }

    private static HttpClient httpClient()
    {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 100000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 100000);
        return httpClient;
    }
}
