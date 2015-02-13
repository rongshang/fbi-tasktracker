package task.service;

import skyline.util.ToolUtil;
import task.repository.dao.WorkorderItemMapper;
import task.repository.dao.not_mybatis.MyWorkorderItemMapper;
import task.repository.model.WorkorderItem;
import task.repository.model.WorkorderItemExample;
import task.repository.model.model_show.WorkorderInfoShow;
import task.repository.model.model_show.WorkorderItemShow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private MyWorkorderItemMapper myWorkOrderItemMapper;

    public Integer getMaxOrderidInEsCttItemList(String strBelongToType,
                                                 String strBelongToPkid,
                                                 String strParentPkid,
                                                 Integer intGrade){
        WorkorderItemExample example = new WorkorderItemExample();
        example.createCriteria()
                .andBelongToPkidEqualTo(strBelongToPkid)
                .andParentPkidEqualTo(strParentPkid)
                .andGradeEqualTo(intGrade);
        example .setOrderByClause("ORDERID DESC") ;
        List<WorkorderItem> workorderItemList = workorderItemMapper.selectByExample(example);
        if(workorderItemList.size() ==0){
            return 0;
        }
        else{
            return  workorderItemList.get(0).getOrderid();
        }
    }
    public List<WorkorderItem> getEsItemList(String strBelongToType,String strItemBelongToPkid){
        WorkorderItemExample example = new WorkorderItemExample();
        example.createCriteria().andBelongToPkidEqualTo(strItemBelongToPkid);
        example .setOrderByClause("GRADE ASC,ORDERID ASC") ;
        return workorderItemMapper.selectByExample(example);
        /*return commonMapper.selEsItemHieRelapListByTypeAndId(strBelongToType,strItemBelongToId);*/
    }

    /**
     * 判断记录是否已存在
     *
     * @param   strPkId
     * @return
     */
    public WorkorderItem getEsCttItemByPkId(String strPkId){
        return workorderItemMapper.selectByPrimaryKey(strPkId) ;
    }
    public boolean isExistSameNameNodeInDb(WorkorderItem workorderItem) {
        WorkorderItemExample example = new WorkorderItemExample();
        WorkorderItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andBelongToPkidEqualTo(workorderItem.getBelongToPkid())
                .andParentPkidEqualTo(workorderItem.getParentPkid())
                .andGradeEqualTo(workorderItem.getGrade());
        if(workorderItem.getName()==null){
            criteria.andNameIsNull();
        }
        else{
            criteria.andNameEqualTo(workorderItem.getName());
        }
        return workorderItemMapper.countByExample(example) >= 1;
    }
    public boolean isExistSameRecordInDb(WorkorderItem workorderItem) {
        WorkorderItemExample example = new WorkorderItemExample();
        WorkorderItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andBelongToPkidEqualTo(workorderItem.getBelongToPkid())
                .andParentPkidEqualTo(workorderItem.getParentPkid())
                .andGradeEqualTo(workorderItem.getGrade())
                .andOrderidEqualTo(workorderItem.getOrderid());

        if(workorderItem.getName()==null){
            criteria.andNameIsNull();
        }
        else{
            criteria.andNameEqualTo(workorderItem.getName());
        }
        return workorderItemMapper.countByExample(example) >= 1;
    }

    public void insertRecord(WorkorderItemShow workorderItemShowPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        WorkorderItem workorderItemTemp =fromModelShowToModel(workorderItemShowPara);
        workorderItemTemp.setArchivedFlag("0");
        workorderItemTemp.setOriginFlag("0");
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
        workorderItemTemp.setUnit(workorderItemShowPara.getUnit());
        workorderItemTemp.setPkid(workorderItemShowPara.getPkid()) ;
        workorderItemTemp.setBelongToPkid(workorderItemShowPara.getBelongToPkid()) ;
        workorderItemTemp.setParentPkid(workorderItemShowPara.getParentPkid()) ;
        workorderItemTemp.setGrade(workorderItemShowPara.getGrade()) ;
        workorderItemTemp.setOrderid(workorderItemShowPara.getOrderid()) ;
        workorderItemTemp.setName(workorderItemShowPara.getName()) ;
        workorderItemTemp.setUnitPrice(workorderItemShowPara.getUnitPrice());
        workorderItemTemp.setQuantity(workorderItemShowPara.getQuantity());
        workorderItemTemp.setAmount(workorderItemShowPara.getAmount());
        workorderItemTemp.setArchivedFlag(workorderItemShowPara.getArchivedFlag());
        workorderItemTemp.setOriginFlag(workorderItemShowPara.getOriginFlag());
        workorderItemTemp.setCreatedTime(workorderItemShowPara.getCreatedTime());
        workorderItemTemp.setCreatedBy(workorderItemShowPara.getCreatedBy());
        workorderItemTemp.setLastUpdTime(workorderItemShowPara.getLastUpdTime());
        workorderItemTemp.setLastUpdBy(workorderItemShowPara.getLastUpdBy());
        workorderItemTemp.setRemark(workorderItemShowPara.getRemark());
        workorderItemTemp.setRecVersion(workorderItemShowPara.getRecVersion());
        return workorderItemTemp;
    }
    /*层级关系到总包合同*/
    private WorkorderItemShow fromModelToModelShow(WorkorderItem workorderItemPara){
        WorkorderItemShow workorderItemShowTemp =new WorkorderItemShow() ;
        workorderItemShowTemp.setPkid(workorderItemPara.getPkid()) ;
        workorderItemShowTemp.setBelongToPkid(workorderItemPara.getBelongToPkid()) ;
        workorderItemShowTemp.setParentPkid(workorderItemPara.getParentPkid()) ;
        workorderItemShowTemp.setGrade(workorderItemPara.getGrade()) ;
        workorderItemShowTemp.setOrderid(workorderItemPara.getOrderid()) ;
        workorderItemShowTemp.setName(workorderItemPara.getName()) ;
        workorderItemShowTemp.setRemark(workorderItemPara.getRemark()) ;
        return workorderItemShowTemp;
    }

    public void updateRecord(WorkorderItemShow workorderItemShowPara) {
        WorkorderItem workorderItemTemp =fromModelShowToModel(workorderItemShowPara);
        workorderItemTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(workorderItemTemp.getRecVersion())+1);
        workorderItemTemp.setArchivedFlag("0");
        workorderItemTemp.setOriginFlag("0");
        workorderItemTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        workorderItemTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        workorderItemMapper.updateByPrimaryKey(workorderItemTemp) ;
    }

    public int deleteRecord(String strPkId){
        return workorderItemMapper.deleteByPrimaryKey(strPkId);
    }
    public int deleteRecord(WorkorderInfoShow workorderInfoShowPara){
        WorkorderItemExample example = new WorkorderItemExample();
        example.createCriteria()
                .andBelongToPkidEqualTo(workorderInfoShowPara.getPkid());
        return workorderItemMapper.deleteByExample(example);
    }

    public void setAfterThisOrderidPlusOneByNode(WorkorderItemShow workorderItemShowPara){
        myWorkOrderItemMapper.setAfterThisOrderidPlusOneByNode(
                workorderItemShowPara.getBelongToPkid(),
                workorderItemShowPara.getParentPkid(),
                workorderItemShowPara.getGrade(),
                workorderItemShowPara.getOrderid());
        insertRecord(workorderItemShowPara);
    }

    public void setAfterThisOrderidSubOneByNode(WorkorderItemShow workorderItemShowPara){
        deleteRecord(workorderItemShowPara.getPkid());
        myWorkOrderItemMapper.setAfterThisOrderidSubOneByNode(
                workorderItemShowPara.getBelongToPkid(),
                workorderItemShowPara.getParentPkid(),
                workorderItemShowPara.getGrade(),
                workorderItemShowPara.getOrderid());
    }
}
