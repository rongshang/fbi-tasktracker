package task.repository.dao.not_mybatis;

import task.repository.model.model_show.WorkorderInfoShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: обнГ8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyWorkorderInfoMapper {
    @Select("select max(id) from WORKORDER_INFO")
    String getStrMaxCttId();

}
