package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by gintong on 2016/7/12.
 */
public class KnowledgeInvestment extends KnowledgeDetail implements Serializable
{
    private String synonyms; // 同义词

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }
}
