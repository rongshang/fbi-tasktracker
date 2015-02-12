package task.repository.dao.not_mybatis;

import task.repository.model.model_show.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: 下午8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyQueryMapper {
    @Select("select ecitem.CORRESPONDING_PKID as strCorrespondingPkid," +
            "     max(eicust.NAME) as strSignPartName," +
            "     max(ecitem.NAME) as strName," +
            "     sum(ecitem.Contract_Unit_Price) as bdUnitPrice," +
            "     sum(ecitem.Contract_Quantity) as bdQuantity," +
            "     sum(ecitem.Contract_Amount) as bdAmount" +
            " from" +
            "     CTT_INFO ecinfo" +
            " inner join " +
            "     CTT_ITEM ecitem" +
            " on " +
            "     ecinfo.PKID=ecitem.BELONG_TO_PKID" +
            " and " +
            "     ecinfo.CTT_TYPE=ecitem.BELONG_TO_TYPE" +
            " join " +
            "     SIGN_PART eicust" +
            " on " +
            "     ecinfo.SIGN_PART_B=eicust.PKID" +
            " where"+
            "     ecinfo.CTT_TYPE = #{strCttType}"+
            "     and ecinfo.PARENT_PKID = #{strParentPkid}" +
            " group by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B,ecitem.PKID" +
            " order by ecitem.CORRESPONDING_PKID,ecinfo.SIGN_PART_B")
    List<QryShow> getCSList(@Param("strCttType") String strCttType,
                            @Param("strParentPkid") String strParentPkid);

    @Select("select" +
            "     c_info_item.strCorrespondingPkid," +
            "     c_info_item.strItemPkid," +
            "     c_info_item.strSignPartName," +
            "     max(c_info_item.NAME) as strName," +
            "     max(c_info_item.UNIT) as strUnit," +
            "     sum(c_info_item.CONTRACT_UNIT_PRICE) as bdUnitPrice," +
            "     sum(c_info_item.SIGN_PART_A_PRICE) as bdSignPartAMPrice," +
            "     sum(c_info_item.CONTRACT_QUANTITY) as bdQuantity," +
            "     sum(ps_info_item.current_period_m_qty) as bdCurrentPeriodQuantity," +
            "     sum(ps_info_item.begin_to_current_period_m_qty) as bdBeginToCurrentPeriodQuantity" +
            " from " +
            "     (" +
            "       select " +
            "              citem.CORRESPONDING_PKID as strCorrespondingPkid," +
            "              citem.PKID as strItemPkid," +
            "              sp.NAME as strSignPartName," +
            "              citem.NAME," +
            "              citem.UNIT," +
            "              citem.CONTRACT_UNIT_PRICE," +
            "              citem.SIGN_PART_A_PRICE," +
            "              citem.CONTRACT_QUANTITY," +
            "              citem.BELONG_TO_PKID" +
            "       from" +
             "             CTT_INFO cinfo" +
            "       inner join " +
            "              SIGN_PART sp" +
            "       on " +
            "              cinfo.SIGN_PART_B=sp.PKID" +
            "       inner join" +
            "              CTT_ITEM citem" +
            "       on" +
            "              cinfo.CTT_TYPE=citem.belong_to_type" +
            "       and" +
            "              cinfo.PKID=citem.belong_to_pkid" +
            "       where" +
            "              cinfo.PARENT_PKID = #{strCstplInfoPkid}" +
            "       and " +  //已经批准了的分包合同
            "              cinfo.FLOW_STATUS='3'" +
               "     )c_info_item" +
            " inner join" +//联分包工程数量详细项
            "    (" +
            "       select" +
            "            subctt_pkid,period_no,subctt_item_pkid," +
            "            current_period_m_qty,begin_to_current_period_m_qty" +
            "       from (" +
            "             select" +
            "                  psism.subctt_pkid,psism.period_no,psism.subctt_item_pkid," +
            "                  psism.current_period_m_qty,psism.begin_to_current_period_m_qty," +
            "                  row_number() over " +
            "                       (partition by psism.subctt_pkid,psism.subctt_item_pkid" +
            "                        order by psism.period_no desc)rn" +
            "            from " +
            "                  PROG_STL_ITEM_SUB_M psism" +
            "            inner join" +
            "                  PROG_STL_INFO eis" +
            "            on" +
            "                  psism.SUBCTT_PKID=eis.stl_pkid" +
            "            and" +
            "                  psism.PERIOD_NO=eis.PERIOD_NO" +
            "            and" +
            "                  eis.STL_TYPE='4'" +
            "            and" +
            "                  eis.FLOW_STATUS='2'" +
            "            and" +
            "                  eis.period_no<=#{strPeriodNo}" +
            "           ) ta " +
            "       where rn=1" +
            "    )ps_info_item" +
            " on" +
            "    c_info_item.BELONG_TO_PKID=ps_info_item.subctt_pkid" +
            " and" +
            "    c_info_item.strItemPkid=ps_info_item.subctt_item_pkid" +
            " group by" +
            "    BELONG_TO_PKID,strSignPartName,strCorrespondingPkid,strItemPkid")
    List<QryShow> getCSStlMList(@Param("strCstplInfoPkid") String strCstplInfoPkid,
                                @Param("strPeriodNo") String strPeriodNo);



}
