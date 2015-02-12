package task.service;

import task.common.enums.EnumResType;
import task.common.enums.EnumFlowStatus;
import task.common.enums.EnumFlowStatusReason;
import task.repository.dao.WorkorderInfoMapper;
import task.repository.dao.not_mybatis.MyTaskMapper;
import task.repository.model.model_show.TaskShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: 上午8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaskService {
    @Autowired
    private WorkorderInfoMapper workorderInfoMapper;
    private MyTaskMapper myTaskMapper;

    public List<TaskShow> getTaskFlowGroup() {
        return myTaskMapper.getTaskFlowGroup();
    }

    /*public List<TaskShow> getOwnTaskFlowGroup(String strOperPkidPara) {
        return myTaskMapper.getOwnTaskFlowGroup(strOperPkidPara);
    }

    public List<TaskShow> getDetailTodoTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getDetailTodoTaskShowList(strOperPkidPara);
    }

    public List<TaskShow> getRencentlyPowerDetailTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getRencentlyPowerDetailTaskShowList(strOperPkidPara);
    }*/

    public List<TaskShow> initRecentlyPowerTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //通过OperatorManager获取相应权限下菜单列表
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();
        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = new ArrayList<>();
        //detailTaskShowListTemp = getRencentlyPowerDetailTaskShowList(strOperPkidTemp);

        TaskShow taskShowTemp=new TaskShow();
        taskShowTemp.setOperResFlowStatusName("待录入(" + detailTaskShowListTemp.size() + ")");
        taskShowList.add(taskShowTemp);
        for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
            //分包价格结算的生成:分包数量结算和分包材料结算均复核后自动生成分包价格结算单
            if(EnumResType.RES_TYPE5.getCode().equals(detailTaskShowUnit.getType())){
                continue;
            }
            detailTaskShowUnit.setId(detailTaskShowUnit.getId());
            detailTaskShowUnit.setFlowStatusName("已授权");
            taskShowList.add(detailTaskShowUnit);
        }
        return taskShowList;
    }
    public List<TaskShow> initTodoTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //通过OperatorManager获取相应权限下菜单列表
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();
        // 以流程状态为分组
        List<TaskShow> ownTaskFlowGroupListTemp=new ArrayList<>();
        // ownTaskFlowGroupListTemp = getOwnTaskFlowGroup(strOperPkidTemp);
        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = new ArrayList<>();
        //detailTaskShowListTemp=getDetailTodoTaskShowList(strOperPkidTemp);

        for (TaskShow taskShowGroupUnit : ownTaskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    EnumFlowStatus.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            // 录入权限时
            if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(taskShowGroupUnit.getFlowStatus())){
                for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                    if(taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getOperResFlowStatus())) {
                        // 资源录入启动且权限分配为录入状态
                        if (detailTaskShowUnit.getFlowStatus() == null) {
                            intHasRecordCount++;
                            detailTaskShowUnit.setId(EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle() + "_" +
                                            ToolUtil.getStrIgnoreNull(detailTaskShowUnit.getId()));
                            if (detailTaskShowUnit.getFlowStatusReason() == null) {
                                detailTaskShowUnit.setFlowStatusReasonName(null);
                            } else {
                                detailTaskShowUnit.setFlowStatusReasonName(
                                        EnumFlowStatusReason.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                            }
                            taskShowList.add(detailTaskShowUnit);
                            if (detailTaskShowUnit.getFlowStatusReason() == null) {
                                detailTaskShowUnit.setStrColorType("1");
                            } else {
                                detailTaskShowUnit.setStrColorType("2");
                            }
                        }
                    }
                }
                taskShowGroupUnit.setOperResFlowStatusName(
                        taskShowGroupUnit.getOperResFlowStatusName()+"("+intHasRecordCount+")");
            }else {// 审复批记归
                for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                    if (taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getOperResFlowStatus())) {
                        if (detailTaskShowUnit.getFlowStatus() != null) {
                            if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(detailTaskShowUnit.getFlowStatus())) {
                                if (EnumResType.RES_TYPE3.getCode().equals(detailTaskShowUnit.getType()) ||
                                        EnumResType.RES_TYPE4.getCode().equals(detailTaskShowUnit.getType())) {
                                    continue;
                                }
                            }
                            if ((((Integer.parseInt(taskShowGroupUnit.getFlowStatus())) - 1) + "")
                                    .equals(detailTaskShowUnit.getFlowStatus())) {
                                intHasRecordCount++;
                                detailTaskShowUnit.setId(
                                        "(" + EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle() + ")" +
                                                ToolUtil.getStrIgnoreNull(detailTaskShowUnit.getId()));
                                detailTaskShowUnit.setFlowStatusReasonName(
                                        EnumFlowStatusReason.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                                taskShowList.add(detailTaskShowUnit);
                            }
                            //颜色区分
                            if ((int) (Integer.parseInt(detailTaskShowUnit.getFlowStatusReason()))
                                    > 2 * (int) (Integer.parseInt(detailTaskShowUnit.getFlowStatus()))) {
                                detailTaskShowUnit.setStrColorType("2");
                            } else if (
                                    (int) (Integer.parseInt(detailTaskShowUnit.getFlowStatus()) +
                                            (int) Integer.parseInt(detailTaskShowUnit.getFlowStatus())) - 1
                                            == (int) Integer.parseInt(detailTaskShowUnit.getFlowStatus())) {
                                detailTaskShowUnit.setStrColorType("1");
                            }
                        }
                    }
                }
                taskShowGroupUnit.setOperResFlowStatusName(
                        taskShowGroupUnit.getOperResFlowStatusName() + "(" + intHasRecordCount + ")");
            }
        }
        return taskShowList;
    }
}
