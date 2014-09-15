package com.ginkgocap.ywxt.knowledge.service.knowledgecolumn.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;

/**
 * 
 * @author guangyuan
 * @since 1.2.2
 */
public class KCHelper {

    public static Integer FIRST_PATH_NUMBER=6;
    public static String[] kcTypeArray={"其他","资讯","投融工具","行业","经典案例","图书报告","资产管理","宏观","观点","判例","法律法规","文章"};
    public static List<String> kcTypeList =new ArrayList<String>(Arrays.asList(kcTypeArray));
    
    public static Map<String,String> kcTypeMap=new HashMap<String,String>();
    static{
        kcTypeMap.put("0", "其它");
        kcTypeMap.put("1", "资讯");
        kcTypeMap.put("2", "投融工具");
        kcTypeMap.put("3", "行业");
        kcTypeMap.put("4", "经典案例");
        kcTypeMap.put("5", "图书报告");
        kcTypeMap.put("6", "资产管理");
        kcTypeMap.put("7", "宏观");
        kcTypeMap.put("8", "观点");
        kcTypeMap.put("9", "判例");
        kcTypeMap.put("10", "法律法规");
        kcTypeMap.put("11", "文章");
        
    }
    
    public static KnowledgeColumn analysisType(KnowledgeColumn kc){
        Integer kcType =0;
        String path=kc.getColumnLevelPath();
        kcType=analysisByLevelPath(path);
        kc.setKcType(kcType);
        return kc;
    }
    
    public static Integer analysisByLevelPath(String path){
        int endIndex=(FIRST_PATH_NUMBER<=path.length())?FIRST_PATH_NUMBER:path.length();
        String pa=path.substring(0, endIndex);
        System.out.println(pa);
        Integer kt=Integer.valueOf(pa);
        return kt;
    }
    
    public static Integer analysisByColumnName(String name){
        return 0;
    }
    
    /**
     * 因数据库类型与期望类型不一致，故作转换
     * @param name
     * @return 
     */
    public static String getMysqlkcType(Integer name){
        return name.toString();
    }
    
    public static void main(String[] args) {
        
//        for (int i = 0; i < kcTypeList.size(); i++) {
//            System.out.println(i+" "+kcTypeList.get(i));
//        }
        
        KnowledgeColumn kc=new KnowledgeColumn();
        kc.setColumnLevelPath("000007000002");
        analysisType(kc);
        System.out.println(kc.getKcType());
    }
}
