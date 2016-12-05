package com.ginkgocap.ywxt.knowledge.model;

import java.util.ArrayList;
import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.mobile.*;
import com.ginkgocap.ywxt.knowledge.utils.CommonUtil;
import com.ginkgocap.ywxt.knowledge.utils.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gintong on 2016/7/11.
 */
public class Knowledge2 {
    private static final long serialVersionUID = 398494308L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private long id;
    // "类型:1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章"
    private int type;
    private int columnId;
    private String title;// 标题
    private long uid;// 作者
    private String uname;// 作者名称
    private long cid;// 创建人id
    private String cname;// 创建人名称
    private String source;// 来源
    private String s_addr;// 来源地址
    private String cpathid;// 栏目路徑
    private String pic;// 封面地址
    private String desc;// 描述
    private String content;// 原内容
    private String hcontent;
    private short essence;// 是否加精
    private long createtime;// 创建时间
    private long modifytime;// 最后修改时间
    private int status;// 状态（1为草稿，2：待审核 3：审核中 4：审核通过 5：未通过 6：回收站)
    private int reportStatus;// 举报状态(1:举报，0：未举报)
    private List<Long> tags;// 标签
    private int ish;// 高亮状态
    private String knowledgeUrl;// 知识Url
    private KnowledgeStatics knowledgeStatics;
    //private Column column;
    private List<UserCategory> listUserCategory;
    private boolean isConnectionAble;// 当前用户对接权限
    private boolean isShareAble;// 当前用户分享权限
    private boolean isZhongLeForMe;// 当前用户分享中乐权限
    private boolean isSaved;// 当前用户保存权限
    private String taskId;// 知识附件索引
    private List<JTFile> listJtFile;
    private boolean collected;
    private String[] listImageUrl;

    private int authorType;

    // 法律法规-发布日期
    private String submitTime;

    // 法律法规-执行日期
    private String performTime;

    // 法律法规-发文单位
    private String postUnit;

    // 法律法规-文号
    private String titanic;

    private String synonyms; // 同义词

    private String fileType;

    // 转换状态
    private int tranStatus;

    private String selectedIds;

    // WEB端和APP分开
    private boolean web;

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public boolean getIsZhongLeForMe() {
        return isZhongLeForMe;
    }

    public void setIsZhongLeForMe(boolean isZhongLeForMe) {
        this.isZhongLeForMe = isZhongLeForMe;
    }

    public boolean getWeb() {
        return web;
    }

    public void setWeb(boolean web) {
        this.web = web;
    }

    public int getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(int tranStatus) {
        this.tranStatus = tranStatus;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getPostUnit() {
        return postUnit;
    }

    public void setPostUnit(String postUnit) {
        this.postUnit = postUnit;
    }

    public String getTitanic() {
        return titanic;
    }

    public void setTitanic(String titanic) {
        this.titanic = titanic;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getPerformTime() {
        return performTime;
    }

    public void setPerformTime(String performTime) {
        this.performTime = performTime;
    }

    public int getAuthorType() {
        return authorType;
    }

    public void setAuthorType(int authorType) {
        this.authorType = authorType;
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public String getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(String selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Knowledge2() {

    }


    /**
     * @desc 从knowledge对象生成当前对象
     * @param knowledge
     * @return
     */
    public static Knowledge2 createFromKnowledge(Knowledge2 knowledge/*,JTFileService jtFileService*/) {
        Knowledge2 self = new Knowledge2();

        self.id = knowledge.getId();
        // self.type = knowledge.getType();
        self.title = knowledge.getTitle();
        self.uid = knowledge.getUid();
        self.uname = knowledge.getUname();
        self.cid = knowledge.getCid();
        self.cname = knowledge.getCname();
        self.source = knowledge.getSource();
        self.s_addr = knowledge.getS_addr();
        self.cpathid = knowledge.getCpathid();
        self.pic = knowledge.getPic();
        self.desc = knowledge.getDesc();
        self.content = knowledge.getContent();
        self.submitTime = knowledge.getSubmitTime();
        self.performTime = knowledge.getPerformTime();
        self.postUnit = knowledge.getPostUnit();
        self.titanic = knowledge.getTitanic();
        self.synonyms = knowledge.getSynonyms();
        self.fileType = knowledge.getFileType();
        self.tranStatus = knowledge.getTranStatus();
        self.taskId = knowledge.getTaskId();
        // self.content =
        // StringEscapeUtils.unescapeHtml4(self.content);//出库的时候转换特殊字符
        // self.content = self.string2Json(self.content);
        self.hcontent = knowledge.getHcontent();
        self.essence = knowledge.getEssence();
        self.createtime = knowledge.getCreateTime(); //CommonDateUtil.dateString2Util(
                //knowledge.getCreatetime(), "yyyy-MM-dd HH:mm"); // 张震加入，为了能使服务器项目能运行而修改
        // ↓
        // self.createtime = knowledge.getCreatetime(); 原写法 2014-11-13 ↑
        // self.modifytime = knowledge.getModifytime(); 注释内容 2014 -- 13
        self.status = knowledge.getStatus();
        self.reportStatus = knowledge.getReportStatus();

        // 标签数组
        self.tags = new ArrayList<Long>();
        List<Long> tags = knowledge.getTags();
        self.tags.addAll(tags);

        self.ish = knowledge.getIsh();
        //self.
        /*
        self.column = new Column();
        try {
            String strColumnID = knowledge.getColumnid();
            if (!Utils.isNullOrEmpty(strColumnID)) {
                self.column.setId(Long.parseLong(knowledge.getColumnid()));
            }
        } catch (Exception e) {

        }*/

        // 生态关联
        /*
        try {
            Knowledge2.parseAsso(self, knowledge.getAsso());
        } catch (Exception e) {

        }

        // 权限管理器待完善,将平台的son转化为java对象数组
        Knowledge2.parseSelectedIds(self, knowledge.getSelectedIds());

        // 读取附件
        self.listJtFile = jtFileService.getJTFileByTaskId(self.getTaskId());
        */

        return self;
    }

    /**
     * JSON字符串特殊字符处理，比如：“\A1;1300”
     *
     * @param s
     * @return String
     */
    public String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                // case'\\': //如果不处理单引号，可以释放此段代码，若结合下面的方法处理单引号就必须注释掉该段代码
                // sb.append("\\\\");
                // break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b': // 退格
                    sb.append("\\b");
                    break;
                case '\f': // 走纸换页
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");// 换行
                    break;
                case '\r': // 回车
                    sb.append("\\r");
                    break;
                case '\t': // 横向跳格
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return "<style>";
        // return sb.toString();
        // return
        // "<html><head><meta charset='utf-8' /><body><div><p></p><img src='http://192.168.101.131:880//mobile/download?id=141127193667238'/><img src='http://192.168.101.131:880//mobile/download?id=141127193670243'/><img src='http://192.168.101.131:880//mobile/download?id=141127193674798'/></div></body></html>";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getS_addr() {
        return s_addr;
    }

    public void setS_addr(String s_addr) {
        this.s_addr = s_addr;
    }

    public String getCpathid() {
        return cpathid;
    }

    public void setCpathid(String cpathid) {
        this.cpathid = cpathid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHcontent() {
        return hcontent;
    }

    public void setHcontent(String hcontent) {
        this.hcontent = hcontent;
    }

    public short getEssence() {
        return essence;
    }

    public void setEssence(short essence) {
        this.essence = essence;
    }

    public long getCreateTime() {
        return createtime;
    }

    public void setCreateTime(long createTime) {
        this.createtime = createTime;
    }

    public long getModifyTime() {
        return modifytime;
    }

    public void setModifyTime(long time) {
        this.modifytime = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(int report_status) {
        this.reportStatus = report_status;
    }

    public int getIsh() {
        return ish;
    }

    public void setIsh(int ish) {
        this.ish = ish;
    }

    public String getKnowledgeUrl() {
        return knowledgeUrl;
    }

    public void setKnowledgeUrl(String knowledgeUrl) {
        this.knowledgeUrl = knowledgeUrl;
    }

    public KnowledgeStatics getKnowledgeStatics() {
        return knowledgeStatics;
    }

    public void setKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
        this.knowledgeStatics = knowledgeStatics;
    }

    /*
    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }*/

    public List<UserCategory> getListUserCategory() {
        return listUserCategory;
    }

    public void setListUserCategory(List<UserCategory> listUserCategory) {
        this.listUserCategory = listUserCategory;
    }

    public boolean getIsConnectionAble() {
        return isConnectionAble;
    }

    public void setIsConnectionAble(boolean isConnectionAble) {
        this.isConnectionAble = isConnectionAble;
    }

    public boolean getIsShareAble() {
        return isShareAble;
    }

    public void setIsShareAble(boolean isShareAble) {
        this.isShareAble = isShareAble;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<JTFile> getListJtFile() {
        return listJtFile;
    }

    public void setListJtFile(List<JTFile> listJtFile) {
        this.listJtFile = listJtFile;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> listTag) {
        this.tags = listTag;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    private static String getString(String str) {
        return str == null ? "" : str.equals("null") ? "" : str;
    }

    public String[] getListImageUrl() {
        return listImageUrl;
    }

    public void setListImageUrl(String[] listImageUrl) {
        this.listImageUrl = listImageUrl;
    }

}
