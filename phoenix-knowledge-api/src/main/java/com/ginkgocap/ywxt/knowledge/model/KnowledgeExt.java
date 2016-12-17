package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by oem on 12/17/16.
 */
public class KnowledgeExt extends Knowledge {

    private boolean collected;

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public static KnowledgeExt cloneExt(Knowledge detail)
    {
        KnowledgeExt ext = new KnowledgeExt();
        ext.clone(detail);
        return ext;
    }

    public void clone(Knowledge detail)
    {
        super.clone(detail);
        this.setCollected(detail.getCollected() == 0 ? false : true);
    }
}
