package task.view.appoint;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import task.common.enums.EnumFirstAppointFlag;
import task.repository.model.WorkorderInfo;
import task.repository.model.not_mybatis.WorkorderAppointShow;
import task.repository.model.not_mybatis.WorkorderInfoShow;
import task.service.WorkorderAppointService;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 * atuo: huzy
 * 工单指派
 */

@ManagedBean
@ViewScoped
public class WorkorderTrackerAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderTrackerAction.class);
    @ManagedProperty(value = "#{workorderAppointService}")
    private WorkorderAppointService workorderAppointService;

    private TreeNode root;
    /*任务跟踪显示标志*/
    private String strTaskTrackerFlag;

    @PostConstruct
    public void init(){
        strTaskTrackerFlag="false";
    }

    public void initTree(WorkorderInfoShow workorderInfoShowPara) {
        WorkorderAppointShow workorderAppointShowPara=new WorkorderAppointShow();
        workorderAppointShowPara.setInfoPkid(workorderInfoShowPara.getPkid());
        workorderAppointShowPara.setFirstAppointFlag(EnumFirstAppointFlag.FIRST_APPOINT_FLAG0.getCode());
        List<WorkorderAppointShow> workorderAppointShowListTemp=
                workorderAppointService.getWorkorderAppointShowListByModelShow(workorderAppointShowPara);
        if(workorderAppointShowListTemp!=null&&workorderAppointShowListTemp.size()>0) {
            WorkorderAppointShow workorderAppointShowTemp=workorderAppointShowListTemp.get(0);
            WorkorderAppointShow workorderAppointShow_TreeNode=new WorkorderAppointShow();
            workorderAppointShow_TreeNode.setRecvTaskPartPkid(workorderAppointShowTemp.getSendTaskPartPkid());
            workorderAppointShow_TreeNode.setRecvTaskPartName(workorderAppointShowTemp.getSendTaskPartName());
            workorderAppointShow_TreeNode.setStrTreeNodeContent(workorderAppointShow_TreeNode.getRecvTaskPartName());
            root = new DefaultTreeNode(workorderAppointShow_TreeNode, null);
            root.setExpanded(true);
            recursiveTreeNode(workorderInfoShowPara.getPkid(),workorderAppointShow_TreeNode.getRecvTaskPartPkid(),root);

            strTaskTrackerFlag="true";
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveTreeNode(String strInfoPkidPara,String strSendTaskPartPkidPara,TreeNode parentNode){
        WorkorderAppointShow workorderAppointShowPara = new WorkorderAppointShow();
        workorderAppointShowPara.setInfoPkid(strInfoPkidPara);
        workorderAppointShowPara.setSendTaskPartPkid(strSendTaskPartPkidPara);
        List<WorkorderAppointShow> workorderAppointShowListTemp =
                workorderAppointService.getWorkorderAppointShowListByModelShow(workorderAppointShowPara);
        for (WorkorderAppointShow workorderAppointShowUnit : workorderAppointShowListTemp) {
            workorderAppointShowUnit.setStrTreeNodeContent(
                    workorderAppointShowUnit.getRecvTaskPartName()+"("+workorderAppointShowUnit.getRecvTaskFinishFlagName()+")");
            TreeNode childNodeTemp = new DefaultTreeNode(workorderAppointShowUnit, parentNode);
            childNodeTemp.setExpanded(true);
            recursiveTreeNode(strInfoPkidPara,workorderAppointShowUnit.getRecvTaskPartPkid(),childNodeTemp);
        }
    }

    public void selectRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            initTree(workorderInfoShowPara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
            logger.info("WorkorderAssignAction类中的selectRecordAction异常:"+e.toString());
        }
    }
    public void onNodeSelect(SelectEvent event) {
        TreeNode node = (TreeNode) event.getObject();

        //populate if not already loaded
        if(node.getChildren().isEmpty()) {
            //Object label = node.getLabel();


        }
    }

    public void onNodeDblselect(SelectEvent event) {
        //this.selectedNode = (TreeNode) event.getObject();
    }

    public TreeNode getRoot() {
        return root;
    }
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void setWorkorderAppointService(WorkorderAppointService workorderAppointService) {
        this.workorderAppointService = workorderAppointService;
    }

    public String getStrTaskTrackerFlag() {
        return strTaskTrackerFlag;
    }

    public void setStrTaskTrackerFlag(String strTaskTrackerFlag) {
        this.strTaskTrackerFlag = strTaskTrackerFlag;
    }
}
