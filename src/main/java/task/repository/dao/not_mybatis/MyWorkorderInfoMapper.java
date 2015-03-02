package task.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import task.repository.model.WorkorderInfo;
import task.repository.model.not_mybatis.DeptOperShow;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: 下午8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyWorkorderInfoMapper {
    @Select("select max(id) from WORKORDER_INFO")
    String getStrMaxCttId();

    /***
     * atuo: huzy
     * 查询每个部门下有哪些人  页面中工单指派时用
     * @return List<DeptOperShow>
     */
    @Select("select  d.name deptName, d.pkid deptId,o.name operName,o.pkid from dept d left join oper o on o.dept_pkid = d.pkid where ENABLED='1' ")
    List<DeptOperShow> getDeptOper();
}
