package com.ginkgocap.ywxt.knowledge.utils;

/**
 * Created by gintong on 2016/7/6.
 */
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/** @Description:  工具类
 * @Author:       qinguochao
 * @CreateDate:  2014-4-18
 * @Version:      [v1.0]
 */

public class Utils {
    /**
     * 判断对象是否为null或空
     * @param obj
     * return IOException
     */
    public static boolean isNullOrEmpty(Object obj){
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
    /**
     * 判断所有对象对象是否为不等null和不为空
     * @param obj
     * return IOException
     */
    public static boolean isAllNotNullOrEmpty(Object... obj){

        for(Object ob:obj){
            if(isNullOrEmpty(ob)){
                return false;
            }
        }
        return true;
    }

    /**
     * 币种转换
     * @param cur
     * @return
     *
    public static InvestKeyword curToInvestKeyword(String cur){
        InvestKeyword investKeyword=null;
        if(cur!=null){
            //获取类型
            String moneyTypeTag [] =new String[]{"CNY", "USD", "EUR", "GBP", "RUB",
                    "HKD", "JPY", "MOP", "KRW", "THB",
                    "MYR", "TWD", "SGD", "NZD", "CHF",
                    "DKK", "NOK", "SEK", "CAD", "IDR",
                    "PHP", "AUD"};
            String moneyTypeName [] = new String[] {"人民币", "美元", "欧元", "英国镑", "俄罗斯卢布",
                    "港币", "日元", "澳门元", "韩国元", "泰国铢",
                    "马来西亚林吉特", "台湾新台币", "新加坡元", "新西兰元", "瑞士法郎",
                    "丹麥克朗", "挪威克朗", "瑞典克朗", "加拿大元", "印尼卢比",
                    "菲律宾比索", "澳大利亚元"};
            for(int i = 0; i < moneyTypeTag.length; i++) {
                if(cur.equals(moneyTypeTag[i])){
                    investKeyword=new InvestKeyword();
                    MoneyType mt =new MoneyType();
                    mt.setTag(moneyTypeTag[i]);
                    mt.setName(moneyTypeName[i]);
                    investKeyword.setMoneyType(mt);
                    break;
                }
            }
            if(null==investKeyword){
                for(int i = 0; i < moneyTypeName.length; i++) {
                    if(cur.equals(moneyTypeName[i])){
                        investKeyword=new InvestKeyword();
                        MoneyType mt =new MoneyType();
                        mt.setTag(moneyTypeTag[i]);
                        mt.setName(moneyTypeName[i]);
                        investKeyword.setMoneyType(mt);
                        break;
                    }
                }
            }
        }
        return investKeyword;
    }*/

    /**
     * douyou
     * <p>获取ip地址，由于经过nginx跳转，所以不能单纯的request.getRemoteAddr</p>
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * txt转html  UTF-8编码
     * @param content txt文本内容字符串，行结尾必须以\r\n结束
     * @param urls 图片url
     * @return html内容字符串
     */
    public static String txt2Html(String content, List<String> urls,String[] listImageUrl,String neturl) {
        StringBuffer htmlsb = new StringBuffer("<!DOCTYPE html><html><head><meta charset='utf-8' /><style>.gtrelated img{margin-top:10px;max-width:96%;margin-left:2%;height:auto;}.gtrelated{word-break: break-all;word-wrap: break-word; overflow-x: hidden; overflow-y:auto; } body { letter-spacing: 0.1em; line-height: 1.5em;} table{ width:100%; border-top: #bbb solid 1px;border-left: #bbb solid 1px; text-align: center;}table td{ border-right: #bbb solid 1px; border-bottom: #bbb solid 1px;} </style></head><body><div class='gtrelated'>");

        String[] lines = content.split("\n");
        for(String line : lines){
            htmlsb.append("<p>" + line.replace("\r", "") + "</p>");
        }
        for(String url : urls){
            htmlsb.append("<img src='" + url + "'/> <br/> <br/>");
        }
        if (null != listImageUrl) {
            for (String img : listImageUrl) {
                htmlsb.append("<img src='" + img + "'/> <br/> <br/>");
            }
        }
        if(!neturl.equals("")) {
            htmlsb.append("<a href='"+ neturl + "'>原网址</a>");
        }
        htmlsb.append("</div></body></html>");
        return htmlsb.toString();
    }

}
