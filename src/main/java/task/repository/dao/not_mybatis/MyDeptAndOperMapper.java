package task.repository.dao.not_mybatis;

import task.repository.model.not_mybatis.DeptOperShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: 下午8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyDeptAndOperMapper {
    @Select("select max(id) from DEPT")
    String getStrMaxDeptId();

    @Select("select max(id) from OPER")
    String getStrMaxOperId();

    @Select("   (select " +
            "        pkid, " +
            "        id, " +
            "        name, " +
            "        1 as type," +
            "        type as operType " +
            "     from " +
            "        oper " +
            "     where " +
            "        dept_pkid=#{parentPkid}" +
           /* "     and" +
            "        type !='0'" +*/
            "    ) " +
            " union " +
            "    (select " +
            "        pkid, " +
            "        id, " +
            "        name, " +
            "        0 as type," +
            "        '' as operType" +
            "     from " +
            "        dept ta " +
            "     where  " +
            "        parentpkid=#{parentPkid}" +
            "     ) " +
            " order by name asc")
    List<DeptOperShow> selectDeptAndOperRecords(@Param("parentPkid") String parentPkidPara);
    @Select("   select " +
            "       NAME as username" +
            "   from" +
            "       oper" +
            "   where " +
            "       PKID=#{operPkidPara} ")
    String getUserName(@Param("operPkidPara") String operPkidPara);

    /***
     * atuo: huzy
     * 查询每个部门下有哪些人  页面中工单指派时用
     * @return List<DeptOperShow>
     */
    @Select(" select  " +
            "       d.name as deptName," +
            "       d.pkid as deptId," +
            "       o.name as operName," +
            "       o.pkid as pkid" +
            " from " +
            "       dept d " +
            " left join " +
            "       oper o " +
            " on" +
            "       o.dept_pkid = d.pkid" +
            " where " +
            "       ENABLED='1'")
    List<DeptOperShow> getDeptOper();
}
