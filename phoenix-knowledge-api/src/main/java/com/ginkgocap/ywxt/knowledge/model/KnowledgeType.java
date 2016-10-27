package com.ginkgocap.ywxt.knowledge.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gintong on 2016/7/12.
 */
public enum KnowledgeType {
    // 资讯
    ENews(1, KnowledgeNews.class, "KnowledgeNews", "资讯"),
    // 投融工具
    EInvestment(2, KnowledgeInvestment.class, "KnowledgeInvestment", "投融工具"),
    // 行业
    EIndustry(3, KnowledgeIndustry.class, "KnowledgeIndustry", "行业"),
    // 经典案例
    ECase(4, KnowledgeCase.class, "KnowledgeCase", "经典案例"),
    // 新材料
    ENewMaterials(5, KnowledgeNewMaterials.class, "KnowledgeNewMaterials", "新材料"),
    // 资产管理
    EAsset( 6, KnowledgeAsset.class, "KnowledgeAsset", "资产管理"),
    // 宏观
    EMacro( 7, KnowledgeMacro.class, "KnowledgeMacro", "宏观"),
    // 观点
    EOpinion(8, KnowledgeOpinion.class, "KnowledgeOpinion", "观点"),
    // 判例
    EExample(9, KnowledgeExample.class, "KnowledgeExample", "判例"),
    // 法律法规
    ELaw(10, KnowledgeLaw.class, "KnowledgeLaw", "法律法规"),
    // 文章
    EArticle(11, KnowledgeArticle.class, "KnowledgeArticle", "文章"),
    //互联网
    EInternet(12, KnowledgeInternet.class, "KnowledgeInternet", "互联网"),
    //大数据
    EBigData(13, KnowledgeBigData.class, "KnowledgeBigData", "大数据"),
    //金融
    EFinance(14, KnowledgeFinance.class, "KnowledgeFinance", "金融"),
    //教育
    EEducation(15, KnowledgeEducation.class, "KnowledgeEducation", "教育"),
    //医疗
    EMedical(16, KnowledgeMedical.class, "KnowledgeMedical", "医疗"),
    //O2O
    EO2O(17, KnowledgeO2O.class, "KnowledgeO2O", "O2O"),
    //游戏
    EGame(18, KnowledgeGame.class, "KnowledgeGame", "游戏"),
    //智能硬件
    ESmartHardware(19,  KnowledgeSmartHardware.class, "KnowledgeSmartHardware", "智能硬件");

    int code;
    Class cls;
    String dec;
    String typeName;

    private final static Logger logger = LoggerFactory.getLogger(KnowledgeType.class);

    private KnowledgeType(int code,Class cls, String dec, String typeName) {
        this.code = code;
        this.cls = cls;
        this.dec = dec;
        this.typeName = typeName;
    }

    public int value() {
        return code;
    }

    public String dec() {
        return dec;
    }

    public String tableName() {
        return dec;
    }

    public String typeName() { return typeName; }

    public Class cls() {
        return cls;
    }

    public static KnowledgeType knowledgeType(int type){
        switch (type){
            case 1:
                return KnowledgeType.ENews;
            case 2:
                return KnowledgeType.EInvestment;
            case 3:
                return KnowledgeType.EIndustry;
            case 4:
                return KnowledgeType.ECase;
            case 5:
                return KnowledgeType.ENewMaterials;
            case 6:
                return KnowledgeType.EAsset;
            case 7:
                return KnowledgeType.EMacro;
            case 8:
                return KnowledgeType.EOpinion;
            case 9:
                return KnowledgeType.EExample;
            case 10:
                return KnowledgeType.ELaw;
            case 11:
                return KnowledgeType.EArticle;
            case 12:
                return KnowledgeType.EInternet;
            case 13:
                return KnowledgeType.EBigData;
            case 14:
                return KnowledgeType.EFinance;
            case 15:
                return KnowledgeType.EEducation;
            case 16:
                return KnowledgeType.EMedical;
            case 17:
                return KnowledgeType.EO2O;
            case 18:
                return KnowledgeType.EGame;
            case 19:
                return KnowledgeType.ESmartHardware;
            default: {
                logger.error("Can't find the type: " + type + ", so return default value: " + KnowledgeType.ENews.value());
                return KnowledgeType.ENews;
            }
        }
    }

}
