package task.service;

import skyline.util.ToolUtil;
import task.repository.dao.WorkorderAppointHisMapper;
import task.repository.dao.OperMapper;
//import task.repository.dao.not_mybatis.MyFlowHisMapper;
import org.springframework.stereotype.Service;
import task.repository.model.WorkorderAppointHis;
import task.repository.model.WorkorderAppointHisExample;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ÏÂÎç6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class WorkorderAppointHisService {
    @Resource
    private WorkorderAppointHisMapper workorderAppointHisMapper;
    @Resource
    private OperMapper operMapper;

    public List<WorkorderAppointHis> getListByModel(WorkorderAppointHis workorderAppointHisPara) {
        WorkorderAppointHisExample example= new WorkorderAppointHisExample();
        WorkorderAppointHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(workorderAppointHisPara.getInfoPkid()) + "%");
        if (!ToolUtil.getStrIgnoreNull(workorderAppointHisPara.getFinishFlag()).equals("")){
            criteria.andFinishFlagLike("%" + workorderAppointHisPara.getFinishFlag() + "%");
        }
        if (!ToolUtil.getStrIgnoreNull(workorderAppointHisPara.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+ workorderAppointHisPara.getCreatedBy()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(workorderAppointHisPara.getCreatedTime()).equals("")){
            criteria.andCreatedTimeLike("%"+ workorderAppointHisPara.getCreatedTime()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(workorderAppointHisPara.getRemark()).equals("")){
            criteria.andRemarkLike("%"+ workorderAppointHisPara.getRemark()+"%");
        }
        example.setOrderByClause("INFO_PKID ASC,CREATED_TIME ASC") ;
        return workorderAppointHisMapper.selectByExample(example);
    }
    /*
    public List<FlowHis> getSubStlListByFlowHis(String powerPkid,String periodNo){
        return myFlowHisMapper.getSubStlListByFlowHis(powerPkid,periodNo);
    }

    public List<FlowHis> selectListByModel(String strInfoType,String strInfoPkid) {
        FlowHisExample example= new FlowHisExample();
        FlowHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + strInfoType + "%")
                .andInfoPkidLike("%" + strInfoPkid + "%");
        return flowHisMapper.selectByExample(example);
    }*/

    public void insertRecord(WorkorderAppointHis workorderAppointHisPara) {
        String strOperatorPkidTemp= ToolUtil.getOperatorManager().getOperator().getPkid();
        String strOperatorNameTemp=ToolUtil.getOperatorManager().getOperator().getName();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        workorderAppointHisPara.setCreatedBy(strOperatorPkidTemp);
        workorderAppointHisPara.setCreatedByName(strOperatorNameTemp);
        workorderAppointHisPara.setCreatedTime(strLastUpdTimeTemp);
        workorderAppointHisMapper.insertSelective(workorderAppointHisPara);
    }
}
