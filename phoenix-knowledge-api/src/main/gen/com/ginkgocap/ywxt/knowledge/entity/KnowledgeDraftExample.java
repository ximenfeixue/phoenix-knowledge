package com.ginkgocap.ywxt.knowledge.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KnowledgeDraftExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public KnowledgeDraftExample() {
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

        public Criteria andDraftnameIsNull() {
            addCriterion("draftname is null");
            return (Criteria) this;
        }

        public Criteria andDraftnameIsNotNull() {
            addCriterion("draftname is not null");
            return (Criteria) this;
        }

        public Criteria andDraftnameEqualTo(String value) {
            addCriterion("draftname =", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameNotEqualTo(String value) {
            addCriterion("draftname <>", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameGreaterThan(String value) {
            addCriterion("draftname >", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameGreaterThanOrEqualTo(String value) {
            addCriterion("draftname >=", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameLessThan(String value) {
            addCriterion("draftname <", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameLessThanOrEqualTo(String value) {
            addCriterion("draftname <=", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameLike(String value) {
            addCriterion("draftname like", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameNotLike(String value) {
            addCriterion("draftname not like", value, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameIn(List<String> values) {
            addCriterion("draftname in", values, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameNotIn(List<String> values) {
            addCriterion("draftname not in", values, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameBetween(String value1, String value2) {
            addCriterion("draftname between", value1, value2, "draftname");
            return (Criteria) this;
        }

        public Criteria andDraftnameNotBetween(String value1, String value2) {
            addCriterion("draftname not between", value1, value2, "draftname");
            return (Criteria) this;
        }

        public Criteria andDrafttypeIsNull() {
            addCriterion("drafttype is null");
            return (Criteria) this;
        }

        public Criteria andDrafttypeIsNotNull() {
            addCriterion("drafttype is not null");
            return (Criteria) this;
        }

        public Criteria andDrafttypeEqualTo(String value) {
            addCriterion("drafttype =", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeNotEqualTo(String value) {
            addCriterion("drafttype <>", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeGreaterThan(String value) {
            addCriterion("drafttype >", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeGreaterThanOrEqualTo(String value) {
            addCriterion("drafttype >=", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeLessThan(String value) {
            addCriterion("drafttype <", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeLessThanOrEqualTo(String value) {
            addCriterion("drafttype <=", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeLike(String value) {
            addCriterion("drafttype like", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeNotLike(String value) {
            addCriterion("drafttype not like", value, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeIn(List<String> values) {
            addCriterion("drafttype in", values, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeNotIn(List<String> values) {
            addCriterion("drafttype not in", values, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeBetween(String value1, String value2) {
            addCriterion("drafttype between", value1, value2, "drafttype");
            return (Criteria) this;
        }

        public Criteria andDrafttypeNotBetween(String value1, String value2) {
            addCriterion("drafttype not between", value1, value2, "drafttype");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("userid is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userid is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Long value) {
            addCriterion("userid =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Long value) {
            addCriterion("userid <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Long value) {
            addCriterion("userid >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Long value) {
            addCriterion("userid >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Long value) {
            addCriterion("userid <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Long value) {
            addCriterion("userid <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Long> values) {
            addCriterion("userid in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Long> values) {
            addCriterion("userid not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Long value1, Long value2) {
            addCriterion("userid between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Long value1, Long value2) {
            addCriterion("userid not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andDraftnameLikeInsensitive(String value) {
            addCriterion("upper(draftname) like", value.toUpperCase(), "draftname");
            return (Criteria) this;
        }

        public Criteria andDrafttypeLikeInsensitive(String value) {
            addCriterion("upper(drafttype) like", value.toUpperCase(), "drafttype");
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