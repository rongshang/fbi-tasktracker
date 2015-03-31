package task.view.appoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import task.common.UrlCtrl;
import task.common.enums.EnumFirstAppointFlag;
import task.common.enums.EnumRecvTaskContinueAppointFlag;
import task.common.enums.EnumRecvTaskExecFlag;
import task.repository.model.WorkorderAppoint;
import task.repository.model.not_mybatis.WorkorderAppointShow;
import task.service.DeptOperService;
import task.service.WorkorderAppointService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/2/13.
 * atuo: huzy
 * ����ָ��
 */

@ManagedBean
@ViewScoped
public class WorkorderExecQryToWAAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderExecQryToWAAction.class);
    @ManagedProperty(value = "#{workorderAppointService}")
    private WorkorderAppointService workorderAppointService;
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;

    private String strWorkorderInfoPkid;
    private String strWorkorderAppointPkid;
    private List<WorkorderAppointShow> workorderAppointShowList;

    @PostConstruct
    public void init(){
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if  (parammap.containsKey("strWorkorderInfoPkid")){
            strWorkorderInfoPkid=parammap.get("strWorkorderInfoPkid").toString();
        }
        if  (parammap.containsKey("strWorkorderAppointPkid")){
            strWorkorderAppointPkid=parammap.get("strWorkorderAppointPkid").toString();
        }

        workorderAppointShowList=new ArrayList<>();
        onQueryAction(strWorkorderInfoPkid,strWorkorderAppointPkid);
    }

    public String onQueryAction(String strWorkorderInfoPkidPara,String strWorkorderAppointPkidPara) {
        try {
            WorkorderAppointShow wAShow_CurrentAppointRecvContinueAppoint=new WorkorderAppointShow();
            List<WorkorderAppoint> wAList_CurrentAppointRecvContinueAppoint=new ArrayList<>();

            WorkorderAppointShow workorderAppointShowPara=new WorkorderAppointShow();
            workorderAppointShowPara.setInfoPkid(strWorkorderInfoPkidPara);
            workorderAppointShowPara.setWorkorderAppointPkid(strWorkorderAppointPkidPara);
            workorderAppointShowList =
                    workorderAppointService.getMyWorkorderAppointShowListByModelShow(workorderAppointShowPara);
            for(WorkorderAppointShow workorderAppointShowUnit:workorderAppointShowList){
                if(workorderAppointShowUnit.getRecvTaskExecFlag()!=null) {
                    workorderAppointShowUnit.setRecvTaskExecFlagName(
                            EnumRecvTaskExecFlag.getValueByKey(workorderAppointShowUnit.getRecvTaskExecFlag()).getTitle());
                }

                if(workorderAppointShowUnit.getRecvTaskPartPkid()!=null) {
                    wAShow_CurrentAppointRecvContinueAppoint.setInfoPkid(workorderAppointShowUnit.getInfoPkid());
                    wAShow_CurrentAppointRecvContinueAppoint.setSendTaskPartPkid(workorderAppointShowUnit.getRecvTaskPartPkid());
                    wAList_CurrentAppointRecvContinueAppoint =
                            workorderAppointService.getWorkorderAppointListByModelShow(wAShow_CurrentAppointRecvContinueAppoint);
                    if (wAList_CurrentAppointRecvContinueAppoint.size() == 0) {
                        workorderAppointShowUnit.setRecvTaskContinueFlag(
                                EnumRecvTaskContinueAppointFlag.RECV_TASK_CONTINUE_APPOINT_FLAG0.getCode());
                    } else if (wAList_CurrentAppointRecvContinueAppoint.size() > 0) {
                        workorderAppointShowUnit.setRecvTaskContinueFlag(
                                EnumRecvTaskContinueAppointFlag.RECV_TASK_CONTINUE_APPOINT_FLAG1.getCode());
                    }

                    if(workorderAppointShowUnit.getRecvTaskContinueFlag()!=null) {
                        workorderAppointShowUnit.setRecvTaskContinueName(
                                EnumRecvTaskContinueAppointFlag.getValueByKey(workorderAppointShowUnit.getRecvTaskContinueFlag()).getTitle());
                    }
                    if((workorderAppointShowUnit.getRecvTaskExecFlag()==null||
                            EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG0.getCode().equals(workorderAppointShowUnit.getRecvTaskExecFlag()))
                            &&
                            EnumRecvTaskContinueAppointFlag.RECV_TASK_CONTINUE_APPOINT_FLAG0.getCode().equals(
                                    workorderAppointShowUnit.getRecvTaskContinueFlag())
                            ){
                        workorderAppointShowUnit.setStrVisableCtrl("true");
                        workorderAppointShowUnit.setStrVisableCtrlNot("false");
                    }else{
                        workorderAppointShowUnit.setStrVisableCtrl("false");
                        workorderAppointShowUnit.setStrVisableCtrlNot("true");
                    }
                }else{
                    workorderAppointShowUnit.setStrVisableCtrl("true");
                    workorderAppointShowUnit.setStrVisableCtrlNot("false");
                }
            }
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
        return null;
    }

    public void onClickForMngAction(WorkorderAppointShow workorderAppointShowNewPara) {
        WorkorderAppoint workorderAppointInDB=new WorkorderAppoint();

        // ��ѯĳһ������ ���ݿ��е� ȫ��ָ�����
        WorkorderAppointShow workorderAppointShowByInfoPkid=new WorkorderAppointShow();
        workorderAppointShowByInfoPkid.setInfoPkid(workorderAppointShowNewPara.getInfoPkid());
        List<WorkorderAppoint> workorderAppointListByInfoPkid=
                workorderAppointService.getWorkorderAppointListByModelShow(workorderAppointShowByInfoPkid);

        // �����ŵ����ݣ����»��߲��룩
        if(workorderAppointShowNewPara.getWorkorderAppointPkid()!=null) {
            workorderAppointInDB =
                    workorderAppointService.getWorkorderAppointByPkid(workorderAppointShowNewPara.getWorkorderAppointPkid());
            // �����ϵ������Ѿ���������
            if (workorderAppointInDB == null) {
                MessageUtil.addError("�ü�¼�Ѿ�ɾ��������ҳ�������ѹ�ʱ��");
                return;
            }

            //������¼Ŀǰ�����ݿ��еİ汾����ҳ���ϵİ汾��һ��
            int intRecVersionInDB= ToolUtil.getIntIgnoreNull(workorderAppointInDB.getRecVersion());
            int intRecVersionNew=ToolUtil.getIntIgnoreNull(workorderAppointShowNewPara.getRecVersion());
            if(intRecVersionInDB!=intRecVersionNew) {
                MessageUtil.addError("�����ϵ����ݰ汾��ʱ��������ȡ�ú��ٲ�����");
                return;
            }

            // ɾ������
            if(workorderAppointShowNewPara.getSendTaskPartPkid()==null &&
                    workorderAppointShowNewPara.getRecvTaskPartPkid()==null){
                workorderAppointService.deleteRecord(workorderAppointShowNewPara.getWorkorderAppointPkid());
                MessageUtil.addInfo("ɾ�����ݳɹ���");
            }else if(workorderAppointShowNewPara.getSendTaskPartPkid()==null ||
                    workorderAppointShowNewPara.getRecvTaskPartPkid()==null){
                MessageUtil.addError("����ߺͽ����߱���ɶԳ���,����ȱһ���ɣ�");
            }else{// ���²���
                // ��ѯĳһ������ �����ϵ� ָ�� ���
                WorkorderAppointShow workorderAppointShowByInfoAppint=new WorkorderAppointShow();
                workorderAppointShowByInfoAppint.setInfoPkid(workorderAppointShowNewPara.getInfoPkid());
                workorderAppointShowByInfoAppint.setSendTaskPartPkid(workorderAppointShowNewPara.getSendTaskPartPkid());
                workorderAppointShowByInfoAppint.setRecvTaskPartPkid(workorderAppointShowNewPara.getRecvTaskPartPkid());
                workorderAppointShowByInfoAppint.setRemark(workorderAppointShowNewPara.getRemark());
                List<WorkorderAppoint> workorderAppointListByInfoAppint=
                        workorderAppointService.getWorkorderAppointListByModelShow(workorderAppointShowByInfoAppint);
                if(workorderAppointListByInfoAppint.size()>0){
                    MessageUtil.addError("��Ŀǰ�Ĺ����Ѿ����ָ�ɹ����벻Ҫ�����ύ��ͬ������");
                    return;
                }
                //����
                workorderAppointService.updateRecord(workorderAppointShowNewPara);
                    MessageUtil.addInfo("�������ݳɹ���");
            }
        }else{// �������ŵ����� �������
            if(ToolUtil.getStrIgnoreNull(workorderAppointShowNewPara.getSendTaskPartPkid()).equals("")){
                MessageUtil.addError("��ѡ�����ߡ�����");
                return;
            }else
            if(ToolUtil.getStrIgnoreNull(workorderAppointShowNewPara.getRecvTaskPartPkid()).equals("")){
                MessageUtil.addError("��ѡ������ߡ�����");
                return;
            }
            if(workorderAppointListByInfoPkid.size()==0){ //�ù���û��ָ�ɹ�
                workorderAppointShowNewPara.setFirstAppointFlag(EnumFirstAppointFlag.FIRST_APPOINT_FLAG0.getCode());
            }else{                                        //�ù���ָ�ɹ�
                workorderAppointShowNewPara.setFirstAppointFlag(EnumFirstAppointFlag.FIRST_APPOINT_FLAG1.getCode());
            }
            workorderAppointShowNewPara.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG0.getCode());
            workorderAppointService.insertRecord(workorderAppointShowNewPara);
            MessageUtil.addInfo("����������ɡ�");
        }
        onQueryAction(strWorkorderInfoPkid,strWorkorderAppointPkid);
    }

    public WorkorderAppointService getWorkorderAppointService() {
        return workorderAppointService;
    }

    public void setWorkorderAppointService(WorkorderAppointService workorderAppointService) {
        this.workorderAppointService = workorderAppointService;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public List<WorkorderAppointShow> getWorkorderAppointShowList() {
        return workorderAppointShowList;
    }

    public void setWorkorderAppointShowList(List<WorkorderAppointShow> workorderAppointShowList) {
        this.workorderAppointShowList = workorderAppointShowList;
    }

    public String getStrWorkorderInfoPkid() {
        return strWorkorderInfoPkid;
    }

    public void setStrWorkorderInfoPkid(String strWorkorderInfoPkid) {
        this.strWorkorderInfoPkid = strWorkorderInfoPkid;
    }

    public String getStrWorkorderAppointPkid() {
        return strWorkorderAppointPkid;
    }

    public void setStrWorkorderAppointPkid(String strWorkorderAppointPkid) {
        this.strWorkorderAppointPkid = strWorkorderAppointPkid;
    }
}
