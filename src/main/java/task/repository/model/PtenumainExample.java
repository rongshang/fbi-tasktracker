package task.repository.model;

import java.util.ArrayList;
import java.util.List;

public class PtenumainExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public PtenumainExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
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
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
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

        public Criteria andEnutypeIsNull() {
            addCriterion("ENUTYPE is null");
            return (Criteria) this;
        }

        public Criteria andEnutypeIsNotNull() {
            addCriterion("ENUTYPE is not null");
            return (Criteria) this;
        }

        public Criteria andEnutypeEqualTo(String value) {
            addCriterion("ENUTYPE =", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeNotEqualTo(String value) {
            addCriterion("ENUTYPE <>", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeGreaterThan(String value) {
            addCriterion("ENUTYPE >", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeGreaterThanOrEqualTo(String value) {
            addCriterion("ENUTYPE >=", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeLessThan(String value) {
            addCriterion("ENUTYPE <", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeLessThanOrEqualTo(String value) {
            addCriterion("ENUTYPE <=", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeLike(String value) {
            addCriterion("ENUTYPE like", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeNotLike(String value) {
            addCriterion("ENUTYPE not like", value, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeIn(List<String> values) {
            addCriterion("ENUTYPE in", values, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeNotIn(List<String> values) {
            addCriterion("ENUTYPE not in", values, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeBetween(String value1, String value2) {
            addCriterion("ENUTYPE between", value1, value2, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnutypeNotBetween(String value1, String value2) {
            addCriterion("ENUTYPE not between", value1, value2, "enutype");
            return (Criteria) this;
        }

        public Criteria andEnunameIsNull() {
            addCriterion("ENUNAME is null");
            return (Criteria) this;
        }

        public Criteria andEnunameIsNotNull() {
            addCriterion("ENUNAME is not null");
            return (Criteria) this;
        }

        public Criteria andEnunameEqualTo(String value) {
            addCriterion("ENUNAME =", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameNotEqualTo(String value) {
            addCriterion("ENUNAME <>", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameGreaterThan(String value) {
            addCriterion("ENUNAME >", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameGreaterThanOrEqualTo(String value) {
            addCriterion("ENUNAME >=", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameLessThan(String value) {
            addCriterion("ENUNAME <", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameLessThanOrEqualTo(String value) {
            addCriterion("ENUNAME <=", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameLike(String value) {
            addCriterion("ENUNAME like", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameNotLike(String value) {
            addCriterion("ENUNAME not like", value, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameIn(List<String> values) {
            addCriterion("ENUNAME in", values, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameNotIn(List<String> values) {
            addCriterion("ENUNAME not in", values, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameBetween(String value1, String value2) {
            addCriterion("ENUNAME between", value1, value2, "enuname");
            return (Criteria) this;
        }

        public Criteria andEnunameNotBetween(String value1, String value2) {
            addCriterion("ENUNAME not between", value1, value2, "enuname");
            return (Criteria) this;
        }

        public Criteria andValuetypeIsNull() {
            addCriterion("VALUETYPE is null");
            return (Criteria) this;
        }

        public Criteria andValuetypeIsNotNull() {
            addCriterion("VALUETYPE is not null");
            return (Criteria) this;
        }

        public Criteria andValuetypeEqualTo(String value) {
            addCriterion("VALUETYPE =", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeNotEqualTo(String value) {
            addCriterion("VALUETYPE <>", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeGreaterThan(String value) {
            addCriterion("VALUETYPE >", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeGreaterThanOrEqualTo(String value) {
            addCriterion("VALUETYPE >=", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeLessThan(String value) {
            addCriterion("VALUETYPE <", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeLessThanOrEqualTo(String value) {
            addCriterion("VALUETYPE <=", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeLike(String value) {
            addCriterion("VALUETYPE like", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeNotLike(String value) {
            addCriterion("VALUETYPE not like", value, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeIn(List<String> values) {
            addCriterion("VALUETYPE in", values, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeNotIn(List<String> values) {
            addCriterion("VALUETYPE not in", values, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeBetween(String value1, String value2) {
            addCriterion("VALUETYPE between", value1, value2, "valuetype");
            return (Criteria) this;
        }

        public Criteria andValuetypeNotBetween(String value1, String value2) {
            addCriterion("VALUETYPE not between", value1, value2, "valuetype");
            return (Criteria) this;
        }

        public Criteria andEnudescIsNull() {
            addCriterion("ENUDESC is null");
            return (Criteria) this;
        }

        public Criteria andEnudescIsNotNull() {
            addCriterion("ENUDESC is not null");
            return (Criteria) this;
        }

        public Criteria andEnudescEqualTo(String value) {
            addCriterion("ENUDESC =", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescNotEqualTo(String value) {
            addCriterion("ENUDESC <>", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescGreaterThan(String value) {
            addCriterion("ENUDESC >", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescGreaterThanOrEqualTo(String value) {
            addCriterion("ENUDESC >=", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescLessThan(String value) {
            addCriterion("ENUDESC <", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescLessThanOrEqualTo(String value) {
            addCriterion("ENUDESC <=", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescLike(String value) {
            addCriterion("ENUDESC like", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescNotLike(String value) {
            addCriterion("ENUDESC not like", value, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescIn(List<String> values) {
            addCriterion("ENUDESC in", values, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescNotIn(List<String> values) {
            addCriterion("ENUDESC not in", values, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescBetween(String value1, String value2) {
            addCriterion("ENUDESC between", value1, value2, "enudesc");
            return (Criteria) this;
        }

        public Criteria andEnudescNotBetween(String value1, String value2) {
            addCriterion("ENUDESC not between", value1, value2, "enudesc");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated do_not_delete_during_merge Wed Aug 27 15:54:09 CST 2014
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
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