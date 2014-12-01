package com.ginkgocap.ywxt.knowledge.entity;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeStaticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public KnowledgeStaticsExample() {
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

        public Criteria andCommentcountIsNull() {
            addCriterion("commentCount is null");
            return (Criteria) this;
        }

        public Criteria andCommentcountIsNotNull() {
            addCriterion("commentCount is not null");
            return (Criteria) this;
        }

        public Criteria andCommentcountEqualTo(Long value) {
            addCriterion("commentCount =", value, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountNotEqualTo(Long value) {
            addCriterion("commentCount <>", value, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountGreaterThan(Long value) {
            addCriterion("commentCount >", value, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountGreaterThanOrEqualTo(Long value) {
            addCriterion("commentCount >=", value, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountLessThan(Long value) {
            addCriterion("commentCount <", value, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountLessThanOrEqualTo(Long value) {
            addCriterion("commentCount <=", value, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountIn(List<Long> values) {
            addCriterion("commentCount in", values, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountNotIn(List<Long> values) {
            addCriterion("commentCount not in", values, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountBetween(Long value1, Long value2) {
            addCriterion("commentCount between", value1, value2, "commentcount");
            return (Criteria) this;
        }

        public Criteria andCommentcountNotBetween(Long value1, Long value2) {
            addCriterion("commentCount not between", value1, value2, "commentcount");
            return (Criteria) this;
        }

        public Criteria andSharecountIsNull() {
            addCriterion("shareCount is null");
            return (Criteria) this;
        }

        public Criteria andSharecountIsNotNull() {
            addCriterion("shareCount is not null");
            return (Criteria) this;
        }

        public Criteria andSharecountEqualTo(Long value) {
            addCriterion("shareCount =", value, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountNotEqualTo(Long value) {
            addCriterion("shareCount <>", value, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountGreaterThan(Long value) {
            addCriterion("shareCount >", value, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountGreaterThanOrEqualTo(Long value) {
            addCriterion("shareCount >=", value, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountLessThan(Long value) {
            addCriterion("shareCount <", value, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountLessThanOrEqualTo(Long value) {
            addCriterion("shareCount <=", value, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountIn(List<Long> values) {
            addCriterion("shareCount in", values, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountNotIn(List<Long> values) {
            addCriterion("shareCount not in", values, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountBetween(Long value1, Long value2) {
            addCriterion("shareCount between", value1, value2, "sharecount");
            return (Criteria) this;
        }

        public Criteria andSharecountNotBetween(Long value1, Long value2) {
            addCriterion("shareCount not between", value1, value2, "sharecount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountIsNull() {
            addCriterion("collectionCount is null");
            return (Criteria) this;
        }

        public Criteria andCollectioncountIsNotNull() {
            addCriterion("collectionCount is not null");
            return (Criteria) this;
        }

        public Criteria andCollectioncountEqualTo(Long value) {
            addCriterion("collectionCount =", value, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountNotEqualTo(Long value) {
            addCriterion("collectionCount <>", value, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountGreaterThan(Long value) {
            addCriterion("collectionCount >", value, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountGreaterThanOrEqualTo(Long value) {
            addCriterion("collectionCount >=", value, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountLessThan(Long value) {
            addCriterion("collectionCount <", value, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountLessThanOrEqualTo(Long value) {
            addCriterion("collectionCount <=", value, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountIn(List<Long> values) {
            addCriterion("collectionCount in", values, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountNotIn(List<Long> values) {
            addCriterion("collectionCount not in", values, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountBetween(Long value1, Long value2) {
            addCriterion("collectionCount between", value1, value2, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andCollectioncountNotBetween(Long value1, Long value2) {
            addCriterion("collectionCount not between", value1, value2, "collectioncount");
            return (Criteria) this;
        }

        public Criteria andClickcountIsNull() {
            addCriterion("clickCount is null");
            return (Criteria) this;
        }

        public Criteria andClickcountIsNotNull() {
            addCriterion("clickCount is not null");
            return (Criteria) this;
        }

        public Criteria andClickcountEqualTo(Long value) {
            addCriterion("clickCount =", value, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountNotEqualTo(Long value) {
            addCriterion("clickCount <>", value, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountGreaterThan(Long value) {
            addCriterion("clickCount >", value, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountGreaterThanOrEqualTo(Long value) {
            addCriterion("clickCount >=", value, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountLessThan(Long value) {
            addCriterion("clickCount <", value, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountLessThanOrEqualTo(Long value) {
            addCriterion("clickCount <=", value, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountIn(List<Long> values) {
            addCriterion("clickCount in", values, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountNotIn(List<Long> values) {
            addCriterion("clickCount not in", values, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountBetween(Long value1, Long value2) {
            addCriterion("clickCount between", value1, value2, "clickcount");
            return (Criteria) this;
        }

        public Criteria andClickcountNotBetween(Long value1, Long value2) {
            addCriterion("clickCount not between", value1, value2, "clickcount");
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

        public Criteria andSourceEqualTo(Short value) {
            addCriterion("source =", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotEqualTo(Short value) {
            addCriterion("source <>", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThan(Short value) {
            addCriterion("source >", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThanOrEqualTo(Short value) {
            addCriterion("source >=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThan(Short value) {
            addCriterion("source <", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThanOrEqualTo(Short value) {
            addCriterion("source <=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceIn(List<Short> values) {
            addCriterion("source in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotIn(List<Short> values) {
            addCriterion("source not in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceBetween(Short value1, Short value2) {
            addCriterion("source between", value1, value2, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotBetween(Short value1, Short value2) {
            addCriterion("source not between", value1, value2, "source");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Short value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Short value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Short value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Short value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Short value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Short> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Short> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Short value1, Short value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Short value1, Short value2) {
            addCriterion("type not between", value1, value2, "type");
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