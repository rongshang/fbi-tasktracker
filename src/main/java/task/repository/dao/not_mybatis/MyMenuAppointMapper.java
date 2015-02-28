package task.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import task.repository.model.not_mybatis.MenuAppointShow;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyMenuAppointMapper {
    @Select("select"+
            "     ma.PKID                                           as pkid,"+
            "     ma.TID                                            as tid,"+
            "     ma.OPER_PKID                                      as operPkid,"+
            "     o.ID                                              as operId,"+
            "     o.NAME                                            as operName,"+
            "     ma.MENU_PKID                                      as menuPkid,"+
            "     pm.MENULABEL                                      as menuName,"+
            "     ma.ARCHIVED_FLAG                                  as archivedFlag,"+
            "     ma.CREATED_BY                                     as createdBy,"+
            "     (select name from oper where PKID=ma.CREATED_BY)  as createdByName,"+
            "     ma.CREATED_TIME                                   as createdTime,"+
            "     ma.LAST_UPD_BY                                    as lastUpdBy,"+
            "     (select name from oper where PKID=ma.LAST_UPD_BY) as lastUpdByName,"+
            "     ma.LAST_UPD_TIME                                  as lastUpdTime,"+
            "     ma.REMARK                                         as remark,"+
            "     ma.REC_VERSION                                    as recVersion"+
            " from"+
            "     MENU_APPOINT ma"+
            " left join"+
            "     OPER o"+
            " on"+
            "     ma.OPER_PKID=o.PKID" +
            " left join" +
            "     PTMENU pm" +
            " on" +
            "     ma.MENU_PKID=pm.PKID" +
            " where " +
            "     (case when #{operPkid} is null then ma.OPER_PKID else #{operPkid} end)=ma.OPER_PKID" +
            " and" +
            "     (case when #{menuPkid} is null then ma.MENU_PKID else #{menuPkid} end)=ma.MENU_PKID")
    List<MenuAppointShow> getMenuAppointShowList(@Param("operPkid") String operPkidPara,
                                                 @Param("menuPkid") String menuPkidPara);
}
