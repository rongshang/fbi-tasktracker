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

    // Todo  ��������  RECV_TASK_FINISH_FLAG=��0�� ��RECV_TASK_PART_PKID=��½�߻���SEND_TASK_PART_PKID=��½�ߣ�����RECV_TASK_PART_PKIDΪ��
    @Select(" select " +
        "   aa.pkid as workorderInfopkid, " +
        "   aa.id   as workorderInfoid," +
        "   aa.name as workorderInfoname," +
        "   bb.created_time  as createdTime" +
        "   from workorder_info  aa" +
        "   INNER JOIN" +
        "   WORKORDER_APPOINT bb" +
        "   ON  bb.info_pkid = aa.pkid" +
        "   and" +
        "  ��bb.RECV_TASK_PART_PKID = #{strOperPkid} or (bb.SEND_TASK_PART_PKID = #{strOperPkid} and  bb.RECV_TASK_PART_PKID ='')��" +
        "   and bb.RECV_TASK_FINISH_FLAG ='0 '" + //  �����
        "   group by aa.pkid��aa.id ��aa.name��bb.created_time" +
        "   order by workorderInfopkid ")
    List<TaskShow> getTodoTaskShowList(@Param("strOperPkid") String strOperPkid);
// DoneTask  ��������� RECV_TASK_FINISH_FLAG=��1�� ��RECV_TASK_PART_PKID=��½��
    @Select(" select " +
            "   aa.pkid as workorderInfopkid, " +
            "   aa.id   as workorderInfoid," +
            "   aa.name as workorderInfoname," +
            "   bb.created_time  as createdTime" +
            "   from workorder_info  aa" +
            "   INNER JOIN" +
            "   WORKORDER_APPOINT bb" +
            "   ON  bb.info_pkid = aa.pkid" +
            "   and" +
            "   bb.RECV_TASK_PART_PKID = #{strOperPkid}" +
            "   and bb.RECV_TASK_FINISH_FLAG ='1 '" +//  �����
            "   group by aa.pkid��aa.id ��aa.name��bb.created_time" +
            "   order by workorderInfopkid ")
    List<TaskShow> getDoneTaskShowList(@Param("strOperPkid") String strOperPkid);
}