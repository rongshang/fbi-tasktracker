package task.repository.dao;

import task.repository.model.Ptenumain;
import task.repository.model.PtenumainExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PtenumainMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int countByExample(PtenumainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int deleteByExample(PtenumainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int deleteByPrimaryKey(String enutype);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int insert(Ptenumain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int insertSelective(Ptenumain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    List<Ptenumain> selectByExample(PtenumainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    Ptenumain selectByPrimaryKey(String enutype);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int updateByExampleSelective(@Param("record") Ptenumain record, @Param("example") PtenumainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int updateByExample(@Param("record") Ptenumain record, @Param("example") PtenumainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int updateByPrimaryKeySelective(Ptenumain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task.PTENUMAIN
     *
     * @mbggenerated Wed Aug 27 15:54:09 CST 2014
     */
    int updateByPrimaryKey(Ptenumain record);
}