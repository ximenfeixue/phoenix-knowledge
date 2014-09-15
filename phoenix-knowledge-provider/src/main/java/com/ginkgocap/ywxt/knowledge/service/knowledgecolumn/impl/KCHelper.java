package com.ginkgocap.ywxt.knowledge.service.knowledgecolumn.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    
    
    public static KnowledgeColumn analysisType(KnowledgeColumn kc){
        Integer kcType =0;
        String path=kc.getColumnLevelPath();
        kcType=analysisByLevelPath(path);
        kc.setKcType(kcType);
        return kc;
    }
    
    public static Integer analysisByLevelPath(String path){
        String pa=path.substring(0, FIRST_PATH_NUMBER);
        System.out.println(pa);
        Integer kt=Integer.valueOf(pa);
        return kt;
    }
    
    public static Integer analysisByColumnName(String name){
        return 0;
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
