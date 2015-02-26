package task.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import task.repository.model.model_show.MenuAppointShow;

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
    List<MenuAppointShow> selectOperResRecordsByModelShow(MenuAppointShow menuAppointShowPara);

    @Select("select" +
            "   opr.OPER_PKID as operPkid," +
            "   pm.MENULABEL as resName" +
            " from " +
            "   MENU_APPOINT opr" +
            " inner join" +
            "   PTMENU pm" +
            " on" +
            "   opr.MENU_PKID=pm.PKID")
    List<MenuAppointShow> getOperResPtmenuList();
}
