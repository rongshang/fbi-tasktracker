package task.repository.dao;

import task.repository.model.Oper;
import task.repository.model.OperExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int countByExample(OperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int deleteByExample(OperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int insert(Oper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int insertSelective(Oper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    List<Oper> selectByExample(OperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    Oper selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int updateByExampleSelective(@Param("record") Oper record, @Param("example") OperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int updateByExample(@Param("record") Oper record, @Param("example") OperExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int updateByPrimaryKeySelective(Oper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.OPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    int updateByPrimaryKey(Oper record);
}