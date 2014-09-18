package com.ginkgocap.ywxt.knowledge.util;

public class Constants {

	public static enum KnowledgeType {
		NEWS(1, "资讯"), Investment(2, "投融工具"), HELP(3, "行业"), Case(4, "经典案例"), ACTIVITY(
				5, "图书报告"), Asset(6, "资产管理 "), Macro(7, "宏观"), Opinion(8, "观点"), Example(
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
	}

}
