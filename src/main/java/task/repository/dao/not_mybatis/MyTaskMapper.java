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
// send
    @Select(" select " +
            "   aa.pkid as taskpkid, " +
            "   aa.id   as taskid," +
            "   aa.name as taskname," +
            "   (select name  from oper  cc where cc.pkid =  bb.send_task_part  ) as sender," +
            "   (select name  from oper cc  where cc.pkid = bb.recv_task_part ) as recer, " +
            "   (select name from oper cc where cc.pkid = bb.created_by)  as createby," +
            "   bb.created_time  as createtime" +
            "   from workorder_info  aa" +
            "   INNER JOIN" +
            "   flow bb" +
            "   ON  bb.info_pkid = aa.pkid" +
            "   and" +
            "   bb.send_task_part = #{strOperPkid}" +
            "   order by taskid ")
    List<TaskShow> getSendTaskShowList(@Param("strOperPkid") String strOperPkid);
//rec
    @Select(" select " +
            "   aa.pkid as taskpkid, " +
            "   aa.id   as taskid," +
            "   aa.name as taskname," +
            "   (select name  from oper  cc where cc.pkid =  bb.send_task_part  ) as sender," +
            "   (select name  from oper cc  where cc.pkid = bb.recv_task_part ) as recer, " +
            "   (select name from oper cc where cc.pkid = bb.created_by)  as createby," +
            "   bb.created_time  as createtime" +
            "   from workorder_info  aa" +
            "   INNER JOIN" +
            "   flow bb" +
            "   ON  bb.info_pkid = aa.pkid" +
            "   and" +
            "   bb.recv_task_part = #{strOperPkid}" +
            "   order by taskid ")
    List<TaskShow> getRecTaskShowList(@Param("strOperPkid") String strOperPkid);
}