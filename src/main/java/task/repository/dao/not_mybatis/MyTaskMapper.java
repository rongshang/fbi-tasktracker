package task.repository.dao.not_mybatis;

import task.repository.model.model_show.TaskShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time:
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyTaskMapper {
    @Select(" select " +
            "   distinct opr.FLOW_STATUS as flowStatus" +
            " from" +
            "   OPER_RES opr" +
            " where" +
            "   opr.TYPE='business'" +
            " order by" +
            "   opr.FLOW_STATUS")
    List<TaskShow> getTaskFlowGroup();

/*    @Select(" select " +
            "   distinct opr.FLOW_STATUS as flowStatus" +
            " from" +
            "   OPER_RES opr" +
            " where" +
            "   opr.OPER_PKID=#{strOperPkid}" +
            " and " +
            "   opr.TYPE='business'" +
            " order by" +
            "   opr.FLOW_STATUS")
    List<TaskShow> getOwnTaskFlowGroup(@Param("strOperPkid") String strOperPkid);*/

    /*@Select(
            " (" +
            "      SELECT" +
            "           DISTINCT" +
            "           opr.PKID," +
            "           opr.INFO_TYPE as type," +
            "           ci.ID," +
            "           ci.NAME," +
            "           opr.FLOW_STATUS as flowStatus," +
            "           opr.TASKDONE_FLAG as taskDoneFlag" +
            "      FROM" +
            "           OPER_RES opr" +
            "      INNER JOIN" +
            "           WORKORDER_INFO ci" +
            "      ON" +
            "           ci.PKID=opr.INFO_PKID" +
            "      AND " +
            "           ci.flow_status='3'" +
            "      where" +
            "           opr.OPER_PKID=#{strOperPkid}" +
            "      and" +
            "           opr.INFO_TYPE>='3'" +
            "      and" +
            "           opr.FLOW_STATUS='0'" +
            "      and " +
            "           opr.TYPE='business'" +
            " )" +
            " order by  " +
            "    type,taskDoneFlag")
    List<TaskShow> getRencentlyPowerDetailTaskShowList(@Param("strOperPkid") String strOperPkid);*/

   /* @Select(
            "    (" +
            "        select" +
            "            distinct" +
            "            opr.INFO_TYPE as type," +
            "            ci.PKID as pkid," +
            "            opr.FLOW_STATUS as operResFlowStatus," +
            "            ci.ID as id," +
            "            ci.NAME as name," +
            "            '' as periodNo," +
            "            ci.FLOW_STATUS as flowStatus," +
            "            ci.FLOW_STATUS_REASON as flowStatusReason," +
            "            ci.FLOW_STATUS_REMARK as flowStatusRemark" +
            "        from" +
            "            OPER_RES opr" +
            "        inner join  " +
            "            WORKORDER_INFO ci" +
            "        on" +
            "            opr.INFO_TYPE=ci.CTT_TYPE" +
            "        and" +
            "            opr.INFO_PKID=ci.PKID" +
            "        where" +
            "            opr.OPER_PKID=#{strOperPkid}" +
            "        and" +
            "            opr.TYPE='business'" +
            "    ) " +
            " order by" +
            "    flowStatus,name,periodNo")
    List<TaskShow> getDetailTodoTaskShowList(@Param("strOperPkid") String strOperPkid);*/
}