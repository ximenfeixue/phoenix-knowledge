package com.ginkgocap.ywxt.knowledge.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 模版相关操作工具
 * ftl文件放在\src\main\resources\template
 * @author Administrator
 *
 */
public class TemplateUtils {
    private static FreeMarkerConfigurer freeMarkerConfigurer;
    private static String charset;
    private static String templateLoaderPath;
    

    /**
     * 更新模版
     * @param templateName 名称
     * @param content 内容
     * @return
     */
    public static boolean updateTemplateContent(String templateName,String content){
        OutputStream os = null;
        OutputStreamWriter osw = null;
        try{
            URL url = ClassUtils.class.getResource(templateLoaderPath+templateName);
            os = new FileOutputStream(url.getPath());
            osw = new OutputStreamWriter(os, Charset.forName("UTF-8"));
            osw.write(content);
            osw.flush();
            os.close();
            osw.close();
        }catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(TemplateUtils.class).error("updateTemplateContent",e);
        }
        return false;
    }
    
    /**
     * 只获取模版内容，不执行
     * @param templateName 模版名称
     * @return
     */
    public static String getTemplateContent(String templateName) {
        StringBuffer content = new StringBuffer();
        try {
            TemplateLoader tl = freeMarkerConfigurer.getConfiguration().getTemplateLoader();
            Object obj = tl.findTemplateSource(templateName);
            Reader reader = tl.getReader(obj, charset);
            BufferedReader br = null;
            String tmp = null;
            br = new BufferedReader(reader);
            while ((tmp = br.readLine()) != null) {
                content.append(tmp);
            }
            br.close();
            reader.close();
            tl.closeTemplateSource(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    /**
     * 渲染模版
     * @param templateName 模版名称
     * @param params 可用参数
     * @return
     */
    public static String mergeTemplateContent(String templateName,Map<String,Object> params){
        String content = null;
        try {
            Template t = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            t.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            content = FreeMarkerTemplateUtils.processTemplateIntoString(t, params);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (TemplateException e) {
            e.printStackTrace();
        }
        return content;
    }
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        TemplateUtils.freeMarkerConfigurer = freeMarkerConfigurer;
    }
    public void setCharset(String charset) {
        TemplateUtils.charset = charset;
    }
    public void setTemplateLoaderPath(String templateLoaderPath) {
        TemplateUtils.templateLoaderPath = templateLoaderPath;
    }
}
