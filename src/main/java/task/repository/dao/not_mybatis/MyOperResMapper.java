package task.repository.dao.not_mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import task.repository.model.model_show.OperResShow;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyOperResMapper {
    List<OperResShow> selectOperResRecordsByModelShow(OperResShow operResShowPara);

    @Select("select" +
            "   opr.OPER_PKID as operPkid," +
            "   pm.MENULABEL as resName" +
            " from " +
            "   OPER_RES opr" +
            " inner join" +
            "   PTMENU pm" +
            " on" +
            "   opr.RES_PKID=pm.PKID")
    List<OperResShow> getOperResPtmenuList();
}
