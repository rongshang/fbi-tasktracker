package task.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import task.repository.model.WorkorderAppoint;
import task.repository.model.WorkorderAppointExample;

public interface WorkorderAppointMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int countByExample(WorkorderAppointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int deleteByExample(WorkorderAppointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int insert(WorkorderAppoint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int insertSelective(WorkorderAppoint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    List<WorkorderAppoint> selectByExample(WorkorderAppointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    WorkorderAppoint selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByExampleSelective(@Param("record") WorkorderAppoint record, @Param("example") WorkorderAppointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByExample(@Param("record") WorkorderAppoint record, @Param("example") WorkorderAppointExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByPrimaryKeySelective(WorkorderAppoint record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByPrimaryKey(WorkorderAppoint record);
}