package task.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import task.repository.model.not_mybatis.WorkorderAppointShow;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyWorkorderAppointMapper {
    @Select("select"+
            "     wi.PKID                                           as infoPkid,"+
            "     wi.ID                                             as infoId,"+
            "     wi.NAME                                           as infoName,"+
            "     wi.START_TIME                                     as infoStartTime,"+
            "     wi.END_TIME                                       as infoEndTime,"+
            "     wi.SIGN_DATE                                      as signDate,"+
            "     wa.PKID                                           as workorderAppointPkid,"+
            "     wa.FIRST_APPOINT_FLAG                             as firstAppointFlag," +
            "     wa.SEND_TASK_PART_PKID                            as sendTaskPartPkid," +
            "     (select name from oper where PKID=wa.SEND_TASK_PART_PKID) as sendTaskPartName,"+
            "     wa.RECV_TASK_PART_PKID                            as recvTaskPartPkid,"+
            "     (select name from oper where PKID=wa.RECV_TASK_PART_PKID) as recvTaskPartName,"+
            "     wa.RECV_TASK_EXEC_FLAG                          as recvTaskExecFlag,"+
            "     wa.ARCHIVED_FLAG                                  as archivedFlag,"+
            "     wa.CREATED_BY                                     as createdBy,"+
            "     (select name from oper where PKID=wa.CREATED_BY)  as createdByName,"+
            "     wa.CREATED_TIME                                   as createdTime,"+
            "     wa.LAST_UPD_BY                                    as lastUpdBy,"+
            "     (select name from oper where PKID=wa.LAST_UPD_BY) as lastUpdByName,"+
            "     wa.LAST_UPD_TIME                                  as lastUpdTime,"+
            "     wa.REMARK                                         as remark,"+
            "     wa.REC_VERSION                                    as recVersion,"+
            "     wa.TID                                            as tid," +
            "     (case when nvl(wa.RECV_TASK_EXEC_FLAG,'0')='0' then '0' else '1' end) as execedFlag"+
            " from"+
            "     WORKORDER_INFO wi"+
            " left join"+
            "     WORKORDER_APPOINT wa"+
            " on"+
            "     wa.INFO_PKID=wi.PKID" +

            " order by wi.ID,wa.FIRST_APPOINT_FLAG,wa.SEND_TASK_PART_PKID,wa.RECV_TASK_EXEC_FLAG")
    List<WorkorderAppointShow> getWorkorderAppointShowList(@Param("infoId") String infoIdPara,
                                                           @Param("infoName") String infoNamePara);
}
