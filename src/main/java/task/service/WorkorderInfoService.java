package task.service;

import task.repository.dao.WorkorderInfoMapper;
import task.repository.dao.not_mybatis.MyWorkorderInfoMapper;
import task.repository.dao.not_mybatis.MyDeptAndOperMapper;
import task.repository.model.*;
import task.repository.model.model_show.WorkorderInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skyline.util.ToolUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: 下午6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class WorkorderInfoService {
    @Autowired
    private WorkorderInfoMapper workorderInfoMapper;
    @Autowired
    private MyWorkorderInfoMapper myWorkorderInfoMapper;
    @Autowired
    private MyDeptAndOperMapper myDeptAndOperMapper;

    public String getUserName(String operPkidPara){
        return myDeptAndOperMapper.getUserName(ToolUtil.getStrIgnoreNull(operPkidPara));
    }

    public List<WorkorderInfo> getListByModelShow(WorkorderInfoShow workorderInfoShowPara) {
        WorkorderInfoExample example= new WorkorderInfoExample();
        WorkorderInfoExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
        if(!ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getCttType()).equals("")){
            criteria.andTypeEqualTo(ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getCttType()));
        }
        if(!ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getId()).equals("")){
            criteria.andIdEqualTo(ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getId()));
        }
        if(!ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getName()).equals("")){
            criteria.andNameEqualTo(ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getName()));
        }
        if(!ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getParentPkid()).equals("")){
            criteria.andParentPkidEqualTo(ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getParentPkid()));
        }
        if(!ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getSignDate()).equals("")){
            criteria.andSignDateLike(ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getSignDate()));
        }
        example.setOrderByClause("ID ASC") ;
        return workorderInfoMapper.selectByExample(example);
    }
    public List<WorkorderInfo> getListByModel(WorkorderInfo workorderInfoPara) {
        return getListByModelShow(fromModelToModelShow(workorderInfoPara));
    }

    public List<WorkorderInfoShow> getCttInfoListByCttType_Status(
            String strCttyTypePara,String strStatusPara) {
        return myWorkorderInfoMapper.getCttInfoListByCttType_Status(strCttyTypePara,strStatusPara);
    }

    public List<WorkorderInfoShow> getCttInfoListByCttType_ParentPkid_Status(
            String strCttyTypePara,
            String strParentPkidPara,
            String strStatusPara) {
        return myWorkorderInfoMapper.getCttInfoListByCttType_ParentPkid_Status(
                strCttyTypePara,
                strParentPkidPara,
                strStatusPara);
    }

    public List<WorkorderInfoShow> selectRecordsFromCtt(String parentPkidPara){
        return  myWorkorderInfoMapper.selectRecordsFromCtt(parentPkidPara);
    }

    public List<WorkorderInfo> getEsInitCttByCttTypeAndBelongToPkId(String strCttType,String strBelongToPkid) {
        WorkorderInfoExample example= new WorkorderInfoExample();
        WorkorderInfoExample.Criteria criteria = example.createCriteria();
        criteria.andParentPkidEqualTo(ToolUtil.getStrIgnoreNull(strBelongToPkid));
        example.setOrderByClause("ID ASC") ;
        return workorderInfoMapper.selectByExample(example);
    }

    public WorkorderInfo getCttInfoByPkId(String strPkid) {
        return workorderInfoMapper.selectByPrimaryKey(strPkid);
    }

    public boolean findChildRecordsByPkid(String strPkidPara) {
        WorkorderInfoExample example = new WorkorderInfoExample();
        WorkorderInfoExample.Criteria criteria = example.createCriteria();
        criteria.andParentPkidEqualTo(strPkidPara);
        return (workorderInfoMapper.selectByExample(example)).size()>0;
    }

    /*public FlowHis fromCttInfoToFlowCtrlHis(CttInfo cttInfoPara,String strOperTypePara){
        FlowCtrlHis flowCtrlHisTemp =new FlowCtrlHis();
        flowCtrlHisTemp.setInfoType(cttInfoPara.getCttType());
        flowCtrlHisTemp.setInfoPkid(cttInfoPara.getPkid());
        flowCtrlHisTemp.setInfoId(cttInfoPara.getId());
        flowCtrlHisTemp.setInfoName(cttInfoPara.getName());
        flowCtrlHisTemp.setFlowStatus(cttInfoPara.getFlowStatus());
        flowCtrlHisTemp.setFlowStatusReason(cttInfoPara.getFlowStatusReason());
        flowCtrlHisTemp.setFlowStatusRemark(cttInfoPara.getFlowStatusRemark());
        flowCtrlHisTemp.setCreatedBy(cttInfoPara.getCreatedBy());
        flowCtrlHisTemp.setCreatedByName(getUserName(cttInfoPara.getCreatedBy()));
        flowCtrlHisTemp.setCreatedTime(cttInfoPara.getCreatedTime());
        flowCtrlHisTemp.setRemark(cttInfoPara.getRemark());
        flowCtrlHisTemp.setOperType(strOperTypePara);
        return flowCtrlHisTemp;
    }*/

    @Transactional
    public void insertRecord(WorkorderInfoShow workorderInfoShowPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        WorkorderInfo workorderInfoTemp =fromModelShowToModel(workorderInfoShowPara);
        workorderInfoTemp.setArchivedFlag("0");
        workorderInfoTemp.setCreatedBy(strOperatorIdTemp);
        workorderInfoTemp.setCreatedTime(strLastUpdTimeTemp);
        workorderInfoTemp.setLastUpdBy(strOperatorIdTemp);
        workorderInfoTemp.setLastUpdTime(strLastUpdTimeTemp);
        workorderInfoMapper.insertSelective(workorderInfoTemp);
       /* flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoTemp,EnumOperType.OPER_TYPE0.getCode()));*/
    }
    @Transactional
    public void insertRecord(WorkorderInfo workorderInfoPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        workorderInfoPara.setArchivedFlag("0");
        workorderInfoPara.setCreatedBy(strOperatorIdTemp);
        workorderInfoPara.setCreatedTime(strLastUpdTimeTemp);
        workorderInfoPara.setLastUpdBy(strOperatorIdTemp);
        workorderInfoPara.setLastUpdTime(strLastUpdTimeTemp);
        workorderInfoMapper.insertSelective(workorderInfoPara);
        /*flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoPara,EnumOperType.OPER_TYPE0.getCode()));*/
    }
    @Transactional
    public String updateRecord(WorkorderInfoShow workorderInfoShowPara){
        // 为了防止异步操作数据
        return updateRecord(fromModelShowToModel(workorderInfoShowPara));
    }
    @Transactional
    public String updateRecord(WorkorderInfo workorderInfoPara){
        WorkorderInfo workorderInfoTemp =getCttInfoByPkId(workorderInfoPara.getPkid());
        if(workorderInfoTemp !=null){
            //此条记录目前在数据库中的版本
            int intRecVersionInDB=ToolUtil.getIntIgnoreNull(workorderInfoTemp.getRecVersion());
            int intRecVersion=ToolUtil.getIntIgnoreNull(workorderInfoPara.getRecVersion());
            if(intRecVersionInDB!=intRecVersion) {
                return "1";
            }
        }
        workorderInfoPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(workorderInfoPara.getRecVersion())+1);
        workorderInfoPara.setArchivedFlag("0");
        workorderInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        workorderInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        workorderInfoMapper.updateByPrimaryKey(workorderInfoPara);
        /*flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoPara,EnumOperType.OPER_TYPE1.getCode()));*/
        return "0";
    }
    @Transactional
    public int deleteRecord(String strCttInfoPkidPara){
        WorkorderInfo workorderInfoTemp = getCttInfoByPkId(strCttInfoPkidPara);
        /*flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoTemp,EnumOperType.OPER_TYPE1.getCode()));*/
        return workorderInfoMapper.deleteByPrimaryKey(strCttInfoPkidPara);
    }

    public String getStrMaxCttId(String strCttType){
        return myWorkorderInfoMapper.getStrMaxCttId(strCttType) ;
    }

    public WorkorderInfo fromModelShowToModel(WorkorderInfoShow workorderInfoShowPara) {
        WorkorderInfo workorderInfoTemp = new WorkorderInfo();
        workorderInfoTemp.setPkid(workorderInfoShowPara.getPkid());
        workorderInfoTemp.setType(workorderInfoShowPara.getCttType());
        workorderInfoTemp.setParentPkid(workorderInfoShowPara.getParentPkid());
        workorderInfoTemp.setId(workorderInfoShowPara.getId());
        workorderInfoTemp.setName(workorderInfoShowPara.getName());
        workorderInfoTemp.setStartTime(workorderInfoShowPara.getCttStartDate());
        workorderInfoTemp.setEndTime(workorderInfoShowPara.getCttEndDate());
        workorderInfoTemp.setSignDate(workorderInfoShowPara.getSignDate());
        workorderInfoTemp.setRemark(workorderInfoShowPara.getRemark());
        workorderInfoTemp.setAttachment(workorderInfoShowPara.getAttachment());
        workorderInfoTemp.setArchivedFlag(workorderInfoShowPara.getArchivedFlag());
        workorderInfoTemp.setCreatedBy(workorderInfoShowPara.getCreatedBy());
        workorderInfoTemp.setCreatedTime(workorderInfoShowPara.getCreatedTime());
        workorderInfoTemp.setLastUpdBy(workorderInfoShowPara.getLastUpdBy());
        workorderInfoTemp.setLastUpdTime(workorderInfoShowPara.getLastUpdTime());
        workorderInfoTemp.setRecVersion(workorderInfoShowPara.getRecVersion());
        workorderInfoTemp.setType(workorderInfoShowPara.getType());
        return workorderInfoTemp;
    }
    public WorkorderInfoShow fromModelToModelShow(WorkorderInfo workorderInfoPara) {
        WorkorderInfoShow workorderInfoShowTemp = new WorkorderInfoShow();
        workorderInfoShowTemp.setPkid(workorderInfoPara.getPkid());
        workorderInfoShowTemp.setCttType(workorderInfoPara.getType());
        workorderInfoShowTemp.setParentPkid(workorderInfoPara.getParentPkid());
        workorderInfoShowTemp.setId(workorderInfoPara.getId());
        workorderInfoShowTemp.setName(workorderInfoPara.getName());
        workorderInfoShowTemp.setCttStartDate(workorderInfoPara.getStartTime());
        workorderInfoShowTemp.setCttEndDate(workorderInfoPara.getEndTime());
        workorderInfoShowTemp.setSignDate(workorderInfoPara.getSignDate());
        workorderInfoShowTemp.setRemark(workorderInfoPara.getRemark());
        workorderInfoShowTemp.setAttachment(workorderInfoPara.getAttachment());
        workorderInfoShowTemp.setArchivedFlag(workorderInfoPara.getArchivedFlag());
        workorderInfoShowTemp.setCreatedBy(workorderInfoPara.getCreatedBy());
        workorderInfoShowTemp.setCreatedTime(workorderInfoPara.getCreatedTime());
        workorderInfoShowTemp.setLastUpdBy(workorderInfoPara.getLastUpdBy());
        workorderInfoShowTemp.setLastUpdTime(workorderInfoPara.getLastUpdTime());
        workorderInfoShowTemp.setRecVersion(workorderInfoPara.getRecVersion());
        workorderInfoShowTemp.setType(workorderInfoPara.getType());
        return workorderInfoShowTemp;
    }
    //更新甲供材情况
    public int updateByPKid(WorkorderInfo workorderInfoPara){
        return workorderInfoMapper.updateByPrimaryKey(workorderInfoPara);
    }

    public Integer getChildrenOfThisRecordInEsInitCtt(String strCttType,String strBelongToPkid){
        return myWorkorderInfoMapper.getChildrenOfThisRecordInEsInitCtt(strCttType,strBelongToPkid);
    }

    public List<WorkorderInfoShow> selectCttByStatusFlagBegin_End(WorkorderInfoShow workorderInfoShowPara){
        return myWorkorderInfoMapper.selectCttByStatusFlagBegin_End(workorderInfoShowPara);
    }
}
