package task.view.appoint;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import task.common.enums.EnumArchivedFlag;
import task.common.enums.EnumFirstAppointFlag;
import task.common.enums.EnumInputFinishFlag;
import task.repository.model.WorkorderAppoint;
import task.repository.model.WorkorderInfo;
import task.repository.model.not_mybatis.DeptOperShow;
import task.repository.model.not_mybatis.WorkorderAppointShow;
import task.repository.model.not_mybatis.WorkorderInfoShow;
import task.service.WorkorderAppointService;
import task.service.WorkorderInfoService;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 * atuo: huzy
 * 工单指派
 */

@ManagedBean
@ViewScoped
public class WorkorderAppointAction {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderAppointAction.class);
    private WorkorderInfoShow workorderInfoShow;

    private WorkorderInfo workorderInfo;

    //workorderAppointOperMng.xhtml(工单指派页面)选中多条数据
    private WorkorderInfo[] selectedWorkorderInfo;

    //workorderAppointOperMng.xhtml(工单指派页面)显示用
    List<WorkorderInfo> workorderInfos = null;


    @ManagedProperty(value = "#{workorderAppointService}")
    private WorkorderAppointService workorderAppointService;

    @ManagedProperty(value = "#{workorderInfoService}")
    private WorkorderInfoService workorderInfoService;
    //页面上的树  hu 和 han  共用
    private TreeNode root;
    /*任务跟踪显示标志*/
    private String strTaskTrackerFlag;

    public void init(){
        strTaskTrackerFlag="true";
    }


    /**
     * atuo: huzy
     */
    @PostConstruct
    public void initTree() {
        workorderInfoShow = new WorkorderInfoShow();
        root = new DefaultTreeNode("Root",null);
        List<DeptOperShow> deptOperShowList = workorderAppointService.getDeptOper();
        DeptOperShow  deptOperShow = new DeptOperShow();
        deptOperShow.setDeptId("######");
        TreeNode node = null;
        for (DeptOperShow deptOperShowlist :deptOperShowList ){
            if(!deptOperShowlist.getDeptId().equals(deptOperShow.getDeptId())){
                node = new DefaultTreeNode(deptOperShowlist.getDeptName(), root);
                //用户的pkid
                new DefaultTreeNode(deptOperShowlist.getOperName()+"||"+deptOperShowlist.getPkid(),node);
            }else{
                //用户的pkid
                new DefaultTreeNode(deptOperShowlist.getOperName()+"||"+deptOperShowlist.getPkid(),node);
            }
            deptOperShow = new DeptOperShow();
            deptOperShow.setDeptId(deptOperShowlist.getDeptId());
        }
    }

    /***
     * atuo: huzy
     * 根据工单ID或者工单名获取该条件的工单信息
     * param:orderId(工单id),orderName(工单名)
     * @return List<WorkorderInfo>
     */
    public void getWorkorderInfoByIdOrName(){
        try{
            //1  录入完成
            workorderInfoShow.setFinishFlag(EnumArchivedFlag.ARCHIVED_FLAG1.getCode());
            //0 未删除
            workorderInfoShow.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
            workorderInfos = workorderInfoService.getWorkorderInfoListByModelShow(workorderInfoShow);
        }catch (Exception e){
            logger.info("WorkorderAssignAction类中的getWorkorderInfoByPkIdOrName异常:"+e.toString());
        }
    }

    /**
     * atuo: huzy
     * @return String 路径
     */
    public String getURL(){
        return "workorderAppointOperQry";
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

            strTaskTrackerFlag="false";
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
            logger.info("WorkorderAppointAction类中的selectRecordAction异常:"+e.toString());
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



    public List<WorkorderInfo> getWorkorderInfos() {
        return workorderInfos;
    }

    public void setWorkorderInfos(List<WorkorderInfo> workorderInfos) {
        this.workorderInfos = workorderInfos;
    }

    public void setWorkorderAppointService(WorkorderAppointService workorderAppointService) {
        this.workorderAppointService = workorderAppointService;
    }

    public WorkorderInfoService getWorkorderInfoService() {
        return workorderInfoService;
    }

    public void setWorkorderInfoService(WorkorderInfoService workorderInfoService) {
        this.workorderInfoService = workorderInfoService;
    }

    public WorkorderInfo[] getSelectedWorkorderInfo() {
        return selectedWorkorderInfo;
    }

    public void setSelectedWorkorderInfo(WorkorderInfo[] selectedWorkorderInfo) {
        this.selectedWorkorderInfo = selectedWorkorderInfo;
    }

    public String getStrTaskTrackerFlag() {
        return strTaskTrackerFlag;
    }

    public void setStrTaskTrackerFlag(String strTaskTrackerFlag) {
        this.strTaskTrackerFlag = strTaskTrackerFlag;
    }

    public WorkorderInfoShow getWorkorderInfoShow() {
        return workorderInfoShow;
    }

    public void setWorkorderInfoShow(WorkorderInfoShow workorderInfoShow) {
        this.workorderInfoShow = workorderInfoShow;
    }

    public WorkorderInfo getWorkorderInfo() {
        return workorderInfo;
    }

    public void setWorkorderInfo(WorkorderInfo workorderInfo) {
        this.workorderInfo = workorderInfo;
    }
}
