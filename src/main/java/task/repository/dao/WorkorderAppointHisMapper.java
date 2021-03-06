package task.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import task.repository.model.WorkorderAppointHis;
import task.repository.model.WorkorderAppointHisExample;

public interface WorkorderAppointHisMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int countByExample(WorkorderAppointHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int deleteByExample(WorkorderAppointHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int insert(WorkorderAppointHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int insertSelective(WorkorderAppointHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    List<WorkorderAppointHis> selectByExample(WorkorderAppointHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    WorkorderAppointHis selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByExampleSelective(@Param("record") WorkorderAppointHis record, @Param("example") WorkorderAppointHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByExample(@Param("record") WorkorderAppointHis record, @Param("example") WorkorderAppointHisExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByPrimaryKeySelective(WorkorderAppointHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.WORKORDER_APPOINT_HIS
     *
     * @mbggenerated Sun Mar 01 16:04:00 CST 2015
     */
    int updateByPrimaryKey(WorkorderAppointHis record);
}