package com.ginkgocap.ywxt.knowledge.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gintong on 2016/7/6.
 */
public class KnowledgeConstant {

    public static final long DEFAULT_APP_ID = 1L;
    public static final short DEFAULT_SOURCE_TYPE = 8;

    public static final String USER_DEFAULT_PIC_PATH_FAMALE = "/web/pic/user/default.jpeg";//个人默认头像：女
    public static final String USER_DEFAULT_PIC_PATH_MALE = "/web/pic/people/original/default.jpeg";//个人默认头像：男
    public static final String ORGAN_DEFAULT_PIC_PATH = "/web1/organ/avatar/default.jpeg";//组织、客户默认头像
    public static final String GIN_USER_KEY = "ginUserkey";
    public static final String GIN_USER_VALUE = "gintong_1998_9167004345845581253L";

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
}
