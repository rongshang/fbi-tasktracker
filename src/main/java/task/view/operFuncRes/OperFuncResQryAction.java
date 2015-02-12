package task.view.operFuncRes;

import task.common.enums.EnumResType;
import task.common.enums.EnumFlowStatus;
import task.repository.model.Oper;
import task.service.DeptOperService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@ManagedBean
@ViewScoped
public class OperFuncResQryAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperFuncResQryAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    private List<SelectItem> taskFunctionList;

    @PostConstruct
    public void init() {
        initFunc();
    }

    private void initFunc(){
        taskFunctionList = new ArrayList<>();
        taskFunctionList.add(new SelectItem("","全部"));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS0.getCode(), EnumFlowStatus.FLOW_STATUS0.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS1.getCode(), EnumFlowStatus.FLOW_STATUS1.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS2.getCode(), EnumFlowStatus.FLOW_STATUS2.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS3.getCode(), EnumFlowStatus.FLOW_STATUS3.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS4.getCode(), EnumFlowStatus.FLOW_STATUS4.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS5.getCode(), EnumFlowStatus.FLOW_STATUS5.getTitle()));
    }

   /* public void selectRecordAction(OperResShow operResShowPara) {
        try {
            operResShowSel= (OperResShow) BeanUtils.cloneBean(operResShowPara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public void onQueryAction() {
        try {
            operResShowQryList.clear();
            if (!("".equals(ToolUtil.getStrIgnoreNull(operResShowQry.getInfoPkidName()).trim()))) {
                operResShowQry.setInfoPkidName("%"+operResShowQry.getInfoPkidName()+"%");
            }
            String strOperIdTemp=ToolUtil.getStrIgnoreNull(operResShowQry.getOperId()).trim();
            if (!("".equals(strOperIdTemp))) {
                List<Oper> operListTemp=deptOperService.getOperListByOperId(strOperIdTemp);
                if(operListTemp.size()>0) {
                    operResShowQry.setOperPkid(operListTemp.get(0).getPkid());
                }
            }
            List<OperResShow> operResShowQryTempList = operResService.selectOperaResRecordsByModelShow(operResShowQry);
            if (!(operResShowQryTempList.size() > 0)) {
                MessageUtil.addInfo("没有查询到数据。");
            } else {
                for (int i = 0; i < operResShowQryTempList.size(); i++) {
                    if(ToolUtil.getStrIgnoreNull(operResShowQryTempList.get(i).getFlowStatus()).length()>0) {
                        operResShowQryTempList.get(i).setFlowStatusName(
                                EnumFlowStatus.getValueByKey(operResShowQryTempList.get(i).getFlowStatus()).getTitle()
                        );
                    }
                    if(ToolUtil.getStrIgnoreNull(operResShowQryTempList.get(i).getInfoType()).length()>0) {
                        operResShowQryTempList.get(i).setInfoPkidName(
                                "(" + EnumResType.getValueByKey(operResShowQryTempList.get(i).getInfoType()).getTitle() + ")-"
                                        + operResShowQryTempList.get(i).getInfoPkidName()
                        );
                    }
                    operResShowQryList.add(operResShowQryTempList.get(i));
                }
            }
        } catch (Exception e) {
            logger.error("权限追加信息查询失败", e);
            MessageUtil.addError("权限追加信息查询失败");
        }
    }*/

    /*智能字段 Start*/

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public List<SelectItem> getTaskFunctionList() {
        return taskFunctionList;
    }

    public void setTaskFunctionList(List<SelectItem> taskFunctionList) {
        this.taskFunctionList = taskFunctionList;
    }
}
