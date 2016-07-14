package com.ginkgocap.ywxt.knowledge.utils;

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
}
