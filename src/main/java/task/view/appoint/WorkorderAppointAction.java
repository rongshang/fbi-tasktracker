package task.view.appoint;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import task.common.enums.EnumFirstAppointFlag;
import task.common.enums.EnumInputFinishFlag;
import task.repository.model.WorkorderAppoint;
import task.repository.model.WorkorderInfo;
import task.repository.model.not_mybatis.WorkorderAppointShow;
import task.repository.model.not_mybatis.WorkorderInfoShow;
import task.service.WorkorderAppointService;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 * atuo: huzy
 * ����ָ��
 */

@ManagedBean
@ViewScoped
public class WorkorderAppointAction {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderAppointAction.class);
    //����pkid
    private String orderId;
    //������
    private String orderName;
    //workorderAppointMng.xhtml(����ָ��ҳ��)ѡ�ж�������
    private WorkorderInfo[] selectedWorkorderInfo;

    //workorderAppointMng.xhtml(����ָ��ҳ��)��ʾ��
    List<WorkorderInfo> workorderInfos = null;


    @ManagedProperty(value = "#{workorderAppointService}")
    private WorkorderAppointService workorderAppointService;

    private TreeNode root;
    /*���������ʾ��־*/
    private String strTaskTrackerFlag;

    public void init(){
        strTaskTrackerFlag="true";
    }

    /***
     * ���ݹ���ID���߹�������ȡ�������Ĺ�����Ϣ
     * param:orderId(����id),orderName(������)
     * @return List<WorkorderInfo>
     */
    public void getWorkorderInfoByIdOrName(){
        try{
            //System.out.print("========1111111111=========");
            //workorderInfos = workorderAssignService.getWorkorderInfoByIdOrName(orderId,orderName);
        }catch (Exception e){
            logger.info("WorkorderAssignAction���е�getWorkorderInfoByPkIdOrName�쳣:"+e.toString());
        }
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
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
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
            logger.info("WorkorderAssignAction���е�selectRecordAction�쳣:"+e.toString());
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderPkId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
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
}
