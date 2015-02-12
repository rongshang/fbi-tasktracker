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
 * Time: ����8:50
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
        //ͨ��OperatorManager��ȡ��ӦȨ���²˵��б�
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();
        // �����ϸ�����б�
        List<TaskShow> detailTaskShowListTemp = new ArrayList<>();
        //detailTaskShowListTemp = getRencentlyPowerDetailTaskShowList(strOperPkidTemp);

        TaskShow taskShowTemp=new TaskShow();
        taskShowTemp.setOperResFlowStatusName("��¼��(" + detailTaskShowListTemp.size() + ")");
        taskShowList.add(taskShowTemp);
        for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
            //�ְ��۸���������:�ְ���������ͷְ����Ͻ�������˺��Զ����ɷְ��۸���㵥
            if(EnumResType.RES_TYPE5.getCode().equals(detailTaskShowUnit.getType())){
                continue;
            }
            detailTaskShowUnit.setId(detailTaskShowUnit.getId());
            detailTaskShowUnit.setFlowStatusName("����Ȩ");
            taskShowList.add(detailTaskShowUnit);
        }
        return taskShowList;
    }
    public List<TaskShow> initTodoTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //ͨ��OperatorManager��ȡ��ӦȨ���²˵��б�
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();
        // ������״̬Ϊ����
        List<TaskShow> ownTaskFlowGroupListTemp=new ArrayList<>();
        // ownTaskFlowGroupListTemp = getOwnTaskFlowGroup(strOperPkidTemp);
        // �����ϸ�����б�
        List<TaskShow> detailTaskShowListTemp = new ArrayList<>();
        //detailTaskShowListTemp=getDetailTodoTaskShowList(strOperPkidTemp);

        for (TaskShow taskShowGroupUnit : ownTaskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    EnumFlowStatus.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            // ¼��Ȩ��ʱ
            if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(taskShowGroupUnit.getFlowStatus())){
                for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                    if(taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getOperResFlowStatus())) {
                        // ��Դ¼��������Ȩ�޷���Ϊ¼��״̬
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
            }else {// �����ǹ�
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
                            //��ɫ����
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
