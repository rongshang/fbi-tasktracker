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

    // Todo  待做任务  RECV_TASK_EXEC_FLAG=‘0’ ，RECV_TASK_PART_PKID=登陆者或者SEND_TASK_PART_PKID=登陆者，但是RECV_TASK_PART_PKID为空
    @Select(" select " +
        "   aa.pkid as workorderInfopkid, " +    //工单信息主键
        "   aa.id   as workorderInfoid," +
        "   aa.name as workorderInfoname," +
        "   bb.created_time  as createdTime," +
        "   bb.pkid     as  workorderAppointPkid " +                       //流程控制表主键
        "   from workorder_info  aa" +
        "   INNER JOIN" +
        "   WORKORDER_APPOINT bb" +
        "   ON  bb.info_pkid = aa.pkid" +
        "   and" +
        "  （bb.RECV_TASK_PART_PKID = #{strOperPkid} or (bb.SEND_TASK_PART_PKID = #{strOperPkid} and  bb.RECV_TASK_PART_PKID ='')）" +
        "   and bb.RECV_TASK_EXEC_FLAG ='0 '" + //  待执行
        "   group by aa.pkid，aa.id ，aa.name，bb.created_time,bb.pkid" +
        "   order by workorderInfopkid ")
    List<TaskShow> getTodoTaskShowList(@Param("strOperPkid") String strOperPkid);
// DoneTask  已完成任务 RECV_TASK_EXEC_FLAG=‘1’ ，RECV_TASK_PART_PKID=登陆者
    @Select(" select " +
            "   aa.pkid as workorderInfopkid, " +
            "   aa.id   as workorderInfoid," +
            "   aa.name as workorderInfoname," +
            "   bb.created_time  as createdTime," +
            "   bb.pkid     as  workorderAppointPkid " +
            "   from workorder_info  aa" +
            "   INNER JOIN" +
            "   WORKORDER_APPOINT bb" +
            "   ON  bb.info_pkid = aa.pkid" +
            "   and" +
            "   bb.RECV_TASK_PART_PKID = #{strOperPkid}" +
            "   and bb.RECV_TASK_EXEC_FLAG ='2 '" +//  已执行
            "   group by aa.pkid，aa.id ，aa.name，bb.created_time,bb.pkid" +
            "   order by workorderInfopkid ")
    List<TaskShow> getDoneTaskShowList(@Param("strOperPkid") String strOperPkid);
    // DoneTask  已完成任务 RECV_TASK_EXEC_FLAG=‘1’ ，RECV_TASK_PART_PKID=登陆者
    @Select(" select " +
            "   aa.pkid as workorderInfopkid, " +
            "   aa.id   as workorderInfoid," +
            "   aa.name as workorderInfoname," +
            "   bb.created_time  as createdTime," +
            "   bb.pkid     as  workorderAppointPkid " +
            "   from workorder_info  aa" +
            "   INNER JOIN" +
            "   WORKORDER_APPOINT bb" +
            "   ON  bb.info_pkid = aa.pkid" +
            "   and" +
            "   bb.RECV_TASK_PART_PKID = #{strOperPkid}" +
            "   and bb.RECV_TASK_EXEC_FLAG ='1 '" +//  执行中
            "   group by aa.pkid，aa.id ，aa.name，bb.created_time,bb.pkid" +
            "   order by workorderInfopkid ")
    List<TaskShow> getDoingTaskShowList(@Param("strOperPkid") String strOperPkid);


}