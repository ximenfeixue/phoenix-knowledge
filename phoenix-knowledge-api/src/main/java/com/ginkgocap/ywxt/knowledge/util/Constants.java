package com.ginkgocap.ywxt.knowledge.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author haiyan
 * 
 */
public class Constants {

	public static long gtnid = 0l;
	
	public static String redisOrderColumn = "redisOrderColumn";
	
	public static String status = "status";

	public static String errormessage = "errormessage";

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

	public enum Status {
		draft(1), waitcheck(2), checking(3), checked(4), uncheck(5), recycle(6);

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
		artNotExsit("文章不存在或已被删除!"), addCollFail("文章收藏失败!");

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
}
