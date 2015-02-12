package task.service;

import task.repository.dao.OperMapper;
//import task.repository.dao.not_mybatis.MyFlowHisMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ����6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FlowCtrlHisService {
   /* @Resource
    private FlowCtrlHisMapper flowCtrlHisMapper;*/
   /* @Resource
    private MyFlowHisMapper myFlowHisMapper;*/
    @Resource
    private OperMapper operMapper;

    /*public List<FlowCtrlHis> selectListByModel(FlowCtrlHis flowCtrlHis) {
        FlowCtrlHisExample example= new FlowCtrlHisExample();
        FlowCtrlHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getInfoType()) + "%")
                .andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getInfoPkid()) + "%")
                .andPeriodNoLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getPeriodNo()) + "%");
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getFlowStatus()).equals("")){
            criteria.andFlowStatusLike("%"+ flowCtrlHis.getFlowStatus()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getFlowStatusReason()).equals("")){
            criteria.andFlowStatusReasonLike("%"+ flowCtrlHis.getFlowStatusReason()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+ flowCtrlHis.getCreatedBy()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getCreatedTime()).equals("")){
            criteria.andCreatedTimeLike("%"+ flowCtrlHis.getCreatedTime()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getRemark()).equals("")){
            criteria.andRemarkLike("%"+ flowCtrlHis.getRemark()+"%");
        }
        example.setOrderByClause("INFO_TYPE ASC,INFO_PKID ASC,PERIOD_NO ASC,FLOW_STATUS ASC,CREATED_TIME ASC") ;
        return flowCtrlHisMapper.selectByExample(example);
    }

    public List<FlowCtrlHis> getSubStlListByFlowCtrlHis(String powerPkid,String periodNo){
        return myFlowHisMapper.getSubStlListByFlowCtrlHis(powerPkid,periodNo);
    }

    public List<FlowCtrlHis> selectListByModel(String strInfoType,String strInfoPkid) {
        FlowCtrlHisExample example= new FlowCtrlHisExample();
        FlowCtrlHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + strInfoType + "%")
                .andInfoPkidLike("%" + strInfoPkid + "%");
        return flowCtrlHisMapper.selectByExample(example);
    }

    public void insertRecord(FlowCtrlHis flowCtrlHisPara) {
        String strOperatorPkidTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strOperatorNameTemp=ToolUtil.getOperatorManager().getOperator().getName();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        flowCtrlHisPara.setCreatedBy(strOperatorPkidTemp);
        flowCtrlHisPara.setCreatedByName(strOperatorNameTemp);
        flowCtrlHisPara.setCreatedTime(strLastUpdTimeTemp);
        flowCtrlHisPara.setArchivedFlag("0");
        flowCtrlHisMapper.insertSelective(flowCtrlHisPara);
    }*/

}
