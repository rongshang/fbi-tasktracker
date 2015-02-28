package task.service;

import skyline.util.ToolUtil;
import org.springframework.stereotype.Service;
import task.repository.dao.MenuAppointMapper;
import task.repository.dao.not_mybatis.MyMenuAppointMapper;
import task.repository.model.MenuAppoint;
import task.repository.model.MenuAppointExample;
import task.repository.model.not_mybatis.MenuAppointShow;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class OperMenuService {
    @Resource
    private MyMenuAppointMapper myMenuAppointMapper;
    @Resource
    private MenuAppointMapper menuAppointMapper;

    public List<MenuAppointShow> getMenuAppointShowList(MenuAppointShow menuAppointShowPara){
        return myMenuAppointMapper.getMenuAppointShowList(
                ToolUtil.getStrIgnoreNull(menuAppointShowPara.getOperPkid()),
                ToolUtil.getStrIgnoreNull(menuAppointShowPara.getMenuPkid()));
    }

    public void insertRecord(MenuAppointShow menuAppointShowPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        menuAppointShowPara.setCreatedBy(strOperatorIdTemp);
        menuAppointShowPara.setCreatedTime(strLastUpdTime);
        menuAppointShowPara.setLastUpdBy(strOperatorIdTemp);
        menuAppointShowPara.setLastUpdTime(strLastUpdTime);
        menuAppointMapper.insert(fromOperShowToModel(menuAppointShowPara));
    }
    public void insertRecord(MenuAppoint menuAppointPara){
        insertRecord(fromModelToModelShow(menuAppointPara));
    }
    public void deleteRecordByMenuPkid(MenuAppoint menuAppointPara){
        MenuAppointExample example =new MenuAppointExample();
        MenuAppointExample.Criteria criteria = example.createCriteria();
        criteria.andMenuPkidEqualTo(menuAppointPara.getMenuPkid());
        menuAppointMapper.deleteByExample(example);
    }
    public void deleteRecordByOperPkid(MenuAppoint menuAppointPara){
        MenuAppointExample example =new MenuAppointExample();
        MenuAppointExample.Criteria criteria = example.createCriteria();
        criteria.andOperPkidEqualTo(menuAppointPara.getOperPkid());
        menuAppointMapper.deleteByExample(example);
    }

    public MenuAppoint fromOperShowToModel(MenuAppointShow record) {
        MenuAppoint menuAppointPara =new MenuAppoint();
        menuAppointPara.setTid(record.getTid());
        menuAppointPara.setOperPkid(record.getOperPkid());
        menuAppointPara.setMenuPkid(record.getMenuPkid());
        menuAppointPara.setArchivedFlag(record.getArchivedFlag());
        menuAppointPara.setCreatedBy(record.getCreatedBy());
        menuAppointPara.setCreatedTime(record.getCreatedTime());
        menuAppointPara.setLastUpdBy(record.getLastUpdBy());
        menuAppointPara.setLastUpdTime(record.getLastUpdTime());
        menuAppointPara.setRemark(record.getRemark());
        menuAppointPara.setRecVersion( ToolUtil.getIntIgnoreNull(record.getRecVersion()));
        return menuAppointPara;
    }
    public MenuAppointShow fromModelToModelShow(MenuAppoint menuAppointPara) {
        MenuAppointShow menuAppointShowTemp =new MenuAppointShow();
        menuAppointShowTemp.setTid(menuAppointPara.getTid());
        menuAppointShowTemp.setOperPkid(menuAppointPara.getOperPkid());
        menuAppointShowTemp.setMenuPkid(menuAppointPara.getMenuPkid());
        menuAppointShowTemp.setArchivedFlag(menuAppointPara.getArchivedFlag());
        menuAppointShowTemp.setCreatedBy(menuAppointPara.getCreatedBy());
        menuAppointShowTemp.setCreatedTime(menuAppointPara.getCreatedTime());
        menuAppointShowTemp.setLastUpdBy(menuAppointPara.getLastUpdBy());
        menuAppointShowTemp.setLastUpdTime(menuAppointPara.getLastUpdTime());
        menuAppointShowTemp.setRemark(menuAppointPara.getRemark());
        menuAppointShowTemp.setRecVersion(ToolUtil.getIntIgnoreNull(menuAppointPara.getRecVersion()));
        return menuAppointShowTemp;
    }
}
