package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;

/**
 * 栏目相关功能辅助类
 * @author guangyuan
 * @since 1.2.2
 */
public class KCHelper {

    public static Integer FIRST_PATH_NUMBER=6;
    public static Integer DEFAULT_TYPE=99;
    public static String[] kcTypeArray={"其他","资讯","投融工具","行业","经典案例","图书报告","资产管理","宏观","观点","判例","法律法规","文章"};
    public static List<String> kcTypeList =new ArrayList<String>(Arrays.asList(kcTypeArray));
    
    //kcTypeMap 存放栏目类型的map，key是栏目所对应的数据库id,value是栏目所对应的名称
    //因栏目表和知识表都无栏目类型标识，但栏目订阅表有此char类型的标识，并且知识查询也用到所属类型
    public static Map<String,String> kcTypeMap=new HashMap<String,String>();
    static{
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
        kcTypeMap.put(DEFAULT_TYPE+"", "其它");//对应sqlKey为0，取值时应做判断,其值为0将与系统根pid的值重复
        
    }
    
    public static Integer resolveKCType(Long id,Long pid){
        Integer type=DEFAULT_TYPE;
        
        if (null==pid) {
            type=DEFAULT_TYPE;
            return type;
        }
        //当栏目父id为0,说明是一级栏目，只需取其id即可,
        if (pid==0) {
            type=id.intValue();
            return type;
        }
        
        String pids=String.valueOf(pid);
        
        if(kcTypeMap.containsKey(pids)){
            type=pid.intValue();
        }else{
            //默认类型
            //如果多层次，那么此处需在service中递归查询pid
            type=DEFAULT_TYPE;
        }
        return type;
    }
    
//    public static KnowledgeColumn setKCType(KnowledgeColumn kc){
//        Long pid=kc.getParentColumnId();
//        
//        if (null==pid) {
//            kc.setKcType(DEFAULT_TYPE);
//            return kc;
//        }
//        //当栏目父id为0,说明是一级栏目，只需取其id即可,
//        if (pid==0) {
//            kc.setKcType(kc.getId().intValue());
//            return kc;
//        }
//        
//        String pids=String.valueOf(pid);
//        
//        if(kcTypeMap.containsKey(pids)){
//            kc.setKcType(pid.intValue());
//        }else{
//            //默认类型
//            //如果多层次，那么此处需在service中递归查询pid
//            kc.setKcType(DEFAULT_TYPE);
//        }
//        return kc;
//    }
    
    /**
     * 因数据库类型与期望类型不一致，故作转换
     * @param name
     * @return 
     */
    public static Short getMysqlkcType(Integer name){
        
        if (name.equals(DEFAULT_TYPE)) {
            return 0;
        }
        
        return name.shortValue();
    }
//    根据columnlevelpath路径无法分析其类型,因path记录的只是等级
//    public static KnowledgeColumn analysisType(KnowledgeColumn kc){
//        Integer kcType =0;
//        String path=kc.getColumnLevelPath();
//        kcType=analysisByLevelPath(path);
//        kc.setKcType(kcType);
//        return kc;
//    }
//    public static Integer analysisByLevelPath(String path){
//        int endIndex=(FIRST_PATH_NUMBER<=path.length())?FIRST_PATH_NUMBER:path.length();
//        String pa=path.substring(0, endIndex);
//        System.out.println(pa);
//        Integer kt=Integer.valueOf(pa);
//        return kt;
//    }
    
    public static Integer analysisByColumnName(String name){
        return 0;
    }
    
    
    
    public static void main(String[] args) {
        
        System.out.println(resolveKCType(29l, 7l));
    }
    
   final class KCType{
        Long id;//对应于kcTypeMap中的key
        String name;
        String mongoCName;
        String sqlKey;//对应于sql中的标识
        
        public KCType(Long id,String name,String mongoCName,String sqlKey) {
            this.id=id;
            this.name=name;
            this.mongoCName=mongoCName;
            this.sqlKey=sqlKey;
        }
    }
}
