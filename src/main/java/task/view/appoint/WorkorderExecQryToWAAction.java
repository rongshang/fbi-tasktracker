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
 * 工单指派
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
            logger.error("工单信息查询失败", e);
            MessageUtil.addError("工单信息查询失败");
        }
        return null;
    }

    public void onClickForMngAction(WorkorderAppointShow workorderAppointShowNewPara) {
        WorkorderAppoint workorderAppointInDB=new WorkorderAppoint();

        // 查询某一工单的 数据库中的 全部指派情况
        WorkorderAppointShow workorderAppointShowByInfoPkid=new WorkorderAppointShow();
        workorderAppointShowByInfoPkid.setInfoPkid(workorderAppointShowNewPara.getInfoPkid());
        List<WorkorderAppoint> workorderAppointListByInfoPkid=
                workorderAppointService.getWorkorderAppointListByModelShow(workorderAppointShowByInfoPkid);

        // 存在着的数据（更新或者插入）
        if(workorderAppointShowNewPara.getWorkorderAppointPkid()!=null) {
            workorderAppointInDB =
                    workorderAppointService.getWorkorderAppointByPkid(workorderAppointShowNewPara.getWorkorderAppointPkid());
            // 画面上的数据已经不存在了
            if (workorderAppointInDB == null) {
                MessageUtil.addError("该记录已经删除，您的页面数据已过时！");
                return;
            }

            //此条记录目前在数据库中的版本和在页面上的版本不一致
            int intRecVersionInDB= ToolUtil.getIntIgnoreNull(workorderAppointInDB.getRecVersion());
            int intRecVersionNew=ToolUtil.getIntIgnoreNull(workorderAppointShowNewPara.getRecVersion());
            if(intRecVersionInDB!=intRecVersionNew) {
                MessageUtil.addError("画面上的数据版本过时，请重新取得后再操作！");
                return;
            }

            // 删除操作
            if(workorderAppointShowNewPara.getSendTaskPartPkid()==null &&
                    workorderAppointShowNewPara.getRecvTaskPartPkid()==null){
                workorderAppointService.deleteRecord(workorderAppointShowNewPara.getWorkorderAppointPkid());
                MessageUtil.addInfo("删除数据成功。");
            }else if(workorderAppointShowNewPara.getSendTaskPartPkid()==null ||
                    workorderAppointShowNewPara.getRecvTaskPartPkid()==null){
                MessageUtil.addError("提出者和接收者必须成对出现,两者缺一不可！");
            }else{// 更新操作
                // 查询某一工单的 画面上的 指派 情况
                WorkorderAppointShow workorderAppointShowByInfoAppint=new WorkorderAppointShow();
                workorderAppointShowByInfoAppint.setInfoPkid(workorderAppointShowNewPara.getInfoPkid());
                workorderAppointShowByInfoAppint.setSendTaskPartPkid(workorderAppointShowNewPara.getSendTaskPartPkid());
                workorderAppointShowByInfoAppint.setRecvTaskPartPkid(workorderAppointShowNewPara.getRecvTaskPartPkid());
                workorderAppointShowByInfoAppint.setRemark(workorderAppointShowNewPara.getRemark());
                List<WorkorderAppoint> workorderAppointListByInfoAppint=
                        workorderAppointService.getWorkorderAppointListByModelShow(workorderAppointShowByInfoAppint);
                if(workorderAppointListByInfoAppint.size()>0){
                    MessageUtil.addError("您目前的工单已经如此指派过，请不要反复提交相同操作！");
                    return;
                }
                //更新
                workorderAppointService.updateRecord(workorderAppointShowNewPara);
                    MessageUtil.addInfo("更新数据成功。");
            }
        }else{// 不存在着的数据 插入操作
            if(ToolUtil.getStrIgnoreNull(workorderAppointShowNewPara.getSendTaskPartPkid()).equals("")){
                MessageUtil.addError("请选择发送者。。。");
                return;
            }else
            if(ToolUtil.getStrIgnoreNull(workorderAppointShowNewPara.getRecvTaskPartPkid()).equals("")){
                MessageUtil.addError("请选择接收者。。。");
                return;
            }
            if(workorderAppointListByInfoPkid.size()==0){ //该工单没有指派过
                workorderAppointShowNewPara.setFirstAppointFlag(EnumFirstAppointFlag.FIRST_APPOINT_FLAG0.getCode());
            }else{                                        //该工单指派过
                workorderAppointShowNewPara.setFirstAppointFlag(EnumFirstAppointFlag.FIRST_APPOINT_FLAG1.getCode());
            }
            workorderAppointShowNewPara.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG0.getCode());
            workorderAppointService.insertRecord(workorderAppointShowNewPara);
            MessageUtil.addInfo("新增数据完成。");
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
