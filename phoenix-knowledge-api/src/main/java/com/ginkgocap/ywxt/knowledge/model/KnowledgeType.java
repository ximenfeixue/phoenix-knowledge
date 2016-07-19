package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by gintong on 2016/7/12.
 */
public enum KnowledgeType {
    // 资讯
    ENews(1, "KnowledgeNews", KnowledgeNews.class),
    // 投融工具
    EInvestment(2, "KnowledgeInvestment", KnowledgeInvestment.class),
    // 行业
    EIndustry(3, "KnowledgeIndustry", KnowledgeIndustry.class),
    // 经典案例
    ECase(4, "KnowledgeCase", KnowledgeCase.class),
    // 新材料
    ENewMaterials(5, "KnowledgeNewMaterials", KnowledgeNewMaterials.class),
    // 资产管理
    EAsset( 6, "KnowledgeAsset",KnowledgeAsset.class),
    // 宏观
    EMacro( 7, "KnowledgeMacro",KnowledgeMacro.class),
    // 观点
    EOpinion(8, "KnowledgeOpinion",KnowledgeOpinion.class),
    // 判例
    EExample(9, "KnowledgeExample", KnowledgeExample.class),
    // 法律法规
    ELaw(10, "KnowledgeLaw",KnowledgeLaw.class),
    // 文章
    EArticle(11,"KnowledgeArticle",KnowledgeArticle.class),
    //互联网
    EInternet(5001, "KnowledgeInternet", KnowledgeInternet.class),
    //大数据
    EBigData(5002, "KnowledgeBigData", KnowledgeBigData.class),
    //金融
    EFinance(5003, "KnowledgeFinance", KnowledgeFinance.class),
    //教育
    EEducation(5004, "KnowledgeEducation", KnowledgeEducation.class),
    //医疗
    EMedical(5005, "KnowledgeMedical", KnowledgeMedical.class),
    //O2O
    EO2O(5006, "KnowledgeO2O", KnowledgeO2O.class),
    //游戏
    EGame(5007, "KnowledgeGame", KnowledgeGame.class),
    //智能硬件
    ESmartHardware(5008, "KnowledgeSmartHardware", KnowledgeSmartHardware.class);

    int code;
    String dec;
    Class cls;

    private KnowledgeType(int code,String dec,Class cls) {
        this.code = code;
        this.dec = dec;
        this.cls = cls;
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

    public Class cls() {
        return cls;
    }

    /**
     * Method: knowledgeEnumFactory <br>
     * Description: 一个简单的工厂，根据传入的类型构造不同的知识枚举 <br>
     * Creator: xutianlong@gingtong.com <br>
     * Date: 2016/1/25 23:13
     */
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
            case 5001:
                return KnowledgeType.EInternet;
            case 5002:
                return KnowledgeType.EBigData;
            case 5003:
                return KnowledgeType.EFinance;
            case 5004:
                return KnowledgeType.EEducation;
            case 5005:
                return KnowledgeType.EMedical;
            case 5006:
                return KnowledgeType.EO2O;
            case 5007:
                return KnowledgeType.EGame;
            case 5008:
                return KnowledgeType.ESmartHardware;
            default:
                return KnowledgeType.ENews;
        }
    }

}
