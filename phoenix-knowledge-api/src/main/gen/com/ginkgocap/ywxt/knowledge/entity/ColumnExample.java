package com.ginkgocap.ywxt.knowledge.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ColumnExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public ColumnExample() {
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

        public Criteria andColumnnameIsNull() {
            addCriterion("columnName is null");
            return (Criteria) this;
        }

        public Criteria andColumnnameIsNotNull() {
            addCriterion("columnName is not null");
            return (Criteria) this;
        }

        public Criteria andColumnnameEqualTo(String value) {
            addCriterion("columnName =", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameNotEqualTo(String value) {
            addCriterion("columnName <>", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameGreaterThan(String value) {
            addCriterion("columnName >", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameGreaterThanOrEqualTo(String value) {
            addCriterion("columnName >=", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameLessThan(String value) {
            addCriterion("columnName <", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameLessThanOrEqualTo(String value) {
            addCriterion("columnName <=", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameLike(String value) {
            addCriterion("columnName like", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameNotLike(String value) {
            addCriterion("columnName not like", value, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameIn(List<String> values) {
            addCriterion("columnName in", values, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameNotIn(List<String> values) {
            addCriterion("columnName not in", values, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameBetween(String value1, String value2) {
            addCriterion("columnName between", value1, value2, "columnname");
            return (Criteria) this;
        }

        public Criteria andColumnnameNotBetween(String value1, String value2) {
            addCriterion("columnName not between", value1, value2, "columnname");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(Long value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(Long value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(Long value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(Long value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(Long value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<Long> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<Long> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(Long value1, Long value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(Long value1, Long value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
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

        public Criteria andPathNameIsNull() {
            addCriterion("path_name is null");
            return (Criteria) this;
        }

        public Criteria andPathNameIsNotNull() {
            addCriterion("path_name is not null");
            return (Criteria) this;
        }

        public Criteria andPathNameEqualTo(String value) {
            addCriterion("path_name =", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameNotEqualTo(String value) {
            addCriterion("path_name <>", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameGreaterThan(String value) {
            addCriterion("path_name >", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameGreaterThanOrEqualTo(String value) {
            addCriterion("path_name >=", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameLessThan(String value) {
            addCriterion("path_name <", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameLessThanOrEqualTo(String value) {
            addCriterion("path_name <=", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameLike(String value) {
            addCriterion("path_name like", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameNotLike(String value) {
            addCriterion("path_name not like", value, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameIn(List<String> values) {
            addCriterion("path_name in", values, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameNotIn(List<String> values) {
            addCriterion("path_name not in", values, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameBetween(String value1, String value2) {
            addCriterion("path_name between", value1, value2, "pathName");
            return (Criteria) this;
        }

        public Criteria andPathNameNotBetween(String value1, String value2) {
            addCriterion("path_name not between", value1, value2, "pathName");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathIsNull() {
            addCriterion("column_level_path is null");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathIsNotNull() {
            addCriterion("column_level_path is not null");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathEqualTo(String value) {
            addCriterion("column_level_path =", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathNotEqualTo(String value) {
            addCriterion("column_level_path <>", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathGreaterThan(String value) {
            addCriterion("column_level_path >", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathGreaterThanOrEqualTo(String value) {
            addCriterion("column_level_path >=", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathLessThan(String value) {
            addCriterion("column_level_path <", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathLessThanOrEqualTo(String value) {
            addCriterion("column_level_path <=", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathLike(String value) {
            addCriterion("column_level_path like", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathNotLike(String value) {
            addCriterion("column_level_path not like", value, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathIn(List<String> values) {
            addCriterion("column_level_path in", values, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathNotIn(List<String> values) {
            addCriterion("column_level_path not in", values, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathBetween(String value1, String value2) {
            addCriterion("column_level_path between", value1, value2, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathNotBetween(String value1, String value2) {
            addCriterion("column_level_path not between", value1, value2, "columnLevelPath");
            return (Criteria) this;
        }

        public Criteria andDelStatusIsNull() {
            addCriterion("del_status is null");
            return (Criteria) this;
        }

        public Criteria andDelStatusIsNotNull() {
            addCriterion("del_status is not null");
            return (Criteria) this;
        }

        public Criteria andDelStatusEqualTo(Byte value) {
            addCriterion("del_status =", value, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusNotEqualTo(Byte value) {
            addCriterion("del_status <>", value, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusGreaterThan(Byte value) {
            addCriterion("del_status >", value, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("del_status >=", value, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusLessThan(Byte value) {
            addCriterion("del_status <", value, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusLessThanOrEqualTo(Byte value) {
            addCriterion("del_status <=", value, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusIn(List<Byte> values) {
            addCriterion("del_status in", values, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusNotIn(List<Byte> values) {
            addCriterion("del_status not in", values, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusBetween(Byte value1, Byte value2) {
            addCriterion("del_status between", value1, value2, "delStatus");
            return (Criteria) this;
        }

        public Criteria andDelStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("del_status not between", value1, value2, "delStatus");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountIsNull() {
            addCriterion("subscribe_count is null");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountIsNotNull() {
            addCriterion("subscribe_count is not null");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountEqualTo(Long value) {
            addCriterion("subscribe_count =", value, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountNotEqualTo(Long value) {
            addCriterion("subscribe_count <>", value, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountGreaterThan(Long value) {
            addCriterion("subscribe_count >", value, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountGreaterThanOrEqualTo(Long value) {
            addCriterion("subscribe_count >=", value, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountLessThan(Long value) {
            addCriterion("subscribe_count <", value, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountLessThanOrEqualTo(Long value) {
            addCriterion("subscribe_count <=", value, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountIn(List<Long> values) {
            addCriterion("subscribe_count in", values, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountNotIn(List<Long> values) {
            addCriterion("subscribe_count not in", values, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountBetween(Long value1, Long value2) {
            addCriterion("subscribe_count between", value1, value2, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andSubscribeCountNotBetween(Long value1, Long value2) {
            addCriterion("subscribe_count not between", value1, value2, "subscribeCount");
            return (Criteria) this;
        }

        public Criteria andColumnnameLikeInsensitive(String value) {
            addCriterion("upper(columnName) like", value.toUpperCase(), "columnname");
            return (Criteria) this;
        }

        public Criteria andPathNameLikeInsensitive(String value) {
            addCriterion("upper(path_name) like", value.toUpperCase(), "pathName");
            return (Criteria) this;
        }

        public Criteria andColumnLevelPathLikeInsensitive(String value) {
            addCriterion("upper(column_level_path) like", value.toUpperCase(), "columnLevelPath");
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