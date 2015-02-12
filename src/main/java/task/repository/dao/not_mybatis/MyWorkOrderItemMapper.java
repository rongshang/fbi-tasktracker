package task.repository.dao.not_mybatis;

import task.repository.model.CttItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ����8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyWorkOrderItemMapper {
    @Update("Update CTT_ITEM set ORDERID=ORDERID+1 where ORDERID >= #{intOrderid}"+
            " and BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}")
    void setAfterThisOrderidPlusOneByNode(@Param("strBelongToType") String strBelongToType,
                                          @Param("strBelongToPkid") String strBelongToPkid,
                                          @Param("strParentPkid") String strParentPkid,
                                          @Param("intGrade") Integer intGrade,
                                          @Param("intOrderid") Integer intOrderid);

    @Update("Update CTT_ITEM set ORDERID=ORDERID-1 where ORDERID >= #{intOrderid}"+
            " and BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}")
    void setAfterThisOrderidSubOneByNode(@Param("strBelongToType") String strBelongToType,
                                         @Param("strBelongToPkid") String strBelongToPkid,
                                         @Param("strParentPkid") String strParentPkid,
                                         @Param("intGrade") Integer intGrade,
                                         @Param("intOrderid") Integer intOrderid);

    @Update("Update CTT_ITEM set PKID = #{strOldPkid} where PKID = #{strNewPkid}")
    void updateRecordPkid(@Param("strNewPkid") String strNewPkid,
                          @Param("strOldPkid") String strOldPkid);

    @Select("select * from CTT_ITEM where"+
            "     BELONG_TO_TYPE = #{strBelongToType}"+
            " and BELONG_TO_PKID = #{strBelongToPkid}"+
            " and PARENT_PKID         = #{strParentPkid}"+
            " and GRADE               = #{intGrade}"+
            " and ORDERID             = #{intOrderid}")
    CttItem getEsItemHieRelapByBusinessPk(@Param("strBelongToType") String strBelongToType,
                                          @Param("strBelongToPkid") String strBelongToPkid,
                                          @Param("strParentPkid") String strParentPkid,
                                          @Param("intGrade") Integer intGrade,
                                          @Param("intOrderid") Integer intOrderid);
}
