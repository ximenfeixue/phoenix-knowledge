package com.ginkgocap.ywxt.knowledge.entity;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeCatalogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public KnowledgeCatalogExample() {
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

        public Criteria andKnowledgeidIsNull() {
            addCriterion("knowledgeid is null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidIsNotNull() {
            addCriterion("knowledgeid is not null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidEqualTo(Long value) {
            addCriterion("knowledgeid =", value, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidNotEqualTo(Long value) {
            addCriterion("knowledgeid <>", value, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidGreaterThan(Long value) {
            addCriterion("knowledgeid >", value, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidGreaterThanOrEqualTo(Long value) {
            addCriterion("knowledgeid >=", value, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidLessThan(Long value) {
            addCriterion("knowledgeid <", value, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidLessThanOrEqualTo(Long value) {
            addCriterion("knowledgeid <=", value, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidIn(List<Long> values) {
            addCriterion("knowledgeid in", values, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidNotIn(List<Long> values) {
            addCriterion("knowledgeid not in", values, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidBetween(Long value1, Long value2) {
            addCriterion("knowledgeid between", value1, value2, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andKnowledgeidNotBetween(Long value1, Long value2) {
            addCriterion("knowledgeid not between", value1, value2, "knowledgeid");
            return (Criteria) this;
        }

        public Criteria andCatalogNameIsNull() {
            addCriterion("catalog_name is null");
            return (Criteria) this;
        }

        public Criteria andCatalogNameIsNotNull() {
            addCriterion("catalog_name is not null");
            return (Criteria) this;
        }

        public Criteria andCatalogNameEqualTo(String value) {
            addCriterion("catalog_name =", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameNotEqualTo(String value) {
            addCriterion("catalog_name <>", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameGreaterThan(String value) {
            addCriterion("catalog_name >", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameGreaterThanOrEqualTo(String value) {
            addCriterion("catalog_name >=", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameLessThan(String value) {
            addCriterion("catalog_name <", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameLessThanOrEqualTo(String value) {
            addCriterion("catalog_name <=", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameLike(String value) {
            addCriterion("catalog_name like", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameNotLike(String value) {
            addCriterion("catalog_name not like", value, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameIn(List<String> values) {
            addCriterion("catalog_name in", values, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameNotIn(List<String> values) {
            addCriterion("catalog_name not in", values, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameBetween(String value1, String value2) {
            addCriterion("catalog_name between", value1, value2, "catalogName");
            return (Criteria) this;
        }

        public Criteria andCatalogNameNotBetween(String value1, String value2) {
            addCriterion("catalog_name not between", value1, value2, "catalogName");
            return (Criteria) this;
        }

        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(Long value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(Long value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(Long value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(Long value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(Long value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(Long value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<Long> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<Long> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(Long value1, Long value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(Long value1, Long value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(Integer value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(Integer value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(Integer value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(Integer value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(Integer value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<Integer> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<Integer> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(Integer value1, Integer value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeIsNull() {
            addCriterion("knowledge_type is null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeIsNotNull() {
            addCriterion("knowledge_type is not null");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeEqualTo(Integer value) {
            addCriterion("knowledge_type =", value, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeNotEqualTo(Integer value) {
            addCriterion("knowledge_type <>", value, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeGreaterThan(Integer value) {
            addCriterion("knowledge_type >", value, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("knowledge_type >=", value, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeLessThan(Integer value) {
            addCriterion("knowledge_type <", value, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeLessThanOrEqualTo(Integer value) {
            addCriterion("knowledge_type <=", value, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeIn(List<Integer> values) {
            addCriterion("knowledge_type in", values, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeNotIn(List<Integer> values) {
            addCriterion("knowledge_type not in", values, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeBetween(Integer value1, Integer value2) {
            addCriterion("knowledge_type between", value1, value2, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andKnowledgeTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("knowledge_type not between", value1, value2, "knowledgeType");
            return (Criteria) this;
        }

        public Criteria andLevelPathIsNull() {
            addCriterion("level_path is null");
            return (Criteria) this;
        }

        public Criteria andLevelPathIsNotNull() {
            addCriterion("level_path is not null");
            return (Criteria) this;
        }

        public Criteria andLevelPathEqualTo(String value) {
            addCriterion("level_path =", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathNotEqualTo(String value) {
            addCriterion("level_path <>", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathGreaterThan(String value) {
            addCriterion("level_path >", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathGreaterThanOrEqualTo(String value) {
            addCriterion("level_path >=", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathLessThan(String value) {
            addCriterion("level_path <", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathLessThanOrEqualTo(String value) {
            addCriterion("level_path <=", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathLike(String value) {
            addCriterion("level_path like", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathNotLike(String value) {
            addCriterion("level_path not like", value, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathIn(List<String> values) {
            addCriterion("level_path in", values, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathNotIn(List<String> values) {
            addCriterion("level_path not in", values, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathBetween(String value1, String value2) {
            addCriterion("level_path between", value1, value2, "levelPath");
            return (Criteria) this;
        }

        public Criteria andLevelPathNotBetween(String value1, String value2) {
            addCriterion("level_path not between", value1, value2, "levelPath");
            return (Criteria) this;
        }

        public Criteria andCatalogNameLikeInsensitive(String value) {
            addCriterion("upper(catalog_name) like", value.toUpperCase(), "catalogName");
            return (Criteria) this;
        }

        public Criteria andLevelPathLikeInsensitive(String value) {
            addCriterion("upper(level_path) like", value.toUpperCase(), "levelPath");
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