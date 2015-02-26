package task.repository.dao.not_mybatis;

import task.repository.model.WorkorderInfo;
import task.repository.model.model_show.WorkorderInfoShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ����8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyWorkorderInfoMapper {
    @Select("select max(id) from WORKORDER_INFO")
    String getStrMaxCttId();

    /***
     * atuo:hu
     * 根据工单ID或者工单名获取该条件的工单信息
     * param:orderPkId(工单pkid),orderName(工单名)
     * @return List<WorkorderInfo>
     */
    @Select("select * from WORKORDER_INFO where ARCHIVED_FLAG=0 and PKID=#{PkId} or name=#{orderName}")
    List<WorkorderInfo> getWorkorderInfoByPkIdOrName(@Param("PkId") String orderPkId ,@Param("orderName") String orderName);

}
