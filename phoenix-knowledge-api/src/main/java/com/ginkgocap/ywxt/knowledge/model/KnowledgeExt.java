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
        this.setId(detail.getId());
        this.setColumnid(detail.getColumnid());
        this.setColumnType(detail.getColumnType());

        this.setCid(detail.getCid());
        this.setCname(detail.getCname());
        this.setTitle(detail.getTitle());
        this.setContent(detail.getContent());
        this.setMultiUrls(detail.getMultiUrls());
        this.setAttachUrls(detail.getAttachUrls());
        this.setModifytime(detail.getModifytime());
        this.setCreatetime(detail.getCreatetime());
        this.setS_addr(detail.getS_addr());
        this.setDirectorys(null);
        this.setTags(null);
        this.setEssence(detail.getEssence());
        this.setStatus(detail.getStatus());
        this.setTranStatus(detail.getTranStatus());
        this.setReport_status(detail.getReport_status());
        this.setTaskid(detail.getTaskid());
        this.setIsh(detail.getIsh());
        this.setVirtual(detail.getVirtual());
        this.setPic(detail.getPic());
        this.setHcontent(detail.getHcontent());
        this.setCollected(detail.getCollected() == 0 ? false : true);
        this.setCpathid(detail.getCpathid());
        this.setDesc(detail.getDesc());
        this.setSource(detail.getSource());
        this.setSelfDef(detail.getSelfDef());
    }
}
