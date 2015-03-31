package task.service;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skyline.util.ToolUtil;
import task.common.enums.EnumArchivedFlag;
import task.common.enums.EnumFirstAppointFlag;
import task.common.enums.EnumRecvTaskExecFlag;
import task.repository.dao.OperMapper;
import task.repository.dao.WorkorderAppointMapper;
import task.repository.dao.not_mybatis.MyWorkorderAppointMapper;
import task.repository.model.WorkorderAppoint;
import task.repository.model.WorkorderAppointExample;
import task.repository.model.not_mybatis.WorkorderAppointShow;
import task.repository.model.not_mybatis.WorkorderInfoShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 */
@Service
public class WorkorderAppointService {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderAppointService.class);
    @Autowired
    private WorkorderAppointMapper workorderAppointMapper;
    @Autowired
    private MyWorkorderAppointMapper myWorkorderAppointMapper;
    @Autowired
    private OperMapper operMapper;

    public String getUserName(String operPkidPara){
        if(ToolUtil.getStrIgnoreNull(operPkidPara).equals("")){
            return "";
        }else {
            return operMapper.selectByPrimaryKey(operPkidPara).getName();
        }
    }

    public List<WorkorderAppoint> getWorkorderAppointListByModelShow(WorkorderAppointShow workorderAppointShowPara) {
        WorkorderAppointExample example= new WorkorderAppointExample();
        WorkorderAppointExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
        // 指派Pkid
        if(!ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getWorkorderAppointPkid()).equals("")){
            criteria.andPkidEqualTo(workorderAppointShowPara.getWorkorderAppointPkid());
        }
        // 工单主题
        if(!ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getInfoPkid()).equals("")){
            criteria.andInfoPkidEqualTo(workorderAppointShowPara.getInfoPkid());
        }
        // 同工单第一次指派
        if(!ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getFirstAppointFlag()).equals("")){
            criteria.andFirstAppointFlagEqualTo(workorderAppointShowPara.getFirstAppointFlag());
        }
        // 接收者Pkid
        if(!ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getRecvTaskPartPkid()).equals("")){
            criteria.andRecvTaskPartPkidEqualTo(workorderAppointShowPara.getRecvTaskPartPkid());
        }
        // 发送给Pkid
        if(!ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getSendTaskPartPkid()).equals("")){
            criteria.andSendTaskPartPkidEqualTo(workorderAppointShowPara.getSendTaskPartPkid());
        }
        // 备注内容
        if(!ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getRemark()).equals("")){
            criteria.andRemarkLike("%"+ workorderAppointShowPara.getRemark()+"%");
        }
        example.setOrderByClause("CREATED_TIME ASC") ;
        return workorderAppointMapper.selectByExample(example);
    }

    public List<WorkorderAppoint> getWorkorderAppointListByModel(WorkorderAppoint workorderAppointPara) {
        return getWorkorderAppointListByModelShow(fromModelToModelShow(workorderAppointPara));
    }
    public List<WorkorderAppointShow> getWorkorderAppointShowListByModel(WorkorderAppoint workorderAppointPara) {
        List<WorkorderAppoint> workorderAppointListTemp=getWorkorderAppointListByModel(workorderAppointPara);
        List<WorkorderAppointShow> workorderAppointShowListTemp=new ArrayList<>();
        for(WorkorderAppoint workorderAppointUnit:workorderAppointListTemp){
            workorderAppointShowListTemp.add(fromModelToModelShow(workorderAppointUnit));
        }
        return workorderAppointShowListTemp;
    }
    public List<WorkorderAppointShow> getWorkorderAppointShowListByModelShow(
            WorkorderAppointShow workorderAppointShowPara) {
        List<WorkorderAppoint> workorderAppointListTemp=getWorkorderAppointListByModelShow(workorderAppointShowPara);
        List<WorkorderAppointShow> workorderAppointShowListTemp=new ArrayList<>();
        for(WorkorderAppoint workorderAppointUnit:workorderAppointListTemp){
            workorderAppointShowListTemp.add(fromModelToModelShow(workorderAppointUnit));
        }
        return workorderAppointShowListTemp;
    }

    public List<WorkorderAppointShow> getMyWorkorderAppointShowListByModelShow(
            WorkorderAppointShow workorderAppointShowPara) {
       return myWorkorderAppointMapper.getWorkorderAppointShowList(
               ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getInfoId()),
               ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getInfoName()),
               ToolUtil.getStrIgnoreNull(workorderAppointShowPara.getWorkorderAppointPkid()));
    }

    public WorkorderAppoint fromModelShowToModel(WorkorderAppointShow workorderAppointShowPara) {
        WorkorderAppoint workorderAppointTemp = new WorkorderAppoint();
        workorderAppointTemp.setPkid(workorderAppointShowPara.getWorkorderAppointPkid());
        workorderAppointTemp.setInfoPkid(workorderAppointShowPara.getInfoPkid());
        workorderAppointTemp.setSendTaskPartPkid(workorderAppointShowPara.getSendTaskPartPkid());
        workorderAppointTemp.setRecvTaskPartPkid(workorderAppointShowPara.getRecvTaskPartPkid());
        workorderAppointTemp.setRecvTaskExecFlag(workorderAppointShowPara.getRecvTaskExecFlag());
        workorderAppointTemp.setFirstAppointFlag(workorderAppointShowPara.getFirstAppointFlag());
        workorderAppointTemp.setArchivedFlag(workorderAppointShowPara.getArchivedFlag());
        workorderAppointTemp.setCreatedBy(workorderAppointShowPara.getCreatedBy());
        workorderAppointTemp.setCreatedTime(workorderAppointShowPara.getCreatedTime());
        workorderAppointTemp.setLastUpdBy(workorderAppointShowPara.getLastUpdBy());
        workorderAppointTemp.setLastUpdTime(workorderAppointShowPara.getLastUpdTime());
        workorderAppointTemp.setRemark(workorderAppointShowPara.getRemark());
        workorderAppointTemp.setRecVersion(workorderAppointShowPara.getRecVersion());
        workorderAppointTemp.setTid(workorderAppointShowPara.getTid());
        return workorderAppointTemp;
    }
    public WorkorderAppointShow fromModelToModelShow(WorkorderAppoint workorderAppointPara) {
        WorkorderAppointShow workorderAppointShowTemp = new WorkorderAppointShow();
        workorderAppointShowTemp.setWorkorderAppointPkid(workorderAppointPara.getPkid());
        workorderAppointShowTemp.setInfoPkid(workorderAppointPara.getInfoPkid());
        //workorderAppointShowTemp.setInfoName(workorderAppointPara.get());
        workorderAppointShowTemp.setSendTaskPartPkid(workorderAppointPara.getSendTaskPartPkid());
        workorderAppointShowTemp.setSendTaskPartName(getUserName(workorderAppointPara.getSendTaskPartPkid()));
        workorderAppointShowTemp.setRecvTaskPartPkid(workorderAppointPara.getRecvTaskPartPkid());
        workorderAppointShowTemp.setRecvTaskPartName(getUserName(workorderAppointPara.getRecvTaskPartPkid()));
        workorderAppointShowTemp.setRecvTaskExecFlag(workorderAppointPara.getRecvTaskExecFlag());
        workorderAppointShowTemp.setRecvTaskExecFlagName(
                EnumRecvTaskExecFlag.getValueByKey(workorderAppointPara.getRecvTaskExecFlag()).getTitle());
        workorderAppointShowTemp.setFirstAppointFlag(workorderAppointPara.getFirstAppointFlag());
        workorderAppointShowTemp.setFirstAppointFlagName(
                EnumFirstAppointFlag.getValueByKey(workorderAppointPara.getFirstAppointFlag()).getTitle());
        workorderAppointShowTemp.setArchivedFlag(workorderAppointPara.getArchivedFlag());
        workorderAppointShowTemp.setArchivedFlagName(
                EnumArchivedFlag.getValueByKey(workorderAppointPara.getArchivedFlag()).getTitle());
        workorderAppointShowTemp.setCreatedBy(workorderAppointPara.getCreatedBy());
        workorderAppointShowTemp.setCreatedByName(getUserName(workorderAppointPara.getCreatedBy()));
        workorderAppointShowTemp.setCreatedTime(workorderAppointPara.getCreatedTime());
        workorderAppointShowTemp.setLastUpdBy(workorderAppointPara.getLastUpdBy());
        workorderAppointShowTemp.setLastUpdByName(getUserName(workorderAppointPara.getLastUpdBy()));
        workorderAppointShowTemp.setLastUpdTime(workorderAppointPara.getLastUpdTime());
        workorderAppointShowTemp.setRemark(workorderAppointPara.getRemark());
        workorderAppointShowTemp.setRecVersion(workorderAppointPara.getRecVersion());
        workorderAppointShowTemp.setTid(workorderAppointPara.getTid());
        return workorderAppointShowTemp;
    }

    public TreeNode getWorkorderAppointShowTreeNode(WorkorderInfoShow workorderInfoShowPara){
        TreeNode workorderAppointShowRoot= new DefaultTreeNode(null, null);
        WorkorderAppointShow workorderAppointShowPara=new WorkorderAppointShow();
        workorderAppointShowPara.setInfoPkid(workorderInfoShowPara.getPkid());
        workorderAppointShowPara.setFirstAppointFlag(EnumFirstAppointFlag.FIRST_APPOINT_FLAG0.getCode());
        List<WorkorderAppointShow> workorderAppointShowListTemp=getWorkorderAppointShowListByModelShow(workorderAppointShowPara);
        if(workorderAppointShowListTemp!=null&&workorderAppointShowListTemp.size()>0) {
            WorkorderAppointShow workorderAppointShowTemp=workorderAppointShowListTemp.get(0);
            WorkorderAppointShow workorderAppointShow_TreeNode=new WorkorderAppointShow();
            workorderAppointShow_TreeNode.setRecvTaskPartPkid(workorderAppointShowTemp.getSendTaskPartPkid());
            workorderAppointShow_TreeNode.setRecvTaskPartName(workorderAppointShowTemp.getSendTaskPartName());
            workorderAppointShow_TreeNode.setStrTreeNodeContent(workorderAppointShow_TreeNode.getRecvTaskPartName());
            workorderAppointShow_TreeNode.setRecvTaskExecFlag(workorderAppointShowTemp.getRecvTaskExecFlag());
            workorderAppointShowRoot = new DefaultTreeNode(workorderAppointShow_TreeNode, null);
            workorderAppointShowRoot.setExpanded(true);
            recursiveTreeNode(workorderInfoShowPara.getPkid(),workorderAppointShow_TreeNode.getRecvTaskPartPkid(),workorderAppointShowRoot);
        }
        return workorderAppointShowRoot;
    }

    private void recursiveTreeNode(String strInfoPkidPara,String strSendTaskPartPkidPara,TreeNode parentNode){
        WorkorderAppointShow workorderAppointShowPara = new WorkorderAppointShow();
        workorderAppointShowPara.setInfoPkid(strInfoPkidPara);
        workorderAppointShowPara.setSendTaskPartPkid(strSendTaskPartPkidPara);
        List<WorkorderAppointShow> workorderAppointShowListTemp =
                getWorkorderAppointShowListByModelShow(workorderAppointShowPara);
        for (WorkorderAppointShow workorderAppointShowUnit : workorderAppointShowListTemp) {
            workorderAppointShowUnit.setStrTreeNodeContent(
                    workorderAppointShowUnit.getRecvTaskPartName()+"("+workorderAppointShowUnit.getRecvTaskExecFlagName()+")");
            TreeNode childNodeTemp = new DefaultTreeNode(workorderAppointShowUnit, parentNode);
            childNodeTemp.setExpanded(true);
            recursiveTreeNode(strInfoPkidPara,workorderAppointShowUnit.getRecvTaskPartPkid(),childNodeTemp);
        }
    }

    public WorkorderAppoint getWorkorderAppointByPkid(String pkid ){
        return  workorderAppointMapper.selectByPrimaryKey(pkid);
    }

    public void  updateBypkid(WorkorderAppoint workorderAppoint ){
        workorderAppointMapper.updateByPrimaryKey(workorderAppoint);
    }

    @Transactional
    public void insertRecord(WorkorderAppointShow workorderAppointShowPara) {
        insertRecord(fromModelShowToModel(workorderAppointShowPara));
    }
    @Transactional
    public void insertRecord(WorkorderAppoint workorderAppointPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        workorderAppointPara.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
        workorderAppointPara.setCreatedBy(strOperatorIdTemp);
        workorderAppointPara.setCreatedTime(strLastUpdTimeTemp);
        workorderAppointPara.setLastUpdBy(strOperatorIdTemp);
        workorderAppointPara.setLastUpdTime(strLastUpdTimeTemp);
        workorderAppointMapper.insertSelective(workorderAppointPara);
    }
    @Transactional
    public String updateRecord(WorkorderAppointShow workorderAppointShowPara){
        // 为了防止异步操作数据
        return updateRecord(fromModelShowToModel(workorderAppointShowPara));
    }
    @Transactional
    public String updateRecord(WorkorderAppoint workorderAppointPara){
        WorkorderAppoint workorderAppointTemp =
                workorderAppointMapper.selectByPrimaryKey(workorderAppointPara.getPkid());
        if(workorderAppointTemp !=null){
            //此条记录目前在数据库中的版本
            int intRecVersionInDB=ToolUtil.getIntIgnoreNull(workorderAppointTemp.getRecVersion());
            int intRecVersion=ToolUtil.getIntIgnoreNull(workorderAppointPara.getRecVersion());
            if(intRecVersionInDB!=intRecVersion) {
                return "1";
            }
        }
        workorderAppointPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(workorderAppointPara.getRecVersion())+1);
        workorderAppointPara.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
        workorderAppointPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        workorderAppointPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        workorderAppointMapper.updateByPrimaryKey(workorderAppointPara);
        return "0";
    }
    @Transactional
    public int deleteRecord(String strPkidPara){
        return workorderAppointMapper.deleteByPrimaryKey(strPkidPara);
    }
}
