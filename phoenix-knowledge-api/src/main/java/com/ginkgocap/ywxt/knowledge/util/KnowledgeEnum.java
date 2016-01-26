package com.ginkgocap.ywxt.knowledge.util;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBaseExample;
import com.ginkgocap.ywxt.knowledge.model.*;

/**
 * Created by think on 2016/1/25 23:00 xutianlong@gintong.com
 * 知识枚举，用于mongo模板方法取值
 */
public enum KnowledgeEnum {
    // 资讯
    News(1, "KnowledgeNews", KnowledgeNews.class),
    // 投融工具
    Investment(2, "KnowledgeInvestment", KnowledgeInvestment.class),
    // 行业
    Industry(3, "KnowledgeIndustry", KnowledgeIndustry.class),
    // 经典案例
    Case(4, "KnowledgeCase", KnowledgeCase.class),
    // 新材料
    NewMaterials(5, "KnowledgeNewMaterials", KnowledgeNewMaterials.class),
    // 资产管理
    Asset( 6, "KnowledgeAsset",KnowledgeAsset.class),
    // 宏观
    Macro( 7, "KnowledgeMacro",KnowledgeMacro.class),
    // 观点
    Opinion(8, "KnowledgeOpinion",KnowledgeOpinion.class),
    // 判例
    Example(9, "example", KnowledgeBaseExample.class),
    // 法律法规
    Law(10, "KnowledgeLaw",KnowledgeLaw.class),
    // 文章
    Article(11,"KnowledgeArticle",KnowledgeArticle.class);

    int code;
    String dec;
    Class cls;

    private KnowledgeEnum(int code,String dec,Class cls) {
        this.code = code;
        this.dec = dec;
        this.cls = cls;
    }

    public int code() {
        return code;
    }

    public String dec() {
       return dec;
    }

    public Class cls() {
        return cls;
    }

    /**
     * Method: knowledgeEnumFactory <br>
     * Description: 一个简单的工厂，根据传入的类型构造不同的知识枚举 <br>
     * Creator: xutianlong@gingtong.com <br>
     * Date: 2016/1/25 23:13
     */
    public static KnowledgeEnum knowledgeEnumFactory(int type){
        switch (type){
            case 1:
                return KnowledgeEnum.News;
            case 2:
                return KnowledgeEnum.Investment;
            case 3:
                return KnowledgeEnum.Industry;
            case 4:
                return KnowledgeEnum.Case;
            case 5:
                return KnowledgeEnum.NewMaterials;
            case 6:
                return KnowledgeEnum.Asset;
            case 7:
                return KnowledgeEnum.Macro;
            case 8:
                return KnowledgeEnum.Opinion;
            case 9:
                return KnowledgeEnum.Example;
            case 10:
                return KnowledgeEnum.Law;
            default:
                return KnowledgeEnum.Article;
        }
    }
}

