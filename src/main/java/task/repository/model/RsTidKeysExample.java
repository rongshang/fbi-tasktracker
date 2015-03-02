package task.repository.model;

import java.util.ArrayList;
import java.util.List;

public class RsTidKeysExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public RsTidKeysExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
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

        public Criteria andPkidIsNull() {
            addCriterion("PKID is null");
            return (Criteria) this;
        }

        public Criteria andPkidIsNotNull() {
            addCriterion("PKID is not null");
            return (Criteria) this;
        }

        public Criteria andPkidEqualTo(String value) {
            addCriterion("PKID =", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotEqualTo(String value) {
            addCriterion("PKID <>", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidGreaterThan(String value) {
            addCriterion("PKID >", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidGreaterThanOrEqualTo(String value) {
            addCriterion("PKID >=", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidLessThan(String value) {
            addCriterion("PKID <", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidLessThanOrEqualTo(String value) {
            addCriterion("PKID <=", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidLike(String value) {
            addCriterion("PKID like", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotLike(String value) {
            addCriterion("PKID not like", value, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidIn(List<String> values) {
            addCriterion("PKID in", values, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotIn(List<String> values) {
            addCriterion("PKID not in", values, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidBetween(String value1, String value2) {
            addCriterion("PKID between", value1, value2, "pkid");
            return (Criteria) this;
        }

        public Criteria andPkidNotBetween(String value1, String value2) {
            addCriterion("PKID not between", value1, value2, "pkid");
            return (Criteria) this;
        }

        public Criteria andTidIsNull() {
            addCriterion("TID is null");
            return (Criteria) this;
        }

        public Criteria andTidIsNotNull() {
            addCriterion("TID is not null");
            return (Criteria) this;
        }

        public Criteria andTidEqualTo(String value) {
            addCriterion("TID =", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidNotEqualTo(String value) {
            addCriterion("TID <>", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidGreaterThan(String value) {
            addCriterion("TID >", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidGreaterThanOrEqualTo(String value) {
            addCriterion("TID >=", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidLessThan(String value) {
            addCriterion("TID <", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidLessThanOrEqualTo(String value) {
            addCriterion("TID <=", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidLike(String value) {
            addCriterion("TID like", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidNotLike(String value) {
            addCriterion("TID not like", value, "tid");
            return (Criteria) this;
        }

        public Criteria andTidIn(List<String> values) {
            addCriterion("TID in", values, "tid");
            return (Criteria) this;
        }

        public Criteria andTidNotIn(List<String> values) {
            addCriterion("TID not in", values, "tid");
            return (Criteria) this;
        }

        public Criteria andTidBetween(String value1, String value2) {
            addCriterion("TID between", value1, value2, "tid");
            return (Criteria) this;
        }

        public Criteria andTidNotBetween(String value1, String value2) {
            addCriterion("TID not between", value1, value2, "tid");
            return (Criteria) this;
        }

        public Criteria andOperCountsIsNull() {
            addCriterion("OPER_COUNTS is null");
            return (Criteria) this;
        }

        public Criteria andOperCountsIsNotNull() {
            addCriterion("OPER_COUNTS is not null");
            return (Criteria) this;
        }

        public Criteria andOperCountsEqualTo(String value) {
            addCriterion("OPER_COUNTS =", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsNotEqualTo(String value) {
            addCriterion("OPER_COUNTS <>", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsGreaterThan(String value) {
            addCriterion("OPER_COUNTS >", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsGreaterThanOrEqualTo(String value) {
            addCriterion("OPER_COUNTS >=", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsLessThan(String value) {
            addCriterion("OPER_COUNTS <", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsLessThanOrEqualTo(String value) {
            addCriterion("OPER_COUNTS <=", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsLike(String value) {
            addCriterion("OPER_COUNTS like", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsNotLike(String value) {
            addCriterion("OPER_COUNTS not like", value, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsIn(List<String> values) {
            addCriterion("OPER_COUNTS in", values, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsNotIn(List<String> values) {
            addCriterion("OPER_COUNTS not in", values, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsBetween(String value1, String value2) {
            addCriterion("OPER_COUNTS between", value1, value2, "operCounts");
            return (Criteria) this;
        }

        public Criteria andOperCountsNotBetween(String value1, String value2) {
            addCriterion("OPER_COUNTS not between", value1, value2, "operCounts");
            return (Criteria) this;
        }

        public Criteria andEsKeyIsNull() {
            addCriterion("ES_KEY is null");
            return (Criteria) this;
        }

        public Criteria andEsKeyIsNotNull() {
            addCriterion("ES_KEY is not null");
            return (Criteria) this;
        }

        public Criteria andEsKeyEqualTo(String value) {
            addCriterion("ES_KEY =", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyNotEqualTo(String value) {
            addCriterion("ES_KEY <>", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyGreaterThan(String value) {
            addCriterion("ES_KEY >", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyGreaterThanOrEqualTo(String value) {
            addCriterion("ES_KEY >=", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyLessThan(String value) {
            addCriterion("ES_KEY <", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyLessThanOrEqualTo(String value) {
            addCriterion("ES_KEY <=", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyLike(String value) {
            addCriterion("ES_KEY like", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyNotLike(String value) {
            addCriterion("ES_KEY not like", value, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyIn(List<String> values) {
            addCriterion("ES_KEY in", values, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyNotIn(List<String> values) {
            addCriterion("ES_KEY not in", values, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyBetween(String value1, String value2) {
            addCriterion("ES_KEY between", value1, value2, "esKey");
            return (Criteria) this;
        }

        public Criteria andEsKeyNotBetween(String value1, String value2) {
            addCriterion("ES_KEY not between", value1, value2, "esKey");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated do_not_delete_during_merge Mon Mar 02 13:49:41 CST 2015
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TASK.RS_TID_KEYS
     *
     * @mbggenerated Mon Mar 02 13:49:41 CST 2015
     */
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