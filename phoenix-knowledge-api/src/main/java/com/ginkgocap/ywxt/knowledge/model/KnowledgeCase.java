package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

/**
 * 知识javaBean （经典案例）
 * 
 * @author Administrator
 * 
 */
public class KnowledgeCase extends Knowledge {
		// 老知识ID
		private long oid;
		
		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		//价格
		private float price;
				
		
		public long getOid() {
			return oid;
		}

		public void setOid(long oid) {
			this.oid = oid;
		}
}