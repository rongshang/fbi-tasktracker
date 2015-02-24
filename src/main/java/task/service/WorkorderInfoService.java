package task.service;

import org.apache.commons.lang.StringUtils;
import skyline.util.MessageUtil;
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
import java.util.ArrayList;
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
        if(ToolUtil.getStrIgnoreNull(operPkidPara).equals("")){
            return "";
        }else {
            return myDeptAndOperMapper.getUserName(ToolUtil.getStrIgnoreNull(operPkidPara));
        }
    }

    public List<WorkorderInfo> getWorkorderInfoListByModelShow(WorkorderInfoShow workorderInfoShowPara) {
        WorkorderInfoExample example= new WorkorderInfoExample();
        WorkorderInfoExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
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
    public List<WorkorderInfoShow> getWorkorderShowInfoListByModelShow(WorkorderInfoShow workorderInfoShowPara) {
        List<WorkorderInfo> workorderInfoListTemp=getWorkorderInfoListByModelShow(workorderInfoShowPara);
        List<WorkorderInfoShow> workorderInfoShowListTemp=new ArrayList<>();
        for(WorkorderInfo workorderInfoUnit:workorderInfoListTemp){
            workorderInfoShowListTemp.add(fromModelToModelShow(workorderInfoUnit));
        }
        return workorderInfoShowListTemp;
    }

    public WorkorderInfo getCttInfoByPkId(String strPkid) {
        return workorderInfoMapper.selectByPrimaryKey(strPkid);
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

    public String getMaxNoPlusOne() {
        Integer intTemp;
        String strMaxId = myWorkorderInfoMapper.getStrMaxCttId() ;
        if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
            strMaxId = "ORDER" + ToolUtil.getStrToday() + "0001";
        } else {
            String strTemp = strMaxId.substring(strMaxId.length() - 4).replaceFirst("^0+", "");
            if (ToolUtil.strIsDigit(strTemp)) {
                intTemp = Integer.parseInt(strTemp);
                intTemp = intTemp + 1;
                strMaxId = strMaxId.substring(0, strMaxId.length() - 4) + StringUtils.leftPad(intTemp.toString(), 4, "0");
            }
        }
        return strMaxId;
    }
    public WorkorderInfo fromModelShowToModel(WorkorderInfoShow workorderInfoShowPara) {
        WorkorderInfo workorderInfoTemp = new WorkorderInfo();
        workorderInfoTemp.setPkid(workorderInfoShowPara.getPkid());
        workorderInfoTemp.setParentPkid(workorderInfoShowPara.getParentPkid());
        workorderInfoTemp.setId(workorderInfoShowPara.getId());
        workorderInfoTemp.setType(workorderInfoShowPara.getType());
        workorderInfoTemp.setName(workorderInfoShowPara.getName());
        workorderInfoTemp.setStartTime(workorderInfoShowPara.getStartDate());
        workorderInfoTemp.setEndTime(workorderInfoShowPara.getEndDate());
        workorderInfoTemp.setSignDate(workorderInfoShowPara.getSignDate());
        workorderInfoTemp.setRemark(workorderInfoShowPara.getRemark());
        workorderInfoTemp.setAttachment(workorderInfoShowPara.getAttachment());
        workorderInfoTemp.setArchivedFlag(workorderInfoShowPara.getArchivedFlag());
        workorderInfoTemp.setCreatedBy(workorderInfoShowPara.getCreatedBy());
        workorderInfoTemp.setCreatedTime(workorderInfoShowPara.getCreatedTime());
        workorderInfoTemp.setLastUpdBy(workorderInfoShowPara.getLastUpdBy());
        workorderInfoTemp.setLastUpdTime(workorderInfoShowPara.getLastUpdTime());
        workorderInfoTemp.setRecVersion(workorderInfoShowPara.getRecVersion());
        return workorderInfoTemp;
    }
    public WorkorderInfoShow fromModelToModelShow(WorkorderInfo workorderInfoPara) {
        WorkorderInfoShow workorderInfoShowTemp = new WorkorderInfoShow();
        workorderInfoShowTemp.setPkid(workorderInfoPara.getPkid());
        workorderInfoShowTemp.setParentPkid(workorderInfoPara.getParentPkid());
        workorderInfoShowTemp.setId(workorderInfoPara.getId());
        workorderInfoShowTemp.setType(workorderInfoPara.getType());
        workorderInfoShowTemp.setName(workorderInfoPara.getName());
        workorderInfoShowTemp.setStartDate(workorderInfoPara.getStartTime());
        workorderInfoShowTemp.setEndDate(workorderInfoPara.getEndTime());
        workorderInfoShowTemp.setSignDate(workorderInfoPara.getSignDate());
        workorderInfoShowTemp.setRemark(workorderInfoPara.getRemark());
        workorderInfoShowTemp.setAttachment(workorderInfoPara.getAttachment());
        workorderInfoShowTemp.setArchivedFlag(workorderInfoPara.getArchivedFlag());
        workorderInfoShowTemp.setCreatedBy(workorderInfoPara.getCreatedBy());
        workorderInfoShowTemp.setCreatedByName(getUserName(workorderInfoPara.getCreatedBy()));
        workorderInfoShowTemp.setCreatedTime(workorderInfoPara.getCreatedTime());
        workorderInfoShowTemp.setLastUpdBy(workorderInfoPara.getLastUpdBy());
        workorderInfoShowTemp.setLastUpdByName(getUserName(workorderInfoPara.getLastUpdBy()));
        workorderInfoShowTemp.setLastUpdTime(workorderInfoPara.getLastUpdTime());
        workorderInfoShowTemp.setRecVersion(workorderInfoPara.getRecVersion());
        return workorderInfoShowTemp;
    }
}
