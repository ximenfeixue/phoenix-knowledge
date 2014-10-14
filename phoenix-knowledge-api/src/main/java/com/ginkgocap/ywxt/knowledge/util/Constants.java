package com.ginkgocap.ywxt.knowledge.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author haiyan
 * 
 */
public class Constants {

	public static long gtnid = 0l;

	public static List<Long> homeColumnIds = new ArrayList<Long>(asList(1, 2,
			3, 4, 6, 7, 10));

	public static String redisOrderColumn = "redisOrderColumn";

	public static String status = "status";

	public static String errormessage = "errormessage";

	public final static String msg = "msg";

	// 1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章
	public enum Type {
		News(1, "com.ginkgocap.ywxt.knowledge.model.KnowledgeNews"), Investment(
				2, "com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment"), Industry(
				3, "com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry"), Case(
				4, "com.ginkgocap.ywxt.knowledge.model.KnowledgeCase"), BookReport(
				5, "com.ginkgocap.ywxt.knowledge.model.bookReport"), Asset(6,
				"com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset"), Macro(7,
				"com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro"), Opinion(
				8, "com.ginkgocap.ywxt.knowledge.model.KnowledgeOpinion"), Example(
				9, "com.ginkgocap.ywxt.knowledge.model.example"), Law(10,
				"com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw"), Article(11,
				"com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle");

		private int v;

		private String obj;

		private Type(int v, String obj) {
			this.v = v;
			this.obj = obj;
		}

		public int v() {
			return v;
		}

		public String obj() {
			return obj;
		}

		@Override
		public String toString() {
			return String.valueOf(v);
		}

	}

	private static Collection<? extends Long> asList(int i, int j, int k,
			int l, int m, int n, int o) {
		List<Long> list = new ArrayList<Long>();
		list.add((long) i);
		list.add((long) j);
		list.add((long) k);
		list.add((long) l);
		list.add((long) m);
		list.add((long) n);
		list.add((long) o);
		return list;
	}

	// 1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章
	public enum KnowledgeType {
		News(1, "资讯"), Investment(2, "投融工具"), Industry(3, "行业"), Case(4, "经典案例"), BookReport(
				5, "图书报告"), Asset(6, "资产管理"), Macro(7, "宏观"), Opinion(8, "观点"), Example(
				9, "判例"), Law(10, "法律法规"), Article(11, "文章");

		private int v;

		private String c;

		private KnowledgeType(int v, String c) {
			this.v = v;
			this.c = c;
		}

		public int v() {
			return v;
		}

		public String c() {
			return c;
		}

		@Override
		public String toString() {
			return String.valueOf(v);
		}

	}

	public enum Status {
		draft(1), waitcheck(2), checking(3), checked(4), uncheck(5), recycle(6), foreverdelete(7);

		private int v;

		private Status(int v) {
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

	public enum ResultType {
		fail(0), success(1);

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
		self(0, "自己"), friends(1, "好友"), jinTN(3, "金桐脑"), none(4, "无关系");

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
		artNotExsit("亲爱的用户你好：你所查看的文章不存在或被删除!"), addCollFail("文章收藏失败!"), addColumnFail("添加栏目失败!"),alreadyCollection(
				"您已经收藏过该文章!"), addCommentFail("评论失败!"), artUserNotExsit(
				"文章作者不存在!"), addReportFail("添加举报失败!"), columnNotFound(
				"未找到知识所属栏目"), addFriendsFail("添加好友失败!"), addFriendsWaiting(
				"您已申请过添加好友,请耐心等待!"), IsFriends("您与该用户已是好友关系!"), UserNotExisitInSession(
				"请确认是否登陆!"), contentIsBlank("评论内容不能为空!"), commentNotExsit(
				"评论不存在!"), delCommentNotPermission("无权删除该评论!"), delCommentFail(
				"删除评论失败!"),notFindColumn("栏目不存在，请刷新页面后重试!"),delColumnNotPermission("无权删除该栏目!"),delFail("删除失败!");

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
		del(0), common(1);
		
		private int v;
		
		private ColumnDelStatus(int v) {
			this.v = v;
		}
		
		public int v() {
			return v;
		}
	}
}
