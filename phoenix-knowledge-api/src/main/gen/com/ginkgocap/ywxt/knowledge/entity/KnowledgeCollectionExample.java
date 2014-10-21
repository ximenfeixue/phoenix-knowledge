package com.ginkgocap.ywxt.knowledge.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KnowledgeCollectionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public KnowledgeCollectionExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdIsNull() {
            addCriterion("knowledge_id is null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdIsNotNull() {
            addCriterion("knowledge_id is not null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdEqualTo(Long value) {
            addCriterion("knowledge_id =", value, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdNotEqualTo(Long value) {
            addCriterion("knowledge_id <>", value, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdGreaterThan(Long value) {
            addCriterion("knowledge_id >", value, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("knowledge_id >=", value, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdLessThan(Long value) {
            addCriterion("knowledge_id <", value, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdLessThanOrEqualTo(Long value) {
            addCriterion("knowledge_id <=", value, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdIn(List<Long> values) {
            addCriterion("knowledge_id in", values, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdNotIn(List<Long> values) {
            addCriterion("knowledge_id not in", values, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdBetween(Long value1, Long value2) {
            addCriterion("knowledge_id between", value1, value2, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andKnowledgeIdNotBetween(Long value1, Long value2) {
            addCriterion("knowledge_id not between", value1, value2, "knowledgeId");
            return (Criteria) this;
        }

        public Criteria andColumnIdIsNull() {
            addCriterion("column_id is null");
            return (Criteria) this;
        }

        public Criteria andColumnIdIsNotNull() {
            addCriterion("column_id is not null");
            return (Criteria) this;
        }

        public Criteria andColumnIdEqualTo(Long value) {
            addCriterion("column_id =", value, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdNotEqualTo(Long value) {
            addCriterion("column_id <>", value, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdGreaterThan(Long value) {
            addCriterion("column_id >", value, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdGreaterThanOrEqualTo(Long value) {
            addCriterion("column_id >=", value, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdLessThan(Long value) {
            addCriterion("column_id <", value, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdLessThanOrEqualTo(Long value) {
            addCriterion("column_id <=", value, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdIn(List<Long> values) {
            addCriterion("column_id in", values, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdNotIn(List<Long> values) {
            addCriterion("column_id not in", values, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdBetween(Long value1, Long value2) {
            addCriterion("column_id between", value1, value2, "columnId");
            return (Criteria) this;
        }

        public Criteria andColumnIdNotBetween(Long value1, Long value2) {
            addCriterion("column_id not between", value1, value2, "columnId");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNull() {
            addCriterion("timestamp is null");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNotNull() {
            addCriterion("timestamp is not null");
            return (Criteria) this;
        }

        public Criteria andTimestampEqualTo(Date value) {
            addCriterion("timestamp =", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotEqualTo(Date value) {
            addCriterion("timestamp <>", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThan(Date value) {
            addCriterion("timestamp >", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThanOrEqualTo(Date value) {
            addCriterion("timestamp >=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThan(Date value) {
            addCriterion("timestamp <", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThanOrEqualTo(Date value) {
            addCriterion("timestamp <=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampIn(List<Date> values) {
            addCriterion("timestamp in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotIn(List<Date> values) {
            addCriterion("timestamp not in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampBetween(Date value1, Date value2) {
            addCriterion("timestamp between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotBetween(Date value1, Date value2) {
            addCriterion("timestamp not between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeIsNull() {
            addCriterion("knowledgeType is null");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeIsNotNull() {
            addCriterion("knowledgeType is not null");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeEqualTo(String value) {
            addCriterion("knowledgeType =", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeNotEqualTo(String value) {
            addCriterion("knowledgeType <>", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeGreaterThan(String value) {
            addCriterion("knowledgeType >", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeGreaterThanOrEqualTo(String value) {
            addCriterion("knowledgeType >=", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeLessThan(String value) {
            addCriterion("knowledgeType <", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeLessThanOrEqualTo(String value) {
            addCriterion("knowledgeType <=", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeLike(String value) {
            addCriterion("knowledgeType like", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeNotLike(String value) {
            addCriterion("knowledgeType not like", value, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeIn(List<String> values) {
            addCriterion("knowledgeType in", values, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeNotIn(List<String> values) {
            addCriterion("knowledgeType not in", values, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeBetween(String value1, String value2) {
            addCriterion("knowledgeType between", value1, value2, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeNotBetween(String value1, String value2) {
            addCriterion("knowledgeType not between", value1, value2, "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andSourceIsNull() {
            addCriterion("source is null");
            return (Criteria) this;
        }

        public Criteria andSourceIsNotNull() {
            addCriterion("source is not null");
            return (Criteria) this;
        }

        public Criteria andSourceEqualTo(String value) {
            addCriterion("source =", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotEqualTo(String value) {
            addCriterion("source <>", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThan(String value) {
            addCriterion("source >", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThanOrEqualTo(String value) {
            addCriterion("source >=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThan(String value) {
            addCriterion("source <", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThanOrEqualTo(String value) {
            addCriterion("source <=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLike(String value) {
            addCriterion("source like", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotLike(String value) {
            addCriterion("source not like", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceIn(List<String> values) {
            addCriterion("source in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotIn(List<String> values) {
            addCriterion("source not in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceBetween(String value1, String value2) {
            addCriterion("source between", value1, value2, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotBetween(String value1, String value2) {
            addCriterion("source not between", value1, value2, "source");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIsNull() {
            addCriterion("category_id is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIsNotNull() {
            addCriterion("category_id is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdEqualTo(Long value) {
            addCriterion("category_id =", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotEqualTo(Long value) {
            addCriterion("category_id <>", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThan(Long value) {
            addCriterion("category_id >", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThanOrEqualTo(Long value) {
            addCriterion("category_id >=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThan(Long value) {
            addCriterion("category_id <", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThanOrEqualTo(Long value) {
            addCriterion("category_id <=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIn(List<Long> values) {
            addCriterion("category_id in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotIn(List<Long> values) {
            addCriterion("category_id not in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdBetween(Long value1, Long value2) {
            addCriterion("category_id between", value1, value2, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotBetween(Long value1, Long value2) {
            addCriterion("category_id not between", value1, value2, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsIsNull() {
            addCriterion("collection_tags is null");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsIsNotNull() {
            addCriterion("collection_tags is not null");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsEqualTo(String value) {
            addCriterion("collection_tags =", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsNotEqualTo(String value) {
            addCriterion("collection_tags <>", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsGreaterThan(String value) {
            addCriterion("collection_tags >", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsGreaterThanOrEqualTo(String value) {
            addCriterion("collection_tags >=", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsLessThan(String value) {
            addCriterion("collection_tags <", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsLessThanOrEqualTo(String value) {
            addCriterion("collection_tags <=", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsLike(String value) {
            addCriterion("collection_tags like", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsNotLike(String value) {
            addCriterion("collection_tags not like", value, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsIn(List<String> values) {
            addCriterion("collection_tags in", values, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsNotIn(List<String> values) {
            addCriterion("collection_tags not in", values, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsBetween(String value1, String value2) {
            addCriterion("collection_tags between", value1, value2, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsNotBetween(String value1, String value2) {
            addCriterion("collection_tags not between", value1, value2, "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentIsNull() {
            addCriterion("collection_comment is null");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentIsNotNull() {
            addCriterion("collection_comment is not null");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentEqualTo(String value) {
            addCriterion("collection_comment =", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentNotEqualTo(String value) {
            addCriterion("collection_comment <>", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentGreaterThan(String value) {
            addCriterion("collection_comment >", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentGreaterThanOrEqualTo(String value) {
            addCriterion("collection_comment >=", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentLessThan(String value) {
            addCriterion("collection_comment <", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentLessThanOrEqualTo(String value) {
            addCriterion("collection_comment <=", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentLike(String value) {
            addCriterion("collection_comment like", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentNotLike(String value) {
            addCriterion("collection_comment not like", value, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentIn(List<String> values) {
            addCriterion("collection_comment in", values, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentNotIn(List<String> values) {
            addCriterion("collection_comment not in", values, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentBetween(String value1, String value2) {
            addCriterion("collection_comment between", value1, value2, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentNotBetween(String value1, String value2) {
            addCriterion("collection_comment not between", value1, value2, "collectionComment");
            return (Criteria) this;
        }

        public Criteria andKnowledgetypeLikeInsensitive(String value) {
            addCriterion("upper(knowledgeType) like", value.toUpperCase(), "knowledgetype");
            return (Criteria) this;
        }

        public Criteria andSourceLikeInsensitive(String value) {
            addCriterion("upper(source) like", value.toUpperCase(), "source");
            return (Criteria) this;
        }

        public Criteria andCollectionTagsLikeInsensitive(String value) {
            addCriterion("upper(collection_tags) like", value.toUpperCase(), "collectionTags");
            return (Criteria) this;
        }

        public Criteria andCollectionCommentLikeInsensitive(String value) {
            addCriterion("upper(collection_comment) like", value.toUpperCase(), "collectionComment");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}