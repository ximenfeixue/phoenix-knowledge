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
    ESmartHardware(19,  KnowledgeSmartHardware.class, "KnowledgeSmartHardware", "智能硬件"),
    //  IT
    EIT(20,  KnowledgeIT.class, "KnowledgeIT", "IT"),
    //消费品
    EConsumerGoods(21,  KnowledgeConsumerGoods.class, "KnowledgeConsumerGoods", "消费品"),
    //制造
    EManufacture(22,  KnowledgeManufacture.class, "KnowledgeManufacture", "制造"),
    //文化
    ECulture(23,  KnowledgeCulture.class, "KnowledgeCulture", "文化"),
    //体育
    ESports(24,  KnowledgeSports.class, "KnowledgeSports", "体育"),
    //传媒
    EMedia(25,  KnowledgeMedia.class, "KnowledgeMedia", "传媒"),
    //建筑
    EBuilding(26,  KnowledgeBuilding.class, "KnowledgeBuilding", "建筑"),
    //房地产
    ERealEstate(27,  KnowledgeRealEstate.class, "KnowledgeRealEstate", "房地产"),
    //贸易
    ETrade(28,  KnowledgeTrade.class, "KnowledgeTrade", "智能硬件"),
    //专业服务
    EProfessionalService(29,  KnowledgeProfessionalService.class, "KnowledgeProfessionalService", "专业服务"),
    //生活服务
    ELifeService(30,  KnowledgeLifeService.class, "KnowledgeLifeService", "生活服务"),
    //政府
    EGovernment(31,  KnowledgeGovernment.class, "KnowledgeGovernment", "政府"),
    EMax(32, null, "EMax", "EMax");
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
            case 2872:
                return KnowledgeType.EInternet;
            case 13:
            case 2873:
                return KnowledgeType.EBigData;
            case 14:
            case 2874:
                return KnowledgeType.EFinance;
            case 15:
            case 2875:
                return KnowledgeType.EEducation;
            case 16:
            case 2876:
                return KnowledgeType.EMedical;
            case 17:
            case 2877:
                return KnowledgeType.EO2O;
            case 18:
            case 2878:
                return KnowledgeType.EGame;
            case 19:
            case 2879:
                return KnowledgeType.ESmartHardware;
            case 20:
            case 2880:
                return KnowledgeType.EIT;
            case 21:
            case 2881:
                return KnowledgeType.EConsumerGoods;
            case 22:
            case 2882:
                return KnowledgeType.EManufacture;
            case 23:
            case 2883:
                return KnowledgeType.ECulture;
            case 24:
            case 2884:
                return KnowledgeType.ESports;
            case 25:
            case 2885:
                return KnowledgeType.EMedia;
            case 26:
            case 2886:
                return KnowledgeType.EBuilding;
            case 27:
            case 2887:
                return KnowledgeType.ERealEstate;
            case 28:
            case 2888:
                return KnowledgeType.ETrade;
            case 29:
            case 2889:
                return KnowledgeType.EProfessionalService;
            case 30:
            case 2890:
                return KnowledgeType.ELifeService;
            case 31:
            case 2891:
                return KnowledgeType.EGovernment;
            default: {
                logger.error("Can't find the type: " + type + ", so return default value: " + KnowledgeType.ENews.value());
                return KnowledgeType.ENews;
            }
        }
    }

}
