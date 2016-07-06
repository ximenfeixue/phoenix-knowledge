package com.ginkgocap.ywxt.knowledge.utils;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpClientHelper {
    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private final static String ENCODING = "utf-8";

    public static String post(String url, List<BasicNameValuePair> pairs) throws Exception {
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
}
