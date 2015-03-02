package task.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
import task.common.enums.EnumArchivedFlag;
import task.common.enums.EnumFirstAppointFlag;
import task.common.enums.EnumTaskFinishFlag;
import task.repository.dao.WorkorderAppointMapper;
import task.repository.dao.WorkorderInfoMapper;
import task.repository.dao.not_mybatis.MyDeptAndOperMapper;
import task.repository.dao.not_mybatis.MyWorkorderInfoMapper;
import task.repository.model.WorkorderAppoint;
import task.repository.model.WorkorderAppointExample;
import task.repository.model.WorkorderInfo;
import task.repository.model.WorkorderInfoExample;
import task.repository.model.not_mybatis.DeptOperShow;
import task.repository.model.not_mybatis.WorkorderAppointShow;

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
    private MyWorkorderInfoMapper myWorkorderInfoMapper;
    @Autowired
    private MyDeptAndOperMapper myDeptAndOperMapper;
    @Autowired
    private WorkorderInfoMapper workorderInfoMapper;

    public String getUserName(String operPkidPara){
        if(ToolUtil.getStrIgnoreNull(operPkidPara).equals("")){
            return "";
        }else {
            return myDeptAndOperMapper.getUserName(ToolUtil.getStrIgnoreNull(operPkidPara));
        }
    }

    public List<WorkorderAppoint> getWorkorderAppointListByModelShow(WorkorderAppointShow workorderAppointShowPara) {
        WorkorderAppointExample example= new WorkorderAppointExample();
        WorkorderAppointExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
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
    public List<WorkorderAppointShow> getWorkorderAppointShowListByModelShow(WorkorderAppointShow workorderAppointShowPara) {
        List<WorkorderAppoint> workorderAppointListTemp=getWorkorderAppointListByModelShow(workorderAppointShowPara);
        List<WorkorderAppointShow> workorderAppointShowListTemp=new ArrayList<>();
        for(WorkorderAppoint workorderAppointUnit:workorderAppointListTemp){
            workorderAppointShowListTemp.add(fromModelToModelShow(workorderAppointUnit));
        }
        return workorderAppointShowListTemp;
    }

    public WorkorderAppoint fromModelShowToModel(WorkorderAppointShow workorderAppointShowPara) {
        WorkorderAppoint workorderAppointTemp = new WorkorderAppoint();
        workorderAppointTemp.setPkid(workorderAppointShowPara.getPkid());
        workorderAppointTemp.setInfoPkid(workorderAppointShowPara.getInfoPkid());
        workorderAppointTemp.setSendTaskPartPkid(workorderAppointShowPara.getSendTaskPartPkid());
        workorderAppointTemp.setRecvTaskPartPkid(workorderAppointShowPara.getRecvTaskPartPkid());
        workorderAppointTemp.setRecvTaskFinishFlag(workorderAppointShowPara.getRecvTaskFinishFlag());
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
        workorderAppointShowTemp.setPkid(workorderAppointPara.getPkid());
        workorderAppointShowTemp.setInfoPkid(workorderAppointPara.getInfoPkid());
        workorderAppointShowTemp.setInfoName(workorderAppointPara.getInfoPkid());
        workorderAppointShowTemp.setSendTaskPartPkid(workorderAppointPara.getSendTaskPartPkid());
        workorderAppointShowTemp.setSendTaskPartName(getUserName(workorderAppointPara.getSendTaskPartPkid()));
        workorderAppointShowTemp.setRecvTaskPartPkid(workorderAppointPara.getRecvTaskPartPkid());
        workorderAppointShowTemp.setRecvTaskPartName(getUserName(workorderAppointPara.getRecvTaskPartPkid()));
        workorderAppointShowTemp.setRecvTaskFinishFlag(workorderAppointPara.getRecvTaskFinishFlag());
        workorderAppointShowTemp.setRecvTaskFinishFlagName(
                EnumTaskFinishFlag.getValueByKey(workorderAppointPara.getRecvTaskFinishFlag()).getTitle());
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

    /***
     * atuo: huzy
     * 根据工单ID或者工单名获取该条件的工单信息
     * param:orderPkId(工单id),orderName(工单名)
     * @return List<WorkorderInfo>
     * huzy注释掉
     */
//    public List<WorkorderInfo> getWorkorderInfoByIdOrName(WorkorderInfo workorderInfo){
//        List<WorkorderInfo> workorderInfos = null;
//        try{
//            WorkorderInfoExample example = new WorkorderInfoExample();
//            example.createCriteria().andArchivedFlagEqualTo(EnumArchivedFlag.ARCHIVED_FLAG0.getCode()); //0 没有删除
//            example.createCriteria().andFinishFlagEqualTo(EnumArchivedFlag.ARCHIVED_FLAG1.getCode());//1 以录入完成
//            WorkorderInfoExample.Criteria criteria = example.createCriteria();
//            if(StringUtils.isNotBlank(workorderInfo.getId())){
//                criteria.andIdLike("%" + workorderInfo.getId() + "%");
//            }
//            if(StringUtils.isNotBlank(workorderInfo.getName())){
//                criteria.andNameLike("%" + workorderInfo.getName() + "%");
//            }
//            example.setOrderByClause("ID ASC") ;
//            workorderInfos = workorderInfoMapper.selectByExample(example);
//        }catch (Exception e){
//            logger.info("WorkorderAssignService类中的getWorkorderInfoByPkIdOrName异常:"+e.toString());
//        }
//        return workorderInfos;
//    }

    /***
     * atuo: huzy
     * 查询每个部门下有哪些人  页面中工单指派时用
     * @return List<DeptOperShow>
     */
    public List<DeptOperShow> getDeptOper(){
        List<DeptOperShow> deptOperShows = null;
        try{
            deptOperShows = myWorkorderInfoMapper.getDeptOper();
        }catch (Exception e){
            logger.info("WorkorderAssignService类中的getDeptOper异常:"+e.toString());
        }
        return deptOperShows;
    }


}
