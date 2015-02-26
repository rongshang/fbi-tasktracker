package task.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import task.repository.model.WorkorderItem;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyWorkorderItemMapper {
    @Select(" select " +
            "     nvl(max(LEVELIDX),0)+1" +
            " from " +
            "     WORKORDER_ITEM " +
            " where"+
            "     INFO_PKID = #{strWorkorderInfoPkid}")
    Integer getMaxLevelidxPlusOne(@Param("strWorkorderInfoPkid") String strWorkorderInfoPkid);
}
