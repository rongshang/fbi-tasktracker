package task.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import task.repository.model.Ptmenu;
import task.repository.model.PtmenuExample;

public interface PtmenuMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int countByExample(PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int deleteByExample(PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int insert(Ptmenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int insertSelective(Ptmenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    List<Ptmenu> selectByExample(PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    Ptmenu selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int updateByExampleSelective(@Param("record") Ptmenu record, @Param("example") PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int updateByExample(@Param("record") Ptmenu record, @Param("example") PtmenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int updateByPrimaryKeySelective(Ptmenu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TASK.PTMENU
     *
     * @mbggenerated Wed Feb 25 13:37:00 CST 2015
     */
    int updateByPrimaryKey(Ptmenu record);
}