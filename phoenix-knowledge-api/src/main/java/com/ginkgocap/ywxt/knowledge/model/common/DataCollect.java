package com.ginkgocap.ywxt.knowledge.model.common;

import java.io.Serializable;
import java.util.*;

import com.ginkgocap.ywxt.knowledge.model.BigData;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.organ.model.organ.OrganResourceVO;
import com.gintong.common.phoenix.permission.ResourceType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.entity.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gintong on 2016/7/18.
 */
public class DataCollect implements Serializable 
{
    private final static Logger logger = LoggerFactory.getLogger(DataCollect.class);
    // 知识简略信息
    private KnowledgeBase knowledge;

    // 知识详细信息
    private Knowledge knowledgeDetail;

    // 知识来源
    private KnowledgeReference reference;

    // 关联
    private List<Associate> asso;

    // 权限
    private Permission permission;
    // 组织 资源 接收前端数据
    private OrganResourceVO organResourceVO;

    //同步到动态
    private short updateDynamic = 0;

    //更新知识，更改栏目
    private int oldType;

    private long readCount;

    private static final int maxLen = 255;
    private static final int maxUrlLen = 512;

    public KnowledgeBase getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(KnowledgeBase knowledge) {
        this.knowledge = knowledge;
    }

    public Knowledge getKnowledgeDetail() {
        return knowledgeDetail;
    }

    public void setKnowledgeDetail(Knowledge knowledgeDetail) {
        this.knowledgeDetail = knowledgeDetail;
    }

    public KnowledgeReference getReference() {
        return reference;
    }

    public void setReference(KnowledgeReference reference) {
        this.reference = reference;
    }

    public short getUpdateDynamic() {
        return updateDynamic;
    }

    public void setUpdateDynamic(short updateDynamic) {
        this.updateDynamic = updateDynamic;
    }


    public DataCollect() {
    }

    public DataCollect(KnowledgeBase knowledgeBase, KnowledgeReference reference) {
        this.knowledge = knowledgeBase;
        this.reference = reference;
    }

    public DataCollect(KnowledgeBase knowledgeBase, Knowledge knowledgeDetail) {
        this.knowledge = knowledgeBase;
        this.knowledgeDetail = knowledgeDetail;
    }

    public List<Associate> getAsso() {
        return asso;
    }

    public void setAsso(List<Associate> asso) {
        this.asso = asso;
    }

    public Permission getPermission() {
        return permission;
    }

    public void initPermission() {
        if (this.knowledgeDetail != null) {
            if (this.knowledgeDetail.getCid() == 0 || this.knowledgeDetail.getCid() == 1) {
                this.knowledgeDetail.setPrivated((short)0);
            } else {
                this.knowledgeDetail.setPrivated(privated(this.permission));
            }
        }
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public int getOldType() {
        return oldType;
    }

    public void setOldType(int oldType) {
        this.oldType = oldType;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public void serUserId(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User is null");
        }
        if (this.knowledgeDetail != null) {
            this.knowledgeDetail.setCid(userId);
        }
    }

    public void serUserInfo(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        if (this.knowledgeDetail != null) {
            this.knowledgeDetail.setCid(user.getId());
            this.knowledgeDetail.setCname(user.getName());
        }
    }

    public KnowledgeBase generateKnowledge() {
        if (this.knowledgeDetail != null) {
            return this.knowledge = generateKnowledge(knowledgeDetail, (short)0);
        }

        return this.knowledge;
    }
    public static KnowledgeBase generateKnowledge(Knowledge detail) {
        return generateKnowledge(detail, (short)0);
    }

    public static KnowledgeBase generateKnowledge(Knowledge detail,short type) {
        KnowledgeBase base = null;
        if (detail != null) {
            base = new KnowledgeBase();
            base.setId(detail.getId());
            base.setKnowledgeId(detail.getId());
            base.setTitle(detail.getTitle());
            String knowledgeContent = HtmlToText.htmlToText(detail.getContent());
            base.setContentDesc(knowledgeContent);

            String coverPicUrl = detail.getPic();
            if (StringUtils.isNotBlank(coverPicUrl) && !"null".equals(coverPicUrl)) {
                logger.info("save cover picture: " + coverPicUrl);
                base.setCoverPic(coverPicUrl);

            } else {
                coverPicUrl = validatePicUrl(detail.getMultiUrls());
                logger.info("save cover picture: " + coverPicUrl);
                base.setCoverPic(coverPicUrl);
            }

            //knowledge.setAuditStatus(auditStatus);
            if (CollectionUtils.isNotEmpty(detail.getTagList())) {
                String tags = KnowledgeUtil.convertLongListToBase(detail.getTagList());
                logger.info("create tags for base: " + tags);
                base.setTags(tags);
            }
            if (type > 0) {
                base.setType(type);
            } else {
                base.setType(KnowledgeUtil.parserShortType(detail.getColumnType()));
            }
            base.setColumnId(KnowledgeUtil.parserColumnId(detail.getColumnid()));
            base.setCreateUserId(detail.getCid());
            //For reference knowledge may be different with author
            base.setCreateUserName(detail.getCname());
            final long currTime = System.currentTimeMillis();
            final long createTime = StringUtils.isNotBlank(detail.getCreatetime()) ? KnowledgeUtil.parserTimeToLong(detail.getCreatetime()) : currTime;
            final long modifyTime = StringUtils.isNotBlank(detail.getModifytime()) ? KnowledgeUtil.parserTimeToLong(detail.getModifytime()) : createTime;
            final long publicTime = StringUtils.isNotBlank(detail.getSubmitTime()) ? KnowledgeUtil.parserTimeToLong(detail.getSubmitTime()) : createTime;
            base.setCreateDate(createTime);
            base.setModifyDate(modifyTime);
            base.setPublicDate(publicTime);
            base.setIsOld((short) 0);
            base.setUserStar((short) 0);
            base.setStatus((short)detail.getStatus());
            base.setReportStatus((short)detail.getReport_status());
            base.setPrivated(detail.getPrivated());
            //check and truncate some data over database limit.
            knowledgeBaseFaultTolerant(base);
            //knowledge.setPublicDate(System.currentTimeMillis());
            //knowledge.setReportStatus(reportStatus);
        }
        return base;
    }

    public BigData toBigData() {
        if (knowledgeDetail != null) {
            if (this.permission == null) {
                this.permission = defaultPermission(this.knowledgeDetail.getCid(), this.knowledgeDetail.getId());
            }
            return generateBigData(this.knowledgeDetail, this.permission);
        }
        return null;
    }

    public static BigData generateBigData(Knowledge detail, Permission permission) {
        if (detail == null) {
            return null;
        }
        BigData data = new BigData();
        if (permission == null) {
            permission = defaultPermission(detail.getCid(), detail.getId());
        }

        data.setKid(detail.getId());
        data.setCid(detail.getCid());
        data.setCname(detail.getCname());
        data.setTitle(detail.getTitle());
        data.setCpathid(detail.getCpathid());
        data.setPic(detail.getPic());
        data.setPublicFlag(permission.getPublicFlag().shortValue());
        data.setConnectFlag(permission.getConnectFlag().shortValue());
        data.setStatus((short)detail.getStatus());
        data.setTags(detail.getTags());
        data.setColumnid(KnowledgeUtil.parserColumnId(detail.getColumnid()));
        data.setColumnType(KnowledgeUtil.parserColumnId(detail.getColumnType()));
        data.setContent(detail.getContent());
        data.setDesc(detail.getDesc());
        data.setCreatetime(KnowledgeUtil.parserTimeToLong(detail.getCreatetime()));

        return data;
    }

    public Permission defaultPermission()
    {
        if (knowledgeDetail != null) {
            this.setPermission(defaultPermission(knowledgeDetail.getCid(), knowledgeDetail.getId()));
            return this.getPermission();
        }
        return null;
    }

    public Permission defaultPublicPermission()
    {
        if (knowledgeDetail != null) {
            this.setPermission(defaultPublicPermission(knowledgeDetail.getCid(), knowledgeDetail.getId()));
            return this.getPermission();
        }
        return null;
    }

    public static Permission defaultPublicPermission(final long userId,final long resId)
    {
        return defaultPermission(userId, resId, 1);
    }

    public static Permission defaultPermission(final long userId,final long resId)
    {
        return defaultPermission(userId, resId, -1);
    }

    public static Permission defaultPermission(final long userId,final long resId, int defaultFlag)
    {
        Permission permission = new Permission();
        permission.setAppId(KnowledgeConstant.DEFAULT_APP_ID);
        permission.setResId(resId);
        permission.setResOwnerId(userId);
        if (defaultFlag < 0) {
            defaultFlag = userId > 1 ? 0 : 1;
        }
        permission.setConnectFlag(defaultFlag);
        permission.setPublicFlag(defaultFlag);
        permission.setShareFlag(defaultFlag);
        permission.setResType(ResourceType.KNOW.getVal());
        return permission;
    }

    public static short privated(final Permission perm) {
        return privated(perm, true);
    }

    public static short privated(final Permission perm, final boolean owner) {
        int privated = 1; //default is private
        if (perm != null && perm.getPublicFlag() != null && perm.getConnectFlag() != null) {
            if (owner) {
                privated = (perm.getPublicFlag() == 1 && perm.getConnectFlag() == 1) ? 0 : 1;
            } else if (perm.getShareFlag() != null){
                privated = (perm.getPublicFlag() == 1 && perm.getConnectFlag() == 1 && perm.getShareFlag() == 1) ? 0 : 1;
            }
        }
        return (short)privated;
    }

    public static List<KnowledgeBase> convertDetailToBaseList(List<Knowledge> detailList, short columnType, boolean desc)
    {
        if (CollectionUtils.isEmpty(detailList)) {
            return null;
        }

        Map<Long,KnowledgeBase> baseMap = new TreeMap<Long, KnowledgeBase>(desc ? descComparator : null);
        for (Knowledge detail : detailList) {
            if (detail != null) {
                KnowledgeBase base = DataCollect.generateKnowledge(detail, columnType);
                if (base != null) {
                    baseMap.put(base.getCreateDate(), base);
                }
            }
        }

        List<KnowledgeBase> baseList = new ArrayList<KnowledgeBase>(detailList.size());
        for (Map.Entry<Long,KnowledgeBase> keyValue : baseMap.entrySet()) {
            baseList.add(keyValue.getValue());
        }
        return baseList;
    }

    private static Comparator descComparator = new Comparator<Long>(){
        public int compare(Long a,Long b){
            return (int)(b.intValue() - a.longValue());
        }
    };

    public static KnowledgeBase knowledgeBaseFaultTolerant(KnowledgeBase base)
    {
        if (base != null) {
            String title = base.getTitle();
            if (title != null) {
                if (title.length() > maxLen) {
                    title = title.substring(0, maxLen - 1);
                    logger.warn("Title over 255, so truncate it to 255");
                }
                title = HtmlToText.removeFourChar(title);
                if (title == null || title.length() <= 0) {
                    title = "title";
                }
                base.setTitle(title);
            }

            String contentDesc = base.getContentDesc();
            if (contentDesc != null) {
                if (contentDesc.length() > maxLen) {
                    logger.warn("ContentDesc over 255, so truncate it to 255");
                    contentDesc = contentDesc.substring(0, maxLen - 1);
                }
                contentDesc = HtmlToText.removeFourChar(contentDesc);
                base.setContentDesc(contentDesc);
            }

            if (base.getSource() != null && base.getSource().length() > maxLen) {
                logger.warn("source over 255, so truncate it to 255");
                base.setSource(base.getSource().substring(0, maxLen - 1));
            }

            if (base.getCoverPic() != null && base.getCoverPic().length() > maxUrlLen) {
                logger.warn("cover Picture over 512, so set it to null");
                base.setCoverPic(null);
            }

            if (base.getCpath() != null && base.getCpath().length() > maxLen) {
                logger.warn("cpath over 255, so truncate it to 255");
                base.setCpath(base.getCpath().substring(0, maxLen - 1));
            }

            if (base.getTaskId() != null && base.getTaskId().length() > maxLen) {
                logger.warn("taskId over 255, so truncate it to 255");
                base.setTaskId(base.getTaskId().substring(0, maxLen - 1));
            }

            if (base.getCreateUserName() != null && base.getCreateUserName().length() > 50) {
                logger.warn("create user name over 255, so truncate it to 50");
                base.setCreateUserName(base.getCreateUserName().substring(0, 50 - 1));
            }

            if (base.getModifyUserName() != null && base.getModifyUserName().length() > maxLen) {
                logger.warn("modify user name over 255, so truncate it to 255");
                base.setModifyUserName(base.getModifyUserName().substring(0, maxLen - 1));
            }
        }

        return base;
    }

    public static String validatePicUrl(List<String> imgUrl)
    {
        if (CollectionUtils.isEmpty(imgUrl)) {
            return null;
        }

        for (String url : imgUrl) {
            if (StringUtils.isNotBlank(url) && !"null".equals(url)) {
                if (url.indexOf(".jpg") > 0 || url.indexOf(".jpeg") > 0 || url.indexOf(".gif") > 0 || url.indexOf(".png") > 0 ||
                        url.indexOf(".JPG") > 0 || url.indexOf(".JPEG") > 0 || url.indexOf(".GIF") > 0 || url.indexOf(".PNG") > 0 ) {
                    return url;
                }
            }
        }
        return null;
    }

    public static String convertLongListToBase(List<Long> ids)
    {
        return KnowledgeUtil.convertLongListToBase(ids);
    }

    public OrganResourceVO getOrganResourceVO() {
        return organResourceVO;
    }

    public void setOrganResourceVO(OrganResourceVO organResourceVO) {
        this.organResourceVO = organResourceVO;
    }
}
