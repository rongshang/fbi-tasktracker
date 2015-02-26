package task.service;

import skyline.util.ToolUtil;
import task.repository.dao.FlowHisMapper;
import task.repository.dao.OperMapper;
//import task.repository.dao.not_mybatis.MyFlowHisMapper;
import org.springframework.stereotype.Service;
import task.repository.model.FlowHis;
import task.repository.model.FlowHisExample;

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
public class FlowHisService {
    @Resource
    private FlowHisMapper flowHisMapper;
    @Resource
    private OperMapper operMapper;

    public List<FlowHis> getListByModel(FlowHis flowHisPara) {
        FlowHisExample example= new FlowHisExample();
        FlowHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(flowHisPara.getInfoPkid()) + "%");
        if (!ToolUtil.getStrIgnoreNull(flowHisPara.getFlowStatus()).equals("")){
            criteria.andFlowStatusLike("%"+ flowHisPara.getFlowStatus()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowHisPara.getFlowStatusReason()).equals("")){
            criteria.andFlowStatusReasonLike("%"+ flowHisPara.getFlowStatusReason()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowHisPara.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+ flowHisPara.getCreatedBy()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowHisPara.getCreatedTime()).equals("")){
            criteria.andCreatedTimeLike("%"+ flowHisPara.getCreatedTime()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowHisPara.getRemark()).equals("")){
            criteria.andRemarkLike("%"+ flowHisPara.getRemark()+"%");
        }
        example.setOrderByClause("INFO_PKID ASC,CREATED_TIME ASC") ;
        return flowHisMapper.selectByExample(example);
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

    public void insertRecord(FlowHis flowHisPara) {
        String strOperatorPkidTemp= ToolUtil.getOperatorManager().getOperator().getPkid();
        String strOperatorNameTemp=ToolUtil.getOperatorManager().getOperator().getName();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        flowHisPara.setCreatedBy(strOperatorPkidTemp);
        flowHisPara.setCreatedByName(strOperatorNameTemp);
        flowHisPara.setCreatedTime(strLastUpdTimeTemp);
        flowHisMapper.insertSelective(flowHisPara);
    }
}
