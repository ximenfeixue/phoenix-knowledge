package com.ginkgocap.ywxt.knowledge.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gintong on 2016/7/6.
 */
public class KnowledgeConstant {

    public static final String USER_DEFAULT_PIC_PATH_FAMALE = "/web/pic/user/default.jpeg";//个人默认头像：女
    public static final String USER_DEFAULT_PIC_PATH_MALE = "/web/pic/people/original/default.jpeg";//个人默认头像：男
    public static final String ORGAN_DEFAULT_PIC_PATH = "/web1/organ/avatar/default.jpeg";//组织、客户默认头像

    /*
    public static enum SourceType {
        SOURCE_GINTONG_BRAIN_ID(0),
        SOURCE_GINTONG_BRAIN(1),
        SOURCE_ALL_PLATFORM(2),
        SOURCE_MY_FRIEND(3),
        SOURCE_MY_SELF(4);

        int value;
        private SourceType(int value)
        {
            this.value = value;
        }
        public int getValue()
        {
            return value;
        }
    };*/
    public static final short PRIVATED = 0;
    public static final short AUDIT_PASS = 4;

    public static  final int SOURCE_GINTONG_BRAIN_ID = 0;
    public static  final int SOURCE_GINTONG_BRAIN = 1;
    public static  final int SOURCE_ALL_PLATFORM = 2;
    public static  final int SOURCE_MY_FRIEND = 3;
    public static  final int SOURCE_MY_SELF = 4;

    private static Collection<? extends Long> asList(int i, int j, int k,
                                                     int l, int m, int n, int o, int p) {
        List<Long> list = new ArrayList<Long>();
        list.add((long) i);
        list.add((long) j);
        list.add((long) k);
        list.add((long) l);
        list.add((long) m);
        list.add((long) n);
        list.add((long) o);
        list.add((long) p);
        return list;
    }

    public enum Status {
        draft(1), waitcheck(2), checking(3), checked(4), uncheck(5), recycle(6), foreverdelete(
                7), forbid(8);

        private int v;

        private Status(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }

    }

    public enum UserType {
        jinTN(0, "金桐脑"), platform(-1, "全平台");

        private int v;
        private String c;

        private UserType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    public enum KnowledgeSource {
        system(0), user(1);

        private int v;

        private KnowledgeSource(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }

    }

    public enum HighLight {
        unlight(0), light(1);

        private int v;

        private HighLight(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum ReportStatus {
        unreport(0), report(1);

        private int v;

        private ReportStatus(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum ReportResolveStatus {
        unresolve(0), resolve(1);

        private int v;

        private ReportResolveStatus(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum ResultType {
        fail(0), success(1), sensitiveWords(2), sameNameError(3);

        private int v;

        private ResultType(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum StaticsType {
        sys(0), user(1);

        private int v;

        private StaticsType(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum Relation {
        // 来源(1：自己，2：好友，3：金桐脑，4：全平台，5：组织)
        self(1, "自己"), friends(2, "好友"), jinTN(3, "金桐脑"), platform(4, "全平台"), org(
                5, "组织"), notFriends(6, "不是好友"), waitAgree(7, "等待确认");

        private int v;
        private String c;

        private Relation(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    public enum ErrorMessage {
        artNotExsit("亲爱的用户你好：你所查看的文章不存在或被删除!"), addKnowledgeFail("添加知识失败!"), addCollFail(
                "文章收藏失败!"), addColumnFail("添加栏目失败!"), alreadyCollection(
                "您已经收藏过该文章!"), addCommentFail("评论失败!"), artUserNotExsit(
                "文章作者不存在!"), addReportFail("添加举报失败!"), columnNotFound(
                "未找到知识所属栏目"), addFriendsFail("添加好友失败!"), addFriendsWaiting(
                "您已申请过添加好友,请耐心等待!"), checkColumnFail("栏目名已存在!"), IsFriends(
                "您与该用户已是好友关系!"), UserNotExisitInSession("请确认是否登陆!"), contentIsBlank(
                "评论内容不能为空!"), commentNotExsit("评论不存在!"), delCommentNotPermission(
                "无权删除该评论!"), delCommentFail("删除评论失败!"), notFindColumn(
                "栏目不存在，请刷新页面后重试!"), delColumnNotPermission("无权删除该栏目!"), delFail(
                "删除失败!"), paramNotValid("用户权限参数不合法!"), updateFail("更新失败!"), paramNotBlank(
                "参数不能为空"), contentTooLong("内容过长"), sensitiveWord("您的文章存在敏感词"), artPermissionNotFound(
                "对不起,您没有查看该文章的权限!"), hasName("名称重复!"), parseError("解析错误!"), userNotLogin(
                "您未登陆,请先登陆!"), addKnowledgeCatalogueIds("添加目录失败!"), addasso(
                "添加关联失败!"), tagExist("标签已存在!");

        private String c;

        private ErrorMessage(String c) {
            this.c = c;
        }

        public String c() {
            return c;
        }
    }

    public enum StaticsValue {
        /* 统计信息权值 */
        commentCount(1), shareCount(1), collCount(1), clickCount(1);

        private int v;

        private StaticsValue(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum SumbitType {
        submit(1, "发布!"), submitandadd(2, "发布并新增!");

        private String c;
        private int v;

        private SumbitType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    public enum CommentStatus {
        common(true), del(false);

        private boolean c;

        private CommentStatus(boolean c) {
            this.c = c;
        }

        public boolean c() {
            return c;
        }
    }

    public enum FriendsType {
        checking(0), agree(1);

        private int v;

        private FriendsType(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum FriendsRelation {
        // (-1=是自己 or 0=不是好友 or 1=好友等待中 or 2=已是好友)
        self(-1), notFriends(0), waiting(1), friends(2);

        private int v;

        private FriendsRelation(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public enum ColumnDelStatus {
        common(0), del(1);

        private int v;

        private ColumnDelStatus(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public static enum Ids {
        EGinTN((short)0), EPlatform((short)-1);

        private short v;

        private Ids(short v) {
            this.v = v;
        }

        public short v() {
            return v;
        }
    }

    public static enum CategoryType {
        // 0 正常目录 1 收藏夹
        common(0), collection(1);

        private int v;

        private CategoryType(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    public static enum KnowledgeCategoryStatus {
        // 状态（0：不生效，1：生效）
        uneffect(0), effect(1);

        private int v;

        private KnowledgeCategoryStatus(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    /*
    public static enum PermissionType {
        dule(1, "dule"), dales(2, "dales"), zhongles(3, "zhongles"), xiaoles(4,
                "xiaoles");
        private int v;

        private String c;

        private PermissionType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }*/

    public static enum KnowledgeConnectType {
        // 关联格式（p:人脉,r:事件,o:组织,k:知识）
        event(1, "r"), people(2, "p"), organization(5, "o"), knowledge(6, "k");
        private int v;

        private String c;

        private KnowledgeConnectType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    /*
    public static Integer getPermissionValue(String c) {
        if (StringUtils.isBlank(c))
            return null;
        PermissionType[] type = PermissionType.values();
        for (PermissionType t : type) {
            if (StringUtils.equalsIgnoreCase(t.c, c)) {
                return t.v;
            }
        }
        return null;
    }*/

    public enum MenuType {
        collect(1, "收藏"), main(0, "主目录");
        int v;
        String obj;

        private MenuType(int v, String obj) {
            this.v = v;
            this.obj = obj;
        }

        public int getV() {
            return v;
        }

        public String getObj() {
            return obj;
        }

        @Override
        public String toString() {
            return v + "";
        }
    }

    // 0-资讯，1-投融工具，2-行业，3-经典案例 4-资产管理，5-宏观，6-观点，7-判例，8-法律法规，9-文章
    public enum TagNum {
        News(0), Investment(1), Industry(2), Case(3), Asset(4), NewMaterials(5), Opinion(
                6), Example(7), Law(8), Article(9);

        private int v;

        private TagNum(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }

        @Override
        public String toString() {
            return String.valueOf(v);
        }

    }

    // 弹出层类型
    public enum cssType {
        people(1), org(2);

        private int v;

        private cssType(int v) {
            this.v = v;
        }

        public int v() {
            return v;
        }
    }

    // 通知类型
    public enum noticeType {
        column(1, "栏目改变通知"), knowledge(2, "知识改变通知"), cases(3, "经典案例通知"), shareToJinTN(
                4, "分享到金桐脑"), dynamic(5, "动态");

        private int v;
        private String c;

        private noticeType(int v, String c) {
            this.v = v;
            this.c = c;
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }
    // 知识详情查看来源
    public enum knowledgeDetailSource {
        common((short)0, "正常"), app((short)1, "app");

        private short v;
        private String c;

        private knowledgeDetailSource(short v, String c) {
            this.v = v;
            this.c = c;
        }

        public short v() {
            return v;
        }

        public String c() {
            return c;
        }
    }

    public static Integer MATERIAL_REQUIREMENT = 1;

    public static Integer MATERIAL_PEOPLE = 2;

    public static Integer MATERIAL_COMMON_USER = 3;

    public static Integer MATERIAL_ORGANIZATION_USER = 4;

    public static Integer MATERIAL_CUSTOMER = 5;

    public static Integer MATERIAL_KNOWLEDGE = 6;
}
