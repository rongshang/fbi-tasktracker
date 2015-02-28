package task.repository.dao.not_mybatis;

import task.repository.model.not_mybatis.TaskShow;
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

// send
    @Select(" select " +
            "   aa.pkid as taskpkid, " +
            "   aa.id   as taskid," +
            "   aa.name as taskname," +
            "   (select name  from oper  cc where cc.pkid =  bb.SEND_TASK_PART_PKID  ) as sender," +
            "   (select name  from oper cc  where cc.pkid = bb.RECV_TASK_PART_PKID ) as recer, " +
            "   (select name from oper cc where cc.pkid = bb.created_by)  as createby," +
            "   bb.created_time  as createtime" +
            "   from workorder_info  aa" +
            "   INNER JOIN" +
            "   WORKORDER_APPOINT bb" +
            "   ON  bb.info_pkid = aa.pkid" +
            "   and" +
            "   bb.SEND_TASK_PART_PKID = #{strOperPkid}" +
            "   order by taskid ")
    List<TaskShow> getSendTaskShowList(@Param("strOperPkid") String strOperPkid);
//rec
    @Select(" select " +
            "   aa.pkid as taskpkid, " +
            "   aa.id   as taskid," +
            "   aa.name as taskname," +
            "   (select name  from oper  cc where cc.pkid =  bb.SEND_TASK_PART_PKID  ) as sender," +
            "   (select name  from oper cc  where cc.pkid = bb.RECV_TASK_PART_PKID ) as recer, " +
            "   (select name from oper cc where cc.pkid = bb.created_by)  as createby," +
            "   bb.created_time  as createtime" +
            "   from workorder_info  aa" +
            "   INNER JOIN" +
            "   WORKORDER_APPOINT bb" +
            "   ON  bb.info_pkid = aa.pkid" +
            "   and" +
            "   bb.RECV_TASK_PART_PKID = #{strOperPkid}" +
            "   order by taskid ")
    List<TaskShow> getRecTaskShowList(@Param("strOperPkid") String strOperPkid);
}