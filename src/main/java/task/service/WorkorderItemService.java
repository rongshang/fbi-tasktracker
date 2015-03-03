package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import skyline.util.ToolUtil;
import task.common.enums.EnumArchivedFlag;
import task.common.enums.EnumOriginFlag;
import task.repository.dao.OperMapper;
import task.repository.dao.WorkorderItemMapper;
import task.repository.dao.not_mybatis.MyDeptAndOperMapper;
import task.repository.dao.not_mybatis.MyWorkorderItemMapper;
import task.repository.model.WorkorderItem;
import task.repository.model.WorkorderItemExample;
import task.repository.model.not_mybatis.WorkorderItemShow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-19
 * Time: 下午9:44
 * To change this template use File | Settings | File Templates.
 */
@Service
public class WorkorderItemService {
    @Resource
    private WorkorderItemMapper workorderItemMapper;
    @Resource
    private MyWorkorderItemMapper myWorkorderItemMapper;
    @Autowired
    private OperMapper operMapper;

    public String getUserName(String operPkidPara){
        if(ToolUtil.getStrIgnoreNull(operPkidPara).equals("")){
            return "";
        }else {
            return operMapper.selectByPrimaryKey(operPkidPara).getName();
        }
    }

    public List<WorkorderItemShow> getWorkorderItemListByInfoPkid(String strWorkorderInfoPkidPara){
        WorkorderItemExample example = new WorkorderItemExample();
        example.createCriteria()
               .andInfoPkidEqualTo(strWorkorderInfoPkidPara);
        example.setOrderByClause("LEVELIDX ASC") ;
        List<WorkorderItem> workorderItemList = workorderItemMapper.selectByExample(example);
        List<WorkorderItemShow> workorderItemShowList=new ArrayList<>();
        for(WorkorderItem workorderItemUnit:workorderItemList){
            workorderItemShowList.add(fromModelToModelShow(workorderItemUnit));
        }
        return workorderItemShowList;
    }

    public Integer getMaxLevelidxPlusOne(String strWorkorderInfoPkidPara){
        return myWorkorderItemMapper.getMaxLevelidxPlusOne(strWorkorderInfoPkidPara);
    }

    public void insertRecord(WorkorderItemShow workorderItemShowPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        WorkorderItem workorderItemTemp =fromModelShowToModel(workorderItemShowPara);
        workorderItemTemp.setArchivedFlag("0");
        workorderItemTemp.setOriginFlag("I");
        workorderItemTemp.setCreatedBy(strOperatorIdTemp);
        workorderItemTemp.setCreatedTime(strLastUpdTimeTemp);
        workorderItemTemp.setLastUpdBy(strOperatorIdTemp);
        workorderItemTemp.setLastUpdTime(strLastUpdTimeTemp);
        workorderItemMapper.insertSelective(workorderItemTemp);
    }
    public void insertRecord(WorkorderItem workorderItemPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        workorderItemPara.setArchivedFlag("0");
        workorderItemPara.setOriginFlag("0");
        workorderItemPara.setCreatedBy(strOperatorIdTemp);
        workorderItemPara.setCreatedTime(strLastUpdTimeTemp);
        workorderItemPara.setLastUpdBy(strOperatorIdTemp);
        workorderItemPara.setLastUpdTime(strLastUpdTimeTemp);
        workorderItemMapper.insert(workorderItemPara);
    }

    /*总包合同到层级关系*/
    public WorkorderItem fromModelShowToModel(WorkorderItemShow workorderItemShowPara){
        WorkorderItem workorderItemTemp =new WorkorderItem() ;
        workorderItemTemp.setPkid(workorderItemShowPara.getPkid());
        workorderItemTemp.setId(workorderItemShowPara.getId());
        workorderItemTemp.setItemContent(workorderItemShowPara.getItemContent());
        workorderItemTemp.setArchivedFlag(workorderItemShowPara.getArchivedFlag());
        workorderItemTemp.setOriginFlag(workorderItemShowPara.getOriginFlag());
        workorderItemTemp.setCreatedBy(workorderItemShowPara.getCreatedBy());
        workorderItemTemp.setCreatedTime(workorderItemShowPara.getCreatedTime());
        workorderItemTemp.setLastUpdBy(workorderItemShowPara.getLastUpdBy());
        workorderItemTemp.setLastUpdTime(workorderItemShowPara.getLastUpdTime());
        workorderItemTemp.setRemark(workorderItemShowPara.getRemark());
        workorderItemTemp.setRecVersion(workorderItemShowPara.getRecVersion());
        workorderItemTemp.setParentPkid(workorderItemShowPara.getParentPkid());
        workorderItemTemp.setInfoPkid(workorderItemShowPara.getInfoPkid());
        workorderItemTemp.setLevelidx(Integer.parseInt(workorderItemShowPara.getLevelidx()));
        workorderItemTemp.setTid(workorderItemShowPara.getTid());
        return workorderItemTemp;
    }
    /*层级关系到总包合同*/
    private WorkorderItemShow fromModelToModelShow(WorkorderItem workorderItemPara){
        WorkorderItemShow workorderItemShowTemp =new WorkorderItemShow() ;
        workorderItemShowTemp.setPkid(workorderItemPara.getPkid());
        workorderItemShowTemp.setId(workorderItemPara.getId());
        workorderItemShowTemp.setItemContent(workorderItemPara.getItemContent());
        workorderItemShowTemp.setArchivedFlag(workorderItemPara.getArchivedFlag());
        workorderItemShowTemp.setArchivedFlagName(
             EnumArchivedFlag.getValueByKey(workorderItemPara.getArchivedFlag()).getTitle());
        workorderItemShowTemp.setOriginFlag(workorderItemPara.getOriginFlag());
        workorderItemShowTemp.setOriginFlagName(
               EnumOriginFlag.getValueByKey(workorderItemPara.getOriginFlag()).getTitle());
        workorderItemShowTemp.setCreatedBy(workorderItemPara.getCreatedBy());
        workorderItemShowTemp.setCreatedByName(getUserName(workorderItemPara.getCreatedBy()));
        workorderItemShowTemp.setCreatedTime(workorderItemPara.getCreatedTime());
        workorderItemShowTemp.setLastUpdBy(workorderItemPara.getLastUpdBy());
        workorderItemShowTemp.setLastUpdByName(getUserName(workorderItemPara.getLastUpdBy()));
        workorderItemShowTemp.setLastUpdTime(workorderItemPara.getLastUpdTime());
        workorderItemShowTemp.setRemark(workorderItemPara.getRemark());
        workorderItemShowTemp.setRecVersion(workorderItemPara.getRecVersion());
        workorderItemShowTemp.setParentPkid(workorderItemPara.getParentPkid());
        workorderItemShowTemp.setInfoPkid(workorderItemPara.getInfoPkid());
        workorderItemShowTemp.setLevelidx(workorderItemPara.getLevelidx().toString());
        workorderItemShowTemp.setTid(workorderItemPara.getTid());
        return workorderItemShowTemp;
    }

    public void updateRecord(WorkorderItemShow workorderItemShowPara) {
        WorkorderItem workorderItemTemp =fromModelShowToModel(workorderItemShowPara);
        workorderItemTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(workorderItemTemp.getRecVersion())+1);
        workorderItemTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        workorderItemTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        workorderItemMapper.updateByPrimaryKey(workorderItemTemp) ;
    }

    public int deleteRecordByPkid(String  strWorkorderItemPkidPara){
        return workorderItemMapper.deleteByPrimaryKey(strWorkorderItemPkidPara);
    }
    public int deleteRecordByInfoPkid(String  strWorkorderInfoPkidPara){
        WorkorderItemExample example = new WorkorderItemExample();
        example.createCriteria()
                .andInfoPkidEqualTo(strWorkorderInfoPkidPara);
        return workorderItemMapper.deleteByExample(example);
    }
}
