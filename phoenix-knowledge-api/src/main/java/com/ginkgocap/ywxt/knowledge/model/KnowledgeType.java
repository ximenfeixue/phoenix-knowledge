package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by gintong on 2016/7/12.
 */
public enum KnowledgeType {
          News(1, "com.ginkgocap.ywxt.knowledge.model.KnowledgeNews"),
    Investment(2, "com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment"),
    Industry  (3, "com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry"), Case(
            4, "com.ginkgocap.ywxt.knowledge.model.KnowledgeCase"), NewMaterials(
            5, "com.ginkgocap.ywxt.knowledge.model.KnowledgeNewMaterials"), Asset(
            6, "com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset"), Macro(
            7, "com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro"), Opinion(
            8, "com.ginkgocap.ywxt.knowledge.model.KnowledgeOpinion"), Example(
            9, "com.ginkgocap.ywxt.knowledge.model.example"), Law(
            10,"com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw"), Article(
            11,"com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle");

    private int value;

    private String obj;

    private KnowledgeType(int v, String obj) {
        this.value = v;
        this.obj = obj;
    }

    public int value() {
        return value;
    }

    public String obj() {
        return obj;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
